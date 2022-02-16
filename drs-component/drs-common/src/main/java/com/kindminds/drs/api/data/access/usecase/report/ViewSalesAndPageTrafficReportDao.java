package com.kindminds.drs.api.data.access.usecase.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ViewSalesAndPageTrafficReportDao {
	List<String> queryAsinBySku(int marketplaceId,List<String> skus);
	List<String> queryAsinByProduct(int marketplaceId, List<String> productBases);
	List<String> queryProductBases(String supplierKcode);
	Map<String, String> queryProductBaseNames(String supplierKcode);
	List<String> queryProductSkus(String productBase);
	Map<String, String> queryProductSkuNames(String productBase);
	Object [] querySummary(Date start,Date end,Integer marketplaceId,List<String> asins);
	List<Object []> queryHistoryLines(Date start, Date end, Integer marketplaceId, List<String> asins);
	Date queryStartDate(int marketplaceId, List<String> asins);
}
