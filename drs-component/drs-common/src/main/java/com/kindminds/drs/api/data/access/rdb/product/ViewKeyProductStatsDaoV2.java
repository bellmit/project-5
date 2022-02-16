package com.kindminds.drs.api.data.access.rdb.product;

import com.kindminds.drs.Filter;
import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductBaseRetailPrice;
import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductInventorySupplyStats;
import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductTotalOrder;

import java.util.Date;
import java.util.List;

public interface ViewKeyProductStatsDaoV2 {
    List<KeyProductBaseRetailPrice> queryBaseRetailPrice(Filter filter);

    List<KeyProductTotalOrder> queryTotalOrder(Filter filter, Date from, Date to);

    List<KeyProductInventorySupplyStats> queryInventorySupplyStatsByFilter(Filter filter);

    Date queryLastPeriodEnd();
}
