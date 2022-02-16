package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;

public interface SalesAndPageTrafficReportSummary {
	Integer getTotalOrderItems();
	BigDecimal getTotalOrderedProductSales();
	Integer getTotalSessions();
	Integer getTotalUnitsOrdered();
}
