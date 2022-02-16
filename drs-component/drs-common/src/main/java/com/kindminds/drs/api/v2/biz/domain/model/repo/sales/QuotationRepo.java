package com.kindminds.drs.api.v2.biz.domain.model.repo.sales;

import com.kindminds.drs.api.v2.biz.domain.model.sales.Quotation;
import com.kindminds.drs.api.v2.biz.domain.model.Repository;
import java.math.BigDecimal;

public  interface QuotationRepo extends Repository<Quotation> {

    String getSummary(int companyId);

    BigDecimal getMonthlyServiceFee(String regionCount, int bpCount);

    int getSerialNumber(int companyId);
}