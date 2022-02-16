package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.*;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import com.kindminds.drs.api.data.access.usecase.accounting.DoSs2spSettlementDao;
import com.kindminds.drs.api.usecase.accounting.DoSs2spSettlementUco;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.BillStatement.BillStatementLineItem;
import com.kindminds.drs.api.v1.model.close.BillStatementLineItemCustomerCaseExemptionInfo;
import com.kindminds.drs.api.v1.model.close.BillStatementProfitShareItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSellBackReport;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementImpl;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import com.kindminds.drs.v1.model.impl.factory.Ss2spStatementReportFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service("statementClose")
public class DoSs2spSettlementUcoImpl implements DoSs2spSettlementUco {

	@Autowired private DoSs2spSettlementDao dao;
	@Autowired private CompanyDao companyRepo;
	@Autowired private CurrencyDao currencyRepo;
	@Autowired private SettlementPeriodDao settlementPeriodRepo;
	@Autowired private ApplicationContext appContext;
	
	private static final int versionNumber = 4;
	private int processSize = 1000;
	private static final String statementNamePrefix = "STM";
	private static final String statementNamePrefixForDraft = "DFT-STM";
	private final BigDecimal freeRateToProductBaseRevenue = new BigDecimal("0.002");
	
	@Override @Transactional("transactionManager")
	public int doSettlementForDraft(String supplierKcode,String utcDateStart,String utcDateEnd){
		Date expectStartPoint = this.fromDateStrToUtcTimestamp(utcDateStart);
		Date actualStartPoint = this.getPeriodStartDate(supplierKcode, expectStartPoint);
		Date actualEndPoint = new Date(this.fromDateStrToUtcTimestamp(utcDateEnd).getTime()+86400000);
		Assert.isTrue(actualEndPoint.after(actualStartPoint),"Start date should be earlier than end date.");
		Assert.isTrue(actualEndPoint.before(new Date()),"End date should be earlier than now.");
		return this.doSettlement(supplierKcode, actualStartPoint, actualEndPoint, BillStatementType.DRAFT);
	}
	
	@Override @Transactional("transactionManager")
	public int doSettlementForOfficial(String supplierKcode, Date expectPeriodStart, Date expectPeriodEnd) {
		Date actualPeriodStartDate = this.getPeriodStartDate(supplierKcode,expectPeriodStart);
		Date actualPeriodEndDate = expectPeriodEnd;
		Assert.isTrue(actualPeriodEndDate.after(actualPeriodStartDate),"actualPeriodEndDate.after(actualPeriodStartDate)");
		return this.doSettlement(supplierKcode, actualPeriodStartDate, actualPeriodEndDate, BillStatementType.OFFICIAL);
	}
	
	@Override
	public int doSettlement(String supplierKcode, Date start, Date end, BillStatementType bsType) {
		String handlerKcode = this.companyRepo.queryHandlerKcode(supplierKcode);
		Integer nextStmntSeq = this.dao.queryMaxStatementSeq(supplierKcode,bsType)+1;
		String stmntName = this.generateStatementName(supplierKcode,nextStmntSeq,bsType);
		int statementId = this.dao.insertStatement(nextStmntSeq,stmntName,supplierKcode, handlerKcode, start, end, versionNumber, bsType);
		this.generateDetail(bsType, statementId,start,end,supplierKcode,handlerKcode);
		this.doBalance(bsType,statementId,start,end);
		return statementId;
	}
	
	private void generateDetail(BillStatementType type,int statementId,Date start,Date end,String supplierKcode,String handlerKcode){
		int processedLineCounts = 0;
		processedLineCounts = this.generateProfitShareItems(type, statementId, processedLineCounts, supplierKcode, handlerKcode,start,end);
		processedLineCounts = this.generateInventoryPaymentRefundItems(statementId, processedLineCounts, supplierKcode, type);
		processedLineCounts = this.generateInventorySellBackItems(type, statementId, processedLineCounts, supplierKcode);
		processedLineCounts = this.generateOtherItems(type, statementId, processedLineCounts, supplierKcode);
		this.doProfitShareCurrencyExchage(type,statementId,start,end,handlerKcode);
		this.doNonProfitShareSubtotal(statementId, handlerKcode, type);
		processedLineCounts = this.generateProfitShareTax(type, statementId, processedLineCounts, supplierKcode, handlerKcode);
	}
	
