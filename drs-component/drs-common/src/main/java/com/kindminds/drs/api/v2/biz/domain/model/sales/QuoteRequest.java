package com.kindminds.drs.api.v2.biz.domain.model.sales;

import com.kindminds.drs.api.v2.biz.domain.model.sales.service.FeeType;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface QuoteRequest {

    String getId();

    OffsetDateTime getCreateTime();

    Integer getSupplierCompanyId();

    QuoteRequestStatusType getStatus();

    FeeType getFeeType();

    Optional<Quotation> getQuotation();


    Quotation generateQuotation();

    void create(String preferentialPrice);

    void modify(String preferentialPrice);

    void accept();

    void reject();

    void confirm();



}
