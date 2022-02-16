package com.kindminds.drs.api.v2.biz.domain.model.marketing;

import java.math.BigDecimal;


public  interface Campaign {

    int getId();
    String getKCode();
    String getName();
    String getStartDate();
    BigDecimal getTotalSepnd();
    int getOrderedProductSales();
    BigDecimal calcAcos();
}