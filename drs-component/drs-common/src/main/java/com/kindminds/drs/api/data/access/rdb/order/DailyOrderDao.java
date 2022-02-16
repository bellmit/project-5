package com.kindminds.drs.api.data.access.rdb.order;

import com.kindminds.drs.Filter;

public interface DailyOrderDao {
    int queryDailyOrderByFilter(Filter filter);
}
