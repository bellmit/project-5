package com.kindminds.drs.api.v1.model.coupon;

import java.math.BigDecimal;

public interface CouponRedemption {
    int getId();
    String getTransactionTime();
    Integer getSupplierId();
    String getSupplierName();
    String getSupplierKcode();
    String getPeriodStartUtc();
    String getPeriodEndUtc();
    String getCoupon();
    BigDecimal getAmount();
    Integer getCurrencyId();
    String getCurrencyName();
    String getMarketplaceName();
    String getMsdcKcode();
    String getReason();
}
