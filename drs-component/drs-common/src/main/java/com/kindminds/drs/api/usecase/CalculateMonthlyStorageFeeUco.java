package com.kindminds.drs.api.usecase;

import java.math.BigDecimal;
import java.util.List;

public interface CalculateMonthlyStorageFeeUco {
	
	public List<String> getYears();
	public List<String> getMonths();
	public String calculate(String year, String month);
	public BigDecimal calculateSumOfTotalEstimatedMonthlyStorageFee(String supplierKcode, String country,String year, String month);
	
}