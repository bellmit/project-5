package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonDetailPageSalesTrafficByChildItemReportRawLine;

@Repository
public class ImportAmazonDetailPageSalesTrafficByChildItemReportDaoImpl extends Dao implements ImportAmazonDetailPageSalesTrafficByChildItemReportDao {
	


	@Override @Transactional("transactionManager")
	public Integer insertRawLines(Marketplace marketplace,int periodId,List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> rawLines) {
		String sql = "insert into amazon_detail_page_sales_traffic_report_by_childItem "
				+ "( settlement_period_id, parent_asin, child_asin,  title,  sessions, session_percentage, page_views, page_views_percentage, buy_box_percentage, units_ordered,  unit_session_percentage,   ordered_product_sales,  total_order_items,   marketplace_id ) values "
				+ "(  :settlementPeriodId, :parentAsin, :childAsin, :title, :sessions, :sessionPercentage, :pageViews,  :pageViewsPercentage,  :buyBoxPercentage, :unitsOrdered,   :unitSessionPercentage,    :orderedProductSales,   :totalOrderItems,  :marketplace_id ) ";
		int insertedRows = 0;
		for(AmazonDetailPageSalesTrafficByChildItemReportRawLine line:rawLines){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("settlementPeriodId",periodId);
			q.addValue("parentAsin",line.getParentAsin());
			q.addValue("childAsin",line.getChildAsin());
			q.addValue("title",line.getTitle());
			q.addValue("sessions",line.getSessions());
			q.addValue("sessionPercentage",line.getSessionPercentage());
			q.addValue("pageViews",line.getPageViews());
			q.addValue("pageViewsPercentage",line.getPageViewsPercentage());
			q.addValue("buyBoxPercentage",line.getBuyBoxPercentage());
			q.addValue("unitsOrdered",line.getUnitsOrdered());
			q.addValue("unitSessionPercentage",line.getUnitSessionPercentage());
			q.addValue("orderedProductSales",line.getOrderedProductSales());
			q.addValue("totalOrderItems",line.getTotalOrderItems());
			q.addValue("marketplace_id",marketplace.getKey());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			insertedRows++;
		}
		return insertedRows;
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
		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySettlementPeriodList(List<Integer> excludedPeriodIds) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select sp.id as id, ")
				.append("sp.period_start as start, ")
				.append("sp.period_end   as end ")
				.append("from settlement_period sp ")
				.append("where sp.id not in (:excludedPeriodIds) ")
				.append("order by sp.period_start desc ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("excludedPeriodIds",excludedPeriodIds);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Integer> queryOccupiedPeriodIdList(Marketplace marketplace) {
		String sql = "select distinct settlement_period_id from amazon_detail_page_sales_traffic_report_by_childItem where marketplace_id = :marketplace_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id",marketplace.getKey());
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}

	@Override @Transactional("transactionManager")
	public int deleteReportDataByPeriod(Marketplace marketplace,Integer settlementPeriodId) {
		String sql = "delete from amazon_detail_page_sales_traffic_report_by_childItem where marketplace_id = :marketplace_id and settlement_period_id = :settlementPeriodId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementPeriodId",settlementPeriodId);
		q.addValue("marketplace_id",marketplace.getKey());
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

}
