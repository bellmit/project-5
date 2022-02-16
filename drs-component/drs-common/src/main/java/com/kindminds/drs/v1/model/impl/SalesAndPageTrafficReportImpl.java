package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.SalesAndPageTrafficReport;
import com.kindminds.drs.api.v1.model.report.SalesAndPageTrafficReportSummary;
import com.kindminds.drs.util.DateHelper;

import java.util.Date;

public class SalesAndPageTrafficReportImpl implements SalesAndPageTrafficReport {
	
	private Currency currency;
	private Date startDate;
	private Date endDate;
	private SalesAndPageTrafficReportSummary summary;
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setSummary(SalesAndPageTrafficReportSummary summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "SalesAndPageTrafficReportImpl [getCurrency()=" + getCurrency() + ", getStartDate()=" + getStartDate()
				+ ", getEndDate()=" + getEndDate() + ", getTotalOrderItems()=" + getTotalOrderItems()
				+ ", getTotalOrderedProductSales()=" + getTotalOrderedProductSales() + ", getTotalSessions()="
				+ getTotalSessions() + "]";
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public String getStartDate() {
		return DateHelper.toString(this.startDate,"yyyy-MM-dd","UTC");
	}

	@Override
	public String getEndDate() {
		return DateHelper.toString(this.endDate,"yyyy-MM-dd","UTC");
	}

	@Override
	public String getTotalOrderItems() {
		return this.summary.getTotalOrderItems()==null?"N/A":this.summary.getTotalOrderItems().toString();
	}

	@Override
	public String getTotalOrderedProductSales() {
		return this.summary.getTotalOrderedProductSales()==null?"N/A":this.summary.getTotalOrderedProductSales().stripTrailingZeros().toPlainString();
	}

	@Override
	public String getTotalSessions() {
		return this.summary.getTotalSessions()==null?"N/A":this.summary.getTotalSessions().toString();
	}

	@Override
	public String getTotalUnitsOrdered() {
		return this.summary.getTotalUnitsOrdered()==null?"N/A":this.summary.getTotalUnitsOrdered().toString();
	}

}
