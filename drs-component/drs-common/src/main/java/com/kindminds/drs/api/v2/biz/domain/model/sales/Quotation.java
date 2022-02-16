package com.kindminds.drs.api.v2.biz.domain.model.sales;

import com.kindminds.drs.api.v2.biz.domain.model.sales.service.FeeType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface Quotation {

    String getId();

    String getSerialNumber();

    OffsetDateTime getCreateTime();

    Integer getSupplierCompanyId();

    FeeType getFeeType();

    BigDecimal getSalePrice();

    BigDecimal getPreferentialPrice();

    BigDecimal getFinalPrice();

    String getSummary();

    void setSummary(String summary);

    void generateQuotationNumber();

    void generatePrice();

    void reducePrice(BigDecimal reducedPrice);

    void finalizePrice();

    void accept();

    void reject();

    boolean isAccepted();

    boolean isRejected();





}
