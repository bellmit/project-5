package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import com.kindminds.drs.api.v1.model.amazon.AmazonSearchTermReportRawLine;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSearchTermReportDao;


@Repository
public class ImportAmazonSearchTermReportDaoImpl extends Dao implements ImportAmazonSearchTermReportDao {
	


	@Override @Transactional("transactionManager")
	public int importReportRawLines(Marketplace marketplace,List<AmazonSearchTermReportRawLine> rawLines) {
		String sql = "insert into amazon_search_term_report "
				+ "( marketplace_id, campaign_name, ad_group_name, customer_search_term,  keyword, match_type, first_day_of_impression, last_day_of_impression,  impressions,  clicks,  ctr, total_spend, average_cpc,  acos,  currency, orders_placed_within_one_week_of_a_click, product_sales_within_one_week_of_a_click, conversion_rate_within_one_week_of_a_click, same_sku_units_ordered_within_one_week_of_click, other_sku_units_ordered_within_one_week_of_click, same_sku_units_product_sales_within_one_week_of_click, other_sku_units_product_sales_within_one_week_of_click ) values "
				+ "(:marketplace_id, :campaignName,  :adGroupName,  :customerSearchTerm, :keyword, :matchType,   :firstDayOfImpression,   :lastDayOfImpression, :impressions, :clicks, :ctr, :totalSpend, :averageCpc, :acos, :currency,       :ordersPlacedWithinOneWeekOfAClick,       :productSalesWithinOneWeekOfAClick,       :conversionRateWithinOneWeekOfAClick,        :sameSkuUnitsOrderedWithinOneWeekOfClick,        :otherSkuUnitsOrderedWithinOneWeekOfClick,         :sameSkuUnitsProductSalesWithinOneWeekOfClick,         :otherSkuUnitsProductSalesWithinOneWeekOfClick ) ";
		int insertedRows = 0;
		for(AmazonSearchTermReportRawLine line:rawLines){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("marketplace_id",marketplace.getKey());
			q.addValue("campaignName",line.getCampaignName());
			q.addValue("adGroupName",line.getAdGroupName());
			q.addValue("customerSearchTerm",line.getCustomerSearchTerm());
			q.addValue("keyword",line.getKeyword());
			q.addValue("matchType",line.getMatchType());
			q.addValue("firstDayOfImpression",line.getFirstDayOfImpression());
			q.addValue("lastDayOfImpression",line.getLastDayOfImpression());
			q.addValue("impressions",line.getImpressions());
			q.addValue("clicks",line.getClicks());
			q.addValue("ctr",line.getCtr());
			q.addValue("totalSpend",line.getTotalSpend());
			q.addValue("averageCpc",line.getAverageCpc());
			q.addValue("acos",line.getAcos());
			q.addValue("currency",line.getCurrency());
			q.addValue("ordersPlacedWithinOneWeekOfAClick",line.getOrdersPlacedWithinOneWeekOfAClick());
			q.addValue("productSalesWithinOneWeekOfAClick",line.getProductSalesWithinOneWeekOfAClick());
			q.addValue("conversionRateWithinOneWeekOfAClick",line.getConversionRateWithinOneWeekOfAClick());
			q.addValue("sameSkuUnitsOrderedWithinOneWeekOfClick",line.getSameSkuUnitsOrderedWithinOneWeekOfClick());
			q.addValue("otherSkuUnitsOrderedWithinOneWeekOfClick",line.getOtherSkuUnitsOrderedWithinOneWeekOfClick());
			q.addValue("sameSkuUnitsProductSalesWithinOneWeekOfClick",line.getSameSkuUnitsProductSalesWithinOneWeekOfClick());
			q.addValue("otherSkuUnitsProductSalesWithinOneWeekOfClick",line.getOtherSkuUnitsProductSalesWithinOneWeekOfClick());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
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

	@Override
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
		String sql = "delete from amazon_search_term_report_campaign_name_supplier_map " +
				" where marketplace_id = :marketplace_id  ";
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
	public void insertCampaignNameList(Marketplace marketplace,Map<String,Integer> campaignNameToSupplierId) {
		Assert.isTrue(campaignNameToSupplierId.size()>=1);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("insert into amazon_search_term_report_campaign_name_supplier_map ");
		sqlSb.append("(campaign_name, supplier_company_id, marketplace_id) values ");
		int parameterIndex=0;
		int size = campaignNameToSupplierId.size();
		for(String campaignName:campaignNameToSupplierId.keySet()){
			Integer supplierCompanyId = campaignNameToSupplierId.get(campaignName);
			String supplierCompanyIdAsString = supplierCompanyId==null?"null":supplierCompanyId.toString();
			sqlSb.append("(").append("?").append(parameterIndex).append(",").append(supplierCompanyIdAsString).append(",").append(marketplace.getKey()).append(")");
			if(parameterIndex<size-1) sqlSb.append(", ");
			parameterIndex++;
		}
		MapSqlParameterSource q = new MapSqlParameterSource();
		parameterIndex=0;
		for(String campaignName:campaignNameToSupplierId.keySet()){
			q.addValue(Integer.toString(parameterIndex),campaignName);
			parameterIndex++;
		}
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q)==size);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryCampaignNameToSupplierKcodeMap(Marketplace marketplace) {
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
	public void updateSupplierOfCampaignName(Marketplace marketplace,String campaignName, String supplierKcode) {
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
