package com.kindminds.drs.api.usecase.inventory;

import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport;

public interface ViewInventoryPaymentReportUco {
	public InventoryPaymentReport getInventoryPaymentReport(String shipmentName);
}
