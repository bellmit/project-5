package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportBriefLineItem;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSponsoredProductsAdvertisedProductReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonSponsoredProductsAdvertisedProductReportLineItem;
import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo;
import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonSponsoredProductsAdvertisedProductReportBriefLineItemImpl;

@Repository
public class ImportAmazonSponsoredProductsAdvertisedProductReportDaoImpl extends Dao implements ImportAmazonSponsoredProductsAdvertisedProductReportDao{


	
	@Override @Transactional("transactionManager")
	public void clearStagingArea(Marketplace marketplace) {
		String sql = " delete from amazon_campaign_performance_report_staging_area where marketplace_id = :marketplace_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id",marketplace.getKey());
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public int insertRawLinesToStagingArea(Marketplace marketplace, List<AmazonSponsoredProductsAdvertisedProductReportLineItem> items) {
		String sql = "insert into amazon_campaign_performance_report_staging_area "
				+ "(  campaign_name,  ad_group_name,  advertised_sku,  keyword,  match_type,  start_date,  end_date,  clicks,  impressions,  ctr,  total_spend,  average_cpc,  currency,  one_day_orders_placed,  one_day_ordered_product_sales,  one_day_conversion_rate,  one_day_same_sku_units_ordered,  one_day_other_sku_units_ordered,  one_day_same_sku_units_ordered_product_sales,  one_day_other_sku_units_ordered_product_sales,  one_week_orders_placed,  one_week_ordered_product_sales,  one_week_conversion_rate,  one_week_same_sku_units_ordered,  one_week_other_sku_units_ordered,  one_week_same_sku_units_ordered_product_sales,  one_week_other_sku_units_ordered_product_sales,  one_month_orders_placed,  one_month_ordered_product_sales,  one_month_conversion_rate,  one_month_same_sku_units_ordered,  one_month_other_sku_units_ordered,  one_month_same_sku_units_ordered_product_sales,  one_month_other_sku_units_ordered_product_sales,  marketplace_id,  portfolio_name ) values "
				+ "( :campaign_name, :ad_group_name, :advertised_sku, :keyword, :match_type, :start_date, :end_date, :clicks, :impressions, :ctr, :total_spend, :average_cpc, :currency, :one_day_orders_placed, :one_day_ordered_product_sales, :one_day_conversion_rate, :one_day_same_sku_units_ordered, :one_day_other_sku_units_ordered, :one_day_same_sku_units_ordered_product_sales, :one_day_other_sku_units_ordered_product_sales, :one_week_orders_placed, :one_week_ordered_product_sales, :one_week_conversion_rate, :one_week_same_sku_units_ordered, :one_week_other_sku_units_ordered, :one_week_same_sku_units_ordered_product_sales, :one_week_other_sku_units_ordered_product_sales, :one_month_orders_placed, :one_month_ordered_product_sales, :one_month_conversion_rate, :one_month_same_sku_units_ordered, :one_month_other_sku_units_ordered, :one_month_same_sku_units_ordered_product_sales, :one_month_other_sku_units_ordered_product_sales, :marketplace_id, :portfolio_name ) ";
		int insertedRows = 0;
		for(AmazonSponsoredProductsAdvertisedProductReportLineItem item:items){
			MapSqlParameterSource query = new MapSqlParameterSource();

			query.addValue("campaign_name", item.getCampaignName());
			query.addValue("ad_group_name", item.getAdGroupName());
			query.addValue("advertised_sku",item.getAdvertisedSku());
			query.addValue("keyword", null);
			query.addValue("match_type", null);
			query.addValue("start_date", item.getStartDate());
			query.addValue("end_date", item.getEndDate());
			query.addValue("clicks", item.getClicks());
			query.addValue("impressions", item.getImpressions());
			query.addValue("ctr", item.getClickThruRateCtr());
			query.addValue("total_spend", item.getSpend());
			query.addValue("average_cpc", item.getCostPerClickCpc());
			query.addValue("currency", item.getCurrency());
			query.addValue("one_day_orders_placed", new Integer(0));
			query.addValue("one_day_ordered_product_sales", BigDecimal.ZERO);
			query.addValue("one_day_conversion_rate", BigDecimal.ZERO);
			query.addValue("one_day_same_sku_units_ordered", new Integer(0));
			query.addValue("one_day_other_sku_units_ordered", new Integer(0));
			query.addValue("one_day_same_sku_units_ordered_product_sales", BigDecimal.ZERO);
			query.addValue("one_day_other_sku_units_ordered_product_sales", BigDecimal.ZERO);
			query.addValue("one_week_orders_placed", item.getSevenDayTotalOrders());
			query.addValue("one_week_ordered_product_sales", item.getSevenDayTotalSales());
			query.addValue("one_week_conversion_rate", item.getSevenDayConversionRate());
			query.addValue("one_week_same_sku_units_ordered", item.getSevenDayAdvertisedSkuUnits());
			query.addValue("one_week_other_sku_units_ordered", item.getSevenDayOtherSkuUnits());
			query.addValue("one_week_same_sku_units_ordered_product_sales", item.getSevenDayAdvertisedSkuSales());
			query.addValue("one_week_other_sku_units_ordered_product_sales", item.getSevenDayOtherSkuSales());			
			query.addValue("one_month_orders_placed", new Integer(0));
			query.addValue("one_month_ordered_product_sales", BigDecimal.ZERO);
			query.addValue("one_month_conversion_rate", BigDecimal.ZERO);
			query.addValue("one_month_same_sku_units_ordered", new Integer(0));
			query.addValue("one_month_other_sku_units_ordered", new Integer(0));
			query.addValue("one_month_same_sku_units_ordered_product_sales", BigDecimal.ZERO);
			query.addValue("one_month_other_sku_units_ordered_product_sales", BigDecimal.ZERO);
			query.addValue("marketplace_id", marketplace.getKey());
			query.addValue("portfolio_name", item.getPortfolioName());
			System.out.println(sql);
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,query)==1);
			insertedRows++;
		}
		return insertedRows;
	}

	@Override @Transactional("transactionManager")
	public int insertFromStagingAreaByInfo(Marketplace marketplace,AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo info) {
		String sql = "insert into amazon_campaign_performance_report "
				+ "( campaign_name, ad_group_name, advertised_sku,  keyword, match_type, start_date, end_date,  clicks,  impressions,  ctr, total_spend, average_cpc,  currency, one_day_orders_placed, one_day_ordered_product_sales, one_day_conversion_rate, one_day_same_sku_units_ordered, one_day_other_sku_units_ordered, one_day_same_sku_units_ordered_product_sales, one_day_other_sku_units_ordered_product_sales, one_week_orders_placed, one_week_ordered_product_sales, one_week_conversion_rate, one_week_same_sku_units_ordered, one_week_other_sku_units_ordered, one_week_same_sku_units_ordered_product_sales, one_week_other_sku_units_ordered_product_sales, one_month_orders_placed, one_month_ordered_product_sales, one_month_conversion_rate, one_month_same_sku_units_ordered, one_month_other_sku_units_ordered, one_month_same_sku_units_ordered_product_sales, one_month_other_sku_units_ordered_product_sales,  marketplace_id, portfolio_name ) "
				+ "select "
				+ "  campaign_name, ad_group_name, advertised_sku,  keyword, match_type, start_date, end_date,  clicks,  impressions,  ctr, total_spend, average_cpc,  currency, one_day_orders_placed, one_day_ordered_product_sales, one_day_conversion_rate, one_day_same_sku_units_ordered, one_day_other_sku_units_ordered, one_day_same_sku_units_ordered_product_sales, one_day_other_sku_units_ordered_product_sales, one_week_orders_placed, one_week_ordered_product_sales, one_week_conversion_rate, one_week_same_sku_units_ordered, one_week_other_sku_units_ordered, one_week_same_sku_units_ordered_product_sales, one_week_other_sku_units_ordered_product_sales, one_month_orders_placed, one_month_ordered_product_sales, one_month_conversion_rate, one_month_same_sku_units_ordered, one_month_other_sku_units_ordered, one_month_same_sku_units_ordered_product_sales, one_month_other_sku_units_ordered_product_sales, :marketplace_id, portfolio_name "
				+ "from amazon_campaign_performance_report_staging_area acprsa "
				+ "where acprsa.currency = :currency "
				+ "and acprsa.start_date = :startDate "
				+ "and acprsa.end_date = :endDate "
				+ "and acprsa.marketplace_id = :marketplace_id "
				+ "order by acprsa.id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("currency", info.getCurrency());
		q.addValue("startDate", info.getStartDate());
		q.addValue("endDate", info.getEndDate());
		q.addValue("marketplace_id", marketplace.getKey());
		System.out.println(sql);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryDistinctDateCurrencyInfoFromStagingArea(
			Marketplace marketplace) {
		String sql = "select distinct start_date, end_date, currency "
				+ "from amazon_campaign_performance_report_staging_area "
				+ "where marketplace_id = :marketplace_id "
				+ "order by start_date ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());
		List<Object[]> resultColumnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return resultColumnsList;
	}

	@Override
	public boolean isDateCurrencyExist(Marketplace marketplace,
			AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo info) {
		String sql = "select exists( "
				+ "    select 1 from amazon_campaign_performance_report acpr "
				+ "    where acpr.currency = :currency "
				+ "    and acpr.start_date = :startDate "
				+ "    and acpr.end_date = :endDate "
				+ "    and marketplace_id = :marketplace_id "
				+ ")";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("currency", info.getCurrency());
		q.addValue("startDate", info.getStartDate());
		q.addValue("endDate", info.getEndDate());
		q.addValue("marketplace_id", marketplace.getKey());
		Boolean result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		Assert.notNull(result);
		return result;
	}

	@Override @SuppressWarnings("unchecked")
	public List<AmazonSponsoredProductsAdvertisedProductReportBriefLineItem> queryBriefLineItem(int startIndex,
																								int size, Marketplace marketplace, Date start, Date end, String sku) {
		String sql = this.composeQueryBriefLineItemSelectSql()
				+ this.composeQueryBriefLineItemFromSql()
				+ this.composeQueryBriefLineItemWhereSql(marketplace,start,end,sku)
				+ "order by acpr.id, acpr.start_date "
				+ "limit :size offset :start";
		MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q,marketplace, start, end, sku);
		q.addValue("size", size);
		q.addValue("start", startIndex-1);

		return (List) getNamedParameterJdbcTemplate().query(sql,q ,(rs , rowNum) -> new
				AmazonSponsoredProductsAdvertisedProductReportBriefLineItemImpl(
						rs.getInt("id"),rs.getString("campaign_name"),
				rs.getString("ad_group_name"),rs.getString("advertised_sku")
				,rs.getString("keyword"),rs.getString("match_type"),rs.getString("start_date"),
				rs.getString("end_date")
				,rs.getInt("clicks")
		));
	}

	private String composeQueryBriefLineItemSelectSql(){
		return "select                                   acpr.id as id, "
				+ "                           acpr.campaign_name as campaign_name, "
				+ "                           acpr.ad_group_name as ad_group_name, "
				+ "                          acpr.advertised_sku as advertised_sku, "
				+ "                                 acpr.keyword as keyword, "
				+ "                              acpr.match_type as match_type,"
				+ "to_char(acpr.start_date at time zone :tz,:fm) as start_date, "
				+ "to_char(acpr.end_date   at time zone :tz,:fm) as end_date, "
				+ "                                  acpr.clicks as clicks ";
	}
	
	private String composeQueryBriefLineItemFromSql(){
		return "from amazon_campaign_performance_report acpr ";
	}
	
	private String composeQueryBriefLineItemWhereSql(Marketplace marketplace,Date start,Date end,String marketSku){
		String sql = "where true ";
		sql += "and acpr.marketplace_id = :marketplace_id ";
		if(StringUtils.hasText(marketSku)) sql += "and acpr.advertised_sku ilike :sku ";
		if(start!=null) sql += "and acpr.start_date >= :startDate ";
		if(end!=null) sql += "and acpr.end_date <= :endDate ";
		return sql;
	}
	
	private void setQueryParameters(MapSqlParameterSource q,Marketplace marketplace,Date start,Date end,String marketSku){
		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD");
		q.addValue("marketplace_id", marketplace.getKey());
		if(StringUtils.hasText(marketSku)) q.addValue("sku",this.attachDbWildcardToBeginAndEnd(marketSku));
		if(start!=null) q.addValue("startDate",start);
		if(end!=null) q.addValue("endDate",end);
	}
	
	private String attachDbWildcardToBeginAndEnd(String str){ return "%"+str+"%"; }
	
	@Override
	public int queryBriefLineItemCount(Marketplace marketplace, Date start, Date end, String marketSku) {
		String sql = "select count(*) from ( "
				+ this.composeQueryBriefLineItemSelectSql() 
				+ this.composeQueryBriefLineItemFromSql()
				+ this.composeQueryBriefLineItemWhereSql(marketplace,start,end,marketSku)
				+ ") as temp ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q,marketplace,start,end,marketSku);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		return o;
	}
	
}