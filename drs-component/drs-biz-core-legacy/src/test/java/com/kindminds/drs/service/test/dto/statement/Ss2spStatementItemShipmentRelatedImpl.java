package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemShipmentRelated;

import java.math.BigDecimal;

public class Ss2spStatementItemShipmentRelatedImpl implements Ss2spStatementItemShipmentRelated {

	private String itemName;
	private String currency;
	private String amountStr;
	private String noteText;
	private String sourceShipmentName;
	private String sourceShipmentInvoiceNumber;
	private BigDecimal amount;

	public Ss2spStatementItemShipmentRelatedImpl(
			String itemName,
			String currency,
			String amountStr,
			String noteText,
			String sourceShipmentName,
			String sourceShipmentInvoiceNumber,
			String amount){
		this.itemName = itemName;
		this.currency = currency;
		this.amountStr = amountStr;
		this.noteText = noteText;
		this.sourceShipmentName = sourceShipmentName;
		this.sourceShipmentInvoiceNumber = sourceShipmentInvoiceNumber;
		this.amount = new BigDecimal(amount);
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Ss2spStatementItemShipmentRelated){
			Ss2spStatementItemShipmentRelated item = (Ss2spStatementItemShipmentRelated)obj;
			return this.getItemName().equals(item.getItemName())
					&& this.getCurrency().equals(item.getCurrency())
					&& this.getAmountStr().equals(item.getAmountStr())
					&& this.getNoteText().equals(item.getNoteText())
					&& this.getSourceShipmentName().equals(item.getSourceShipmentName())
					&& this.getSourceShipmentInvoiceNumber().equals(item.getSourceShipmentInvoiceNumber())
					&& this.getAmount().equals(item.getAmount());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Ss2spStatementItemShipmentRelatedImpl [getItemName()=" + getItemName() + ", getCurrency()="
				+ getCurrency() + ", getAmountStr()=" + getAmountStr() + ", getNoteText()=" + getNoteText()
				+ ", getSourceShipmentName()=" + getSourceShipmentName() + ", getSourceShipmentInvoiceNumber()="
				+ getSourceShipmentInvoiceNumber() + ", getAmount()=" + getAmount() + "]";
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
	public String getSourceShipmentName() {
		return this.sourceShipmentName;
	}

	@Override
	public String getSourceShipmentInvoiceNumber() {
		return this.sourceShipmentInvoiceNumber;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
