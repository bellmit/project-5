package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonAdjustmentEvent {
    private int id;
    private String adjustmentType;
    private String adjustmentCurrency;
    private BigDecimal adjustmentAmount;
    private int adjustmentItemId;
    private Date postedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public String getAdjustmentCurrency() {
        return adjustmentCurrency;
    }

    public void setAdjustmentCurrency(String adjustmentCurrency) {
        this.adjustmentCurrency = adjustmentCurrency;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public int getAdjustmentItemId() {
        return adjustmentItemId;
    }

    public void setAdjustmentItemId(int adjustmentItemId) {
        this.adjustmentItemId = adjustmentItemId;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }
}
