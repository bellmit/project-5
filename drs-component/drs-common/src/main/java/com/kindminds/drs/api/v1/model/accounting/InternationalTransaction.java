package com.kindminds.drs.api.v1.model.accounting;

import com.kindminds.drs.Currency;

import java.util.List;

public interface InternationalTransaction {
	
	public enum CashFlowDirection{
		SP2MS(0,"Supplier to MSDC"),
		MS2SP(1,"MSDC to Supplier");
		private int key;
		private String displayName;
		CashFlowDirection(int key,String displayName) { this.key=key; this.displayName=displayName; }
		public int getKey() { return this.key; }
		public String getDisplayName(){ return this.displayName; }
	}
	
	Integer getId();
	String getUtcDate();
	Integer getCashFlowDirectionKey();
	String getMsdcKcode();
	String getMsdcName();
	String getSsdcKcode();
	String getSsdcName();
	String getSplrKcode();
	String getSplrName();
	Currency getCurrency();
	String getTotal();
	String getNote();
	Boolean isEditable();
	List<InternationalTransactionLineItem> getLineItems();
	
	public interface InternationalTransactionLineItem{
		Integer getLineSeq();
		Integer getItemKey();
		String getItemName();
		String getItemNote();
		String getSubtotal();
		String getVatRate();
		String getVatAmount();
	}
	
}
