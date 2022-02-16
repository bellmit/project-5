package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemSellBackRelated;

import java.math.BigDecimal;

public class Ss2spStatementItemSellBackRelatedImpl implements Ss2spStatementItemSellBackRelated {

	private String itemName;
	private String currency;
	private String amountStr;
	private String noteText;
	private String invoiceNumber;
	private BigDecimal amount;

	public Ss2spStatementItemSellBackRelatedImpl(
			String itemName,
			String currency,
			String amountStr,
			String noteText,
			String invoiceNumber,
			String amount){
		this.itemName = itemName;
		this.currency = currency;
		this.amountStr = amountStr;
		this.noteText = noteText;
		this.invoiceNumber = invoiceNumber;
		this.amount = new BigDecimal(amount);
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Ss2spStatementItemSellBackRelated){
			Ss2spStatementItemSellBackRelated item = (Ss2spStatementItemSellBackRelated)obj;
			return this.getItemName().equals(item.getItemName())
				&& this.getCurrency().equals(item.getCurrency())
				&& this.getAmountStr().equals(item.getAmountStr())
				&& this.getNoteText().equals(item.getNoteText())
				&& this.getInvoiceNumber().equals(item.getInvoiceNumber())
				&& this.getAmount().equals(item.getAmount());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Ss2spStatementItemSellBackRelatedImpl [getItemName()=" + getItemName() + ", getCurrency()="
				+ getCurrency() + ", getAmountStr()=" + getAmountStr() + ", getNoteText()=" + getNoteText()
				+ ", getInvoiceNumber()=" + getInvoiceNumber() + ", getAmount()=" + getAmount() + "]";
	}

	@Override
	public String getItemName() {
		return this.itemName;
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
	public String getNoteText() {
		return this.noteText;
	}

	@Override
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}



}
