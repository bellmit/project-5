package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.SalesAndPageTrafficReportSummary;

import java.math.BigDecimal;

public class SalesAndPageTrafficReportSummaryImpl implements SalesAndPageTrafficReportSummary{
	
	private Integer totalOrderItems;
	private BigDecimal totalOrderedProductSales;
	private Integer totalSessions;
	private Integer totalUnitsOrdered;

	public SalesAndPageTrafficReportSummaryImpl(Integer totalOrderItems, BigDecimal totalOrderedProductSales,
												Integer totalSessions, Integer totalUnitsOrdered) {
		super();
		this.totalOrderItems = totalOrderItems;
		this.totalOrderedProductSales = totalOrderedProductSales;
		this.totalSessions = totalSessions;
		this.totalUnitsOrdered = totalUnitsOrdered;
	}

	@Override
	public String toString() {
		return "SalesAndPageTrafficReportSummaryImpl [getTotalOrderItems()=" + getTotalOrderItems()
				+ ", getTotalOrderedProductSales()=" + getTotalOrderedProductSales() + ", getTotalSessions()="
				+ getTotalSessions() + ", getTotalUnitsOrdered()="
				+ getTotalUnitsOrdered() + "]";
	}

	@Override
	public Integer getTotalOrderItems() {
		return this.totalOrderItems;
	}

	@Override
	public BigDecimal getTotalOrderedProductSales() {
		return this.totalOrderedProductSales;
	}

	@Override
	public Integer getTotalSessions() {
		return this.totalSessions;
	}

	@Override
	public Integer getTotalUnitsOrdered() {
		return this.totalUnitsOrdered;
	}

}
