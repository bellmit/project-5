package com.kindminds.drs.api.v2.biz.domain.model.sales.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface Fee {

    String getId();

    OffsetDateTime getCreateTime();

    Integer getSupplierCompanyId();

    FeeType getType();

    BigDecimal getAmount();

    Boolean isActivated();

    void activate();

    void deactivate();


}
