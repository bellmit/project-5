package com.kindminds.drs.persist.cqrs.store.read.queries;

import com.kindminds.drs.api.data.cqrs.store.read.queries.QuoteRequestViewQueries;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;



import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class QuoteRequestViewQueriesImpl extends Dao implements QuoteRequestViewQueries {





    @Override @SuppressWarnings("unchecked")
    public String getEmailListByCompanyId(Integer companyId) {
        String sql = "SELECT string_agg(user_email, ',')  " +
                " FROM public.user_info " +
                " WHERE company_id = :company_id " +
                " group by company_id ";
       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("company_id", companyId);

        List<String> results = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        if (results.isEmpty()) return null;

        return results.get(0);
    }

    @Override @SuppressWarnings("unchecked")
    public Object[] getCompanyInfoByCode(Integer companyId) {
        String sql = "SELECT cpy.k_code, cse.email, name_en_us, name_local, short_name_en_us, short_name_local, \n" +
                " address, phone_number, official_registration_number, is_drs_company, is_supplier " +
                " FROM public.company cpy " +
                " INNER JOIN public.company_service_email cse on cse.company_id = cpy.id " +
                " WHERE cpy.id = :company_id ";

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("company_id", companyId);
        List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    @Override
    public String queryCompanyCodeById(Integer companyId) {
        String sql = "SELECT k_code " +
                " FROM public.company " +
                " WHERE id = :company_id ";
        
       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("company_id", companyId);
        return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
    }

    @Override @SuppressWarnings("unchecked")
    public Map<String, Integer[]> getBaseCodesToCountryIds(Integer companyId) {

        String sql = "select pbm.product_base_code, string_agg(pbm.market_side, ',') " +
                " from " +
                "(SELECT pv.product_base_code, pv.market_side\\:\\:text " +
                " FROM product.product_view pv " +
                " INNER JOIN company cpy ON cpy.id = pv.supplier_company_id " +
                " where cpy.id = :companyId " +
                " group by pv.product_base_code, pv.market_side " +
                ") pbm " +
                " group by pbm.product_base_code " +
                " order by pbm.product_base_code ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("companyId", companyId);
        List<Object[]> results = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        int marketplaceCount = 7;
        Map<String, Integer[]> baseCodesToMarketplacesMap = new TreeMap<>();
        for (Object[] column : results) {
            String baseProduct = (String) column[0];
            String[] marketplaces = ((String)column[1]).split(",");

            Integer[] countryIds = new Integer[marketplaceCount];
            Arrays.fill(countryIds, 0);

            for (String country : marketplaces) {
                Integer countryId = Integer.valueOf(country);
                if (countryId > 1) {
                    countryIds[countryId - 2] = 1;
                }
            }
            baseCodesToMarketplacesMap.put(baseProduct, countryIds);
        }

        return baseCodesToMarketplacesMap;
    }

}
