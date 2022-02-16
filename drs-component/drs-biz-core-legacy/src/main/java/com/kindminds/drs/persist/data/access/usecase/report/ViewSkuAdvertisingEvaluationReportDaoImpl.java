package com.kindminds.drs.persist.data.access.usecase.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.api.data.access.usecase.report.ViewSkuAdvertisingEvaluationReportDao;

@Repository
public class ViewSkuAdvertisingEvaluationReportDaoImpl extends Dao implements ViewSkuAdvertisingEvaluationReportDao {
	

	
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

	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySettlementPeriodList() {
		StringBuilder sqlSb = new StringBuilder()
				.append("select    sp.id as id, ")
				.append("sp.period_start as start, ")
				.append("sp.period_end   as end ")
				.append("from settlement_period sp ")
				.append("order by sp.period_start desc ");

		List<Object[]> columnsList = getJdbcTemplate().query(sqlSb.toString(),objArrayMapper);
		return  columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryLineItems(String supplierKcode, int marketplaceId, int settlementPeriodId){
		StringBuilder sqlSb = new StringBuilder()
				.append("select                    ps.code_by_drs as sku,")
				.append("                      p.name_by_supplier as sku_name, ")
				.append("                    sum(adpstr.sessions) as total_sessions, ")
				.append("                  sum(adpstr.page_views) as total_page_views, ")
				.append(" sum(buy_box_percentage*adpstr.sessions) as total_buy_box_times_sessions, ")
				.append("               sum(adpstr.units_ordered) as total_unit_ordered, ")
				.append("       sum(adpstr.ordered_product_sales) as total_ordered_product_sales ")
				.append("from product_sku ps ")
				.append("inner join product p on p.id = ps.product_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("inner join product_marketplace_info pmi on pmi.product_id = p.id ")
				.append("inner join product_marketplace_info_amazon pmia on pmi.id = pmia.product_marketplace_info_id ")
				.append("inner join amazon_detail_page_sales_traffic_report_by_childItem adpstr on adpstr.child_asin = pmia.asin ")
				.append("where splr.k_code = :supplierKcode ")
				.append("and pmi.marketplace_id = :marketplaceId ")
				.append("and adpstr.marketplace_id = :marketplaceId ")
				.append("and adpstr.settlement_period_id = :settlementPeriodId ")
				.append("group by ps.code_by_drs, p.name_by_supplier ")
				.append("order by ps.code_by_drs ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode",supplierKcode);
		q.addValue("marketplaceId",marketplaceId);
		q.addValue("settlementPeriodId",settlementPeriodId);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);

		return columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySkuInfoFromCampaignPerformanceReport(List<String> skuList,int marketplaceId,Date start,Date end){

		if(skuList.isEmpty()) return new ArrayList<>();
		StringBuilder sqlSb = new StringBuilder()
				.append("select                    ps.code_by_drs as sku, ")
				.append("                        sum(acpr.clicks) as total_clicks, ")
				.append("                   sum(acpr.total_spend) as total_spend, ")
				.append("sum(acpr.one_week_ordered_product_sales) as total_one_week_ordered_product_sales ")
				.append("from amazon_campaign_performance_report acpr ")
				.append("inner join product_marketplace_info pmi on pmi.marketplace_sku = acpr.advertised_sku ")
				.append("inner join product p on p.id = pmi.product_id ")
				.append("inner join product_sku ps on ps.product_id = p.id ")
				.append("where ps.code_by_drs in (:skuList) ")
				.append("and pmi.marketplace_id = :marketplaceId ")
				.append("and acpr.marketplace_id = :marketplaceId ")
				.append("and acpr.start_date >= :start ")
				.append("and acpr.start_date < :end ")
				.append("group by ps.code_by_drs ")
				.append("order by ps.code_by_drs ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuList",skuList);
		q.addValue("marketplaceId",marketplaceId);
		q.addValue("start",start);
		q.addValue("end",end);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);

		return columnsList;
	}

}
