package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Marketplace;

public interface Ss2spSettleableBrowserForProfitShareToExport {
	
	public String getStatementName();
	public String getDateStart();
	public String getDateEnd();
	public String getCountry();	
	public String getSourceCurrency();
	public String getExchangeRate();
	public String getSku();
	public String getSkuName();
	public String getName();
	public String getUtcDate();
	public Marketplace getMarketplace();
	public String getOrderId();
	public Integer getQuantity();
	public String getPretaxPrincipalPrice();
	public String getFcaInMarketSideCurrency();
	public String getMarketplaceFee();
	public String getFulfillmentFee();
	public String getDrsRetainment();
	public String getProfitShare();
	
}