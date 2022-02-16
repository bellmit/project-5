package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionOtherRefundReport;

import java.math.BigDecimal;

public class ProfitShareSubtractionOtherRefundReportLineItemImpl implements ProfitShareSubtractionOtherRefundReport.ProfitShareSubtractionOtherRefundReportLineItem {
    private String itemNote;
    private String currency;
    private BigDecimal amount;

    public ProfitShareSubtractionOtherRefundReportLineItemImpl(String itemNote, String currency, BigDecimal amount) {
        this.itemNote = itemNote;
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public String getItemNote() {
        return itemNote;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
