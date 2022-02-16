package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonHeadlineSearchAdReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Repository
public class ImportAmazonHeadlineSearchAdReportDaoImpl extends Dao implements ImportAmazonHeadlineSearchAdReportDao {



	@Override @Transactional("transactionManager")
	public Integer deleteByDateAndMarketplace(int marketplaceId, String reportDate, String reportType) {
		String deleteSql = "delete from amazon_hsa_" + reportType + "_report "
				+ "where marketplace_id = :marketplaceId  "
				+ "AND report_date = :reportDate\\:\\:date ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("reportDate", reportDate);
		return getNamedParameterJdbcTemplate().update(deleteSql,q);
	}

	private Boolean campaignRecordExists(AmazonHeadlineSearchAdReportItem record) {
		String existSql = "SELECT exists(select 1 from amazon_hsa_campaign_report"
				+ " WHERE marketplace_id = :marketplaceId "
				+ " AND campaign_name = :campaignName "
				+ " AND currency = :currency "
				+ " AND report_date = :reportDate )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", record.getMarketplaceId());
		Date reportDate = record.getReportDate();
		q.addValue("reportDate", reportDate);
		q.addValue("campaignName", record.getCampaignName());
		q.addValue("currency", record.getCurrency());
		return getNamedParameterJdbcTemplate().queryForObject(existSql,q,Boolean.class);
	}

	private Boolean keywordRecordExists(AmazonHeadlineSearchAdReportItem record) {
		String existSql = "SELECT exists(select 1 from amazon_hsa_keyword_report"
				+ " WHERE marketplace_id = :marketplaceId "
				+ " AND campaign_name = :campaignName "
				+ " AND currency = :currency "
				+ " AND targeting = :targeting "
				+ " AND match_type = :matchType "
				+ " AND report_date = :reportDate )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", record.getMarketplaceId());
		Date reportDate = record.getReportDate();
		q.addValue("reportDate", reportDate);
		q.addValue("campaignName", record.getCampaignName());
		q.addValue("currency", record.getCurrency());
		q.addValue("targeting", record.getTargeting());
		q.addValue("matchType", record.getMatchType());
		return getNamedParameterJdbcTemplate().queryForObject(existSql,q,Boolean.class);
	}

	@Override @Transactional("transactionManager")
	public Integer insertKeywordRecord(List<AmazonHeadlineSearchAdReportItem> records) {
		String insertSql = "INSERT INTO amazon_hsa_keyword_report ( "
				+ " marketplace_id, report_date, currency, campaign_name, "
				+ " targeting, match_type, impressions, clicks, "
				+ " click_thru_rate, cost_per_click, spend, total_advertising_cost_of_sales, "
				+ " total_return_on_advertising_spend, total_sales_14days, total_orders_14days, "
				+ " total_units_14days, conversion_rate_14days, portfolio_name, " +
				" orders_new_to_brand, pct_orders_new_to_brand, sales_new_to_brand,\n" +
				" pct_sales_new_to_brand, units_new_to_brand, pct_units_new_to_brand,\n" +
				" order_rate_new_to_brand, video) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false) ";

		int[][] updateCounts = this.getJdbcTemplate().batchUpdate(insertSql, records, 20 ,
				new ParameterizedPreparedStatementSetter<AmazonHeadlineSearchAdReportItem>() {
					@Override
					public void setValues(PreparedStatement pstmt, AmazonHeadlineSearchAdReportItem record) throws SQLException {
						if (!keywordRecordExists(record)) {
							int marketplaceId = record.getMarketplaceId();
							pstmt.setInt(1, marketplaceId);
							Date dateTime = record.getReportDate();
							pstmt.setDate(2, new java.sql.Date(dateTime.getTime()));
							pstmt.setString(3, record.getCurrency());
							pstmt.setString(4, record.getCampaignName());
							pstmt.setString(5, record.getTargeting());
							pstmt.setString(6, record.getMatchType());
							pstmt.setInt(7, record.getImpressions());
							pstmt.setInt(8, record.getClicks());
							pstmt.setBigDecimal(9, record.getClickThruRate());
							pstmt.setBigDecimal(10, record.getCostPerClick());
							pstmt.setBigDecimal(11, record.getSpend());
							pstmt.setBigDecimal(12, record.getTotalACOS());
							pstmt.setBigDecimal(13, record.getTotalRoAS());
							pstmt.setBigDecimal(14, record.getTotalSales14days());
							pstmt.setInt(15, record.getTotalOrders14days());
							pstmt.setInt(16, record.getTotalUnits14days());
							pstmt.setBigDecimal(17, record.getConversionRate14days());
							pstmt.setString(18, record.getPortfolioName());
							pstmt.setInt(19, record.getOrdersNewToBrand());
							pstmt.setBigDecimal(20, record.getPctOrdersNewToBrand());
							pstmt.setBigDecimal(21, record.getSalesNewToBrand());
							pstmt.setBigDecimal(22, record.getPctSalesNewToBrand());
							pstmt.setInt(23, record.getUnitsNewToBrand());
							pstmt.setBigDecimal(24, record.getPctUnitsNewToBrand());
							pstmt.setBigDecimal(25, record.getOrderRateNewToBrand());
						}
					}
				});

		return records.size();

	}

	@Override @Transactional("transactionManager")
	public Integer insertKeywordVideoRecord(List<AmazonHeadlineSearchAdReportItem> records) {

		String insertSql = "INSERT INTO amazon_hsa_keyword_report ( "
				+ " marketplace_id, report_date, currency, campaign_name, "
				+ " targeting, match_type, impressions, clicks, "
				+ " click_thru_rate, cost_per_click, spend, total_advertising_cost_of_sales, "
				+ " total_return_on_advertising_spend, total_sales_14days, total_orders_14days, "
				+ " total_units_14days, conversion_rate_14days, portfolio_name, "
				+ " video) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true) ";

		int[][] updateCounts = this.getJdbcTemplate().batchUpdate(insertSql, records, 20 ,
				(pstmt, record) -> {
					int marketplaceId = record.getMarketplaceId();
					pstmt.setInt(1, marketplaceId);
					Date dateTime = record.getReportDate();
					pstmt.setDate(2, new java.sql.Date(dateTime.getTime()));
					pstmt.setString(3, record.getCurrency());
					pstmt.setString(4, record.getCampaignName());
					pstmt.setString(5, record.getTargeting());
					pstmt.setString(6, record.getMatchType());
					pstmt.setInt(7, record.getImpressions());
					pstmt.setInt(8, record.getClicks());
					pstmt.setBigDecimal(9, record.getClickThruRate());
					pstmt.setBigDecimal(10, record.getCostPerClick());
					pstmt.setBigDecimal(11, record.getSpend());
					pstmt.setBigDecimal(12, record.getTotalACOS());
					pstmt.setBigDecimal(13, record.getTotalRoAS());
					pstmt.setBigDecimal(14, record.getTotalSales14days());
					pstmt.setInt(15, record.getTotalOrders14days());
					pstmt.setInt(16, record.getTotalUnits14days());
					pstmt.setBigDecimal(17, record.getConversionRate14days());
					pstmt.setString(18, record.getPortfolioName());

				});

			return records.size();
	}

	@Override @Transactional("transactionManager")
	public Integer insertCampaignRecord(List<AmazonHeadlineSearchAdReportItem> records) {
		String insertSql = "INSERT INTO amazon_hsa_campaign_report ( "
				+ " marketplace_id, report_date, currency, campaign_name, "
				+ " impressions, clicks, "
				+ " click_thru_rate, cost_per_click, spend, total_advertising_cost_of_sales, "
				+ " total_return_on_advertising_spend, total_sales_14days, total_orders_14days, "
				+ " total_units_14days, conversion_rate_14days, portfolio_name, " +
				" orders_new_to_brand, pct_orders_new_to_brand, sales_new_to_brand,\n" +
				" pct_sales_new_to_brand, units_new_to_brand, pct_units_new_to_brand,\n" +
				" order_rate_new_to_brand, video, report_type) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false, 'campaign') ";

		this.getJdbcTemplate().batchUpdate(insertSql, records, 20 , (pstmt, record) -> {
//			if (!campaignRecordExists(record)) {
				int marketplaceId = record.getMarketplaceId();
				pstmt.setInt(1, marketplaceId);
				Date dateTime = record.getReportDate();
				pstmt.setDate(2, new java.sql.Date(dateTime.getTime()));
				pstmt.setString(3, record.getCurrency());
				pstmt.setString(4, record.getCampaignName());
				pstmt.setInt(5, record.getImpressions());
				pstmt.setInt(6, record.getClicks());
				pstmt.setBigDecimal(7, record.getClickThruRate());
				pstmt.setBigDecimal(8, record.getCostPerClick());
				pstmt.setBigDecimal(9, record.getSpend());
				pstmt.setBigDecimal(10, record.getTotalACOS());
				pstmt.setBigDecimal(11, record.getTotalRoAS());
				pstmt.setBigDecimal(12, record.getTotalSales14days());
				pstmt.setInt(13, record.getTotalOrders14days());
				pstmt.setInt(14, record.getTotalUnits14days());
				pstmt.setBigDecimal(15, record.getConversionRate14days());
				pstmt.setString(16, record.getPortfolioName());
				pstmt.setInt(17, record.getOrdersNewToBrand());
				pstmt.setBigDecimal(18, record.getPctOrdersNewToBrand());
				pstmt.setBigDecimal(19, record.getSalesNewToBrand());
				pstmt.setBigDecimal(20, record.getPctSalesNewToBrand());
				pstmt.setInt(21, record.getUnitsNewToBrand());
				pstmt.setBigDecimal(22, record.getPctUnitsNewToBrand());
				pstmt.setBigDecimal(23, record.getOrderRateNewToBrand());
//			}

		});

		return records.size();
	}

	@Override @Transactional("transactionManager")
	public Integer insertCampaignVideoRecord(List<AmazonHeadlineSearchAdReportItem> records) {
		String insertSql = "INSERT INTO amazon_hsa_campaign_report ( "
				+ " marketplace_id, report_date, currency, campaign_name, "
				+ " impressions, clicks, "
				+ " click_thru_rate, cost_per_click, spend, total_advertising_cost_of_sales, "
				+ " total_return_on_advertising_spend, total_sales_14days, total_orders_14days, "
				+ " total_units_14days, conversion_rate_14days, portfolio_name, "
				+ " viewable_impressions, view_thru_rate, click_thru_rate_for_views, "
				+ " video_first_quartile_views, video_midpoint_views, video_third_quartile_views, "
				+ " video_complete_views, video_unmutes, views_5second, view_rate_5second, "
				+ " video, report_type) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, 'video') ";



		int[][] updateCounts = this.getJdbcTemplate().batchUpdate(insertSql, records, 20 ,
				new ParameterizedPreparedStatementSetter<AmazonHeadlineSearchAdReportItem>() {
					@Override
					public void setValues(PreparedStatement pstmt, AmazonHeadlineSearchAdReportItem record) throws SQLException {
						int marketplaceId = record.getMarketplaceId();
						pstmt.setInt(1, marketplaceId);
						Date dateTime = record.getReportDate();
						pstmt.setDate(2, new java.sql.Date(dateTime.getTime()));
						pstmt.setString(3, record.getCurrency());
						pstmt.setString(4, record.getCampaignName());
						pstmt.setInt(5, record.getImpressions());
						pstmt.setInt(6, record.getClicks());
						pstmt.setBigDecimal(7, record.getClickThruRate());
						pstmt.setBigDecimal(8, record.getCostPerClick());
						pstmt.setBigDecimal(9, record.getSpend());
						pstmt.setBigDecimal(10, record.getTotalACOS());
						pstmt.setBigDecimal(11, record.getTotalRoAS());
						pstmt.setBigDecimal(12, record.getTotalSales14days());
						pstmt.setInt(13, record.getTotalOrders14days());
						pstmt.setInt(14, record.getTotalUnits14days());
						pstmt.setBigDecimal(15, record.getConversionRate14days());
						pstmt.setString(16, record.getPortfolioName());
						pstmt.setInt(17, record.getViewableImpressions());
						pstmt.setBigDecimal(18, record.getViewThruRate());
						pstmt.setBigDecimal(19, record.getClickThruRateForViews());
						pstmt.setInt(20, record.getVideoFirstQuartileViews());
						pstmt.setInt(21, record.getVideoMidpointViews());
						pstmt.setInt(22, record.getVideoThirdQuartileViews());
						pstmt.setInt(23, record.getVideoCompleteViews());
						pstmt.setInt(24, record.getVideoUnmutes());
						pstmt.setInt(25, record.getViews5second());
						pstmt.setBigDecimal(26, record.getViewRate5second());


					}
		});

		return records.size();
	}


	@Override @Transactional("transactionManager")
	public Integer insertDisplayRecord(List<AmazonHeadlineSearchAdReportItem> records) {
		String insertSql = "INSERT INTO amazon_hsa_campaign_report ( "
				+ " marketplace_id, report_date, currency, campaign_name, "
				+ " impressions, clicks, "
				+ " click_thru_rate, cost_per_click, spend, total_advertising_cost_of_sales, "
				+ " total_return_on_advertising_spend, total_sales_14days, total_orders_14days, "
				+ " total_units_14days, conversion_rate_14days, "
				+ " video, report_type) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false, 'display') ";


		int[][] updateCounts = this.getJdbcTemplate().batchUpdate(insertSql, records, 20 ,
				new ParameterizedPreparedStatementSetter<AmazonHeadlineSearchAdReportItem>() {
					@Override
					public void setValues(PreparedStatement pstmt, AmazonHeadlineSearchAdReportItem record) throws SQLException {
						int marketplaceId = record.getMarketplaceId();
						pstmt.setInt(1, marketplaceId);
						//Date dateTime = stringToDate(record.getReportDate());
						Date dateTime = record.getReportDate();
						pstmt.setDate(2, new java.sql.Date(dateTime.getTime()));
						pstmt.setString(3, record.getCurrency());
						pstmt.setString(4, record.getCampaignName());
						pstmt.setInt(5, record.getImpressions());
						pstmt.setInt(6, record.getClicks());
						pstmt.setBigDecimal(7, record.getClickThruRate());
						pstmt.setBigDecimal(8, record.getCostPerClick());
						pstmt.setBigDecimal(9, record.getSpend());
						pstmt.setBigDecimal(10, record.getTotalACOS());
						pstmt.setBigDecimal(11, record.getTotalRoAS());
						pstmt.setBigDecimal(12, record.getTotalSales14days());
						pstmt.setInt(13, record.getTotalOrders14days());
						pstmt.setInt(14, record.getTotalUnits14days());
						pstmt.setBigDecimal(15, record.getConversionRate14days());

					}
				});

		return records.size();
	}
	private static final String DATE_FORMAT = "M dd, yyyy"; //CH format
//	private static final String DATE_FORMAT = "dd-M-yyyy"; //20210318 CH format
//	private static final String DATE_FORMAT = "dd-MMM-yyyy"; //US format

	private Date stringToDate(String dateStr) {
		dateStr = dateStr.replace("月", "");
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		Date date = null;
	    try {
	    	date = sdf.parse(dateStr);
	    } catch (Exception e) {
	    	System.out.println("exception: " + e);
	    }
		return date;
	}

}