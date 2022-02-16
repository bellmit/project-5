package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Warehouse;

public interface ImportAmazonMonthlyStorageFeeReportUco {	
	List<Warehouse> getWarehouses();
	String importFile(String warehouseId, byte[] bytes);		
}