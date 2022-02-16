package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.*;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItemSource;
import com.kindminds.drs.api.v1.model.amazon.AmazonSettlementReportTransactionInfo;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSourceSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionSvcImpl;
import com.kindminds.drs.v1.model.impl.amazon.AmazonSettlementReportTransactionInfoImpl;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class FbaLostDamage extends AmazonTransaction {


	private DrsTransactionDao dtRepo = (DrsTransactionDao) SpringAppCtx
			.get().getBean(DrsTransactionDao.class);

	private ShipmentSkuIdentificationDao skuIdentifyDao = (ShipmentSkuIdentificationDao)SpringAppCtx
			.get().getBean(ShipmentSkuIdentificationDao.class);

	private final BigDecimal ssdcAllocationRate = new BigDecimal("0.712");
	private final BigDecimal msdcAllocationRate = new BigDecimal("0.288");
	private Date drsSettlementStart = null;
	private Date drsSettlementEnd = null;

	private Currency msCurrency=null;

	public FbaLostDamage(int id, Date transactionDate, String type,
									 String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}

	public FbaLostDamage(int id, Date transactionDate, String type,
									 String source, String sourceId, String sku, String description) {

		super(id, transactionDate, type, source, sourceId, sku, description);
	}

	
	@Override
	public boolean needAllocationInfos(){ return true; }
	
	@Override
	public Integer getTotalQuantityPurchased( ){
		Marketplace sourceMarketplace = Marketplace.fromName(super.getSource());
		List<Object []> columnsList = this.dao.queryAmazonSettlementReportTransactionInfo(super.getTransactionDate(),
				super.getType(),sourceMarketplace.getKey(),super.getSourceId(),"FBA Inventory Reimbursement",
				null,super.getSku());

        List<AmazonSettlementReportTransactionInfo> transactionInfos = new ArrayList<>();
        for(Object[] columns:columnsList){
            String currency = (String)columns[0];
            String resultAmountType = (String)columns[1];
            String resultAmountDescription = (String)columns[2];
            BigDecimal amount = (BigDecimal)columns[3];
            Integer quantityPurchased = (Integer)columns[4];
            Date postedDate = (Date) columns[5];
            transactionInfos.add(new AmazonSettlementReportTransactionInfoImpl(currency,resultAmountType,resultAmountDescription,amount,quantityPurchased,postedDate));
        }

		return this.getTotalQuantityPurchased(transactionInfos);
	}

	@Override
	public List<Integer> process(Date periodStart, Date periodEnd) {
		this.drsSettlementStart = periodStart;
		this.drsSettlementEnd = periodEnd;
		Marketplace sourceMarketplace = Marketplace.fromName(this.getSource());
		this.msCurrency = sourceMarketplace.getCurrency();

		List<Object []> columnsList = this.dao.queryAmazonSettlementReportTransactionInfo(this.getTransactionDate(),
				this.getType(),sourceMarketplace.getKey(),this.getSourceId(),"FBA Inventory Reimbursement",null,
				this.getSku());
		List<AmazonSettlementReportTransactionInfo> transactionInfos = new ArrayList<>();
		for(Object[] columns:columnsList){
			String currency = (String)columns[0];
			String resultAmountType = (String)columns[1];
			String resultAmountDescription = (String)columns[2];
			BigDecimal amount = (BigDecimal)columns[3];
			Integer quantityPurchased = (Integer)columns[4];
			Date postedDate = (Date) columns[5];
			transactionInfos.add(new AmazonSettlementReportTransactionInfoImpl(currency,resultAmountType,
					resultAmountDescription,amount,quantityPurchased,postedDate));
		}

		Assert.isTrue(transactionInfos.size()==1,"transactionInfos.size()==1");
		int totalQuantityPurchased = this.getTotalQuantityPurchased(transactionInfos);
		Map<AmzAmountTypeDesc,BigDecimal> totalAmountGroup = this.createTotalAbsAmountGroup(transactionInfos);
		Assert.isTrue(!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_COMMISSION),"!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_COMMISSION)");
		Assert.isTrue(!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_TRANSACTION),"!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_TRANSACTION)");
		Assert.isTrue(!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_SHIPPINGHB),"!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_SHIPPINGHB)");
		Assert.isTrue(!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_SALESTAXSERVICE),"!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_SALESTAXSERVICE)");
		Assert.isTrue(!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_PERUNIT),"!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_PERUNIT)");
		Assert.isTrue(!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_FBA_WEIGHTBASED),"!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_FBA_WEIGHTBASED)");
		Assert.isTrue(!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_PERORDER),"!totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_PERORDER)");
		Assert.isTrue(!totalAmountGroup.containsKey(AmzAmountTypeDesc.SHIPMENTFEES_TRANSPORTATION),"!totalAmountGroup.containsKey(AmzAmountTypeDesc.SHIPMENTFEES_TRANSPORTATION)");
		Assert.isTrue(
				totalAmountGroup.containsKey(AmzAmountTypeDesc.FBA_LOST)||
						totalAmountGroup.containsKey(AmzAmountTypeDesc.FBA_DAMAGE)||
						totalAmountGroup.containsKey(AmzAmountTypeDesc.FBA_MISSING_FROM_INBOUND)||
						totalAmountGroup.containsKey(AmzAmountTypeDesc.FBA_LOST_MANUAL)||
						totalAmountGroup.containsKey(AmzAmountTypeDesc.FBA_REMOVAL_ORDER_LOST)||
						totalAmountGroup.containsKey(AmzAmountTypeDesc.FREE_REPLACEMENT_REFUND_ITEMS),"contain FBA_LOST || FBA_DAMAGE || FBA_MISSING_FROM_INBOUND || FREE_REPLACEMENT_REFUND_ITEMS");
		List<Map<AmzAmountTypeDesc,BigDecimal>> unitAmountGroups = this.createUnitAmountGroups(totalQuantityPurchased,totalAmountGroup);
		Assert.isTrue(this.getAllocationInfos().size()==totalQuantityPurchased,"tx.getAllocationInfos().size()==totalQuantityPurchased");

		return this.processTransactionInOrderStyle(periodStart,periodEnd,sourceMarketplace, totalQuantityPurchased,unitAmountGroups);
	}


	private int getTotalQuantityPurchased(List<AmazonSettlementReportTransactionInfo> transactionInfos) {
		int totalQuantity = 0;
		for(AmazonSettlementReportTransactionInfo info:transactionInfos){
			totalQuantity+=info.getQuantityPurchased();
		}
		return totalQuantity;
	}
	
	private Map<AmzAmountTypeDesc,BigDecimal> createTotalAbsAmountGroup(List<AmazonSettlementReportTransactionInfo> transactionInfos){
		Map<AmzAmountTypeDesc,BigDecimal> totalAmount = new HashMap<>();
		for(AmazonSettlementReportTransactionInfo info:transactionInfos){
			AmzAmountTypeDesc typeDesc = AmzAmountTypeDesc.fromValue(info.getAmountType(),info.getAmountDescription());
			BigDecimal absAmount = info.getAmount().abs();
//			System.out.println("typeDesc: " + typeDesc);
//			System.out.println("absAmount: " + absAmount);
			if(!totalAmount.containsKey(typeDesc)) totalAmount.put(typeDesc,BigDecimal.ZERO);
			totalAmount.put(typeDesc,totalAmount.get(typeDesc).add(absAmount));
		}
		return totalAmount;
	}
	
	private List<Map<AmzAmountTypeDesc,BigDecimal>> createUnitAmountGroups(int totalQuantityPurchased,Map<AmzAmountTypeDesc,BigDecimal> totalAmounts){
		Map<AmzAmountTypeDesc,BigDecimal> unitAmount = this.createHalfDownUnitAmountGroup(totalQuantityPurchased, totalAmounts);
		List<Map<AmzAmountTypeDesc,BigDecimal>> unitAmounts = new ArrayList<>();
		for(int i=0;i<totalQuantityPurchased;i++){
			if(i<totalQuantityPurchased-1){
				unitAmounts.add(unitAmount);
				for(AmzAmountTypeDesc typeDesc:totalAmounts.keySet()){
					totalAmounts.put(typeDesc,totalAmounts.get(typeDesc).subtract(unitAmount.get(typeDesc)));
				}
			}
			else{
				unitAmounts.add(totalAmounts);
			}
		}
		return unitAmounts;
	}
	
	private Map<AmzAmountTypeDesc,BigDecimal> createHalfDownUnitAmountGroup(int totalQuantityPurchased,Map<AmzAmountTypeDesc,BigDecimal> totalAmountGroup){
		if(totalQuantityPurchased==1) return totalAmountGroup;
		BigDecimal quantity = new BigDecimal(totalQuantityPurchased);
		Map<AmzAmountTypeDesc,BigDecimal> unitAmounts = new HashMap<>();
		for(AmzAmountTypeDesc typeDesc:totalAmountGroup.keySet()){
			unitAmounts.put(typeDesc,totalAmountGroup.get(typeDesc).divide(quantity,RoundingMode.DOWN));
		}
		return unitAmounts;
	}

	private List<Integer> processTransactionInOrderStyle(Date periodStart, Date periodEnd,
														 Marketplace marketplace, int totalQuantityPurchased,
														 List<Map<AmzAmountTypeDesc,BigDecimal>> unitAmountGroups
	) {
		List<Integer> resultIds = new ArrayList<>();
		for(int i=0;i<totalQuantityPurchased;i++){
			int sequence = i+1;
			Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup = unitAmountGroups.get(i);

			this.doIvsSkuIdentification(i);

			SkuShipmentAllocationInfo allocationInfo = this.getAllocationInfos().get(i);

			resultIds.add(this.createDrsTransactionOrderStyle(periodStart, periodEnd,marketplace,
					sequence, unitAmountGroup , allocationInfo));
		}
		return resultIds;
	}

	private int createDrsTransactionOrderStyle(Date peroidStart, Date periodEnd, Marketplace marketplace,
											   int sequence, Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup
			, SkuShipmentAllocationInfo allocationInfo  ){

		String sku = this.getSku();
		DrsTransactionSvcImpl dt =
				new DrsTransactionSvcImpl(this.getType(),this.getTransactionDate(),
						sequence,this.getSku(),1,marketplace,this.getSourceId(),
						allocationInfo.getIvsName(),allocationInfo.getUnsName());

		dt.setSettleableItemElements(this.createRetailOrderSettleableItemElements(marketplace,sku,unitAmountGroup,allocationInfo));
		dt.setSettleableItemList(this.createRetailOrderSettleableItemList(dt,allocationInfo));

		int drsTranId = this.dtRepo.insert(dt);

		skuIdentifyDao.update(allocationInfo,drsTranId);

		return drsTranId;
	}


	private void doIvsSkuIdentification( int index){

		if(StringUtils.hasText(this.getAllocationInfos().get(index).getIvsSkuIdentificationStatus())){

			String remark = this.getAllocationInfos().get(index).getIvsSkuIdentificationRemark() + "/"+
					this.getAllocationInfos().get(index).getIvsSkuIdentificationDrsTranId()+"_"+
					this.getAllocationInfos().get(index).getIvsSkuIdentificationStatus();

			this.getAllocationInfos().get(index).setIvsSkuIdentificationRemark(remark);
		}

		this.getAllocationInfos().get(index).setIvsSkuIdentificationStatus("other-transaction");

	}
	

	
	private DrsTransactionLineItemSource createRetailOrderSettleableItemElements(Marketplace marketplace,String sku,Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup,SkuShipmentAllocationInfo allocateInfo) {
		String supplierKcode = this.dao.querySupplierKcode(sku);
		BigDecimal drsRetainmentRate = this.getDrsRetainmentRate(this.drsSettlementStart,this.drsSettlementEnd,marketplace.getCountry(),supplierKcode);
		BigDecimal pretaxPrincipalPrice = this.getPretaxPrincipalPrice(unitAmountGroup);
		BigDecimal msdc_ur = this.getMsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal umf = BigDecimal.ZERO;
		BigDecimal umfnr = BigDecimal.ZERO;
		BigDecimal uff = BigDecimal.ZERO;
		BigDecimal ssdc_ur = this.getSsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal exchangeRate = this.getExportExchangeRate(marketplace,allocateInfo);
		BigDecimal ss2spUipMarketCurrency = allocateInfo.getFcaPrice().multiply(exchangeRate).setScale(6,RoundingMode.HALF_UP);
		BigDecimal profitShare = this.getSs2spUnitProfitShareAddition(pretaxPrincipalPrice,msdc_ur,umf,umfnr,uff,ssdc_ur,ss2spUipMarketCurrency);
		return new DrsTransactionLineItemSourceSvcImpl(marketplace.getCurrency(),pretaxPrincipalPrice,msdc_ur,umf,umfnr,uff,ssdc_ur,ss2spUipMarketCurrency,profitShare,null);
	}

	private BigDecimal getPretaxPrincipalPrice(Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup) {
		BigDecimal result = BigDecimal.ZERO;
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_LOST)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_LOST));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_LOST_MANUAL)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_LOST_MANUAL));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_REMOVAL_ORDER_LOST)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_REMOVAL_ORDER_LOST));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FREE_REPLACEMENT_REFUND_ITEMS)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FREE_REPLACEMENT_REFUND_ITEMS));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_DAMAGE)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_DAMAGE));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_MISSING_FROM_INBOUND)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_MISSING_FROM_INBOUND));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_REVERSAL_REIMBURSEMENT)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_REVERSAL_REIMBURSEMENT));
		return result;
	}
	
	private List<DrsTransactionLineItem> createRetailOrderSettleableItemList(DrsTransaction dt,SkuShipmentAllocationInfo allocationInfo) {
		String splrKcode = this.dao.queryCompanyKcodeBySku(dt.getProductSku());
		String ssdcKcode = this.dao.queryHandlerDrsKcode(splrKcode);
		String msdcKcode = this.dao.queryDrsCompanyKcodeByCountry(dt.getMarketplace().getCountry().name());
		BigDecimal salesTaxRate = allocationInfo.getIvsSalesTaxRate();
		BigDecimal salesTax = allocationInfo.getFcaPrice().multiply(salesTaxRate);
		BigDecimal unitTotal = allocationInfo.getFcaPrice().add(salesTax);
		Assert.isTrue(allocationInfo.getDdpCurrency()==Currency.USD,"allocationInfo.getDdpCurrency()==Currency.USD");
		Assert.isTrue(dt.getSettleableItemElements().getCurrency()==dt.getMarketplace().getCurrency(),"dt.getSettleableItemElements().getCurrency()==dt.getMarketplace().getCurrency()");
		BigDecimal ss2sp_upsa = dt.getSettleableItemElements().getSpProfitShare(); 
		BigDecimal ms2ssUipInMarketSideCurrency = this.getMs2ssUnitInventoryPayment(dt.getSettleableItemElements());
		BigDecimal exchangeRate = this.getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(dt.getMarketplace().getCurrency());
		BigDecimal ms2ssUipUsd = ms2ssUipInMarketSideCurrency.multiply(exchangeRate).setScale(6, RoundingMode.HALF_UP);
		BigDecimal ms2ssUpa = this.getMs2ssUnitPurchaseAlwnc(allocationInfo.getDdpAmount(),ms2ssUipUsd);
		int lineSeq=1;
		List<DrsTransactionLineItem> items = new ArrayList<>();
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,ssdcKcode, TransactionLineType.MS2SS_UNIT_DDP_PAYMENT.getName(),                            allocationInfo.getDdpCurrency().name(),allocationInfo.getDdpAmount()));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,ssdcKcode,splrKcode,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT.getName(),               allocationInfo.getFcaCurrency().name(),                   unitTotal));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,ssdcKcode,splrKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION.getName(),         dt.getMarketplace().getCurrency().name(),                  ss2sp_upsa));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,ssdcKcode,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES.getName(),allocationInfo.getDdpCurrency().name(),                    ms2ssUpa));
		Assert.isTrue(items.size()>=1,"items.size()>=1");
		return items;
	}
	
	private BigDecimal getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(Currency src){
		return src==Currency.USD?BigDecimal.ONE:this.currencyRepo.queryExchangeRate(this.drsSettlementStart,this.drsSettlementEnd,src,Currency.USD,
				DrsConstants.interbankRateToUsdForActualMs2ssUnitInventoryPayment);
	}
	
	private BigDecimal getMs2ssUnitPurchaseAlwnc(BigDecimal ddpAmount,BigDecimal ms2ssUip) {
		Assert.isTrue(ddpAmount.signum()==1,"ddpAmount.signum()==1");
		return ms2ssUip.subtract(ddpAmount);
	}
	
	private BigDecimal getDrsRetainmentRate(Date start, Date end, Country country, String supplierKcode) {
		BigDecimal actualDrsRetainmentRate = this.dao.queryRetainmentRate(start, end, country, supplierKcode);
		Assert.notNull(actualDrsRetainmentRate,"actualDrsRetainmentRate null");
		return actualDrsRetainmentRate;
	}

	private BigDecimal getMs2ssUnitInventoryPayment(DrsTransactionLineItemSource elements) {
		BigDecimal ssdcRetainment = elements.getSsdcRetainment();
		BigDecimal fcaMsCurrency  = elements.getFcaInMarketsideCurrency();
		BigDecimal spProfitShare  = elements.getSpProfitShare();
		Assert.isTrue(elements.getCurrency()==this.msCurrency,"elements.getCurrency()==this.msCurrency");
		return ssdcRetainment.add(fcaMsCurrency).add(spProfitShare);
	}
	
	private BigDecimal getSs2spUnitProfitShareAddition(BigDecimal salePriceUntaxed,BigDecimal msdc_ur,BigDecimal umf,BigDecimal umfnr,BigDecimal uff,BigDecimal ssdc_ur,BigDecimal ss2spUipMarketCurrency) {
		BigDecimal localUmfnr = umfnr==null?BigDecimal.ZERO:new BigDecimal(umfnr.toPlainString());
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
