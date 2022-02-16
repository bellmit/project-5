package com.kindminds.drs.persist.cqrs.store.read.dao.productV2.onboarding;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.onboarding.OnboardingApplicationViewDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Repository
public class OnboardingApplicationViewDaoImpl extends Dao implements OnboardingApplicationViewDao {



    private Boolean isOAExisted(String id){

        String sql = "select onboarding_application_id  " +
                "from product.onboarding_application_view  " +
                " where onboarding_application_id = :id ";

      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",id);

        String rList = null;
        try{
            rList = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
        } catch (EmptyResultDataAccessException e) {
        }

        return rList != null ? true : false;
    }

    private Boolean isOALineitemExisted(String id){

        String sql = "select onboarding_application_lineitem_id  " +
                "from product.onboarding_application_lineitem_view  " +
                " where onboarding_application_lineitem_id = :id ";

      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",id);

        String rList = null;
        try{
         rList = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
        } catch (EmptyResultDataAccessException e) {
        }

        return rList != null ? true : false;
    }

    @Override @Transactional("transactionManager")
    public String saveView(OnboardingApplication onboardingApplication) {

        if(onboardingApplication.getLineitems() != null){
            onboardingApplication.getLineitems().forEach(x->{
                this.saveLineitemView(x);
            });

        }


        boolean isUpeate= false;
        String sql ="";
        if(!isOAExisted(onboardingApplication.getId())){
            sql = "insert into product.onboarding_application_view "
                    + "( onboarding_application_id , supplier_company_id   " +
                    ",create_time , update_time, serial_number, status ) select "
                    + "  :onboarding_applications_id , splr.id " +
                    ",:create_time , :update_time, :serial_number, :status  "
                    + " from company splr where splr.k_code = :kcode ";

        }else{
            isUpeate = true;
            sql = "update product.onboarding_application_view "
                    + " set serial_number  = :serial_number  , status = :status  ," +
                    " update_time = :update_time "
                    + " where onboarding_application_id = :onboarding_applications_id " ;
        }

      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("onboarding_applications_id", onboardingApplication.getId());

        if(!isUpeate){
            q.addValue("kcode", onboardingApplication.getSupplierKcode());
            q.addValue("create_time",  Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
        }

        q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
        q.addValue("serial_number", onboardingApplication.getSerialNumber());
        q.addValue("status", onboardingApplication.getStatus().getKey());

        getNamedParameterJdbcTemplate().update(sql,q);



        //return onboardingApplication.getData();

        return "";
    }

    @Override
    @Transactional("transactionManager")
    public String deleteView(String onboardingApplicationsId) {


        String sql = "delete from product.onboarding_application_view  " +
                " where onboarding_application_id = :id";
      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",onboardingApplicationsId);
        getNamedParameterJdbcTemplate().update(sql,q);


        return "";
    }

    @Transactional("transactionManager")
    private void saveLineitemView(OnboardingApplicationLineitem lineitem){

        String sql ="";
        boolean isUpeate= false;
        if(!isOALineitemExisted(lineitem.getId())){
            sql = "insert into product.onboarding_application_lineitem_view "
                    + "( onboarding_application_lineitem_id , onboarding_application_id ," +
                    "  product_id , product_market_side , product_base_code , data , status , create_time , update_time ) values "
                    + "  ( :lineitemId , :id , :productId , :product_market_side  , :product_base_code , CAST(:data as json)  " +
                    "  , :status , :create_time , :update_time )";
        }else{
            isUpeate = true;
            sql = "update product.onboarding_application_lineitem_view "
                    + " set onboarding_application_id = :id  , data = CAST(:data as json) ," +
                    "  status = :status  , update_time = :update_time "
                    + " where onboarding_application_lineitem_id = :lineitemId " ;
        }


      MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("lineitemId", lineitem.getId());
        q.addValue("id", lineitem.getOnboardingApplicationId());

        if(!isUpeate){
            q.addValue("productId", lineitem.getProduct().getId());
            q.addValue("product_market_side", lineitem.getProduct().getMarketside().getKey());
            q.addValue("product_base_code", lineitem.getProduct().getProductBaseCode());
            q.addValue("create_time", Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
        }

        q.addValue("data", lineitem.getProduct().getData());

        q.addValue("status", lineitem.getStatus().getKey());

        q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        getNamedParameterJdbcTemplate().update(sql,q);





    }

}
