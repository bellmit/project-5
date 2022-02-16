package com.kindminds.drs.api.v2.biz.domain.model.sales;

import java.math.BigDecimal;
import java.util.Map;

public interface QuoteRequestView {

    String getQuoteRequestId();
    String getCompanyCode();
    Integer getCompanyId();
    String getQuotationId();
    String getQuotationSerialNumber();
    BigDecimal getOriginalPrice();
    BigDecimal getPreferentialPrice();
    BigDecimal getFinalPrice();
    String getQuotationSummary();
    String getRequestStatus();
    Integer getRequestStatusId();
    String getOnboardingApplicationId();
    String getOnboardingSerialNumber();
    Map<String, Integer[]> getBpTable();

}
