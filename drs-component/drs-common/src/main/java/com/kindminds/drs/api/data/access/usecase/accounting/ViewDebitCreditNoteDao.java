package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteItem;
import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteItem.DebitCreditNoteSkuLine;

public interface ViewDebitCreditNoteDao {
	public String queryStatementDateEnd(String statementName);
	public BigDecimal queryNoteAmountTotal(String statementName);
	public List<DebitCreditNoteItem> queryDebitCreditNoteItems(String statementName);
	public List<DebitCreditNoteSkuLine> queryDebitCreditNoteSkuLines(String statementName,String invoiceNumber);
	public List<Object[]> queryDebitCreditNoteSkuItems(String statementName);
}
