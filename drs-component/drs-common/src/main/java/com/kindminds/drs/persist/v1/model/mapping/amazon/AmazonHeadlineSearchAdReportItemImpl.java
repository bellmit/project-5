package com.kindminds.drs.persist.v1.model.mapping.amazon;


import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;


public class AmazonHeadlineSearchAdReportItemImpl implements AmazonHeadlineSearchAdReportItem {
	//@Id ////@Column(name="id")
	private Integer id;
	//@Column(name="marketplace_id")
	private Integer marketplaceId;
	//@Column(name="report_date")
	private Date reportDate;
	//@Column(name="currency")
	private String currency;
	//@Column(name="campaign_name")
	private String campaignName;
	//@Column(name="targeting")
	private String targeting = null;
	//@Column(name="match_type")
	private String matchType = null;
	//@Column(name="impressions")
	private Integer impressions;
	//@Column(name="clicks")
	private Integer clicks;
	//@Column(name="click_thru_rate")
	private BigDecimal clickThruRate;
	//@Column(name="cost_per_click")
	private BigDecimal costPerClick;
	//@Column(name="spend")
	private BigDecimal spend;
	//@Column(name="total_advertising_cost_of_sales")
	private BigDecimal totalACOS;
	//@Column(name="total_return_on_advertising_spend")
	private BigDecimal totalRoAS;
	//@Column(name="total_sales_14days")
	private BigDecimal totalSales14days;
	//@Column(name="total_orders_14days")
	private Integer totalOrders14days;
	//@Column(name="total_units_14days")
	private Integer totalUnits14days;
	//@Column(name="conversion_rate_14days")
	private BigDecimal conversionRate14days;

	private Integer viewableImpressions;

	private BigDecimal viewThruRate;

	private BigDecimal clickThruRateForViews;

	private Integer videoFirstQuartileViews;

	private Integer videoMidpointViews;

	private Integer videoThirdQuartileViews;

	private Integer videoCompleteViews;

	private Integer videoUnmutes;

	private Integer views5second;

	private BigDecimal viewRate5second;

	//@Column(name="portfolio_name")
	private String portfolioName;

	//@Column(name="orders_new_to_brand")
	private Integer ordersNewToBrand;

	//@Column(name="pct_orders_new_to_brand")
	private BigDecimal pctOrdersNewToBrand;

	//@Column(name="sales_new_to_brand")
	private BigDecimal salesNewToBrand;

	//@Column(name="pct_sales_new_to_brand")
	private BigDecimal pctSalesNewToBrand;

	//@Column(name="units_new_to_brand")
	private Integer unitsNewToBrand;

	//@Column(name="pct_units_new_to_brand")
	private BigDecimal pctUnitsNewToBrand;

	//@Column(name="order_rate_new_to_brand")
	private BigDecimal orderRateNewToBrand;

	public AmazonHeadlineSearchAdReportItemImpl(){}

	public AmazonHeadlineSearchAdReportItemImpl(Integer id, Integer marketplaceId, Date reportDate, String currency, String campaignName, String targeting, String matchType, Integer impressions, Integer clicks, BigDecimal clickThruRate, BigDecimal costPerClick, BigDecimal spend, BigDecimal totalACOS, BigDecimal totalRoAS, BigDecimal totalSales14days, Integer totalOrders14days, Integer totalUnits14days, BigDecimal conversionRate14days) {
		this.id = id;
		this.marketplaceId = marketplaceId;
		this.reportDate = reportDate;
		this.currency = currency;
		this.campaignName = campaignName;
		this.targeting = targeting;
		this.matchType = matchType;
		this.impressions = impressions;
		this.clicks = clicks;
		this.clickThruRate = clickThruRate;
		this.costPerClick = costPerClick;
		this.spend = spend;
		this.totalACOS = totalACOS;
		this.totalRoAS = totalRoAS;
		this.totalSales14days = totalSales14days;
		this.totalOrders14days = totalOrders14days;
		this.totalUnits14days = totalUnits14days;
		this.conversionRate14days = conversionRate14days;
	}