	private void doProfitShareCurrencyExchage(BillStatementType bsType,int statementId,Date start,Date end,String handlerKcode) {
		Currency stmntCurrency = currencyRepo.queryCompanyCurrency(handlerKcode);
		List<BillStatementLineItem> items = this.dao.getAllProfitShareItems(statementId, bsType);
		Map<Country,BigDecimal> countryToRoundedSubtotalMap = this.getCountryToRoundedSubtotal(items);
		int lineSeq = 1;
		for(Country country:countryToRoundedSubtotalMap.keySet()){
			Assert.isTrue(country.getCurrency()!=stmntCurrency,"country.getCurrency()!=stmntCurrency" + country.getCurrency() + stmntCurrency);
			BigDecimal sourceAmount = countryToRoundedSubtotalMap.get(country);
			BigDecimal exchangeRate = this.currencyRepo.queryExchangeRate(start, end, country.getCurrency(), stmntCurrency,DrsConstants.interbankRateForMs2ssSettlement);
			BigDecimal unroundedStmntSubtotal = sourceAmount.multiply(exchangeRate);
			BigDecimal roundedSubtotal = unroundedStmntSubtotal.setScale(stmntCurrency.getScale(), RoundingMode.HALF_UP);
			this.dao.insertProfitShareItem(bsType,statementId,lineSeq++,country,country.getCurrency(),sourceAmount,stmntCurrency,roundedSubtotal,exchangeRate);
		}
	}
	
	private Map<Country,BigDecimal> getCountryToRoundedSubtotal(List<BillStatementLineItem> items){
		Map<Country,BigDecimal> countryToSubtotalMap = new TreeMap<Country,BigDecimal>();
		for(BillStatementLineItem item:items){
			Country country = Country.valueOf(item.getCountry());
			Assert.isTrue(country.getCurrency()==Currency.valueOf(item.getOriginalCurrency()),"country.getCurrency()==Currency.valueOf(item.getOriginalCurrency())"
					+ country.getCurrency() + Currency.valueOf(item.getOriginalCurrency()));
			if(countryToSubtotalMap.containsKey(country))
				countryToSubtotalMap.put(country, countryToSubtotalMap.get(country).add(item.getOriginalAmount()));
			else
				countryToSubtotalMap.put(country, item.getOriginalAmount());
		}
		// Round to original currency scale
		for(Country srcCountry:countryToSubtotalMap.keySet()){
			Currency srcCountryCurrency = srcCountry.getCurrency();
			BigDecimal unroundedAmount = countryToSubtotalMap.get(srcCountry);
			countryToSubtotalMap.put(srcCountry,unroundedAmount.setScale(srcCountryCurrency.getScale(), RoundingMode.HALF_UP));
		}
		return countryToSubtotalMap;
	}

	private void doNonProfitShareSubtotal(int sid, String handlerKcode, BillStatementType bsType) {
		Currency stmntCurrency = currencyRepo.queryCompanyCurrency(handlerKcode);
		List<BillStatementLineItem> items = this.dao.queryNonProfitShareStatementLineItems(bsType,sid);
		for(BillStatementLineItem item:items){
			Assert.isTrue(stmntCurrency==Currency.valueOf(item.getOriginalCurrency()),"stmntCurrency==Currency.valueOf(item.getOriginalCurrency())");
			BigDecimal stmntAmount = item.getOriginalAmount().setScale(stmntCurrency.getScale(), RoundingMode.HALF_UP);
			this.dao.setLineItemStatementCurrencyAndAmount(bsType, sid, item.getLineSeq(), stmntCurrency, stmntAmount);
		}
	}

