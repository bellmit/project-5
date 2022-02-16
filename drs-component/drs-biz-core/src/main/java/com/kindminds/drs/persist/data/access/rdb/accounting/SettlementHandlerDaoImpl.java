package com.kindminds.drs.persist.data.access.rdb.accounting;

import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementHandlerDao;

@Repository
public class SettlementHandlerDaoImpl extends Dao implements SettlementHandlerDao {
	

	
	@Override @SuppressWarnings("unchecked")
	public List<Integer> queryAmazonSettlementReportReadyMarketplaceIds(int periodId){
		StringBuilder sqlSb = new StringBuilder()
				.append("select ai.source_marketplace_id ")
				.append("from amazon_settlement_report_v2_info ai ")
				.append("inner join settlement_period sp on ( true ")
				.append("    and ai.settlement_start_date >= sp.period_start ")
				.append("    and ai.settlement_start_date <  sp.period_end ")
				.append("    and ai.settlement_end_date   >= sp.period_end ")
				.append(") where sp.id = :periodId and is_imported = true ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("periodId", periodId);
		return getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,Integer.class);
	}

}
