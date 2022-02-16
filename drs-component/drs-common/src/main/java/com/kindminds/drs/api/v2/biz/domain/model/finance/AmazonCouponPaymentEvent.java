package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonCouponPaymentEvent {
    private int id;
    private Date postedDate;
    private String couponId;
    private String sellerCouponDescription;
    private Long clipOrRedemptionCount;
    private String paymentEventId;
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

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getSellerCouponDescription() {
        return sellerCouponDescription;
    }

    public void setSellerCouponDescription(String sellerCouponDescription) {
        this.sellerCouponDescription = sellerCouponDescription;
    }

    public Long getClipOrRedemptionCount() {
        return clipOrRedemptionCount;
    }

    public void setClipOrRedemptionCount(Long clipOrRedemptionCount) {
        this.clipOrRedemptionCount = clipOrRedemptionCount;
    }

    public String getPaymentEventId() {
        return paymentEventId;
    }

    public void setPaymentEventId(String paymentEventId) {
        this.paymentEventId = paymentEventId;
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
