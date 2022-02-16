package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemServiceExpense;

import java.math.BigDecimal;

public class Ss2spStatementItemServiceExpenseImpl implements Ss2spStatementItemServiceExpense {

	private String displayName;
	private String currency;
	private String amountStr;
	private BigDecimal amount;
	private String invoiceNumber;
	
	public Ss2spStatementItemServiceExpenseImpl(
			String displayName,
			String currency,
			String amountStr,
			String amount,
			String invoiceNumber){
		this.displayName=displayName;
		this.currency=currency;
		this.amountStr=amountStr;
		this.amount = new BigDecimal(amount);
		this.invoiceNumber=invoiceNumber;
	}
	
	@Override
	public String toString() {
		return "Ss2spStatementItemServiceExpenseImpl [getDisplayName()=" + getDisplayName() + ", getCurrency()="
				+ getCurrency() + ", getAmountStr()=" + getAmountStr() + ", getAmount()=" + getAmount()
				+ ", getInvoiceNumber()=" + getInvoiceNumber() + "]";
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Ss2spStatementItemServiceExpense){
			Ss2spStatementItemServiceExpense item = (Ss2spStatementItemServiceExpense)obj;
			return this.getDisplayName().equals(item.getDisplayName())
				&& this.getCurrency().equals(item.getCurrency())
				&& this.getAmountStr().equals(item.getAmountStr())
				&& this.getAmount().equals(item.getAmount())
				&& this.getInvoiceNumber().equals(item.getInvoiceNumber());
		}
		return false;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getAmountStr() {
		return this.amountStr;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

	@Override
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

}
