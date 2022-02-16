package com.kindminds.drs.core.biz.repo.sales;



import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequestStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequestView;

import java.math.BigDecimal;
import java.util.Map;

public class QuoteRequestViewImpl implements QuoteRequestView {

    private String quoteRequestId;
    private String companyCode;
    private Integer companyId;
    private String quotationId;
    private String quotationSerialNumber;
    private BigDecimal originalPrice;
    private BigDecimal preferentialPrice;
    private BigDecimal finalPrice;
    private String quotationSummary;
    private String requestStatus;
    private Integer requestStatusId;
    private String onboardingApplicationId;
    private String onboardingSerialNumber;
    private Map<String, Integer[]> bpTable;


    public QuoteRequestViewImpl(String quoteRequestId, String companyCode, Integer companyId,
                                String quotationId, String quotationSerialNumber,
                                BigDecimal originalPrice, BigDecimal preferentialPrice,
                                BigDecimal finalPrice, String quotationSummary,
                                Integer requestStatusId, String onboardingApplicationId,
                                String onboardingSerialNumber, Map<String, Integer[]> bpTable) {

        this.quoteRequestId = quoteRequestId;
        this.companyCode = companyCode;
        this.companyId = companyId;
        this.quotationId = quotationId;
        this.quotationSerialNumber = quotationSerialNumber;
        this.originalPrice = originalPrice;
        this.preferentialPrice = preferentialPrice;
        this.finalPrice = finalPrice;
        this.quotationSummary = quotationSummary;
        this.requestStatus = QuoteRequestStatusType.fromKey(requestStatusId).getText();
        this.requestStatusId = requestStatusId;
        this.onboardingApplicationId = onboardingApplicationId;
        this.onboardingSerialNumber = onboardingSerialNumber;
        this.bpTable = bpTable;
    }

    @Override
    public String getQuoteRequestId() {
        return quoteRequestId;
    }

    @Override
    public String getCompanyCode() {
        return companyCode;
    }

    @Override
    public Integer getCompanyId() {
        return companyId;
    }

    @Override
    public String getQuotationId() {
        return quotationId;
    }

    @Override
    public String getQuotationSerialNumber() {
        return quotationSerialNumber;
    }

    @Override
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    @Override
    public BigDecimal getPreferentialPrice() {
        return preferentialPrice;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    @Override
    public String getQuotationSummary() {
        return quotationSummary;
    }

    @Override
    public String getRequestStatus() {
        return requestStatus;
    }

    @Override
    public Integer getRequestStatusId() {
        return requestStatusId;
    }

    @Override
    public String getOnboardingApplicationId() {
        return onboardingApplicationId;
    }

    @Override
    public String getOnboardingSerialNumber() {
        return onboardingSerialNumber;
    }

    @Override
    public Map<String, Integer[]> getBpTable() {
        return bpTable;
    }

}
