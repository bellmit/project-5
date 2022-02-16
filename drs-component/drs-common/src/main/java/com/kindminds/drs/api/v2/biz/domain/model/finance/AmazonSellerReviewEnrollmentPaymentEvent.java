package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonSellerReviewEnrollmentPaymentEvent {
    private int id;
    private Date postedDate;
    private String enrollmentId;
    private String parentAsin;
    private int feeComponentId;
    private int chargeComponentId;
    private String currency;
    private BigDecimal totalAmount;

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

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getParentAsin() {
        return parentAsin;
    }

    public void setParentAsin(String parentAsin) {
        this.parentAsin = parentAsin;
    }

    public int getFeeComponentId() {
        return feeComponentId;
    }

    public void setFeeComponentId(int feeComponentId) {
        this.feeComponentId = feeComponentId;
    }

    public int getChargeComponentId() {
        return chargeComponentId;
    }

    public void setChargeComponentId(int chargeComponentId) {
        this.chargeComponentId = chargeComponentId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
