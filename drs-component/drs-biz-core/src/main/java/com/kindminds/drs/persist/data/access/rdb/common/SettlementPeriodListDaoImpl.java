package com.kindminds.drs.persist.data.access.rdb.common;

import com.kindminds.drs.api.data.access.rdb.common.SettlementPeriodListDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;

@Repository
public class SettlementPeriodListDaoImpl extends Dao implements SettlementPeriodListDao {


    @Override
    @SuppressWarnings("unchecked")
    @Transactional("transactionManager")
    public List<Object []> querySettlementPeriodList() {
        StringBuilder sqlSb = new StringBuilder().append("select    sp.id as id, ").append("sp.period_start as start, ")
                .append("sp.period_end   as end ").append("from settlement_period sp ")
                .append("order by sp.period_start desc ");

        List<Object[]> columnsList = getJdbcTemplate().query(sqlSb.toString(),objArrayMapper);

        return columnsList;
    }

    @Override
    public Date queryLastSettlementEnd() {
        String sql = "select max(period_end) from bill_statement ";

        return getJdbcTemplate().queryForObject(sql,Date.class);
    }

    @Override
    public Boolean isLatestSettlementPeriodSettled() {
        String sql = "select case when exists ( " +
                " select " +
                "  bs.period_end " +
                " from " +
                "  bill_statement bs " +
                " where " +
                "  (select max(period_end) from bill_statement bs) =  " +
                "   (select max(period_end) from settlement_period)) " +
                " then cast(1 as bit) " +
                " else cast(0 as bit) " +
                "end ";

        return getJdbcTemplate().queryForObject(sql,Boolean.class);
    }
}
