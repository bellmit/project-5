package com.kindminds.drs.api.data.es.dao.sales;

import com.kindminds.drs.api.v2.biz.domain.model.sales.Quotation;

import java.math.BigDecimal;
import java.util.List;

public interface QuotationDao {

    Integer queryNextSerialNumber(Integer companyId);

    void insert(Quotation quotation);

    void insert(Iterable<Quotation> quotations);

    List<Object[]> queryQuotationById(String quotationId);

    String querySummaryByCompanyId(Integer companyId);

    BigDecimal queryMonthlyServiceFee(String regionCount, Integer bpCount);

}
