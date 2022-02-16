package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.inventory.SkuShipmentStockAllocator;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.v1.model.impl.accounting.NonProcessedMarketSideTransactionImpl;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;



import com.kindminds.drs.core.biz.strategy.MarketSideTransactionProcessor;
import com.kindminds.drs.api.v1.model.accounting.NonProcessedMarketSideTransaction;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.data.access.usecase.accounting.ProcessMarketSideTransactionDao;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import com.kindminds.drs.enums.Settlement;
import com.kindminds.drs.service.helper.MarketSideTransactionHelper;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.util.SettlementPeriodHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;


//@Service
public class ProcessMarketSideTransactionUcoImpl {
	
	@Autowired private ProcessMarketSideTransactionDao dao;
	@Autowired private SkuShipmentStockAllocator stockAllocator;
	@Autowired private MarketSideTransactionHelper helper;

	@Autowired private SettlementPeriodHelper settlementPeriodHelper;
	@Autowired private SettlementPeriodDao settlementPeriodRepo;
	@Autowired private MailUtil mailUtil;

	private static final String MAIL_ADDRESS_NOREPLY = "DRS.network <drs-noreply@tw.drs.network>";
	private static final String[] ACCOUNT_MANAGERS = {"account.managers@tw.drs.network"};
	private static final String[] SOFTWARE_ENGINEERS = {"software.engineering@drs.network"};
	private static final String REQUEST_TOO_MUCH = "Request too much.";
	
	private List<String> ignoreMerchantOrderIdPrefixs = Arrays.asList(
			"USTTS","RUSTTS",
			"sb","Rsb",
			"ebay","Rebay",
			"eBay","ReBay");

//	@Override
	public List<SettlementPeriod> getSettlementPeriodList() {
		List<Object []>  columnsList =  this.dao.querySettlementPeriodList();

		List<SettlementPeriod> listToReturn = new ArrayList<SettlementPeriod>();
		for(Object[] columns:columnsList){
			int id =(int)columns[0];
			Date start =(Date)columns[1];
			Date end =(Date)columns[2];
			listToReturn.add(new SettlementPeriodImpl(id, start, end));
		}

		return listToReturn;
	}

//	@Override
	public String collectMarketSideTransactions(String settlementPeriodId) {

		if(!StringUtils.hasText(settlementPeriodId)) return "Settlement period error.";
		int periodId = Integer.parseInt(settlementPeriodId);
		Date dateStartPoint = this.settlementPeriodHelper.getPeriodStart(periodId);
		Date dateEndPoint = this.settlementPeriodHelper.getPeriodEnd(periodId);
		Date dateLatestDrsSettlementEnd = this.dao.queryLatestStatementPeriodEndDateUtc();
		Assert.isTrue(dateStartPoint.compareTo(dateLatestDrsSettlementEnd)==0,"dateStartPoint.compareTo(dateLatestDrsSettlementEnd)==0");
		Assert.isTrue(dateStartPoint.before(dateEndPoint),"dateStartPoint.before(dateEndPoint)");
		int savedCounts = this.collectAndSaveMarketSideTransactions(dateStartPoint, dateEndPoint);

		return savedCounts + " transaction(s) has been generated.\nInterval:"+dateStartPoint+" to "+dateEndPoint;

	}

//	@Override
	public String collectMarketSideTransactionsForTest(Date start, Date end) {
		int foundCounts = this.collectAndSaveMarketSideTransactions(start, end);
		return foundCounts + " transaction(s) has been collected.";
	}

