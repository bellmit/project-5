package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.SearchTermReport;

import java.util.List;

public class SearchTermReportImpl implements SearchTermReport {
	
	private Marketplace marketplace;
	private List<SearchTermReportLineItem> lineItems;
	
	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;
	}
	
	public void setLineItems(List<SearchTermReportLineItem> lineItems) {
		this.lineItems = lineItems;
	}

	@Override
	public String toString() {
		return "SearchTermReportImpl [getMarketplace()=" + getMarketplace() + ", getLineItems()=" + getLineItems()
				+ "]";
	}

	@Override
	public Marketplace getMarketplace() {
		return this.marketplace;
	}

	@Override
	public List<SearchTermReportLineItem> getLineItems() {
		return this.lineItems;
	}

}
