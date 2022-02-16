package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;

import java.math.BigDecimal;

public class DrsTransactionLineItemImpl implements DrsTransactionLineItem {

	private Integer lineSeq;
	private String isurKcode;
	private String rcvrKcode;
	private String itemName;
	private String currency;
	private BigDecimal amount;
	public DrsTransactionLineItemImpl(
			Integer lineSeq,
			String isurKcode,
			String rcvrKcode,
			String itemName,
			String currency,
			String itemAmount){
		this.lineSeq = lineSeq;
		this.isurKcode = isurKcode;
		this.rcvrKcode = rcvrKcode;
		this.itemName = itemName;
		this.currency = currency;
		this.amount = new BigDecimal(itemAmount);
	}

	@Override
	public boolean equals(Object object){
		if(object instanceof DrsTransactionLineItem){
			DrsTransactionLineItem dtsi = (DrsTransactionLineItem)object;
			return this.getLineSeq().equals(dtsi.getLineSeq())
				&& this.getIsurKcode().equals(dtsi.getIsurKcode())
				&& this.getRcvrKcode().equals(dtsi.getRcvrKcode())
				&& this.getItemName().equals(dtsi.getItemName())
				&& this.getCurrency().equals(dtsi.getCurrency())
				&& this.getAmount().equals(dtsi.getAmount());
		}
		return false;
	}

	@Override
	public String toString() {
		return "DrsTransactionLineItemImpl [getLineSeq()=" + getLineSeq() + ", getIsurKcode()=" + getIsurKcode()
				+ ", getRcvrKcode()=" + getRcvrKcode() + ", getItemName()=" + getItemName() + ", getCurrency()="
				+ getCurrency() + ", getAmount()=" + getAmount() + "]";
	}

	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}

	@Override
	public String getIsurKcode() {
		return this.isurKcode;
	}

	@Override
	public String getRcvrKcode() {
		return this.rcvrKcode;
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
	public BigDecimal getAmount() {
		return this.amount;
	}

}
