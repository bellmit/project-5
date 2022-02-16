package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;

public interface ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco {
	List<Marketplace> getMarketplaces();
	String importFile(String marketplaceId,String utcDate,byte[] fileBytes);
	String importECMFile(String marketplaceId,String utcDate,byte[] fileBytes);
	String importFile(String marketplaceId,String utcDate,String fullPath);
	List<String> getYears();
	List<String> getMonths();
	String getDefaultYear();
	String getDefaultMonth();
	Map<String,Map<Marketplace,Boolean>> getImportStatus(String year,String month);
	String delete(String utcDate,String marketplaceId);
}
