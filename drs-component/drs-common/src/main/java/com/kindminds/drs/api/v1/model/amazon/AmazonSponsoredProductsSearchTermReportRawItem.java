package com.kindminds.drs.api.v1.model.amazon;

import java.math.BigDecimal;
import java.util.Date;

public interface AmazonSponsoredProductsSearchTermReportRawItem {
	Date getStartDate();
	Date getEndDate();
	String getPortfolioName();
	String getCurrency();
	String getCampaignName();
	String getAdGroupName();
	String getKeyword();
	String getMatchType();
	String getCustomerSearchTerm();
	Integer getImpressions();
	Integer getClicks();
	BigDecimal getCtr();	
	BigDecimal getCpc();
	BigDecimal getSpend();
	BigDecimal getSevenDayTotalSales();
	BigDecimal getAcos();
	BigDecimal getRoas();
	Integer getSevenDayTotalOrders();
	Integer getSevenDayTotalUnits();
	BigDecimal getSevenDayConversionRate();
	Integer getSevenDayAdvertisedSkuUnits();
	Integer getSevenDayOtherSkuUnits();
	BigDecimal getSevenDayAdvertisedSkuSales();
	BigDecimal getSevenDayOtherSkuSales();	
}