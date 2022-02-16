package com.kindminds.drs.api.v1.model.marketing;

import java.math.BigDecimal;

public interface AdSpendTransaction {
    String getSupplierKcode();
    String getMsdcKcode();
    int getCurrencyId();
    BigDecimal getSum();
    String getPeriodStartUtc();
    String getPeriodEndUtc();
}