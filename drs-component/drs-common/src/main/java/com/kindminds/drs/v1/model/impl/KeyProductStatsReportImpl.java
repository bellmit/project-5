package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport;

import java.util.List;

public class KeyProductStatsReportImpl implements KeyProductStatsReport {
	
	private String nextStatementPeriod = null;
	private List<KeyProductStatsByCountry> statsByCountry = null;
	
	public void setNextStatementPeriod(String period){
		this.nextStatementPeriod = period;
	}
	
	public void setKeyProductStatsByCountryList(List<KeyProductStatsByCountry> list){
		this.statsByCountry = list;
	}
	
	@Override
	public String toString() {
		return "KeyProductStatsReportImpl [getNextSettlementPeriod()=" + getNextSettlementPeriod()
				+ ", getKeyProductStatsByCountryList()=" + getKeyProductStatsByCountryList() + "]";
	}

	@Override
	public String getNextSettlementPeriod() {
		return this.nextStatementPeriod;
	}
	
	@Override
	public List<KeyProductStatsByCountry> getKeyProductStatsByCountryList() {
		return this.statsByCountry;
	}



}
