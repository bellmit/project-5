package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

public class AmazonTaxWithholdingEvent {
    private int id;
    private Date postedDate;
    private OffsetDateTime periodStart;
    private OffsetDateTime periodEnd;
    private String currency;
    private BigDecimal baseAmount;
    private BigDecimal withheldAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public OffsetDateTime getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(OffsetDateTime periodStart) {
        this.periodStart = periodStart;
    }

    public OffsetDateTime getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(OffsetDateTime periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getWithheldAmount() {
        return withheldAmount;
    }

    public void setWithheldAmount(BigDecimal withheldAmount) {
        this.withheldAmount = withheldAmount;
    }
}
