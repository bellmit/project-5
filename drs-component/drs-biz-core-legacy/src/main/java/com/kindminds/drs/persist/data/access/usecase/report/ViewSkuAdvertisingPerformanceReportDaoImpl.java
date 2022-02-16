package com.kindminds.drs.persist.data.access.usecase.report;

import java.util.Date;
import java.util.List;


import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.report.ViewSkuAdvertisingPerformanceReportDao;

@Repository
public class ViewSkuAdvertisingPerformanceReportDaoImpl extends Dao  implements ViewSkuAdvertisingPerformanceReportDao {
	


	@Override
	public String queryLatestSettlementStartUtcDate() {
		String sql = "select to_char( max(period_start) at time zone :tz,:fm ) from settlement_period sp ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD");
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}
	
	@Override
	public String queryLatestSettlementEndUtcDate() {
		String sql = "select to_char( (max(period_end)-interval '1 day' ) at time zone :tz,:fm ) from settlement_period sp ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD");
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryCampaignNames(String supplierKcode,Marketplace marketplace, Date start, Date end) {
		String sql = "select distinct acpr.campaign_name "
				+ "from amazon_campaign_performance_report acpr "
				+ "inner join product_marketplace_info pmi on pmi.marketplace_sku = acpr.advertised_sku "
				+ this.composeQueryLineItemWhereSql(start, end);
		MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q, supplierKcode, marketplace, start, end);
		return getNamedParameterJdbcTemplate().queryForList(sql,q, String.class);
	}
	
	private String composeQueryLineItemWhereSql(Date start,Date end){
		String sql = "where UPPER(acpr.campaign_name) like concat('[',:supplierKcode,'%') "
				+ "and pmi.marketplace_id = :marketplaceId "
				+ "and acpr.marketplace_id = :marketplaceId "
				+ "and acpr.currency = :currencyName ";
		if(start!=null) sql += "and acpr.start_date >= :startDate ";
		if(end!=null) sql += "and acpr.start_date < :endDate ";
		return sql;
	}
	
	private void setQueryParameters(MapSqlParameterSource q,String supplierKcode,Marketplace marketplace,Date start,Date end){
		q.addValue("supplierKcode",supplierKcode);
		q.addValue("marketplaceId",marketplace.getKey());
		q.addValue("currencyName",marketplace.getCurrency().name());
		if(start!=null) q.addValue("startDate",start);
		if(end!=null) q.addValue("endDate",end);
	}
}
