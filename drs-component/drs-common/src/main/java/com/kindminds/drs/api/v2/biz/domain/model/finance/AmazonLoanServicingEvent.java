package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;

public class AmazonLoanServicingEvent {
    private int id;
    private String loanCurrency;
    private BigDecimal loanAmount;
    private String sourceBusinessEventType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoanCurrency() {
        return loanCurrency;
    }

    public void setLoanCurrency(String loanCurrency) {
        this.loanCurrency = loanCurrency;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getSourceBusinessEventType() {
        return sourceBusinessEventType;
    }

    public void setSourceBusinessEventType(String sourceBusinessEventType) {
        this.sourceBusinessEventType = sourceBusinessEventType;
    }
}
