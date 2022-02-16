package com.kindminds.drs.api.usecase.settlement;

import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;

public interface AddSettlementPeriodUco {
	List<SettlementPeriod> getRecentPeriods();
	String addPeriod();
}
