package com.kindminds.drs.persist.es.onboarding;

import com.kindminds.drs.api.data.es.queries.productV2.onboarding.OnboardingApplicationDetailQueries;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;




import java.util.List;
import java.util.Optional;


@Repository
public class OnboardingApplicationDetailQueriesImpl extends Dao implements OnboardingApplicationDetailQueries {




    @Override
    public Optional<String> getTrialList(String lineitemId) {


        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_trial_listing  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();


    }

    @Override
    public Optional<String> getEvalSample(String lineitemId) {

        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_eval_sample  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();
    }

    @Override
    public Optional<String> getPresentSample(String lineitemId) {
        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_present_sample  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();
    }

    @Override
    public Optional<String> getApproveSample(String lineitemId) {
        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_approve_sample  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();
    }

    @Override
    public Optional<String> getProvideComment(String lineitemId) {
        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_provide_comment  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();
    }

    @Override
    public Optional<String> getGiveFeedback(String lineitemId) {
        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_give_feedback  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();
    }

    @Override
    public Optional<String> getCheckInsurance(String lineitemId) {
        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_check_insurance  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();
    }

    @Override
    public Optional<String> getCheckProfitability(String lineitemId) {
        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_check_profitability  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();
    }

    @Override
    public Optional<String> getCheckCompliance(String lineitemId) {
        String sql = "select CAST(data as text) as data  " +
                " from product.onboarding_application_lineitem_check_cp_ca  "
                + " where onboarding_application_lineitem_id = :id " +
                " order by create_time desc ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",lineitemId);


        List rList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);

        return rList.size() > 0 ? Optional.of(rList.get(0).toString()) : Optional.empty();
    }


}
