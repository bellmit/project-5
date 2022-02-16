package com.kindminds.drs.v1.model.impl.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.api.v1.model.amazon.AmazonInventoryHealthReportImportInfo;

import java.util.Date;

public class AmazonInventoryHealthReportImportInfoImpl implements AmazonInventoryHealthReportImportInfo {
	
	private int marketplaceId;
	private Date snapshotDate;
	
	public AmazonInventoryHealthReportImportInfoImpl(int marketplaceId, Date snapshotDate) {
		this.marketplaceId = marketplaceId;
		this.snapshotDate = snapshotDate;
	}

	@Override
	public String toString() {
		return "AmazonInventoryHealthReportImportInfoImpl [getMarketplace()=" + getMarketplace()
				+ ", getSnapshotDate()=" + getSnapshotDate() + "]";
	}

	@Override
	public Marketplace getMarketplace() {
		return Marketplace.fromKey(this.marketplaceId);
	}

	@Override
	public String getSnapshotDate() {
		return DateHelper.toString(this.snapshotDate,"yyyy-MM-dd","UTC");
	}

}