	public String processMarketSideTransactions(String settlementPeriodId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		if(!StringUtils.hasText(settlementPeriodId)) return "Settlement period error.";
		int periodId = Integer.parseInt(settlementPeriodId);
		Date periodStartPoint = this.settlementPeriodHelper.getPeriodStart(periodId);
		Date periodEndPoint = this.settlementPeriodHelper.getPeriodEnd(periodId);
		Date dateLatestDrsSettlementEnd = this.dao.queryLatestStatementPeriodEndDateUtc();
		Assert.isTrue(periodStartPoint.compareTo(dateLatestDrsSettlementEnd)==0,"periodStartPoint.compareTo(dateLatestDrsSettlementEnd)==0");

		Set<String> skusOutOfStock = new TreeSet<>();
		List<Object []> columnsList = this.dao.queryNonProcessedTransactionList("kcode");

		List<NonProcessedMarketSideTransaction> nonProcessedTransactionList = new ArrayList<>();
		for(Object[] columns:columnsList){
			Integer id = (Integer)columns[0];
			Date transactionDate = (Date)columns[1];
			String type = (String)columns[2];
			String source = (String)columns[3];
			String sourceId = (String)columns[4];
			String sku = (String)columns[5];
			String description = (String)columns[6];
			String eMsg = (String)columns[7];
			String st = (String)columns[8];
			nonProcessedTransactionList.add(new NonProcessedMarketSideTransactionImpl(id,transactionDate,type,source,sourceId,
					sku,description , eMsg,st) );
		}


//		for(MarketSideTransaction nonProcessedTransaction:nonProcessedTransactionList){
//			try {
//				Assert.isTrue(!nonProcessedTransaction.getTransactionDate().before(periodStartPoint),
//						"!nonProcessedTransaction.getTransactionDate().before(periodStartPoint)");
//				Assert.isTrue(nonProcessedTransaction.getTransactionDate().before(periodEndPoint),
//						"nonProcessedTransaction.getTransactionDate().before(periodEndPoint)");
//				this.processTransaction(periodStartPoint,periodEndPoint,nonProcessedTransaction,false);
//			} catch(Exception e) {
//
//				if (REQUEST_TOO_MUCH.equals(e.getMessage())) {
//					skusOutOfStock.add(nonProcessedTransaction.getSku());
//				}
//
//				this.dao.deleteMarketSideTransactionException(nonProcessedTransaction.getId());
//				this.dao.insertMarketSideTransactionException(nonProcessedTransaction.getId(), e.getMessage(), ExceptionUtils.getStackTrace(e));
//			}
//		}
		stopWatch.stop();

		sendNotificationForRequestedTooMuch(skusOutOfStock);

		return this.generateTimeSpentInfo((long)stopWatch.getTotalTimeSeconds());
	}

	//	@Override
	public List<NonProcessedMarketSideTransaction> getNonProcessedTransactionList() {
		List<Object []>  columnsList = this.dao.queryNonProcessedTransactionList("kcode");

		List<NonProcessedMarketSideTransaction> nonProcessedTransactionList = new ArrayList<>();
		for(Object[] columns:columnsList){
			Integer id = (Integer)columns[0];
			Date transactionDate = (Date)columns[1];
			String type = (String)columns[2];

			String source = (String)columns[3];
			String sourceId = (String)columns[4];
			String sku = (String)columns[5];
			String description = (String)columns[6];
			String eMsg = (String)columns[7];
			String st = (String)columns[8];
			nonProcessedTransactionList.add(new NonProcessedMarketSideTransactionImpl(id,transactionDate,type,source,sourceId,
					sku,description , eMsg,st) );
		}

		return nonProcessedTransactionList;
	}

	//	@Override
	public String deleteNonProcessedTransactions() {
		int deletedRows = this.dao.deleteNonProcessedTransactions();
		return deletedRows + " row(s) have been deleted.";
	}

	public String collectAndProcessMarketSideTransactions() {
		SettlementPeriod period = this.processRecentPeriods(1).get(0);
		if(!this.isMarketSideTransactionProcessed(period)){
			this.collectMarketSideTransactions(String.valueOf(period.getId()));
			this.processMarketSideTransactions(String.valueOf(period.getId()));
			return "DRS transactions have been generated for "+period.getStartDate()+" to "+period.getEndDate();
		}
		return "Transactions exist. Nothing has been started.";
	}


