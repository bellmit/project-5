package com.kindminds.drs.api.data.access.rdb.accounting;

import java.util.List;

public interface SettlementPeriodDao {
	List<Object []> queryRecentPeriods(int counts);
	Object [] queryPeriodById(int periodId);
}
