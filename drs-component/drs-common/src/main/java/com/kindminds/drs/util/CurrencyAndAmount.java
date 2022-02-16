package com.kindminds.drs.util;

import java.math.BigDecimal;

import com.kindminds.drs.Currency;

public class CurrencyAndAmount {
	private Currency currency;
	private BigDecimal amount;
	public CurrencyAndAmount(Currency currency, BigDecimal amount){
		this.currency=currency;
		this.amount=amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Currency getCurrency() {
		return this.currency;
	}
	public BigDecimal getAmount() {
		return this.amount;
	}
}