	//KEYWORD
	public AmazonHeadlineSearchAdReportItemImpl(
			int marketplaceId,
			Date reportDate,
			String portfolioName,
			String currency,
			String campaignName,
			String targeting,
			String matchType,
			String impressions,
			String clicks,
			String clickThruRate,
			String costPerClick,
			String spend,
			String totalACOS,
			String totalRoAS,
			String totalSales14days,
			String totalOrders14days,
			String totalUnits14days,
			String conversionRate14days,
			String ordersNewToBrand,
			String pctOrdersNewToBrand,
			String salesNewToBrand,
			String pctSalesNewToBrand,
			String unitsNewToBrand,
			String pctUnitsNewToBrand,
			String orderRateNewToBrand) {
		this.marketplaceId = marketplaceId;
		this.reportDate = reportDate;
		this.portfolioName = portfolioName;
		this.currency = currency;
		this.campaignName = campaignName;
		this.targeting = targeting;
		this.matchType = matchType;
		this.impressions = new BigDecimal(impressions).intValue();
		this.clicks = new BigDecimal(clicks).intValue();
		this.clickThruRate = toBigDecimal(clickThruRate, 6);
		this.costPerClick = toBigDecimal(costPerClick, 2);
		this.spend = new BigDecimal(spend);
		this.totalACOS = toBigDecimal(totalACOS, 6);
		this.totalRoAS = toBigDecimal(totalRoAS, 2);
		this.totalSales14days = toBigDecimal(totalSales14days, 2);
		this.totalOrders14days = new BigDecimal(totalOrders14days).intValue();
		this.totalUnits14days = new BigDecimal(totalUnits14days).intValue();
		this.conversionRate14days = toBigDecimal(conversionRate14days, 6);
		this.ordersNewToBrand = new BigDecimal(ordersNewToBrand).intValue();
		this.pctOrdersNewToBrand = toBigDecimal(pctOrdersNewToBrand, 6);
		this.salesNewToBrand = toBigDecimal(salesNewToBrand, 2);
		this.pctSalesNewToBrand = toBigDecimal(pctSalesNewToBrand, 6);
		this.unitsNewToBrand = new BigDecimal(unitsNewToBrand).intValue();
		this.pctUnitsNewToBrand = toBigDecimal(pctUnitsNewToBrand, 6);
		this.orderRateNewToBrand = toBigDecimal(orderRateNewToBrand, 6);
	}

	//KEYWORD_VIDEO
	public AmazonHeadlineSearchAdReportItemImpl(
			int marketplaceId,
			Date reportDate,
			String portfolioName,
			String currency,
			String campaignName,
			String targeting,
			String matchType,
			String impressions,
			String clicks,
			String clickThruRate,
			String costPerClick,
			String spend,
			String totalACOS,
			String totalRoAS,
			String totalSales14days,
			String totalOrders14days,
			String totalUnits14days,
			String conversionRate14days) {
		this.marketplaceId = marketplaceId;
		this.reportDate = reportDate;
		this.portfolioName = portfolioName;
		this.currency = currency;
		this.campaignName = campaignName;
		this.targeting = targeting;
		this.matchType = matchType;
		this.impressions = new BigDecimal(impressions).intValue();
		this.clicks = new BigDecimal(clicks).intValue();
		this.clickThruRate = toBigDecimal(clickThruRate, 6);
		this.costPerClick = toBigDecimal(costPerClick, 2);
		this.spend = new BigDecimal(spend);
		this.totalACOS = toBigDecimal(totalACOS, 6);
		this.totalRoAS = toBigDecimal(totalRoAS, 2);
		this.totalSales14days = toBigDecimal(totalSales14days, 2);
		this.totalOrders14days = new BigDecimal(totalOrders14days).intValue();
		this.totalUnits14days = new BigDecimal(totalUnits14days).intValue();
		this.conversionRate14days = toBigDecimal(conversionRate14days, 6);
	}

