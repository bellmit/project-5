package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.api.v1.model.amazon.AmazonInventoryHealthReportLineItem;

public interface ImportAmazonInventoryHealthReportDao {
	int deleteLineItems(int marketplaceId);
	int insertLineItems(int marketplaceId, List<AmazonInventoryHealthReportLineItem> lineItems);
	List<Object []> queryImportInfoList();
}
