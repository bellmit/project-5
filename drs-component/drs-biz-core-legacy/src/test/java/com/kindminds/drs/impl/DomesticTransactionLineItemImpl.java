package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction.DomesticTransactionLineItem;

public class DomesticTransactionLineItemImpl implements DomesticTransactionLineItem {
	
	private Integer lineSeq;
	private Integer itemKey;
	private String itemName;
	private String note;
	private String amount;
	
	public DomesticTransactionLineItemImpl(
			Integer lineSeq,
			Integer itemKey,
			String itemName,
			String note,
			String amount) {
		this.lineSeq = lineSeq;
		this.itemKey = itemKey;
		this.itemName = itemName;
		this.note = note;
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "DomesticTransactionLineItemImpl [getLineSeq()=" + getLineSeq() + ", getItemKey()=" + getItemKey()
				+ ", getItemName()=" + getItemName() + ", getNote()=" + getNote() + ", getAmount()=" + getAmount()
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DomesticTransactionLineItem){
			DomesticTransactionLineItem item = (DomesticTransactionLineItem)obj;
			return this.getLineSeq().equals(item.getLineSeq())
				&& this.getItemKey().equals(item.getItemKey())
				&& this.getItemName().equals(item.getItemName())
				&& this.getNote().equals(item.getNote())
				&& this.getAmount().equals(item.getAmount());
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
	public String getNote() {
		return this.note;
	}

	@Override
	public String getAmount() {
		return this.amount;
	}

}
