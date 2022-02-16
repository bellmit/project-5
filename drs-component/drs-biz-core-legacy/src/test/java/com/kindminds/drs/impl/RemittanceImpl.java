package com.kindminds.drs.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.Remittance;
import com.kindminds.drs.util.TestUtil;

public class RemittanceImpl implements Remittance {

	private int remittanceId;
	private String utcDateSent;
	private String utcDateReceived;
	private String sender;
	private String receiver;
	private Currency currency;
	private String amount;
	private String feeAmount;
	private Boolean feeIncluded;
	private String statementName;
	private String bankPayment;
	private String reference;

	public RemittanceImpl(
			int remittanceId,
			String utcDateSent,
			String utcDateReceived, 
			String sender,
			String receiver,
			Currency currency,
			String amount,
			String reference){
		this.remittanceId = remittanceId;
		this.utcDateSent = utcDateSent;
		this.utcDateReceived = utcDateReceived;
		this.sender = sender;
		this.receiver = receiver;
		this.currency = currency;
		this.amount = amount;
		this.reference = reference;
	}
	
	@Override
	public boolean equals(Object obj){
		if ( obj instanceof Remittance ){
			Remittance rmt = (Remittance)obj;
			return this.getRemittanceId()==rmt.getRemittanceId()
				&& TestUtil.nullableEquals(this.getUtcDateSent(),rmt.getUtcDateSent())
				&& TestUtil.nullableEquals(this.getUtcDateReceived(),rmt.getUtcDateReceived())
				&& this.getSender().equals(rmt.getSender())
				&& this.getReceiver().equals(rmt.getReceiver())
				&& this.getCurrency()==rmt.getCurrency()
				&& this.getAmount().equals(rmt.getAmount())
				&& TestUtil.nullableEquals(this.getReference(),rmt.getReference());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "RemittanceImpl [getRemittanceId()=" + getRemittanceId()
				+ ", getDateSent()=" + getUtcDateSent() + ", getDateReceived()="
				+ getUtcDateReceived() + ", getSender()=" + getSender()
				+ ", getReceiver()=" + getReceiver() + ", getCurrency()="
				+ getCurrency() + ", getAmount()=" + getAmount()
				+ ", getReference()=" + getReference() + "]";
	}
	
	@Override
	public int getRemittanceId() {
		return this.remittanceId;
	}
	
	@Override
	public String getUtcDateSent() {
		return this.utcDateSent;
	}
	
	@Override
	public String getUtcDateReceived() {
		return this.utcDateReceived;
	}
	
	@Override
	public String getSender() {
		return this.sender;
	}
	
	@Override
	public String getReceiver() {
		return this.receiver;
	}
	
	@Override
	public Currency getCurrency() {
		return this.currency;
	}
	
	@Override
	public String getAmount() {
		return this.amount;
	}

	public String getFeeAmount() {
		return this.feeAmount;
	}

	public String getFeeIncluded() {
		return this.feeIncluded.toString();
	}

	public String getStatementName() {
		return this.statementName;
	}

	public String getBankPayment() {
		return bankPayment;
	}
	
	@Override
	public String getReference() {
		return this.reference;
	}
}