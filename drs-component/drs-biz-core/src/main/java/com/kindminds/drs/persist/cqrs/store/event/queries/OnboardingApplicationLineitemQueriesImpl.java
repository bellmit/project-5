package com.kindminds.drs.persist.cqrs.store.event.queries;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.data.cqrs.store.event.queries.OnboardingApplicationLineitemQueries;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OnboardingApplicationLineitemQueriesImpl extends Dao implements OnboardingApplicationLineitemQueries {



    @Override
    public Optional<String> getId(String onboardingApplicationId, Country marketSide) {

        String sql = "select onboarding_application_lineitem_id , " +
                " onboarding_application_create_time " +
                " from product.onboarding_application_lineitem  "
                + " where onboarding_application_id = :onboardingApplicationId "
                + "  and product_market_side = :marketSide "
                + " order by onboarding_application_create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("onboardingApplicationId",onboardingApplicationId);
        q.addValue("marketSide",marketSide.getKey());


        List<String> ids = new ArrayList<String>();
        List<Object[]> columnsList  = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        if(columnsList.size()>0){
            String ct = columnsList.get(0)[1].toString();
            for(Object[] columns:columnsList){
                if(ct.equals(columns[1].toString()))
                    ids.add(columns[0].toString());
                else
                    break;
            }
        }


        return columnsList.size() > 0 ? Optional.of(ids.get(0)) : Optional.empty();
    }

    @Override
    public Optional<List<String>> getIds(String onboardingApplicationId) {


        String sql = "select onboarding_application_lineitem_id , " +
                " onboarding_application_create_time " +
                " from product.onboarding_application_lineitem  "
                + " where onboarding_application_id = :onboardingApplicationId " +
                " order by onboarding_application_create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("onboardingApplicationId",onboardingApplicationId);


        List<String> ids = new ArrayList<String>();
        List<Object[]> columnsList  = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        if(columnsList.size()>0){
            String ct = columnsList.get(0)[1].toString();
            for(Object[] columns:columnsList){
                if(ct.equals(columns[1].toString()))
                    ids.add(columns[0].toString());
                else
                    break;
            }
        }


        return Optional.of(ids);


    }


}
