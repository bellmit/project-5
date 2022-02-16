package com.kindminds.drs.web.view.product;


import java.util.List;




public class KeyProductStatsReport {


	private String nextSettlementPeriod = null;
	
	 
	private List<KeyProductStatsByCountry> keyProductStatsByCountryList = null;
	
	public void setSextSettlementPeriod(String nextSettlementPeriod){
		this.nextSettlementPeriod = nextSettlementPeriod;
	}
	
	public void setKeyProductStatsByCountryList(List<KeyProductStatsByCountry> keyProductStatsByCountryList){
		this.keyProductStatsByCountryList = keyProductStatsByCountryList;
	}
	
	
	public String getNextSettlementPeriod() {
		return this.nextSettlementPeriod;
	}
	
	
	public List<KeyProductStatsByCountry> getKeyProductStatsByCountryList() {
		return this.keyProductStatsByCountryList;
	}



}
