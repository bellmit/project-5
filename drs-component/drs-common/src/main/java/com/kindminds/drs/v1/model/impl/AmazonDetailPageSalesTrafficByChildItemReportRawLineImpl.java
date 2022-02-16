package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.amazon.AmazonDetailPageSalesTrafficByChildItemReportRawLine;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class AmazonDetailPageSalesTrafficByChildItemReportRawLineImpl implements AmazonDetailPageSalesTrafficByChildItemReportRawLine {

	private String parentAsin;
	private String childAsin;
	private String title;
	private Integer sessions;
	private BigDecimal sessionPercentage;
	private Integer pageViews;
	private BigDecimal pageViewsPercentage;
	private BigDecimal buyBoxPercentage;
	private Integer unitsOrdered;
	private BigDecimal unitSessionPercentage;
	private BigDecimal orderedProductSales;
	private Integer totalOrderItems;

	public AmazonDetailPageSalesTrafficByChildItemReportRawLineImpl(
			String parentAsin,
			String childAsin,
			String title,
			Integer sessions,
			String sessionPercentage,
			Integer pageViews,
			String pageViewsPercentage,
			String buyBoxPercentage,
			Integer unitsOrdered,
			String unitSessionPercentage,
			BigDecimal orderedProductSales,
			Integer totalOrderItems){
		this.parentAsin = parentAsin;
		this.childAsin = childAsin;
		this.title = StringUtils.hasText(title)?title:null;
		this.sessions = sessions;
		this.sessionPercentage = this.fromPercentageStr(sessionPercentage);
		this.pageViews = pageViews;
		this.pageViewsPercentage = this.fromPercentageStr(pageViewsPercentage);
		this.buyBoxPercentage = this.fromPercentageStr(buyBoxPercentage);
		this.unitsOrdered = unitsOrdered;
		this.unitSessionPercentage = this.fromPercentageStr(unitSessionPercentage);
		this.orderedProductSales = orderedProductSales;
		this.totalOrderItems = totalOrderItems;
	}

	private BigDecimal fromPercentageStr(String percentageStr){
		char lastChar = percentageStr.charAt(percentageStr.length()-1);
		Assert.isTrue(lastChar=='%');
		percentageStr = percentageStr.replace(",", "");
		BigDecimal percentage = new BigDecimal(percentageStr.substring(0,percentageStr.length()-1)); 
		return percentage.divide(new BigDecimal("100"));
	}

	@Override
	public String getParentAsin() {
		return this.parentAsin;
	}

	@Override
	public String getChildAsin() {
		return this.childAsin;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public Integer getSessions() {
		return this.sessions;
	}

	@Override
	public BigDecimal getSessionPercentage() {
		return this.sessionPercentage;
	}

	@Override
	public Integer getPageViews() {
		return this.pageViews;
	}

	@Override
	public BigDecimal getPageViewsPercentage() {
		return this.pageViewsPercentage;
	}

	@Override
	public BigDecimal getBuyBoxPercentage() {
		return this.buyBoxPercentage;
	}

	@Override
	public Integer getUnitsOrdered() {
		return this.unitsOrdered;
	}

	@Override
	public BigDecimal getUnitSessionPercentage() {
		return this.unitSessionPercentage;
	}

	@Override
	public BigDecimal getOrderedProductSales() {
		return this.orderedProductSales;
	}

	@Override
	public Integer getTotalOrderItems() {
		return this.totalOrderItems;
	}

}
