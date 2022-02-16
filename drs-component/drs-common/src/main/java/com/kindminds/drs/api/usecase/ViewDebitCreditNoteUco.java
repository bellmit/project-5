package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote;

public interface ViewDebitCreditNoteUco {
	public DebitCreditNote getDebitCreditNote(String statementName);
	public String getTsvReport(String statementName);
}