	private Date fromDateStrToUtcTimestamp(String dateStr){
		return DateHelper.toDate(dateStr+" UTC", "yyyy-MM-dd z");
	}

	private Date getPeriodStartDate(String supplierKcode,Date expectPeriodStart){
		Date previousPeriodEnd = this.dao.queryPreviousPeriodEndDate(supplierKcode);
		if(previousPeriodEnd==null){
			return expectPeriodStart;
		}
		return previousPeriodEnd;
	}
	
	private String generateStatementName(String supplierKcode,Integer nextStmntSeq, BillStatementType bsType){
		if(bsType==BillStatementType.DRAFT) return statementNamePrefixForDraft+"-"+supplierKcode+"-"+nextStmntSeq;
		if(bsType==BillStatementType.OFFICIAL) return statementNamePrefix+"-"+supplierKcode+"-"+nextStmntSeq;
		Assert.isTrue(false,"Type is not draft nor official");
		return null;
	}
	
	private int generateProfitShareItems(BillStatementType bsType, int statementId, int currentIndex, String supplierKcode, String handlerKcode, Date start, Date end) {
		currentIndex = this.doDrsTransactionRelatedItem(bsType,currentIndex,statementId,supplierKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION);
		currentIndex = this.doDrsTransactionRelatedItem(bsType,currentIndex,statementId,supplierKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION);
		currentIndex = this.doDrsTransactionRelatedItem(bsType,currentIndex,statementId,supplierKcode,TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE);
		currentIndex = this.doImportDutyRelatedItem(bsType, currentIndex, statementId, TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY);
		currentIndex = this.doInternationalTransactionItems(bsType,currentIndex,statementId,CashFlowDirection.MS2SP);
		currentIndex = this.doInternationalTransactionItems(bsType,currentIndex,statementId,CashFlowDirection.SP2MS);
//		currentIndex = this.doCustomerCareCaseRelated(bsType, currentIndex, statementId, supplierKcode, start, end);
		return currentIndex;
	}

	private int generateInventoryPaymentRefundItems(int statementId, int currentIndex, String supplierKcode, BillStatementType bsType) {
		currentIndex = this.doPurchaseOrderRelatedItem(bsType, currentIndex, statementId, supplierKcode,TransactionLineType.SS2SP_UNIT_INVENTORY_PAYMENT);
		currentIndex = this.doPurchaseOrderRelatedItem(bsType, currentIndex, statementId, supplierKcode,TransactionLineType.SS2SP_UNIT_INVENTORY_REFUND);
		return currentIndex;
	}
	
	private int generateInventorySellBackItems(BillStatementType bsType, int statementId, int currentIndex,String supplierKcode) {
		currentIndex = this.doPurchaseOrderRelatedItem(bsType,currentIndex,statementId,supplierKcode,TransactionLineType.SS2SP_UNIT_INVENTORY_SELL_BACK);
		currentIndex = this.doPurchaseOrderRelatedItem(bsType,currentIndex,statementId,supplierKcode,TransactionLineType.SS2SP_INVENTORY_SELL_BACK_TAIWAN);
		currentIndex = this.doPurchaseOrderRelatedItem(bsType,currentIndex,statementId,supplierKcode,TransactionLineType.SS2SP_INVENTORY_SELL_BACK_OTHER);
		currentIndex = this.doPurchaseOrderRelatedItem(bsType,currentIndex,statementId,supplierKcode,TransactionLineType.SS2SP_INVENTORY_SELL_BACK_DISPOSE);
		currentIndex = this.doPurchaseOrderRelatedItem(bsType,currentIndex,statementId,supplierKcode,TransactionLineType.SS2SP_INVENTORY_SELL_BACK_RECOVERY);
		return currentIndex;
	}

