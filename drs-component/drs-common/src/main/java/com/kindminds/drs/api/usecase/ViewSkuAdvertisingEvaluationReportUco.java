package com.kindminds.drs.api.usecase;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingEvaluationReport;

public interface ViewSkuAdvertisingEvaluationReportUco {
	List<Marketplace> getMarketplaces();
	String getDefaultMarketplaceId();
	SkuAdvertisingEvaluationReport getReport(String supplierKcode,String marketplaceId,String settlementPeriodId);
	List<SettlementPeriod> getSettlementPeriodList();
	Map<String,String> getSupplierKcodeNameMap();
}
