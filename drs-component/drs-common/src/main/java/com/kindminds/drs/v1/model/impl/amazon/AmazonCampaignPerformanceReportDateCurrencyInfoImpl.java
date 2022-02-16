package com.kindminds.drs.v1.model.impl.amazon;

import com.kindminds.drs.api.v1.model.report.AmazonCampaignPerformanceReportDateCurrencyInfo;

import java.util.Date;

public class AmazonCampaignPerformanceReportDateCurrencyInfoImpl implements AmazonCampaignPerformanceReportDateCurrencyInfo {
	
	private Date start;
	private Date end;
	private String currency;
	
	public AmazonCampaignPerformanceReportDateCurrencyInfoImpl(
			Date start,
			Date end,
			String currency){
		this.start = start;
		this.end = end;
		this.currency = currency;
	}

	@Override
	public Date getStartDate() {
		return this.start;
	}

	@Override
	public Date getEndDate() {
		return this.end;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

}
