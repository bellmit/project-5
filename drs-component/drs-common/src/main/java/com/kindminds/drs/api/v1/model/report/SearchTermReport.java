package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;

import java.util.List;

public interface SearchTermReport {
	
	Marketplace getMarketplace();
	List<SearchTermReportLineItem> getLineItems();
	
	public interface SearchTermReportLineItem {
		String getCampaignName();
		String getAdGroupName();
		String getCustomerSearchTerm();
		String getKeyword();
		String getMatchType();
		String getFirstDayOfImpression();
		String getLastDayOfImpression();
		Integer getImpressions();
		Integer getClicks();
		String getCtr();
		String getTotalSpend();
		String getAverageCpc();
		String getAcos();
		Currency getCurrency();
		Integer getOrdersPlacedWithinOneWeekOfAClick();
		String getProductSalesWithinOneWeekOfAClick();
		String getConversionRateWithinOneWeekOfAClick();
		Integer getSameSkuUnitsOrderedWithinOneWeekOfClick();
		Integer getOtherSkuUnitsOrderedWithinOneWeekOfClick();
		String getSameSkuUnitsProductSalesWithinOneWeekOfClick();
		String getOtherSkuUnitsProductSalesWithinOneWeekOfClick();
	}
}
