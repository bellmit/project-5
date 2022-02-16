package com.kindminds.drs.api.v1.model.amazon;

import java.math.BigDecimal;
import java.util.Date;

public interface AmazonSponsoredProductsAdvertisedProductReportLineItem{
	Date getStartDate();
	Date getEndDate();
	String getPortfolioName();
	String getCurrency();
	String getCampaignName();
	String getAdGroupName();
	String getAdvertisedSku();
	String getAdvertisedAsin();
	Integer getImpressions();
	Integer getClicks();
	BigDecimal getClickThruRateCtr();
	BigDecimal getCostPerClickCpc();
	BigDecimal getSpend();
	BigDecimal getSevenDayTotalSales();
	BigDecimal getTotalAdvertisingCostOfSalesAcos();
	BigDecimal getTotalReturnOnAdvertisingSpendRoas();
	Integer getSevenDayTotalOrders();
	Integer getSevenDayTotalUnits();
	BigDecimal getSevenDayConversionRate();
	Integer getSevenDayAdvertisedSkuUnits();
	Integer getSevenDayOtherSkuUnits();
	BigDecimal getSevenDayAdvertisedSkuSales();
	BigDecimal getSevenDayOtherSkuSales();
}