package com.kindminds.drs.api.v2.biz.domain.model.sales;

import java.math.BigDecimal;

public interface DetailPageSalesTraffic {

    int getSession();

    int getPageView();

    BigDecimal getBuyBoxPercentage();
}
