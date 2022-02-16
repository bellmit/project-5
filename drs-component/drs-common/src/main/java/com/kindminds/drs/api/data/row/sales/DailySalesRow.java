package com.kindminds.drs.api.data.row.sales;

import java.math.BigDecimal;


public interface DailySalesRow{

     int getId();

     String getKcode();

     String getSalesDate();

     String getSalesChannel();

     String getProductBaseCode();

     String getProductSkuCode();

     String getProductName();

     String getAsin();

     BigDecimal getRevenue();

     BigDecimal getRevenueUsd();

     BigDecimal getGrossProfit();

     BigDecimal getGrossProfitUsd();

     int getQty();


}