package com.kindminds.drs.api.data.access.rdb.sales;

import com.kindminds.drs.Filter;
import com.kindminds.drs.api.v2.biz.domain.model.sales.DetailPageSalesTraffic;

import java.util.Date;

public interface DetailPageSalesTrafficDao {
    DetailPageSalesTraffic queryPageSalesTraffic(Filter filter, Date from, Date to);
}
