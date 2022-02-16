package com.kindminds.drs.persist.cqrs.store.event.queries;

import com.kindminds.drs.api.data.cqrs.store.event.queries.OnboardingApplicationQueries;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public class OnboardingApplicationQueriesImpl extends Dao implements OnboardingApplicationQueries {






    @Override
    public Optional<String> getId(String serialNumber) {


        String sql = "select onboarding_application_id from product.onboarding_application "
                + "where serial_number = :serialNumber  " +
                " order by create_time desc ";
        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("serialNumber",serialNumber);


        String rList = null;
        try{
             rList = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
        } catch (EmptyResultDataAccessException e) {
        }

        return rList !=null ? Optional.of(rList) : Optional.empty();
    }

    @Override
    public Integer getNextSeq(String supplierKcode) {

        String sql = "select MAX(serial_number) " +
                "from product.onboarding_application poa " +
                "inner join company c on poa.supplier_company_id = c.id " +
                "where c.k_code = :kcode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("kcode", supplierKcode);
        String serial = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
        if(serial != null && !serial.isEmpty())
            return Integer.parseInt(serial.substring(serial.lastIndexOf('-') + 1)) + 1;
        else
            return 1;
    }



}
