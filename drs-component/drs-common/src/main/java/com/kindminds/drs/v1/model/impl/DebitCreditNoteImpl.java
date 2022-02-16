package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote;

import java.math.BigDecimal;
import java.util.List;

public class DebitCreditNoteImpl implements DebitCreditNote {
	
	private String date;
	private BigDecimal amountTotal;
	private List<DebitCreditNoteItem> items = null;

	public void setDate(String date) {
		this.date = date;
	}
	
	public void setAmountTotal(BigDecimal amount){
		this.amountTotal = amount;
	}

	public void setItems(List<DebitCreditNoteItem> items) {
		this.items = items;
	}
	
	@Override
	public String toString() {
		return "DebitCreditNoteImpl [getType()=" + getType() + ", getDate()="
				+ getDate() + ", getAmountTotal()=" + getTotal()
				+ ", getItems()=" + getItems() + "]";
	}

	@Override
	public NoteType getType() {
		if(this.amountTotal.signum()==-1) return NoteType.DEBIT;
		if(this.amountTotal.signum()== 1) return NoteType.CREDIT;
		return null;
	}

	@Override
	public String getDate() {
		return this.date+"(UTC)";
	}

	@Override
	public String getTotal() {
		return this.amountTotal.setScale(2).toPlainString();
	}

	@Override
	public String getTotalAmountOfDebitCredit() {
		return this.amountTotal.abs().setScale(2).toPlainString();
	}

	@Override
	public List<DebitCreditNoteItem> getItems() {
		return this.items;
	}

}
