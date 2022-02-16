package com.kindminds.drs.persist.v1.model.mapping.report;

import java.math.BigDecimal;





import com.kindminds.drs.Currency;

import com.kindminds.drs.api.v1.model.report.SearchTermReport;
import com.kindminds.drs.util.BigDecimalHelper;


public class SearchTermReportLineItemImpl implements SearchTermReport.SearchTermReportLineItem {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="campaign_name")
	private String campaignName;
	//@Column(name="ad_group_name")
	private String adGroupName;
	//@Column(name="customer_search_term")
	private String customerSearchTerm;
	//@Column(name="keyword")
	private String keyword;
	//@Column(name="match_type")
	private String matchType;
	//@Column(name="first_day_of_impression")
	private String firstDayOfImpression;
	//@Column(name="last_day_of_impression")
	private String lastDayOfImpression;
	//@Column(name="impressions")
	private Integer impressions;
	//@Column(name="clicks")
	private Integer clicks;
	//@Column(name="ctr")
	private BigDecimal ctr;
	//@Column(name="total_spend")
	private BigDecimal totalSpend;
	//@Column(name="average_cpc")
	private BigDecimal averageCpc;
	//@Column(name="acos")
	private BigDecimal acos;
	//@Column(name="currency")
	private String currency;
	//@Column(name="orders_placed_within_one_week_of_a_click")
	private Integer ordersPlacedWithinOneWeekOfAClick;
	//@Column(name="product_sales_within_one_week_of_a_click")
	private BigDecimal productSalesWithinOneWeekOfAClick;
	//@Column(name="conversion_rate_within_one_week_of_a_click")
	private BigDecimal conversionRateWithinOneWeekOfAClick;
	//@Column(name="same_sku_units_ordered_within_one_week_of_click")
	private Integer sameSkuUnitsOrderedWithinOneWeekOfClick;
	//@Column(name="other_sku_units_ordered_within_one_week_of_click")
	private Integer otherSkuUnitsOrderedWithinOneWeekOfClick;
	//@Column(name="same_sku_units_product_sales_within_one_week_of_click")
	private BigDecimal sameSkuUnitsProductSalesWithinOneWeekOfClick;
	//@Column(name="other_sku_units_product_sales_within_one_week_of_click")
	private BigDecimal otherSkuUnitsProductSalesWithinOneWeekOfClick;

	public SearchTermReportLineItemImpl() {
	}

	public SearchTermReportLineItemImpl(int id, String campaignName, String adGroupName, String customerSearchTerm, String keyword, String matchType, String firstDayOfImpression, String lastDayOfImpression, Integer impressions, Integer clicks, BigDecimal ctr, BigDecimal totalSpend, BigDecimal averageCpc, BigDecimal acos, String currency, Integer ordersPlacedWithinOneWeekOfAClick, BigDecimal productSalesWithinOneWeekOfAClick, BigDecimal conversionRateWithinOneWeekOfAClick, Integer sameSkuUnitsOrderedWithinOneWeekOfClick, Integer otherSkuUnitsOrderedWithinOneWeekOfClick, BigDecimal sameSkuUnitsProductSalesWithinOneWeekOfClick, BigDecimal otherSkuUnitsProductSalesWithinOneWeekOfClick) {
		this.id = id;
		this.campaignName = campaignName;
		this.adGroupName = adGroupName;
		this.customerSearchTerm = customerSearchTerm;
		this.keyword = keyword;
		this.matchType = matchType;
		this.firstDayOfImpression = firstDayOfImpression;
		this.lastDayOfImpression = lastDayOfImpression;
		this.impressions = impressions;
		this.clicks = clicks;
		this.ctr = ctr;
		this.totalSpend = totalSpend;
		this.averageCpc = averageCpc;
		this.acos = acos;
		this.currency = currency;
		this.ordersPlacedWithinOneWeekOfAClick = ordersPlacedWithinOneWeekOfAClick;
		this.productSalesWithinOneWeekOfAClick = productSalesWithinOneWeekOfAClick;
		this.conversionRateWithinOneWeekOfAClick = conversionRateWithinOneWeekOfAClick;
		this.sameSkuUnitsOrderedWithinOneWeekOfClick = sameSkuUnitsOrderedWithinOneWeekOfClick;
		this.otherSkuUnitsOrderedWithinOneWeekOfClick = otherSkuUnitsOrderedWithinOneWeekOfClick;
		this.sameSkuUnitsProductSalesWithinOneWeekOfClick = sameSkuUnitsProductSalesWithinOneWeekOfClick;
		this.otherSkuUnitsProductSalesWithinOneWeekOfClick = otherSkuUnitsProductSalesWithinOneWeekOfClick;
	}

	@Override
	public String toString() {
		return "SearchTermReportLineItemImpl [getCampaignName()=" + getCampaignName() + ", getAdGroupName()="
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
	public String getFirstDayOfImpression() {
		return this.firstDayOfImpression;
	}

	@Override
	public String getLastDayOfImpression() {
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
	public String getCtr() {
		if(this.ctr == null) return "N/A";
		Currency currency = this.getCurrency();
		return BigDecimalHelper.toPercentageString(this.ctr,currency.getScale());
	}

	@Override
	public String getTotalSpend() {
		Currency currency = this.getCurrency();
		return this.totalSpend.setScale(currency.getScale()).toPlainString();
	}

	@Override
	public String getAverageCpc() {
		if(this.averageCpc == null) return "N/A";
		Currency currency = this.getCurrency();
		return this.averageCpc.setScale(currency.getScale()).toPlainString();
	}

	@Override
	public String getAcos() {
		if(this.acos == null) return "N/A";
		Currency currency = this.getCurrency();
		return BigDecimalHelper.toPercentageString(this.acos,currency.getScale());
	}

	@Override
	public Currency getCurrency() {
		return Currency.valueOf(this.currency);
	}

	@Override
	public Integer getOrdersPlacedWithinOneWeekOfAClick() {
		return this.ordersPlacedWithinOneWeekOfAClick;
	}

	@Override
	public String getProductSalesWithinOneWeekOfAClick() {
		Currency currency = this.getCurrency();
		return this.productSalesWithinOneWeekOfAClick.setScale(currency.getScale()).toPlainString();
	}

	@Override
	public String getConversionRateWithinOneWeekOfAClick() {
		if(this.conversionRateWithinOneWeekOfAClick == null) return "N/A";
		Currency currency = this.getCurrency();
		return BigDecimalHelper.toPercentageString(this.conversionRateWithinOneWeekOfAClick,currency.getScale());
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
	public String getSameSkuUnitsProductSalesWithinOneWeekOfClick() {
		Currency currency = this.getCurrency();
		return this.sameSkuUnitsProductSalesWithinOneWeekOfClick.setScale(currency.getScale()).toPlainString();
	}

	@Override
	public String getOtherSkuUnitsProductSalesWithinOneWeekOfClick() {
		Currency currency = this.getCurrency();
		return this.otherSkuUnitsProductSalesWithinOneWeekOfClick.setScale(currency.getScale()).toPlainString();
	}

}
