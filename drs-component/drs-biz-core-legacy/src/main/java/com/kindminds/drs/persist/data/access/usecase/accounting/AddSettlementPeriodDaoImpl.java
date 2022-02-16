package com.kindminds.drs.persist.data.access.usecase.accounting;





import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.accounting.AddSettlementPeriodDao;


@Repository
public class AddSettlementPeriodDaoImpl extends Dao implements AddSettlementPeriodDao {
	

	
	@Override @Transactional("transactionManager")
	public void insertPeriod(SettlementPeriod period) {
		String sql = "INSERT into settlement_period  " +
				"(  period_start,  period_end, bisettlement )  " +
				" SELECT :period_start, :period_end, NOT bisettlement " +
				" FROM settlement_period  " +
				" ORDER BY id DESC LIMIT 1 ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("period_start", period.getStartPoint());
		q.addValue("period_end", period.getEndPoint());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1);
		return;
	}
	
	@Override
	public boolean selectPeriodExisted(SettlementPeriod period) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select count(*) from  settlement_period ")
				.append("where period_start = :period_start and ")
				.append("period_end = :period_end  ");

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("period_start", period.getStartPoint());
		q.addValue("period_end", period.getEndPoint());
		String o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q , String.class);
		if("1".equals(o)) return true;
		return false;
	}

}
