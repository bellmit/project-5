package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.amazon.AmazonDetailPageSalesTrafficByChildItemReportRawLine;

public interface ImportAmazonDetailPageSalesTrafficByChildItemReportDao {
	Integer insertRawLines(Marketplace marketplace,int periodId, List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> rawLines);
	List<Integer> queryOccupiedPeriodIdList(Marketplace marketplace);
	List<Object []> querySettlementPeriodList();
	List<Object []> querySettlementPeriodList(List<Integer> excludedPeriodIds);
	int deleteReportDataByPeriod(Marketplace marketplace,Integer settlementPeriodId);
}
