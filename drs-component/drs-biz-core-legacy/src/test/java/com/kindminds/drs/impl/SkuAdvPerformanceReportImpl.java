package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReportLineItem;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReport;

import java.util.List;
import java.util.Map;


public class SkuAdvPerformanceReportImpl implements SkuAdvertisingPerformanceReport {

	private String marketplace;
	private String currecny;
	private String grandTotalImpression;
	private String grandTotalClick;
	private String grandClickThroughRate;
	private String grandTotalSpend;
	private String grandCostPerClick;
	private String grandTotalOneWeekOrdersPlaced;
	private String grandTotalOneWeekOrderedProductSales;
	private String grandAcos;
	private Map<String,List<SkuAdvertisingPerformanceReportLineItem>> campaignItems;
	
	public SkuAdvPerformanceReportImpl(
			String marketplace,
			String currecny,
			String grandTotalImpression,
			String grandTotalClick,
			String grandClickThroughRate,
			String grandTotalSpend,
			String grandCostPerClick,
			String grandTotalOneWeekOrdersPlaced,
			String grandTotalOneWeekOrderedProductSales,
			String grandAcos,
			Map<String,List<SkuAdvertisingPerformanceReportLineItem>> campaignItems){
		this.marketplace = marketplace;
		this.currecny = currecny;
		this.grandTotalImpression = grandTotalImpression;
		this.grandTotalClick = grandTotalClick;
		this.grandClickThroughRate = grandClickThroughRate;
		this.grandTotalSpend = grandTotalSpend;
		this.grandCostPerClick = grandCostPerClick;
		this.grandTotalOneWeekOrdersPlaced = grandTotalOneWeekOrdersPlaced;
		this.grandTotalOneWeekOrderedProductSales = grandTotalOneWeekOrderedProductSales;
		this.grandAcos = grandAcos;
		this.campaignItems = campaignItems;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof SkuAdvertisingPerformanceReport){
			SkuAdvertisingPerformanceReport rpt = (SkuAdvertisingPerformanceReport)obj;
			return this.getMarketplace().equals(rpt.getMarketplace())
				&& this.getCurrency().equals(rpt.getCurrency())
				&& this.getGrandTotalImpression().equals(rpt.getGrandTotalImpression())
				&& this.getGrandTotalClick().equals(rpt.getGrandTotalClick())
				&& this.getGrandClickThroughRate().equals(rpt.getGrandClickThroughRate())
				&& this.getGrandTotalSpend().equals(rpt.getGrandTotalSpend())
				&& this.getGrandCostPerClick().equals(rpt.getGrandCostPerClick())
				&& this.getGrandTotalOneWeekOrdersPlaced().equals(rpt.getGrandTotalOneWeekOrdersPlaced())
				&& this.getGrandTotalOneWeekOrderedProductSales().equals(rpt.getGrandTotalOneWeekOrderedProductSales())
				&& this.getGrandAcos().equals(rpt.getGrandAcos())
				&& this.getCampaignItems().equals(rpt.getCampaignItems());
		}
		return false;
	}

	@Override
	public String getMarketplace() {
		return this.marketplace;
	}

	@Override
	public String getCurrency() {
		return this.currecny;
	}

	@Override
	public String getGrandTotalImpression() {
		return this.grandTotalImpression;
	}

	@Override
	public String getGrandTotalClick() {
		return this.grandTotalClick;
	}
	
	@Override
	public String getGrandClickThroughRate() {
		return this.grandClickThroughRate;
	}
	
	@Override
	public String getGrandTotalSpend() {
		return this.grandTotalSpend;
	}
	
	@Override
	public String getGrandCostPerClick() {
		return this.grandCostPerClick;
	}

	@Override
	public String getGrandTotalOneWeekOrdersPlaced() {
		return this.grandTotalOneWeekOrdersPlaced;
	}

	@Override
	public String getGrandTotalOneWeekOrderedProductSales() {
		return this.grandTotalOneWeekOrderedProductSales;
	}
	
	@Override
	public String getGrandAcos() {
		return this.grandAcos;
	}

	@Override
	public Map<String,List<SkuAdvertisingPerformanceReportLineItem>> getCampaignItems() {
		return this.campaignItems;
	}

}
