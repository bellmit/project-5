package com.kindminds.drs.api.v1.model.report;

import java.util.List;
import java.util.Map;

public interface SkuAdvertisingPerformanceReport {
	String getMarketplace();
	String getCurrency();
	String getGrandTotalImpression();
	String getGrandTotalClick();
	String getGrandClickThroughRate();
	String getGrandTotalSpend();
	String getGrandCostPerClick();
	String getGrandTotalOneWeekOrdersPlaced();
	String getGrandTotalOneWeekOrderedProductSales();
	String getGrandAcos();
	Map<String,List<SkuAdvertisingPerformanceReportLineItem>> getCampaignItems();
}
