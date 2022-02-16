package com.kindminds.drs.web.view.marketing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmazonHeadlineSearchAdReportItem implements Serializable{

	private static final long serialVersionUID = -6727720158233158691L;
	private Integer marketplaceId;
	private String reportDate;
	private String portfolioName;
	private String currency;
	private String campaignName;
	private String targeting;
	private String matchType;
	private Integer impressions;
	private Integer clicks;
	private BigDecimal clickThruRate;
	private BigDecimal costPerClick;
	private BigDecimal spend;
	private BigDecimal totalACOS;
	private BigDecimal totalRoAS;
	private BigDecimal totalSales14days;
	private Integer totalOrders14days;
	private Integer totalUnits14days;
	private BigDecimal conversionRate14days;
	private Integer ordersNewToBrand;
	private BigDecimal pctOrdersNewToBrand;
	private BigDecimal salesNewToBrand;
	private BigDecimal pctSalesNewToBrand;
	private Integer unitsNewToBrand;
	private BigDecimal pctUnitsNewToBrand;
	private BigDecimal orderRateNewToBrand;
	
	public AmazonHeadlineSearchAdReportItem(){
		
	}

	@Override
	public String toString() {
		return "AmazonHeadlineSearchAdReportItem [getMarketplaceId()=" + getMarketplaceId() + ", getReportDate()="
				+ getReportDate() + ", getCurrency()=" + getCurrency() + ", getCampaignName()=" + getCampaignName()
				+ ", getTargeting()=" + getTargeting() + ", getMatchType()=" + getMatchType() + ", getImpressions()="
				+ getImpressions() + ", getClicks()=" + getClicks() + ", getClickThruRate()=" + getClickThruRate()
				+ ", getCostPerClick()=" + getCostPerClick() + ", getSpend()=" + getSpend() + ", getTotalACOS()="
				+ getTotalACOS() + ", getTotalRoAS()=" + getTotalRoAS() + ", getTotalSales14days()="
				+ getTotalSales14days() + ", getTotalOrders14days()=" + getTotalOrders14days()
				+ ", getTotalUnits14days()=" + getTotalUnits14days() + ", getConversionRate14days()="
				+ getConversionRate14days() + ", getPortfolioName()=" + getPortfolioName() + "]";
	}



	public Integer getMarketplaceId(){
		return this.marketplaceId;
	}
	
	public void setMarketplaceId(Integer marketplaceId){
		this.marketplaceId = marketplaceId;
	}
		
	public String getReportDate(){
		return this.reportDate;
	}
	
	public void setReportDate(String reportDate){
		this.reportDate = reportDate;
	}
	
	public String getCurrency(){
		return this.currency;
	}
	
	public void setCurrency(String currency){
		this.currency = currency;
	}
		
	public String getCampaignName(){
		return this.campaignName;
	}
		
	public void setCampaignName(String campaignName){
		this.campaignName = campaignName;
	}
	
	public String getTargeting(){
		return this.targeting;
	}
	
	public void setTargeting(String targeting){
		this.targeting = targeting;
	}
		
	public String getMatchType(){
		return this.matchType;
	}
		
	public void setMatchType(String matchType){
		this.matchType = matchType;
	}
		
	public Integer getImpressions(){
		return this.impressions;
	}
	
	public void setImpressions(Integer impressions){
		this.impressions = impressions;
	}
		
	public Integer getClicks(){
		return this.clicks;
	}
	
	public void setClicks(Integer clicks){
		this.clicks = clicks;
	}
		
	public String getClickThruRate(){
		if(this.clickThruRate==null) return "N/A";
		return this.clickThruRate.setScale(2, RoundingMode.HALF_UP).toPlainString()+"%";
	}
	
	public void setClickThruRate(BigDecimal clickThruRate){
		this.clickThruRate = clickThruRate;
	} 
		
	public String getCostPerClick(){
		if(this.costPerClick==null) return "N/A";
		return this.costPerClick.toPlainString();
	}
	
	public void setCostPerClick(BigDecimal costPerClick){
		this.costPerClick = costPerClick;
	}
		
	public BigDecimal getSpend(){
		return this.spend;
	}
	
	public void setSpend(BigDecimal spend){
		this.spend = spend;
	}
		
	public BigDecimal getTotalACOS(){
		return this.totalACOS;
	}
	
	public void setTotalACOS(BigDecimal totalACOS){
		this.totalACOS = totalACOS;
	}
		
	public BigDecimal getTotalRoAS(){
		return this.totalRoAS;
	}
	
	public void setTotalRoAS(BigDecimal totalRoAS){
		this.totalRoAS = totalRoAS;
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
	
	public String getConversionRate14days(){
		if(this.conversionRate14days==null) return "N/A";
		return this.conversionRate14days.setScale(2, RoundingMode.HALF_UP).toPlainString()+"%";
	}
	
	public void setConversionRate14days(BigDecimal conversionRate14days){
		this.conversionRate14days = conversionRate14days;
	}
	
	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Integer getOrdersNewToBrand() {
		return ordersNewToBrand;
	}

	public void setOrdersNewToBrand(Integer ordersNewToBrand) {
		this.ordersNewToBrand = ordersNewToBrand;
	}

	public BigDecimal getPctOrdersNewToBrand() {
		return pctOrdersNewToBrand;
	}

	public void setPctOrdersNewToBrand(BigDecimal pctOrdersNewToBrand) {
		this.pctOrdersNewToBrand = pctOrdersNewToBrand;
	}

	public BigDecimal getSalesNewToBrand() {
		return salesNewToBrand;
	}

	public void setSalesNewToBrand(BigDecimal salesNewToBrand) {
		this.salesNewToBrand = salesNewToBrand;
	}

	public BigDecimal getPctSalesNewToBrand() {
		return pctSalesNewToBrand;
	}

	public void setPctSalesNewToBrand(BigDecimal pctSalesNewToBrand) {
		this.pctSalesNewToBrand = pctSalesNewToBrand;
	}

	public Integer getUnitsNewToBrand() {
		return unitsNewToBrand;
	}

	public void setUnitsNewToBrand(Integer unitsNewToBrand) {
		this.unitsNewToBrand = unitsNewToBrand;
	}

	public BigDecimal getPctUnitsNewToBrand() {
		return pctUnitsNewToBrand;
	}

	public void setPctUnitsNewToBrand(BigDecimal pctUnitsNewToBrand) {
		this.pctUnitsNewToBrand = pctUnitsNewToBrand;
	}

	public BigDecimal getOrderRateNewToBrand() {
		return orderRateNewToBrand;
	}

	public void setOrderRateNewToBrand(BigDecimal orderRateNewToBrand) {
		this.orderRateNewToBrand = orderRateNewToBrand;
	}
	
	
	
	
	
}