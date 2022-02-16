package com.kindminds.drs.persist.cqrs.store.event.dao.productV2.onboarding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.OnboardingApplicationLineitemDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class OnboardingApplicationLineitemDaoImpl  extends Dao implements
        OnboardingApplicationLineitemDao {






    @Override @Transactional("transactionManager")
    public void save(OnboardingApplicationLineitem lineitem){



        String sql = "insert into product.onboarding_application_lineitem "
                + "( onboarding_application_lineitem_id , create_time  , " +
                "onboarding_application_id ," +
                " onboarding_application_create_time ,  " +
                "  product_id ,product_create_time , product_market_side , data , status , eval_product_info_status) values "
                + "  ( :lineitemId , :lineitem_createTime , :id , :create_time , " +
                " :productId, :product_create_time , :product_market_side  ,  CAST(:data as json) , :status , " +
                "  CAST(:epiStatus as json)    )";


     MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("lineitemId", lineitem.getId());
        q.addValue("lineitem_createTime",  Timestamp.valueOf(lineitem.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("id", lineitem.getOnboardingApplicationId());
        q.addValue("create_time", Timestamp.valueOf(lineitem.getOnboardingApplicationCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        /* todo temp commnet
        q.addValue("productId", lineitem.getProduct().getId());
        q.addValue("product_create_time", Timestamp.valueOf(lineitem.getProduct().getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
        q.addValue("product_market_side", lineitem.getProduct().getMarketside().getKey());
        q.addValue("data", lineitem.getProduct().getData());
        */
        q.addValue("productId", "");
        q.addValue("product_create_time",Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
        q.addValue("product_market_side", 1);
        q.addValue("data", "{}");

        q.addValue("status", lineitem.getStatus().getKey());

        if(lineitem.getEvalProductInfoStatus() != null){

           List<Integer> evalStList = lineitem.getEvalProductInfoStatus().stream().map(x -> x.getKey()).
                    collect(toList());

            ObjectMapper mapper = new ObjectMapper();
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                mapper.writeValue(out, evalStList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final byte[] data = out.toByteArray();

            q.addValue("epiStatus", new String(data));
        }else{
            q.addValue("epiStatus", "[]");
        }


        getNamedParameterJdbcTemplate().update(sql,q);


        //Assert.isTrue(==1);


    }


    @Override
    public List<Object[]> findOnboardingApplicationLineitem(String id) {

        String sql = "SELECT onboarding_application_lineitem_id, create_time, onboarding_application_id, " +
                "onboarding_application_create_time, product_id, product_create_time, " +
                "product_market_side, CAST(data as text) as data, status , " +
                " CAST(eval_product_info_status as text) as eval_product_info_status " +
                "FROM product.onboarding_application_lineitem " +
                "where onboarding_application_lineitem_id = :id " +
                " order by create_time desc LIMIT 1";
     MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id", id);


        List<Object[]> rList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return rList;
    }
}
