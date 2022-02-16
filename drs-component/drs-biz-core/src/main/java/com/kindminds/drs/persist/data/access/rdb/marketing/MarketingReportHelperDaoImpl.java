package com.kindminds.drs.persist.data.access.rdb.marketing;

import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.rdb.report.MarketingReportHelperDao;

@Repository
public class MarketingReportHelperDaoImpl extends Dao implements MarketingReportHelperDao {
	


	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySkuAdvertisingPerformanceReportLineItems(String supplierKcode, Marketplace marketplace, Date start, Date end, String campaignName) {
		String sql = "select                  acpr.currency as currency, "
				+ "                          ps.code_by_drs as sku, "
				+ "                   sum(acpr.impressions) as total_impressions, "
				+ "                        sum(acpr.clicks) as total_clicks, "
				+ "                   sum(acpr.total_spend) as total_spend, "
				+ "        sum(acpr.one_week_orders_placed) as total_one_week_orders_placed, "
				+ "sum(acpr.one_week_ordered_product_sales) as total_one_week_ordered_product_sales "
				+ "from amazon_campaign_performance_report acpr "
				+ "inner join product_marketplace_info pmi on pmi.marketplace_sku = acpr.advertised_sku "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "where UPPER(acpr.campaign_name) like concat('[',:supplierKcode,'%') "
				+ "and pmi.marketplace_id = :marketplaceId "
				+ "and acpr.marketplace_id = :marketplaceId "
				+ "and acpr.currency = :currencyName "
				+ this.composeNullableCondition(start, end, campaignName)
				+ "group by ps.id, acpr.currency, ps.code_by_drs "
				+ "order by ps.code_by_drs ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode",supplierKcode);
		q.addValue("marketplaceId",marketplace.getKey());
		q.addValue("currencyName",marketplace.getCurrency().name());
		this.setNullableParameters(q,start,end,campaignName);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}
	
	private String composeNullableCondition(Date start,Date end,String campaignName){
		String sql = "";
		if(start!=null) sql += "and acpr.start_date >= :startDate ";
		if(end!=null) sql += "and acpr.start_date < :endDate ";
		if(campaignName!=null) sql += "and acpr.campaign_name = :campaignName ";
		return sql;
	}
	
	private void setNullableParameters(MapSqlParameterSource q,Date start,Date end,String campaignName){
		if(start!=null) q.addValue("startDate",start);
		if(end!=null) q.addValue("endDate",end);
		if(campaignName!=null) q.addValue("campaignName",campaignName);
	}
}