	//CAMPAIGN_VIDEO
	public AmazonHeadlineSearchAdReportItemImpl(
			int marketplaceId,
			Date reportDate,
			String portfolioName,
			String currency,
			String campaignName,
			String impressions,
			String clicks,
			String clickThruRate,
			String costPerClick,
			String spend,
			String totalACOS,
			String totalRoAS,
			String totalSales14days,
			String totalOrders14days,
			String totalUnits14days,
			String conversionRate14days,
			String viewableImpressions,
			String viewThruRate,
			String clickThruRateForViews,
			String videoFirstQuartileViews,
			String videoMidpointViews,
			String videoThirdQuartileViews,
			String videoCompleteViews,
			String videoUnmutes,
			String views5second,
			String viewRate5second
			) {
		this.marketplaceId = marketplaceId;
		this.reportDate = reportDate;
		this.portfolioName = portfolioName;
		this.currency = currency;
		this.campaignName = campaignName;
		this.impressions = new BigDecimal(impressions).intValue();
		this.clicks = new BigDecimal(clicks).intValue();
		this.clickThruRate = toBigDecimal(clickThruRate, 6);
		this.costPerClick = toBigDecimal(costPerClick, 2);
		this.spend = new BigDecimal(spend);
		this.totalACOS = toBigDecimal(totalACOS, 6);
		this.totalRoAS = toBigDecimal(totalRoAS, 2);
		this.totalSales14days = toBigDecimal(totalSales14days, 2);
		this.totalOrders14days = new BigDecimal(totalOrders14days).intValue();
		this.totalUnits14days = new BigDecimal(totalUnits14days).intValue();
		this.conversionRate14days = toBigDecimal(conversionRate14days, 6);
		this.viewableImpressions = new BigDecimal(viewableImpressions).intValue();
		this.viewThruRate= toBigDecimal(viewThruRate,6);
		this.clickThruRateForViews = toBigDecimal(clickThruRateForViews,6);
		this.videoFirstQuartileViews = new BigDecimal(videoFirstQuartileViews).intValue();
		this.videoMidpointViews = new BigDecimal(videoMidpointViews).intValue();
		this.videoThirdQuartileViews = new BigDecimal(videoThirdQuartileViews).intValue();
		this.videoCompleteViews = new BigDecimal(videoCompleteViews).intValue();
		this.videoUnmutes = new BigDecimal(videoUnmutes).intValue();
		this.views5second = new BigDecimal(views5second).intValue();
		this.viewRate5second = toBigDecimal(viewRate5second,6);

	}

	//CAMPAIGN
	public AmazonHeadlineSearchAdReportItemImpl(
			int marketplaceId,
			Date reportDate,
			String portfolioName,
			String currency,
			String campaignName,
			String impressions,
			String clicks,
			String clickThruRate,
			String costPerClick,
			String spend,
			String totalACOS,
			String totalRoAS,
			String totalSales14days,
			String totalOrders14days,
			String totalUnits14days,
			String conversionRate14days,
			String ordersNewToBrand,
			String pctOrdersNewToBrand,
			String salesNewToBrand,
			String pctSalesNewToBrand,
			String unitsNewToBrand,
			String pctUnitsNewToBrand,
			String orderRateNewToBrand) {
		this.marketplaceId = marketplaceId;
		this.reportDate = reportDate;
		this.portfolioName = portfolioName;
		this.currency = currency;
		this.campaignName = campaignName;
		this.impressions = new BigDecimal(impressions).intValue();
		this.clicks = new BigDecimal(clicks).intValue();
		this.clickThruRate = toBigDecimal(clickThruRate, 6);
		this.costPerClick = toBigDecimal(costPerClick, 2);
		this.spend = new BigDecimal(spend);
		this.totalACOS = toBigDecimal(totalACOS, 6);
		this.totalRoAS = toBigDecimal(totalRoAS, 2);
		this.totalSales14days = toBigDecimal(totalSales14days, 2);
		this.totalOrders14days = new BigDecimal(totalOrders14days).intValue();
		this.totalUnits14days = new BigDecimal(totalUnits14days).intValue();
		this.conversionRate14days = toBigDecimal(conversionRate14days, 6);
		this.ordersNewToBrand = new BigDecimal(ordersNewToBrand).intValue();
		this.pctOrdersNewToBrand = toBigDecimal(pctOrdersNewToBrand, 6);
		this.salesNewToBrand = toBigDecimal(salesNewToBrand, 2);
		this.pctSalesNewToBrand = toBigDecimal(pctSalesNewToBrand, 6);
		this.unitsNewToBrand = new BigDecimal(unitsNewToBrand).intValue();
		this.pctUnitsNewToBrand = toBigDecimal(pctUnitsNewToBrand, 6);
		this.orderRateNewToBrand = toBigDecimal(orderRateNewToBrand, 6);
	}

