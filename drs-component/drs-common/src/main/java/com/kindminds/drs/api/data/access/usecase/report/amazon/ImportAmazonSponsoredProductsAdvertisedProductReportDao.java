package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.v1.model.amazon.AmazonSponsoredProductsAdvertisedProductReportLineItem;
import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportBriefLineItem;
import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo;

public interface ImportAmazonSponsoredProductsAdvertisedProductReportDao {
	void clearStagingArea(Marketplace marketplace);
	int insertRawLinesToStagingArea(Marketplace marketplace, List<AmazonSponsoredProductsAdvertisedProductReportLineItem> items);
	int insertFromStagingAreaByInfo(Marketplace marketplace,AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo info);
	public List<Object []> queryDistinctDateCurrencyInfoFromStagingArea(Marketplace marketplace);
	public boolean isDateCurrencyExist(Marketplace marketplace,AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo info);
	public List<AmazonSponsoredProductsAdvertisedProductReportBriefLineItem> queryBriefLineItem(int startIndex, int size, Marketplace marketplace, Date start, Date end, String sku);
	public int queryBriefLineItemCount(Marketplace marketplace,Date start,Date end,String marketSku);
}
