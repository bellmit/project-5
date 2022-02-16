package com.kindminds.drs.web.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesAndPageTrafficReportCondition {
		
	private String supplierKcode;
	private String baseProduct;
	private String sku;
	private String marketplaceId = "1";	//Amazon.com		
	private String timeScale = "2";		//Week
	private String trafficType = "3";	//Ordered Items
	private String dateRange = "5";		//Lifetime
	private String startDate;
	private String endDate;
	private int totalOrAverage = 1;		//Total
	
	public String getSupplierKcode() {		
		return StringUtils.isEmpty(this.supplierKcode)?null:this.supplierKcode;		
	}
	
	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode; 		
	}
	
	public String getBaseProduct() {		
		return StringUtils.isEmpty(this.baseProduct)?null:this.baseProduct;		
	}
	
	public void setBaseProduct(String baseProduct) {
		this.baseProduct = baseProduct; 		
	}
	
	public String getSku() {		
		return StringUtils.isEmpty(this.sku)?null:this.sku;		
	}
	
	public void setSku(String sku) {
		this.sku = sku; 		
	}
	
	public String getMarketplaceId() {
		return StringUtils.isEmpty(this.marketplaceId)?null:this.marketplaceId;
	}

	public void setMarketplaceId(String marketplaceId) {
		this.marketplaceId = marketplaceId;		
	}

	public String getTimeScale() {		
		return StringUtils.isEmpty(this.timeScale)?null:this.timeScale;		
	}
	
	public void setTimeScale(String timeScale) {
		this.timeScale = timeScale; 		
	}
	
	public String getTrafficType() {		
		return StringUtils.isEmpty(this.trafficType)?null:this.trafficType;		
	}
	
	public void setTrafficType(String trafficType) {
		this.trafficType = trafficType; 		
	}

	public String getDateRange() {
		return StringUtils.isEmpty(this.dateRange)?null:this.dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public String getStartDate() {
		return StringUtils.isEmpty(this.startDate)?null:this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return StringUtils.isEmpty(this.endDate)?null:this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getTotalOrAverage() {
		return totalOrAverage;
	}
	
	public void setTotalOrAverage(int totalOrAverage) {
		this.totalOrAverage = totalOrAverage;
	}

	@Override
	public String toString() {
		return "SalesAndPageTrafficReportCondition{" +
				"supplierKcode='" + supplierKcode + '\'' +
				", baseProduct='" + baseProduct + '\'' +
				", sku='" + sku + '\'' +
				", marketplaceId='" + marketplaceId + '\'' +
				", timeScale='" + timeScale + '\'' +
				", trafficType='" + trafficType + '\'' +
				", dateRange='" + dateRange + '\'' +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", totalOrAverage=" + totalOrAverage +
				'}';
	}
}
