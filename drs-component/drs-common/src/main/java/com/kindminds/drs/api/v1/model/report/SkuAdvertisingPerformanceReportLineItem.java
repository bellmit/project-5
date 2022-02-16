package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

public interface SkuAdvertisingPerformanceReportLineItem {
	Currency getCurrency();
	String getSku();
	String getTotalImpression();
	String getTotalClicks();
	String getClickThroughRate();
	String getTotalSpend();
	String getCostPerClick();
	String getTotalOneWeekOrdersPlaced();
	String getTotalOneWeekOrderedProductSales();
	String getAcos();
}