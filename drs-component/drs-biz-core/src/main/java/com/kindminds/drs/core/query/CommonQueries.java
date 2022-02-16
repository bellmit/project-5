package com.kindminds.drs.core.query;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;




import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class CommonQueries extends Dao {



    private final List<String> unAvailableComKcodes = Arrays.asList("K101","K408","K448","K490", "K493", "K496", "K501", "K505", "K509", "K512");
    private final List<String> unAvailableComKcodesIncludeRetail = Arrays.asList("K408","K448","K490", "K493", "K496", "K501", "K505", "K509", "K512");


    public Map<String, String> queryAllCompanyKcodeToShortEnUsNameMap() {
        String sql = "select k_code, short_name_en_us from company "
                + "where ( is_supplier = TRUE or is_drs_company = TRUE ) "
                + "and k_code not in (:unAvailableComKcodes) "
                + "order by k_code";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("unAvailableComKcodes", this.unAvailableComKcodes);
        List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        Map<String,String> allCompanyKcodeToNameMap = new TreeMap<>();
        for(Object[] items:result){
            allCompanyKcodeToNameMap.put((String)items[0], (String)items[1]);
        }
        return allCompanyKcodeToNameMap;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> querySupplierKcodeToShortEnUsNameMap() {
        String sql = "select k_code, short_name_en_us from company "
                + "where is_supplier=TRUE and k_code not in (:unAvailableComKcodes) "
                + "order by k_code";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("unAvailableComKcodes", this.unAvailableComKcodes);
        Map<String,String> supplierKcodeToNameMap = new TreeMap<>();
        List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        for(Object[] items:result){
            supplierKcodeToNameMap.put((String)items[0], (String)items[1]);
        }
        return supplierKcodeToNameMap;
    }

}