	private int generateOtherItems(BillStatementType bsType, int statementId, int currentIndex, String supplierKcode) {
		currentIndex = this.doDomesticTransaction(bsType, currentIndex, statementId, supplierKcode);
		return currentIndex;
	}
	
	private int generateProfitShareTax(BillStatementType bsType, int statementId, int currentIndex, String supplierKcode, String handlerKcode) {
		Currency stmntCurrency = currencyRepo.queryCompanyCurrency(handlerKcode);
		BigDecimal totalUntaxed = this.dao.queryProfitShareItemStatementAmountUntaxedTotal(bsType, statementId);
		if(totalUntaxed==null) return currentIndex;
		BigDecimal vatRate = this.dao.queryVatRate(handlerKcode);
		BigDecimal vat = totalUntaxed.multiply(vatRate).setScale(stmntCurrency.getScale(),RoundingMode.HALF_UP);
		return this.dao.insertProfitShareTax(statementId, currentIndex, stmntCurrency, vat, bsType);
	}
	
//	private int doCustomerCareCaseRelated(BillStatementType bsType, int currentIndex, int statementId, String supplierKcode, Date start, Date end) {
//		this.doCurrencyExchangeRateForMsdcReplyMessage(bsType,statementId);
//		currentIndex = this.doCustomerCareCaseItem(bsType, currentIndex, statementId, supplierKcode, start, end);
//		if(bsType==BillStatementType.OFFICIAL) this.dao.updateSs2spNameOfCustomerCareCaseMsdcMessage(statementId);
//		return currentIndex;
//	}
	
//	private void doCurrencyExchangeRateForMsdcReplyMessage(BillStatementType type,int statementId){
//		Date start = this.dao.queryPeriodStartDate(type,statementId);
//		Date end = this.dao.queryPeriodEndDate(type,statementId);
//		List<CustomerCaseMsdcMsgChargeInfo> customerCaseMsdcMsgChargeInfoList = this.dao.queryCustomerCaseMsdcMsgChargeInfoList(start,end);
//		for(CustomerCaseMsdcMsgChargeInfo info:customerCaseMsdcMsgChargeInfoList){
//			Assert.isTrue(info.getOriginCurrency()==Currency.USD,info.getOriginCurrency().name());
//			BigDecimal exchangeRate = info.getCountry().getCurrency()==Currency.USD?BigDecimal.ONE:this.currencyRepo.queryExchangeRate(start, end, Currency.USD, info.getCountry().getCurrency(), DrsConstants.interbankRateForCalculatingCustomerCaseMsdcCharge);
//			BigDecimal chargeInActualCurrency = info.getChargeInOriginCurrency().multiply(exchangeRate).setScale(6, RoundingMode.HALF_UP);
//			((CustomerCaseMsdcMsgChargeInfoImpl)info).setActualCurrency(info.getCountry().getCurrency());
//			((CustomerCaseMsdcMsgChargeInfoImpl)info).setChargeInActualCurrency(chargeInActualCurrency);
//		}
//		this.dao.updateCustomerCaseMsdcChargeInActualCurrency(customerCaseMsdcMsgChargeInfoList);
//	}
//	
	private int doDrsTransactionRelatedItem(BillStatementType bsType, int beginIndex, int statementId, String supplierKcode, TransactionLineType type) {
		int startInd = 1, endInd = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = this.dao.doDrsTransactionItem(statementId, beginIndex, supplierKcode, startInd, endInd, type, bsType);
			currentIndex += insertSize;
			if (insertSize < processSize)
				break;
			startInd += processSize;
			endInd += processSize;
		}
		return currentIndex;
	}
	
	private int doImportDutyRelatedItem(BillStatementType bsType, int beginIndex, int statementId, TransactionLineType item){
		int startInd = 1, endInd = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = this.dao.doImportDutyItem(statementId, beginIndex, startInd, endInd, item, bsType);
			currentIndex += insertSize;
			if (insertSize < processSize)
				break;
			startInd += processSize;
			endInd += processSize;
		}
		return currentIndex;
	}
	
	private int doInternationalTransactionItems(BillStatementType bsType, int beginIndex, int statementId, CashFlowDirection cashFlowDirection) {
		int startInd = 1, endInd = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = this.dao.doInternationalTransactionItems(bsType,statementId, beginIndex, startInd,endInd, cashFlowDirection.getKey());
			currentIndex += insertSize;
			if (insertSize < processSize) break;
			startInd += processSize;
			endInd += processSize;
		}
		return currentIndex;
	}
	
