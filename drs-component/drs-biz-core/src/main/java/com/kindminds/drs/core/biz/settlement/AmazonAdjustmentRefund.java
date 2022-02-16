package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.*;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItemSource;
import com.kindminds.drs.api.v1.model.amazon.AmazonSettlementReportTransactionInfo;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.persist.v1.model.mapping.settleable.DrsTransactionImpl;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSourceSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionSvcImpl;
import com.kindminds.drs.v1.model.impl.amazon.AmazonSettlementReportTransactionInfoImpl;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class AmazonAdjustmentRefund extends AmazonTransaction {

	private DrsTransactionDao dtRepo = (DrsTransactionDao) SpringAppCtx
			.get().getBean(DrsTransactionDao.class);

	private ShipmentSkuIdentificationDao skuIdentifyDao = (ShipmentSkuIdentificationDao)SpringAppCtx
			.get().getBean(ShipmentSkuIdentificationDao.class);

	private Date drsSettlementStart = null;
	private Date drsSettlementEnd = null;
	private Marketplace marketplace = null;
	private String kcodeMsdc = null;
	private Currency msCurrency=null;

	public AmazonAdjustmentRefund(Integer id, Date transactionDate, String type,
                                  String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}

	public AmazonAdjustmentRefund(Integer id, Date transactionDate, String type,
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

		System.out.println("AAAAAAAAA");
		System.out.println(this.marketplace);
		System.out.println(this.drsSettlementStart);
		System.out.println(this.drsSettlementEnd);
		System.out.println("AAAAAAAAA");

		return this.createDrsTransactions(periodStart,periodEnd);
	}
	


	private List<Integer> createDrsTransactions(Date periodStart,Date periodEnd) {

		System.out.println(this.getSource());
		System.out.println(this.getSourceId());
		System.out.println(this.getSku());

		List<DrsTransaction> dtList	 = this.dtRepo.query(this.getSourceId(),this.getSku());
		List<Integer> resultIds = new ArrayList<>();

		System.out.println(dtList.size());

		for(int i=0;i<dtList.size();i++){
			int sequence = i+1;
			resultIds.add(this.createDrsTransaction(periodStart,periodEnd,sequence,dtList.get(i)));
		}

		return resultIds;
	}

	private Integer createDrsTransaction(Date periodStart,Date periodEnd, int seq , DrsTransaction originalDrsTran) {

		//this.AssertNoFulfillmentFeeExist(unitAmountGroup);

		DrsTransactionSvcImpl dt = new DrsTransactionSvcImpl(AmazonTransactionType.REFUND.getValue(),
				this.getTransactionDate(),seq,this.getSku(),1,this.marketplace,this.getSourceId(),
				originalDrsTran.getShipmentIvsName(),originalDrsTran.getShipmentUnsName());


		dt.setSettleableItemElements(this.createRetailRefundSettleableItemElements(dt,originalDrsTran ));

		dt.setSettleableItemList(this.createSettleableItemListForRefund(dt,originalDrsTran));

		int drsTranId = this.dtRepo.insert(dt);

		this.doShipmentSkuIdentification(drsTranId , ((DrsTransactionImpl)originalDrsTran).getId() ,
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

			this.skuIdentifyDao.update(id , drsTranId , "Refund" ,remark );
		}

	}


	private DrsTransactionLineItemSource createRetailRefundSettleableItemElements(DrsTransactionSvcImpl dt ,
																			DrsTransaction originalDrsTran	 ) {

		//todo arthur
		BigDecimal pretaxedPrice   = originalDrsTran.getSettleableItemElements().getPretaxPrincipalPrice().negate();
		BigDecimal msdcRetainment  = originalDrsTran.getSettleableItemElements().getMsdcRetainment().negate();
		BigDecimal marketplaceFee  = originalDrsTran.getSettleableItemElements().getMarketplaceFee().negate();
		BigDecimal nonRefundableMF = originalDrsTran.getSettleableItemElements().getMarketplaceFeeNonRefundable().negate();
		BigDecimal fulfillmentFee  = originalDrsTran.getSettleableItemElements().getFulfillmentFee().negate();
		BigDecimal ssdcRetainment  = originalDrsTran.getSettleableItemElements().getSsdcRetainment().negate();
		BigDecimal fcaInMarketCur  = originalDrsTran.getSettleableItemElements().getFcaInMarketsideCurrency().negate();
		BigDecimal splrProfitShare = originalDrsTran.getSettleableItemElements().getSpProfitShare().negate();

		BigDecimal refundCommission = originalDrsTran.getSettleableItemElements().getFulfillmentFee();

		return new DrsTransactionLineItemSourceSvcImpl(this.msCurrency,pretaxedPrice,msdcRetainment,
				marketplaceFee,nonRefundableMF,fulfillmentFee,ssdcRetainment,fcaInMarketCur,splrProfitShare,refundCommission);


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
	


	
	private void refundSumCheck(BigDecimal pretaxedPrice, BigDecimal msdcUr, BigDecimal umf, BigDecimal umfnr, BigDecimal uff, BigDecimal ssdcUr, BigDecimal fcaMsCurrency, BigDecimal spProfitShare){
		BigDecimal localUmfnr = umfnr==null?BigDecimal.ZERO:new BigDecimal(umfnr.toPlainString());
		BigDecimal elementsSum = msdcUr.add(umf).add(localUmfnr).add(uff).add(ssdcUr).add(fcaMsCurrency).add(spProfitShare);
		Assert.isTrue(pretaxedPrice.compareTo(elementsSum)==0,elementsSum.toPlainString()+","+pretaxedPrice.toPlainString());
	}
	

	
}