	private int collectAndSaveMarketSideTransactions(Date startPoint,Date endPoint){

		List<MarketSideTransaction> transactions = new ArrayList<MarketSideTransaction>();

		List<Object []> columnsList = this.dao.queryAmazonOrdersRefunds(startPoint,endPoint,this.ignoreMerchantOrderIdPrefixs, null);
		List<MarketSideTransaction> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			Date transactionDate = (Date)columns[0];
			String type = (String)columns[1];
			String source = (String)columns[2];
			String sourceId = (String)columns[3];
			String sku = (String)columns[4];
		//	resultList.add(new MarketSideTransactionImpl(0,transactionDate,type,source,sourceId,sku));
		}

		transactions.addAll(resultList);

		columnsList = this.dao.queryAmazonOtherTransactions(startPoint,endPoint,this.ignoreMerchantOrderIdPrefixs, null);
		List<MarketSideTransaction> resultList2 = new ArrayList<>();
		for(Object[] columns:columnsList){
			Date transactionDate = (Date)columns[0];
			String type = (String)columns[1];
			String source = (String)columns[2];
			String sourceId = (String)columns[3];
			String sku = (String)columns[4];
			String description = (String)columns[5];
			//resultList2.add(new MarketSideTransactionImpl(0,transactionDate,type,source,sourceId,sku,description));
		}

		transactions.addAll(resultList2);

		//Return
		transactions.addAll(this.findReturnToSellableTransactionsFromFba(startPoint,endPoint));

		transactions.addAll(this.findEbayTransactions(startPoint,endPoint));
		transactions.addAll(this.findShopifyOrderTransactions(startPoint,endPoint));

		columnsList =  this.dao.queryFbaReturnToSupplierTransactions(startPoint,endPoint, null);
		List<MarketSideTransaction> resultList3 = new ArrayList<>();
		for(Object[] columns:columnsList){
			Date date = (Date)columns[0];
			String source = (String)columns[1];
			String sku = (String)columns[2];
			String sellbackType = (String)columns[3];
			if ("TAIWAN".equalsIgnoreCase(sellbackType)) {
				sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURNS_TAIWAN.getTextValue();
			} else if ("OTHER".equalsIgnoreCase(sellbackType)) {
				sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURNS_OTHER.getTextValue();
			} else if ("DISPOSE".equalsIgnoreCase(sellbackType)) {
				sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURNS_DISPOSE.getTextValue();
			} else {
				sellbackType = DrsTransaction.DrsTransactionType.FBA_RETURN_TO_SUPPLIER.getTextValue();
			}
			//resultList3.add(new MarketSideTransactionImpl(null,date,sellbackType,source,null,sku));
		}
		transactions.addAll(resultList3);

		this.sortTransactionListByDateAsc(transactions);

