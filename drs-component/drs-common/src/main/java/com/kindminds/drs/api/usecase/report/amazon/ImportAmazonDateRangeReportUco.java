package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.DateRangeImportStatus;

public interface ImportAmazonDateRangeReportUco {

	List<Marketplace> getMarketplaces();

	List<DateRangeImportStatus> getImportStatus();

	String importReport(int marketplaceId, byte[] fileBytes);

}