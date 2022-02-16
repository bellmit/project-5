package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.report.ViewAmazonSponsoredBrandsCampaignReport;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ViewAmazonSponsoredBrandsCampaignReportUco {
    Map<String,String> getSupplierKcodeToShortEnNameMap();
    Map<Integer, String> getMarketplaces();
    List<String> queryCampaignNames(String supplierKcode, Integer marketplaceId, Date start, Date end);
    ViewAmazonSponsoredBrandsCampaignReport queryReport(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName);
}
