package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Marketplace;

import java.math.BigDecimal;
import java.util.List;

public interface SkuAdvertisingEvaluationReport {
	
	Marketplace getMarketplace();
	public String getCurrency();
	String getGrandTotalAdClicks();
	String getGrandTotalAdSpend();
	String getGrandTotalSessions();
	String getGrandTotalPageViews();
	String getGrandBuyBoxPercentage();
	String getGrandTotalUnitOrdered();
	String getGrandTotalOrderedProductSales();
	String getGrandConversionRate();
	String getGrandAcos();
	String getGrandTotalClicksOverTotalPageViews();
	public List<SkuAdvertisingEvaluationReportLineItem> getLineItems();
	
	public interface SkuAdvertisingEvaluationReportLineItem {
		String getSku();
		String getSkuName();
		String getTotalAdClicks();
		String getTotalAdSpend();
		String getTotalSessions();
		String getTotalPageViews();
		String getBuyBoxPercentage();
		String getTotalUnitOrdered();
		String getTotalOrderedProductSales();
		String getConversionRate();		
		String getAcos();
		String getTotalClicksOverTotalPageViews();
	}
	
	public interface SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport {
		Integer getTotalClicks();
		BigDecimal getTotalSpend();
		BigDecimal getTotalOneWeekOrderedProductSales();
	}

}
