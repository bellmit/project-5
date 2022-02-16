package com.kindminds.drs.api.data.access.rdb.customercare;

import com.kindminds.drs.Filter;
import com.kindminds.drs.api.data.row.customercare.CustomerCareCaseRow;

import java.util.List;


public interface CustomerCareCaseDao {

    List<CustomerCareCaseRow> getCustomerCareCaseByFilter(Filter filter) ;
}