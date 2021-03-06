package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.*;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.enums.AmazonTransactionType;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class FbaReimbursement extends AmazonTransaction {


	private DrsTransactionDao dtRepo = (DrsTransactionDao) SpringAppCtx
			.get().getBean(DrsTransactionDao.class);

	private ShipmentSkuIdentificationDao skuIdentifyDao = (ShipmentSkuIdentificationDao)SpringAppCtx
			.get().getBean(ShipmentSkuIdentificationDao.class);

	private final BigDecimal ssdcAllocationRate = new BigDecimal("0.712");
	private final BigDecimal msdcAllocationRate = new BigDecimal("0.288");
	private Date drsSettlementStart = null;
	private Date drsSettlementEnd = null;

	private Currency msCurrency=null;

	public FbaReimbursement(int id, Date transactionDate, String type,
						 String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}

	public FbaReimbursement(int id, Date transactionDate, String type,
						 String source, String sourceId, String sku, String description) {

		super(id, transactionDate, type, source, sourceId, sku, description);
	}


	@Override
	public List<Integer> process(Date periodStart, Date periodEnd) {

		this.drsSettlementStart = periodStart;
		this.drsSettlementEnd = periodEnd;
		Marketplace sourceMarketplace = Marketplace.fromName(this.getSource());
		this.msCurrency = sourceMarketplace.getCurrency();
		List<Object []> columnsList = this.dao.queryAmazonSettlementReportTransactionInfo(this.getTransactionDate(),
				this.getType(),sourceMarketplace.getKey(),this.getSourceId(),"FBA Inventory Reimbursement",null
				,this.getSku());

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
		Assert.isTrue(totalAmountGroup.containsKey(AmzAmountTypeDesc.FBA_REVERSAL_REIMBURSEMENT),"totalAmountGroup.containsKey(AmzAmountTypeDesc.FBA_REVERSAL_REIMBURSEMENT)");
		List<Map<AmzAmountTypeDesc,BigDecimal>> unitAmountGroups = this.createUnitAmountGroups(totalQuantityPurchased,totalAmountGroup);

		return this.processTransactionInReturnStyle(periodStart,periodEnd,sourceMarketplace,totalQuantityPurchased,unitAmountGroups);
	}
	
	private int getSourceOrderDrsTransactionId(String sourceId, String sku, Integer assignedSourceOrderSeq) { // to get the first source order DRS transactoin id which has not been refunded.
		List<Integer> sourceOrderDrsTransactionIds = this.dao.queryDrsTransactionIds(AmazonTransactionType.ORDER.getValue(),sourceId,sku);
		Assert.isTrue(sourceOrderDrsTransactionIds.size()>=1,"No Related orders founded.");
		if(assignedSourceOrderSeq!=null) return sourceOrderDrsTransactionIds.get(assignedSourceOrderSeq); 
		else{
			List<Integer> sourceRefndDrsTransactionIds = this.dao.queryDrsTransactionIds(AmazonTransactionType.REFUND.getValue(),sourceId,sku);
			List<Integer> sourceOtherDrsTransactionIds = this.dao.queryDrsTransactionIds(AmazonTransactionType.OTHER.getValue(),sourceId,sku);
			Assert.isTrue(sourceOrderDrsTransactionIds.size()>=sourceRefndDrsTransactionIds.size(),"sourceOrderDrsTransactionIds.size()>=sourceRefndDrsTransactionIds.size()");
			//Assert.isTrue(sourceRefndDrsTransactionIds.size()> sourceOtherDrsTransactionIds.size(),"All related orders have been reimbursed.");
			return sourceOrderDrsTransactionIds.get(sourceOtherDrsTransactionIds.size());
		}
	}

	private List<Integer> processTransactionInReturnStyle(Date periodStart,Date periodEnd,
														  Marketplace sourceMarketplace,
														  int totalQuantityPurchased,List<Map<AmzAmountTypeDesc,BigDecimal>> unitAmountGroups) {
		List<Integer> resultIds = new ArrayList<>();
		for(int i=0;i<totalQuantityPurchased;i++){
			int sequence = i+1;
			Map<AmzAmountTypeDesc, BigDecimal> unitAmountGroup = unitAmountGroups.get(i);
			resultIds.add(this.createDrsTransactionRefundStyle(periodStart, periodEnd, sourceMarketplace, sequence,unitAmountGroup));
		}
		return resultIds;
	}


	private Integer createDrsTransactionRefundStyle(Date periodStart, Date periodEnd,
													Marketplace sourceMarketplace, int sequence,
													Map<AmzAmountTypeDesc,
			BigDecimal> unitAmountGroup) {

		Assert.notNull(this.getSourceId(),"other.getSourceId()");
		int sourceOrderDrsTransactionId = this.getSourceOrderDrsTransactionId(this.getSourceId(),
				this.getSku(),this.getAssignedSourceOrderSeq());

		DrsTransaction sourceOrder = this.dtRepo.query(sourceOrderDrsTransactionId);

		List<Object []> resultList = this.skuIdentifyDao.query(sourceOrderDrsTransactionId);
		boolean canUseSourceIvs = true;
		if(resultList.size() == 0){
			canUseSourceIvs = false;
		}else{
			String status = resultList.get(0)[2] != null ? resultList.get(0)[2].toString() : "";
			if(status.equals("") || status.equals("Refund")){
				canUseSourceIvs = true;
			}else{
				canUseSourceIvs = false;
			}
		}


		if(!canUseSourceIvs){
			resultList = this.skuIdentifyDao.queryCanBeSold( Marketplace.fromName(this.getSource()).
					getUnsDestinationCountry(),this.getSku());
		}

		String newIvsName =  resultList.get(0)[4].toString();
		String newUnsName =  resultList.get(0)[5].toString();

		DrsTransactionSvcImpl dt = new DrsTransactionSvcImpl("other-transaction",
				this.getTransactionDate(),sequence,this.getSku(),1,
				sourceMarketplace,this.getSourceId(),
				newIvsName,newUnsName);

		DrsTransactionLineItemSource drsTransactionLineItemSource =
				this.createRetailOtherSettleableItemElementListForOtherTypeRefundRelated(
						dt,sourceOrder,unitAmountGroup);

		dt.setSettleableItemElements(drsTransactionLineItemSource);
		dt.setSettleableItemList(this.createSettleableItemListForOtherTypeRefundRelated(dt, sourceOrder));


		int dtId = this.dtRepo.insert(dt);
		this.doShipmentSkuIdentification(dtId,sourceOrderDrsTransactionId, this.getSku() ,resultList);


		return dtId;
	}

	private void doShipmentSkuIdentification(int drsTranId , int sourceDrsTranId , String sku
			, List<Object []> resultList){

		Integer id = ((Long) resultList.get(0)[0]).intValue();
		Integer oldDrsTranId =  resultList.get(0)[1] != null ?  (Integer) resultList.get(0)[1] : 0;
		String status =  resultList.get(0)[2] != null ? resultList.get(0)[2].toString() : "";
		String remark =  resultList.get(0)[3] != null ? resultList.get(0)[3] .toString() : "";

		remark = remark + "/" + oldDrsTranId + "_" + status;

		this.skuIdentifyDao.update(id , drsTranId,"other-transaction",remark);


	}
	
	private List<DrsTransactionLineItem> createSettleableItemListForOtherTypeRefundRelated(DrsTransaction dt,DrsTransaction sourceOrder){
		String splrKcode = this.dao.queryCompanyKcodeBySku(dt.getProductSku());
		String ssdcKcode = this.dao.queryHandlerDrsKcode(splrKcode);
		String msdcKcode = this.dao.queryDrsCompanyKcodeByCountry(dt.getMarketplace().getCountry().name());
		DrsTransactionLineItem sourceDdpAmount = this.getDrsTransactionSettleableItem(sourceOrder.getSettleableItemList(),TransactionLineType.MS2SS_UNIT_DDP_PAYMENT.getName());
		DrsTransactionLineItem sourceSs2spUip = this.getDrsTransactionSettleableItem(sourceOrder.getSettleableItemList(),TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT.getName());
		Assert.isTrue(sourceDdpAmount.getCurrency().equals(Currency.USD.name()),"sourceDdpAmount.getCurrency().equals(Currency.USD.name())");
		BigDecimal ss2sp_upsa = dt.getSettleableItemElements().getSpProfitShare();		
		BigDecimal ms2ssUipInMarketSideCurrency = this.getMs2ssUnitInventoryPayment(dt.getSettleableItemElements());
		BigDecimal exchangeRate = this.getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(dt.getMarketplace().getCurrency());
		BigDecimal ms2ssUipUsd = ms2ssUipInMarketSideCurrency.multiply(exchangeRate).setScale(6, RoundingMode.HALF_UP);
		BigDecimal ms2ssUpa = this.getMs2ssUnitPurchaseAlwnc(sourceDdpAmount.getAmount(),ms2ssUipUsd);
		
		int lineSeq=1;
		List<DrsTransactionLineItem> itemList = new ArrayList<>();
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,ssdcKcode, TransactionLineType.MS2SS_UNIT_DDP_PAYMENT.getName(),sourceDdpAmount.getCurrency(),sourceDdpAmount.getAmount()));
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++,ssdcKcode,splrKcode,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT.getName(),sourceSs2spUip.getCurrency(),sourceSs2spUip.getAmount()));
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++,ssdcKcode,splrKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION.getName(),this.msCurrency.name(),          ss2sp_upsa));
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,ssdcKcode,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES.getName(),sourceDdpAmount.getCurrency(),ms2ssUpa));
		if(itemList.size()==0) return null;
		return itemList;
	}
	
	private DrsTransactionLineItem getDrsTransactionSettleableItem(List<DrsTransactionLineItem> items,String targetItemName){
		int occurrence = 0;
		DrsTransactionLineItem result = null;
		for(DrsTransactionLineItem item:items){
			if(item.getItemName().equals(targetItemName)){
				occurrence++;
				result = item;
			}
		}
		Assert.isTrue(occurrence==1,"occurrence==1");
		return result;
	}
	
	private DrsTransactionLineItemSource createRetailOtherSettleableItemElementListForOtherTypeRefundRelated(DrsTransaction dt,DrsTransaction sourceOrder,Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup) {
		String supplierKcode = this.dao.querySupplierKcode(dt.getProductSku());
		BigDecimal drsRetainmentRate = this.getDrsRetainmentRate(this.drsSettlementStart,this.drsSettlementEnd,dt.getMarketplace().getCountry(),supplierKcode);
		BigDecimal pretaxPrincipalPrice = this.getPretaxPrincipalPrice(unitAmountGroup);
		BigDecimal msdcUr = this.getMsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal umf = BigDecimal.ZERO;
		BigDecimal umfnr = BigDecimal.ZERO;
		BigDecimal uff = BigDecimal.ZERO;
		BigDecimal ssdcUr = this.getSsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal fcaMsCurrency = sourceOrder.getSettleableItemElements().getFcaInMarketsideCurrency();
		BigDecimal profitShare = this.getSs2spUnitProfitShareAddition(pretaxPrincipalPrice,msdcUr,umf,umfnr,uff,ssdcUr,fcaMsCurrency);		
		return new DrsTransactionLineItemSourceSvcImpl(this.msCurrency,pretaxPrincipalPrice,msdcUr,umf,umfnr,uff,ssdcUr,fcaMsCurrency,profitShare,null);
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

	private BigDecimal getPretaxPrincipalPrice(Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup) {
		BigDecimal result = BigDecimal.ZERO;
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_LOST)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_LOST));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_LOST_MANUAL)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_LOST_MANUAL));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_REMOVAL_ORDER_LOST)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_REMOVAL_ORDER_LOST));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_DAMAGE)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_DAMAGE));
		if(unitAmountGroup.containsKey(AmzAmountTypeDesc.FBA_REVERSAL_REIMBURSEMENT)) result = result.add(unitAmountGroup.get(AmzAmountTypeDesc.FBA_REVERSAL_REIMBURSEMENT));
		return result;
	}
	
	private BigDecimal getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(Currency src){
		return src==Currency.USD?BigDecimal.ONE:this.currencyRepo.queryExchangeRate(this.drsSettlementStart,this.drsSettlementEnd,src,Currency.USD, DrsConstants.interbankRateToUsdForActualMs2ssUnitInventoryPayment);
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
