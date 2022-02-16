package com.kindminds.drs.v1.model.impl.logistics;

import com.kindminds.drs.api.v1.model.amazon.OriginalIvsUnsInfo;

import java.math.BigDecimal;

public class OriginalIvsUnsInfoImpl implements OriginalIvsUnsInfo {
    private String ivsName;
    private String unsName;
    private BigDecimal fcaPrice;
    private BigDecimal exportExchangeRate;

    public OriginalIvsUnsInfoImpl() {

    }

    public OriginalIvsUnsInfoImpl(String ivsName, String unsName, BigDecimal fcaPrice, BigDecimal exportExchangeRate) {
        this.ivsName = ivsName;
        this.unsName = unsName;
        this.fcaPrice = fcaPrice;
        this.exportExchangeRate = exportExchangeRate;
    }

    public String getIvsName() {
        return ivsName;
    }

    public BigDecimal getFcaPrice() {
        return fcaPrice;
    }

    public String getUnsName() {
        return unsName;
    }

    public BigDecimal getExportExchangeRate() {
        return exportExchangeRate;
    }
}
