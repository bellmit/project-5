package com.kindminds.drs.api.data.access.rdb.sales;

import com.kindminds.drs.Filter;


import java.util.List;

public  interface DailySalesDao {

    List queryDailySalesByFilter(Filter filter);

}