package com.kindminds.drs.api.data.report;



import com.kindminds.drs.api.v1.model.report.HsaCampaignReportDetail;

import java.math.BigDecimal;
import java.util.List;

public interface HsaCampaignReport {

    Integer getTotalImpressions();
    Integer getTotalClicks();
    BigDecimal getTotalClickThruRate();
    BigDecimal getTotalCostPerClick();
    BigDecimal getTotalSpend();
    BigDecimal getTotalSales14days();
    Integer getTotalOrders14days();
    Integer getTotalUnits14days();
    BigDecimal getTotalConversionRate14days();
    String getCurrency();
    List<HsaCampaignReportDetail> getHsaCampaignReportItems();

}
