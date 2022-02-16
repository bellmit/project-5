package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Context;
import com.kindminds.drs.Currency;
import com.kindminds.drs.DrsConstants;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.usecase.accounting.DoMs2ssSettlementUco;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.BillStatement.BillStatementLineItem;
import com.kindminds.drs.api.v1.model.close.Remittance;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.DoMs2ssSettlementDao;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementImpl;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementLineItemImpl;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service("DoMs2ssSettlementUco")
public class DoMs2ssSettlementUcoImpl implements DoMs2ssSettlementUco {
	
	@Autowired private DoMs2ssSettlementDao dao;
	@Autowired private CompanyDao companyRepo;
	@Autowired private CurrencyDao currencyRepo;
	@Autowired private SettlementPeriodDao settlementPeriodRepo;
	
	private int processSize = 1000;
	private Currency statementCurrency=Currency.USD;
	private final String statementNamePrefix = "STM";
	private final String draftStatementNamePrefix = "DFT-STM";
	private static final int versionNumber = 1;
	
	@Override
	public Map<String, String> getSupplierKcodeToCompanyNameMap() {
		return this.companyRepo.queryDrsCompanyKcodeToShortEnUsNameMap();
	}

	@Override @Transactional("transactionManager")
	public int createDraft(String isurKcode, String rcvrKcode,String utcDateStart, String utcDateEnd) {
		Assert.isTrue(!isurKcode.equals(rcvrKcode),"!isurKcode.equals(rcvrKcode)");
		Date expectStartPoint = this.fromDateStrToUtcTimestamp(utcDateStart);
		Date actualStartPoint=this.getActualStartDate(isurKcode,rcvrKcode,expectStartPoint);
		Date actualEndPoint = new Date(this.fromDateStrToUtcTimestamp(utcDateEnd).getTime()+86400000);
		Assert.isTrue(actualEndPoint.after(actualStartPoint),"Start date should be earlier than end date.");
		Assert.isTrue(actualEndPoint.before(new Date()),"End date should be earlier than now.");
		return this.doSettlement(BillStatementType.DRAFT,isurKcode, rcvrKcode, actualStartPoint, actualEndPoint);
	}

	@Override @Transactional("transactionManager")
	public int commitDraft(String statementName) {
		String isurKcode = this.dao.queryIsurKcode(BillStatementType.DRAFT,statementName);
		String rcvrKcode = this.dao.queryRcvrKcode(BillStatementType.DRAFT,statementName);
		Date start = this.dao.queryStartDateOfDraft(statementName);
		Date end   = this.dao.queryEndDateOfDraft(statementName);
		this.deleteDraft(statementName);
		return this.doStatementOfficial(isurKcode,rcvrKcode,start,end);
	}

	@Override
	public void deleteDraft(String statementName) {
		this.dao.delete(BillStatementType.DRAFT,statementName);
	}

	@Override
	public void deleteOfficial(String statementName) {
		Assert.isTrue(Context.getCurrentUser().isDrsUser(),"Auth Failed");
		String isurKcode = this.dao.queryIsurKcode(BillStatementType.OFFICIAL,statementName);
		String rcvrKcode = this.dao.queryRcvrKcode(BillStatementType.OFFICIAL,statementName);
		String newestStatementName = this.dao.queryNewestStatementName(isurKcode, rcvrKcode);
		Assert.isTrue(statementName.equals(newestStatementName),"NOT NEWEST");
		this.dao.delete(BillStatementType.OFFICIAL,statementName);
	}

	@Override @Transactional("transactionManager")
	public int doStatementOfficial(String isurKcode, String rcvrKcode, Date expectDateStart, Date expectDateEnd) {
		Date actualDateStart=this.getActualStartDate(isurKcode,rcvrKcode,expectDateStart);
		Assert.isTrue(expectDateEnd.after(actualDateStart),"expectDateEnd.after(actualDateStart)");
		Date actualDateEnd=expectDateEnd;
		return this.doSettlement(BillStatementType.OFFICIAL,isurKcode, rcvrKcode, actualDateStart, actualDateEnd);
	}
	
	@Override @Transactional("transactionManager")
	public int doStatementForTest(String isurKcode, String rcvrKcode,Date expectDateStart, Date expectDateEnd) {
		Assert.isTrue(expectDateEnd.after(expectDateStart),"expectDateEnd.after(expectDateStart)");
		Assert.isTrue(expectDateEnd.before(new Date()),"expectDateEnd.before(new Date())");
		return this.doSettlement(BillStatementType.OFFICIAL,isurKcode, rcvrKcode, expectDateStart, expectDateEnd);
	}
	
