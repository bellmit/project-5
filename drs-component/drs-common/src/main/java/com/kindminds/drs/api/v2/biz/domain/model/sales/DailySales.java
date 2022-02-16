package com.kindminds.drs.api.v2.biz.domain.model.sales;

import java.math.BigDecimal;

public interface DailySales{

    String getKcode() ;
    int getQty();
    BigDecimal getRevenueUSD();
    String getSalesDate();

}