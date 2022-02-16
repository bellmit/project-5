package com.kindminds.drs.persist.es.dao.sales.service;

import com.kindminds.drs.api.v2.biz.domain.model.sales.service.Fee;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import com.kindminds.drs.api.data.es.dao.sales.service.FeeDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;






@Repository
public class FeeDaoImpl extends Dao implements FeeDao {



    @Override
    @Transactional("transactionManager")
    public void insert(Fee fee) {
        String sql = "INSERT INTO sales.fee( \n" +
                "\t fee_id, create_time, supplier_company_id, type, amount, activated) \n" +
                "\t VALUES ( \n" +
                "\t :fee_id, :create_time, :supplier_company_id, :type, :amount, :activated) ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("fee_id", fee.getId());
        q.addValue("create_time", fee.getCreateTime());
        q.addValue("supplier_company_id", fee.getSupplierCompanyId());
        q.addValue("type", fee.getType());
        q.addValue("amount", fee.getAmount());
        q.addValue("activated", fee.isActivated());

        getNamedParameterJdbcTemplate().update(sql,q);


    }
}
