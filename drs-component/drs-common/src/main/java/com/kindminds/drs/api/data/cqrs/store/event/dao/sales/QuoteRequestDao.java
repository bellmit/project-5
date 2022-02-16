package com.kindminds.drs.api.data.cqrs.store.event.dao.sales;

import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest;

import java.util.List;

public interface QuoteRequestDao {

    void insert(QuoteRequest quoteRequest);
    void insert(Iterable<QuoteRequest> quoteRequests);
    void insertQuoteRequestOnboardingApp(String quoteRequestId, String onboardingId);

    List<Object[]> getQuoteRequestById(String id);

    String queryOnboardingIdByQuoteRequestId(String quoteRequestId);

    String queryCompanyCodeById(Integer companyId);

}
