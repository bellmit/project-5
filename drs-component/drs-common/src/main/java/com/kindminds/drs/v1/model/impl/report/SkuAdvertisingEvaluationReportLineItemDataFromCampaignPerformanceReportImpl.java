package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.SkuAdvertisingEvaluationReport.SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport;

import java.math.BigDecimal;

public class SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReportImpl implements SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport {
	
	private Integer totalClicks;
	private BigDecimal totalSpend;
	private BigDecimal totalOneWeekOrderedProductSales;

	public SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReportImpl(
			Integer totalClicks,
			BigDecimal totalSpend,
			BigDecimal totalOneWeekOrderedProductSales){
		this.totalClicks=totalClicks;
		this.totalSpend=totalSpend;
		this.totalOneWeekOrderedProductSales=totalOneWeekOrderedProductSales;
		
	}

	@Override
	public BigDecimal getTotalSpend() {
		return this.totalSpend;
	}

	@Override
	public BigDecimal getTotalOneWeekOrderedProductSales() {
		return this.totalOneWeekOrderedProductSales;
	}

	@Override
	public Integer getTotalClicks() {
		return this.totalClicks;
	}

}
