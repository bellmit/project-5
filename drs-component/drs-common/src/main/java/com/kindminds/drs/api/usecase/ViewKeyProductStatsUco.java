package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport;

public interface ViewKeyProductStatsUco {

	KeyProductStatsReport getKeyProductStatsReport(Boolean isSupplier , String companyKcode );

}