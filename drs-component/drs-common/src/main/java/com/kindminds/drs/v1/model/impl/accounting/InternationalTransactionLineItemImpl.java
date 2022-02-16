package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.InternationalTransactionLineItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InternationalTransactionLineItemImpl implements InternationalTransactionLineItem {
	
	private Integer lineSeq;
	private Integer itemKey;
	private String itemName;
	private String itemNote;
	private BigDecimal subtotal;
	private BigDecimal vatRate;
	private BigDecimal vatAmount;
	private Integer currencyId;
	
	public void setLineSeq(Integer lineSeq) {
		this.lineSeq = lineSeq;
	}
	
	public void setItemKey(Integer itemKey) {
		this.itemKey = itemKey;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setItemNote(String itemNote){
		this.itemNote = itemNote;		
	}
		
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public void setVatRate(BigDecimal vatRate) {
		this.vatRate = vatRate;
	}

	public void setVatAmount(BigDecimal vatAmount) {
		this.vatAmount = vatAmount;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
		
	@Override
	public String toString() {
		return "InternationalTransactionLineItemImpl [getLineSeq()=" + getLineSeq()
				+ ", getItemKey()=" + getItemKey() + ", getItemName()=" + getItemName()
				+ ", getItemNote()=" + getItemNote() + ", getSubtotal()=" + getSubtotal()
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
	public String getItemNote() {
		return this.itemNote;
	}
	
	@Override
	public String getSubtotal() {
		int scale = Currency.fromKey(this.currencyId).getScale();
		return this.subtotal.setScale(scale, RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getVatRate() {
		if (vatRate == null) {
			return "0";
		}
		return this.vatRate.setScale(4, RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getVatAmount() {
		if (vatAmount == null) {
			return "0";
		}
		int scale = Currency.fromKey(this.currencyId).getScale();
		return this.vatAmount.setScale(scale, RoundingMode.HALF_UP).toPlainString();
	}



}
