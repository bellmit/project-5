package com.kindminds.drs.persist.cqrs.store.event.dao.productV2.onboarding;

import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.OnboardingApplicationDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;



import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;

@Repository
public class OnboardingApplicationDaoImpl extends Dao implements
        OnboardingApplicationDao {



    @Override @Transactional("transactionManager")
    public String save(OnboardingApplication onboardingApplication) {

        if(onboardingApplication.getLineitems() != null){
            onboardingApplication.getLineitems().forEach(x->{
                this.saveLineitem(x);
            });
        }


        String sql = "insert into product.onboarding_application "
                + "( onboarding_application_id , create_time  ," +
                " supplier_company_id,  " +
                "  serial_number, status ) select "
                + "  :onboarding_applications_id , :create_time ," +
                " splr.id, :serial_number, :status "
                + "from company splr where splr.k_code = :kcode ";

        if(!StringUtils.hasText(onboardingApplication.getSupplierKcode())){
            sql = "insert into product.onboarding_application "
                    + "( onboarding_application_id , create_time  ," +
                    " supplier_company_id, serial_number, status ) values ( "
                    + " :onboarding_applications_id , :create_time ," +
                    " :supplierComId ,:serial_number, :status )" ;
        }




       MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("onboarding_applications_id", onboardingApplication.getId());

        q.addValue("create_time",  Timestamp.valueOf(onboardingApplication.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        if(!StringUtils.hasText(onboardingApplication.getSupplierKcode())) {
            q.addValue("supplierComId", onboardingApplication.getSupplierCompanyId());
        }else{
            q.addValue("kcode", onboardingApplication.getSupplierKcode());
        }

        q.addValue("serial_number", onboardingApplication.getSerialNumber());
        q.addValue("status", onboardingApplication.getStatus().getKey());


        Assert.isTrue( getNamedParameterJdbcTemplate().update(sql,q) ==1);

        //return onboardingApplication.getData();

        return "";
    }



    @Transactional("transactionManager")
    private void saveLineitem(OnboardingApplicationLineitem lineitem){



        String sql = "insert into product.onboarding_application_lineitem "
                + "( onboarding_application_lineitem_id , create_time  , " +
                "onboarding_application_id ," +
                " onboarding_application_create_time ,  " +
                "  product_id ,product_create_time , product_market_side , data , status ) values "
                + "  ( :lineitemId , :lineitem_createTime , :id , :create_time , " +
                " :productId, :product_create_time , :product_market_side  ,  CAST(:data as json) , :status )";


       MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("lineitemId", lineitem.getId());
        q.addValue("lineitem_createTime",  Timestamp.valueOf(lineitem.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("id", lineitem.getOnboardingApplicationId());
        q.addValue("create_time", Timestamp.valueOf(lineitem.getOnboardingApplicationCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("productId", lineitem.getProduct().getId());
        q.addValue("product_create_time", Timestamp.valueOf(lineitem.getProduct().getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
        q.addValue("product_market_side", lineitem.getProduct().getMarketside().getKey()

        );
        q.addValue("data", lineitem.getProduct().getData());
        q.addValue("status", lineitem.getStatus().getKey());


        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q) ==1);


    }



    @Override
    public List<Object[]> findOnboardingApplicationById(String id) {

        String sql = "select onboarding_application_id, create_time, supplier_company_id, " +
                "  serial_number, status " +
                "from product.onboarding_application " +
                "where onboarding_application_id = :id " +
                " order by create_time desc LIMIT 1";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id", id);


        List<Object[]> rList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return rList;
    }







}
