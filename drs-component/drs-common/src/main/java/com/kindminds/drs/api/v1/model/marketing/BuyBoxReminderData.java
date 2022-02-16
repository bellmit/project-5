package com.kindminds.drs.api.v1.model.marketing;

import java.math.BigDecimal;
import java.util.Date;

public interface BuyBoxReminderData {

    Date getReportDate();

    String getMarketplaceName();

    String getCompanyCode();

    String getShortNameEnUs();

    String getSkuCode();

    String getBaseCode();

    BigDecimal getBuyBoxRate();

}
