package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemSellBackRelated;

public class Ss2spStatementItemSellBackRelatedImpl implements Ss2spStatementItemSellBackRelated {
	

	private Integer currencyId;
	private BigDecimal amount;
	private String invoice;
	
	public Ss2spStatementItemSellBackRelatedImpl(
			Integer currencyId,
			BigDecimal amount,
			String invoiceNumber){
		this.currencyId = currencyId;
		this.amount = amount;
		this.invoice = invoiceNumber;
	}

	@Override
	public String toString() {
		return "Ss2spStatementItemSellBackRelatedImpl [getItemName()=" + getItemName() + ", getCurrency()="
				+ getCurrency() + ", getAmountStr()=" + getAmountStr() + ", getNoteText()=" + getNoteText()
				+ ", getInvoiceNumber()=" + getInvoiceNumber() + ", getAmount()=" + getAmount() + "]";
	}

	@Override
	public String getItemName() {
		return "ss2spStatement.SellBack";
	}

	@Override
	public String getCurrency() {
		return Currency.fromKey(this.currencyId).name();
	}

	@Override
	public String getAmountStr() {
		return this.amount.stripTrailingZeros().toPlainString();
	}

	@Override
	public String getNoteText() {
		return "ss2spStatement.noteForSellBack";
	}
	
	@Override
	public String getInvoiceNumber() {
		return this.invoice;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
