package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.List;

public interface ProfitShareSubtractionMarketingActivityExpenseReport {

    public String getDateStart();
    public String getDateEnd();
    public String getIsurKcode();
    public String getRcvrKcode();
    public BigDecimal getTotal();
    public List<ProfitShareSubtractionMarketingActivityExpenseReportLineItem> getLineItems();

    public interface ProfitShareSubtractionMarketingActivityExpenseReportLineItem{
        public String getItemNote();
        public String getCurrency();
        public BigDecimal getAmount();
    }
}
