package com.kindminds.drs.persist.cqrs.store.read.dao.productV2;


import com.kindminds.drs.api.v2.biz.domain.model.product.MarketingMaterialEditingRequest;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.MarketingMaterialEditingRequestViewDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class MarketingMaterialEditingRequestViewDaoImpl extends Dao implements MarketingMaterialEditingRequestViewDao {





    @Override @Transactional("transactionManager")
    public void insertView(String supplierKcode,
                           MarketingMaterialEditingRequest marketingMaterialEditingRequest ,
                           Object[] productVariationView) {

        String sql = "insert into product.marketing_material_editing_request_view "
                + "( marketing_material_editing_request_id , marketing_material_id  , " +
                " variation_id , product_id ," +
                " product_market_side , supplier_kcode ," +
                " variation_code , status , create_time , update_time ) values "
                + "  ( :id , :marketing_material_id , :variation_id , " +
                " :product_id , :market_side , :supplier_kcode , :variation_code " +
                "  , :status  , :create_time , :update_time )";


       MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("id", marketingMaterialEditingRequest.getId());
        q.addValue("marketing_material_id",
                marketingMaterialEditingRequest.getProductMarketingMaterial().getId());


        q.addValue("variation_id", (String)productVariationView[0]);

        q.addValue("product_id", (String)productVariationView[2]);

        q.addValue("market_side", (Integer)productVariationView[3]);

        q.addValue("supplier_kcode", supplierKcode);
        q.addValue("variation_code", (String)productVariationView[1]);

        q.addValue("status", marketingMaterialEditingRequest.getStatus().getKey());

        q.addValue("create_time",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

       getNamedParameterJdbcTemplate().update(sql,q);


    }

    @Override @Transactional("transactionManager")
    public void updateView(MarketingMaterialEditingRequest marketingMaterialEditingRequest) {


        String sql = "update marketing_material_editing_request_view "
                + " set  status = :status  , update_time = :updateTime "
                + " where marketing_material_editing_request_id = :id " ;


       MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("id", marketingMaterialEditingRequest.getId());
        q.addValue("status", marketingMaterialEditingRequest.getStatus().getKey());

        q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));


        getNamedParameterJdbcTemplate().update(sql,q);

    }

    @Override @Transactional("transactionManager")
    public void deleteVive(String id) {

        String sql = "delete from marketing_material_editing_request_view " +
                " where marketing_material_editing_request_id = :id";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",id);
        getNamedParameterJdbcTemplate().update(sql,q);


    }

    @Override
    public List<Object[]> findView(String id) {

        String sql = "select  marketing_material_editing_request_id, " +
                " marketing_material_id, variation_id, product_id," +
                " product_market_side, supplier_kcode, variation_code, " +
                "status, create_time, update_time " +
                " from product.marketing_material_editing_request_view  "
                + "where marketing_material_editing_request_id = :id "
                + " order by create_time desc";

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", id);

        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return columnsList;
    }


}
