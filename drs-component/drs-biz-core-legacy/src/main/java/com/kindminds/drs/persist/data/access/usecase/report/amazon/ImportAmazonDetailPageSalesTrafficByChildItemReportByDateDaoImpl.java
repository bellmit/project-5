package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportByDateDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonDetailPageSalesTrafficByChildItemReportRawLine;

@Repository
public class ImportAmazonDetailPageSalesTrafficByChildItemReportByDateDaoImpl  extends Dao implements ImportAmazonDetailPageSalesTrafficByChildItemReportByDateDao {
	


	@Override
	public boolean queryExist(int marketplaceId, Date reportDate) {
		String sql = "select exists("
				+ "    select 1 from amazon_detail_page_sales_traffic_report_by_childItem_daily r "
				+ "    where r.marketplace_id = :marketplace_id "
				+ "    and r.report_date =  :report_date "
				+ ")";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id",marketplaceId);
		q.addValue("report_date",reportDate);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
	}
	
	@Override @Transactional("transactionManager")
	public int insertReportLines(int marketplaceId, Date reportDate, List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> lineItems) {
		String sql = "insert into amazon_detail_page_sales_traffic_report_by_childItem_daily "
				+ "(  report_date,  marketplace_id,  parent_asin,  child_asin,  title,  sessions,  session_percentage,  page_views,  page_views_percentage,  buy_box_percentage,  units_ordered,  unit_session_percentage,  ordered_product_sales,  total_order_items ) values "
				+ "( :report_date, :marketplace_id, :parent_asin, :child_asin, :title, :sessions, :session_percentage, :page_views, :page_views_percentage, :buy_box_percentage, :units_ordered, :unit_session_percentage, :ordered_product_sales, :total_order_items ) ";
		int insertedRows = 0;
		for(AmazonDetailPageSalesTrafficByChildItemReportRawLine line:lineItems){
		MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("report_date",reportDate);
			q.addValue("marketplace_id",marketplaceId);
			q.addValue("parent_asin",line.getParentAsin());
			q.addValue("child_asin",line.getChildAsin());
			q.addValue("title",line.getTitle());
			q.addValue("sessions",line.getSessions());
			q.addValue("session_percentage",line.getSessionPercentage());
			q.addValue("page_views",line.getPageViews());
			q.addValue("page_views_percentage",line.getPageViewsPercentage());
			q.addValue("buy_box_percentage",line.getBuyBoxPercentage());
			q.addValue("units_ordered",line.getUnitsOrdered());
			q.addValue("unit_session_percentage",line.getUnitSessionPercentage());
			q.addValue("ordered_product_sales",line.getOrderedProductSales());
			q.addValue("total_order_items",line.getTotalOrderItems());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			insertedRows++;
		}
		return insertedRows;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryDistinctReportDateMarketplace(Date start, Date end) {
		String sql = "select distinct to_char(report_date at time zone :tz,:fm), marketplace_id from amazon_detail_page_sales_traffic_report_by_childItem_daily "
				+ "where true "
				+ "and report_date >= :start "
				+ "and report_date <  :end ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD");
		q.addValue("start",start);
		q.addValue("end",end);
		return getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
	}

	@Override @Transactional("transactionManager")
	public int delete(Date date, int marketplaceId) {
		String sql = "delete from amazon_detail_page_sales_traffic_report_by_childItem_daily where report_date = :date and marketplace_id = :marketplace_id ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date",date);
		q.addValue("marketplace_id",marketplaceId);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

}
