package com.kindminds.drs.api.v1.model.report;

import java.util.List;

public interface MonthlyStorageFeeReport {
	
	String getSumOfTotalEstimatedMonthlyStorageFee();	
	public List<MonthlyStorageFeeReportLineItem> getLineItems();
		
	public interface MonthlyStorageFeeReportLineItem{
		String getSku();
		String getProductName();
		String getItemVolume();
		String getVolumeUnit();
		String getTotalAverageQuantityOnHand();
		String getTotalAverageQuantityPendingRemoval();
		String getTotalEstimatedTotalItemVolume();
		String getMonthOfCharge();
		String getStorageRate();
		String getCurrency();
		String getTotalEstimatedMonthlyStorageFee();		
	}
		
}