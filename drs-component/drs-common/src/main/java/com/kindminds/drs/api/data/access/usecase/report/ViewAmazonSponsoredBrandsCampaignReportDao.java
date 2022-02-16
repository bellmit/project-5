package com.kindminds.drs.api.data.access.usecase.report;

import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;

import java.util.Date;
import java.util.List;

public interface ViewAmazonSponsoredBrandsCampaignReportDao {
    List<String> queryCampaignNames(String supplierKcode, Integer marketplaceId, Date start, Date end);
    List<AmazonHeadlineSearchAdReportItem> queryReportDaily(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName);
    List<AmazonHeadlineSearchAdReportItem> queryReportByPeriod(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName);
}
