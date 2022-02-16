package com.kindminds.drs.api.data.access.rdb.report;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.Marketplace;

public interface MarketingReportHelperDao {
	List<Object []> querySkuAdvertisingPerformanceReportLineItems(String supplierKcode, Marketplace marketplace, Date start, Date end, String campaignName);
}
