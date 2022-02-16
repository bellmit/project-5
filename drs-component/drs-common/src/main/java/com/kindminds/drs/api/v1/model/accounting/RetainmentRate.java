package com.kindminds.drs.api.v1.model.accounting;

public interface RetainmentRate {
	int getId();
	String getUtcDateStart();
	String getUtcDateEnd();
	String getCountry();
	String getSupplierKcode();
	String getOriginalCurrency();
	String getRevenueInOriginalCurrency();
	String getCurrencyExchangeRateToUsd();
	String getRevenueInUsd();
	String getRate();
	Boolean isDeletable();
}
