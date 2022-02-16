package com.kindminds.drs.api.data.row.marketing;

import java.math.BigDecimal;

public interface CampaignRow {

     int getId();

     String getkCode();

     String getName();

     String getStartDate();

     BigDecimal getTotalSpend();

     int getOrderedProductSales();


}