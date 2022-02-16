package com.kindminds.drs.api.v1.model.accounting;

import java.math.BigDecimal;
import java.util.Date;

public interface AmazonCampaignPerformanceReportRawLine {
	public String getCampaignName();
	public String getAdGroupName();
	public String getAdvertisedSku();
	public String getKeyword();
	public String getMatchType();
	public Date getStartDate();
	public Date getEndDate();
	public Integer getClicks();
	public Integer getImpressions();
	public BigDecimal getCtr();
	public BigDecimal getTotalSpend();
	public BigDecimal getAverageCpc();
	public String getCurrency();
	
	public Integer getOneDayOrdersPlaced();
	public BigDecimal getOneDayOrderedProductSales();
	public BigDecimal getOneDayConversionRate();
	public Integer getOneDaySameSkuUnitsOrdered();
	public Integer getOneDayOtherSkuUnitsOrdered();
	public BigDecimal getOneDaySameSkuUnitsOrderedProductSales();
	public BigDecimal getOneDayOtherSkuUnitsOrderedProductSales();
	
	public Integer getOneWeekOrdersPlaced();
	public BigDecimal getOneWeekOrderedProductSales();
	public BigDecimal getOneWeekConversionRate();
	public Integer getOneWeekSameSkuUnitsOrdered();
	public Integer getOneWeekOtherSkuUnitsOrdered();
	public BigDecimal getOneWeekSameSkuUnitsOrderedProductSales();
	public BigDecimal getOneWeekOtherSkuUnitsOrderedProductSales();
	
	public Integer getOneMonthOrdersPlaced();
	public BigDecimal getOneMonthOrderedProductSales();
	public BigDecimal getOneMonthConversionRate();
	public Integer getOneMonthSameSkuUnitsOrdered();
	public Integer getOneMonthOtherSkuUnitsOrdered();
	public BigDecimal getOneMonthSameSkuUnitsOrderedProductSales();
	public BigDecimal getOneMonthOtherSkuUnitsOrderedProductSales();
}
