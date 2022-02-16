package com.kindminds.drs.api.data.cqrs.store.read.dao.sales;

import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest;

import java.util.List;

public interface QuoteRequestViewDao {

    void insertView(QuoteRequest QuoteRequest);

    void deleteView(String quoteRequestId);

    List<Object[]> getViewById(String quoteRequestId);


}
