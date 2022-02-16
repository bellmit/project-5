package com.kindminds.drs.api.data.access.rdb.common;

import java.util.Date;
import java.util.List;

public interface SettlementPeriodListDao {
    List<Object []> querySettlementPeriodList();
    Date queryLastSettlementEnd();
    Boolean isLatestSettlementPeriodSettled();
}
