package com.kindminds.drs.web.view.marketing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class AmazonHsaCampaignReport implements Serializable{
	
	private static final long serialVersionUID = 1325642480535311371L;
	private Integer totalImpressions;
	private Integer totalClicks;
	private BigDecimal totalClickThruRate;
	private BigDecimal totalCostPerClick;
	private BigDecimal totalSpend;
	private BigDecimal totalSales14days;
	private Integer totalOrders14days;
	private Integer totalUnits14days;
	private BigDecimal totalConversionRate14days;
	private String currency;
	private List<AmazonHsaCampaignReportItem> lineItems = null;
				
	@Override
	public String toString() {
		return "AmazonHsaCampaignReport [getTotalImpressions()=" + getTotalImpressions()
				+ ", getTotalClicks()=" + getTotalClicks() + ", getTotalClickThruRate()=" + getTotalClickThruRate()
				+ ", getTotalCostPerClick()=" + getTotalCostPerClick() + ", getTotalSpend()=" + getTotalSpend()
				+ ", getTotalSales14days()=" + getTotalSales14days() + ", getTotalOrders14days()="
				+ getTotalOrders14days() + ", getTotalUnits14days()=" + getTotalUnits14days()
				+ ", getTotalConversionRate14days()=" + getTotalConversionRate14days() + ", getCurrency()="
				+ getCurrency() + ", gethsaCampaignReportItems()=" + getHsaCampaignReportItems()
				+ "]";
	}

	public Integer getTotalImpressions(){
		return this.totalImpressions;
	}
	
	public void setTotalImpressions(Integer totalImpressions){
		this.totalImpressions = totalImpressions;
	}
		
	public Integer getTotalClicks(){
		return this.totalClicks;
	}
	
	public void setTotalClicks(Integer totalClicks){
		this.totalClicks = totalClicks;
	}
		
	public String getTotalClickThruRate(){
		if(this.totalClickThruRate==null) return "N/A";
		return this.totalClickThruRate.setScale(2, RoundingMode.HALF_UP).toPlainString()+"%";
	}
	
	public void setTotalClickThruRate(BigDecimal totalClickThruRate){
		this.totalClickThruRate = totalClickThruRate;
	}
		
	public String getTotalCostPerClick(){
		if(this.totalCostPerClick==null) return "N/A";
		return this.totalCostPerClick.toPlainString();
	};
		
	public void setTotalCostPerClick(BigDecimal totalCostPerClick){
		this.totalCostPerClick = totalCostPerClick;
	}
		
	public BigDecimal getTotalSpend(){
		return this.totalSpend;
	}
	
	public void setTotalSpend(BigDecimal totalSpend){
		this.totalSpend = totalSpend;
	}
		
	public BigDecimal getTotalSales14days(){
		return this.totalSales14days;
	}
	
	public void setTotalSales14days(BigDecimal totalSales14days){
		this.totalSales14days = totalSales14days;
	}
		
	public Integer getTotalOrders14days(){
		return this.totalOrders14days;
	}
	
	public void setTotalOrders14days(Integer totalOrders14days){
		this.totalOrders14days = totalOrders14days;
	}
		
	public Integer getTotalUnits14days(){
		return this.totalUnits14days;
	}
	
	public void setTotalUnits14days(Integer totalUnits14days){
		this.totalUnits14days = totalUnits14days;
	}
		
	public String getTotalConversionRate14days(){
		if(this.totalConversionRate14days==null) return "N/A";
		return this.totalConversionRate14days.setScale(2, RoundingMode.HALF_UP).toPlainString()+"%";
	}
	
	public void setTotalConversionRate14days(BigDecimal totalConversionRate14days){
		this.totalConversionRate14days = totalConversionRate14days;
	}
	
	public String getCurrency(){
		return this.currency;
	}
	
	public void setCurrency(String currency){
		this.currency = currency;		
	}
	
	public List<AmazonHsaCampaignReportItem> getHsaCampaignReportItems(){
		return this.lineItems;
	}
	
	public void setHsaCampaignReportItems(List<AmazonHsaCampaignReportItem> lineItems){
		this.lineItems = lineItems;
	}
	
}