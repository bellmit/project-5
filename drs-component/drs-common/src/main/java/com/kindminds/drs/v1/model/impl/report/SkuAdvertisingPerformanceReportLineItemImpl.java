package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.Currency;
import com.kindminds.drs.util.BigDecimalHelper;
import com.kindminds.drs.util.NumberHelper;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReportLineItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SkuAdvertisingPerformanceReportLineItemImpl implements SkuAdvertisingPerformanceReportLineItem {

	private Currency currency;
	private String sku;
	private Integer totalImpressions;
	private Integer totalClicks;
	private BigDecimal totalSpend;
	private Integer totalOneWeekOrdersPlaced;
	private BigDecimal totalOneWeekOrderedProductSales;
	
	public Integer getNumericTotalImpression(){return this.totalImpressions;}
	public Integer getNumericTotalClicks(){return this.totalClicks;}
	public BigDecimal getNumericTotalSpend(){return this.totalSpend;}
	public Integer getNumericTotalOneWeekOrdersPlaced(){return this.totalOneWeekOrdersPlaced;}
	public BigDecimal getNumericTotalOneWeekOrderedProductSales(){return this.totalOneWeekOrderedProductSales;}
	
	public SkuAdvertisingPerformanceReportLineItemImpl(
			String currency,
			String sku,
			Integer totalImpressions,
			Integer totalClicks,
			BigDecimal totalSpend,
			Integer totalOneWeekOrdersPlaced,
			BigDecimal totalOneWeekOrderedProductSales){
		this.currency = Currency.valueOf(currency);
		this.sku = sku;
		this.totalImpressions = totalImpressions;
		this.totalClicks = totalClicks;
		this.totalSpend = totalSpend;
		this.totalOneWeekOrdersPlaced = totalOneWeekOrdersPlaced;
		this.totalOneWeekOrderedProductSales = totalOneWeekOrderedProductSales;
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
	public Currency getCurrency(){
		return this.currency;
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getTotalImpression() {
		return NumberHelper.toGeneralCommaSeparatedString(this.totalImpressions, 0);		
	}

	@Override
	public String getTotalClicks() {
		return this.totalClicks.toString();
	}
	
	@Override
	public String getClickThroughRate() {
		if(this.totalImpressions.compareTo(0)==0) return "N/A";
		BigDecimal totalClicks = new BigDecimal(this.totalClicks);
		BigDecimal totalImpressions = new BigDecimal(this.totalImpressions);
		BigDecimal clickTroughRate = totalClicks.divide(totalImpressions,6,RoundingMode.HALF_UP);
		return BigDecimalHelper.toPercentageString(clickTroughRate,2);
	}

	@Override
	public String getTotalSpend() {
		return this.totalSpend.setScale(this.currency.getScale(),RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getCostPerClick() {
		if(this.totalClicks.compareTo(0)==0) return "N/A";
		BigDecimal totalClicks = new BigDecimal(this.totalClicks);
		return this.totalSpend.divide(totalClicks,2,RoundingMode.HALF_UP).toPlainString();
	}
	
	@Override
	public String getTotalOneWeekOrdersPlaced() {
		return this.totalOneWeekOrdersPlaced.toString();
	}

	@Override
	public String getTotalOneWeekOrderedProductSales() {
		return this.totalOneWeekOrderedProductSales.setScale(this.currency.getScale(),RoundingMode.HALF_UP).toPlainString();
	}
	
	@Override
	public String getAcos() {
		if(this.totalOneWeekOrderedProductSales.compareTo(BigDecimal.ZERO)==0) return "N/A";
		BigDecimal acos = this.totalSpend.divide(this.totalOneWeekOrderedProductSales,6,RoundingMode.HALF_UP); 
		return BigDecimalHelper.toPercentageString(acos, 2);
	}

}
