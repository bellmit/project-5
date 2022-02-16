package com.kindminds.drs.persist.cqrs.store.read.dao.productV2;

import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.ProductMarketingMaterialViewDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;


@Repository
public class ProductMarketingMaterialViewDaoImpl extends Dao implements ProductMarketingMaterialViewDao {






    @Override
    public List<Object[]> findView(String id) {


        String sql = "select marketing_material_id," +
                " variation_id, product_id, product_market_side, " +
                "product_base_code, variation_code, supplier_kcode," +
                " status, CAST(data as text) as data , create_time, update_time " +
                " from product.marketing_material_view  "
                + "where marketing_material_id = :id "
                + " order by create_time desc";

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", id);

        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return columnsList;

    }


    @Override @Transactional("transactionManager")
    public void insertView(String supplierKCode,String productBaseCode,
                           ProductMarketingMaterial productMarketingMaterial,
                           Object[] productVariationView , ProductEditingStatusType status) {

        String sql = "insert into product.marketing_material_view "
                + "( marketing_material_id  , " +
                " variation_id , product_id " +
                " product_market_side , product_base_code , " +
                " variation_code , supplier_kcode, " +
                " status , data , create_time , update_time ) values "
                + "  ( :marketing_material_id , :variation_id , " +
                " :product_id , :product_market_side , :product_base_code , " +
                ":variation_code ,  :supplier_kcode " +
                "  , :status  , CAST(:data as json) , :create_time , :update_time )";


       MapSqlParameterSource q = new MapSqlParameterSource();



        q.addValue("marketing_material_id",
                productMarketingMaterial.getId());


        q.addValue("variation_id", (String)productVariationView[0]);

        q.addValue("product_id", (String)productVariationView[2]);

        q.addValue("product_market_side", (Integer)productVariationView[3]);

        q.addValue("product_base_code", productBaseCode);
        q.addValue("variation_code", (String)productVariationView[1]);
        q.addValue("supplier_kcode", supplierKCode);

        q.addValue("status", status.getKey());
        q.addValue("data",productMarketingMaterial.getData() );

        q.addValue("createTime",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

       getNamedParameterJdbcTemplate().update(sql,q);

    }



    @Override @Transactional("transactionManager")
    public void updateView(ProductMarketingMaterial productMarketingMaterial ,  ProductEditingStatusType status) {

        String sql = "update product.marketing_material_view "
                + " set data = CAST(:data as json)  , status = :status  , update_time = :updateTime "
                + " where marketing_material_id = :id " ;


       MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("id", productMarketingMaterial.getId());
        q.addValue("data", productMarketingMaterial.getData());
        q.addValue("status", status.getKey());

        q.addValue("updateTime",  Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));


        getNamedParameterJdbcTemplate().update(sql,q);


    }

    @Override @Transactional("transactionManager")
    public void deleteView(String id) {

    }

}
