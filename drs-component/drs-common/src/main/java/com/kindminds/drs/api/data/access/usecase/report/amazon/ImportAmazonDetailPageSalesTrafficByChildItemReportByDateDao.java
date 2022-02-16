package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.api.v1.model.amazon.AmazonDetailPageSalesTrafficByChildItemReportRawLine;

public interface ImportAmazonDetailPageSalesTrafficByChildItemReportByDateDao {
	boolean queryExist(int marketplaceId,Date reportDate);
	int insertReportLines(int marketplaceId,Date reportDate,List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> lineItems);
	List<Object[]> queryDistinctReportDateMarketplace(Date start,Date end);
	int delete(Date date,int marketplaceId);
}
