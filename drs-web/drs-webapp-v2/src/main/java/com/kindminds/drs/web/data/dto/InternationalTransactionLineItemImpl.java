package com.kindminds.drs.web.data.dto;


import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;

public class InternationalTransactionLineItemImpl implements InternationalTransaction.InternationalTransactionLineItem {

	private Integer lineSeq;
	private Integer itemKey;
	private String itemName;
	private String itemNote;
	private String subtotal;
	private String vatRate;
	private String vatAmount;
		
	public InternationalTransactionLineItemImpl(){}
	
	public InternationalTransactionLineItemImpl(InternationalTransaction.InternationalTransactionLineItem origLineItem) {
		this.lineSeq = origLineItem.getLineSeq(); 
		this.itemKey = origLineItem.getItemKey();
		this.itemName = origLineItem.getItemName();
		this.itemNote = origLineItem.getItemNote();
		this.subtotal = origLineItem.getSubtotal();
		this.vatRate = origLineItem.getVatRate();
		this.vatAmount = origLineItem.getVatAmount();
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
	public String getItemNote() {
		return this.itemNote;
	}
	
	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;	
	}
		
	@Override
	public String getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal; 				
	}

	@Override
	public String getVatRate() {
		return this.vatRate;
	}

	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}

	@Override
	public String getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(String vatAmount) {
		this.vatAmount = vatAmount;
	}
}