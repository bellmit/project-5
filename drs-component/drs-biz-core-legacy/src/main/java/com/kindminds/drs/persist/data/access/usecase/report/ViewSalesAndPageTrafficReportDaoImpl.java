package com.kindminds.drs.persist.data.access.usecase.report;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;


import com.kindminds.drs.api.data.access.usecase.report.ViewSalesAndPageTrafficReportDao;


@Repository
public class ViewSalesAndPageTrafficReportDaoImpl extends Dao implements ViewSalesAndPageTrafficReportDao {


	
	@Override @SuppressWarnings("unchecked")
	public List<String> queryAsinBySku(int marketplaceId, List<String> skus) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select pmia.asin ")
				.append("from product_marketplace_info_amazon pmia ")
				.append("inner join product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id ")
				.append("inner join product_sku ps on ps.product_id = pmi.product_id ")
				.append("where pmi.marketplace_id = :marketplace_id ")
				.append("and ps.code_by_drs in (:skus) ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id",marketplaceId);
		q.addValue("skus",skus);

		List<String> rList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);

		if(rList.size() == 0){
			sqlSb = new StringBuilder()
					.append("select pmia.asin ")
					.append("from product_k101_marketplace_info_amazon pmia ")
					.append("inner join product_k101_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id ")
					.append("inner join product_sku ps on ps.product_id = pmi.product_id ")
					.append("where pmi.marketplace_id = :marketplace_id ")
					.append("and ps.code_by_drs in (:skus) ");
			q = new MapSqlParameterSource();
			q.addValue("marketplace_id",marketplaceId);
			q.addValue("skus",skus);
			rList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
		}


		return rList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryAsinByProduct(int marketplaceId, List<String> productBases) {
		StringBuilder sqlSb = new StringBuilder()
				.append(" select pmia.asin ")
				.append(" from product_marketplace_info_amazon pmia ")
				.append(" inner join product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id ")
				.append(" inner join product_sku ps on ps.product_id = pmi.product_id ")
				.append(" inner join product_base pb on ps.product_base_id = pb.id ")
				.append(" where pmi.marketplace_id = :marketplace_id ")
				.append(" and pb.code_by_drs in (:productBases) ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplaceId);
		q.addValue("productBases", productBases);

		List<String> rList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class );
		if(rList.size() ==0){
			 sqlSb = new StringBuilder()
					.append(" select pmia.asin ")
					.append(" from product_k101_marketplace_info_amazon pmia ")
					.append(" inner join product_k101_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id ")
					.append(" inner join product_sku ps on ps.product_id = pmi.product_id ")
					.append(" inner join product_base pb on ps.product_base_id = pb.id ")
					.append(" where pmi.marketplace_id = :marketplace_id ")
					.append(" and pb.code_by_drs in (:productBases) ");
			q = new MapSqlParameterSource();
			q.addValue("marketplace_id", marketplaceId);
			q.addValue("productBases", productBases);
			rList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q, String.class);
		}

		return rList;
	}

	@Override
	public Date queryStartDate(int marketplaceId, List<String> asins) {
		if(asins.isEmpty()) return null;
		StringBuilder sqlSb = new StringBuilder()
				.append(" select min(r.report_date) ")
				.append(" from amazon_detail_page_sales_traffic_report_by_childitem_daily r ")
				.append(" where r.marketplace_id = :marketplace_id and r.child_asin in (:asins) ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplaceId);
		q.addValue("asins", asins);
		Date startDate = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Date.class);
		return startDate;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<String> queryProductBases(String supplierKcode){
		StringBuilder sqlSb = new StringBuilder()
				.append(" select pb.code_by_drs from product_base pb ") 
				.append(" inner join company com on com.id = pb.supplier_company_id ") 
				.append(" where com.k_code = :supplierKcode ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode",supplierKcode);
		return getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryProductBaseNames(String supplierKcode){
		StringBuilder sqlSb = new StringBuilder()
				.append(" select pb.code_by_drs, p.name_by_supplier from product_base pb ")
				.append(" inner join company com on com.id = pb.supplier_company_id ")
				.append(" inner join product p on p.id = pb.product_id ")
				.append(" where com.k_code = :supplierKcode ")
				.append(" order by pb.code_by_drs");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode",supplierKcode);
        List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Map<String,String> baseCodeToProductName = new TreeMap<String,String>();
        for(Object[] items:result){
        	baseCodeToProductName.put((String)items[0], (String)items[0] + " " + (String)items[1]);
        }
        return baseCodeToProductName;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<String> queryProductSkus(String productBase) {
		String sql = "select ps.code_by_drs from product_sku ps inner join product_base pb on pb.id = ps.product_base_id where pb.code_by_drs = :productBase ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBase",productBase);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryProductSkuNames(String productBase) {
		StringBuilder sqlSb = new StringBuilder()
				.append(" select ps.code_by_drs, p.name_by_supplier from product_sku ps ")
				.append(" inner join product p on p.id = ps.product_id ")
				.append(" inner join product_base pb on pb.id = ps.product_base_id ")
				.append(" where pb.code_by_drs = :productBase ")
				.append(" order by ps.code_by_drs");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBase",productBase);
        List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Map<String,String> baseCodeToSkuName = new TreeMap<String,String>();
        for(Object[] items:result){
        	baseCodeToSkuName.put((String)items[0], (String)items[0] + " " + (String)items[1]);
        }
        return baseCodeToSkuName;
	}

	@Override @SuppressWarnings("unchecked")
	public Object[] querySummary(Date start,Date end,Integer marketplaceId,List<String> asins) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("sum(rpt.total_order_items)     as total_order_items, ")
				.append("sum(rpt.ordered_product_sales) as total_ordered_product_sales, ")
				.append("sum(rpt.sessions)              as total_sessions, ")
				.append("sum(rpt.units_ordered)              as total_units_ordered ")
				.append("from amazon_detail_page_sales_traffic_report_by_childitem_daily rpt ")
				.append("where rpt.marketplace_id = :marketplace_id ")
				.append(this.composeAsinQueryConditionForQueryHistoryLines(asins))
				.append("and rpt.report_date >= :start ")
				.append("and rpt.report_date <= :end ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		if(!asins.isEmpty())q.addValue("asins", asins);
		q.addValue("marketplace_id", marketplaceId);
		q.addValue("start", start);
		q.addValue("end", end);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		if(columnsList.size()==0) return null;
		Object[] columns = columnsList.get(0);
		return  columns ;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryHistoryLines(Date start,Date end,Integer marketplaceId,List<String> asins) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select list_date.date,")
				.append(" detail.sessions,")
				.append(" detail.page_views,")
				.append(" detail.sessions_multiply_buy_box_percentage,")
				.append(" detail.units_ordered,")
				.append(" detail.ordered_product_sales,")
				.append(" detail.total_order_items ")
				.append(" from ( select date from ")
				.append(" generate_series(:end,:start,'-1 day'\\:\\:interval) ")
				.append(" date ) list_date ")
				.append("left join ( ")
				.append("    select rpt.report_date as report_date, ")
				.append("    sum(rpt.sessions)               as sessions, ")
				.append("    sum(rpt.page_views)                as page_views, ")
				.append("    sum(rpt.sessions*rpt.buy_box_percentage) as sessions_multiply_buy_box_percentage, ")
				.append("    sum(rpt.units_ordered)             as units_ordered, ")
				.append("    sum(rpt.ordered_product_sales)     as ordered_product_sales, ")
				.append("    sum(rpt.total_order_items)         as total_order_items ")
				.append("    from amazon_detail_page_sales_traffic_report_by_childitem_daily rpt ")
				.append("    where rpt.marketplace_id = :marketplace_id ")
				.append(this.composeAsinQueryConditionForQueryHistoryLines(asins))
				.append("    group by rpt.report_date ")
				.append(") detail on list_date.date = detail.report_date ")
				.append("    order by list_date.date asc");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		if(!asins.isEmpty()) q.addValue("asins", asins);
		q.addValue("marketplace_id", marketplaceId);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);

		return columnsList;
	}
	
	private String composeAsinQueryConditionForQueryHistoryLines(List<String> asins){
		if(asins.isEmpty()) return "and false ";
		else return "and rpt.child_asin in (:asins) ";
	}

}
