package com.kindminds.drs.persist.v1.model.mapping.accounting;

import com.kindminds.drs.api.v1.model.coupon.CouponRedemption;


import java.math.BigDecimal;


public class CouponRedemptionImpl implements CouponRedemption {
    //@Id
    ////@Column(name="id")
    private int id;
    //@Column(name="transaction_time")
    private String transactionTime;
    //@Column(name="supplier_id", nullable = true)
    private Integer supplierId;

    //@Column(name="supplier_name")
    private String supplierName;

    //@Column(name="supplier_kcode")
    private String supplierKcode;
    //@Column(name="period_start_utc")
    private String periodStartUtc;
    //@Column(name="period_end_utc")
    private String periodEndUtc;
    //@Column(name="coupon")
    private String coupon;
    //@Column(name="amount")
    private BigDecimal amount;
    //@Column(name="currency_id")
    private Integer currencyId;
    //@Column(name="currency_name")
    private String currencyName;
    //@Column(name="marketplace_name")
    private String marketplaceName;
    //@Column(name="msdc_kcode")
    private String msdcKcode;
    // Reason if unable to process
    //@Column(name="reason", nullable = true)
    private String reason;

    public CouponRedemptionImpl() {
    }

    public CouponRedemptionImpl(int id, String transactionTime, Integer supplierId, String supplierName, String supplierKcode, String periodStartUtc, String periodEndUtc, String coupon, BigDecimal amount, Integer currencyId, String currencyName, String marketplaceName, String msdcKcode, String reason) {
        this.id = id;
        this.transactionTime = transactionTime;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierKcode = supplierKcode;
        this.periodStartUtc = periodStartUtc;
        this.periodEndUtc = periodEndUtc;
        this.coupon = coupon;
        this.amount = amount;
        this.currencyId = currencyId;
        this.currencyName = currencyName;
        this.marketplaceName = marketplaceName;
        this.msdcKcode = msdcKcode;
        this.reason = reason;
    }


    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTransactionTime() {
        return this.transactionTime;
    }

    @Override
    public Integer getSupplierId() {
        return this.supplierId;
    }

    @Override
    public String getSupplierName() {
        return this.supplierName;
    }

    @Override
    public String getSupplierKcode() { return this.supplierKcode; }

    @Override
    public String getPeriodStartUtc() {
        return this.periodStartUtc;
    }

    @Override
    public String getPeriodEndUtc() {
        return this.periodEndUtc;
    }

    @Override
    public String getCoupon() {
        return this.coupon;
    }

    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public Integer getCurrencyId() { return this.currencyId; }

    @Override
    public String getCurrencyName() {
        return this.currencyName;
    }

    @Override
    public String getMarketplaceName() {
        return this.marketplaceName;
    }

    @Override
    public String getMsdcKcode() {
        return this.msdcKcode;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CouponRedemptionImpl{" +
                "id=" + id +
                ", transactionTime='" + transactionTime + '\'' +
                ", supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                ", supplierKcode='" + supplierKcode + '\'' +
                ", periodStartUtc='" + periodStartUtc + '\'' +
                ", periodEndUtc='" + periodEndUtc + '\'' +
                ", coupon='" + coupon + '\'' +
                ", amount=" + amount +
                ", currencyId=" + currencyId +
                ", currencyName='" + currencyName + '\'' +
                ", marketplaceName='" + marketplaceName + '\'' +
                ", msdcKcode='" + msdcKcode + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
