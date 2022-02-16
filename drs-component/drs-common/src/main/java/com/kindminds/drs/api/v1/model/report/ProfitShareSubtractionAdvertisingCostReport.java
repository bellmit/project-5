package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.List;

public interface ProfitShareSubtractionAdvertisingCostReport {

    public String getDateStart();
    public String getDateEnd();
    public String getIsurKcode();
    public String getRcvrKcode();
    public BigDecimal getTotal();
    public List<ProfitShareSubtractionAdvertisingCostReportLineItem> getLineItems();

    public interface ProfitShareSubtractionAdvertisingCostReportLineItem{
        public String getItemNote();
        public String getCurrency();
        public BigDecimal getAmount();
        BigDecimal getVatRate();
        BigDecimal getSubTotal();
    }
}