	//DISPLAY
	public AmazonHeadlineSearchAdReportItemImpl(
			int marketplaceId,
			Date reportDate,
			String currency,
			String campaignName,
			String impressions,
			String clicks,
			String clickThruRate,
			String spend,
			String costPerClick,
			String totalACOS,
			String totalRoAS,
			String totalOrders14days,
			String totalUnits14days,
			String totalSales14days,
			String conversionRate14days) {
		this.marketplaceId = marketplaceId;
		this.reportDate = reportDate;
		this.currency = currency;
		this.campaignName = campaignName;
		this.impressions = new BigDecimal(impressions).intValue();
		this.clicks = new BigDecimal(clicks).intValue();
		this.clickThruRate = toBigDecimal(clickThruRate, 6);
		this.spend = new BigDecimal(spend);
		this.costPerClick = toBigDecimal(costPerClick, 2);
		this.totalACOS = toBigDecimal(totalACOS, 6);
		this.totalRoAS = toBigDecimal(totalRoAS, 2);
		this.totalOrders14days = new BigDecimal(totalOrders14days).intValue();
		this.totalUnits14days = new BigDecimal(totalUnits14days).intValue();
		this.totalSales14days = toBigDecimal(totalSales14days, 2);
		this.conversionRate14days = toBigDecimal(conversionRate14days, 6);
	}



	public AmazonHeadlineSearchAdReportItemImpl(
			Integer marketplaceId,
			String currency,
			String campaignName,
			String targeting,
			String matchType,
			BigInteger impressions,
			BigInteger clicks,
			BigDecimal spend,
			BigDecimal totalSales14days,
			BigInteger totalOrders14days,
			BigInteger totalUnits14days) {
		this.marketplaceId = marketplaceId;
		this.currency = currency;
		this.campaignName = campaignName;
		this.targeting = targeting;
		this.matchType = matchType;
		this.impressions = impressions.intValue();
		this.clicks = clicks.intValue();
		this.spend = spend;
		this.totalSales14days = totalSales14days;
		this.totalOrders14days = totalOrders14days.intValue();
		this.totalUnits14days = totalUnits14days.intValue();
	}

