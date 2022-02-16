package com.kindminds.drs.persist.cqrs.store.event.dao.productV2;

import com.kindminds.drs.api.v2.biz.domain.model.product.MarketingMaterialEditingRequest;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.MarketingMaterialEditingRequestDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository
public class MarketingMaterialEditingRequestDaoImpl extends Dao
        implements MarketingMaterialEditingRequestDao {




    @Override
    public List<Object[]> find(String productMarketingMaterialId) {

        String sql ="SELECT marketing_material_editing_request_id, create_time, " +
                " marketing_material_id, marketing_material_create_time, " +
                "  CAST(data as text) as data , status " +
                " FROM product.marketing_material_editing_request " +
                " where  marketing_material_id = :pmmId" +
                " order by create_time desc ";

        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("pmmId",productMarketingMaterialId);

        List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return columnsList;
    }

    @Override
    public Optional<String> findId(String productMarketingMaterialId) {

        String sql = "select marketing_material_editing_request_id " +
                " from product.marketing_material_editing_request "
                + " where marketing_material_id = :pmmId  " +
                " order by create_time desc ";
        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("pmmId",productMarketingMaterialId);

        String rList = null;
        try{
           rList  = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
        } catch (EmptyResultDataAccessException e) {
        }

        return rList != null ? Optional.of(rList) : Optional.empty();
    }

    @Override @Transactional("transactionManager")
    public void insert(MarketingMaterialEditingRequest request) {

        String sql = "insert into product.marketing_material_editing_request "
                + "( marketing_material_editing_request_id , create_time  , " +
                "marketing_material_id ," +
                " marketing_material_create_time ,  " +
                " data , status ) values "
                + "  ( :id , :createTime , :pmId , :pmCreate_time , " +
                "  CAST(:data as json) , :status )";


        MapSqlParameterSource q = new MapSqlParameterSource();


        q.addValue("id", request.getId());
        q.addValue("createTime",  Timestamp.valueOf(request.getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("pmId", request.getProductMarketingMaterial().getId());
        q.addValue("pmCreate_time", Timestamp.valueOf(request.getProductMarketingMaterial().getCreateTime()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        q.addValue("data", request.getProductMarketingMaterial().getData());
        q.addValue("status", request.getStatus().getKey());

        getNamedParameterJdbcTemplate().update(sql,q);

    }




}
