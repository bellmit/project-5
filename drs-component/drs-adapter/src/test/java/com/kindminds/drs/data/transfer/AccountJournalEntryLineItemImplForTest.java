package com.kindminds.drs.v1.model;

import java.math.BigDecimal;

import com.kindminds.drs.api.v1.model.accounting.AccountJournalEntry.AccountJournalEntryLineItem;

public class AccountJournalEntryLineItemImplForTest implements AccountJournalEntryLineItem {
	private String name;
	private String accountCode;
	private BigDecimal debitAmount=BigDecimal.ZERO;
	private BigDecimal creditAmount=BigDecimal.ZERO;
	
	public AccountJournalEntryLineItemImplForTest(String name, String accountCode, BigDecimal debitAmount, BigDecimal creditAmount) {
		this.name = name;
		this.accountCode = accountCode;
		this.debitAmount = debitAmount;
		this.creditAmount = creditAmount;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getAccountCode() {
		return this.accountCode;
	}

	@Override
	public BigDecimal getDebitAmount() {
		return this.debitAmount;
	}

	@Override
	public BigDecimal getCreditAmount() {
		return this.creditAmount;
	}

}
