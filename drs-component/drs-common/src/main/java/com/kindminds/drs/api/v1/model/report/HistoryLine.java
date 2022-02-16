package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.Date;

public interface HistoryLine{
    String getDate();
    String getSessions();
    String getPageViews();
    String getBuyBoxPercentage();
    String getUnitSessionPercentage();
    String getOrderedProductSales();
    String getOrderItems();
    String getUnitsOrdered();
    Date getDateObj();
    BigDecimal getSessionsDec();
    BigDecimal getPageViewsDec();
    BigDecimal getSessionsXBuyBoxPercentDec();
    BigDecimal getUnitsOrderedDec();
    BigDecimal getOrderedProductSalesDec();
    BigDecimal getTotalOrderItemsDec();
    void setDate(Date date);
    void setSessions(BigDecimal sessions);
    void setPageViews(BigDecimal pageViews);
    void setSessionsMultiplyBuyboxPercentage(BigDecimal sessionsMultiplyBuyboxPercentage);
    void setUnitsOrdered(BigDecimal unitsOrdered);
    void setOrderedProductSales(BigDecimal orderedProductSales);
    void setTotalOrderItems(BigDecimal totalOrderItems);
}