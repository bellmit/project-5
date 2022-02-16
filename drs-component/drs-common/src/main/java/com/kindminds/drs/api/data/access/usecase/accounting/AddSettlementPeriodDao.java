package com.kindminds.drs.api.data.access.usecase.accounting;

import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;

public interface AddSettlementPeriodDao {
	void insertPeriod(SettlementPeriod period);
	boolean selectPeriodExisted(SettlementPeriod period);
}
