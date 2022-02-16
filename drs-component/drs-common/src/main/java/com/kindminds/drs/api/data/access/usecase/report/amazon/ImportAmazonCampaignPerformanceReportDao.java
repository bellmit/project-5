package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.AmazonCampaignPerformanceReportBriefLineItem;
import com.kindminds.drs.api.v1.model.accounting.AmazonCampaignPerformanceReportRawLine;
import com.kindminds.drs.api.v1.model.report.AmazonCampaignPerformanceReportDateCurrencyInfo;

public interface ImportAmazonCampaignPerformanceReportDao {
	public void clearStagingArea(Marketplace marketplace);
	public void insertRawLinesToStagingArea(Marketplace marketplace, List<AmazonCampaignPerformanceReportRawLine> lineList);
	public List<Object []> queryDistinctDateCurrencyInfoFromStagingArea(Marketplace marketplace);
	public boolean isDateCurrencyExist(Marketplace marketplace, AmazonCampaignPerformanceReportDateCurrencyInfo info);
	public int insertFromStagingAreaByInfo(Marketplace marketplace, AmazonCampaignPerformanceReportDateCurrencyInfo info);
	public int updateFromStagingAreaByInfo(Marketplace marketplace, AmazonCampaignPerformanceReportDateCurrencyInfo info);
	public List<AmazonCampaignPerformanceReportBriefLineItem> queryBriefLineItem(int startIndex, int size, Marketplace marketplace, Date start, Date end, String sku);
	public int queryBriefLineItemCount(Marketplace marketplace, Date start, Date end, String marketSku);
}
