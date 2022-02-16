package com.kindminds.drs.api.data.access.usecase.report;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.Marketplace;

public interface ViewSkuAdvertisingPerformanceReportDao {
	String queryLatestSettlementStartUtcDate();
	String queryLatestSettlementEndUtcDate();
	List<String> queryCampaignNames(String supplierKcode,Marketplace marketplace,Date start,Date end);
}
