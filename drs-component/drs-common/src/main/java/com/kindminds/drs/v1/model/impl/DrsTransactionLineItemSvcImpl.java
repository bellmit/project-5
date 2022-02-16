package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;

import java.math.BigDecimal;

public class DrsTransactionLineItemSvcImpl implements DrsTransactionLineItem {

	private Integer lineSeq;
	private String isurKcode;
	private String rcvrKcode;
	private String itemName;
	private String currency;
	private BigDecimal amount;
	public DrsTransactionLineItemSvcImpl(
			Integer lineSeq,
			String isurKcode,
			String rcvrKcode,
			String itemName,
			String currency,
			BigDecimal amount){
		this.lineSeq = lineSeq;
		this.isurKcode = isurKcode;
		this.rcvrKcode = rcvrKcode;
		this.itemName = itemName;
		this.currency = currency;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "DrsTransactionLineItemSvcImpl [getLineSeq()=" + getLineSeq() + ", getIsurKcode()=" + getIsurKcode()
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
