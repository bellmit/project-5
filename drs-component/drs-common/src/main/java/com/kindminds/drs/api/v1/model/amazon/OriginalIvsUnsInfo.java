package com.kindminds.drs.api.v1.model.amazon;

import java.math.BigDecimal;

public interface OriginalIvsUnsInfo {

    String getIvsName();
    BigDecimal getFcaPrice();
    String getUnsName();
    BigDecimal getExportExchangeRate();
}
