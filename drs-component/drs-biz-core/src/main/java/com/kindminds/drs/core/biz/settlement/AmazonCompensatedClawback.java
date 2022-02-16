package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.*;
import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.api.data.access.rdb.inventory.SkuShipmentStockAllocatorDao;
import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItemSource;
import com.kindminds.drs.api.v1.model.amazon.AmazonReimbursementInfo;
import com.kindminds.drs.api.v1.model.amazon.AmazonSettlementReportTransactionInfo;
import com.kindminds.drs.api.v1.model.amazon.OriginalIvsUnsInfo;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSourceSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionLineItemSvcImpl;
import com.kindminds.drs.v1.model.impl.DrsTransactionSvcImpl;
import com.kindminds.drs.core.biz.inventory.SkuShipmentAllocationInfoImplSvc;
import com.kindminds.drs.v1.model.impl.amazon.AmazonReimbursementInfoImpl;
import com.kindminds.drs.v1.model.impl.amazon.AmazonSettlementReportTransactionInfoImpl;
import com.kindminds.drs.v1.model.impl.logistics.OriginalIvsUnsInfoImpl;
import com.kindminds.drs.v1.model.impl.stock.IvsInfoImpl;
import com.kindminds.drs.v1.model.impl.stock.UnsInfoImpl;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AmazonCompensatedClawback extends AmazonTransaction {
	
	private DrsTransactionDao dtRepo = (DrsTransactionDao)SpringAppCtx
			.get().getBean(DrsTransactionDao.class);

	private SkuShipmentStockAllocatorDao skuStockdao = (SkuShipmentStockAllocatorDao)SpringAppCtx
			.get().getBean(SkuShipmentStockAllocatorDao.class);

	private ShipmentSkuIdentificationDao skuIdentifyDao = (ShipmentSkuIdentificationDao)SpringAppCtx
			.get().getBean(ShipmentSkuIdentificationDao.class);


	private final BigDecimal ssdcAllocationRate = new BigDecimal("0.712");
	private final BigDecimal msdcAllocationRate = new BigDecimal("0.288");
	private Date drsSettlementStart = null;
	private Date drsSettlementEnd = null;

	private Currency msCurrency=null;

	public AmazonCompensatedClawback(Integer id, Date transactionDate, String type,
					   String source, String sourceId, String sku) {

		super(id, transactionDate, type, source, sourceId, sku);
	}

	public AmazonCompensatedClawback(Integer id, Date transactionDate, String type,
					   String source, String sourceId, String sku, String description) {

		super(id, transactionDate, type, source, sourceId, sku, description);
	}


	@Override
	public Integer getTotalQuantityPurchased(){
		Marketplace sourceMarketplace = Marketplace.fromName(this.getSource());
		List<Object []> columnsList = this.dao.queryAmazonSettlementReportTransactionInfo(
				this.getTransactionDate(),this.getType(),sourceMarketplace.getKey(),
				this.getSourceId(),"FBA Inventory Reimbursement",null,this.getSku());
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

		Assert.isTrue(this.getDescription().equals("COMPENSATED_CLAWBACK")
						|| this.getDescription().equals("MISSING_FROM_INBOUND_CLAWBACK"),
				"Description is not COMPENSATED_CLAWBACK");

		this.drsSettlementStart = periodStart;
		this.drsSettlementEnd = periodEnd;
		OriginalIvsUnsInfo ivsUnsInfo = null;
		Marketplace sourceMarketplace = Marketplace.fromName(this.getSource());
		this.msCurrency = sourceMarketplace.getCurrency();

		Object[] amountPostedDate = dao.queryAmountTotalPostedDate(this);

		Object [] result = dao.queryReimbursementInfo(this,
				(BigDecimal) amountPostedDate[0], (Date) amountPostedDate[1]);

		AmazonReimbursementInfo reimbursement = new AmazonReimbursementInfoImpl(result);

		String origReimburseId = reimbursement.getOriginalReimbursementId();
		String reimburseType = reimbursement.getOriginalReimbursementType();

//		System.out.println("PPPPPPPPPPPPPPPPPPPPP");
//		System.out.println(reimbursement.getReimbursementId());
//		System.out.println(origReimburseId);
//		System.out.println(reimburseType);
//		System.out.println("PPPPPPPPPPPPPPPPPPPPP");

		Object [] result2 = dao.queryOriginalReimbursementInfoById(origReimburseId , this.getSku());

		AmazonReimbursementInfo origReimbursementInfo = new AmazonReimbursementInfoImpl(result2);

		//todo arthru do we have to use order id to get source drs tran ?
		List<Object []> skuIdentifyList =
				this.skuIdentifyDao.queryCanBeRefund(  Marketplace.fromName(this.getSource()).getUnsDestinationCountry() ,
						this.getSku());

		Assert.isTrue(skuIdentifyList.size() > 0 , "Can't find refund shipment sku identification");

		if (reimbursement.getOriginalReimbursementType().equals("CustomerReturn") ||
				reimbursement.getOriginalReimbursementType().equals("CustomerServiceIssue")) {

			Object [] result1 = dao.queryOriginalIvsUnsInfo(origReimbursementInfo.getAmazonOrderId());

			ivsUnsInfo = new OriginalIvsUnsInfoImpl((String) result1[0], (String) result1[1],
					(BigDecimal) result1[2], (BigDecimal) result1[3]);

			setAllocationInfoByIvsUnsInfo(skuIdentifyList ,ivsUnsInfo);

		} else if (reimbursement.getOriginalReimbursementType().equals("Damaged_Warehouse") ||
				reimbursement.getOriginalReimbursementType().equals("Lost_Warehouse") ||
				reimbursement.getOriginalReimbursementType().equals("Lost_Inbound")) {

			Object[] dateTimeSku = dao.querySettlementInfoOfOriginalReimbursement(origReimbursementInfo);

			Object [] result1 = dao.queryOriginalIvsUnsInfoByDateSku(
					(Date) dateTimeSku[0], (String) dateTimeSku[1]);

			ivsUnsInfo =  new OriginalIvsUnsInfoImpl((String) result1[0], (String) result1[1],
					(BigDecimal) result1[2], (BigDecimal) result1[3]);

			setAllocationInfoByIvsUnsInfo(skuIdentifyList ,ivsUnsInfo);

		}

		Assert.notNull(ivsUnsInfo, "ivsUnsInfo is null. Original Reimbursement Type is: "
				+ reimbursement.getOriginalReimbursementType());

		List<Integer> processed = this.processTransactionInClawbackStyle(sourceMarketplace , reimbursement,
				origReimbursementInfo.getAmazonOrderId(),skuIdentifyList);

		//todo arhtur
		updateStockQuantityForClawback(ivsUnsInfo, reimbursement);

		this.setAllocationInfos(null);

		return processed;
	}


	private int getTotalQuantityPurchased(List<AmazonSettlementReportTransactionInfo> transactionInfos) {
		int totalQuantity = 0;
		for(AmazonSettlementReportTransactionInfo info:transactionInfos){
			totalQuantity+=info.getQuantityPurchased();
		}
		return totalQuantity;
	}



	private void updateStockQuantityForClawback(OriginalIvsUnsInfo ivsUnsInfo,
												AmazonReimbursementInfo reimbursement) {

		BigInteger lineItemId = dao.queryShipmentLineItemId(
				ivsUnsInfo.getUnsName(),ivsUnsInfo.getIvsName(),reimbursement.getSku());
		dao.updateSoldQuantityByLineItemId(lineItemId, reimbursement.getQuantityReimbursementInventory());
	}


	private void setAllocationInfoByIvsUnsInfo(List<Object []> skuIdentifyList ,OriginalIvsUnsInfo ivsUnsInfo) {

		List<SkuShipmentAllocationInfo> skuShipmentAllocationInfos = new ArrayList<>();

		Object [] columns1 = skuStockdao.queryIvsInfo(ivsUnsInfo.getIvsName(),this.getSku());

		String ivsName = (String)columns1[0];
		int fcaCurrencyId = (int)columns1[1];
		BigDecimal fcaPrice = (BigDecimal)columns1[2];
		BigDecimal salesTaxRate = (BigDecimal)columns1[3];
		SkuShipmentAllocationInfo.IvsInfo ivsInfo = new IvsInfoImpl(ivsName, fcaCurrencyId, fcaPrice, salesTaxRate);

		Object [] columns =
				skuStockdao.queryUnsInfo(ivsUnsInfo.getUnsName(),this.getSku());

		String unsName = (String)columns[0];
		int ddpCurrencyId = (int)columns[1];
		BigDecimal ddpAmount = (BigDecimal)columns[2];
		BigDecimal fxRateFromFcaCurrencyToDestinationCountryCurrency = (BigDecimal)columns[3];
		BigDecimal fxRateFromFcaCurrencyToEur = (BigDecimal)columns[4];
		UnsInfoImpl unsInfo = new UnsInfoImpl(unsName, ddpCurrencyId, ddpAmount, fxRateFromFcaCurrencyToDestinationCountryCurrency, fxRateFromFcaCurrencyToEur);



		if(skuIdentifyList.size() > 0){


			for (int i = 0 ; i <= skuIdentifyList.size() -1 ; i++){

				Integer id = ((Long) skuIdentifyList.get(i)[0]).intValue();
				Integer oldDrsTranId =  skuIdentifyList.get(i)[1] != null ?  (Integer) skuIdentifyList.get(i)[1] : 0 ;
				String status =  skuIdentifyList.get(i)[2] != null ? skuIdentifyList.get(i)[2].toString() : "" ;
				String remark =  skuIdentifyList.get(i)[3] != null ? skuIdentifyList.get(i)[3].toString() : "" ;

				skuShipmentAllocationInfos.add(new SkuShipmentAllocationInfoImplSvc(id , "",
						remark, oldDrsTranId,status , this.getSku(),ivsInfo,unsInfo));
			}



			this.setAllocationInfos(skuShipmentAllocationInfos);

		}


	}

	private List<Integer> processTransactionInClawbackStyle(
			Marketplace marketplace, AmazonReimbursementInfo reimbursement, String originalOrderId ,
			List<Object []> skuIdentifyList) {

		List<Integer> resultIds = new ArrayList<>();
		SkuShipmentAllocationInfo allocationInfo = allocationInfos.get(0);

		for (int i = 0; i < reimbursement.getQuantityReimbursementInventory().abs().intValue(); i++) {

			Integer canRefundId = ((Long) skuIdentifyList.get(i)[0]).intValue();
			Integer dtId  =  this.createClawbackDrsTransaction(marketplace, i+1, allocationInfo, reimbursement,
					originalOrderId , canRefundId);
			resultIds.add(dtId);
		}

		return resultIds;
	}


	private int createClawbackDrsTransaction(Marketplace marketplace,
			int sequence, SkuShipmentAllocationInfo allocationInfo,
			AmazonReimbursementInfo reimbursement, String originalOrderId , Integer canRefundId){

		String sku = this.getSku();

		DrsTransactionSvcImpl dt = new DrsTransactionSvcImpl(AmazonTransactionType.REFUND.getValue(),
				this.getTransactionDate(),sequence,this.getSku(),1,marketplace, originalOrderId,
				allocationInfo.getIvsName(),allocationInfo.getUnsName());

		dt.setSettleableItemElements(
				this.createClawbackTransactionSettleableItemElements(
						marketplace,sku,allocationInfo, reimbursement));

		dt.setSettleableItemList(
				this.createClawbackTransactionSettleableItemList(dt,allocationInfo));

		int drsTranId = this.dtRepo.insert(dt);

		this.doShipmentSkuIdentification(drsTranId , allocationInfo,canRefundId);

		return drsTranId;
	}

	private void doShipmentSkuIdentification(int drsTranId , SkuShipmentAllocationInfo allocationInfo ,Integer canRefundId){


		String remark =   allocationInfo.getIvsSkuIdentificationRemark() + "/"
				+ allocationInfo.getIvsSkuIdentificationDrsTranId() + "_" + allocationInfo.getIvsSkuIdentificationStatus();

		this.skuIdentifyDao.update(canRefundId , drsTranId , "Refund" ,remark );


	}


	private DrsTransactionLineItemSource createClawbackTransactionSettleableItemElements(
			Marketplace marketplace,String sku, SkuShipmentAllocationInfo allocateInfo,
			AmazonReimbursementInfo reimbursement) {
		String supplierKcode = this.dao.querySupplierKcode(sku);
		BigDecimal drsRetainmentRate = this.getDrsRetainmentRate(this.drsSettlementStart,this.drsSettlementEnd,marketplace.getCountry(),supplierKcode);
		BigDecimal pretaxPrincipalPrice = reimbursement.getAmountPerUnit();
		BigDecimal msdcRetainment = this.getMsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal marketplaceFee = BigDecimal.ZERO;
		BigDecimal nonRefundableMF = BigDecimal.ZERO;
		BigDecimal fulfillmentFee = BigDecimal.ZERO;
		BigDecimal ssdcRetainment = this.getSsdcUnitRetainment(pretaxPrincipalPrice,drsRetainmentRate);
		BigDecimal exchangeRate = this.getExportExchangeRate(marketplace,allocateInfo);
		BigDecimal fcaInMarketCur = allocateInfo.getFcaPrice().multiply(exchangeRate).setScale(6,RoundingMode.HALF_UP).negate();
		BigDecimal profitShare = this.calculateProfitShareReturn(pretaxPrincipalPrice,msdcRetainment,
				marketplaceFee,nonRefundableMF,fulfillmentFee,ssdcRetainment,fcaInMarketCur);
		BigDecimal refundFee = BigDecimal.ZERO;
		return new DrsTransactionLineItemSourceSvcImpl(marketplace.getCurrency(),pretaxPrincipalPrice,
				msdcRetainment,marketplaceFee,nonRefundableMF,fulfillmentFee,ssdcRetainment,
				fcaInMarketCur,profitShare,refundFee);
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

	private List<DrsTransactionLineItem> createClawbackTransactionSettleableItemList(
			DrsTransaction dt,SkuShipmentAllocationInfo allocationInfo) {

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
		BigDecimal ms2ssUipUsd = ms2ssUipInMarketSideCurrency.multiply(exchangeRate).setScale(6, RoundingMode.HALF_UP).negate();
		BigDecimal ms2ssUpa = this.getMs2ssUnitPurchaseAlwnc(allocationInfo.getDdpAmount(),ms2ssUipUsd);

		int lineSeq=1;
		List<DrsTransactionLineItem> items = new ArrayList<>();
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,ssdcKcode, TransactionLineType.MS2SS_UNIT_DDP_REFUND.getName(),                            allocationInfo.getDdpCurrency().name(),allocationInfo.getDdpAmount().negate()));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,ssdcKcode,splrKcode,TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND.getName(),               allocationInfo.getFcaCurrency().name(),                   unitTotal.negate()));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,ssdcKcode,splrKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION.getName(),         dt.getMarketplace().getCurrency().name(),                  ss2sp_upsa));
		items.add(new DrsTransactionLineItemSvcImpl(lineSeq++,msdcKcode,ssdcKcode,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND.getName(),allocationInfo.getDdpCurrency().name(),                    ms2ssUpa.negate()));
		Assert.isTrue(items.size()>=1,"items.size()>=1");
		return items;
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

	private BigDecimal getSsdcUnitRetainment(BigDecimal salePriceUntaxed,BigDecimal drsRetainmentRate) {
		BigDecimal ssdcRetainmentRate = this.ssdcAllocationRate.multiply(drsRetainmentRate).setScale(6, RoundingMode.HALF_UP);
		return salePriceUntaxed.multiply(ssdcRetainmentRate).setScale(6, RoundingMode.HALF_UP);
	}

	private BigDecimal getMsdcUnitRetainment(BigDecimal salePriceUntaxed,BigDecimal drsRetainmentRate) {
		BigDecimal msdcRetainmentRate = this.msdcAllocationRate.multiply(drsRetainmentRate).setScale(6, RoundingMode.HALF_UP);
		return salePriceUntaxed.multiply(msdcRetainmentRate).setScale(6, RoundingMode.HALF_UP);
	}


}
