package com.kindminds.drs.api.usecase;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReport;

public interface ViewSkuAdvertisingPerformanceReportUco {
	List<Marketplace> getMarketplaces();
	boolean isDrsUser();
	String getActualSupplierKcode(String requestSupplierKcode);
	String getDefaultMarketplaceId();
	String getDefaultUtcDateStart();
	String getDefaultUtcDateEnd();
	Map<String,String> getSupplierKcodeToShortEnNameMap();
	SkuAdvertisingPerformanceReport getReport(String supplierKcode,String marketplaceId,String utcStartDate,String utcEndDate,String campaignName);
	List<String> getCampaignNameList(String supplierKcode,String marketplaceId,String utcStartDate,String utcEndDate);
}
