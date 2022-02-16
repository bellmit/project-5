package com.kindminds.drs.persist.data.access.rdb.accounting;

import java.util.Date;


import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodHelperDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;



@Repository
public class SettlementPeriodHelperDaoImpl extends Dao implements SettlementPeriodHelperDao {
	

	@Override
	public Date queryPeriodStart(int settlementPeriodId) {
		String sql = "select period_start from settlement_period where id = :settlementPeriodId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementPeriodId", settlementPeriodId);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override
	public Date queryPeriodEnd(int settlementPeriodId) {
		String sql = "select period_end from settlement_period where id = :settlementPeriodId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementPeriodId", settlementPeriodId);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

}
