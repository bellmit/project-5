package com.kindminds.drs.api.data.row.logistics;

import java.math.BigDecimal;

public final class FreightFeeRateRow{

    private int id;
    private BigDecimal freightFeeRate;

    public FreightFeeRateRow() {}

    public FreightFeeRateRow(int id, BigDecimal freightFeeRate) {
        this.id  = id;
        this.freightFeeRate = freightFeeRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getFreightFeeRate() {
        return freightFeeRate;
    }

    public void setFreightFeeRate(BigDecimal freightFeeRate) {
        this.freightFeeRate = freightFeeRate;
    }
}
