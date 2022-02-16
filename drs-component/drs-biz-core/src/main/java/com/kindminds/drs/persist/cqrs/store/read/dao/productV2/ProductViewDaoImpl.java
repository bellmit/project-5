package com.kindminds.drs.persist.cqrs.store.read.dao.productV2;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.persist.data.access.rdb.Dao;




import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.ProductViewDao;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class ProductViewDaoImpl extends Dao implements ProductViewDao {



    @Override @Transactional("transactionManager")
    public String saveView(Product product) {

        product.getProductVariations().forEach(x->{
            this.saveVariationsView(x , product , product.getEditingStatus());
        });



        String qSql = "select product_id "
                + "from product.product_view  "
                + "where product_id =:id  "
                + "and market_side = :market_side ";

       MapSqlParameterSource q1 = new MapSqlParameterSource();

        q1.addValue("id", product.getId());
        q1.addValue("market_side",product.getMarketside().getKey());
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(qSql,q1,objArrayMapper);

        if(columnsList.size()>0){

            String sql = "update product.product_view  set " +
                    " product_base_code = :product_base_code , editing_status = :editing_status " +
                    " , data =  CAST(:data as json) , activate = :activate   " +
                    ", update_time =   :update_time "
                    + " where product_id=:product_id and  market_side = :market_side ";



            MapSqlParameterSource q = new MapSqlParameterSource();

            q.addValue("product_id", product.getId());
            q.addValue("market_side",product.getMarketside().getKey());

            q.addValue("product_base_code", product.getProductBaseCode());
            q.addValue("editing_status", product.getEditingStatus().getKey());
            q.addValue("activate", Integer.toString(BooleanUtils.toInteger(product.isActivated())));

            q.addValue("data", product.getData());

            q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));


           getNamedParameterJdbcTemplate().update(sql,q);


        }else{
            String sql = "insert into product.product_view "
                    + "( product_id ,market_side  , supplier_company_id,  " +
                    " product_base_code, editing_status , data , activate  " +
                    ", create_time , update_time ) select "
                    + "  :product_id,:market_side , splr.id, " +
                    " :product_base_code  , :editing_status ,  CAST(:data as json) , " +
                    " :activate ,:create_time , :update_time  "
                    + " from company splr where splr.k_code = :kcode ";



            MapSqlParameterSource q = new MapSqlParameterSource();

            q.addValue("product_id", product.getId());
            q.addValue("market_side",product.getMarketside().getKey());

            q.addValue("kcode", product.getSupplierKcode());
            q.addValue("product_base_code", product.getProductBaseCode());
            q.addValue("editing_status", product.getEditingStatus().getKey());
            q.addValue("activate", Integer.toString(BooleanUtils.toInteger(product.isActivated())));


            q.addValue("data", product.getData());


            q.addValue("create_time",  Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));


            q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));



            getNamedParameterJdbcTemplate().update(sql,q);
        }



        return "";



    }



    @Override
    @Transactional("transactionManager")
    public String deleteView(String productId) {

        String sql = "delete from product.marketing_material_view  where product_id = :id";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",productId);
        getNamedParameterJdbcTemplate().update(sql,q);

        String sql2 = "delete from product.variation_view  where product_id = :id";
        q = new MapSqlParameterSource();
        q.addValue("id",productId);
        getNamedParameterJdbcTemplate().update(sql,q);

        String sql3 = "delete from product.product_view  where product_id = :id";
        q = new MapSqlParameterSource();
        q.addValue("id",productId);
        getNamedParameterJdbcTemplate().update(sql,q);


        return "";
    }

    @Override
    @Transactional("transactionManager")
    public String deleteView(String productId , Country marketSide) {

        String sql = "delete from product.marketing_material_view  " +
                " where product_id = :id and product_market_side = :marketSide ";
     MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",productId);
        q.addValue("marketSide",marketSide.getKey());
        getNamedParameterJdbcTemplate().update(sql,q);

        String sql2 = "delete from product.variation_view  " +
                " where product_id = :id and product_market_side = :marketSide";
        q = new MapSqlParameterSource();
        q.addValue("id",productId);
        q.addValue("marketSide",marketSide.getKey());
        getNamedParameterJdbcTemplate().update(sql,q);

        String sql3 = "delete from product.product_view  " +
                " where product_id = :id and market_side = :marketSide";
        q = new MapSqlParameterSource();
        q.addValue("id",productId);
        q.addValue("marketSide",marketSide.getKey());
        getNamedParameterJdbcTemplate().update(sql,q);


        return "";
    }

    @Transactional("transactionManager")
    private void saveVariationsView(ProductVariation productVariation , Product p ,
                                    ProductEditingStatusType status){

        if(productVariation.getProductMarketingMaterials() != null){
            productVariation.getProductMarketingMaterials().forEach(x->{
                this.saveMarketingMaterialView(x ,p ,productVariation,status);
            });

        }

        String qSql = "select variation_id "
                + "from product.variation_view  "
                + "where variation_id =:id  "
                + "and product_market_side = :market_side ";

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", productVariation.getId());
        q.addValue("market_side",p.getMarketside().getKey());
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(qSql,q,objArrayMapper);

        if(columnsList.size()>0){


            String sql = "update product.variation_view  set " +
                    "  variation_code = :variation_code  , data = CAST(:data as json)  , " +
                    "  update_time = :update_time where  " +
                   " variation_id=:variation_id and product_market_side = :market_side";


          q = new MapSqlParameterSource();

            q.addValue("variation_id", productVariation.getId());
            q.addValue("market_side", productVariation.getMarketside().getKey());
            q.addValue("variation_code", productVariation.getVariationCode());

            q.addValue("data", productVariation.getData());

            q.addValue("update_time", Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

            getNamedParameterJdbcTemplate().update(sql,q);

        }else{
            String sql = "insert into product.variation_view "
                    + "( variation_id , product_id ,  product_market_side , variation_code , data , " +
                    "   create_time , update_time) values "
                    + "  ( :variation_id , :productId , :market_side ,:variation_code  , CAST(:data as json) , " +
                    " :create_time , :update_time )";


          q = new MapSqlParameterSource();


            q.addValue("variation_id", productVariation.getId());
            q.addValue("productId", productVariation.getProductId());
            q.addValue("market_side", productVariation.getMarketside().getKey());
            q.addValue("variation_code", productVariation.getVariationCode());

            q.addValue("data", productVariation.getData());

            q.addValue("create_time",  Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

            q.addValue("update_time", Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));
            getNamedParameterJdbcTemplate().update(sql,q);
        }


    }

    @Transactional("transactionManager")
    private void saveMarketingMaterialView(ProductMarketingMaterial marketingMaterial ,
                                           Product p , ProductVariation productVariation , ProductEditingStatusType status){


        String qSql = "select marketing_material_id "
                + "from product.marketing_material_view  "
                + "where product_base_code =:bpcode and variation_code = :code "
                + "and product_market_side = :market_side ";

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("bpcode",p.getProductBaseCode());
        q.addValue("code",productVariation.getVariationCode());
        q.addValue("market_side",p.getMarketside().getKey());
        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(qSql,q,objArrayMapper);
        if(columnsList.size()>0){

            String sql = "update product.marketing_material_view  set " +
                    "  status = :status  , data = CAST(:data as json)  , " +
                    "  update_time = :update_time where  " +
                    "  marketing_material_id=:marketing_material_id and " +
                    " product_market_side = :product_market_side";


          q = new MapSqlParameterSource();


            q.addValue("marketing_material_id", marketingMaterial.getId());
            q.addValue("product_market_side", p.getMarketside().getKey());
            q.addValue("status", status.getKey());

            q.addValue("data", marketingMaterial.getData());

            q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

            getNamedParameterJdbcTemplate().update(sql,q);



        }else{
            String sql = "insert into product.marketing_material_view "
                    + "( marketing_material_id ,  variation_id , product_id , " +
                    " product_market_side ," +
                    " supplier_kcode , product_base_code ,variation_code ," +
                    " status  ,data , create_time , update_time ) values "
                    + "  ( :marketing_material_id ,:variation_id , :product_id ," +
                    " :product_market_side , :supplier_kcode , :product_base_code ,:variation_code , :status" +
                    ", CAST(:data as json) , " +
                    " :create_time , :update_time   )";


          q = new MapSqlParameterSource();


            q.addValue("marketing_material_id", marketingMaterial.getId());
            q.addValue("variation_id", marketingMaterial.getProductVariationId());
            q.addValue("product_id", p.getId());

            q.addValue("product_market_side", p.getMarketside().getKey());

            q.addValue("supplier_kcode", p.getSupplierKcode());
            q.addValue("product_base_code", p.getProductBaseCode());
            q.addValue("variation_code", productVariation.getVariationCode());
            q.addValue("status", status.getKey());

            q.addValue("data", marketingMaterial.getData());

            q.addValue("create_time",  Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

            q.addValue("update_time",  Timestamp.valueOf(OffsetDateTime.now()
                    .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

            getNamedParameterJdbcTemplate().update(sql,q);
        }

    }





}
