package com.kindminds.drs.persist.v1.model.mapping.amazon;


import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportBriefLineItem;

public class AmazonSponsoredProductsAdvertisedProductReportBriefLineItemImpl implements AmazonSponsoredProductsAdvertisedProductReportBriefLineItem {

	//@Id ////@Column(name="id")
	private Integer id;
	//@Column(name="campaign_name")
	private String campaignName;
	//@Column(name="ad_group_name")
	private String adGroupName;
	//@Column(name="advertised_sku")
	private String advertisedSku;
	//@Column(name="keyword")
	private String keyword;
	//@Column(name="match_type")
	private String matchType;
	//@Column(name="start_date")
	private String startDate;
	//@Column(name="end_date")
	private String endDate;
	//@Column(name="clicks")
	private Integer clicks;

	public AmazonSponsoredProductsAdvertisedProductReportBriefLineItemImpl() {
	}

	public AmazonSponsoredProductsAdvertisedProductReportBriefLineItemImpl(Integer id, String campaignName, String adGroupName, String advertisedSku, String keyword, String matchType, String startDate, String endDate, Integer clicks) {
		this.id = id;
		this.campaignName = campaignName;
		this.adGroupName = adGroupName;
		this.advertisedSku = advertisedSku;
		this.keyword = keyword;
		this.matchType = matchType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.clicks = clicks;
	}

	@Override
	public String toString() {
		return "AmazonSponsoredProductsAdvertisedProductReportBriefLineItemImpl [getCampaignName()=" + getCampaignName()
				+ ", getAdGroupName()=" + getAdGroupName() + ", getAdvertisedSku()=" + getAdvertisedSku()
				+ ", getKeyword()=" + getKeyword() + ", getMatchType()=" + getMatchType() + ", getStartDate()="
				+ getStartDate() + ", getEndDate()=" + getEndDate() + ", getClicks()=" + getClicks() + "]";
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
	public String getKeyword() {
		return this.keyword;
	}

	@Override
	public String getMatchType() {
		return this.matchType;
	}

	@Override
	public String getStartDate() {
		return this.startDate;
	}

	@Override
	public String getEndDate() {
		return this.endDate;
	}

	@Override
	public String getClicks() {
		return this.clicks.toString();
	}

}