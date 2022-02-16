package com.kindminds.drs.persist.v1.model.mapping.accounting;






import com.kindminds.drs.api.v1.model.marketing.AdSpendTransaction;

import java.io.Serializable;
import java.math.BigDecimal;


public class AdSpendTransactionImpl implements AdSpendTransaction, Serializable {
    //@Id
    //@Column(name="supplier_kcode")
    private String supplierKcode;
    //@Id
    //@Column(name="msdc_kcode")
    private String msdcKcode;
    //@Column(name="currency_id")
    private int currencyId;
    //@Column(name="sum")
    private BigDecimal sum;
    //@Column(name="period_start_utc")
    private String periodStartUtc;
    //@Column(name="period_end_utc")
    private String periodEndUtc;

    public AdSpendTransactionImpl() {
    }

    public AdSpendTransactionImpl(String supplierKcode, String msdcKcode, int currencyId, BigDecimal sum, String periodStartUtc, String periodEndUtc) {
        this.supplierKcode = supplierKcode;
        this.msdcKcode = msdcKcode;
        this.currencyId = currencyId;
        this.sum = sum;
        this.periodStartUtc = periodStartUtc;
        this.periodEndUtc = periodEndUtc;
    }

    @Override
    public String getSupplierKcode() {
        return supplierKcode;
    }

    @Override
    public String getMsdcKcode() {
        return msdcKcode;
    }

    @Override
    public int getCurrencyId() {
        return currencyId;
    }

    @Override
    public BigDecimal getSum() {
        return sum;
    }

    @Override
    public String getPeriodStartUtc() {
        return periodStartUtc;
    }

    @Override
    public String getPeriodEndUtc() {
        return periodEndUtc;
    }
}