package com.kindminds.drs.util;

import java.util.Date;

public interface SettlementPeriodHelper {
	Date getPeriodStart(int settlementPeriodId);
	Date getPeriodEnd(int settlementPeriodId);
}
