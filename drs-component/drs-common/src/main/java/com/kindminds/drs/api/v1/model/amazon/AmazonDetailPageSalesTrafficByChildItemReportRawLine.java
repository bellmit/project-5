package com.kindminds.drs.api.v1.model.amazon;

import java.math.BigDecimal;

public interface AmazonDetailPageSalesTrafficByChildItemReportRawLine {
	String getParentAsin();
	String getChildAsin();
	String getTitle();
	Integer getSessions();
	BigDecimal getSessionPercentage();
	Integer getPageViews();
	BigDecimal getPageViewsPercentage();
	BigDecimal getBuyBoxPercentage();
	Integer getUnitsOrdered();
	BigDecimal getUnitSessionPercentage();
	BigDecimal getOrderedProductSales();
	Integer getTotalOrderItems();
}
