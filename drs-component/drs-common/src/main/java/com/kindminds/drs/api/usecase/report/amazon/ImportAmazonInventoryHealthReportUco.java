package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.amazon.AmazonInventoryHealthReportImportInfo;

public interface ImportAmazonInventoryHealthReportUco {
	List<Marketplace> getMarketplaceList();
	String importReport(String marketplaceId,byte[] fileBytes);
	List<AmazonInventoryHealthReportImportInfo> getImportStatuses();
}
