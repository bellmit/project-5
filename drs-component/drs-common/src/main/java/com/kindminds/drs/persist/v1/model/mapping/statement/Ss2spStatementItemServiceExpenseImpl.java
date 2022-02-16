package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;


import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;



public class Ss2spStatementItemServiceExpenseImpl implements Ss2spStatementReport.Ss2spStatementItemServiceExpense {

	//@Column(name="name")
	private String name;
	//@Column(name="currency_id")
	private Integer currencyId;
	//@Column(name="amount")
	private BigDecimal amount;
	//@Id //@Column(name="invoice_number")
	private String invoiceNumber;

	public Ss2spStatementItemServiceExpenseImpl() {
	}

	public Ss2spStatementItemServiceExpenseImpl(String name, Integer currencyId, BigDecimal amount, String invoiceNumber) {
		this.name = name;
		this.currencyId = currencyId;
		this.amount = amount;
		this.invoiceNumber = invoiceNumber;
	}

	@Override
	public String toString() {
		return "Ss2spStatementItemServiceExpenseImpl [getDisplayName()=" + getDisplayName() + ", getCurrency()="
				+ getCurrency() + ", getAmountStr()=" + getAmountStr() + ", getAmount()=" + getAmount()
				+ ", getInvoiceNumber()=" + getInvoiceNumber() + "]";
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}
	
	@Override
	public String getCurrency() {
		Currency currency = Currency.fromKey(this.currencyId);
		Assert.notNull(currency);
		return currency.name();
	}
	
	@Override
	public String getAmountStr() {
		Currency currency = Currency.fromKey(this.currencyId);
		return this.amount.setScale(currency.getScale()).toString();
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
