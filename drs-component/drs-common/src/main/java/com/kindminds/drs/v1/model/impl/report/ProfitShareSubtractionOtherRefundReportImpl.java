package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionOtherRefundReport;

import java.math.BigDecimal;
import java.util.List;

public class ProfitShareSubtractionOtherRefundReportImpl implements ProfitShareSubtractionOtherRefundReport {
    private String dateStart;
    private String dateEnd;
    private String isurKcode;
    private String rcvrKcode;
    private BigDecimal total;
    private List<ProfitShareSubtractionOtherRefundReportLineItem> lineItems;

    @Override
    public String getDateStart() {
        return dateStart;
    }

    @Override
    public String getDateEnd() {
        return dateEnd;
    }

    @Override
    public String getIsurKcode() {
        return isurKcode;
    }

    @Override
    public String getRcvrKcode() {
        return rcvrKcode;
    }

    @Override
    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public List<ProfitShareSubtractionOtherRefundReportLineItem> getLineItems() {
        return lineItems;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setIsurKcode(String isurKcode) {
        this.isurKcode = isurKcode;
    }

    public void setRcvrKcode(String rcvrKcode) {
        this.rcvrKcode = rcvrKcode;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setLineItems(List<ProfitShareSubtractionOtherRefundReportLineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
