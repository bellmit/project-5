package com.kindminds.drs.api.v1.model.accounting;

import com.kindminds.drs.Currency;

import java.util.Date;
import java.util.List;

public interface DomesticTransaction {
	
	Integer getId();
	String getUtcDate();
	String getSsdcKcode();
	String getSsdcName();
	String getSplrKcode();
	String getSplrName();
	String getInvoiceNumber();
	String getTaxPercentage();
	Currency getCurrency();
	String getAmountSubtotal();
	String getAmountTax();
	String getAmountTotal();
	Date getTransactionDate();
	Boolean isEditable();
	List<DomesticTransactionLineItem> getLineItems();

	void setIsEditable(Boolean isEditable);
	
	public interface DomesticTransactionLineItem{
		Integer getLineSeq();
		Integer getItemKey();
		String getItemName();
		String getNote();
		String getAmount();
	}
	
}
