package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction.DomesticTransactionLineItem;

import java.math.BigDecimal;

public class DomesticTransactionLineItemImpl implements DomesticTransactionLineItem {
	
	private Integer lineSeq;
	private Integer itemKey;
	private String itemName;
	private String note;
	private BigDecimal amount;
	
	public void setLineSeq(Integer lineSeq) {
		this.lineSeq = lineSeq;
	}
	
	public void setItemKey(Integer itemKey) {
		this.itemKey = itemKey;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public void setNote(String note) {
		this.note = note;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "DomesticTransactionLineItemImpl [getLineSeq()=" + getLineSeq() + ", getItemKey()=" + getItemKey()
				+ ", getItemName()=" + getItemName() + ", getNote()=" + getNote() + ", getAmount()=" + getAmount()
				+ "]";
	}

	@Override
	public Integer getLineSeq(){
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
		return this.amount.stripTrailingZeros().toPlainString();
	}

}
