package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportBriefLineItem;

public interface ImportAmazonSponsoredProductsAdvertisedProductReportUco {
	List<Marketplace> getMarketplaces();
	String importFile(String marketplaceId, byte[] fileBytes);
	String importFile(String marketplaceId, String fullPath);

	String importECMFile(String marketplaceId, byte[] fileBytes);

	DtoList<AmazonSponsoredProductsAdvertisedProductReportBriefLineItem> getBriefLineItem(int pageIndex, String marketplaceId, String dateStartUtc, String dateEndUtc, String marketSku);
	

	
}