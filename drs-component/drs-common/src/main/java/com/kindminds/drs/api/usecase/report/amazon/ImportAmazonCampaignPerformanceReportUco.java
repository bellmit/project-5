package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.report.AmazonCampaignPerformanceReportBriefLineItem;

public interface ImportAmazonCampaignPerformanceReportUco {
	List<Marketplace> getMarketplaces();
	String importFile(String marketplaceId, byte[] bytes);
	String importFile(String marketplaceId, String fullPath);
	DtoList<AmazonCampaignPerformanceReportBriefLineItem> getBriefLineItem(int pageIndex, String marketplaceId, String dateStartUtc, String dateEndUtc, String marketSku);
	

}
