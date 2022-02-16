package com.kindminds.drs.web.view.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;



public class KeyProductStatsByCountry{

	
	private Country country = null;
	private Currency currency = null;
	private String estimatedSevenDayRevenue=null;
	
	private String lastUpdateDateUTC = null;

	
	private Map<String,List<KeyProductStatsReportLineItem>> baseToKeySkuStatsListMap;
	
	public void setCountry(Country country) {this.country = country;}
	
	public void setCurrency(Currency currency) {this.currency = currency;}
	
	public void setEstimatedSevenDayRevenue(String value) {
		this.estimatedSevenDayRevenue = value;}
	
	public void setLastUpdateDateUtc(String lastUpdateDateUTC) {this.lastUpdateDateUTC = lastUpdateDateUTC;}
	
	public void setBaseToKeySkuStatsListMap(Map<String, List<KeyProductStatsReportLineItem>> map) {this.baseToKeySkuStatsListMap = map;}

	

	public String getCountry() {
		return this.country.name();
	}

	
	public String getCurrency() {
		return this.currency.name();
	}

	
	public String getEstimatedSevenDayRevenue() {
		return estimatedSevenDayRevenue;
	}

	
	public String getLastUpdateDateUTC() {
		return this.lastUpdateDateUTC;
	}

	
	public Map<String, List<KeyProductStatsReportLineItem>> getBaseToKeySkuStatsListMap() {
		return this.baseToKeySkuStatsListMap;
	}

}
