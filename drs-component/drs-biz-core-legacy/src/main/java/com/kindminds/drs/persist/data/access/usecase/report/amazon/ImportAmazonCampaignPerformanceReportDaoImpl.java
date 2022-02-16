package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;




import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.report.AmazonCampaignPerformanceReportBriefLineItem;


import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonCampaignPerformanceReportDao;
import com.kindminds.drs.api.v1.model.accounting.AmazonCampaignPerformanceReportRawLine;
import com.kindminds.drs.api.v1.model.report.AmazonCampaignPerformanceReportDateCurrencyInfo;
import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonCampaignPerformanceReportBriefLineItemImpl;

@Repository
public class ImportAmazonCampaignPerformanceReportDaoImpl extends Dao implements ImportAmazonCampaignPerformanceReportDao {
	

	
	@Override @Transactional("transactionManager")
	public void clearStagingArea(Marketplace marketplace) {
		String sql = " delete from amazon_campaign_performance_report_staging_area where marketplace_id = :marketplace_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id",marketplace.getKey());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void insertRawLinesToStagingArea(Marketplace marketplace,List<AmazonCampaignPerformanceReportRawLine> lineList) {
		String insertSql = "insert into amazon_campaign_performance_report_staging_area "
				+ "( campaign_name, ad_group_name, advertised_sku,  keyword, match_type, start_date, end_date,  clicks,  impressions,  ctr, total_spend, average_cpc,  currency, one_day_orders_placed, one_day_ordered_product_sales, one_day_conversion_rate, one_day_same_sku_units_ordered, one_day_other_sku_units_ordered, one_day_same_sku_units_ordered_product_sales, one_day_other_sku_units_ordered_product_sales, one_week_orders_placed, one_week_ordered_product_sales, one_week_conversion_rate, one_week_same_sku_units_ordered, one_week_other_sku_units_ordered, one_week_same_sku_units_ordered_product_sales, one_week_other_sku_units_ordered_product_sales, one_month_orders_placed, one_month_ordered_product_sales, one_month_conversion_rate, one_month_same_sku_units_ordered, one_month_other_sku_units_ordered, one_month_same_sku_units_ordered_product_sales, one_month_other_sku_units_ordered_product_sales,  marketplace_id ) values "
				+ "( ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?, ?,?, ?,?, ?, ?,?, ?,?,?, ?,?, ?, ?, ?, ?,? ) ";


		int[][] updateCounts = this.getJdbcTemplate().batchUpdate(insertSql, lineList, 20 ,
				new ParameterizedPreparedStatementSetter<AmazonCampaignPerformanceReportRawLine>() {
					@Override
					public void setValues(PreparedStatement pstmt, AmazonCampaignPerformanceReportRawLine line) throws SQLException {
						pstmt.setString(1,line.getCampaignName());
						pstmt.setString(2,line.getAdGroupName());
						pstmt.setString(3,line.getAdvertisedSku());
						pstmt.setString(4,line.getKeyword());
						pstmt.setString(5,line.getMatchType());
						pstmt.setTimestamp(6,new java.sql.Timestamp(line.getStartDate().getTime()));
						pstmt.setTimestamp(7,new java.sql.Timestamp(line.getEndDate().getTime()));
						pstmt.setInt(8,line.getClicks());
						pstmt.setInt(9,line.getImpressions());
						pstmt.setBigDecimal(10,line.getCtr());
						pstmt.setBigDecimal(11,line.getTotalSpend());
						pstmt.setBigDecimal(12,line.getAverageCpc());
						pstmt.setString(13,line.getCurrency());
						pstmt.setInt(14,line.getOneDayOrdersPlaced());
						pstmt.setBigDecimal(15,line.getOneDayOrderedProductSales());
						pstmt.setBigDecimal(16,line.getOneDayConversionRate());
						pstmt.setInt(17,line.getOneDaySameSkuUnitsOrdered());
						pstmt.setInt(18,line.getOneDayOtherSkuUnitsOrdered());
						pstmt.setBigDecimal(19,line.getOneDaySameSkuUnitsOrderedProductSales());
						pstmt.setBigDecimal(20,line.getOneDayOtherSkuUnitsOrderedProductSales());
						pstmt.setInt(21,line.getOneWeekOrdersPlaced());
						pstmt.setBigDecimal(22,line.getOneWeekOrderedProductSales());
						pstmt.setBigDecimal(23,line.getOneWeekConversionRate());
						pstmt.setInt(24,line.getOneWeekSameSkuUnitsOrdered());
						pstmt.setInt(25,line.getOneWeekOtherSkuUnitsOrdered());
						pstmt.setBigDecimal(26,line.getOneWeekSameSkuUnitsOrderedProductSales());
						pstmt.setBigDecimal(27,line.getOneWeekOtherSkuUnitsOrderedProductSales());
						pstmt.setInt(28,line.getOneMonthOrdersPlaced());
						pstmt.setBigDecimal(29,line.getOneMonthOrderedProductSales());
						pstmt.setBigDecimal(30,line.getOneMonthConversionRate());
						pstmt.setInt(31,line.getOneMonthSameSkuUnitsOrdered());
						pstmt.setInt(32,line.getOneMonthOtherSkuUnitsOrdered());
						pstmt.setBigDecimal(33,line.getOneMonthSameSkuUnitsOrderedProductSales());
						pstmt.setBigDecimal(34,line.getOneMonthOtherSkuUnitsOrderedProductSales());
						pstmt.setInt(35,marketplace.getKey());

					}

				});

	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryDistinctDateCurrencyInfoFromStagingArea(Marketplace marketplace) {
		String sql = "select distinct start_date, end_date, currency "
				+ "from amazon_campaign_performance_report_staging_area "
				+ "where marketplace_id = :marketplace_id "
				+ "order by start_date ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());
		List<Object[]> resultColumnsList = getNamedParameterJdbcTemplate().query(sql,q , objArrayMapper);
		return resultColumnsList;

	}

	@Override
	public boolean isDateCurrencyExist(Marketplace marketplace,AmazonCampaignPerformanceReportDateCurrencyInfo info) {
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

	@Override @Transactional("transactionManager")
	public int insertFromStagingAreaByInfo(Marketplace marketplace,AmazonCampaignPerformanceReportDateCurrencyInfo info) {
		String sql = "insert into amazon_campaign_performance_report "
				+ "( campaign_name, ad_group_name, advertised_sku,  keyword, match_type, start_date, end_date,  clicks,  impressions,  ctr, total_spend, average_cpc,  currency, one_day_orders_placed, one_day_ordered_product_sales, one_day_conversion_rate, one_day_same_sku_units_ordered, one_day_other_sku_units_ordered, one_day_same_sku_units_ordered_product_sales, one_day_other_sku_units_ordered_product_sales, one_week_orders_placed, one_week_ordered_product_sales, one_week_conversion_rate, one_week_same_sku_units_ordered, one_week_other_sku_units_ordered, one_week_same_sku_units_ordered_product_sales, one_week_other_sku_units_ordered_product_sales, one_month_orders_placed, one_month_ordered_product_sales, one_month_conversion_rate, one_month_same_sku_units_ordered, one_month_other_sku_units_ordered, one_month_same_sku_units_ordered_product_sales, one_month_other_sku_units_ordered_product_sales,  marketplace_id ) "
				+ "select "
				+ "  campaign_name, ad_group_name, advertised_sku,  keyword, match_type, start_date, end_date,  clicks,  impressions,  ctr, total_spend, average_cpc,  currency, one_day_orders_placed, one_day_ordered_product_sales, one_day_conversion_rate, one_day_same_sku_units_ordered, one_day_other_sku_units_ordered, one_day_same_sku_units_ordered_product_sales, one_day_other_sku_units_ordered_product_sales, one_week_orders_placed, one_week_ordered_product_sales, one_week_conversion_rate, one_week_same_sku_units_ordered, one_week_other_sku_units_ordered, one_week_same_sku_units_ordered_product_sales, one_week_other_sku_units_ordered_product_sales, one_month_orders_placed, one_month_ordered_product_sales, one_month_conversion_rate, one_month_same_sku_units_ordered, one_month_other_sku_units_ordered, one_month_same_sku_units_ordered_product_sales, one_month_other_sku_units_ordered_product_sales, :marketplace_id "
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
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public int updateFromStagingAreaByInfo(Marketplace marketplace,
										   AmazonCampaignPerformanceReportDateCurrencyInfo info) {


		String sql = " select campaign_name, ad_group_name, advertised_sku, keyword, match_type, "
				+ "one_day_orders_placed, one_day_ordered_product_sales, one_day_conversion_rate, one_day_same_sku_units_ordered, one_day_other_sku_units_ordered, " +
				"one_day_same_sku_units_ordered_product_sales, one_day_other_sku_units_ordered_product_sales, one_week_orders_placed, one_week_ordered_product_sales, " +
				"one_week_conversion_rate, one_week_same_sku_units_ordered, one_week_other_sku_units_ordered, one_week_same_sku_units_ordered_product_sales," +
				" one_week_other_sku_units_ordered_product_sales, one_month_orders_placed, one_month_ordered_product_sales, one_month_conversion_rate, " +
				"one_month_same_sku_units_ordered, one_month_other_sku_units_ordered, one_month_same_sku_units_ordered_product_sales, one_month_other_sku_units_ordered_product_sales "
				+ "from amazon_campaign_performance_report_staging_area  "
				+ "where currency = :currency "
				+ "and start_date = :startDate "
				+ "and end_date = :endDate "
				+ "and marketplace_id = :marketplace_id "
				+ "order by id ";
	MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("currency", info.getCurrency());
		q.addValue("startDate", info.getStartDate());
		q.addValue("endDate", info.getEndDate());
		q.addValue("marketplace_id", marketplace.getKey());
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q , objArrayMapper);

		int count = 0;
		for(Object[] columns:resultList){

			String updateSql = "update amazon_campaign_performance_report set " +
					"one_day_orders_placed = :one_day_orders_placed ," +
					"one_day_ordered_product_sales = :one_day_ordered_product_sales, " +
					"one_day_conversion_rate = :one_day_conversion_rate, " +
					"one_day_same_sku_units_ordered = :one_day_same_sku_units_ordered, " +
					"one_day_other_sku_units_ordered = :one_day_other_sku_units_ordered, " +
					"one_day_same_sku_units_ordered_product_sales = :one_day_same_sku_units_ordered_product_sales, " +
					"one_day_other_sku_units_ordered_product_sales = :one_day_other_sku_units_ordered_product_sales," +
					"one_week_orders_placed = :one_week_orders_placed," +
					"one_week_ordered_product_sales = :one_week_ordered_product_sales," +
					"one_week_conversion_rate = :one_week_conversion_rate," +
					"one_week_same_sku_units_ordered = :one_week_same_sku_units_ordered," +
					"one_week_other_sku_units_ordered = :one_week_other_sku_units_ordered," +
					"one_week_same_sku_units_ordered_product_sales = :one_week_same_sku_units_ordered_product_sales," +
					"one_week_other_sku_units_ordered_product_sales = :one_week_other_sku_units_ordered_product_sales," +
					"one_month_orders_placed = :one_month_orders_placed, " +
					"one_month_ordered_product_sales = :one_month_ordered_product_sales, " +
					"one_month_conversion_rate = :one_month_conversion_rate, " +
					"one_month_same_sku_units_ordered = :one_month_same_sku_units_ordered, " +
					"one_month_other_sku_units_ordered = :one_month_other_sku_units_ordered, " +
					"one_month_same_sku_units_ordered_product_sales = :one_month_same_sku_units_ordered_product_sales, " +
					"one_month_other_sku_units_ordered_product_sales = :one_month_other_sku_units_ordered_product_sales "
					+ "where campaign_name = :campaign_name "
					+ "and ad_group_name = :ad_group_name "
					+ "and advertised_sku = :advertised_sku "
					+ "and keyword = :keyword "
					+ "and match_type = :match_type "
					+ "and start_date = :startDate "
					+ "and end_date = :endDate "
					+ "and marketplace_id = :marketplace_id ";


			MapSqlParameterSource updateQuery = new MapSqlParameterSource();

			updateQuery.addValue("campaign_name", columns[0]);
			updateQuery.addValue("ad_group_name", columns[1]);
			updateQuery.addValue("advertised_sku",columns[2]);
			updateQuery.addValue("keyword", columns[3]);
			updateQuery.addValue("match_type", columns[4]);
			updateQuery.addValue("startDate", info.getStartDate());
			updateQuery.addValue("endDate", info.getEndDate());
			updateQuery.addValue("marketplace_id", marketplace.getKey());

			updateQuery.addValue("one_day_orders_placed", columns[5]);
			updateQuery.addValue("one_day_ordered_product_sales", columns[6]);
			updateQuery.addValue("one_day_conversion_rate",columns[7]);
			updateQuery.addValue("one_day_same_sku_units_ordered", columns[8]);
			updateQuery.addValue("one_day_other_sku_units_ordered", columns[9]);
			updateQuery.addValue("one_day_same_sku_units_ordered_product_sales",columns[10]);
			updateQuery.addValue("one_day_other_sku_units_ordered_product_sales", columns[11]);

			updateQuery.addValue("one_week_orders_placed", columns[12]);
			updateQuery.addValue("one_week_ordered_product_sales",columns[13]);
			updateQuery.addValue("one_week_conversion_rate", columns[14]);
			updateQuery.addValue("one_week_same_sku_units_ordered", columns[15]);
			updateQuery.addValue("one_week_other_sku_units_ordered",columns[16]);
			updateQuery.addValue("one_week_same_sku_units_ordered_product_sales", columns[17]);
			updateQuery.addValue("one_week_other_sku_units_ordered_product_sales", columns[18]);

			updateQuery.addValue("one_month_orders_placed",columns[19]);
			updateQuery.addValue("one_month_ordered_product_sales", columns[20]);
			updateQuery.addValue("one_month_conversion_rate", columns[21]);
			updateQuery.addValue("one_month_same_sku_units_ordered",columns[22]);
			updateQuery.addValue("one_month_other_sku_units_ordered",columns[23]);
			updateQuery.addValue("one_month_same_sku_units_ordered_product_sales", columns[24]);
			updateQuery.addValue("one_month_other_sku_units_ordered_product_sales", columns[25]);


			count += getNamedParameterJdbcTemplate().update(sql,q);

		}



		return count;
	}

	@Override @SuppressWarnings("unchecked")
	public List<AmazonCampaignPerformanceReportBriefLineItem> queryBriefLineItem(int startIndex, int size, Marketplace marketplace, Date start, Date end, String sku) {
		String sql = this.composeQueryBriefLineItemSelectSql()
				+ this.composeQueryBriefLineItemFromSql()
				+ this.composeQueryBriefLineItemWhereSql(marketplace,start,end,sku)
				+ "order by acpr.id, acpr.start_date "
				+ "limit :size offset :start";;
		MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q,marketplace, start, end, sku);
		q.addValue("size", size);
		q.addValue("start", startIndex-1);


		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) ->
				new AmazonCampaignPerformanceReportBriefLineItemImpl(
						rs.getInt("id"),rs.getString("campaign_name"),rs.getString("ad_group_name")
						,rs.getString("advertised_sku")
						,rs.getString("keyword"),rs.getString("match_type"),
						rs.getString("start_date"),rs.getString("end_date")
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
	public int queryBriefLineItemCount(Marketplace marketplace,Date start,Date end,String marketSku) {
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
