package com.kindminds.drs.api.data.access.rdb.accounting;

import java.util.List;

public interface SettlementHandlerDao {
	List<Integer> queryAmazonSettlementReportReadyMarketplaceIds(int periodId);
}
