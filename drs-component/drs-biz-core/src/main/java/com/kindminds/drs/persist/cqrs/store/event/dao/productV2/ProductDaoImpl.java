package com.kindminds.drs.persist.cqrs.store.event.dao.productV2;

import com.kindminds.drs.Country;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;



import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository("manageproduct_productDao")
public class ProductDaoImpl extends Dao implements ProductDao {




    @Override @Transactional("transactionManager")
    public String save(Product product) {

        product.getProductVariations().forEach(x->{
            this.saveVariations(x);
        });


        String sql = "insert into product.product "
                + "( product_id , create_time  , product_base_code , data ,  " +
                " market_side , editing_status , activate  ) values "
                + "  ( :product_id , :create_time , :product_base_code , CAST(:data as json)  , " +
                " :market_side  " +
                " , :editing_status , :activate)";


        MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("product_id", product.getId());

        q.addValue("create_time",  Timestamp.valueOf(product.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("product_base_code", product.getProductBaseCode());

        q.addValue("data", product.getData());

        q.addValue("market_side", product.getMarketside().getKey());
        q.addValue("editing_status", product.getEditingStatus().getKey());
        q.addValue("activate", Integer.toString(BooleanUtils.toInteger(product.isActivated())));


        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        //return onboardingApplication.getData();

        return "";


    }


    @Transactional("transactionManager")
    private void saveVariations(ProductVariation productVariation){

        if(productVariation.getProductMarketingMaterials() != null){
            productVariation.getProductMarketingMaterials().forEach(x->{
                this.saveMarketingMaterial(x);
            });
        }


        String sql = "insert into product.variation "
                + "( variation_id , create_time , variation_code  , data , " +
                "  product_id ,product_create_time , product_market_side ) values "
                + "  ( :variation_id , :create_time , :variation_code , CAST(:data as json) , " +
                " :productId, :product_create_time  , :market_side )";


        MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("variation_id", productVariation.getId());
        q.addValue("create_time",  Timestamp.valueOf(productVariation.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("variation_code", productVariation.getVariationCode());
        q.addValue("data", productVariation.getData());

        q.addValue("productId", productVariation.getProductId());
        q.addValue("product_create_time", Timestamp.valueOf(productVariation.getProductCreatetime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("market_side", productVariation.getMarketside().getKey());


        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);


    }

    @Transactional("transactionManager")
    private void saveMarketingMaterial(ProductMarketingMaterial marketingMaterial){



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


    @Override
    public Optional<String> getId(String baseCode , Country marketSide) {

        String sql = "select product_id from product.product pis "
                + "where pis.product_base_code = :baseCode  and pis.market_side = :marketSide" +
                " order by create_time desc limit 1";
        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("baseCode",baseCode);
        q.addValue("marketSide",marketSide.getKey());

        List<String> rList =null;
        try {
            rList  = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
        }catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }


        return rList != null && rList.size() >0 ?  Optional.of(rList.get(0)) : Optional.empty();
    }

    @Override
    public List<Object[]> get(String id, Country marketSide) {

        String sql = "select product_id , create_time , market_side , product_base_code , " +
                "  CAST(data as text) as data  , activate , " +
                "  editing_status from product.product pis "
                + "where pis.product_id = :id  and pis.market_side = :marketSide" +
                " order by create_time desc LIMIT 1";
        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id",id);
        q.addValue("marketSide",marketSide.getKey());

        List<Object[]> rList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return rList;
    }


    @Override
    public List<Object[]> get(String id) {

        String sql = "  select * from ( " +
                " SELECT product_id, create_time, market_side, " +
                " product_base_code ,  CAST(data as text) as data ,  activate , " +
                "                editing_status ,  " +
                " ROW_NUMBER() OVER (partition BY market_side order by create_time desc)  " +
                "    AS rn " +
                "    FROM product.product  " +
                "    where product_id = :id " +
                "    ) as t where t.rn = 1 " +
                "    order by market_side ";
        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id",id);


        List<Object[]> rList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return rList;
    }

    public void insertMpProductToProductLink(String mpProductId, String marketSide, Integer productId) {
        
    }

}
