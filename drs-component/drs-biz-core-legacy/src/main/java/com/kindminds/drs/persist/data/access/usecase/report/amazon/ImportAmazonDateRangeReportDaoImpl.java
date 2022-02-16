package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonDateRangeReportDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.kindminds.drs.api.v1.model.report.DateRangeReportItem;

@Repository
public class ImportAmazonDateRangeReportDaoImpl extends Dao implements ImportAmazonDateRangeReportDao {



	@Override @Transactional("transactionManager")
	public int deleteExistingEntriesByMarketplace(int marketplaceId, String dateTime) {
		StringBuilder sb = new StringBuilder()
				.append("WITH import_time AS (  ")
				.append("select min(inserted_on) as min_date from amazon_date_range_report  ")
				.append("where marketplace_id = :marketplaceId  ")
				.append("AND date_time = :dateTime ) ")
				.append("delete from amazon_date_range_report ")
				.append("where inserted_on BETWEEN (select min_date from import_time )  ")
				.append("AND (select min_date + interval '1 second' from import_time ) ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		Date formattedDate = conformDate(marketplaceId, dateTime);
		q.addValue("dateTime", formattedDate);
		return getNamedParameterJdbcTemplate().update(sb.toString(),q);
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryImportStatus() {
		String sql = "select marketplace_id, "
				+ " max(inserted_on) as imported_date, "
				+ " max(date_time) as report_date "
				+ " from amazon_date_range_report "
				+ " group by marketplace_id order by marketplace_id ";


		List<Object[]> results = getJdbcTemplate().query(sql,objArrayMapper);

        return results;
	}

	@Override @Transactional("transactionManager")
	public Integer insertRecord(List<DateRangeReportItem> records) {
		String insertSql = "INSERT INTO amazon_date_range_report( " +
				" marketplace_id, date_time, settlement_id, type, " +
				" order_id, sku, description, quantity, marketplace, fulfillment, " +
				" order_city, order_state, order_postal, tax_collection_model, " +
				" product_sales, product_sales_tax, shipping_credits, shipping_credits_tax, " +
				" gift_wrap_credits, gift_wrap_credits_tax, promotional_rebates, promotional_rebates_tax, " +
				" marketplace_facilitator_tax, " +
				" selling_fees, fba_fees, other_transaction_fees, other, total, account_type) " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
				"		?, ?, ?, ?, ?, ?, ?, ?, ?); ";

		int[][] updateCounts = this.getJdbcTemplate().batchUpdate(insertSql, records, 50 ,
				new ParameterizedPreparedStatementSetter<DateRangeReportItem>() {
					@Override
					public void setValues(PreparedStatement pstmt, DateRangeReportItem record) throws SQLException {
						int marketplaceId = record.getMarketplaceId();
						pstmt.setInt(1, marketplaceId);
						Date dateTime = conformDate(marketplaceId, record.getDateTime());
						pstmt.setTimestamp(2, new java.sql.Timestamp(dateTime.getTime()));
						pstmt.setString(3, record.getSettlementId());
						pstmt.setString(4, record.getType());
						pstmt.setString(5, record.getOrderId());
						pstmt.setString(6, record.getSku());
						pstmt.setString(7, record.getDescription());
						pstmt.setInt(8, record.getQuantity());
						pstmt.setString(9, record.getMarketplace());
						pstmt.setString(10, record.getFulfillment());
						pstmt.setString(11, record.getOrderCity());
						pstmt.setString(12, record.getOrderState());
						pstmt.setString(13, record.getOrderPostal());
						pstmt.setString(14, record.getTaxCollectionModel());
						pstmt.setBigDecimal(15, record.getProductSales());
						pstmt.setBigDecimal(16, record.getProductSalesTax());
						pstmt.setBigDecimal(17, record.getShippingCredits());
						pstmt.setBigDecimal(18, record.getShippingCreditsTax());
						pstmt.setBigDecimal(19, record.getGiftWrapCredits());
						pstmt.setBigDecimal(20, record.getGiftWrapCreditsTax());
						pstmt.setBigDecimal(21, record.getPromotionalRebates());
						pstmt.setBigDecimal(22, record.getPromotionalRebatesTax());
						pstmt.setBigDecimal(23, record.getMarketplaceFacilitatorTax());
						pstmt.setBigDecimal(24, record.getSellingFees());
						pstmt.setBigDecimal(25, record.getFbaFees());
						pstmt.setBigDecimal(26, record.getOtherTransactionFees());
						pstmt.setBigDecimal(27, record.getOther());
						pstmt.setBigDecimal(28, record.getTotal());
						pstmt.setString(29, record.getAccountType());

					}

				});

		return records.size();
	}

	private Date conformDate(int marketplaceId, String dateStr) {
		 if (marketplaceId == 7) {
			return convertToDate(7, dateStr.replaceAll("UTC", "GMT"));
		} else {
			return convertToDate(marketplaceId, dateStr);
		}
	}

	private static final String DATEFORMAT_US = "MMM d, yyyy h:mm:ss a z";
	private static final String DATEFORMAT_UK = "d MMM yyyy HH:mm:ss zzz";
	private static final String DATEFORMAT_CA = "MMM d, yyyy h:mm:ss a z";
	private static final String DATEFORMAT_DE = "dd.MM.yyyy HH:mm:ss zzz";
	private static final String DATEFORMAT_FR = "d MMMM yyyy HH:mm:ss zzz";
	private static final String DATEFORMAT_IT = "d MMM yyyy HH:mm:ss zzz";
	private static final String DATEFORMAT_ES = "d MMMM yyyy HH:mm:ss zzz";
	private static final String DATEFORMAT_MX = "d MMM. yyyy h:mm:ss";

	private Date convertToDate(int marketplaceId, String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_US, Locale.US);
		Date date = null;
	    try {
	    	if (marketplaceId == 4) {
	    		sdf = new SimpleDateFormat(DATEFORMAT_UK, Locale.UK);
	    	} else if (marketplaceId == 5) {
	    		sdf = new SimpleDateFormat(DATEFORMAT_CA, Locale.CANADA);
	    	} else if (marketplaceId == 6) {
	    		sdf = new SimpleDateFormat(DATEFORMAT_DE, Locale.GERMANY);
	    	} else if (marketplaceId == 7) {
	    		sdf = new SimpleDateFormat(DATEFORMAT_FR, Locale.FRANCE);
	    	} else if (marketplaceId == 8) {
	    		sdf = new SimpleDateFormat(DATEFORMAT_IT, Locale.ITALIAN);
	    	} else if (marketplaceId == 9) {
	    		sdf = new SimpleDateFormat(DATEFORMAT_ES, new Locale("es", "ES"));
	    	} else if (marketplaceId == 16) {
	    		sdf = new SimpleDateFormat(DATEFORMAT_MX);
	    		dateStr = dateStr.substring(0, dateStr.length()-6);
			}
	    	date = sdf.parse(dateStr);
	    } catch (Exception e) {
	    	System.out.println("exception: " + e);
	    }
//	    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.US);
//	    sdf.setTimeZone(getTimeZone(marketplaceId));
		return date;
	}

//	private TimeZone getTimeZone(int marketplaceId) {
//		if (marketplaceId == 1 || marketplaceId == 5) {
//			return TimeZone.getTimeZone("America/Los_Angeles");
//		} else {
//			return TimeZone.getTimeZone("GMT");
//		}
//	}

}