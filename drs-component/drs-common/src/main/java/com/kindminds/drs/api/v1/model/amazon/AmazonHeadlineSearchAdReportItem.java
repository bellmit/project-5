package com.kindminds.drs.api.v1.model.amazon;

import java.math.BigDecimal;
import java.util.Date;

public interface AmazonHeadlineSearchAdReportItem {
    Integer getMarketplaceId();
    Date getReportDate();
    String getPortfolioName();
    String getCurrency();
    String getCampaignName();
    String getTargeting();
    String getMatchType();
    Integer getImpressions();
    Integer getClicks();
    BigDecimal getClickThruRate();

    Integer getViewableImpressions();

    BigDecimal getViewThruRate();

    BigDecimal getClickThruRateForViews();

    Integer getVideoFirstQuartileViews();

    Integer getVideoMidpointViews();

    Integer getVideoThirdQuartileViews();

    Integer getVideoCompleteViews();

    Integer getVideoUnmutes();

    Integer getViews5second();

    BigDecimal getViewRate5second();

    void setClickThruRate(BigDecimal clickThruRate);
    BigDecimal getCostPerClick();
    void setCostPerClick(BigDecimal costPerClick);
    BigDecimal getSpend();
    BigDecimal getTotalACOS();
    void setTotalACOS(BigDecimal totalACOS);
    BigDecimal getTotalRoAS();
    void setTotalRoAS(BigDecimal totalRoAS);
    BigDecimal getTotalSales14days();
    Integer getTotalOrders14days();
    Integer getTotalUnits14days();
    BigDecimal getConversionRate14days();
    void setConversionRate14days(BigDecimal conversionRate14days);
    Integer getOrdersNewToBrand();
    BigDecimal getPctOrdersNewToBrand();
    BigDecimal getSalesNewToBrand();
    BigDecimal getPctSalesNewToBrand();
    Integer getUnitsNewToBrand();
    BigDecimal getPctUnitsNewToBrand();
    BigDecimal getOrderRateNewToBrand();
}
