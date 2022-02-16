package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;





import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSponsoredProductsSearchTermReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonSponsoredProductsSearchTermReportRawItem;

@Repository
public class ImportAmazonSponsoredProductsSearchTermReportDaoImpl extends Dao implements ImportAmazonSponsoredProductsSearchTermReportDao{


	
	@Override @Transactional("transactionManager")
	public int importReportRawLines(Marketplace marketplace, List<AmazonSponsoredProductsSearchTermReportRawItem> rawLines) {
		String sql = "insert into amazon_search_term_report "
				+ "( marketplace_id, campaign_name, ad_group_name, customer_search_term,  keyword, match_type, first_day_of_impression, last_day_of_impression,  impressions,  clicks,  ctr, total_spend, average_cpc,  acos,  currency, orders_placed_within_one_week_of_a_click, product_sales_within_one_week_of_a_click, conversion_rate_within_one_week_of_a_click, same_sku_units_ordered_within_one_week_of_click, other_sku_units_ordered_within_one_week_of_click, same_sku_units_product_sales_within_one_week_of_click, other_sku_units_product_sales_within_one_week_of_click,  portfolio_name,  date_updated ) values "
				+ "(:marketplace_id, :campaignName,  :adGroupName,  :customerSearchTerm, :keyword, :matchType,   :firstDayOfImpression,   :lastDayOfImpression, :impressions, :clicks, :ctr, :totalSpend, :averageCpc, :acos, :currency,       :ordersPlacedWithinOneWeekOfAClick,       :productSalesWithinOneWeekOfAClick,       :conversionRateWithinOneWeekOfAClick,        :sameSkuUnitsOrderedWithinOneWeekOfClick,        :otherSkuUnitsOrderedWithinOneWeekOfClick,         :sameSkuUnitsProductSalesWithinOneWeekOfClick,         :otherSkuUnitsProductSalesWithinOneWeekOfClick, :portfolio_name, :date_updated ) ";
		int insertedRows = 0;
		Date today = new Date();
		for(AmazonSponsoredProductsSearchTermReportRawItem line:rawLines){
			MapSqlParameterSource query = new MapSqlParameterSource();

			query.addValue("marketplace_id",marketplace.getKey());
			query.addValue("campaignName",line.getCampaignName());
			query.addValue("adGroupName",line.getAdGroupName());
			query.addValue("customerSearchTerm",line.getCustomerSearchTerm());
			query.addValue("keyword",line.getKeyword());
			query.addValue("matchType",line.getMatchType());
			query.addValue("firstDayOfImpression",line.getStartDate());
			query.addValue("lastDayOfImpression",line.getEndDate());
			query.addValue("impressions",line.getImpressions());
			query.addValue("clicks",line.getClicks());
			query.addValue("ctr",line.getCtr());
			query.addValue("totalSpend",line.getSpend());
			query.addValue("averageCpc",line.getCpc());
			query.addValue("acos",line.getAcos());
			query.addValue("currency",line.getCurrency());
			query.addValue("ordersPlacedWithinOneWeekOfAClick",line.getSevenDayTotalOrders());
			query.addValue("productSalesWithinOneWeekOfAClick",line.getSevenDayTotalSales());
			query.addValue("conversionRateWithinOneWeekOfAClick",line.getSevenDayConversionRate());
			query.addValue("sameSkuUnitsOrderedWithinOneWeekOfClick",line.getSevenDayAdvertisedSkuUnits());
			query.addValue("otherSkuUnitsOrderedWithinOneWeekOfClick",line.getSevenDayOtherSkuUnits());
			query.addValue("sameSkuUnitsProductSalesWithinOneWeekOfClick",line.getSevenDayAdvertisedSkuSales());
			query.addValue("otherSkuUnitsProductSalesWithinOneWeekOfClick",line.getSevenDayOtherSkuSales());
			query.addValue("portfolio_name", line.getPortfolioName());
			query.addValue("date_updated", today);
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,query)==1);
			insertedRows++;			
		}				
		return insertedRows;
	}

	@Override @Transactional("transactionManager")
	public void deleteDCPExistingReportLine(Marketplace marketplace) {
		String sql = "delete from amazon_search_term_report " +
				"  where marketplace_id = :marketplace_id and is_dcp = '1' ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void deleteDCPExistingReportLineByDate(Marketplace marketplace, Date startDate, Date endDate) {
		String sql = "delete from amazon_search_term_report " +
				"  where marketplace_id = :marketplace_id and is_dcp = '1' " +
				" and  ( first_day_of_impression >= :sd and last_day_of_impression  <= :ed)" ;
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());

		q.addValue("sd",Timestamp.valueOf(startDate.toInstant()
				.atOffset(ZoneOffset.UTC).toLocalDateTime()));
		q.addValue("ed",Timestamp.valueOf(endDate.toInstant()
				.atOffset(ZoneOffset.UTC).toLocalDateTime()));

		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void deleteExistingReportLine(Marketplace marketplace, String kcode) {
		String sql = "delete from amazon_search_term_report " +
				"  where marketplace_id = :marketplace_id " +
				" and  UPPER(campaign_name) like concat('[',:supplierKcode,'%') ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());
		q.addValue("supplierKcode", kcode);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void deleteExistingCampaignNameSupplierMap(Marketplace marketplace) {
		String sql = "delete from amazon_search_term_report_campaign_name_supplier_map where marketplace_id = :marketplace_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryDistinctCampaignNameList(Marketplace marketplace) {
		String sql = "select distinct astr.campaign_name from amazon_search_term_report astr where astr.marketplace_id = :marketplace_id order by astr.campaign_name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @Transactional("transactionManager")
	public void insertCampaignNameList(Marketplace marketplace, Map<String, Integer> campaignNameToSupplierId) {

		Assert.isTrue(campaignNameToSupplierId.size()>=1);

		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("insert into amazon_search_term_report_campaign_name_supplier_map ");
		sqlSb.append("(campaign_name, supplier_company_id, marketplace_id) values " +
				" (:campaign_name, :supplier_company_id, :marketplace_id) ");

		for(String campaignName:campaignNameToSupplierId.keySet()){

			MapSqlParameterSource query = new MapSqlParameterSource();

			Integer supplierCompanyId = campaignNameToSupplierId.get(campaignName);

			query.addValue("campaign_name",campaignName);
			query.addValue("supplier_company_id",supplierCompanyId);
			query.addValue("marketplace_id",marketplace.getKey());

			Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),query)==1);
		}


	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryCampaignNameToSupplierKcodeMap(Marketplace marketplace) {
		String sql = "select acsm.campaign_name, splr.k_code "
				+ "from amazon_search_term_report_campaign_name_supplier_map acsm "
				+ "left join company splr on splr.id = acsm.supplier_company_id "
				+ "where acsm.marketplace_id = :marketplace_id "
				+ "order by splr.k_code ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());
		Map<String,String> campaignNameToSupplierKcodeMap = new TreeMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result)
			campaignNameToSupplierKcodeMap.put((String)items[0], (String)items[1]);
		return campaignNameToSupplierKcodeMap;
	}

	@Override @Transactional("transactionManager")
	public void updateSupplierOfCampaignName(Marketplace marketplace, String campaignName, String supplierKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("update amazon_search_term_report_campaign_name_supplier_map ")
				.append("set supplier_company_id = splr.id ")
				.append("from company splr ")
				.append("where campaign_name = :campaignName ")
				.append("and marketplace_id = :marketplace_id ")
				.append("and splr.k_code = :supplierKcode ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("campaignName", campaignName);
		q.addValue("marketplace_id", marketplace.getKey());
		q.addValue("supplierKcode", supplierKcode);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q)==1);
	}

	@Override @Transactional("transactionManager")
	public void clearBelongedSupplierOfCampaignName(Marketplace marketplace) {
		String sql = "update amazon_search_term_report_campaign_name_supplier_map set supplier_company_id = NULL where marketplace_id = :marketplace_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public Integer queryCompanyId(String probablyKcode) {
		String sql="select id from company where k_code=:probablyKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("probablyKcode", probablyKcode);
		List<Integer> results = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		Assert.isTrue(results.size()<=1);
		if(results.isEmpty()) return null;
		return results.get(0);
	}

}
