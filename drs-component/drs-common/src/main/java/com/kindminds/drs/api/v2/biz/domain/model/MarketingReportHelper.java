package com.kindminds.drs.api.v2.biz.domain.model;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReportLineItem;

public interface MarketingReportHelper {
	List<SkuAdvertisingPerformanceReportLineItem> getSkuAdvertisingPerformanceReportLineItems(String supplierKcode,Marketplace marketplace,Date start,Date end);
	List<SkuAdvertisingPerformanceReportLineItem> getSkuAdvertisingPerformanceReportLineItems(String supplierKcode,Marketplace marketplace,Date start,Date end,String campaignName);
}
