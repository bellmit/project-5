package com.kindminds.drs.api.usecase.report.amazon;

public interface ImportAmazonHeadlineSearchAdReportUco {

	String importReport(int marketplaceId, byte[] fileBytes, String reportType);

	int deleteReport(int marketplaceId, String reportDate, String reportType);
}