package com.kindminds.drs.api.data.cqrs.store.read.queries;


import java.util.List;
import java.util.Map;

public interface QuoteRequestViewQueries {

    String getEmailListByCompanyId(Integer companyId);

    Object[] getCompanyInfoByCode(Integer companyId);

    String queryCompanyCodeById(Integer companyId);

    Map<String, Integer[]> getBaseCodesToCountryIds(Integer companyId);

}
