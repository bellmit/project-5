package com.kindminds.drs.api.v2.biz.domain.model.sales;

import java.math.BigDecimal;
import java.util.List;

public interface  DailySalesSet{

    List<DailySales> getDailySales();

    BigDecimal sumTotalRevenue();

    int sumTotalQty() ;

}
