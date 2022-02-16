package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemProfitShare;

import java.math.BigDecimal;

public class Ss2spStatementItemProfitShareImpl implements Ss2spStatementItemProfitShare {
	
	private String displayName;
	private String amountUntaxStr;
	private String amountTaxStr;
	private String amountStr;
	private Currency currency;
	private BigDecimal amount;

	public Ss2spStatementItemProfitShareImpl(
			String displayName,
			String amountUntaxStr,
			String amountTaxStr,
			String amountStr,
			String currency,
			String amount){
		this.displayName = displayName;
		this.amountUntaxStr = amountUntaxStr;
		this.amountTaxStr = amountTaxStr;
		this.amountStr = amountStr;
		this.currency = Currency.valueOf(currency);
		this.amount = new BigDecimal(amount);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Ss2spStatementItemProfitShare){
			Ss2spStatementItemProfitShare item = (Ss2spStatementItemProfitShare)obj;
			return this.getDisplayName().equals(item.getDisplayName())
				&& this.getAmountUntaxStr().equals(item.getAmountUntaxStr())
				&& this.getAmountTaxStr().equals(item.getAmountTaxStr())
				&& this.getAmountStr().equals(item.getAmountStr())
				&& this.getCurrency().equals(item.getCurrency())
				&& this.getAmount().equals(item.getAmount());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Ss2spStatementItemProfitShareImpl [getDisplayName()=" + getDisplayName() + ", getAmountUntaxStr()="
				+ getAmountUntaxStr() + ", getAmountTaxStr()=" + getAmountTaxStr() + ", getAmountStr()="
				+ getAmountStr() + ", getCurrency()=" + getCurrency() + ", getAmount()=" + getAmount() + "]";
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getAmountUntaxStr() {
		return this.amountUntaxStr;
	}

	@Override
	public String getAmountTaxStr() {
		return this.amountTaxStr;
	}

	@Override
	public String getAmountStr() {
		return this.amountStr;
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
