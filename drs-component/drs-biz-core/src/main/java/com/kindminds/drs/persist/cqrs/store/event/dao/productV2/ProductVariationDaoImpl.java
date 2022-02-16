package com.kindminds.drs.persist.cqrs.store.event.dao.productV2;


import com.kindminds.drs.Country;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductVariationDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;



import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("manageproduct_productVariationDao")
public class ProductVariationDaoImpl extends Dao implements ProductVariationDao {




    @Override
    public Optional<List<String>> getIds(String productId , Country marketSide) {

        String sql = "select variation_id , product_create_time from product.variation  "
                + "where product_id = :productId and product_market_side = :marketSide " +
                "order by product_create_time desc ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productId",productId);
        q.addValue("marketSide",marketSide.getKey());

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
    public List<String> findMarketSides(String productBaseCode, String productVariationCode) {


        String sql = "select product_market_side  from product.variation_view " +
                "where product_id in (select product_id from product.product_view  where product_base_code = :bpCode ) " +
                "and variation_code = :vcode and product_market_side > 0 ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("bpCode",productBaseCode);
        q.addValue("vcode",productVariationCode);

        List<Integer> rList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
        List<String> markSides = new ArrayList<String>();
        rList.forEach(r->{
            markSides.add(Country.fromKey(r).name());
        });

        return markSides;
    }

    @Override
    public List<Object[]> get(String productId, OffsetDateTime productCreateTime,
                              Country marketSide) {


        String sql = " select * from ( " +
        " select variation_id, create_time , variation_code,    CAST(data as text) as data , " +
                " product_id , product_create_time  , " +
        "      product_market_side  ," +
        "       ROW_NUMBER() OVER (partition BY variation_code order by create_time desc)" +
        "       AS rn" +
        "       from product.variation " +
        "       where product_id = :productId " +
        "       and    product_market_side = :marketSide " +
        " ) as t where t.rn = 1 " ;


        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("productId",productId);

       // Timestamp pct = Timestamp.valueOf(productCreateTime
         //       .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime());

        //System.out.println(productCreateTime);
        //q.addValue("productCreateTime",pct);

        q.addValue("marketSide",marketSide.getKey());
        List<Object[]> columnsList  = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return columnsList;
    }
}