	private Date fromDateStrToUtcTimestamp(String dateStr){
		return DateHelper.toDate(dateStr+" UTC", "yyyy-MM-dd z");
	}
	
	@Override
	public int doSettlement(BillStatementType type,String isurKcode, String rcvrKcode,Date start, Date end) {
		int nextSerialId = this.dao.queryMaxStatementSeq(rcvrKcode,isurKcode,type)+1;
		String newStmntName = this.generateStatementName(isurKcode,rcvrKcode,nextSerialId,type);
		int statementId=this.dao.insertStatement(nextSerialId,newStmntName,rcvrKcode,isurKcode,start,end,versionNumber,type);
		int currentIndex = 0;
		currentIndex = this.doPaymentAndRefund(currentIndex,statementId,isurKcode,rcvrKcode,type);
		currentIndex = this.doPurchaseAllowance(currentIndex,statementId,isurKcode,rcvrKcode,type);
		currentIndex = this.doImportDutyItems(type,currentIndex,statementId);
		currentIndex = this.doInternationalTransactionItem(type,currentIndex,statementId,start,end,isurKcode,rcvrKcode,CashFlowDirection.SP2MS);
		currentIndex = this.doInternationalTransactionItem(type,currentIndex,statementId,start,end,isurKcode,rcvrKcode,CashFlowDirection.MS2SP);
		currentIndex = this.doInternationalTransactionItemMsdcPaymentOnBehalfOfSsdc(type,currentIndex,statementId,start,end,isurKcode,rcvrKcode,CashFlowDirection.SP2MS);
		currentIndex = this.doPaymentOnbehalfImportDuty(type,currentIndex,statementId,isurKcode,rcvrKcode);
		currentIndex = this.doProductInventoryReturn(currentIndex,statementId,type);
		this.assignUnsToLineItemReference(type, statementId);
		this.doCurrencyExchage(type,statementId,start,end);
		this.doBalance(statementId,rcvrKcode,start,end,type);
		return statementId;
	}

	private Date getActualStartDate(String isurKcode,String rcvrKcode,Date expectDateStart) {
		Date previousPreviousEnd = this.dao.queryPreviousPeriodEndDate(isurKcode,rcvrKcode);
		if(previousPreviousEnd==null){
			return expectDateStart;
		}
		return previousPreviousEnd;
	}
	
	private String generateStatementName(String isurKcode,String rcvrKcode,Integer nextStmntSeq,BillStatementType bsType){
		Assert.isTrue(bsType==BillStatementType.DRAFT||bsType==BillStatementType.OFFICIAL,"bsType==BillStatementType.DRAFT||bsType==BillStatementType.OFFICIAL");
		String prefix = bsType==BillStatementType.DRAFT?this.draftStatementNamePrefix:this.statementNamePrefix;
		return prefix+"-"+isurKcode+"-"+rcvrKcode+"-"+nextStmntSeq;
	}
	private int doPaymentAndRefund(int currentIndex, int statementId,String isurKcode, String rcvrKcode,BillStatementType type) {
		currentIndex = this.doShipmentRelatedItem(currentIndex,statementId,TransactionLineType.MS2SS_UNIT_DDP_PAYMENT,type);
		currentIndex = this.doShipmentRelatedItem(currentIndex,statementId,TransactionLineType.MS2SS_UNIT_DDP_REFUND,type);
		return currentIndex;
	}
	
	private int doPurchaseAllowance(int currentIndex, int statementId,String isurKcode, String rcvrKcode,BillStatementType type) {
		currentIndex = this.doShipmentRelatedItem(currentIndex,statementId,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES,type);
		currentIndex = this.doShipmentRelatedItem(currentIndex,statementId,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND,type);
		currentIndex = this.doShipmentRelatedItem(currentIndex,statementId,TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE,type);
		return currentIndex;
	}
	
	private int doProductInventoryReturn(int currentIndex, int statementId, BillStatementType type) {
		currentIndex = this.doProductInventoryReturnItem(currentIndex,statementId,type);
		return currentIndex;
	}
	
	private final List<String> unrelatedToUnsTypeNameList = Arrays.asList(
			TransactionLineType.MS2SS_PURCHASE_ALWNC_STORAGECOSTS.getName(),
			TransactionLineType.MS2SS_PURCHASE_ALWNC_REPLACEMENTPROCESSING.getName(),
			TransactionLineType.MS2SS_PURCHASE_ALWNC_OTHERCONCESSIONTOCUSTOMER_SUBTRACTION.getName()
			);
	
