package com.kindminds.drs.persist.cqrs.store.event.dao.productV2;

import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductMarketingMaterialDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;



import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("manageproductMarketingMaterialDao")
public class ProductMarketingMaterialDaoImpl extends Dao implements ProductMarketingMaterialDao {



    @Override
    public Optional<List<String>> getIds(String productVariationId) {

        String sql = "select marketing_material_id , variation_create_time" +
                " from product.marketing_material  "
                + "where variation_id = :variation_id  " +
                "order by variation_create_time desc ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("variation_id",productVariationId);

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




    @Override
    public List<Object[]> queryById(String id) {


        String sql = "select marketing_material_id, create_time , "
                + "  CAST(data as text) as json_data ,  " +
                " variation_id,  variation_create_time  "
                + "from product.marketing_material  "
                + "where marketing_material_id = :id "
                + " order by create_time desc";

        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", id);

        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return columnsList;

    }






    @Transactional("transactionManager")
    public void insert(ProductMarketingMaterial marketingMaterial){



        String sql = "insert into product.marketing_material "
                + "( marketing_material_id , create_time , data , variation_id ," +
                " variation_create_time  ) values "
                + "  ( :marketing_material_id , :create_time , CAST(:data as json)  , " +
                " :variation_id, :variation_create_time    )";


        MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("marketing_material_id", marketingMaterial.getId());
        q.addValue("create_time",  Timestamp.valueOf(marketingMaterial.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("data", marketingMaterial.getData());

        q.addValue("variation_id", marketingMaterial.getProductVariationId());
        q.addValue("variation_create_time", Timestamp.valueOf(marketingMaterial.getProductVariationCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));



        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);


    }




}
