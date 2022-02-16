package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;

import java.util.List;

public interface Ss2spSkuProfitShareDetailReport {
	
	String getDateStart();
	String getDateEnd();
	String getIsurKcode();
	String getRcvrKcode();
	String getSku();
	String getTotal();
	Currency getCurrency();
	List<Ss2spSkuProfitShareDetailReportShippedItem> getShippedItems();
	List<Ss2spSkuProfitShareDetailReportRefundedItem> getRefundedItems();
	
	public interface Ss2spSkuProfitShareDetailReportShippedItem{
		String getUtcDate();
		Marketplace getMarketplace();
		String getOrderId();
		String getPretaxPrincipalPrice();
		String getFcaInMarketSideCurrency();
		String getMarketplaceFee();
		String getFulfillmentFee();
		String getDrsRetainment();
		String getProfitShare();
	}
	
	public interface Ss2spSkuProfitShareDetailReportRefundedItem{
		String getUtcDate();
		Marketplace getMarketplace();
		String getOrderId();
		String getPretaxPrincipalPrice();
		String getFcaInMarketSideCurrency();
		String getMarketplaceFee();
		String getDrsRetainment();
		String getRefundFee();
		String getProfitShare();
	}
	
}
