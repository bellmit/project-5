package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.api.v1.model.report.DateRangeReportItem;

public interface ImportAmazonDateRangeReportDao {

	Integer insertRecord(List<DateRangeReportItem> record);

	List<Object []> queryImportStatus();

	int deleteExistingEntriesByMarketplace(int marketplaceId, String dateTime);

}
