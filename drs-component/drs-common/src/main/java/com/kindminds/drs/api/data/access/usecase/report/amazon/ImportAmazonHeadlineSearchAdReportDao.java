package com.kindminds.drs.api.data.access.usecase.report.amazon;

import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;

import java.util.List;

public interface ImportAmazonHeadlineSearchAdReportDao {

	Integer insertKeywordRecord(List<AmazonHeadlineSearchAdReportItem> record);
	Integer insertKeywordVideoRecord(List<AmazonHeadlineSearchAdReportItem> records);
	Integer insertCampaignRecord(List<AmazonHeadlineSearchAdReportItem> records);
	Integer insertCampaignVideoRecord(List<AmazonHeadlineSearchAdReportItem> records);
	Integer insertDisplayRecord(List<AmazonHeadlineSearchAdReportItem> records);

	Integer deleteByDateAndMarketplace(int marketplaceId, String dateTime, String reportType);

}
