package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.List;

public interface Ss2spProfitShareReport {
	
	int getVersionNumber();
	String getDateStart();
	String getDateEnd();
	String getIsurKcode();
	String getRcvrKcode();
	String getAmountSubTotal();
	String getAmountTax();
	String getAmountTotal();
	String getStatementCurrency();
	List<Ss2spProfitShareReportLineItem> getLineItems();

	public interface Ss2spProfitShareReportLineItem{
		String getRevenueInUsd();
		void setAchievedRetainmentRateGrade(BigDecimal achievedRetainmentRateGrade);
		String getAchievedRetainmentRateGrade();
		String getEffectiveRetainmentRate();
		String getSourceCountry();
		String getSourceCurrency();
		String getStatementCurrency();
		String getSourceAmount();
		String getStatementAmount();
		String getExchangeRate();
	}

}
