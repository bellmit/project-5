package com.kindminds.drs.api.v1.model.amazon;

import com.kindminds.drs.Marketplace;

public interface AmazonInventoryHealthReportImportInfo {
	Marketplace getMarketplace();
	String getSnapshotDate();
}
