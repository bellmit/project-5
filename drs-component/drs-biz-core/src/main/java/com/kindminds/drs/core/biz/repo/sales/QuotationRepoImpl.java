package com.kindminds.drs.core.biz.repo.sales;

import com.kindminds.drs.Filter;


import com.kindminds.drs.api.data.es.dao.sales.QuotationDao;
import com.kindminds.drs.api.v2.biz.domain.model.repo.sales.QuotationRepo;

import com.kindminds.drs.api.v2.biz.domain.model.sales.Quotation;
import org.apache.commons.lang.NotImplementedException;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class QuotationRepoImpl implements QuotationRepo {


   // @Autowired
    private QuotationDao dao;


    @Override
    public void add(Quotation quotation) {
        dao.insert(quotation);
    }

    /*
    @Override
    public void add(Iterable<Quotation> quotations) {

    }
    */

    @Override
    public void edit(Quotation quotation) {
        throw new NotImplementedException();
    }

    @Override
    public void remove(Quotation quotation) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Quotation> findById(String id) {

        List<Object[]> resultList = dao.queryQuotationById(id);

        if(resultList.size() > 0){
            Object[] result = resultList.get(0);

            Quotation quotation = QuotationImpl.createFromQuery(result);

            return Optional.of(quotation);
        }

        return Optional.empty();
    }

    @Override
    public String getSummary(int companyId) {
        return dao.querySummaryByCompanyId(companyId);
    }

    @Override
    public BigDecimal getMonthlyServiceFee(String regionCount, int bpCount) {
        return dao.queryMonthlyServiceFee(regionCount, bpCount);
    }

    @Override
    public int getSerialNumber(int companyId) {
        return dao.queryNextSerialNumber(companyId);
    }

    @Override
    public void add(List<Quotation> items) {
        dao.insert(items);
    }

    @Override
    public List<Quotation> find( Filter filter) {
        return null;
    }

    @Override
    public Optional<Quotation> findOne( Filter filter) {
        return Optional.empty();
    }
}
