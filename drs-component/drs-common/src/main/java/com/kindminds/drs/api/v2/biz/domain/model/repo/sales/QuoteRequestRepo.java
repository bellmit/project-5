package com.kindminds.drs.api.v2.biz.domain.model.repo.sales;

import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequestView;
import com.kindminds.drs.api.v2.biz.domain.model.Repository;

public interface QuoteRequestRepo extends Repository<QuoteRequest> {

    void addQuoteRequestOnboardingApp(String quoteRequestId, String onboardingId);

    String getCompanyCode(int companyId);

    void addView(QuoteRequest quoteRequest);

    void deleteView(String quoteRequestId);

    QuoteRequestView findViewById(String quoteRequestId);

}