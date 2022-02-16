package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.Remittance;
import com.kindminds.drs.api.v1.model.close.BillStatement.BillStatementLineItem;
import com.kindminds.drs.enums.BillStatementType;

public interface DoMs2ssSettlementDao {
	public int queryMaxStatementSeq(String rcvrKcode, String isurKcode, BillStatementType bsType);
	public int insertStatement(int nextSerialId, String newStmntName, String rcvrName, String isurName, Date startDate, Date endDate, int versionNumber, BillStatementType type);
	public int doShipmentRelatedItem(int statementId, int beginLineSeq, int startIndex, int endIndex, TransactionLineType sItem, BillStatementType type);
	public int doInternationalTransactionItem(BillStatementType bsType, int beginIndex, int startIndex, int endIndex, int statementId, Date start, Date end, String isurKcode, String rcvrKcode, CashFlowDirection direction, boolean isMsdcPayOnBehalfOfSsdc);
	public int doInternationalTransactionItemMsdcPaymentOnBehalfOfSsdc(BillStatementType bsType, int beginIndex, int startIndex, int endIndex, int statementId, Date start, Date end, String isurKcode, String rcvrKcode, CashFlowDirection direction, boolean isMsdcPayOnBehalfOfSsdc);
	public int doCustomerCareCaseItem(int statementId, int beginLineSeq, int startIndex, int endIndex, String rcvrKcode, String isurKcode, BillStatementType type);
	public BigDecimal getAllTransactionTotal(int statementId, BillStatementType bsType);
	public BigDecimal queryPreviousBalance(int statementId, BillStatementType bsType);
	public BillStatement getStatement(int statementId, BillStatementType bsType);
	public void updateStatement(BillStatement statement, BillStatementType bsType);
	public void updateStatementLineItemAmount(BillStatementLineItem item, BillStatementType bsType);
	public void updateStatementLineItemReference(BillStatementLineItem item, BillStatementType type);
	public Remittance getRemittanceIsur2RcvrTotal(Date startDate, Date endDate, String sender, String receiver);
	public Remittance getRemittanceRcvr2IsurTotal(Date startDate, Date endDate, String sender, String receiver);
	public String queryNewestRelatedShipmentNameInLineItem(int statementId);
	public BillStatement getExistingStatement(int statementId, BillStatementType bsType);
	public List<BillStatementLineItem> queryLineItems(BillStatementType bsType, int statementId);
	public List<BillStatementLineItem> queryLineItems(BillStatementType bsType, int statementId, List<String> typeNameList);
	public Date queryPreviousPeriodEndDate(String isurKcode, String rcvrKcode);
	public void updateMs2ssNameOfCustomerCareCaseMsdcMessage(int statementId);
	public void delete(BillStatementType bsType, String statementName);
	public String queryIsurKcode(BillStatementType type, String statementName);
	public String queryRcvrKcode(BillStatementType type, String statementName);
	public Date queryStartDateOfDraft(String statementName);
	public Date queryEndDateOfDraft(String statementName);
	public String queryNewestStatementName(String isurKcode,String rcvrKcode);
	public int doProductInventoryReturnItem(int statementId, int beginIndex, int startIndex, int endIndex,TransactionLineType type, BillStatementType bsType);
	public Map<String,String> querySkuToNewestArrivalUnsNameMap(BillStatementType type, int statementId);
	public List<String> queryDraftStatementNameList();
	public boolean existMs2ssStatement(Date startPoint, Date endPoint);
	public int doImportDutyItems(BillStatementType bsType, int statementId, int beginIndex, int startIndex, int endIndex);
	public int doPaymentOnBehalfRelatedItemImportDuty(int statementId, int beginIndex, int startIndex, int endIndex, String rcvrKcode, String isurKcode, BillStatementType bsType);
}
