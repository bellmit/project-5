package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.v1.model.impl.report.SkuAdvertisingPerformanceReportLineItemImpl;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReport;
import com.kindminds.drs.util.BigDecimalHelper;
import com.kindminds.drs.util.NumberHelper;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReportLineItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class SkuAdvertisingPerformanceReportImpl implements SkuAdvertisingPerformanceReport {
	
	private Marketplace marketplace = null;
	private Integer grandTotalImpressions=0;
	private Integer grandTotalClicks=0;
	private BigDecimal grandTotalSpend=BigDecimal.ZERO;
	private Integer grandTotalOneWeekOrdersPlaced=0;
	private BigDecimal grandTotalOneWeekOrderedProductSales=BigDecimal.ZERO;
	private Currency currecny=null;
	private Map<String,List<SkuAdvertisingPerformanceReportLineItem>> campaignItems;
	
	public void setCurrency(Currency currency){this.currecny=currency;}
	public void setMarketplace(Marketplace marketplace){this.marketplace=marketplace;}

	public SkuAdvertisingPerformanceReportImpl(Map<String,List<SkuAdvertisingPerformanceReportLineItem>> campaignItems){
		for(List<SkuAdvertisingPerformanceReportLineItem> campaignLineItems:campaignItems.values()){
			for(SkuAdvertisingPerformanceReportLineItem item:campaignLineItems){
				SkuAdvertisingPerformanceReportLineItemImpl origItem = (SkuAdvertisingPerformanceReportLineItemImpl)item;
				this.grandTotalImpressions += origItem.getNumericTotalImpression();
				this.grandTotalClicks += origItem.getNumericTotalClicks();
				this.grandTotalSpend = this.grandTotalSpend.add(origItem.getNumericTotalSpend());
				this.grandTotalOneWeekOrdersPlaced += origItem.getNumericTotalOneWeekOrdersPlaced();
				this.grandTotalOneWeekOrderedProductSales = this.grandTotalOneWeekOrderedProductSales.add(origItem.getNumericTotalOneWeekOrderedProductSales()); 
			}
		}
		this.campaignItems = campaignItems;
	}

	@Override
	public String getMarketplace() {
		return this.marketplace.getName();
	}
	
	@Override
	public String getCurrency() {
		return this.currecny.name();
	}
	
	@Override
	public String getGrandTotalImpression() {
		return NumberHelper.toGeneralCommaSeparatedString(this.grandTotalImpressions,0);
	}

	@Override
	public String getGrandTotalClick() {
		return this.grandTotalClicks.toString();
	}

	@Override
	public String getGrandClickThroughRate() {
		if(this.grandTotalImpressions.compareTo(0)==0) return "N/A";
		BigDecimal grandTotalClicks = new BigDecimal(this.grandTotalClicks);
		BigDecimal grandTotalImpressions = new BigDecimal(this.grandTotalImpressions);
		BigDecimal grandClickTroughRate = grandTotalClicks.divide(grandTotalImpressions,6,RoundingMode.HALF_UP);
		return BigDecimalHelper.toPercentageString(grandClickTroughRate,2);
	}
	
	@Override
	public String getGrandTotalSpend() {
		return this.grandTotalSpend.setScale(this.currecny.getScale()).toPlainString();
	}

	@Override
	public String getGrandCostPerClick() {
		if(this.grandTotalClicks.compareTo(0)==0) return "N/A";
		BigDecimal grandTotalClicks = new BigDecimal(this.grandTotalClicks);
		return this.grandTotalSpend.divide(grandTotalClicks,2,RoundingMode.HALF_UP).toPlainString();
	}
	
	@Override
	public String getGrandTotalOneWeekOrdersPlaced() {
		return this.grandTotalOneWeekOrdersPlaced.toString();
	}

	@Override
	public String getGrandTotalOneWeekOrderedProductSales() {
		return this.grandTotalOneWeekOrderedProductSales.setScale(this.currecny.getScale()).toPlainString();
	}
	
	@Override
	public String getGrandAcos() {
		if(this.grandTotalOneWeekOrderedProductSales.compareTo(BigDecimal.ZERO)==0) return "N/A";
		BigDecimal acos = this.grandTotalSpend.divide(this.grandTotalOneWeekOrderedProductSales,6,RoundingMode.HALF_UP); 
		return BigDecimalHelper.toPercentageString(acos, 2);
	}
	
	@Override
	public Map<String,List<SkuAdvertisingPerformanceReportLineItem>> getCampaignItems() {
		return this.campaignItems;
	}




}