		return this.dao.insertTransactions(transactions);
	}
	
	private List<MarketSideTransaction> findShopifyOrderTransactions(Date startPoint,Date endPoint) {
		Marketplace marketplace = Marketplace.TRUETOSOURCE;
		String type = "Order";

		List<Object[]> columnsList = this.dao.queryShopifyOrderIdToTransactionDateMap(marketplace,type,startPoint,endPoint, null);

		List<MarketSideTransaction> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			Date transactionDate = (Date)columns[0];
			String sourceId = (String)columns[1];
			String sku = (String)columns[2];
			//resultList.add(new MarketSideTransactionImpl(0,transactionDate,type,marketplace.getName(),sourceId,sku));
		}

		return resultList;
	}
	
	private List<MarketSideTransaction> findEbayTransactions(Date startPoint, Date endPoint) {

		List<Object []> columnsList =  this.dao.queryEbayTransactionInfos(startPoint, endPoint, "Order", null);

		List<MarketSideTransaction> marketSideTransactions = new ArrayList<>();
		for(Object[] columns:columnsList){
			Date date = (Date)columns[0];
			String source = (String)columns[1];
			String sourceId = (String)columns[2];
			String sku = (String)columns[3];
			//marketSideTransactions.add(new MarketSideTransactionImpl(null,date,"Order",source,sourceId,sku));
		}

		return marketSideTransactions;

	}
	
	private List<MarketSideTransaction> findReturnToSellableTransactionsFromFba(Date startPoint,
																				Date endPoint){

		String type = "FBA Return To Sellable";
		Settlement.AmazonReturnReportDetailedDisposition targetDisposition = Settlement.AmazonReturnReportDetailedDisposition.SELLABLE;
		Settlement.AmazonReturnReportLineStatus targetStatus = Settlement.AmazonReturnReportLineStatus.RETURNED;
		List<Object []>  columnsList = this.dao.queryFbaTransactions(startPoint,endPoint,type,targetDisposition,targetStatus, null);

		List<MarketSideTransaction> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			Date transactionDate = (Date)columns[0];
			String source = (String)columns[1];
			String sourceId = (String)columns[2];
			String sku = (String)columns[3];
			//resultList.add(new MarketSideTransactionImpl(0,transactionDate,type,source,sourceId,sku));
		}
		return resultList;
	}

	private void sortTransactionListByDateAsc(List<MarketSideTransaction> transactionList){
		Collections.sort(
				transactionList,
				new Comparator<MarketSideTransaction>(){
					public int compare(MarketSideTransaction e1,MarketSideTransaction e2){ return e1.getTransactionDate().compareTo(e2.getTransactionDate()); }
				}
		);
	}


//	@Override
//	public String processMarketSideTransactions(String settlementPeriodId) {
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		if(!StringUtils.hasText(settlementPeriodId)) return "Settlement period error.";
//		int periodId = Integer.parseInt(settlementPeriodId);
//		Date periodStartPoint = this.settlementPeriodHelper.getPeriodStart(periodId);
//		Date periodEndPoint = this.settlementPeriodHelper.getPeriodEnd(periodId);
//		Date dateLatestDrsSettlementEnd = this.dao.queryLatestStatementPeriodEndDateUtc();
//		Assert.isTrue(periodStartPoint.compareTo(dateLatestDrsSettlementEnd)==0,"periodStartPoint.compareTo(dateLatestDrsSettlementEnd)==0");
//
//		Set<String> skusOutOfStock = new TreeSet<>();
//		List<Object []> columnsList = this.dao.queryNonProcessedTransactionList();
//
//		List<NonProcessedMarketSideTransaction> nonProcessedTransactionList = new ArrayList<>();
//		for(Object[] columns:columnsList){
//			Integer id = (Integer)columns[0];
//			Date transactionDate = (Date)columns[1];
//			String type = (String)columns[2];
//			String source = (String)columns[3];
//			String sourceId = (String)columns[4];
//			String sku = (String)columns[5];
//			String description = (String)columns[6];
//			String eMsg = (String)columns[7];
//			String st = (String)columns[8];
//			nonProcessedTransactionList.add(new NonProcessedMarketSideTransactionImpl(id,transactionDate,type,source,sourceId,
//					sku,description , eMsg,st) );
//		}
//
//
//		for(MarketSideTransaction nonProcessedTransaction:nonProcessedTransactionList){
//			try {
//				Assert.isTrue(!nonProcessedTransaction.getTransactionDate().before(periodStartPoint),
//                        "!nonProcessedTransaction.getTransactionDate().before(periodStartPoint)");
//				Assert.isTrue(nonProcessedTransaction.getTransactionDate().before(periodEndPoint),
//                        "nonProcessedTransaction.getTransactionDate().before(periodEndPoint)");
//				this.processTransaction(periodStartPoint,periodEndPoint,nonProcessedTransaction,false);
//			} catch(Exception e) {
//
//				e.printStackTrace();
//
//				if (REQUEST_TOO_MUCH.equals(e.getMessage())) {
//					skusOutOfStock.add(nonProcessedTransaction.getSku());
//				}
//
//				this.dao.deleteMarketSideTransactionException(nonProcessedTransaction.getId());
//				this.dao.insertMarketSideTransactionException(nonProcessedTransaction.getId(), e.getMessage(), ExceptionUtils.getStackTrace(e));
//			}
//		}
//		stopWatch.stop();
//
//
//		sendNotificationForRequestedTooMuch(skusOutOfStock);
//
//		return this.generateTimeSpentInfo((long)stopWatch.getTotalTimeSeconds());
//	}


	private void sendNotificationForRequestedTooMuch(Set<String> skusOutOfStock) {
		if (!skusOutOfStock.isEmpty()) {
			String subject = "SKUs potentially out of stock after processing transactions";
			StringBuilder body = new StringBuilder()
					.append("<p>Here are the SKUs that encountered the Requested Too Much exception.</p>");
			for (String sku : skusOutOfStock) {
				body.append(sku).append("<br>");
			}
			body.append("<br>").append(mailUtil.getSignature(MailUtil.SignatureType.NO_REPLY));

			this.mailUtil.SendMimeWithBcc(ACCOUNT_MANAGERS, SOFTWARE_ENGINEERS, MAIL_ADDRESS_NOREPLY, subject, body.toString());
		}
	}

