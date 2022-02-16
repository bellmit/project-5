package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.*;
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

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class EbayOrder extends EBayTransaction {


	private DrsTransactionDao dtRepo = (DrsTransactionDao) SpringAppCtx
			.get().getBean(DrsTransactionDao.class);

	private ShipmentSkuIdentificationDao skuIdentifyDao = (ShipmentSkuIdentificationDao)SpringAppCtx
			.get().getBean(ShipmentSkuIdentificationDao.class);


	private Date drsSettlementStart;
	private Date drsSettlementEnd;

	private final BigDecimal ssdcAllocationRate = new BigDecimal("0.712");
	private final BigDecimal msdcAllocationRate = new BigDecimal("0.288");

	public EbayOrder(Integer id, Date transactionDate, String type,
						 String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}

	public EbayOrder(Integer id, Date transactionDate, String type,
						 String source, String sourceId, String sku, String description) {

		super(id, transactionDate, type, source, sourceId, sku, description);
	}

	public EbayOrder(Integer id, Date transactionDate, String type,
						String source, String sourceId, String sku,
						String description, String exceptionMsg, String stackTrace) {

		super(id, transactionDate, type, source, sourceId, sku, description, exceptionMsg, stackTrace);
	}

	@Override
	public boolean needAllocationInfos(){ return true; }
	
	@Override
	public Integer getTotalQuantityPurchased(){
		return 1;
	}

	@Override
	public List<Integer> process(Date periodStart, Date periodEnd) {

		this.drsSettlementStart = periodStart;
		this.drsSettlementEnd = periodEnd;
		Assert.isTrue(this.getAllocationInfos().size()==1,"transaction.getAllocationInfos().size()==1");

		this.doIvsSkuIdentification(0);
		SkuShipmentAllocationInfo allocationInfo=this.getAllocationInfos().get(0);
		Marketplace marketplace = Marketplace.fromKey(this.dao.queryEbayMarketplaceId(this.getSourceId()));
		String drsSku = this.getSku();//this.dao.queryEbayDrsSku(transaction.getSourceId());

		DrsTransactionSvcImpl dt = new DrsTransactionSvcImpl("Order",
				this.getTransactionDate(),1,drsSku,1,
				marketplace,this.getSourceId(),
				allocationInfo.getIvsName(),allocationInfo.getUnsName());

		dt.setSettleableItemElements(this.createRetailOrderSettleableItemElements(marketplace,dt,allocationInfo));
		dt.setSettleableItemList(this.createRetailOrderSettleableItemList(dt,allocationInfo));
		int dtId = this.dtRepo.insert(dt);
		skuIdentifyDao.update(allocationInfo,dtId);

		return Arrays.asList(dtId);
	}

	private void doIvsSkuIdentification(int index){

		if(StringUtils.hasText(this.getAllocationInfos().get(index).getIvsSkuIdentificationStatus())){

			String remark = this.getAllocationInfos().get(index).getIvsSkuIdentificationRemark() + "/"+
					this.getAllocationInfos().get(index).getIvsSkuIdentificationDrsTranId()+"_"+
					this.getAllocationInfos().get(index).getIvsSkuIdentificationStatus();

			this.getAllocationInfos().get(index).setIvsSkuIdentificationRemark(remark);
		}

		this.getAllocationInfos().get(index).setIvsSkuIdentificationStatus("Sold");

	}
	
	private DrsTransactionLineItemSource createRetailOrderSettleableItemElements(Marketplace marketplace, DrsTransaction dt, SkuShipmentAllocationInfo allocateInfo) {
		String supplierKcode = this.dao.querySupplierKcode(dt.getProductSku());
		BigDecimal drsRetainmentRate = this.getDrsRetainmentRate(this.drsSettlementStart, this.drsSettlementEnd,dt.getMarketplace().getCountry(),supplierKcode);
		BigDecimal pretaxPrincipalPrice = this.dao.queryEbayPretaxPrice(dt.getSourceTransactionId(), dt.getProductSku());
		BigDecimal msdc_ur = this.getMsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal umf = this.dao.queryEbayMarketplaceFee(dt.getSourceTransactionId(), dt.getProductSku());
		BigDecimal umfnr = BigDecimal.ZERO;
		BigDecimal uff = this.dao.queryEbayFulfillmentFee(dt.getSourceTransactionId(), dt.getProductSku());
		BigDecimal ssdc_ur = this.getSsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal exchangeRate = this.getExportExchangeRate(marketplace,allocateInfo);
		BigDecimal ss2spUipMarketCurrency = allocateInfo.getFcaPrice().multiply(exchangeRate).setScale(6, RoundingMode.HALF_UP);
		BigDecimal profitShare = this.getSs2spUnitProfitShareAddition(pretaxPrincipalPrice,msdc_ur,umf,umfnr,uff,ssdc_ur,ss2spUipMarketCurrency);
		return new DrsTransactionLineItemSourceSvcImpl(dt.getMarketplace().getCountry().getCurrency(),pretaxPrincipalPrice,msdc_ur,umf,umfnr,uff,ssdc_ur,ss2spUipMarketCurrency,profitShare, null);
	}

	private BigDecimal getDrsRetainmentRate(Date start, Date end, Country country, String supplierKcode) {
		BigDecimal actualDrsRetainmentRate = this.dao.queryRetainmentRate(start, end, country, supplierKcode);
		Assert.notNull(actualDrsRetainmentRate,"actualDrsRetainmentRate null");
		return actualDrsRetainmentRate;
	}
	
	private BigDecimal getMsdcUnitRetainment(BigDecimal salePriceUntaxed,BigDecimal drsRetainmentRate) {
		BigDecimal msdcRetainmentRate = this.msdcAllocationRate.multiply(drsRetainmentRate).setScale(6, RoundingMode.HALF_UP);
		return salePriceUntaxed.multiply(msdcRetainmentRate).setScale(6, RoundingMode.HALF_UP);
	}
	
	private BigDecimal getSsdcUnitRetainment(BigDecimal salePriceUntaxed,BigDecimal drsRetainmentRate) {
		BigDecimal ssdcRetainmentRate = this.ssdcAllocationRate.multiply(drsRetainmentRate).setScale(6, RoundingMode.HALF_UP);
		return salePriceUntaxed.multiply(ssdcRetainmentRate).setScale(6, RoundingMode.HALF_UP);
	}
	
	private BigDecimal getSs2spUnitProfitShareAddition(BigDecimal salePriceUntaxed,BigDecimal msdc_ur,BigDecimal umf,BigDecimal umfnr,BigDecimal uff,BigDecimal ssdc_ur,BigDecimal ss2spUipMarketCurrency) {
		BigDecimal localUmfnr = umfnr==null?BigDecimal.ZERO:new BigDecimal(umfnr.toPlainString()); 
		BigDecimal uffcharge = uff.compareTo(BigDecimal.ZERO) > 0?uff.abs():uff;
		return salePriceUntaxed.subtract( msdc_ur.add(umf.abs()).add(localUmfnr.abs()).add(uffcharge).add(ssdc_ur).add(ss2spUipMarketCurrency));
	}
	
	@SuppressWarnings("unchecked")
	private List<DrsTransactionLineItem> createRetailOrderSettleableItemList(DrsTransaction dt,SkuShipmentAllocationInfo allocationInfo) {
		String supplierKcode = this.dao.queryCompanyKcodeBySku(dt.getProductSku());
		String handlerDrsKcode = this.dao.queryHandlerDrsKcode(supplierKcode);
		String msdcKcode = this.dao.queryDrsCompanyKcodeByCountry(dt.getMarketplace().getCountry().name());
		BigDecimal salesTaxRate = allocationInfo.getIvsSalesTaxRate();
		BigDecimal salesTax = allocationInfo.getFcaPrice().multiply(salesTaxRate);
		BigDecimal unitTotal = allocationInfo.getFcaPrice().add(salesTax);
		Assert.isTrue(allocationInfo.getDdpCurrency()==Currency.USD,"allocationInfo.getDdpCurrency()==Currency.USD");
		Assert.isTrue(dt.getSettleableItemElements().getCurrency()==dt.getMarketplace().getCurrency(),"dt.getSettleableItemElements().getCurrency()==dt.getMarketplace().getCurrency()");
		BigDecimal ss2sp_upsa = dt.getSettleableItemElements().getSpProfitShare(); 
		BigDecimal ms2ssUipInMarketSideCurrency = this.getMs2ssUnitInventoryPayment(dt.getSettleableItemElements(),dt.getMarketplace().getCountry().getCurrency());
		BigDecimal exchangeRate = this.getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(dt.getMarketplace().getCountry().getCurrency());
		BigDecimal ms2ssUipUsd = ms2ssUipInMarketSideCurrency.multiply(exchangeRate).setScale(6, RoundingMode.HALF_UP);
		BigDecimal ms2ssUpa = this.getMs2ssUnitPurchaseAlwnc(allocationInfo.getDdpAmount(),ms2ssUipUsd);
		int lineSeq=1;
		List<DrsTransactionLineItemSvcImpl> items = new ArrayList<DrsTransactionLineItemSvcImpl>();
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,      msdcKcode,handlerDrsKcode, TransactionLineType.MS2SS_UNIT_DDP_PAYMENT.getName(),                            allocationInfo.getDdpCurrency().name(),allocationInfo.getDdpAmount()));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,handlerDrsKcode,  supplierKcode, TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT.getName(),               allocationInfo.getFcaCurrency().name(),                   unitTotal));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,handlerDrsKcode,  supplierKcode, TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION.getName(),         dt.getMarketplace().getCurrency().name(),                  ss2sp_upsa));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,      msdcKcode,handlerDrsKcode, TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES.getName(),allocationInfo.getDdpCurrency().name(),                    ms2ssUpa));
		Assert.isTrue(items.size()>=1,"items.size()>=1");
		return (List<DrsTransactionLineItem>)((List<?>)items);
	}
	
	private BigDecimal getMs2ssUnitInventoryPayment(DrsTransactionLineItemSource elements, Currency msCurrency) {
		BigDecimal ssdcRetainment = elements.getSsdcRetainment();
		BigDecimal fcaMsCurrency  = elements.getFcaInMarketsideCurrency();
		BigDecimal spProfitShare  = elements.getSpProfitShare();
		Assert.isTrue(elements.getCurrency()==msCurrency,"elements.getCurrency()==msCurrency");
		return ssdcRetainment.add(fcaMsCurrency).add(spProfitShare);
	}
	
	private BigDecimal getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(Currency src){
		return src==Currency.USD?BigDecimal.ONE:this.currencyRepo.queryExchangeRate(this.drsSettlementStart,this.drsSettlementEnd,src,Currency.USD, DrsConstants.interbankRateToUsdForActualMs2ssUnitInventoryPayment);
	}
	
	private BigDecimal getMs2ssUnitPurchaseAlwnc(BigDecimal ddpAmount,BigDecimal ms2ssUip) {
		Assert.isTrue(ddpAmount.signum()==1,"ddpAmount.signum()==1");
		return ms2ssUip.subtract(ddpAmount);
	}

}
