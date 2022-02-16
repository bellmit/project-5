package com.kindminds.drs.web.view.marketing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmazonHsaCampaignReportItem implements Serializable{

	private static final long serialVersionUID = -6727720158233158691L;

	private int id;
	private String supplierKcode;

	private String marketplaceName;
	private Integer marketplaceId;
	private String reportDate;

	private String currencyName;
	private String campaignName;

	private Integer impressions;
	private Integer clicks;
	private BigDecimal clickThruRate;
	private BigDecimal costPerClick;
	private BigDecimal totalSpend;
	private BigDecimal totalACOS;
	private BigDecimal totalRoAS;
	private BigDecimal totalSales14days;
	private Integer totalOrders14days;
	private Integer totalUnits14days;
	private BigDecimal conversionRate14days;


	/*
	*   @Id


    @Column(name="marketplace_name")


    @Column(name="marketplace_id")
    private Integer marketplaceId;


    @Column(name="campaign_name")
    private String campaignName;

    @Column(name="report_date")
    private String reportDate;
    @Column(name="currency_name")
    private String currencyName;

    @Column(name="impressions")
    private Integer impressions;

    @Column(name="clicks")
    private Integer clicks;

    @Column(name="click_thru_rate")
    private BigDecimal ctr;

    @Column(name="cost_per_click")
    private BigDecimal cpc;

    @Column(name="spend")
    private BigDecimal totalSpend;

    @Column(name="total_advertising_cost_of_sales")
    private BigDecimal totalACOS;

    @Column(name="total_return_on_advertising_spend")
    private BigDecimal totalRoAS;

    @Column(name="total_sales_14days")
    private BigDecimal totalSales14days;

    @Column(name="total_orders_14days")
    private Integer totalOrders14days;

    @Column(name="total_units_14days")
    private Integer totalUnits14days;

    @Column(name="conversion_rate_14days")
    private BigDecimal conversionRate14days;
	* */




	public AmazonHsaCampaignReportItem(){
		
	}

	@Override
	public String toString() {
		return "AmazonHsaCampaignReportItem [" +
				" getId()=" + getId() +"," +" getSupplierKcode()=" + getSupplierKcode() +","+" getMarketplaceName()=" +getMarketplaceName() +"," +
				"getMarketplaceId()=" + getMarketplaceId() + ", getReportDate()="
				+ getReportDate() + ", getCurrencyName()=" + getCurrencyName() + ", getCampaignName()=" + getCampaignName()
				+ ", getImpressions()="
				+ getImpressions() + ", getClicks()=" + getClicks() + ", getClickThruRate()=" + getClickThruRate()
				+ ", getCostPerClick()=" + getCostPerClick() + ", getTotalSpend()=" + getTotalSpend() + ", getTotalACOS()="
				+ getTotalACOS() + ", getTotalRoAS()=" + getTotalRoAS() + ", getTotalSales14days()="
				+ getTotalSales14days() + ", getTotalOrders14days()=" + getTotalOrders14days()
				+ ", getTotalUnits14days()=" + getTotalUnits14days() + ", getConversionRate14days()="
				+ getConversionRate14days() + "]";
	}


	public Integer getId(){
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSupplierKcode(){
		return this.supplierKcode;
	}

	public void setSupplierKcode(String supplierKcode){
		this.supplierKcode = supplierKcode;
	}

	public String getMarketplaceName(){
		return this.marketplaceName;
	}

	public void setMarketplaceName(String marketplaceName){
		this.marketplaceName = marketplaceName;
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
	
	public String getCurrencyName(){
		return this.currencyName;
	}
	
	public void setCurrencyName(String currency){
		this.currencyName = currency;
	}
		
	public String getCampaignName(){
		return this.campaignName;
	}
		
	public void setCampaignName(String campaignName){
		this.campaignName = campaignName;
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
		
	public BigDecimal getTotalSpend(){
		return this.totalSpend;
	}
	
	public void setTotalSpend(BigDecimal spend){
		this.totalSpend = spend;
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
	

	
	
	
}