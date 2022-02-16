package com.kindminds.drs.api.v1.model.marketing;

import java.math.BigDecimal;

public interface CampaignReportDetail {
    int getId();
    String getSupplierKcode();
    String getMarketplaceName();
    String getCampaignName();
    String getAdvertisedSku();
    String getSku();
    String getBp();
    String getPeriodStartUtc();
    String getPeriodEndUtc();
    String getCurrencyName();
    BigDecimal getTotalSpend();
}