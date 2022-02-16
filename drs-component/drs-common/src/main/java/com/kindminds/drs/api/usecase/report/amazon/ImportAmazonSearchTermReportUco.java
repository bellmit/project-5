package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;

public interface ImportAmazonSearchTermReportUco {
	List<Marketplace> getMarketplaceList();
	String importFile(String marketplaceId, byte[] fileBytes);
	String importECMFile(String marketplaceId, byte[] fileBytes);
	String importFile(String marketplaceId, String fullPath);
	Map<String,String> getCampaignNameToSupplierKcodeMap(Marketplace marketplace);
	Map<String,String> getSupplierKcodeNameMap();
	Map<String,String> getSupplierKcodeToShortEnUsNameWithRetailMap();

	void updateCampaignNameSupplierMap(Marketplace marketplace, Map<String, String> campaignNameSupplierMap);
}
