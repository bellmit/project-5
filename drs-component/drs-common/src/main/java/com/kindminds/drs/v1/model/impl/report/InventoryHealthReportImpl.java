package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.InventoryHealthReport;
import com.kindminds.drs.util.DateHelper;

import java.util.Date;
import java.util.List;

public class InventoryHealthReportImpl implements InventoryHealthReport {
	
	private Marketplace marketplace;
	private String supplierKcode;
	private Date snapshotDate;
	private List<InventoryHealthReportLineItem> lineItems;
	
	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;
	}
	
	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode;
	}
	
	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}
	
	public void setLineItems(List<InventoryHealthReportLineItem> lineItems) {
		this.lineItems = lineItems;
	}

	@Override
	public Marketplace getMarketplace() {
		return this.marketplace;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	@Override
	public String getSnapshotDate() {
		return DateHelper.toString(this.snapshotDate, "yyyy-MM-dd", "UTC");
	}

	@Override
	public List<InventoryHealthReportLineItem> getLineItems() {
		return this.lineItems;
	}

}
