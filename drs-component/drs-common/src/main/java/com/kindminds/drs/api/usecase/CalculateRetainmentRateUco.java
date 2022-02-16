package com.kindminds.drs.api.usecase;

import java.util.List;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v1.model.accounting.RetainmentRate;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;

public interface CalculateRetainmentRateUco {
	List<SettlementPeriod> getAvailableSettlementPeriodList();
	List<RetainmentRate> getList();
	String calculate();
	void calculate(String settlementPeriodId);
	int delete(int rateId);
	// FOR TEST
	void calculateForTest(String utcStartDate,String utcEndDate);
	String getRate(String utcStartDate,String utcEndDate,Country c,String supplierKcode);
	String getRevenueInOriginalCurrency(String startDate,String endDate,Country c,String supplierKcode);
}
