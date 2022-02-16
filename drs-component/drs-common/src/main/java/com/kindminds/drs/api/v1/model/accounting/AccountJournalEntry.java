package com.kindminds.drs.api.v1.model.accounting;

import java.math.BigDecimal;
import java.util.List;

public interface AccountJournalEntry {
	public String getJournalName();
	public String getPeriodName();
	public String getDate();
	public List<AccountJournalEntryLineItem> getLineItems();
	public interface AccountJournalEntryLineItem{
		public String getName();
		public String getAccountCode();
		public BigDecimal getDebitAmount();
		public BigDecimal getCreditAmount();
	}
}
