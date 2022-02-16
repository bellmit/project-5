package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.Currency;
import com.kindminds.drs.util.BigDecimalHelper;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingEvaluationReport.SkuAdvertisingEvaluationReportLineItem;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingEvaluationReport.SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SkuAdvertisingEvaluationReportLineItemImpl implements SkuAdvertisingEvaluationReportLineItem {
	
	private Currency currency;
	private String sku;
	private String skuName;
	private Integer totalSessions;
	private Integer totalPageViews;
	private BigDecimal totalBuyBoxTimesSessions;
	private Integer totalUnitOrdered;
	private BigDecimal totalOrderedProductSales;
	private SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport dataFromCampaignPerformanceReport;
	
	public Integer getNumericTotalSessions() {return this.totalSessions;}
	public Integer getNumericTotalPageViews() {return this.totalPageViews;}
	public BigDecimal getNumericBuyBoxRate() {
		if (this.totalSessions.compareTo(0)==0) return BigDecimal.ZERO;
		BigDecimal totalSessions = new BigDecimal(this.totalSessions);
		return totalBuyBoxTimesSessions.divide(totalSessions,6,RoundingMode.HALF_UP);
	}
	public Integer getNumericTotalUnitOrdered() {return this.totalUnitOrdered;}
	public BigDecimal getNumericTotalOrderedProductSales() {return this.totalOrderedProductSales;}
	public SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport getDataFromCampaignPerformanceReport() {return dataFromCampaignPerformanceReport;}
	
	public SkuAdvertisingEvaluationReportLineItemImpl(
			String sku,
			String skuName,
			Integer totalSessions,
			Integer totalPageViews,
			BigDecimal totalBuyBoxTimesSessions,
			Integer totalUnitOrdered,
			BigDecimal totalOrderedProductSales){
		this.sku = sku;
		this.skuName = skuName;
		this.totalSessions = totalSessions;
		this.totalPageViews = totalPageViews;
		this.totalBuyBoxTimesSessions = totalBuyBoxTimesSessions;
		this.totalUnitOrdered = totalUnitOrdered;
		this.totalOrderedProductSales = totalOrderedProductSales;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public void setDataFromCampaignPerformanceReport(SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport dataFromCampaignPerformanceReport) {
		this.dataFromCampaignPerformanceReport = dataFromCampaignPerformanceReport;
	}
	
	private int getScale(){
		return this.currency.getScale();
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getSkuName() {
		return this.skuName;
	}

	@Override
	public String getTotalAdClicks() {
		if(this.dataFromCampaignPerformanceReport==null) return "N/A";
		Integer totalClicks = this.dataFromCampaignPerformanceReport.getTotalClicks();
		return totalClicks.toString();
	}

	@Override
	public String getTotalAdSpend() {
		if(this.dataFromCampaignPerformanceReport==null) return "N/A";
		BigDecimal totalSpend = this.dataFromCampaignPerformanceReport.getTotalSpend();
		return totalSpend.setScale(this.currency.getScale(),RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getTotalSessions() {
		return this.totalSessions.toString();
	}

	@Override
	public String getTotalPageViews() {
		return this.totalPageViews.toString();
	}

	@Override
	public String getBuyBoxPercentage() {
		if (this.totalSessions.compareTo(0)==0) return "N/A";
		return BigDecimalHelper.toPercentageString(getNumericBuyBoxRate(),2);
	}

	@Override
	public String getTotalUnitOrdered() {
		return this.totalUnitOrdered.toString();
	}

	@Override
	public String getTotalOrderedProductSales() {
		return this.totalOrderedProductSales.setScale(this.getScale()).toPlainString();
	}

	@Override
	public String getConversionRate() {
		if(this.totalSessions.compareTo(0)==0) return "N/A";
		BigDecimal totalUnitOrdered = new BigDecimal(this.totalUnitOrdered);
		BigDecimal totalSessions = new BigDecimal(this.totalSessions);
		BigDecimal conversionRate = totalUnitOrdered.divide(totalSessions,6,RoundingMode.HALF_UP);
		return BigDecimalHelper.toPercentageString(conversionRate,2);
	}

	@Override
	public String getAcos() {
		if(this.dataFromCampaignPerformanceReport==null) return "N/A";
		if(this.totalOrderedProductSales.compareTo(BigDecimal.ZERO)==0) return "N/A";
		BigDecimal totalSpend = this.dataFromCampaignPerformanceReport.getTotalSpend();
		return BigDecimalHelper.toPercentageString(totalSpend.divide(this.totalOrderedProductSales,4,RoundingMode.HALF_UP),2);
	}

	@Override
	public String getTotalClicksOverTotalPageViews() {
		if(this.dataFromCampaignPerformanceReport==null) return "N/A";
		if(this.totalPageViews.compareTo(0)==0) return "N/A";
		BigDecimal totalClicks = new BigDecimal(this.dataFromCampaignPerformanceReport.getTotalClicks());
		BigDecimal totalPageViews = new BigDecimal(this.totalPageViews);
		return BigDecimalHelper.toPercentageString(totalClicks.divide(totalPageViews,4,RoundingMode.HALF_UP),2);
	}


}
