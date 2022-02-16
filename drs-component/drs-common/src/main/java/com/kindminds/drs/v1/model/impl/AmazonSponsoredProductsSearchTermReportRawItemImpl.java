package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.amazon.AmazonSponsoredProductsSearchTermReportRawItem;
import com.kindminds.drs.util.DateHelper;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonSponsoredProductsSearchTermReportRawItemImpl implements AmazonSponsoredProductsSearchTermReportRawItem{

	private Date startDate;
	private Date endDate;
	private String portfolioName;
	private String currency;
	private String campaignName;
	private String adGroupName;
	private String keyword;
	private String matchType;
	private String customerSearchTerm;
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
	
	public AmazonSponsoredProductsSearchTermReportRawItemImpl(
				String dateFormat,
				String timezoneText,
				String startDate,
				String endDate,
				String portfolioName,
				String currency,
				String campaignName,
				String adGroupName,
				String keyword,
				String matchType,
				String customerSearchTerm,
				String impressions,
				String clicks,
				String ctr,
				String cpc,
				String spend,
				String sevenDayTotalSales,
				String acos,			
				String sevenDayTotalOrders,
				String sevenDayConversionRate,
				String sevenDayAdvertisedSkuUnits,
				String sevenDayOtherSkuUnits,
				String sevenDayAdvertisedSkuSales,
				String sevenDayOtherSkuSales){
		this.startDate = DateHelper.toDate(String.join(" ",startDate,timezoneText),dateFormat);
		this.endDate = DateHelper.toDate(String.join(" ",endDate,timezoneText),dateFormat);
		this.portfolioName = portfolioName;
		this.currency = currency;
		this.campaignName = campaignName;
		this.adGroupName = adGroupName;
		this.keyword = keyword;
		this.matchType = matchType;
		this.customerSearchTerm = customerSearchTerm;
		this.impressions = new BigDecimal(impressions).intValue();
		this.clicks = new BigDecimal(clicks).intValue();
		this.ctr = this.toBigDecimal(ctr);
		this.cpc = this.setScale(cpc);
		this.spend = this.setScale(spend);
		this.sevenDayTotalSales = this.setScale(sevenDayTotalSales);
		this.acos = this.toBigDecimal(acos);
		this.sevenDayTotalOrders = new BigDecimal(sevenDayTotalOrders).intValue();
		this.sevenDayConversionRate = this.toBigDecimal(sevenDayConversionRate);
		this.sevenDayAdvertisedSkuUnits = new BigDecimal(sevenDayAdvertisedSkuUnits).intValue();
		this.sevenDayOtherSkuUnits = new BigDecimal(sevenDayOtherSkuUnits).intValue();
		this.sevenDayAdvertisedSkuSales = this.setScale(sevenDayAdvertisedSkuSales); 
		this.sevenDayOtherSkuSales = this.setScale(sevenDayOtherSkuSales);
	};
	
	private BigDecimal toBigDecimal(String numberStr){
		if(numberStr.isEmpty()) return null;
		return new BigDecimal(numberStr).setScale(4, BigDecimal.ROUND_HALF_UP);		
	}

	private BigDecimal setScale(String priceStr){
		if(priceStr.isEmpty()) return null;
		return new BigDecimal(priceStr).setScale(2, BigDecimal.ROUND_HALF_UP);		
	}
		
	@Override
	public String toString() {
		return "AmazonSponsoredProductsSearchTermReportRawItemImpl [getStartDate()=" + getStartDate()
				+ ", getEndDate()=" + getEndDate() + ", getCurrency()=" + getCurrency() + ", getCampaignName()="
				+ getCampaignName() + ", getAdGroupName()=" + getAdGroupName() + ", getKeyword()=" + getKeyword()
				+ ", getMatchType()=" + getMatchType() + ", getCustomerSearchTerm()=" + getCustomerSearchTerm()
				+ ", getImpressions()=" + getImpressions() + ", getClicks()=" + getClicks() + ", getCtr()=" + getCtr()
				+ ", getCpc()=" + getCpc() + ", getSpend()=" + getSpend() + ", getSevenDayTotalSales()="
				+ getSevenDayTotalSales() + ", getAcos()=" + getAcos() + ", getRoas()=" + getRoas()
				+ ", getSevenDayTotalOrders()=" + getSevenDayTotalOrders() + ", getSevenDayTotalUnits()="
				+ getSevenDayTotalUnits() + ", getSevenDayConversionRate()=" + getSevenDayConversionRate()
				+ ", getSevenDayAdvertisedSkuUnits()=" + getSevenDayAdvertisedSkuUnits()
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
	public String getKeyword() {
		return this.keyword;
	}

	@Override
	public String getMatchType() {
		return this.matchType;
	}

	@Override
	public String getCustomerSearchTerm() {
		return this.customerSearchTerm;
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
	public BigDecimal getCtr() {
		return this.ctr;
	}

	@Override
	public BigDecimal getCpc() {
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
	public BigDecimal getAcos() {
		return this.acos;
	}

	@Override
	public BigDecimal getRoas() {
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
