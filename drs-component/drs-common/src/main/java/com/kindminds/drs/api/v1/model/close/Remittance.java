package com.kindminds.drs.api.v1.model.close;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;

public interface Remittance{
	public enum RemittenceType {Sent, Received};
	public RemittenceType getType();
	public Currency getCurrency();
	public BigDecimal getAmount();
	public BigDecimal getStatementCurrencyAmount();
	public void setStatementCurrencyAmount(BigDecimal statementCurrencyAmount);
}
