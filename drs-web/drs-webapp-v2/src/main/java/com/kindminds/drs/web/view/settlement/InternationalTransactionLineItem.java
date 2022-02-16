package com.kindminds.drs.web.view.settlement;

import java.io.Serializable;

public class InternationalTransactionLineItem implements Serializable{
		
	private static final long serialVersionUID = 8446493498721989725L;
	private Integer lineSeq;
	private Integer itemKey;
	private String itemName;
	private String itemNote;
	private String subtotal;
	private String vatRate;
	private String vatAmount;
	
	@Override
	public String toString() {
		return "InternationalTransactionLineItem [getLineSeq()=" + getLineSeq() + ", getItemKey()=" + getItemKey()
				+ ", getItemName()=" + getItemName() + ", getItemNote()=" + getItemNote() + ", getSubtotal()="
				+ getSubtotal() + "]";
	}

	public InternationalTransactionLineItem(){
		
	}
			
	public Integer getLineSeq(){
		return this.lineSeq;
	};
	
	public void setLineSeq(Integer lineSeq) {
		this.lineSeq = lineSeq;		
	}
	
	public Integer getItemKey(){
		return this.itemKey;
	};
	
	public void setItemKey(Integer itemKey) {
		this.itemKey = itemKey;		
	}
	
	public String getItemName(){
		return this.itemName;
	};
	
	public void setItemName(String itemName) {
		this.itemName = itemName;		
	}
		
	public String getItemNote(){
		return this.itemNote;
	};
	
	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;	
	}
	
	public String getSubtotal(){
		return this.subtotal;
	};
	
	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal; 				
	}

	public String getVatRate() {
		return vatRate;
	}

	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}

	public String getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(String vatAmount) {
		this.vatAmount = vatAmount;
	}
}