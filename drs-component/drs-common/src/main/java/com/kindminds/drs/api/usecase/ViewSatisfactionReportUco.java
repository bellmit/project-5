package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.report.CustomerSatisfactionReport;

public interface ViewSatisfactionReportUco {
	public CustomerSatisfactionReport getReport(String marketPlace, String supplierKCode);

}
