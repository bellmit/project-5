package com.kindminds.drs.v1.model;

import com.kindminds.drs.api.v1.model.accounting.AccountJournalEntry;

import java.util.ArrayList;
import java.util.List;

public class AccountJournalEntryImplForTest implements AccountJournalEntry {
	private String journalName;
	private String periodName;
	private String dateStr;
	private List<AccountJournalEntryLineItem> lineItems = null;
	
	public AccountJournalEntryImplForTest(String journalName, String periodName, String dateStr, List<AccountJournalEntryLineItem> lineItems) {
		this.journalName = journalName;
		this.periodName = periodName;
		this.dateStr = dateStr;
		this.lineItems = lineItems;
	}

	@Override
	public String getJournalName() {
		return this.journalName;
	}

	@Override
	public String getPeriodName() {
		return this.periodName;
	}

	@Override
	public String getDate() {
		return this.dateStr;
	}

	@Override
	public List<AccountJournalEntryLineItem> getLineItems() {
		return this.lineItems;
	}
	
	public void addLineItem(AccountJournalEntryLineItem item){
		if (this.lineItems==null){
			this.lineItems=new ArrayList<AccountJournalEntryLineItem>();
		}
		this.lineItems.add(item);
	} 

}
