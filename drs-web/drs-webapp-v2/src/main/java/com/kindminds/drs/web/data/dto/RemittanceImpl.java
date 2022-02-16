package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.Remittance;

public class RemittanceImpl implements Remittance {

	private int remittanceId;
	private String utcDateSent;
	private String utcDateReceived;
	private String sender;
	private String receiver;
	private Currency currency;
	private String amount;
	private String feeAmount;
	private String feeIncluded;
	private String statementName;
	private String bankPayment;
	private String reference;

	@Override
	public int getRemittanceId() {
		return this.remittanceId;
	}

	public void setRemittanceId(int remittanceId) {
		this.remittanceId = remittanceId;
	}

	@Override
	public String getUtcDateSent() {
		return this.utcDateSent;
	}

	public void setUtcDateSent(String utcDateSent) {
		this.utcDateSent = utcDateSent;
	}

	@Override
	public String getUtcDateReceived() {
		return this.utcDateReceived;
	}

	public void setUtcDateReceived(String utcDateReceived) {
		this.utcDateReceived = utcDateReceived;
	}

	@Override
	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFeeAmount() {
		return this.feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getFeeIncluded() {
		return this.feeIncluded;
	}

	public void setFeeIncluded(String feeIncluded) {
		this.feeIncluded = feeIncluded;
	}

	public String getStatementName() {
		return this.statementName;
	}

	public void setStatementName(String statementName) {
		this.statementName = statementName;
	}

	public String getBankPayment() {
		return bankPayment;
	}

	public void setBankPayment(String bankPayment) {
		this.bankPayment = bankPayment;
	}

	@Override
	public String getReference() {		
		return this.reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;		
	}
	
}