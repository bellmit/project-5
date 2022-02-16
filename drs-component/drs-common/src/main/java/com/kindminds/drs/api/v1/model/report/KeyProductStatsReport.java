package com.kindminds.drs.api.v1.model.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface KeyProductStatsReport {
	String getNextSettlementPeriod();
	List<KeyProductStatsByCountry> getKeyProductStatsByCountryList();
	
	public interface KeyProductStatsByCountry{
		String getCountry();
		String getCurrency();
		String getEstimatedSevenDayRevenue();
		String getLastUpdateDateUTC();
		Map<String,List<KeyProductStatsReportLineItem>> getBaseToKeySkuStatsListMap(); 
	}
	
	public interface KeyProductStatsReportLineItem extends
			Serializable {

		//String getSupplierKCode();
		//String getSupplierName();

		String getMarketplaceSku();

		String getBaseCode();
		String getBaseCodeByDrs();

		String getSupplierKCode();
		String getSupplierName();

		String getSkuCode();
		String getSkuCodeByDrs();
		String getSkuName();
		String getCurrentBaseRetailPrice();
		String getQtyOrderedInCurrentSettlementPeriod();
		String getQtyOrderedInLastSevenDays();
		String getQtyToReceive();
		String getQtyInStock();
		String getFbaQtyInBound();
		String getFbaQtyInStock();
		String getFbaQtyTransfer();
		BigDecimal getNumericCurrentBaseRetailPrice();
		BigDecimal getNumericQtyOrderedInLastSevenDays();
	}
	
}
