package com.kindminds.drs.persist.cqrs.store.read.dao.productV2;

import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.ProductVariationViewDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;



import java.util.List;

@Repository
public class ProductVariationViewDaoImpl  extends Dao implements ProductVariationViewDao {




    @Override
    public List<Object[]> findView(String id) {

        String sql = "select  variation_id, variation_code, product_id," +
                " product_market_side,  " +
                " CAST(data as text) as data ,  create_time, update_time " +
                " from product.variation_view  "
                + "where variation_id = :id  " ;

      MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("id",id);

        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return columnsList;
    }


}
