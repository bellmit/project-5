package com.kindminds.drs.persist.data.access.rdb.product;

import com.kindminds.drs.api.data.access.rdb.product.LegacyProductDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




import java.util.List;
import java.util.Optional;

@Repository
public class LegacyProductDaoImpl extends Dao implements LegacyProductDao {



    @Override @Transactional
    public void insertProductRef(String mpProductId, int mpMarketSide, String productBaseCode) {


        String sql = "INSERT INTO product.product_settlement_product " +
                "(product_id, market_side , settlement_product_id) " +
                "VALUES(:product_id, :market_side , " +
                " (select pb.product_id from product_base pb where pb.code_by_drs = :product_code)); ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("product_id", mpProductId);
        q.addValue("product_code", productBaseCode);
        q.addValue("market_side", mpMarketSide);
      getNamedParameterJdbcTemplate().update(sql,q);


    }

    @Override @Transactional
    public void insertProductRef(String mpProductId, int mpMarketSide, int productId) {


        String sql = "INSERT INTO product.product_settlement_product " +
                "(product_id, market_side , settlement_product_id) " +
                " VALUES (:product_id, :market_side , :sproductId ); ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("product_id", mpProductId);
        q.addValue("market_side", mpMarketSide);
        q.addValue("sproductId", productId);

        getNamedParameterJdbcTemplate().update(sql,q);

    }

    @Override @Transactional
    public void insertDraftProductRef(String mpProductId, int mpMarketSide, String productBaseCode) {
        System.out.println("draft");
        String sql = "INSERT INTO product.product_draft_product_info_source " +
                "(product_id, market_side , draft_product_info_source_id ) " +
                " VALUES( :product_id, :market_side , " +
                " (select pb.id from draft_product_info_source pb where pb.key = :product_code) ); ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("product_id", mpProductId);
        q.addValue("product_code", productBaseCode);
        q.addValue("market_side", mpMarketSide);
        getNamedParameterJdbcTemplate().update(sql,q);
    }

    @Override
    public Optional<Integer> getSettlementProductId(String productBaseCode) {


        String sql = " select pb.product_id from product_base pb where pb.code_by_drs = :product_code  ";
        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("product_code", productBaseCode);

        List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);


        return resultList.size() > 0 ? Optional.of(resultList.get(0)) : Optional.empty();
    }





}