	private BigDecimal toBigDecimal(String numberStr, int scale){
		if(numberStr.isEmpty()) return null;
		if(scale == 2) return new BigDecimal(numberStr).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return new BigDecimal(numberStr).multiply(new BigDecimal(100))
				.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AmazonHeadlineSearchAdReportItemImpl that = (AmazonHeadlineSearchAdReportItemImpl) o;
		return Objects.equals(marketplaceId, that.marketplaceId) &&
				Objects.equals(reportDate, that.reportDate) &&
				Objects.equals(currency, that.currency) &&
				Objects.equals(campaignName, that.campaignName) &&
				Objects.equals(targeting, that.targeting) &&
				Objects.equals(matchType, that.matchType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(marketplaceId, reportDate, currency, campaignName, targeting, matchType);
	}

	@Override
	public String toString() {
		return "AmazonHeadlineSearchAdReportItemImpl{" +
				"marketplaceId=" + marketplaceId +
				", reportDate='" + reportDate + '\'' +
				", currency='" + currency + '\'' +
				", campaignName='" + campaignName + '\'' +
				", targeting='" + targeting + '\'' +
				", matchType='" + matchType + '\'' +
				", impressions=" + impressions +
				", clicks=" + clicks +
				", clickThruRate=" + clickThruRate +
				", costPerClick=" + costPerClick +
				", spend=" + spend +
				", totalACOS=" + totalACOS +
				", totalRoAS=" + totalRoAS +
				", totalSales14days=" + totalSales14days +
				", totalOrders14days=" + totalOrders14days +
				", totalUnits14days=" + totalUnits14days +
				", conversionRate14days=" + conversionRate14days +
				'}';
	}

	@Override
	public Integer getMarketplaceId() {
		return marketplaceId;
	}

	@Override
	public Date getReportDate() {
		return reportDate;
	}

	@Override
	public String getPortfolioName() {
		return portfolioName;
	}

	@Override
	public String getCurrency() {
		return currency;
	}

	@Override
	public String getCampaignName() {
		return campaignName;
	}

	@Override
	public String getTargeting() {
		return targeting;
	}

	@Override
	public String getMatchType() {
		return matchType;
	}

	@Override
	public Integer getImpressions() {
		return impressions;
	}

	@Override
	public Integer getClicks() {
		return clicks;
	}

	@Override
	public BigDecimal getClickThruRate() {
		return clickThruRate;
	}

	@Override
	public BigDecimal getCostPerClick() {
		return costPerClick;
	}

	@Override
	public BigDecimal getSpend() {
		return spend;
	}

	@Override
	public BigDecimal getTotalACOS() {
		return totalACOS;
	}

	@Override
	public BigDecimal getTotalRoAS() {
		return totalRoAS;
	}

	@Override
	public BigDecimal getTotalSales14days() {
		return totalSales14days;
	}

	@Override
	public Integer getTotalOrders14days() {
		return totalOrders14days;
	}

	@Override
	public Integer getTotalUnits14days() {
		return totalUnits14days;
	}

	@Override
	public BigDecimal getConversionRate14days() {
		return conversionRate14days;
	}

	@Override
	public Integer getOrdersNewToBrand() {
		return ordersNewToBrand;
	}

	@Override
	public BigDecimal getPctOrdersNewToBrand() {
		return pctOrdersNewToBrand;
	}

	@Override
	public BigDecimal getSalesNewToBrand() {
		return salesNewToBrand;
	}

	@Override
	public BigDecimal getPctSalesNewToBrand() {
		return pctSalesNewToBrand;
	}

	@Override
	public Integer getUnitsNewToBrand() {
		return unitsNewToBrand;
	}

	@Override
	public BigDecimal getPctUnitsNewToBrand() {
		return pctUnitsNewToBrand;
	}

	@Override
	public BigDecimal getOrderRateNewToBrand() {
		return orderRateNewToBrand;
	}

	@Override
	public Integer getViewableImpressions() {
		return viewableImpressions;
	}

	@Override
	public BigDecimal getViewThruRate() {
		return viewThruRate;
	}

	@Override
	public BigDecimal getClickThruRateForViews() {
		return clickThruRateForViews;
	}

	@Override
	public Integer getVideoFirstQuartileViews() {
		return videoFirstQuartileViews;
	}

	@Override
	public Integer getVideoMidpointViews() {
		return videoMidpointViews;
	}

	@Override
	public Integer getVideoThirdQuartileViews() {
		return videoThirdQuartileViews;
	}

	@Override
	public Integer getVideoCompleteViews() {
		return videoCompleteViews;
	}

	@Override
	public Integer getVideoUnmutes() {
		return videoUnmutes;
	}

	@Override
	public Integer getViews5second() {
		return views5second;
	}

	@Override
	public BigDecimal getViewRate5second() {
		return viewRate5second;
	}

	@Override
	public void setClickThruRate(BigDecimal clickThruRate) {
		this.clickThruRate = clickThruRate;
	}

	@Override
	public void setCostPerClick(BigDecimal costPerClick) {
		this.costPerClick = costPerClick;
	}

	@Override
	public void setTotalACOS(BigDecimal totalACOS) {
		this.totalACOS = totalACOS;
	}

	@Override
	public void setTotalRoAS(BigDecimal totalRoAS) {
		this.totalRoAS = totalRoAS;
	}

	@Override
	public void setConversionRate14days(BigDecimal conversionRate14days) {
		this.conversionRate14days = conversionRate14days;
	}
}
