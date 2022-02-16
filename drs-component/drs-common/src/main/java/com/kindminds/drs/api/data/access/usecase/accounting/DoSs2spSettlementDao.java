package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.BillStatement.BillStatementLineItem;
import com.kindminds.drs.api.v1.model.close.BillStatementLineItemCustomerCaseExemptionInfo;
import com.kindminds.drs.api.v1.model.close.BillStatementProfitShareItem;
import com.kindminds.drs.api.v1.model.close.CustomerCaseMsdcMsgChargeInfo;
import com.kindminds.drs.enums.BillStatementType;

public interface DoSs2spSettlementDao {

	public Date queryPeriodStartDate(BillStatementType bsType, int statementId);
	public Date queryPeriodEndDate(BillStatementType bsType,int statementId);
	public Date queryPreviousPeriodEndDate(String supplierKcode);
	public Integer queryMaxStatementSeq(String supplierKcode, BillStatementType bsType);
	public int insertStatement(Integer stmntSeq,String stmntName, String supplierKcode, String isurKcode, Date startDate, Date endDate, int versionNumber, BillStatementType bsType);
	public List<BillStatementLineItem> getAllLineItems(int statementId, BillStatementType bsType);
	public List<BillStatementLineItem> getAllProfitShareItems(int statementId, BillStatementType bsType);
	public void setLineItemStatementCurrencyAndAmount(BillStatementType bsType,int statementId,int lineItemLineSeq,Currency stmntCurrency,BigDecimal amount);
	public int doDrsTransactionItem(int statementId, int beginLineSeq, String supplierKcode, int startIndex, int endIndex,TransactionLineType sItem, BillStatementType bsType);
	public int doImportDutyItem(int statementId, int beginLineSeq, int startIndex, int endIndex,TransactionLineType type, BillStatementType bsType);
	public int doInternationalTransactionItems(BillStatementType bsType,int statementId,int beginLineSeq,int startIndex,int endIndex,int cashFlowDirectionKey);
	public int doCustomerCareCaseItem(int statementId, int beginLineSeq, String supplierKcode,int startIndex, int endIndex, BillStatementType bsType);
	public int doPurchaseOrderRelatedItem(int statementId, int beginLineSeq, String supplierKcode, int startIndex, int endIndex,TransactionLineType tltType, BillStatementType bsType);
	public int doDomesticTransaction(int statementId, int beginLineSeq, String supplierKcode, int startIndex, int endIndex,String sltName, BillStatementType bsType);
	public int insertProfitShareTax(int statementId, int beginLineSeq, Currency statementCurrency,BigDecimal amount, BillStatementType bsType );
	public BillStatement getStatement(int statementId, BillStatementType bsType);
	public BillStatement getExistingStatement(int statementId, BillStatementType bsType);
	public BigDecimal queryProfitShareDestinationUntaxedTotal(BillStatementType bsType,int statementId);
	public BigDecimal queryProfitShareTax(BillStatementType bsType,int statementId);
	public BigDecimal queryNonProfitShareTotal(BillStatementType bsType,int statementId);
	public BigDecimal queryPreviousBalance(int statementId, BillStatementType bsType);
	public BigDecimal queryVatRate(String supplierKcode);
	public void updateStatement(BillStatement statement, BillStatementType bsType);
	public List<BillStatementLineItem> getExistingStatementLineItems(int statementId, BillStatementType bsType);
	public Object[] queryCurrencyAndTotalRemittanceFromIsurToRcvr(Date startDate, Date endDate,String isurKcode, String rcvrKcode);
	public Object[] queryCurrencyAndTotalRemittanceFromRcvrToIsur(Date startDate, Date endDate,String isurKcode, String rcvrKcode);
	public void updateSs2spNameOfCustomerCareCaseMsdcMessage(int statementId);
	public List<String> querySupplierKcodeList();
	public void insertProfitShareItem(BillStatementType bsType, int statementId, int lineSeq, Country country,Currency srcCurrency, BigDecimal sourceAmount, Currency stmntCurrency, BigDecimal subtotal,BigDecimal exchangeRate);
	public List<BillStatementLineItem> queryNonProfitShareStatementLineItems(BillStatementType bsType,int statementId);
	public List<BillStatementProfitShareItem> queryProfitShareItems(BillStatementType bsType, int statementId);
	public BigDecimal queryProfitShareItemStatementAmountUntaxedTotal(BillStatementType bsType,int statementId);
	public void deleteOfficial(String statementName);
	public String querySupplierKcode(BillStatementType bsType,String statementName);
	public String queryNewestStatementName(String supplierKcode);
	public Map<Country,Map<String,BigDecimal>> queryCountryToProductBaseCustomerFeeMap(BillStatementType bsType, int statementId);
	public void insertCustomerCaseLineItem(BillStatementType bsType, int statementId, int lineSeq, Country country, Currency currency,BigDecimal amount, String productBase);
	public void insertCustomerCaseExemptionAndInfo(BillStatementType bsType, int statementId, int lineSeq, Country country, Currency currency, BigDecimal exemptionAmount, String productBase, BigDecimal productBaseRevenue, BigDecimal freeRateToProductBaseRevenue, BigDecimal freeThreshold, BigDecimal customerCaseFee);
	public List<BillStatementLineItemCustomerCaseExemptionInfo> queryCustomerCaseInfoList(BillStatementType bsType,int statementId);
	public List<Object []> queryCustomerCaseMsdcMsgChargeInfoList(Date start,Date end);
	public void updateCustomerCaseMsdcChargeInActualCurrency(List<CustomerCaseMsdcMsgChargeInfo> customerCaseMsdcMsgChargeInfoList);
	public boolean existSs2spStatement(Date startPoint);
	// FOR DRAFT STATEMENT
	public String querySupplierKcodeOfDraft(String statementName);
	public Date queryDateStartOfDraft(String statementName);
	public Date queryDateEndOfDraft(String statementName);
	public List<String> queryDraftStatementNameList();
	public void deleteDraft(String statementName);
}
