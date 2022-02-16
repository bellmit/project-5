package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.InternationalTransactionLineItem;

public class InternationalTransactionLineItemImpl implements InternationalTransactionLineItem{

	private Integer lineSeq;
	private Integer itemKey;
	private String itemName;
	private String itemNote;
	private String subtotal;
	private String vatRate;
	private String vatAmount;

	public InternationalTransactionLineItemImpl() {}
	
	public InternationalTransactionLineItemImpl(
			Integer lineSeq,
			Integer itemKey,
			String itemName,
			String itemNote,
			String subtotal) {
		this.lineSeq = lineSeq;
		this.itemKey = itemKey;
		this.itemName = itemName;
		this.itemNote = itemNote; 
		this.subtotal = subtotal;
		this.vatRate = "0";
		this.vatAmount = "0";
	}

	public InternationalTransactionLineItemImpl(
			Integer lineSeq,
			Integer itemKey,
			String itemName,
			String itemNote,
			String subtotal,
			String vatRate,
			String vatAmount) {
		this.lineSeq = lineSeq;
		this.itemKey = itemKey;
		this.itemName = itemName;
		this.itemNote = itemNote;
		this.subtotal = subtotal;
		this.vatRate = vatRate;
		this.vatAmount = vatAmount;
	}
	
	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}
	
	@Override
	public Integer getItemKey() {
		return this.itemKey;
	}

	@Override
	public String getItemName() {
		return this.itemName;
	}

	@Override
	public String getItemNote() {
		return this.itemNote;
	}
	
	@Override
	public String getSubtotal() {
		return this.subtotal;
	}

	@Override
	public String getVatRate() {
		return this.vatRate;
	}

	@Override
	public String getVatAmount() {
		return this.vatAmount;
	}
	
}
