package com.kindminds.drs.api.v1.model.close;

public interface BillStatementLineItemCustomerCaseExemptionInfo {
	public String getStatementName();
	public String getCountry();
	public String getCurrency();
	public String getProductBaseCode();
	public String getRevenue();
	public String getFreeRate();
	public String getFreeAmount();
	public String getCustomerCaseFeeAmount();
	public String getExemptionAmount();
}
