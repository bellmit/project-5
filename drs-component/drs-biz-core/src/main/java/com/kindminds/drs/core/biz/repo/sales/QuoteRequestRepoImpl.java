package com.kindminds.drs.core.biz.repo.sales;

import com.kindminds.drs.Filter;
import com.kindminds.drs.api.data.cqrs.store.event.dao.sales.QuoteRequestDao;
import com.kindminds.drs.api.data.cqrs.store.read.dao.sales.QuoteRequestViewDao;
import com.kindminds.drs.api.v2.biz.domain.model.repo.sales.QuoteRequestRepo;
import com.kindminds.drs.api.v2.biz.domain.model.sales.Quotation;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequestStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequestView;
import com.kindminds.drs.api.v2.biz.domain.model.sales.service.FeeType;
import com.kindminds.drs.persist.cqrs.store.read.queries.QuoteRequestViewQueriesImpl;


import com.kindminds.drs.service.util.SpringAppCtx;
import org.apache.commons.lang.NotImplementedException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QuoteRequestRepoImpl implements QuoteRequestRepo {


    private QuoteRequestDao dao =
            SpringAppCtx.get().getBean(QuoteRequestDao.class);

    private QuoteRequestViewDao viewDao =
            SpringAppCtx.get().getBean(QuoteRequestViewDao.class);

    private QuoteRequestViewQueriesImpl viewQuery =
            SpringAppCtx.get().getBean(QuoteRequestViewQueriesImpl.class);


    @Override
    public void add(QuoteRequest quoteRequest) {

        dao.insert(quoteRequest);
    }


    @Override
    public void edit(QuoteRequest quoteRequest) {
        throw new NotImplementedException();
    }

    @Override
    public void remove(QuoteRequest quoteRequest) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<QuoteRequest> findById(String id) {

        List<Object[]> resultList = dao.getQuoteRequestById(id);
        if(resultList.size() > 0){
            Object[] result = resultList.get(0);

            Integer supplierCompanyId = (Integer) result[3];
            FeeType feeType = FeeType.fromKey((Integer) result[4]);

            Quotation quotation = QuotationImpl.createFromQuery(result);

            String quoteRequestId = (String) result[11];
            OffsetDateTime createTime =
                    OffsetDateTime.ofInstant(((Timestamp)result[12]).toInstant(), ZoneId.of("Asia/Taipei"));

            QuoteRequestStatusType status = QuoteRequestStatusType.fromKey((Integer)result[13]);

            QuoteRequest quoteRequest = QuoteRequestImpl.createFromQuery(quoteRequestId,
                    createTime, supplierCompanyId, quotation, status, feeType);

            return  Optional.of(quoteRequest);

        }

        return Optional.empty();
    }

    @Override
    public void addQuoteRequestOnboardingApp(String requestId, String appId) {
        dao.insertQuoteRequestOnboardingApp(requestId, appId);
    }



    @Override
    public String getCompanyCode(int companyId) {
        return viewQuery.queryCompanyCodeById(companyId);
    }


    @Override
    public void addView(QuoteRequest quoteRequest) {
        viewDao.insertView(quoteRequest);
    }

    @Override
    public void deleteView(String quoteRequestId) {
        viewDao.deleteView(quoteRequestId);
    }

    @Override
    public QuoteRequestView findViewById(String quoteRequestId) {
        Object[] requestView = viewDao.getViewById(quoteRequestId).get(0);

        String requestId = (String) requestView[0];
        String companyCode = (String) requestView[1];
        Integer companyId = (Integer) requestView[2];
        String quotationId = (String) requestView[3];
        String quotationSerialNumber = (String) requestView[4];
        BigDecimal originalPrice = (BigDecimal) requestView[5];
        BigDecimal preferentialPrice = (BigDecimal) requestView[6];
        BigDecimal finalPrice = (BigDecimal) requestView[7];
        String quotationSummary = (String) requestView[8];
        Integer status = (Integer) requestView[9];
        String onboardingApplicationId = (String) requestView[10];
        String onboardingSerialNumber = (String) requestView[11];
        Map<String, Integer[]> bpTable = viewQuery.getBaseCodesToCountryIds(companyId);

        return new QuoteRequestViewImpl(requestId, companyCode, companyId,
                quotationId, quotationSerialNumber,
                originalPrice, preferentialPrice,
                finalPrice, quotationSummary,
                status, onboardingApplicationId,
                onboardingSerialNumber, bpTable);
    }

    @Override
    public void add(List<QuoteRequest> items) {

    }

    @Override
    public List<QuoteRequest> find( Filter filter) {
        return null;
    }

    @Override
    public Optional<QuoteRequest> findOne( Filter filter) {
        return Optional.empty();
    }
}
