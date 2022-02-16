package com.kindminds.drs.persist.data.access.rdb;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;

@Repository
public class RevenueCalculatorDao extends Dao {
	

	@SuppressWarnings("unchecked")
	public Map<Country,Map<String,BigDecimal>> queryRevenueGroupByCountryAndProductBaseFromAmazon(Date start,Date end,String supplierKcode){
		String sql = "select m.country_id, pb.code_by_drs, sum(amount) "
				+ "from company splr "
				+ "inner join product_base pb on pb.supplier_company_id=splr.id "
				+ "inner join product_sku ps on ps.product_base_id=pb.id "
				+ "inner join product_marketplace_info pmi on pmi.product_id=ps.product_id "
				+ "inner join amazon_settlement_report_v2 asr on (asr.sku=pmi.marketplace_sku and asr.source_marketplace_id=pmi.marketplace_id) "
				+ "inner join marketplace m on m.id=pmi.marketplace_id "
				+ "where true "
				+ "and asr.posted_date_time >= :start "
				+ "and asr.posted_date_time <  :end "
				+ "and splr.k_code=:supplierKcode "
				+ "and asr.amount_description in ('Principal') "
				+ this.generateWhereConditionForIgnoring()
				+ "group by m.country_id, pb.code_by_drs "
				+ "order by m.country_id, pb.code_by_drs";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		q.addValue("supplierKcode",supplierKcode);
		Map<Country, Map<String,BigDecimal>> countryProductBaseRevenue = new HashMap<>();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] columns:columnsList){
			Integer countryId = (Integer)columns[0];
			String productBase = (String)columns[1];
			BigDecimal revenue = (BigDecimal)columns[2];
			Country country = Country.fromKey(countryId);
			if(!countryProductBaseRevenue.containsKey(country)) countryProductBaseRevenue.put(country, new HashMap<>());
			countryProductBaseRevenue.get(country).put(productBase,revenue);
		}
		return countryProductBaseRevenue;
	}
	
	private String generateWhereConditionForIgnoring(){
		List<String> ignoreMerchantOrderIdPrefixs = Arrays.asList(
				"USTTS","RUSTTS",
				"sb","Rsb",
				"ebay","Rebay",
				"eBay","ReBay");
		StringBuilder sb = new StringBuilder();
		for(String prefix:ignoreMerchantOrderIdPrefixs){
			sb.append("and (asr.merchant_order_id not like '").append(prefix).append("%' or asr.merchant_order_id is NULL ) ");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public Map<Country,Map<String,BigDecimal>> queryRevenueGroupByCountryAndProductBaseFromEbay(Date start,Date end,String supplierKcode){
		String sql = "select m.country_id, pb.code_by_drs, sum(et.pretax_price) "
				+ "from company splr "
				+ "inner join product_base pb on pb.supplier_company_id=splr.id "
				+ "inner join product_sku ps on ps.product_base_id = pb.id "
				+ "inner join ebay_transaction et on et.drs_sku = ps.code_by_drs "
				+ "inner join marketplace m on m.id = et.marketplace_id "
				+ "where true "
				+ "and et.transaction_date >= :start "
				+ "and et.transaction_date <  :end "
				+ "and splr.k_code=:supplierKcode "
				+ "group by m.country_id, pb.code_by_drs "
				+ "order by m.country_id, pb.code_by_drs";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		q.addValue("supplierKcode",supplierKcode);
		Map<Country, Map<String,BigDecimal>> countryProductBaseRevenue = new HashMap<>();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] columns:columnsList){
			Integer countryId = (Integer)columns[0];
			String productBase = (String)columns[1];
			BigDecimal revenue = (BigDecimal)columns[2];
			Country country = Country.fromKey(countryId);
			if(!countryProductBaseRevenue.containsKey(country)) countryProductBaseRevenue.put(country, new HashMap<>());
			countryProductBaseRevenue.get(country).put(productBase,revenue);
		}
		return countryProductBaseRevenue;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Country,Map<String,BigDecimal>> queryRevenueGroupByCountryAndProductBaseFromShopify(Date start,Date end,String supplierKcode){
		String sql = "select m.country_id, pb.code_by_drs, sum(sor.subtotal) "
				+ "from company splr "
				+ "inner join product_base pb on pb.supplier_company_id = splr.id "
				+ "inner join product_sku ps on ps.product_base_id = pb.id "
				+ "inner join product_marketplace_info pmi on pmi.product_id = ps.product_id "
				+ "inner join marketplace m on m.id = pmi.marketplace_id "
				+ "inner join shopify_order_report sor on sor.lineitem_sku = pmi.marketplace_sku "
				+ "inner join shopify_payment_transaction_report sptr on sptr.order_name = sor.name "
				+ "where pmi.marketplace_id = :marketplaceKey "
				+ "and sptr.transaction_date >= :start "
				+ "and sptr.transaction_date <  :end "
				+ "and splr.k_code=:supplierKcode "
				+ "group by m.country_id, pb.code_by_drs "
				+ "order by m.country_id, pb.code_by_drs";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		q.addValue("marketplaceKey",Marketplace.TRUETOSOURCE.getKey());
		q.addValue("supplierKcode",supplierKcode);
		Map<Country, Map<String,BigDecimal>> countryProductBaseRevenue = new HashMap<>();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] columns:columnsList){
			Integer countryId = (Integer)columns[0];
			String productBase = (String)columns[1];
			BigDecimal revenue = (BigDecimal)columns[2];
			Country country = Country.fromKey(countryId);
			if(!countryProductBaseRevenue.containsKey(country)) countryProductBaseRevenue.put(country, new HashMap<>());
			countryProductBaseRevenue.get(country).put(productBase,revenue);
		}
		return countryProductBaseRevenue;
	}

}
