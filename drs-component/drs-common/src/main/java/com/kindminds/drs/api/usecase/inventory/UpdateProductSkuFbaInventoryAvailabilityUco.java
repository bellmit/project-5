package com.kindminds.drs.api.usecase.inventory;

import java.util.List;
import java.util.Map;

public interface UpdateProductSkuFbaInventoryAvailabilityUco {
	List<String> getYears();
	List<String> getMonths();
	String getDefaultYear();
	String getDefaultMonth();	
	String uploadFileAndUpdate(String utcDate,byte[] fileBytes);
	Map<String,Boolean> getUpdateStatus(String year,String month);
	String clearAvailabilityData(String utcDateStr);
}