//	private int doCustomerCareCaseItem(BillStatementType bsType, int beginIndex, int statementId, String supplierKcode, Date start, Date end ) {
//		int currentIndex = beginIndex;
//		RevenueCalculator revenueCalculator = (RevenueCalculator)SpringAppCtx.get().getBean("revenueCalculator");
//		Map<Country,Map<String,BigDecimal>> countryToProductBaseRevenueMap = revenueCalculator.getRevenueGroupByCountryAndProductBase(start, end, supplierKcode);
//		Map<Country,Map<String,BigDecimal>> countryToProductBaseCustomerFeeMap = this.dao.queryCountryToProductBaseCustomerFeeMap(bsType, statementId);
//		for(Country country:countryToProductBaseCustomerFeeMap.keySet()){
//			for(String productBase:countryToProductBaseCustomerFeeMap.get(country).keySet()){
//				BigDecimal customerFee = countryToProductBaseCustomerFeeMap.get(country).get(productBase);
//				BigDecimal revenue = this.getRevenue(country, productBase, countryToProductBaseRevenueMap);
//				BigDecimal freeAmount = revenue.signum()<=0?BigDecimal.ZERO:revenue.multiply(this.freeRateToProductBaseRevenue);
//				BigDecimal exemption = freeAmount.min(customerFee);
//				Assert.isTrue(exemption.signum()>=0,"exemption.signum()>=0");
//				this.dao.insertCustomerCaseLineItem(bsType,statementId,++currentIndex,country,country.getCurrency(),customerFee.negate(),productBase);
//				if(exemption.signum()>0){
//					this.dao.insertCustomerCaseExemptionAndInfo(bsType,statementId,++currentIndex,country,country.getCurrency(),exemption,productBase,revenue,this.freeRateToProductBaseRevenue,freeAmount,customerFee);
//				}
//			}
//		}
//		return currentIndex;
//	}
	
