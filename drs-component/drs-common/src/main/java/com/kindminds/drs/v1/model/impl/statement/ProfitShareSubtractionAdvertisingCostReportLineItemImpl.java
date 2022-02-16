package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionAdvertisingCostReport;

import java.math.BigDecimal;

public class ProfitShareSubtractionAdvertisingCostReportLineItemImpl implements ProfitShareSubtractionAdvertisingCostReport.ProfitShareSubtractionAdvertisingCostReportLineItem {
    private String itemNote;
    private String currency;
    private BigDecimal amount;
    private BigDecimal vatRate;
    private BigDecimal subTotal;

    public ProfitShareSubtractionAdvertisingCostReportLineItemImpl(String itemNote, String currency,
                                                                   BigDecimal amount, BigDecimal vatRate,
                                                                   BigDecimal subTotal) {
        this.itemNote = itemNote;
        this.currency = currency;
        this.amount = amount;
        this.vatRate = vatRate;
        this.subTotal = subTotal;
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

    @Override
    public BigDecimal getVatRate() {
        return vatRate;
    }

    @Override
    public BigDecimal getSubTotal() {
        return subTotal;
    }
}
