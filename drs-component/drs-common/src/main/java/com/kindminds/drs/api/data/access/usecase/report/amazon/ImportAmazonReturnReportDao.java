package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.amazon.AmazonReturnReportRawLine;

public interface ImportAmazonReturnReportDao {
	public Map<String,Date> queryFulfillmentCenterIdToLatestReturnEventDateMap();
	public int insertLineTimes(List<AmazonReturnReportRawLine> rawLineList);
}
