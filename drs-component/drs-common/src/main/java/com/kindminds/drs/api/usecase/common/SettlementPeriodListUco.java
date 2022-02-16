package com.kindminds.drs.api.usecase.common;

import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;

import java.util.Date;
import java.util.List;

public interface SettlementPeriodListUco {

    List<SettlementPeriod> getSettlementPeriodList();
    SettlementPeriod getLatestSettlementPeriod();
    Date getLastSettlementEnd();
    Boolean isLatestSettlementPeriodSettled();
}
