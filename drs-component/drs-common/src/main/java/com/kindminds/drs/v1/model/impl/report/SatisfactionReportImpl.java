package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.SatisfactionReportLineItem;

public class SatisfactionReportImpl implements SatisfactionReportLineItem {
	
	private String productSku;
	private String productName;
	private DashboardStatistics lastOnePeriodData;
	private DashboardStatistics lastTwoPeriodData;
	private DashboardStatistics lastSixPeriodData;

	public SatisfactionReportImpl(){

	}
	
	public SatisfactionReportImpl(SatisfactionReportLineItem proxy) {
		this.productSku = proxy.getProductSku();
		this.productName = proxy.getProductName();

		this.lastOnePeriodData = new DashboardStatisticsImpl(proxy.getLastOnePeriodData());
		this.lastTwoPeriodData = new DashboardStatisticsImpl(proxy.getLastTwoPeriodData());
		this.lastSixPeriodData = new DashboardStatisticsImpl(proxy.getLastSixPeriodData());
	}

	@Override
	public String getProductSku() {
		return this.productSku;
	}

	@Override
	public String getProductName() {
		return this.productName;
	}

	@Override
	public DashboardStatistics getLastOnePeriodData() {
		return this.lastOnePeriodData;
	}

	@Override
	public DashboardStatistics getLastTwoPeriodData() {
		return this.lastTwoPeriodData;
	}

	@Override
	public DashboardStatistics getLastSixPeriodData() {
		return this.lastSixPeriodData;
	}




	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setLastOnePeriodData(DashboardStatisticsImpl lastOnePeriodData) {
		this.lastOnePeriodData = lastOnePeriodData;
	}

	public void setLastTwoPeriodData(DashboardStatisticsImpl lastTwoPeriodData) {
		this.lastTwoPeriodData = lastTwoPeriodData;
	}

	public void setLastSixPeriodData(DashboardStatisticsImpl lastSixPeriodData) {
		this.lastSixPeriodData = lastSixPeriodData;
	}


}
