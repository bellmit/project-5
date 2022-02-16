package com.kindminds.drs.persist.data.access.rdb.accounting;

import java.util.List;





import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class SettlementPeriodDaoImpl extends Dao implements SettlementPeriodDao {

	

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryRecentPeriods(int counts) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select      sp.id as id, ")
				.append("  sp.period_start as start, ")
				.append("  sp.period_end   as end ")
				.append("from settlement_period sp ")
				.append("order by period_start desc ")
				.append("limit :counts offset 0 ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("counts",counts);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);

		return columnsList;

	}

	@Override @SuppressWarnings("unchecked")
	public Object [] queryPeriodById(int periodId) {
		String sql = "select  id, period_start, period_end " +
				" from settlement_period " +
				" WHERE id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",periodId);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql.toString(),q,objArrayMapper);
		return  columnsList.get(0);
	}

}
