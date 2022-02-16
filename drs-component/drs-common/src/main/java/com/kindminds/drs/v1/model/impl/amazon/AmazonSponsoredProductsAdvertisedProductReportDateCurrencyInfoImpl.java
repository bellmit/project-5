package com.kindminds.drs.v1.model.impl.amazon;

import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo;

import java.util.Date;

public class AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfoImpl implements AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo{

	private Date start;
	private Date end;
	private String currency;

	public AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfoImpl(
			Date start,
			Date end,
			String currency){
		this.start = start;
		this.end = end;
		this.currency = currency;
	}
			
	@Override
	public String toString() {
		return "AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfoImpl [getStartDate()=" + getStartDate()
				+ ", getEndDate()=" + getEndDate() + ", getCurrency()=" + getCurrency() + "]";
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