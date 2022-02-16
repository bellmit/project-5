package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction.DomesticTransactionLineItem;

public class DomesticTransactionLineItemImpl implements DomesticTransactionLineItem{

	private Integer lineSeq;
	private Integer itemKey;
	private String itemName;
	private String note;
	private String amount;
	
	public DomesticTransactionLineItemImpl(){};
	
	public DomesticTransactionLineItemImpl(DomesticTransactionLineItem origLineItem){
		this.lineSeq = origLineItem.getLineSeq(); 
		this.itemKey = origLineItem.getItemKey();
		this.itemName = origLineItem.getItemName();
		this.note = origLineItem.getNote();
		this.amount = origLineItem.getAmount();					
	};
	
	@Override
	public String toString() {
		return "DomesticTransactionLineItemImpl [getLineSeq()=" + getLineSeq() + ", getItemKey()=" + getItemKey()
				+ ", getItemName()=" + getItemName() + ", getNote()=" + getNote() + ", getAmount()=" + getAmount()
				+ "]";
	}

	@Override
	public Integer getLineSeq() {		
		return this.lineSeq;
	}

	public void setLineSeq(Integer lineSeq) {
		this.lineSeq = lineSeq;		
	}
	
	@Override
	public Integer getItemKey() {
		return this.itemKey;
	}

	public void setItemKey(Integer itemKey) {
		this.itemKey = itemKey;		
	}
	
	@Override
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;		
	}
	
	@Override
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
		
	@Override
	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
		
}