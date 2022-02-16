package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;

public interface HsaCampaignReportDetail {

    int getId();
    String getSupplierKcode();
    String getMarketplaceName();
    Integer getMarketplaceId();
    String getCampaignName();
    String getReportDate();
    String getCurrencyName();
    BigDecimal getTotalSpend();

    Integer getImpressions();
    Integer getClicks();
    BigDecimal getClickThruRate();
    BigDecimal getCostPerClick();
    //BigDecimal getSpend();
    BigDecimal getTotalACOS();
    BigDecimal getTotalRoAS();
    BigDecimal getTotalSales14days();
    Integer getTotalOrders14days();
    Integer getTotalUnits14days();
    BigDecimal getConversionRate14days();




}