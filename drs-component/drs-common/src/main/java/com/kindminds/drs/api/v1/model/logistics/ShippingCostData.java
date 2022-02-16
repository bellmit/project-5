package com.kindminds.drs.api.v1.model.logistics;

import java.math.BigDecimal;

public interface ShippingCostData {

    String getSkuCode();
    String getProductBaseCode();
    String getCountryCode();
    String getHsCode();
    BigDecimal getPackageWeight();
    String getPackageWeightUnit();
    BigDecimal getPackageDimLong();
    BigDecimal getPackageDimMedium();
    BigDecimal getPackageDimShort();
    String getPackageDimUnit();
}
