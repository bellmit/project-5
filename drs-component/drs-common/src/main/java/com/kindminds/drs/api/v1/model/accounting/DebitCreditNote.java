package com.kindminds.drs.api.v1.model.accounting;

import java.util.List;

public interface DebitCreditNote {
	NoteType getType();
	String getDate();
	String getTotal();
	String getTotalAmountOfDebitCredit();
	List<DebitCreditNoteItem> getItems();
	
	public interface DebitCreditNoteItem{
		String getDescription();
		String getInvoiceNumber();
		String getTotal();
		List<DebitCreditNoteSkuLine> getSkuLines();
		
		public interface DebitCreditNoteSkuLine{
			String getSkuCode();
			String getDescription();
			String getSumForSku();
		}
		
	}
	
	public interface DebitCreditNoteSkuItems{
		String getSkuCode(); 
		String getInvoiceNumber();
		String getSumForSku();
	}
		
	public enum NoteType{
		DEBIT,CREDIT;
	}
	
}
