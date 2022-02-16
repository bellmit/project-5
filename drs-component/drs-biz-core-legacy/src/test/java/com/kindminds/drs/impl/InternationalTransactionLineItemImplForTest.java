package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.InternationalTransactionLineItem;

public class InternationalTransactionLineItemImplForTest implements InternationalTransactionLineItem {
	
	private Integer lineSeq;
	private Integer itemKey;
	private String itemName;
	private String itemNote;
	private String subtotal;
	private String vatRate;
	private String vatAmount;
	
	public InternationalTransactionLineItemImplForTest(
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
	}
	
	@Override
	public String toString() {
		return "InternationalTransactionLineItemImpl [getLineSeq()=" + getLineSeq() + ", getItemKey()=" + getItemKey()
				+ ", getItemName()=" + getItemName() + ", getItemNote()=" + getItemNote() + ", getSubtotal()=" + getSubtotal()
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof InternationalTransactionLineItem){
			InternationalTransactionLineItem item = (InternationalTransactionLineItem)obj;
			return this.getLineSeq().equals(item.getLineSeq())
				&& this.getItemKey().equals(item.getItemKey())
				&& this.getItemName().equals(item.getItemName())
				&& this.getItemNote().equals(item.getItemNote())
				&& this.getSubtotal().equals(item.getSubtotal());
		}
		return false;
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
