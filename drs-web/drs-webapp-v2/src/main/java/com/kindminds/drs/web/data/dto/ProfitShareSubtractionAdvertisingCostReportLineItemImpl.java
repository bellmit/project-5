package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionAdvertisingCostReport;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProfitShareSubtractionAdvertisingCostReportLineItemImpl implements ProfitShareSubtractionAdvertisingCostReport.ProfitShareSubtractionAdvertisingCostReportLineItem {
    private String itemNote;
    private String currency;
    private BigDecimal amount;
    private BigDecimal vatRate;
    private BigDecimal subTotal;
    
    public ProfitShareSubtractionAdvertisingCostReportLineItemImpl() {}

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
        return vatRate.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getSubTotal() {
        return subTotal;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProfitShareSubtractionAdvertisingCostReportLineItemImpl [itemNote=");
		builder.append(itemNote);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", amount=");
		builder.append(amount);
		builder.append("]");
		return builder.toString();
	}
}
