package com.kindminds.drs.api.v1.model.accounting;

import com.kindminds.drs.Currency;

public interface Remittance {
		
	int getRemittanceId();
	String getUtcDateSent();
	String getUtcDateReceived();
	String getSender();
	String getReceiver();
	Currency getCurrency();
	String getAmount();
	String getFeeAmount();
	String getFeeIncluded();
	String getStatementName();
	String getBankPayment();
	String getReference();
	
}