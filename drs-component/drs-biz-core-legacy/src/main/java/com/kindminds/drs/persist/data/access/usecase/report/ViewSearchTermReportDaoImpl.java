package com.kindminds.drs.persist.data.access.usecase.report;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;


import com.kindminds.drs.api.data.access.usecase.report.ViewSearchTermReportDao;
import com.kindminds.drs.api.v1.model.report.SearchTermReport;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.Marketplace;


import com.kindminds.drs.persist.v1.model.mapping.report.SearchTermReportLineItemImpl;

@Repository
public class ViewSearchTermReportDaoImpl extends Dao implements ViewSearchTermReportDao {
	


	@Override @SuppressWarnings("unchecked")
	public List<SearchTermReport.SearchTermReportLineItem> queryLineItems(Marketplace marketplace, String supplierKcode, String campaignName) {

		String sql = "select     astr.id as id, " +
				" astr.campaign_name as campaign_name,  " +
				" astr.ad_group_name as ad_group_name,  " +
				" astr.customer_search_term as customer_search_term,  " +
				" astr.keyword as keyword, " +
				" astr.match_type as match_type, " +
				" to_char(astr.first_day_of_impression at time zone :tz, :fm) as first_day_of_impression, " +
				" to_char( astr.last_day_of_impression at time zone :tz, :fm) as last_day_of_impression, " +
				" astr.impressions as impressions,  " +
				" astr.clicks as clicks,  " +
				" astr.ctr as ctr,  " +
				" astr.total_spend as total_spend,  " +
				" astr.average_cpc as average_cpc, " +
				" astr.acos as acos, " +
				" astr.currency as currency, " +
				" astr.orders_placed_within_one_week_of_a_click as orders_placed_within_one_week_of_a_click, " +
				" astr.product_sales_within_one_week_of_a_click as product_sales_within_one_week_of_a_click, " +
				" astr.conversion_rate_within_one_week_of_a_click as conversion_rate_within_one_week_of_a_click, " +
				" astr.same_sku_units_ordered_within_one_week_of_click as same_sku_units_ordered_within_one_week_of_click, " +
				" astr.other_sku_units_ordered_within_one_week_of_click as other_sku_units_ordered_within_one_week_of_click, " +
				" astr.same_sku_units_product_sales_within_one_week_of_click as same_sku_units_product_sales_within_one_week_of_click, " +
				" astr.other_sku_units_product_sales_within_one_week_of_click as other_sku_units_product_sales_within_one_week_of_click " +
				" from amazon_search_term_report astr " +
				" inner join amazon_search_term_report_campaign_name_supplier_map astrcnmp on (UPPER(astrcnmp.campaign_name) = UPPER(astr.campaign_name) and astr.marketplace_id = astrcnmp.marketplace_id ) " +
				" inner join company splr on splr.id = astrcnmp.supplier_company_id " +
				" where splr.k_code = :kcode and astr.marketplace_id = :marketplace_id " +
				" and astr.campaign_name = :campaignName and astr.first_day_of_impression >= :monthStart" +
				" order by campaign_name, ad_group_name, customer_search_term, keyword ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD");
		q.addValue("kcode", supplierKcode);
		q.addValue("marketplace_id", marketplace.getKey());
		q.addValue("campaignName", campaignName);

		OffsetDateTime monthStart = OffsetDateTime.now().minusDays(2).withOffsetSameLocal(ZoneOffset.UTC)
				.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		q.addValue("monthStart", monthStart);

		
		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new SearchTermReportLineItemImpl(
				rs.getInt("id"),rs.getString("campaign_name")
				,rs.getString("ad_group_name"),rs.getString("customer_search_term"),
				rs.getString("keyword")
				,rs.getString("match_type"),rs.getString("first_day_of_impression"),
				rs.getString("last_day_of_impression")
				,rs.getInt("impressions"),rs.getInt("clicks"),rs.getBigDecimal("ctr"),
				rs.getBigDecimal("total_spend")
				,rs.getBigDecimal("average_cpc"),rs.getBigDecimal("acos"),
				rs.getString("currency")
				,rs.getInt("orders_placed_within_one_week_of_a_click"),
				rs.getBigDecimal("product_sales_within_one_week_of_a_click"),
				rs.getBigDecimal("conversion_rate_within_one_week_of_a_click"),
				rs.getInt("same_sku_units_ordered_within_one_week_of_click")
				,rs.getInt("other_sku_units_ordered_within_one_week_of_click"),
				rs.getBigDecimal("same_sku_units_product_sales_within_one_week_of_click"),
				rs.getBigDecimal("other_sku_units_product_sales_within_one_week_of_click")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryCampaignNameList(Marketplace marketplace,String supplierKcode){
		StringBuilder sqlSb = new StringBuilder()
				.append("select distinct astr.campaign_name ")
				.append("from amazon_search_term_report astr ")
				.append("inner join amazon_search_term_report_campaign_name_supplier_map astrcnmp on (UPPER(astrcnmp.campaign_name) = UPPER(astr.campaign_name) and astr.marketplace_id = astrcnmp.marketplace_id ) ")
				.append("inner join company splr on splr.id = astrcnmp.supplier_company_id ")
				.append("where splr.k_code = :kcode and astr.marketplace_id = :marketplace_id order by astr.campaign_name asc ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", supplierKcode);
		q.addValue("marketplace_id", marketplace.getKey());
		return getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
	}

}
