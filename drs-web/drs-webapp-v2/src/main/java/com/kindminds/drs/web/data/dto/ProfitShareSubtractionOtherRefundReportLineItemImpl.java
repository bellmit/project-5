package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionOtherRefundReport;

import java.math.BigDecimal;

public class ProfitShareSubtractionOtherRefundReportLineItemImpl implements ProfitShareSubtractionOtherRefundReport.ProfitShareSubtractionOtherRefundReportLineItem {
	private String itemNote;
    private String currency;
    private BigDecimal amount;

    public ProfitShareSubtractionOtherRefundReportLineItemImpl() {}
    
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProfitShareSubtractionOtherRefundReportLineItemImpl [itemNote=");
		builder.append(itemNote);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", amount=");
		builder.append(amount);
		builder.append("]");
		return builder.toString();
	}

}