//	@Override
	public String processMarketSideTransactions(String settlementPeriodId,
												int transactionId ) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		if(!StringUtils.hasText(settlementPeriodId)) return "Settlement period error.";
		int periodId = Integer.parseInt(settlementPeriodId);
		Date periodStartPoint = this.settlementPeriodHelper.getPeriodStart(periodId);
		Date periodEndPoint = this.settlementPeriodHelper.getPeriodEnd(periodId);
		Date dateLatestDrsSettlementEnd = this.dao.queryLatestStatementPeriodEndDateUtc();

		//Assert.isTrue(periodStartPoint.compareTo(dateLatestDrsSettlementEnd)==0,
		// "periodStartPoint.compareTo(dateLatestDrsSettlementEnd)==0");

//		Set<String> skusOutOfStock = new TreeSet<>();
		List<Object []>  columnsList =
				this.dao.queryNonProcessedTransactionList(transactionId);

		List<NonProcessedMarketSideTransaction> nonProcessedTransactionList = new ArrayList<>();

		for(Object[] columns:columnsList){
			Integer id = (Integer)columns[0];
			Date transactionDate = (Date)columns[1];
			String type = (String)columns[2];
			String source = (String)columns[3];
			String sourceId = (String)columns[4];
			String sku = (String)columns[5];
			String description = (String)columns[6];
			String eMsg = (String)columns[7];
			String st = (String)columns[8];
			nonProcessedTransactionList.add(new NonProcessedMarketSideTransactionImpl(id,transactionDate,type,source,sourceId
					,sku,description , eMsg,st));
		}


//		for(MarketSideTransaction nonProcessedTransaction:nonProcessedTransactionList){
//			try {
//				/*
//				Assert.isTrue(!nonProcessedTransaction.getTransactionDate().before(periodStartPoint),
//						"!nonProcessedTransaction.getTransactionDate().before(periodStartPoint)");
//				Assert.isTrue(nonProcessedTransaction.getTransactionDate().before(periodEndPoint),
//						"nonProcessedTransaction.getTransactionDate().before(periodEndPoint)");
//						*/
//
//				this.processTransaction(periodStartPoint,periodEndPoint,nonProcessedTransaction,true);
//
//			} catch(Exception e) {
//				e.printStackTrace();
////				if (REQUEST_TOO_MUCH.equals(e.getMessage())) {
////					skusOutOfStock.add(nonProcessedTransaction.getSku());
////				}
//				this.dao.deleteMarketSideTransactionException(nonProcessedTransaction.getId());
//				this.dao.insertMarketSideTransactionException(nonProcessedTransaction.getId(), e.getMessage(), ExceptionUtils.getStackTrace(e));
//			}
//		}
		stopWatch.stop();

