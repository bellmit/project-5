package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.*;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.persist.v1.model.mapping.settleable.DrsTransactionImpl;
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
import org.apache.poi.util.SystemOutLogger;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class AmazonRefund extends AmazonTransaction {

	private DrsTransactionDao dtRepo = (DrsTransactionDao) SpringAppCtx
			.get().getBean(DrsTransactionDao.class);

	private ShipmentSkuIdentificationDao skuIdentifyDao = (ShipmentSkuIdentificationDao)SpringAppCtx
			.get().getBean(ShipmentSkuIdentificationDao.class);
	
	private Date drsSettlementStart = null;
	private Date drsSettlementEnd = null;
	private Marketplace marketplace = null;
	private String kcodeMsdc = null;
	private Currency msCurrency=null;

	public AmazonRefund(Integer id, Date transactionDate, String type,
					   String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}

	public AmazonRefund(Integer id, Date transactionDate, String type,
					   String source, String sourceId, String sku, String description) {

		super(id, transactionDate, type, source, sourceId, sku, description);
	}

	@Override
	public List<Integer> process(Date periodStart, Date periodEnd) {

		Assert.isTrue(!"".equals(this.getSourceId()),"equals(refund.getSourceId())");
		this.marketplace = Marketplace.fromName(this.getSource());
		this.msCurrency = this.marketplace.getCurrency();
		this.kcodeMsdc = this.dao.queryDrsCompanyKcodeByCountry(this.marketplace.getCountry().name());
		this.drsSettlementStart = periodStart;
		this.drsSettlementEnd = periodEnd;

		Assert.notNull(this.getSourceId(),"refund.getSourceId()");

		List<Object []> columnsList =
				this.dao.queryAmazonSettlementReportTransactionInfo(this.getTransactionDate(),this.getType(),
						this.marketplace.getKey(),this.getSourceId(),null,null,
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


		Map<AmzAmountTypeDesc,BigDecimal> totalAmountGroup =
				this.createTotalAmountGroup(transactionInfos);

		if(this.ignorable(totalAmountGroup)) return null;

		//todo arthur get refund qty from data range report
		int totalRefundQuantity=this.getTotalRefundQty(totalAmountGroup);

		List<Map<AmzAmountTypeDesc,BigDecimal>> unitAmountGroups =
				this.createUnitAmountGroups(totalRefundQuantity,totalAmountGroup);

		return this.createDrsTransactions(periodStart,periodEnd,totalRefundQuantity,unitAmountGroups);
	}
	
	private boolean ignorable(Map<AmzAmountTypeDesc,BigDecimal> totalAmountGroup){
		boolean result=false;
		if(totalAmountGroup.size()==2
		 &&totalAmountGroup.containsKey(AmzAmountTypeDesc.PRICE_SHIPPING)
		 &&totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_SHIPPING_CHARGEBACK)
		 &&totalAmountGroup.get(AmzAmountTypeDesc.PRICE_SHIPPING).add(totalAmountGroup.get(AmzAmountTypeDesc.FEE_SHIPPING_CHARGEBACK)).compareTo(BigDecimal.ZERO)==0){
			result = true;
		}
		if(totalAmountGroup.size()==3
		 &&totalAmountGroup.containsKey(AmzAmountTypeDesc.PRICE_SHIPPINGTAX)
		 &&totalAmountGroup.containsKey(AmzAmountTypeDesc.PRICE_SHIPPING)
		 &&totalAmountGroup.containsKey(AmzAmountTypeDesc.FEE_SHIPPING_CHARGEBACK)
		 &&totalAmountGroup.get(AmzAmountTypeDesc.PRICE_SHIPPING).add(totalAmountGroup.get(AmzAmountTypeDesc.FEE_SHIPPING_CHARGEBACK)).compareTo(BigDecimal.ZERO)==0){
			result = true;
		}
		if(totalAmountGroup.size()==2
		 &&totalAmountGroup.containsKey(AmzAmountTypeDesc.PRICE_SHIPPING)
		 &&totalAmountGroup.containsKey(AmzAmountTypeDesc.PROMOTION_SHIPPING)
		 &&totalAmountGroup.get(AmzAmountTypeDesc.PRICE_SHIPPING).add(totalAmountGroup.get(AmzAmountTypeDesc.PROMOTION_SHIPPING)).compareTo(BigDecimal.ZERO)==0){
			result = true;
		}
		return result;
	}
	
	private Map<AmzAmountTypeDesc,BigDecimal> createTotalAmountGroup(
			List<AmazonSettlementReportTransactionInfo> transactionInfos){

		Map<AmzAmountTypeDesc,BigDecimal> totalAmount = new HashMap<>();

		for(AmazonSettlementReportTransactionInfo info:transactionInfos){
			AmzAmountTypeDesc typeDesc =
					AmzAmountTypeDesc.fromValue(info.getAmountType(),info.getAmountDescription());

			if(!totalAmount.containsKey(typeDesc)) totalAmount.put(typeDesc,BigDecimal.ZERO);

			totalAmount.put(typeDesc,totalAmount.get(typeDesc).add(info.getAmount()));
		}

		return totalAmount;
	}

	//todo arthur use date range report to get qty
	private int getTotalRefundQty(Map<AmzAmountTypeDesc,BigDecimal> totalAmountGroup) {

		//_d is drs adjustment
//		String sourceId = this.getSourceId().contains("_d") ?
//				this.getSourceId().split("_d")[0] : this.getSourceId();
//
//		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
//		System.out.println(sourceId);
//		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");

		BigDecimal amountSum = this.dao.queryAmazonPricePrincipalAmountSum(this.marketplace,
				this.getSourceId(),this.getSku());

		BigDecimal quantitySum = this.dao.queryAmazonPricePrincipalQuantitySum(this.marketplace,
				this.getSourceId(),this.getSku());

		BigDecimal unitPrice = amountSum.divide(quantitySum);
		BigDecimal totalRefundAmount = totalAmountGroup.get(AmzAmountTypeDesc.PRICE_PRINCIPAL);
		BigDecimal result = totalRefundAmount.divide(unitPrice ,2, RoundingMode.HALF_UP).abs();

//		System.out.println("AAAAAAAAAAAAAAAAA");
//		System.out.println(totalRefundAmount);
//		System.out.println(unitPrice);
//		System.out.println("AAAAAAAAAAAAAAAAA");
//		System.out.println(result);

//		int refundQty = 0; partial refund
//		if(result.compareTo(BigDecimal.ZERO) ==1 ){
//			if(result.compareTo(BigDecimal.ONE) == 1){
//				return result.intValueExact();
//			}else{
//				return  1;
//			}
//		}else{
//			return 0;
//		}

		return totalRefundAmount.divide(unitPrice).abs().intValueExact();


	}
	
	private List<Map<AmzAmountTypeDesc,BigDecimal>> createUnitAmountGroups(int totalQuantityPurchased,Map<AmzAmountTypeDesc,BigDecimal> totalAmounts){

		Map<AmzAmountTypeDesc,BigDecimal> unitAmount =
				this.createHalfDownUnitAmountGroup(totalQuantityPurchased, totalAmounts);

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

	private List<Integer> createDrsTransactions(Date periodStart,Date periodEnd,
												int quantity,List<Map<AmzAmountTypeDesc,BigDecimal>> unitAmountGroups) {

		List<Integer> resultIds = new ArrayList<>();
		for(int i=0;i<quantity;i++){
			int sequence = i+1;
			Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup = unitAmountGroups.get(i);
			resultIds.add(this.createDrsTransaction(periodStart,periodEnd,sequence,unitAmountGroup));
		}

		return resultIds;
	}

	private Integer createDrsTransaction(Date periodStart,Date periodEnd,
										 int seq,Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup) {

		this.AssertNoFulfillmentFeeExist(unitAmountGroup);

		DrsTransaction sourceOrder = this.dtRepo.query(seq,this.getSourceId(),this.getSku());

		//verify partial refund , refund

		DrsTransactionSvcImpl dt = new DrsTransactionSvcImpl(AmazonTransactionType.REFUND.getValue(),
				this.getTransactionDate(),seq,this.getSku(),1,this.marketplace,this.getSourceId(),
				sourceOrder.getShipmentIvsName(),sourceOrder.getShipmentUnsName());


		dt.setSettleableItemElements(this.createRetailRefundSettleableItemElements(dt,sourceOrder,unitAmountGroup));
		dt.setSettleableItemList(this.createSettleableItemListForRefund(dt,sourceOrder));

		int drsTranId = this.dtRepo.insert(dt);

		this.doShipmentSkuIdentification(drsTranId , ((DrsTransactionImpl)sourceOrder).getId() ,
				this.getSku(), Marketplace.fromName(this.getSource()).getUnsDestinationCountry());

		return drsTranId;

	}

	private void doShipmentSkuIdentification(int drsTranId , int sourceDrsTranId , String sku
			, Country unsDestinationCountry){

		List<Object []> resultList =  this.skuIdentifyDao.query(sourceDrsTranId , "Sold" );

		if(resultList.size() == 0){
			resultList =  this.skuIdentifyDao.queryCanBeRefund(  unsDestinationCountry , sku);
		}


		Assert.isTrue(resultList.size() > 0 , "Can't find refund shipment sku identification");

		if(resultList.size() > 0){
			Integer id = ((Long) resultList.get(0)[0]).intValue();
			Integer oldDrsTranId = resultList.get(0)[1] != null ? (Integer)resultList.get(0)[1] : 0;
			String status =  resultList.get(0)[2] != null ? resultList.get(0)[2].toString() : "";
			String remark =  resultList.get(0)[3] != null ? resultList.get(0)[3].toString() : "";

			remark = remark + "/" + oldDrsTranId + "_" + status;

			//todo arthur verify partial refund , refund

			this.skuIdentifyDao.update(id , drsTranId , "Refund" ,remark );
		}

	}


	private DrsTransactionLineItemSource createRetailRefundSettleableItemElements(DrsTransactionSvcImpl dt,DrsTransaction sourceOrder,Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup) {
		Assert.isTrue(!unitAmountGroup.containsKey(AmzAmountTypeDesc.FEE_SALESTAXSERVICE),"!unitAmountGroup.containsKey(AmzAmountTypeDesc.FEE_SALESTAXSERVICE)");
		BigDecimal pretaxedRefundPrice = this.getPretaxPrincipalPrice(unitAmountGroup);

		System.out.println(pretaxedRefundPrice);
		System.out.println(sourceOrder.getSettleableItemElements().getPretaxPrincipalPrice().negate());

		//Assert.isTrue(this.valueEqual(pretaxedRefundPrice,sourceOrder.getSettleableItemElements().getPretaxPrincipalPrice().negate()),
			//	"this.valueEqual(pretaxedRefundPrice,sourceOrder.getSettleableItemElements().getPretaxPrincipalPrice()");

		BigDecimal pretaxedPrice   = pretaxedRefundPrice;
		BigDecimal msdcRetainment  = sourceOrder.getSettleableItemElements().getMsdcRetainment().negate();
		BigDecimal marketplaceFee  = unitAmountGroup.get(AmzAmountTypeDesc.FEE_COMMISSION).abs().negate();
		BigDecimal nonRefundableMF = sourceOrder.getSettleableItemElements().getMarketplaceFeeNonRefundable()==null?BigDecimal.ZERO:sourceOrder.getSettleableItemElements().getMarketplaceFeeNonRefundable().negate();
		BigDecimal fulfillmentFee  = sourceOrder.getSettleableItemElements().getFulfillmentFee().negate();
		BigDecimal ssdcRetainment  = sourceOrder.getSettleableItemElements().getSsdcRetainment().negate();
		BigDecimal fcaInMarketCur  = sourceOrder.getSettleableItemElements().getFcaInMarketsideCurrency().negate();
		BigDecimal splrProfitShare = calculateProfitShareReturn(pretaxedPrice, msdcRetainment, marketplaceFee, nonRefundableMF, fulfillmentFee, ssdcRetainment, fcaInMarketCur);
		this.refundSumCheck(pretaxedPrice, msdcRetainment, marketplaceFee, nonRefundableMF, fulfillmentFee, ssdcRetainment, fcaInMarketCur, splrProfitShare);
		BigDecimal refundCommission = unitAmountGroup.get(AmzAmountTypeDesc.FEE_REFUND_COMMISSION);
		return new DrsTransactionLineItemSourceSvcImpl(this.msCurrency,pretaxedPrice,msdcRetainment,marketplaceFee,nonRefundableMF,fulfillmentFee,ssdcRetainment,fcaInMarketCur,splrProfitShare,refundCommission);
	}
	
	private BigDecimal calculateProfitShareReturn(
			BigDecimal pretaxedPrice,
			BigDecimal msdcRetainment,
			BigDecimal marketplaceFee,
			BigDecimal nonRefundableMF,
			BigDecimal fulfillmentFee,
			BigDecimal ssdcRetainment,
			BigDecimal fcaInMarketCur){
		return pretaxedPrice.subtract(
				msdcRetainment
				.add(marketplaceFee)
				.add(nonRefundableMF)
				.add(fulfillmentFee)
				.add(ssdcRetainment)
				.add(fcaInMarketCur));
	}
	
	private BigDecimal getPretaxPrincipalPrice(Map<AmzAmountTypeDesc,BigDecimal> unitAmountGroup){
		Assert.isTrue(unitAmountGroup.containsKey(AmzAmountTypeDesc.PRICE_PRINCIPAL),"unitAmountGroup.containsKey(AmzAmountTypeDesc.PRICE_PRINCIPAL)");
		BigDecimal principal = unitAmountGroup.get(AmzAmountTypeDesc.PRICE_PRINCIPAL);
		BigDecimal promotion = unitAmountGroup.containsKey(AmzAmountTypeDesc.PROMOTION_PRINCIPAL)?unitAmountGroup.get(AmzAmountTypeDesc.PROMOTION_PRINCIPAL):BigDecimal.ZERO;
		return principal.add(promotion);
	}
	
	private List<DrsTransactionLineItem> createSettleableItemListForRefund(DrsTransactionSvcImpl dt,DrsTransaction sourceOrder) {
		String supplierKcode = this.dao.queryCompanyKcodeBySku(dt.getProductSku());
		String handlerDrsKcode = this.dao.queryHandlerDrsKcode(supplierKcode);
		DrsTransactionLineItem sourceMs2ssUip = this.getDrsTransactionSettleableItem(sourceOrder.getSettleableItemList(),TransactionLineType.MS2SS_UNIT_DDP_PAYMENT.getName());
		DrsTransactionLineItem sourceSs2spUip = this.getDrsTransactionSettleableItem(sourceOrder.getSettleableItemList(),TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT.getName());
		DrsTransactionLineItem sourcePchAlwnc = this.getDrsTransactionSettleableItem(sourceOrder.getSettleableItemList(),TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES.getName());
		Assert.isTrue(sourceMs2ssUip.getCurrency().equals(Currency.USD.name()),"sourceMs2ssUip.getCurrency().equals(Currency.USD.name())");
		BigDecimal ms2ssUir = sourceMs2ssUip.getAmount().negate();
		BigDecimal profitShareReverse = dt.getSettleableItemElements().getSpProfitShare(); 
		BigDecimal ms2ssUpar = sourcePchAlwnc.getAmount().negate();
		BigDecimal nonRefundableMarketplaceFee = dt.getSettleableItemElements().getMarketplaceFeeNonRefundable();
		BigDecimal fulfillmentFeeReverse = dt.getSettleableItemElements().getFulfillmentFee();
		BigDecimal refundFee = dt.getSettleableItemElements().getRefundFee();
		BigDecimal tempNonRefundableMarketplaceFee = nonRefundableMarketplaceFee==null?BigDecimal.ZERO:nonRefundableMarketplaceFee;
		BigDecimal refundRelateFeeInMarketSideCurrency = tempNonRefundableMarketplaceFee.add(fulfillmentFeeReverse).add(refundFee);
		BigDecimal exchangeRate = this.getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(this.msCurrency);
		BigDecimal refundRelateFeeUsd = refundRelateFeeInMarketSideCurrency.multiply(exchangeRate).setScale(6,RoundingMode.HALF_UP);
		int lineSeq=1;
		List<DrsTransactionLineItem> itemList = new ArrayList<>();
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++, this.kcodeMsdc,handlerDrsKcode,TransactionLineType.MS2SS_UNIT_DDP_REFUND.getName(),Currency.USD.name(),ms2ssUir));	
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++,handlerDrsKcode,  supplierKcode,TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND.getName(),sourceSs2spUip.getCurrency(),sourceSs2spUip.getAmount().negate()));
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++,handlerDrsKcode,  supplierKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION.getName(),this.msCurrency.name(),          profitShareReverse));
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++, this.kcodeMsdc,handlerDrsKcode, TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND.getName(),Currency.USD.name(),          ms2ssUpar));
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++,handlerDrsKcode,  supplierKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE.getName(),this.msCurrency.name(),  refundRelateFeeInMarketSideCurrency));
		itemList.add(new DrsTransactionLineItemSvcImpl(lineSeq++, this.kcodeMsdc,handlerDrsKcode,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE.getName(),Currency.USD.name(),refundRelateFeeUsd));
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
	
	private BigDecimal getCurrencyExchangeRateToUsdForMs2ssActualUnitPayment(Currency src){
		return src==Currency.USD?BigDecimal.ONE:this.currencyRepo.queryExchangeRate(this.drsSettlementStart,this.drsSettlementEnd,src,Currency.USD, DrsConstants.interbankRateToUsdForActualMs2ssUnitInventoryPayment);
	}
	
	private void AssertNoFulfillmentFeeExist(Map<AmzAmountTypeDesc, BigDecimal> unitAmountGroup){
		Assert.isTrue(!unitAmountGroup.containsKey(AmzAmountTypeDesc.FEE_PERUNIT),"!unitAmountGroup.containsKey(AmzAmountTypeDesc.FEE_PERUNIT)");
		Assert.isTrue(!unitAmountGroup.containsKey(AmzAmountTypeDesc.FEE_FBA_WEIGHTBASED),"!unitAmountGroup.containsKey(AmzAmountTypeDesc.FEE_FBA_WEIGHTBASED)");
		Assert.isTrue(!unitAmountGroup.containsKey(AmzAmountTypeDesc.FEE_PERORDER),"!unitAmountGroup.containsKey(AmzAmountTypeDesc.FEE_PERORDER)");
		Assert.isTrue(!unitAmountGroup.containsKey(AmzAmountTypeDesc.SHIPMENTFEES_TRANSPORTATION),"!unitAmountGroup.containsKey(AmzAmountTypeDesc.SHIPMENTFEES_TRANSPORTATION)");
		return;
	}


	
	private void refundSumCheck(BigDecimal pretaxedPrice, BigDecimal msdcUr, BigDecimal umf, BigDecimal umfnr, BigDecimal uff, BigDecimal ssdcUr, BigDecimal fcaMsCurrency, BigDecimal spProfitShare){
		BigDecimal localUmfnr = umfnr==null?BigDecimal.ZERO:new BigDecimal(umfnr.toPlainString());
		BigDecimal elementsSum = msdcUr.add(umf).add(localUmfnr).add(uff).add(ssdcUr).add(fcaMsCurrency).add(spProfitShare);
		Assert.isTrue(pretaxedPrice.compareTo(elementsSum)==0,elementsSum.toPlainString()+","+pretaxedPrice.toPlainString());
	}
	
	private boolean valueEqual(BigDecimal a,BigDecimal b){
		return a.compareTo(b)==0;
	}
	
}
