package com.kindminds.drs.api.adapter;

import com.kindminds.drs.api.v1.model.accounting.AccountJournalEntry;

public interface AccountingAdapterDao {
	public boolean checkConnection();
	public void setUserName(String name);
	public void setPassword(String pswd);
	public int createEntry(AccountJournalEntry entry);
	public int deleteEntry();
}
