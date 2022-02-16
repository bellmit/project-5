package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.DrsConstants;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItemSource;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSourceSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionSvcImpl;
import com.kindminds.drs.v1.model.impl.shopify.ShopifyOrderReportOrderInfoImpl;
import com.kindminds.drs.v1.model.impl.shopify.ShopifyOrderReportOrderLineInfoImpl;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderReportOrderInfo;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderReportOrderLineInfo;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class ShopifyOrder extends ShopifyTransaction {


	private DrsTransactionDao dtRepo = (DrsTransactionDao) SpringAppCtx
			.get().getBean(DrsTransactionDao.class);

	private ShipmentSkuIdentificationDao skuIdentifyDao = (ShipmentSkuIdentificationDao)SpringAppCtx
			.get().getBean(ShipmentSkuIdentificationDao.class);


	public enum ShopifyAmountType{
		PRINCIPAL,
		SHIPPING_FEE,
		TAXES,
		SHOPIFY_FEE,
		TRUETOSOURCE_FEE,
		FBA_PER_UNIT,
		FBA_PER_ORDER,
		FBA_TRANSPORTATION
	}
	
	private final BigDecimal ssdcAllocationRate = new BigDecimal("0.712");
	private final BigDecimal msdcAllocationRate = new BigDecimal("0.288");
	private Date drsSettlementStart = null;
	private Date drsSettlementEnd = null;
	private Marketplace marketplace = null;


	public ShopifyOrder(int id, Date transactionDate, String type,
							   String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}


	public ShopifyOrder(int id, Date transactionDate, String type,
							   String source, String sourceId, String sku, String description) {

		super(id, transactionDate, type, source, sourceId, sku, description);
	}


	
	@Override
	public boolean needAllocationInfos(){ return true; }
	
	@Override
	public Integer getTotalQuantityPurchased(){
		return this.dao.queryShopifyOrderQuantity(super.getSourceId(),super.getSku());
	}

	@Override
	public List<Integer> process(Date periodStart, Date periodEnd) {

		this.marketplace=Marketplace.fromName(this.getSource());
		this.drsSettlementStart = periodStart;
		this.drsSettlementEnd = periodEnd;

		Object [] columns =this.dao.queryShopifyOrderReportOrderInfo(this.getSourceId());
		String name = (String)columns[0];
		String currency = (String)columns[1];
		BigDecimal subtotal = (BigDecimal)columns[2];
		BigDecimal shipping = (BigDecimal)columns[3];
		BigDecimal taxes = (BigDecimal)columns[4];
		BigDecimal total = (BigDecimal)columns[5];
		BigDecimal discountAmount = (BigDecimal)columns[6];
		ShopifyOrderReportOrderInfo orderInfo = new ShopifyOrderReportOrderInfoImpl(name,currency,subtotal,shipping,taxes,total,discountAmount);


		Object [] columns1 = this.dao.queryShopifyOrderReportOrderLineInfo(marketplace.getKey(),this.getSourceId(),
				this.getSku());

		String skuCode=(String)columns1[0];
		Integer quantity=(Integer)columns1[1];
		BigDecimal unitPrice=(BigDecimal)columns1[2];
		ShopifyOrderReportOrderLineInfo orderLineInfo = new ShopifyOrderReportOrderLineInfoImpl(skuCode,quantity,unitPrice);


		BigDecimal fbaTransportation=this.dao.queryAbsShopifyShipmentFeeTransportationFromAmazonSettlementReport(
				this.getSourceId());
		BigDecimal fbaPerOrderFee=this.dao.queryAbsShopifyPerOrderFeeFromAmazonSettlementReport(
				this.getSourceId());
		BigDecimal fbaPerUnitFee=this.dao.queryShopifyPerUnitFeeFromAmazonSettlementReport(
				marketplace.getKey(),this.getSku(),this.getSourceId()).abs();

		Map<ShopifyAmountType,BigDecimal> unitAmountGroup=this.createUnitAmountGroup(this.getSku(),
				orderInfo,orderLineInfo,fbaTransportation,fbaPerOrderFee,fbaPerUnitFee);
		Assert.isTrue(orderLineInfo.getLineItemQuantity()==this.getAllocationInfos().size(),"orderLineInfo.getLineItemQuantity()==order.getAllocationInfos().size()");

		List<Integer> resultIds = new ArrayList<>();
		for(int i=0;i<orderLineInfo.getLineItemQuantity();i++){

			int seq=i+1;

			this.doIvsSkuIdentification(i);
			resultIds.add(this.createDrsTransaction(seq,unitAmountGroup,this.getAllocationInfos().get(i)));
		}


		return resultIds;
	}

	private void doIvsSkuIdentification( int index){

		if(StringUtils.hasText(this.getAllocationInfos().get(index).getIvsSkuIdentificationStatus())){

			String remark = this.getAllocationInfos().get(index).getIvsSkuIdentificationRemark() + "/"+
					this.getAllocationInfos().get(index).getIvsSkuIdentificationDrsTranId()+"_"+
					this.getAllocationInfos().get(index).getIvsSkuIdentificationStatus();

			this.getAllocationInfos().get(index).setIvsSkuIdentificationRemark(remark);
		}

		this.getAllocationInfos().get(index).setIvsSkuIdentificationStatus("Sold");

	}


	private int createDrsTransaction(int seq,Map<ShopifyAmountType,BigDecimal> unitAmountGroup,
									 SkuShipmentAllocationInfo allocationInfo) {

		DrsTransactionSvcImpl dt = new DrsTransactionSvcImpl("Order",this.getTransactionDate(),seq,
				this.getSku(),1,
				this.marketplace,this.getSourceId(),allocationInfo.getIvsName(),allocationInfo.getUnsName());
		dt.setSettleableItemElements(this.createRetailOrderSettleableItemElements(dt,unitAmountGroup,allocationInfo));
		dt.setSettleableItemList(this.createRetailOrderSettleableItemList(dt,allocationInfo));

		int dtTranId = this.dtRepo.insert(dt);
		skuIdentifyDao.update(allocationInfo , dtTranId);

		return dtTranId;
	}


	private Map<ShopifyAmountType,BigDecimal> createUnitAmountGroup(
			String drsSku,
			ShopifyOrderReportOrderInfo orderInfo,
			ShopifyOrderReportOrderLineInfo orderLineInfo,
			BigDecimal fbaTransportation,
			BigDecimal fbaPerOrderFee,
			BigDecimal fbaPerUnitFee){
		BigDecimal unitTax = this.getUnitAmountByPrice(orderInfo.getSubtotal(), orderLineInfo.getUnitPrice(), orderInfo.getTaxes());
		BigDecimal unitDiscountAmount = this.getUnitDiscountAmount(orderInfo.getOrderName(),drsSku);
		BigDecimal unitPrice = orderLineInfo.getUnitPrice().subtract(unitDiscountAmount);
		BigDecimal shippingFee = this.getUnitAmountByPrice(orderInfo.getSubtotal(),orderLineInfo.getUnitPrice(),orderInfo.getShipping());
		BigDecimal trueToSourceFee = this.calculateTrueToSourceFee(drsSku,orderInfo.getCurrency(),unitPrice);
		BigDecimal unitFbaPerOrder = this.getUnitAmountByPrice(orderInfo.getSubtotal(),orderLineInfo.getUnitPrice(),fbaPerOrderFee);
		BigDecimal unitFbaPerUnitFee = this.getUnitAmountByPrice(orderInfo.getSubtotal(),orderLineInfo.getUnitPrice(),fbaTransportation);
		Map<ShopifyAmountType,BigDecimal> unitAmountGroup = new HashMap<>();
		unitAmountGroup.put(ShopifyAmountType.PRINCIPAL,unitPrice);
		unitAmountGroup.put(ShopifyAmountType.SHIPPING_FEE,shippingFee);
		unitAmountGroup.put(ShopifyAmountType.TAXES,unitTax);
		unitAmountGroup.put(ShopifyAmountType.TRUETOSOURCE_FEE,trueToSourceFee);
		unitAmountGroup.put(ShopifyAmountType.FBA_PER_UNIT,fbaPerUnitFee);
		if(unitFbaPerOrder!=null) unitAmountGroup.put(ShopifyAmountType.FBA_PER_ORDER,unitFbaPerOrder);
		if(unitFbaPerUnitFee!=null) unitAmountGroup.put(ShopifyAmountType.FBA_TRANSPORTATION,unitFbaPerUnitFee);
		for(ShopifyAmountType type:unitAmountGroup.keySet()) Assert.isTrue(unitAmountGroup.get(type).signum()>=0,"unitAmountGroup.get(type).signum()>=0");
		return unitAmountGroup;
	}

	private BigDecimal getUnitAmountByPrice(BigDecimal total,BigDecimal unitPrice,BigDecimal amountToDistribute){
		if(amountToDistribute==null) return null;
		return amountToDistribute.multiply(unitPrice).divide(total,6,RoundingMode.HALF_UP);
	}
	
	private BigDecimal getUnitDiscountAmount(String orderId, String drsSku){
		return this.dao.queryShopifyUnitDiscountAmount(orderId,drsSku);		
	}


	private DrsTransactionLineItemSource createRetailOrderSettleableItemElements(DrsTransaction dt,Map<ShopifyAmountType,BigDecimal> unitAmountGroup,SkuShipmentAllocationInfo allocateInfo) {
		String supplierKcode = this.dao.querySupplierKcode(dt.getProductSku());
		BigDecimal drsRetainmentRate = this.getDrsRetainmentRate(this.drsSettlementStart,this.drsSettlementEnd,supplierKcode);
		BigDecimal pretaxPrincipalPrice = unitAmountGroup.get(ShopifyAmountType.PRINCIPAL);
		BigDecimal msdc_ur = this.getMsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal umf = unitAmountGroup.get(ShopifyAmountType.TRUETOSOURCE_FEE);
		BigDecimal umfnr = null;
		BigDecimal shippingFee = unitAmountGroup.get(ShopifyAmountType.SHIPPING_FEE);
		Assert.isTrue(shippingFee.signum()>=0,"shippingFee.signum()>=0");
		BigDecimal uFba = this.getTotalFulfillmentFee(unitAmountGroup);
		BigDecimal uff = uFba.subtract(shippingFee);
		BigDecimal ssdc_ur = this.getSsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal exchangeRate = this.getExportExchangeRate(this.marketplace,allocateInfo);
		BigDecimal ss2spUipMarketCurrency = allocateInfo.getFcaPrice().multiply(exchangeRate).setScale(6, RoundingMode.HALF_UP);
		BigDecimal profitShare = this.getSs2spUnitProfitShareAddition(pretaxPrincipalPrice,msdc_ur,umf,umfnr,uff,ssdc_ur,ss2spUipMarketCurrency);
		return new DrsTransactionLineItemSourceSvcImpl(this.marketplace.getCurrency(),pretaxPrincipalPrice,msdc_ur,umf,umfnr,uff,ssdc_ur,ss2spUipMarketCurrency,profitShare,null);
	}
	
	private List<DrsTransactionLineItem> createRetailOrderSettleableItemList(DrsTransaction dt,SkuShipmentAllocationInfo allocationInfo) {
		String splrKcode = this.dao.queryCompanyKcodeBySku(dt.getProductSku());
		String ssdcKcode = this.dao.queryHandlerDrsKcode(splrKcode);
		String msdcKcode = this.dao.queryDrsCompanyKcodeByCountry(this.marketplace.getCountry().name());
		BigDecimal salesTaxRate = allocationInfo.getIvsSalesTaxRate();
		BigDecimal salesTax = allocationInfo.getFcaPrice().multiply(salesTaxRate);
		BigDecimal unitTotal = allocationInfo.getFcaPrice().add(salesTax);
		Assert.isTrue(allocationInfo.getDdpCurrency()==Currency.USD,"allocationInfo.getDdpCurrency()==Currency.USD");
		Assert.isTrue(dt.getSettleableItemElements().getCurrency()==dt.getMarketplace().getCurrency(),"dt.getSettleableItemElements().getCurrency()==dt.getMarketplace().getCurrency()");
		BigDecimal ss2sp_upsa = dt.getSettleableItemElements().getSpProfitShare();
		BigDecimal ms2ssUipInMarketSideCurrency = this.getMs2ssUnitInventoryPayment(dt.getSettleableItemElements());
		BigDecimal exchangeRate = this.getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(this.marketplace.getCurrency());
		BigDecimal ms2ssUipUsd = ms2ssUipInMarketSideCurrency.multiply(exchangeRate).setScale(6, RoundingMode.HALF_UP);
		BigDecimal ms2ssUpa = this.getMs2ssUnitPurchaseAlwnc(allocationInfo.getDdpAmount(),ms2ssUipUsd);
		int lineSeq=1;
		List<DrsTransactionLineItem> items = new ArrayList<DrsTransactionLineItem>();
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,ssdcKcode,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT.getName(),                            allocationInfo.getDdpCurrency().name(),allocationInfo.getDdpAmount()));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,ssdcKcode,splrKcode,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT.getName(),               allocationInfo.getFcaCurrency().name(),             unitTotal));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,ssdcKcode,splrKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION.getName(),         dt.getMarketplace().getCurrency().name(),            ss2sp_upsa));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,ssdcKcode,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES.getName(),allocationInfo.getDdpCurrency().name(),              ms2ssUpa));
		Assert.isTrue(items.size()>=1,"items.size()>=1");
		return items;
	}
	
	private BigDecimal calculateTrueToSourceFee(String sku,Currency currency,BigDecimal lineItemSubtotal){
		BigDecimal truetosourceFeeRate = this.dao.queryProductCategoryTrueToSourceFeeRate(sku);
		return truetosourceFeeRate.multiply(lineItemSubtotal).setScale(currency.getScale(),RoundingMode.HALF_UP);
	}
	
	private BigDecimal getDrsRetainmentRate(Date start,Date end,String supplierKcode) {
		BigDecimal actualDrsRetainmentRate = this.dao.queryRetainmentRate(start,end,this.marketplace.getCountry(),supplierKcode);
		Assert.notNull(actualDrsRetainmentRate,"actualDrsRetainmentRate null");
		return actualDrsRetainmentRate;
	}
	
	private BigDecimal getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(Currency src){
		return src==Currency.USD?BigDecimal.ONE:this.currencyRepo.queryExchangeRate(this.drsSettlementStart,this.drsSettlementEnd,src,Currency.USD,DrsConstants.interbankRateToUsdForActualMs2ssUnitInventoryPayment);
	}
	
	private BigDecimal getTotalFulfillmentFee(Map<ShopifyAmountType,BigDecimal> unitAmountGroup){
		if(unitAmountGroup.isEmpty()) return BigDecimal.ZERO;
		BigDecimal fee = BigDecimal.ZERO;
		if(unitAmountGroup.get(ShopifyAmountType.FBA_PER_UNIT)!=null) fee=fee.add(unitAmountGroup.get(ShopifyAmountType.FBA_PER_UNIT));
		if(unitAmountGroup.get(ShopifyAmountType.FBA_PER_ORDER)!=null) fee=fee.add(unitAmountGroup.get(ShopifyAmountType.FBA_PER_ORDER));
		if(unitAmountGroup.get(ShopifyAmountType.FBA_TRANSPORTATION)!=null) fee=fee.add(unitAmountGroup.get(ShopifyAmountType.FBA_TRANSPORTATION));
		return fee;
	}
	
	private BigDecimal getMs2ssUnitInventoryPayment(DrsTransactionLineItemSource elements) {
		BigDecimal ssdcRetainment = elements.getSsdcRetainment();
		BigDecimal fcaMsCurrency  = elements.getFcaInMarketsideCurrency();
		BigDecimal spProfitShare  = elements.getSpProfitShare();
		Assert.isTrue(elements.getCurrency()==this.marketplace.getCurrency(),"elements.getCurrency()==this.marketplace.getCurrency()");
		return ssdcRetainment.add(fcaMsCurrency).add(spProfitShare);
	}
	
	private BigDecimal getMs2ssUnitPurchaseAlwnc(BigDecimal ddpAmount,BigDecimal ms2ssUip) {
		Assert.isTrue(ddpAmount.signum()==1,"ddpAmount.signum()==1");
		return ms2ssUip.subtract(ddpAmount);
	}
	
	private BigDecimal getSs2spUnitProfitShareAddition(BigDecimal salePriceUntaxed, BigDecimal msdc_ur,BigDecimal umf,BigDecimal umfnr,BigDecimal uff,BigDecimal ssdc_ur,BigDecimal ss2spUipMarketCurrency) {
		BigDecimal localUmfnr = umfnr==null?BigDecimal.ZERO:new BigDecimal(umfnr.toPlainString());
		Assert.isTrue(uff.signum()>=0,"uff.signum()>=0");
		return salePriceUntaxed.subtract( msdc_ur.add(umf.abs()).add(localUmfnr.abs()).add(uff.abs()).add(ssdc_ur).add(ss2spUipMarketCurrency));
	}

	private BigDecimal getSsdcUnitRetainment(BigDecimal salePriceUntaxed,BigDecimal drsRetainmentRate) {
		BigDecimal ssdcRetainmentRate = this.ssdcAllocationRate.multiply(drsRetainmentRate).setScale(6, RoundingMode.HALF_UP);
		return salePriceUntaxed.multiply(ssdcRetainmentRate).setScale(6, RoundingMode.HALF_UP);
	}

	private BigDecimal getMsdcUnitRetainment(BigDecimal salePriceUntaxed,BigDecimal drsRetainmentRate) {
		BigDecimal msdcRetainmentRate = this.msdcAllocationRate.multiply(drsRetainmentRate).setScale(6, RoundingMode.HALF_UP);
		return salePriceUntaxed.multiply(msdcRetainmentRate).setScale(6, RoundingMode.HALF_UP);
	}
	
}