	@SuppressWarnings("unchecked")
	private void assignUnsToLineItemReference(BillStatementType type, int statementId){
		Map<String,String> skuToNewestArrivalUnsNameMap = this.dao.querySkuToNewestArrivalUnsNameMap(type,statementId);
		List<BillStatementLineItemImpl> items = (List<BillStatementLineItemImpl>)((List<?>)this.dao.queryLineItems(type,statementId,this.unrelatedToUnsTypeNameList));
		for (BillStatementLineItemImpl item : items) {
			String unsName = skuToNewestArrivalUnsNameMap.get(item.getProductName());
			Assert.notNull(unsName,"unsName null");
			item.setReference(unsName);
			this.dao.updateStatementLineItemReference(item,type);
		}
	}

	private int doShipmentRelatedItem(int beginIndex,int statementId,TransactionLineType sItem,BillStatementType type) {
		int startIndex = 1, endIndex = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = dao.doShipmentRelatedItem(statementId, beginIndex, startIndex, endIndex,sItem, type);
			currentIndex += insertSize;
			if (insertSize < processSize)
				break;
			startIndex += processSize;
			endIndex += processSize;
		}
		return currentIndex;
	}
	
	private int doImportDutyItems(BillStatementType bsType,int beginIndex, int statementId) {
		int startIndex = 1, endIndex = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = this.dao.doImportDutyItems(bsType,statementId,beginIndex,startIndex,endIndex);
			currentIndex += insertSize;
			if (insertSize < processSize)
				break;
			startIndex += processSize;
			endIndex += processSize;
		}
		return currentIndex;
	}

	private int doInternationalTransactionItem(BillStatementType bsType,int beginIndex,int statementId,Date start,Date end,String isurKcode,String rcvrKcode,CashFlowDirection direction) {
		int startIndex = 1, endIndex = processSize, insertSize = 0, currentIndex = beginIndex;
		boolean isMsdcPayOnBehalfOfSsdc = false;
		while (true) {
			insertSize = this.dao.doInternationalTransactionItem(bsType,beginIndex,startIndex,endIndex,statementId,start,end,isurKcode,rcvrKcode,direction,isMsdcPayOnBehalfOfSsdc);
			currentIndex += insertSize;
			if (insertSize < processSize) break;
			startIndex += processSize;
			endIndex += processSize;
		}
		return currentIndex;
	}
	
	private int doInternationalTransactionItemMsdcPaymentOnBehalfOfSsdc(BillStatementType bsType,int beginIndex,int statementId,Date start,Date end,String isurKcode,String rcvrKcode,CashFlowDirection direction) {
		int startIndex = 1, endIndex = processSize, insertSize = 0, currentIndex = beginIndex;
		boolean isMsdcPayOnBehalfOfSsdc = true;
		while (true) {
			insertSize = this.dao.doInternationalTransactionItemMsdcPaymentOnBehalfOfSsdc(bsType,beginIndex,startIndex,endIndex,statementId,start,end,isurKcode,rcvrKcode,direction,isMsdcPayOnBehalfOfSsdc);
			currentIndex += insertSize;
			if (insertSize < processSize) break;
			startIndex += processSize;
			endIndex += processSize;
		}
		return currentIndex;
	}
	
	private int doPaymentOnbehalfImportDuty(BillStatementType bsType,int beginIndex, int statementId,String isurKcode,String rcvrKcode){
		int startIndex = 1, endIndex = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = this.dao.doPaymentOnBehalfRelatedItemImportDuty(statementId, beginIndex, startIndex, endIndex, rcvrKcode, isurKcode, bsType);
			currentIndex += insertSize;
			if (insertSize < processSize)
				break;
			startIndex += processSize;
			endIndex += processSize;
		}
		return currentIndex;
	}
	
	private int doProductInventoryReturnItem(int beginIndex, int statementId, BillStatementType bsType) {
		int startIndex = 1, endIndex = processSize, insertSize = 0, currentIndex = beginIndex;
		while (true) {
			insertSize = this.dao.doProductInventoryReturnItem(statementId, beginIndex, startIndex, endIndex, TransactionLineType.MS2SS_PRODUCT_INVENTORY_RETURN, bsType);
			currentIndex += insertSize;
			if (insertSize < processSize)
				break;
			startIndex += processSize;
			endIndex += processSize;
		}
		return currentIndex;
	}

	private void doCurrencyExchage(BillStatementType bsType,int statementId,Date start,Date end) {
		List<BillStatementLineItem> items = this.dao.queryLineItems(bsType,statementId);
		for (BillStatementLineItem item : items) {
			BigDecimal exchangeRate = 
					this.getCurrencyExchangeRate(start,end,Currency.valueOf(item.getOriginalCurrency()), this.statementCurrency);
			BigDecimal statementAmount = 
					item.getOriginalAmount().multiply(exchangeRate).setScale(statementCurrency.getScale(),RoundingMode.HALF_UP);
			((BillStatementLineItemImpl)item).setStatementCurrency(this.statementCurrency);
			((BillStatementLineItemImpl)item).setStatementAmount(statementAmount);				
			this.dao.updateStatementLineItemAmount(item,bsType);						
		}			
	}
	private BigDecimal getCurrencyExchangeRate(Date start,Date end,Currency src,Currency dst){
		return src==dst? BigDecimal.ONE:this.currencyRepo.queryExchangeRate(start, end, src, 
				dst,DrsConstants.interbankRateForSs2spSettlement);
	}
	
	private void doBalance(int statementId, String rcvrName, Date start, Date end, BillStatementType bsType) {
		
		BillStatementImpl statement = (BillStatementImpl)this.dao.getStatement(statementId,bsType);
		BigDecimal amountRmtIsur2Rcvr = BigDecimal.ZERO; 
		Remittance rmtIsurToRcvr = this.dao.getRemittanceIsur2RcvrTotal(start, end, statement.getIssuerKcode(), statement.getReceiverKcode());
		if(rmtIsurToRcvr!=null){
			amountRmtIsur2Rcvr=rmtIsurToRcvr.getAmount();
			Assert.isTrue(rmtIsurToRcvr.getCurrency()==this.statementCurrency,"rmtIsurToRcvr.getCurrency()==this.statementCurrency");
		}
		
		BigDecimal amountRmtRcvr2Isur = BigDecimal.ZERO;
		Remittance rmtRcvrToIsur = this.dao.getRemittanceRcvr2IsurTotal(start, end, statement.getIssuerKcode(), statement.getReceiverKcode());
		if(rmtRcvrToIsur!=null){
			amountRmtRcvr2Isur=rmtRcvrToIsur.getAmount();
			Assert.isTrue(rmtRcvrToIsur.getCurrency()==this.statementCurrency,"rmtRcvrToIsur.getCurrency()==this.statementCurrency");
		}
		BigDecimal previousBalance = this.dao.queryPreviousBalance(statementId,bsType);
		BigDecimal total = this.dao.getAllTransactionTotal(statementId,bsType);
		BigDecimal balance = total.add(previousBalance).add(amountRmtIsur2Rcvr.negate()).add(amountRmtRcvr2Isur);
		statement.setCurrency(this.statementCurrency);
		statement.setTotal(total);
		statement.setPreviousBalance(previousBalance);
		statement.setRemittanceIsurToRcvr(amountRmtIsur2Rcvr);
		statement.setRemittanceRcvrToIsur(amountRmtRcvr2Isur);
		statement.setBalance(balance);
		this.dao.updateStatement(statement,bsType);
	}
	
	@Override
	public BillStatement getExistingBillStatement(int id) {
		return this.dao.getExistingStatement(id,BillStatementType.OFFICIAL);
	}

	@Override
	public BillStatement getExistingDraftBillStatement(int id) {
		return this.dao.getExistingStatement(id,BillStatementType.DRAFT);
	}

	@Override
	public void confirmAllDraft() {
		List<String> draftNameList=this.dao.queryDraftStatementNameList();
		for(String name:draftNameList){
			this.commitDraft(name);
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
	
	@Override
	public String doSettlement() {
		SettlementPeriod period = this.processRecentPeriods(1).get(0);
		if(!this.isMs2ssSettlementDone(period)){
			this.doStatementOfficial("K3", "K2", period.getStartPoint(), period.getEndPoint());
			this.doStatementOfficial("K4", "K2", period.getStartPoint(), period.getEndPoint());
			this.doStatementOfficial("K5", "K2", period.getStartPoint(), period.getEndPoint());
			this.doStatementOfficial("K6", "K2", period.getStartPoint(), period.getEndPoint());
			this.doStatementOfficial("K7", "K2", period.getStartPoint(), period.getEndPoint());
			this.doStatementOfficial("K8", "K2", period.getStartPoint(), period.getEndPoint());
			this.doStatementOfficial("K9", "K2", period.getStartPoint(), period.getEndPoint());
			this.confirmAllDraft();
		}
		return "Ms2ss for current period has already done.";
	}
	
	private boolean isMs2ssSettlementDone(SettlementPeriod period){
		return this.dao.existMs2ssStatement(period.getStartPoint(), period.getEndPoint());
	}

}
