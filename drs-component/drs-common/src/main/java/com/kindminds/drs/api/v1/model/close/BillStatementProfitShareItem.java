package com.kindminds.drs.api.v1.model.close;

public interface BillStatementProfitShareItem {
	public Integer getStatementId();
	public Integer getLineSeq();
	public String getSourceCountry();
	public String getSourceCurrency();
	public String getSourceAmountUntaxed();
	public String getDestinationCurrency();
	public String getDestinationAmountUntaxed();
	public String getExchangeRate();
}
