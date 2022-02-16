package com.kindminds.drs.api.v1.model.accounting;

import java.util.List;

public interface ImportDutyTransaction {
	
	String getUnsName();
	String getUtcDate();
	String getDstCountry();
	String getCurrency();
	String getTotal();
	Boolean isEditable();
	List<ImportDutyTransactionLineItem> getLineItems();
	
	public interface ImportDutyTransactionLineItem{
		String getSourceIvsName();
		String getSku();
		Integer getQuantity();
		String getAmount();
	}
}
