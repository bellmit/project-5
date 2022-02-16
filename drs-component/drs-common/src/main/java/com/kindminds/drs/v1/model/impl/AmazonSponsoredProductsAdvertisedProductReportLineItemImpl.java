package com.kindminds.drs.v1.model.impl;


import com.kindminds.drs.api.v1.model.amazon.AmazonSponsoredProductsAdvertisedProductReportLineItem;
import com.kindminds.drs.util.DateHelper;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class AmazonSponsoredProductsAdvertisedProductReportLineItemImpl implements AmazonSponsoredProductsAdvertisedProductReportLineItem {

	private Date startDate;
	private Date endDate;
	private String portfolioName;
	private String currency;
	private String campaignName;
	private String adGroupName;
	private String advertisedSku;
	private String advertisedAsin;
	private Integer impressions;
	private Integer clicks;
	private BigDecimal ctr;
	private BigDecimal cpc;
	private BigDecimal spend;
	private BigDecimal sevenDayTotalSales;
	private BigDecimal acos;
	private BigDecimal roas;
	private Integer sevenDayTotalOrders;
	private Integer sevenDayTotalUnits;
	private BigDecimal sevenDayConversionRate;
	private Integer sevenDayAdvertisedSkuUnits;	
	private Integer sevenDayOtherSkuUnits;	
	private BigDecimal sevenDayAdvertisedSkuSales;	
	private BigDecimal sevenDayOtherSkuSales;
	
	public AmazonSponsoredProductsAdvertisedProductReportLineItemImpl(
			 String dateFormat,
			 String timezoneText,
			 Date date,
			 String portfolioName,
			 String currency,
			 String campaignName,			 
			 String adGroupName,
			 String advertisedSku,
			 String advertisedAsin,
			 String impressions,
			 String clicks,
			 String ctr,
			 String cpc,
			 String spend,
			 String sevenDayTotalSales,
			 String sevenDayTotalOrders,
			 String sevenDayTotalUnits,
			 String sevenDayConversionRate,
			 String sevenDayAdvertisedSkuUnits,	
			 String sevenDayOtherSkuUnits,
			 String sevenDayAdvertisedSkuSales,	
			 String sevenDayOtherSkuSales){

		//this.startDate = DateHelper.toDate(String.join(" ",date,timezoneText),dateFormat);
		//this.endDate = this.toEndDate(date, dateFormat, timezoneText);

		this.startDate = date;
		this.endDate = this.toEndDate(date);
		this.portfolioName = portfolioName;
		this.currency = currency;
		this.campaignName = campaignName;
		this.adGroupName = adGroupName;
		this.advertisedSku = advertisedSku; 
		this.advertisedAsin = advertisedAsin;
		this.impressions = new BigDecimal(impressions).intValue();
		this.clicks = new BigDecimal(clicks).intValue();
		this.ctr = this.toBigDecimal(ctr);
		this.cpc = this.setScale(cpc);
		this.spend = this.setScale(spend);
		this.sevenDayTotalSales = this.setScale(sevenDayTotalSales);
		this.sevenDayTotalOrders = new BigDecimal(sevenDayTotalOrders).intValue();
		this.sevenDayTotalUnits = new BigDecimal(sevenDayTotalUnits).intValue();
		this.sevenDayConversionRate = this.toBigDecimal(sevenDayConversionRate);
		this.sevenDayAdvertisedSkuUnits = new BigDecimal(sevenDayAdvertisedSkuUnits).intValue();
		this.sevenDayOtherSkuUnits = new BigDecimal(sevenDayOtherSkuUnits).intValue();
		this.sevenDayAdvertisedSkuSales = this.setScale(sevenDayAdvertisedSkuSales); 
		this.sevenDayOtherSkuSales = this.setScale(sevenDayOtherSkuSales);
	}
			
	private BigDecimal toBigDecimal(String numberStr){
		if(numberStr.isEmpty()) return null;
		return new BigDecimal(numberStr).setScale(4, BigDecimal.ROUND_HALF_UP);		
	}

	private BigDecimal setScale(String priceStr){
		if(priceStr.isEmpty()) return null;
		return new BigDecimal(priceStr).setScale(2, BigDecimal.ROUND_HALF_UP);		
	}
	
	private Date toEndDate(String dateStr,String dateFormat,String timezoneText){				
		Date date = DateHelper.toDate(String.join(" ",dateStr,timezoneText),dateFormat);
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		return DateHelper.toDate(String.join(" ",date.toString(),timezoneText),"EEE MMM dd HH:mm:ss z yyyy");	
	}
	private Date toEndDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		return date;
	}
		
	@Override
	public String toString() {
		return "AmazonSponsoredProductsAdvertisedProductReportLineItemImpl [getStartDate()=" + getStartDate()
				+ ", getEndDate()=" + getEndDate() + ", getCurrency()=" + getCurrency() + ", getCampaignName()="
				+ getCampaignName() + ", getAdGroupName()=" + getAdGroupName() + ", getAdvertisedSku()="
				+ getAdvertisedSku() + ", getAdvertisedAsin()=" + getAdvertisedAsin() + ", getImpressions()="
				+ getImpressions() + ", getClicks()=" + getClicks() + ", getClickThruRateCtr()=" + getClickThruRateCtr()
				+ ", getCostPerClickCpc()=" + getCostPerClickCpc() + ", getSpend()=" + getSpend()
				+ ", getSevenDayTotalSales()=" + getSevenDayTotalSales() + ", getTotalAdvertisingCostOfSalesAcos()="
				+ getTotalAdvertisingCostOfSalesAcos() + ", getTotalReturnOnAdvertisingSpendRoas()="
				+ getTotalReturnOnAdvertisingSpendRoas() + ", getSevenDayTotalOrders()=" + getSevenDayTotalOrders()
				+ ", getSevenDayTotalUnits()=" + getSevenDayTotalUnits() + ", getSevenDayConversionRate()="
				+ getSevenDayConversionRate() + ", getSevenDayAdvertisedSkuUnits()=" + getSevenDayAdvertisedSkuUnits()
				+ ", getSevenDayOtherSkuUnits()=" + getSevenDayOtherSkuUnits() + ", getSevenDayAdvertisedSkuSales()="
				+ getSevenDayAdvertisedSkuSales() + ", getSevenDayOtherSkuSales()=" + getSevenDayOtherSkuSales() + "]";
	}

	@Override
	public Date getStartDate() {
		return this.startDate;
	}

	@Override
	public Date getEndDate() {
		return this.endDate;
	}
	
	@Override
	public String getPortfolioName() {
		return this.portfolioName;
	}
	
	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getCampaignName() {
		return this.campaignName;
	}

	@Override
	public String getAdGroupName() {
		return this.adGroupName;
	}

	@Override
	public String getAdvertisedSku() {
		return this.advertisedSku;
	}

	@Override
	public String getAdvertisedAsin() {
		return this.advertisedAsin;
	}

	@Override
	public Integer getImpressions() {
		return this.impressions;
	}

	@Override
	public Integer getClicks() {
		return this.clicks;
	}

	@Override
	public BigDecimal getClickThruRateCtr() {
		return this.ctr;
	}

	@Override
	public BigDecimal getCostPerClickCpc() {
		return this.cpc;
	}

	@Override
	public BigDecimal getSpend() {
		return this.spend;
	}

	@Override
	public BigDecimal getSevenDayTotalSales() {
		return this.sevenDayTotalSales;
	}

	@Override
	public BigDecimal getTotalAdvertisingCostOfSalesAcos() {
		return this.acos;
	}

	@Override
	public BigDecimal getTotalReturnOnAdvertisingSpendRoas() {
		return this.roas;
	}

	@Override
	public Integer getSevenDayTotalOrders() {
		return this.sevenDayTotalOrders;
	}

	@Override
	public Integer getSevenDayTotalUnits() {
		return this.sevenDayTotalUnits;
	}

	@Override
	public BigDecimal getSevenDayConversionRate() {
		return this.sevenDayConversionRate;
	}

	@Override
	public Integer getSevenDayAdvertisedSkuUnits() {
		return this.sevenDayAdvertisedSkuUnits;
	}

	@Override
	public Integer getSevenDayOtherSkuUnits() {
		return this.sevenDayOtherSkuUnits;
	}

	@Override
	public BigDecimal getSevenDayAdvertisedSkuSales() {
		return this.sevenDayAdvertisedSkuSales;
	}

	@Override
	public BigDecimal getSevenDayOtherSkuSales() {
		return this.sevenDayOtherSkuSales;
	}

}
