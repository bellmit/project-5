package com.kindminds.drs.api.data.access.usecase.report;

import java.util.Date;
import java.util.List;

public interface ViewSkuAdvertisingEvaluationReportDao {
	Date queryPeriodStart(int settlementPeriodId);
	Date queryPeriodEnd(int settlementPeriodId);
	List<Object []> querySettlementPeriodList();
	List<Object []> queryLineItems(String supplierKcode, int marketplaceId, int settlementPeriodId);
	List<Object []> querySkuInfoFromCampaignPerformanceReport(List<String> skuList,int marketplaceId,Date start,Date end);
}
