package com.kindminds.drs.persist.cqrs.store.read.dao.productV2.onboarding;

import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.onboarding.OnboardingApplicationLineitemViewDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Repository
public class OnboardingApplicationLineitemViewDaoImpl extends Dao implements OnboardingApplicationLineitemViewDao {





    @Override @Transactional("transactionManager")
    public void saveView(OnboardingApplicationLineitem lineitem){


        String sql = "insert into product.onboarding_application_lineitem_view "
                + "( onboarding_application_lineitem_id , onboarding_application_id ," +
                "  product_id , product_market_side , data , status , create_time , update_time ) values "
                + "  ( :lineitemId , :id , :productId , :product_market_side  ,  CAST(:data as json)  " +
                "  , :status , :create_time , :update_time )";


      MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("lineitemId", lineitem.getId());
        q.addValue("id", lineitem.getOnboardingApplicationId());

        q.addValue("productId", lineitem.getProduct().getId());
        q.addValue("product_market_side", lineitem.getProduct().getMarketside().getKey());

        q.addValue("data", lineitem.getProduct().getData());

        q.addValue("status", lineitem.getStatus().getKey());


        q.addValue("create_time", Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));


        //Assert.isTrue(q.executeUpdate()==1);


    }


    @Override
    @Transactional("transactionManager")
    public void deleteView(String onboardingApplicationId) {

        String sql = "delete from product.onboarding_application_lineitem_view  " +
                " where onboarding_application_id = :id";
      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",onboardingApplicationId);
        getNamedParameterJdbcTemplate().update(sql,q);


    }


}