//		sendNotificationForRequestedTooMuch(skusOutOfStock);

		return this.generateTimeSpentInfo((long)stopWatch.getTotalTimeSeconds());
	}

	private String generateTimeSpentInfo(long totalSeconds){
		long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds);
		long seconds = totalSeconds%60;
		return "Total time spent: "+minutes+" mins "+seconds+" secs.";
	}
	
	@Transactional("transactionManager")
	private List<Integer> processTransaction(Date periodStart,Date periodEnd,MarketSideTransaction transaction,
											 boolean isTest){

		Assert.notNull(transaction.getType(),"transaction.getType()");

		if(transaction.getType().equals("Order")&&transaction.getSourceId().equals("112-8687629-3146667")) {
			this.dao.setTransactionProcessed(transaction.getId(),true);
			return null;
		}

		if(transaction.getType().equals("Refund")&&transaction.getSourceId().equals("111-1732008-6192200")) {
			this.dao.setTransactionProcessed(transaction.getId(),true);
			return null;
		}

		if(transaction.getType().equals("Refund")&&transaction.getSourceId().equals("111-2650759-1619429")) {
			this.dao.setTransactionProcessed(transaction.getId(),true);
			return null;
		}

		MarketSideTransactionProcessor processor = this.helper.getProcessor(transaction);


		List<SkuShipmentAllocationInfo> allocationInfos =
				this.getSkuShipmentAllocationInfos(processor, transaction);

		//((NonProcessedMarketSideTransactionImpl)transaction).setAllocationInfos(allocationInfos);

		List<Integer> resultDtIdList = processor.process(periodStart,periodEnd,transaction);

		if (!"FREE_REPLACEMENT_REFUND_ITEMS".equals(transaction.getDescription())) {
			this.updateSkuStockQuantity(transaction.getAllocationInfos());
		}

		if(!isTest) this.dao.setTransactionProcessed(transaction.getId(),true);

		return resultDtIdList;
	}
	
	private List<SkuShipmentAllocationInfo> getSkuShipmentAllocationInfos(MarketSideTransactionProcessor processor,
																		  MarketSideTransaction transaction){
		if(processor.needAllocationInfos()){
			Integer totalQuantityPurchased = processor.getTotalQuantityPurchased(transaction);
			Marketplace sourceMarketplace = Marketplace.fromName(transaction.getSource());
			Country unsDestinationCountry = sourceMarketplace.getUnsDestinationCountry();
//			System.out.println("sourceMarketplace: " + sourceMarketplace);
//			System.out.println("unsDestinationCountry: " + unsDestinationCountry);

			return this.stockAllocator.requestAllocations(unsDestinationCountry, transaction,totalQuantityPurchased);
		}
		return null;
	}


	private void updateSkuStockQuantity(List<SkuShipmentAllocationInfo> skuShipmentAllocationInfos ){

		// check update stock qty
		if(skuShipmentAllocationInfos==null) return;
		for(SkuShipmentAllocationInfo allocationInfo:skuShipmentAllocationInfos){
			this.stockAllocator.increaseStockQuantity(allocationInfo.getUnsName(),allocationInfo.getIvsName(), allocationInfo.getSku(), 1);
		}
	}


	private List<SettlementPeriod> processRecentPeriods(int counts) {
		List<Object []> columnsList = this.settlementPeriodRepo.queryRecentPeriods(counts);
		List<SettlementPeriod> periods = new ArrayList<SettlementPeriod>();
		for(Object[] columns:columnsList){
			int id =(int)columns[0];
			Date start =(Date)columns[1];
			Date end =(Date)columns[2];
			periods.add(new SettlementPeriodImpl(id, start, end));
		}

		return periods;
	}

//	@Override

	private boolean isMarketSideTransactionProcessed(SettlementPeriod period){
		return this.dao.existMarketSideTransaction(period.getStartPoint(), period.getEndPoint());
	}

}
