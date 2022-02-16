package com.kindminds.drs.api.v1.model.amazon;

import java.math.BigDecimal;
import java.util.Date;

public interface AmazonSearchTermReportRawLine {
	public String getCampaignName();
	public String getAdGroupName();
	public String getCustomerSearchTerm();
	public String getKeyword();
	public String getMatchType();
	public Date getFirstDayOfImpression();
	public Date getLastDayOfImpression();
	public Integer getImpressions();
	public Integer getClicks();
	public BigDecimal getCtr();
	public BigDecimal getTotalSpend();
	public BigDecimal getAverageCpc();
	public BigDecimal getAcos();
	public String getCurrency();
	public Integer getOrdersPlacedWithinOneWeekOfAClick();
	public BigDecimal getProductSalesWithinOneWeekOfAClick();
	public BigDecimal getConversionRateWithinOneWeekOfAClick();
	public Integer getSameSkuUnitsOrderedWithinOneWeekOfClick();
	public Integer getOtherSkuUnitsOrderedWithinOneWeekOfClick();
	public BigDecimal getSameSkuUnitsProductSalesWithinOneWeekOfClick();
	public BigDecimal getOtherSkuUnitsProductSalesWithinOneWeekOfClick();
}
