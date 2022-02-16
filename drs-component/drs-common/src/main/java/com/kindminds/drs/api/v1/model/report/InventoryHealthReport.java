package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Marketplace;

import java.util.List;

public interface InventoryHealthReport{
	Marketplace getMarketplace();
	String getSupplierKcode();
	String getSnapshotDate();
	List<InventoryHealthReportLineItem> getLineItems();
	
	public interface InventoryHealthReportLineItem {
		String getSku();
		String getProductName();
		String getCondition();
		Integer getUnsellableQuantity();
		Integer getSellableQuantity();
		Integer getQtyToBeChargedLtsf6Mo();
		Integer getQtyToBeChargedLtsf12Mo();
		String getProjectedLtsf6Mo();
		String getProjectedLtsf12Mo();
		Integer getUnitsShippedLast30Days();
		String getWeeksOfCoverT30();
	}
}


