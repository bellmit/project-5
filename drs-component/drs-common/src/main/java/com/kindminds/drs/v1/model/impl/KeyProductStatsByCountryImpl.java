package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport.KeyProductStatsByCountry;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport.KeyProductStatsReportLineItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class KeyProductStatsByCountryImpl implements KeyProductStatsByCountry {
	
	private Country country = null;
	private Currency currency = null;
	private BigDecimal estimatedSevenDayRevenue=null;
	private String lastUpdateDateUtc = null;
	private Map<String,List<KeyProductStatsReportLineItem>> baseToKeySkuStatsListMap;
	
	public void setCountry(Country country) {this.country = country;}
	
	public void setCurrency(Currency currency) {this.currency = currency;}
	
	public void setEstimatedSevenDayRevenue(BigDecimal value) {this.estimatedSevenDayRevenue = value;}
	
	public void setLastUpdateDateUtc(String date) {this.lastUpdateDateUtc = date;}
	
	public void setBaseToKeySkuStatsListMap(Map<String, List<KeyProductStatsReportLineItem>> map) {this.baseToKeySkuStatsListMap = map;}

	@Override
	public String toString() {
		return "KeyProductStatsByCountryImpl [getCountry()=" + getCountry() + ", getCurrency()=" + getCurrency()
				+ ", getEstimatedSevenDayRevenue()=" + getEstimatedSevenDayRevenue() + ", getLastUpdateDateUTC()="
				+ getLastUpdateDateUTC() + ", getBaseToKeySkuStatsListMap()=" + getBaseToKeySkuStatsListMap() + "]";
	}

	@Override
	public String getCountry() {
		return this.country.name();
	}

	@Override
	public String getCurrency() {
		return this.currency.name();
	}

	@Override
	public String getEstimatedSevenDayRevenue() {
		if(this.estimatedSevenDayRevenue==null) return null;
		return this.estimatedSevenDayRevenue.setScale(this.currency.getScale()).toPlainString();
	}

	@Override
	public String getLastUpdateDateUTC() {
		return this.lastUpdateDateUtc;
	}

	@Override
	public Map<String, List<KeyProductStatsReportLineItem>> getBaseToKeySkuStatsListMap() {
		return this.baseToKeySkuStatsListMap;
	}

}
