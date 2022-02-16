package com.kindminds.drs.api.data.access.rdb.accounting;

import java.util.Date;

public interface SettlementPeriodHelperDao {
	Date queryPeriodStart(int settlementPeriodId);
	Date queryPeriodEnd(int settlementPeriodId);
}