//	private BigDecimal getRevenue(Country country, String productBase, Map<Country,Map<String,BigDecimal>> countryToProductBaseRevenueMap ){
//		if(countryToProductBaseRevenueMap.containsKey(country)){
//			if(countryToProductBaseRevenueMap.get(country).containsKey(productBase)){
//				return countryToProductBaseRevenueMap.get(country).get(productBase);
//			}
//		}
//		return BigDecimal.ZERO;
//	}
	
	private int doPurchaseOrderRelatedItem(BillStatementType bsType, int beginIndex, int statementId, String supplierKcode, TransactionLineType type){
		int startInd = 1, endInd = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = this.dao.doPurchaseOrderRelatedItem(statementId, beginIndex, supplierKcode, startInd, endInd, type, bsType);
			currentIndex += insertSize;
			if (insertSize < processSize)
				break;
			startInd += processSize;
			endInd += processSize;
		}
		return currentIndex;
	}
	
	private int doDomesticTransaction(BillStatementType bsType, int beginIndex, int statementId, String supplierKcode){
		int startInd = 1, endInd = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = this.dao.doDomesticTransaction(statementId, beginIndex, supplierKcode, startInd, endInd,"SS_ServiceSaleForSP",bsType);
			currentIndex += insertSize;
			if (insertSize < processSize) break;
			startInd += processSize;
			endInd += processSize;
		}
		return currentIndex;
	}


	private void doBalance(BillStatementType bsType,int statementId,Date start, Date end) {

		// 1. retrieve statement
		BillStatementImpl statement = (BillStatementImpl)this.dao.getStatement(statementId,bsType);
		Currency stmntCurrency = currencyRepo.queryCompanyCurrency(statement.getReceiverKcode());
		// 2. process Remittence
		BigDecimal amountRmtIsur2Rcvr = BigDecimal.ZERO; 
		Object[] rmtIsurToRcvr = this.dao.queryCurrencyAndTotalRemittanceFromIsurToRcvr(start, end, statement.getIssuerKcode(), statement.getReceiverKcode());
		if(rmtIsurToRcvr!=null){
			Assert.isTrue(stmntCurrency==Currency.valueOf((String)rmtIsurToRcvr[0]),"stmntCurrency==Currency.valueOf((String)rmtIsurToRcvr[0])");
			amountRmtIsur2Rcvr=(BigDecimal)rmtIsurToRcvr[1];
		}
		BigDecimal amountRmtRcvr2Isur = BigDecimal.ZERO;
		Object[] rmtRcvrToIsur = this.dao.queryCurrencyAndTotalRemittanceFromRcvrToIsur(start, end, statement.getIssuerKcode(), statement.getReceiverKcode());
		if(rmtRcvrToIsur!=null){
			Assert.isTrue(stmntCurrency==Currency.valueOf((String)rmtRcvrToIsur[0]),"stmntCurrency==Currency.valueOf((String)rmtRcvrToIsur[0])");
			amountRmtRcvr2Isur=(BigDecimal)rmtRcvrToIsur[1];
		}
		
		BigDecimal previousBalance = this.dao.queryPreviousBalance(statementId,bsType);

		BigDecimal profitShareUnroundedUntaxedTotal =
				this.dao.queryProfitShareDestinationUntaxedTotal(bsType,statementId);
		BigDecimal profitShareRoundedUntaxedTotal =
				profitShareUnroundedUntaxedTotal.setScale(stmntCurrency.getScale(), RoundingMode.HALF_UP);

		BigDecimal profitShareTax = this.dao.queryProfitShareTax(bsType, statementId);
		BigDecimal nonProfitShareTotal = this.dao.queryNonProfitShareTotal(bsType,statementId);


		String factoryBeanId = Ss2spStatementReportFactory.ReportVersion.fromVersionNumber(3).getFactoryBeanId();
		Ss2spStatementReportFactory factory = (Ss2spStatementReportFactory)this.appContext.getBean(factoryBeanId);
		Ss2spSellBackReport sellBackReport = factory.createSellBackReport(bsType,statement.getDisplayName(),stmntCurrency);
		sellBackReport.getTotal();


		BigDecimal total = profitShareRoundedUntaxedTotal.add(profitShareTax).add(nonProfitShareTotal).
				add(new BigDecimal(sellBackReport.getTotalWithTax()));

		BigDecimal balance = total
				.add(previousBalance)
				.add(amountRmtIsur2Rcvr.negate())
				.add(amountRmtRcvr2Isur);

		statement.setPreviousBalance(previousBalance);
		statement.setRemittanceIsurToRcvr(amountRmtIsur2Rcvr);
		statement.setRemittanceRcvrToIsur(amountRmtRcvr2Isur);
		statement.setTotal(total);
		statement.setBalance(balance);
		statement.setCurrency(stmntCurrency);
		this.dao.updateStatement(statement,bsType);
	}

	@Override
	public BillStatement getStatement(BillStatementType type,int id) {
		return dao.getExistingStatement(id,type);
	}

	@Override
	public List<BillStatementProfitShareItem> getProfitShareItems(BillStatementType bsType, int statementId) {
		return this.dao.queryProfitShareItems(bsType, statementId);
	}
	
	@Override
	public void deleteAllDraft() {
		List<String> draftStatementNameList = this.dao.queryDraftStatementNameList();
		for(String name:draftStatementNameList){
			this.dao.deleteDraft(name);
		}
	}

	@Override
	public void deleteDraft(String statementName) {
		this.dao.deleteDraft(statementName);
	}

	@Override @Transactional("transactionManager")
	public int confirmDraft(String statementName) {
		String supplierKcode = this.dao.querySupplierKcodeOfDraft(statementName);
		Date start = this.dao.queryDateStartOfDraft(statementName);
		Date end   = this.dao.queryDateEndOfDraft(statementName);
		this.deleteDraft(statementName);
		return this.doSettlementForOfficial(supplierKcode, start, end);
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
	
	@Override
	public String createDraftForAllSupplier() {
		SettlementPeriod period = this.processRecentPeriods(1).get(0);
		if(!this.isSs2spSettlementDone(period)){
			return this.createDraftForAllSupplier(period.getStartDate(), period.getEndDate());
		}
		return null;
	}
	
	private boolean isSs2spSettlementDone(SettlementPeriod period){
		return this.dao.existSs2spStatement(period.getStartPoint());
	}

	@Override
	public String createDraftForAllSupplier(String utcDateStart, String utcDateEnd) {
		List<String> supplierKcodeList = this.dao.querySupplierKcodeList();
		for(String kcode:supplierKcodeList){
			this.doSettlementForDraft(kcode, utcDateStart, utcDateEnd);
		}
		return supplierKcodeList.size()+" draft statement(s) has been created. ";
	}

	@Override
	public String createDraft(String supplierKcode) {
		SettlementPeriod period = this.processRecentPeriods(1).get(0);
		try{
			this.doSettlementForDraft(supplierKcode, period.getStartDate(), period.getEndDate());
		}
		catch(Exception e){
			return e.getMessage();
		}
		return "The draft statement for " + supplierKcode + " has been successfully created.\n"
				+ "Period Start:\t" + period.getStartDate() + "\n"
				+ "Period End:\t" + period.getEndDate();
	}

	@Override
	public String createDraft(String supplierKcode, String utcDateStart,String utcDateEnd) {
//		if (!StringUtils.hasText(utcDateStart) || !StringUtils.hasText(utcDateEnd)) {
//			SettlementPeriod period = this.processRecentPeriods(1).get(0);
//			utcDateStart = period.getStartDate();
//			utcDateEnd = period.getEndDate();
//		}
		try{
			this.doSettlementForDraft(supplierKcode, utcDateStart, utcDateEnd);
		}
		catch(Exception e){
			return e.getMessage();
		}
		return "The draft statement for " + supplierKcode + " has been successfully created.\n"
				+ "Period Start:\t" + utcDateStart+"\n"
				+ "Period End:\t" + utcDateEnd;
	}

	@Override
	public Map<String,String> getSupplierKcodeToCompanyNameMap() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap("K2");
	}

	@Override 
	public void deleteOfficial(String statementName) {
		Assert.isTrue(Context.getCurrentUser().isDrsUser(),"Auth Failed");
		String supplierKcode = this.dao.querySupplierKcode(BillStatementType.OFFICIAL,statementName);
		Assert.isTrue(statementName.equals(this.dao.queryNewestStatementName(supplierKcode)),"Not Newest");
		this.dao.deleteOfficial(statementName);
	}

	@Override
	public List<BillStatementLineItemCustomerCaseExemptionInfo> getCustomerCaseInfoList(BillStatementType bsType, int statementId) {
		return this.dao.queryCustomerCaseInfoList(bsType,statementId);
	}

	@Override
	public void commitAllDraft() {
		List<String> draftStatementNameList = this.dao.queryDraftStatementNameList();
		for(String statementName:draftStatementNameList){

			String supplierKcode = this.dao.querySupplierKcode(BillStatementType.DRAFT, statementName);
            Date start = this.dao.queryDateStartOfDraft(statementName);
            Date end = this.dao.queryDateEndOfDraft(statementName);
            this.deleteDraft(statementName);
            this.doSettlementForOfficial(supplierKcode, start, end);


		}
	}
	
}
