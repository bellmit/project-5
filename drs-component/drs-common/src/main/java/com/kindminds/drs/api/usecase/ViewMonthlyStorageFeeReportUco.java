package com.kindminds.drs.api.usecase;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v1.model.report.MonthlyStorageFeeReport;

public interface ViewMonthlyStorageFeeReportUco {
	Map<String,String> getSupplierKcodeToShortEnNameMap();	
	public List<String> getYears();
	public List<String> getMonths();
	public List<Country> getCountries();
	boolean isDrsUser();
	String getActualSupplierKcode(String requestSupplierKcode);
	String getDefaultMarketplaceId();
	String getDefaultCountry();
	String getDefaultYear();
	String getDefaultMonth();	
	MonthlyStorageFeeReport getReport(String supplierKcode,String country,String year,String month);		
}