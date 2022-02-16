package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionMarketingActivityExpenseReport;

import java.math.BigDecimal;

public class ProfitShareSubtractionMarketingActivityExpenseReportLineItemImpl implements ProfitShareSubtractionMarketingActivityExpenseReport.ProfitShareSubtractionMarketingActivityExpenseReportLineItem {
    private String itemNote;
    private String currency;
    private BigDecimal amount;
    
    public ProfitShareSubtractionMarketingActivityExpenseReportLineItemImpl() {}

    public ProfitShareSubtractionMarketingActivityExpenseReportLineItemImpl(String itemNote, String currency, BigDecimal amount) {
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProfitShareSubtractionMarketingActivityExpenseReportLineItemImpl [itemNote=");
		builder.append(itemNote);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", amount=");
		builder.append(amount);
		builder.append("]");
		return builder.toString();
	}
}
