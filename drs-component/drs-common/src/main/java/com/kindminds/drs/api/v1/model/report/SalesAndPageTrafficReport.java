package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

public interface SalesAndPageTrafficReport {
	Currency getCurrency();
	String getStartDate();
	String getEndDate();
	String getTotalOrderItems();
	String getTotalOrderedProductSales();
	String getTotalSessions();
	String getTotalUnitsOrdered();
}
