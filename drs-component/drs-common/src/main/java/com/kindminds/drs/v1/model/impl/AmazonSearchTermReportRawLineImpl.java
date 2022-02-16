package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.amazon.AmazonSearchTermReportRawLine;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonSearchTermReportRawLineImpl implements AmazonSearchTermReportRawLine {

	private String campaignName;
	private String adGroupName;
	private String customerSearchTerm;
	private String keyword;
	private String matchType;
	private Date firstDayOfImpression;
	private Date lastDayOfImpression;
	private Integer impressions;
	private Integer clicks;
	private BigDecimal ctr;
	private BigDecimal totalSpend;
	private BigDecimal averageCpc;
	private BigDecimal acos;
	private String currency;
	private Integer ordersPlacedWithinOneWeekOfAClick;
	private BigDecimal productSalesWithinOneWeekOfAClick;
	private BigDecimal conversionRateWithinOneWeekOfAClick;
	private Integer sameSkuUnitsOrderedWithinOneWeekOfClick;
	private Integer otherSkuUnitsOrderedWithinOneWeekOfClick;
	private BigDecimal sameSkuUnitsProductSalesWithinOneWeekOfClick;
	private BigDecimal otherSkuUnitsProductSalesWithinOneWeekOfClick;

	public AmazonSearchTermReportRawLineImpl(
			String campaignName,
			String adGroupName,
			String customerSearchTerm,
			String keyword,
			String matchType,
			Date firstDayOfImpression,
			Date lastDayOfImpression,
			String impressions,
			String clicks,
			String ctr,
			BigDecimal totalSpend,
			BigDecimal averageCpc,
			String acos,
			String currency,
			String ordersPlacedWithinOneWeekOfAClick,
			BigDecimal productSalesWithinOneWeekOfAClick,
			String conversionRateWithinOneWeekOfAClick,
			String sameSkuUnitsOrderedWithinOneWeekOfClick,
			String otherSkuUnitsOrderedWithinOneWeekOfClick,
			BigDecimal sameSkuUnitsProductSalesWithinOneWeekOfClick,
			BigDecimal otherSkuUnitsProductSalesWithinOneWeekOfClick){
		this.campaignName = campaignName;
		this.adGroupName = adGroupName;
		this.customerSearchTerm = customerSearchTerm;
		this.keyword = keyword;
		this.matchType = matchType;
		this.firstDayOfImpression = firstDayOfImpression;
		this.lastDayOfImpression = lastDayOfImpression;
		this.impressions = Integer.valueOf(impressions);
		this.clicks = Integer.valueOf(clicks);
		this.ctr = this.toBigDecimal(ctr);
		this.totalSpend = totalSpend;
		this.averageCpc = averageCpc;
		this.acos = this.toBigDecimal(acos);
		this.currency = currency;
		this.ordersPlacedWithinOneWeekOfAClick = Integer.valueOf(ordersPlacedWithinOneWeekOfAClick);
		this.productSalesWithinOneWeekOfAClick = productSalesWithinOneWeekOfAClick;
		this.conversionRateWithinOneWeekOfAClick = this.toBigDecimal(conversionRateWithinOneWeekOfAClick);
		this.sameSkuUnitsOrderedWithinOneWeekOfClick = Integer.valueOf(sameSkuUnitsOrderedWithinOneWeekOfClick);
		this.otherSkuUnitsOrderedWithinOneWeekOfClick = Integer.valueOf(otherSkuUnitsOrderedWithinOneWeekOfClick);
		this.sameSkuUnitsProductSalesWithinOneWeekOfClick = sameSkuUnitsProductSalesWithinOneWeekOfClick;
		this.otherSkuUnitsProductSalesWithinOneWeekOfClick = otherSkuUnitsProductSalesWithinOneWeekOfClick;
	}
	
	private BigDecimal toBigDecimal(String percentageStr){
		char lastChar = percentageStr.charAt(percentageStr.length()-1);
		Assert.isTrue(lastChar=='%');
		BigDecimal percentage = new BigDecimal(percentageStr.substring(0,percentageStr.length()-1)); 
		return percentage.divide(new BigDecimal("100"));
	}
	
	@Override
	public String toString() {
		return "AmazonSearchTermReportRawLineImpl [getCampaignName()=" + getCampaignName() + ", getAdGroupName()="
				+ getAdGroupName() + ", getCustomerSearchTerm()=" + getCustomerSearchTerm() + ", getKeyword()="
				+ getKeyword() + ", getMatchType()=" + getMatchType() + ", getFirstDayOfImpression()="
				+ getFirstDayOfImpression() + ", getLastDayOfImpression()=" + getLastDayOfImpression()
				+ ", getImpressions()=" + getImpressions() + ", getClicks()=" + getClicks() + ", getCtr()=" + getCtr()
				+ ", getTotalSpend()=" + getTotalSpend() + ", getAverageCpc()=" + getAverageCpc() + ", getAcos()="
				+ getAcos() + ", getCurrency()=" + getCurrency() + ", getOrdersPlacedWithinOneWeekOfAClick()="
				+ getOrdersPlacedWithinOneWeekOfAClick() + ", getProductSalesWithinOneWeekOfAClick()="
				+ getProductSalesWithinOneWeekOfAClick() + ", getConversionRateWithinOneWeekOfAClick()="
				+ getConversionRateWithinOneWeekOfAClick() + ", getSameSkuUnitsOrderedWithinOneWeekOfClick()="
				+ getSameSkuUnitsOrderedWithinOneWeekOfClick() + ", getOtherSkuUnitsOrderedWithinOneWeekOfClick()="
				+ getOtherSkuUnitsOrderedWithinOneWeekOfClick() + ", getSameSkuUnitsProductSalesWithinOneWeekOfClick()="
				+ getSameSkuUnitsProductSalesWithinOneWeekOfClick()
				+ ", getOtherSkuUnitsProductSalesWithinOneWeekOfClick()="
				+ getOtherSkuUnitsProductSalesWithinOneWeekOfClick() + "]";
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
	public String getCustomerSearchTerm() {
		return this.customerSearchTerm;
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
	public Date getFirstDayOfImpression() {
		return this.firstDayOfImpression;
	}

	@Override
	public Date getLastDayOfImpression() {
		return this.lastDayOfImpression;
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
	public BigDecimal getTotalSpend() {
		return this.totalSpend;
	}

	@Override
	public BigDecimal getAverageCpc() {
		return this.averageCpc;
	}

	@Override
	public BigDecimal getAcos() {
		return this.acos;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public Integer getOrdersPlacedWithinOneWeekOfAClick() {
		return this.ordersPlacedWithinOneWeekOfAClick;
	}

	@Override
	public BigDecimal getProductSalesWithinOneWeekOfAClick() {
		return this.productSalesWithinOneWeekOfAClick;
	}

	@Override
	public BigDecimal getConversionRateWithinOneWeekOfAClick() {
		return this.conversionRateWithinOneWeekOfAClick;
	}

	@Override
	public Integer getSameSkuUnitsOrderedWithinOneWeekOfClick() {
		return this.sameSkuUnitsOrderedWithinOneWeekOfClick;
	}

	@Override
	public Integer getOtherSkuUnitsOrderedWithinOneWeekOfClick() {
		return this.otherSkuUnitsOrderedWithinOneWeekOfClick;
	}

	@Override
	public BigDecimal getSameSkuUnitsProductSalesWithinOneWeekOfClick() {
		return this.sameSkuUnitsProductSalesWithinOneWeekOfClick;
	}

	@Override
	public BigDecimal getOtherSkuUnitsProductSalesWithinOneWeekOfClick() {
		return this.otherSkuUnitsProductSalesWithinOneWeekOfClick;
	}

}
