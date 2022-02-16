package com.kindminds.drs.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReportLineItem;


public class SkuAdvertisingPerformanceReportLineItemImpl implements SkuAdvertisingPerformanceReportLineItem {
	
	private Currency currency;
	private String sku;
	private String totalImpression;
	private String totalClicks;
	private String clickThroughRate;
	private String totalSpend;
	private String costPerClick;
	private String totalOneWeekOrdersPlaced;
	private String totalOneWeekOrderedProductSales;
	private String acos;
	
	public SkuAdvertisingPerformanceReportLineItemImpl(
			Currency currency,
			String sku,
			String totalImpression,
			String totalClicks,
			String clickThroughRate,
			String totalSpend,
			String costPerClick,
			String totalOneWeekOrdersPlaced,
			String totalOneWeekOrderedProductSales,
			String acos){
		this.currency = currency;
		this.sku = sku;
		this.totalImpression = totalImpression;
		this.totalClicks = totalClicks;
		this.clickThroughRate = clickThroughRate;
		this.totalSpend = totalSpend;
		this.costPerClick = costPerClick;
		this.totalOneWeekOrdersPlaced = totalOneWeekOrdersPlaced;
		this.totalOneWeekOrderedProductSales = totalOneWeekOrderedProductSales;
		this.acos = acos;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof SkuAdvertisingPerformanceReportLineItem){
			SkuAdvertisingPerformanceReportLineItem item = (SkuAdvertisingPerformanceReportLineItem)obj;
			return this.getCurrency().equals(item.getCurrency())
				&& this.getSku().equals(item.getSku())
				&& this.getTotalImpression().equals(item.getTotalImpression())
				&& this.getTotalClicks().equals(item.getTotalClicks())
				&& this.getClickThroughRate().equals(item.getClickThroughRate())
				&& this.getTotalSpend().equals(item.getTotalSpend())
				&& this.getCostPerClick().equals(item.getCostPerClick())
				&& this.getTotalOneWeekOrdersPlaced().equals(item.getTotalOneWeekOrdersPlaced())
				&& this.getTotalOneWeekOrderedProductSales().equals(item.getTotalOneWeekOrderedProductSales())
				&& this.getAcos().equals(item.getAcos());
		}
		return false;
	}

	@Override
	public String toString() {
		return "SkuAdvertisingPerformanceReportLineItemImpl [getCurrency()=" + getCurrency() + ", getSku()=" + getSku()
				+ ", getTotalImpression()=" + getTotalImpression() + ", getTotalClicks()=" + getTotalClicks()
				+ ", getClickThroughRate()=" + getClickThroughRate() + ", getTotalSpend()=" + getTotalSpend()
				+ ", getCostPerClick()=" + getCostPerClick() + ", getTotalOneWeekOrdersPlaced()="
				+ getTotalOneWeekOrdersPlaced() + ", getTotalOneWeekOrderedProductSales()="
				+ getTotalOneWeekOrderedProductSales() + ", getAcos()=" + getAcos() + "]";
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getTotalImpression() {
		return this.totalImpression;
	}

	@Override
	public String getTotalClicks() {
		return this.totalClicks;
	}

	@Override
	public String getClickThroughRate() {
		return this.clickThroughRate;
	}

	@Override
	public String getTotalSpend() {
		return this.totalSpend;
	}

	@Override
	public String getCostPerClick() {
		return this.costPerClick;
	}

	@Override
	public String getTotalOneWeekOrdersPlaced() {
		return this.totalOneWeekOrdersPlaced;
	}

	@Override
	public String getTotalOneWeekOrderedProductSales() {
		return this.totalOneWeekOrderedProductSales;
	}

	@Override
	public String getAcos() {
		return this.acos;
	}
	
}
