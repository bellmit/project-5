package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.accounting.AmazonCampaignPerformanceReportRawLine;
import com.kindminds.drs.util.DateHelper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonCampaignPerformanceReportRawLineImpl implements AmazonCampaignPerformanceReportRawLine {

	private String campaignName;
	private String adGroupName;
	private String advertisedSku;
	private String keyword;
	private String matchType;
	private Date startDate;
	private Date endDate;
	private Integer clicks;
	private Integer impressions;
	private BigDecimal ctr;
	private BigDecimal totalSpend;
	private BigDecimal averageCpc;
	private String currency;
	
	private Integer oneDayOrdersPlaced;
	private BigDecimal oneDayOrderedProductSales;
	private BigDecimal oneDayConversionRate;
	private Integer oneDaySameSkuUnitsOrdered;
	private Integer oneDayOtherSkuUnitsOrdered;
	private BigDecimal oneDaySameSkuUnitsOrderedProductSales;
	private BigDecimal oneDayOtherSkuUnitsOrderedProductSales;
	
	private Integer oneWeekOrdersPlaced;
	private BigDecimal oneWeekOrderedProductSales;
	private BigDecimal oneWeekConversionRate;
	private Integer oneWeekSameSkuUnitsOrdered;
	private Integer oneWeekOtherSkuUnitsOrdered;
	private BigDecimal oneWeekSameSkuUnitsOrderedProductSales;
	private BigDecimal oneWeekOtherSkuUnitsOrderedProductSales;
	
	private Integer oneMonthOrdersPlaced;
	private BigDecimal oneMonthOrderedProductSales;
	private BigDecimal oneMonthConversionRate;
	private Integer oneMonthSameSkuUnitsOrdered;
	private Integer oneMonthOtherSkuUnitsOrdered;
	private BigDecimal oneMonthSameSkuUnitsOrderedProductSales;
	private BigDecimal oneMonthOtherSkuUnitsOrderedProductSales;

	public AmazonCampaignPerformanceReportRawLineImpl(
			String dateFormat,
			String timezoneText,
			String campaignName,
			String adGroupName,
			String advertisedSku,
			String keyword,
			String matchType,
			String startDate,
			String endDate,
			String clicks,
			String impressions,
			String ctr,
			BigDecimal totalSpend,
			BigDecimal averageCpc,
			String currency,
			String oneDayOrdersPlaced,
			BigDecimal oneDayOrderedProductSales,
			String oneDayConversionRate,
			String oneDaySameSkuUnitsOrdered,
			String oneDayOtherSkuUnitsOrdered,
			BigDecimal oneDaySameSkuUnitsOrderedProductSales,
			BigDecimal oneDayOtherSkuUnitsOrderedProductSales,
			String oneWeekOrdersPlaced,
			BigDecimal oneWeekOrderedProductSales,
			String oneWeekConversionRate,
			String oneWeekSameSkuUnitsOrdered,
			String oneWeekOtherSkuUnitsOrdered,
			BigDecimal oneWeekSameSkuUnitsOrderedProductSales,
			BigDecimal oneWeekOtherSkuUnitsOrderedProductSales,
			String oneMonthOrdersPlaced,
			BigDecimal oneMonthOrderedProductSales,
			String oneMonthConversionRate,
			String oneMonthSameSkuUnitsOrdered,
			String oneMonthOtherSkuUnitsOrdered,
			BigDecimal oneMonthSameSkuUnitsOrderedProductSales,
			BigDecimal oneMonthOtherSkuUnitsOrderedProductSales) {
		
		this.campaignName = campaignName;
		this.adGroupName = adGroupName;
		this.advertisedSku = advertisedSku;
		this.keyword = keyword;
		this.matchType = matchType;
		this.startDate = DateHelper.toDate(String.join(" ",startDate,timezoneText),dateFormat);
		this.endDate = DateHelper.toDate(String.join(" ",endDate,timezoneText),dateFormat);
		this.clicks = Integer.valueOf(clicks);
		this.impressions = Integer.valueOf(impressions);
		this.ctr = this.toBigDecimal(ctr);
		this.totalSpend = totalSpend;
		this.averageCpc = averageCpc;
		this.currency = currency;
		
		this.oneDayOrdersPlaced = Integer.valueOf(oneDayOrdersPlaced);
		this.oneDayOrderedProductSales = oneDayOrderedProductSales;
		this.oneDayConversionRate = this.toBigDecimal(oneDayConversionRate);
		this.oneDaySameSkuUnitsOrdered = Integer.valueOf(oneDaySameSkuUnitsOrdered);
		this.oneDayOtherSkuUnitsOrdered = Integer.valueOf(oneDayOtherSkuUnitsOrdered);
		this.oneDaySameSkuUnitsOrderedProductSales = oneDaySameSkuUnitsOrderedProductSales;
		this.oneDayOtherSkuUnitsOrderedProductSales = oneDayOtherSkuUnitsOrderedProductSales;
		
		this.oneWeekOrdersPlaced = Integer.valueOf(oneWeekOrdersPlaced);
		this.oneWeekOrderedProductSales = oneWeekOrderedProductSales;
		this.oneWeekConversionRate = this.toBigDecimal(oneWeekConversionRate);
		this.oneWeekSameSkuUnitsOrdered = Integer.valueOf(oneWeekSameSkuUnitsOrdered);
		this.oneWeekOtherSkuUnitsOrdered = Integer.valueOf(oneWeekOtherSkuUnitsOrdered);
		this.oneWeekSameSkuUnitsOrderedProductSales = oneWeekSameSkuUnitsOrderedProductSales;
		this.oneWeekOtherSkuUnitsOrderedProductSales = oneWeekOtherSkuUnitsOrderedProductSales;
		
		this.oneMonthOrdersPlaced = Integer.valueOf(oneMonthOrdersPlaced);
		this.oneMonthOrderedProductSales = oneMonthOrderedProductSales;
		this.oneMonthConversionRate = this.toBigDecimal(oneMonthConversionRate);
		this.oneMonthSameSkuUnitsOrdered = Integer.valueOf(oneMonthSameSkuUnitsOrdered);
		this.oneMonthOtherSkuUnitsOrdered = Integer.valueOf(oneMonthOtherSkuUnitsOrdered);
		this.oneMonthSameSkuUnitsOrderedProductSales = oneMonthSameSkuUnitsOrderedProductSales;
		this.oneMonthOtherSkuUnitsOrderedProductSales = oneMonthOtherSkuUnitsOrderedProductSales;
		this.assertAllFieldWithText();
	}
	
	private BigDecimal toBigDecimal(String percentageStr){
		char lastChar = percentageStr.charAt(percentageStr.length()-1);
		Assert.isTrue(lastChar=='%');
		BigDecimal percentage = new BigDecimal(percentageStr.substring(0,percentageStr.length()-1)); 
		return percentage.divide(new BigDecimal("100"));
	}
	
	private void assertAllFieldWithText(){
		Assert.isTrue(StringUtils.hasText(this.campaignName));
		Assert.isTrue(StringUtils.hasText(this.adGroupName));
		Assert.isTrue(StringUtils.hasText(this.advertisedSku));
		Assert.isTrue(StringUtils.hasText(this.keyword));
		// Old report has no column "Match Type, so not necessary to check"
		Assert.notNull(this.startDate);
		Assert.notNull(this.endDate);
		Assert.notNull(this.clicks);
		Assert.notNull(this.impressions);
		Assert.notNull(this.ctr);
		Assert.notNull(this.totalSpend);
		// Assert.notNull(this.averageCpc);
		Assert.notNull(this.currency);
		Assert.notNull(this.oneDayOrdersPlaced);
		Assert.notNull(this.oneDayOrderedProductSales);
		Assert.notNull(this.oneDayConversionRate);
		Assert.notNull(this.oneDaySameSkuUnitsOrdered);
		Assert.notNull(this.oneDayOtherSkuUnitsOrdered);
		Assert.notNull(this.oneDaySameSkuUnitsOrderedProductSales);
		Assert.notNull(this.oneDayOtherSkuUnitsOrderedProductSales);
		Assert.notNull(this.oneWeekOrdersPlaced);
		Assert.notNull(this.oneWeekOrderedProductSales);
		Assert.notNull(this.oneWeekConversionRate);
		Assert.notNull(this.oneWeekSameSkuUnitsOrdered);
		Assert.notNull(this.oneWeekOtherSkuUnitsOrdered);
		Assert.notNull(this.oneWeekSameSkuUnitsOrderedProductSales);
		Assert.notNull(this.oneWeekOtherSkuUnitsOrderedProductSales);
		Assert.notNull(this.oneMonthOrdersPlaced);
		Assert.notNull(this.oneMonthOrderedProductSales);
		Assert.notNull(this.oneMonthConversionRate);
		Assert.notNull(this.oneMonthSameSkuUnitsOrdered);
		Assert.notNull(this.oneMonthOtherSkuUnitsOrdered);
		Assert.notNull(this.oneMonthSameSkuUnitsOrderedProductSales);
		Assert.notNull(this.oneMonthOtherSkuUnitsOrderedProductSales);
		Assert.isTrue(this.endDate.getTime()-this.startDate.getTime()==86400000);
	}
	
	@Override
	public String toString() {
		return "AmazonCampaignPerformanceReportRawLineImpl [getCampaignName()=" + getCampaignName()
				+ ", getAdGroupName()=" + getAdGroupName() + ", getAdvertisedSku()=" + getAdvertisedSku()
				+ ", getKeyword()=" + getKeyword() + ", getMatchType()=" + getMatchType() + ", getStartDate()="
				+ getStartDate() + ", getEndDate()=" + getEndDate() + ", getClicks()=" + getClicks()
				+ ", getImpressions()=" + getImpressions() + ", getCtr()=" + getCtr() + ", getTotalSpend()="
				+ getTotalSpend() + ", getAverageCpc()=" + getAverageCpc() + ", getCurrency()=" + getCurrency()
				+ ", getOneDayOrdersPlaced()=" + getOneDayOrdersPlaced() + ", getOneDayOrderedProductSales()="
				+ getOneDayOrderedProductSales() + ", getOneDayConversionRate()=" + getOneDayConversionRate()
				+ ", getOneDaySameSkuUnitsOrdered()=" + getOneDaySameSkuUnitsOrdered()
				+ ", getOneDayOtherSkuUnitsOrdered()=" + getOneDayOtherSkuUnitsOrdered()
				+ ", getOneDaySameSkuUnitsOrderedProductSales()=" + getOneDaySameSkuUnitsOrderedProductSales()
				+ ", getOneDayOtherSkuUnitsOrderedProductSales()=" + getOneDayOtherSkuUnitsOrderedProductSales()
				+ ", getOneWeekOrdersPlaced()=" + getOneWeekOrdersPlaced() + ", getOneWeekOrderedProductSales()="
				+ getOneWeekOrderedProductSales() + ", getOneWeekConversionRate()=" + getOneWeekConversionRate()
				+ ", getOneWeekSameSkuUnitsOrdered()=" + getOneWeekSameSkuUnitsOrdered()
				+ ", getOneWeekOtherSkuUnitsOrdered()=" + getOneWeekOtherSkuUnitsOrdered()
				+ ", getOneWeekSameSkuUnitsOrderedProductSales()=" + getOneWeekSameSkuUnitsOrderedProductSales()
				+ ", getOneWeekOtherSkuUnitsOrderedProductSales()=" + getOneWeekOtherSkuUnitsOrderedProductSales()
				+ ", getOneMonthOrdersPlaced()=" + getOneMonthOrdersPlaced() + ", getOneMonthOrderedProductSales()="
				+ getOneMonthOrderedProductSales() + ", getOneMonthConversionRate()=" + getOneMonthConversionRate()
				+ ", getOneMonthSameSkuUnitsOrdered()=" + getOneMonthSameSkuUnitsOrdered()
				+ ", getOneMonthOtherSkuUnitsOrdered()=" + getOneMonthOtherSkuUnitsOrdered()
				+ ", getOneMonthSameSkuUnitsOrderedProductSales()=" + getOneMonthSameSkuUnitsOrderedProductSales()
				+ ", getOneMonthOtherSkuUnitsOrderedProductSales()=" + getOneMonthOtherSkuUnitsOrderedProductSales()
				+ "]";
	}

	@Override public String getCampaignName() {return this.campaignName;}
	@Override public String getAdGroupName() {return this.adGroupName;}
	@Override public String getAdvertisedSku() {return this.advertisedSku;}
	@Override public String getKeyword() {return this.keyword;}
	@Override public String getMatchType() {return this.matchType;}
	@Override public Date getStartDate() {return this.startDate;}
	@Override public Date getEndDate() {return this.endDate;}
	@Override public Integer getClicks() {return this.clicks;}
	@Override public Integer getImpressions() {return this.impressions;}
	@Override public BigDecimal getCtr() {return this.ctr;}
	@Override public BigDecimal getTotalSpend() {return this.totalSpend;}
	@Override public BigDecimal getAverageCpc() {return this.averageCpc;}
	@Override public String getCurrency() {return this.currency;}
	
	@Override public Integer getOneDayOrdersPlaced() {return this.oneDayOrdersPlaced;}
	@Override public BigDecimal getOneDayOrderedProductSales() {return this.oneDayOrderedProductSales;}
	@Override public BigDecimal getOneDayConversionRate() {return this.oneDayConversionRate;}
	@Override public Integer getOneDaySameSkuUnitsOrdered() {return this.oneDaySameSkuUnitsOrdered;}
	@Override public Integer getOneDayOtherSkuUnitsOrdered() {return this.oneDayOtherSkuUnitsOrdered;}
	@Override public BigDecimal getOneDaySameSkuUnitsOrderedProductSales() {return this.oneDaySameSkuUnitsOrderedProductSales;}
	@Override public BigDecimal getOneDayOtherSkuUnitsOrderedProductSales() {return this.oneDayOtherSkuUnitsOrderedProductSales;}
	
	@Override public Integer getOneWeekOrdersPlaced() {return this.oneWeekOrdersPlaced;}
	@Override public BigDecimal getOneWeekOrderedProductSales() {return this.oneWeekOrderedProductSales;}
	@Override public BigDecimal getOneWeekConversionRate() {return this.oneWeekConversionRate;}
	@Override public Integer getOneWeekSameSkuUnitsOrdered() {return this.oneWeekSameSkuUnitsOrdered;}
	@Override public Integer getOneWeekOtherSkuUnitsOrdered() {return this.oneWeekOtherSkuUnitsOrdered;}
	@Override public BigDecimal getOneWeekSameSkuUnitsOrderedProductSales() {return this.oneWeekSameSkuUnitsOrderedProductSales;}
	@Override public BigDecimal getOneWeekOtherSkuUnitsOrderedProductSales() {return this.oneWeekOtherSkuUnitsOrderedProductSales;}
	
	@Override public Integer getOneMonthOrdersPlaced() {return this.oneMonthOrdersPlaced;}
	@Override public BigDecimal getOneMonthOrderedProductSales() {return this.oneMonthOrderedProductSales;}
	@Override public BigDecimal getOneMonthConversionRate() {return this.oneMonthConversionRate;}
	@Override public Integer getOneMonthSameSkuUnitsOrdered() {return this.oneMonthSameSkuUnitsOrdered;}
	@Override public Integer getOneMonthOtherSkuUnitsOrdered() {return this.oneMonthOtherSkuUnitsOrdered;}
	@Override public BigDecimal getOneMonthSameSkuUnitsOrderedProductSales() {return this.oneMonthSameSkuUnitsOrderedProductSales;}
	@Override public BigDecimal getOneMonthOtherSkuUnitsOrderedProductSales() {return this.oneMonthOtherSkuUnitsOrderedProductSales;}

}
