package com.kindminds.drs.persist.data.access.usecase.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import com.kindminds.drs.api.data.access.usecase.report.ViewMonthlyStorageFeeReportDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.kindminds.drs.Country;
import com.kindminds.drs.Warehouse;


@Repository
public class ViewMonthlyStorageFeeReportDaoImpl extends Dao implements ViewMonthlyStorageFeeReportDao {


		
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryItemList(String supplierKcode, String country,int marketplaceId, String year, String month) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select                            pmi.marketplace_sku as sku, ")
				.append("                              p.name_by_supplier as sku_name, ")
				.append("                               amsf.country_code as country_code, ")
				.append("                                amsf.item_volume as item_volume, ")
				.append("                               amsf.volume_units as volume_units, ")
				.append("              sum(amsf.average_quantity_on_hand) as total_average_quantity_on_hand, ")
				.append("      sum(amsf.average_quantity_pending_removal) as total_average_quantity_pending_removal, ")
				.append("           sum(amsf.estimated_total_item_volume) as total_estimated_total_item_volume, ")
				.append("                            amsf.month_of_charge as month_of_charge, ")
				.append("                               amsf.storage_rate as storage_rate, ")
				.append("                                   amsf.currency as currency, ")
				.append("sum(round(amsf.estimated_monthly_storage_fee,2)) as total_estimated_monthly_storage_fee ")				
				.append("from product_sku ps ")
				.append("inner join product p on p.id = ps.product_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("inner join product_marketplace_info pmi on pmi.product_id = p.id ")
				.append("inner join product_marketplace_info_amazon pmia on pmi.id = pmia.product_marketplace_info_id ")
				.append("inner join amazon_monthly_storage_fee amsf on amsf.asin = pmia.asin AND amsf.fnsku = pmia.fnsku ")
				.append("where pmi.marketplace_id = :marketplaceId ")			
				.append("and pmia.storage_fee_flag = true ");
				if (supplierKcode != null && !supplierKcode.equals("all")) sqlSb.append("and splr.k_code = :supplierKcode ");
		sqlSb.append("and amsf.month_of_charge = :monthOfCharge ")
				.append("and amsf.country_code = :country ");
				 if(country.equals("US") || country.equals("CA")) sqlSb.append("and amsf.source_warehouse_id = :warehouseId ");
		sqlSb.append("group by pmi.marketplace_sku, p.name_by_supplier,amsf.item_volume,amsf.volume_units,amsf.month_of_charge,amsf.storage_rate,amsf.currency,amsf.country_code ")
				.append("order by pmi.marketplace_sku ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId",marketplaceId);
		if (supplierKcode != null && !supplierKcode.equals("all")) q.addValue("supplierKcode", supplierKcode);
		if (country.equals("US") || country.equals("CA")) q.addValue("warehouseId", Warehouse.fromCountry(Country.valueOf(country)).getKey());
		q.addValue("monthOfCharge",year+"-"+month);
		q.addValue("country",country);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal querySumOfTotalEstimatedMonthlyStorageFee(String supplierKcode, String country, int marketplaceId,
			String year, String month) {
		String sql = "select sum(round(amsf.estimated_monthly_storage_fee,2)) as total_estimated_monthly_storage_fee "
				+ "from amazon_monthly_storage_fee amsf "
				+ "inner join product_marketplace_info_amazon pmia on pmia.asin = amsf.asin AND amsf.fnsku = pmia.fnsku "
				+ "inner join product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where pmi.marketplace_id = :marketplaceId "
				+ "and amsf.country_code = :country "
				+ "and amsf.month_of_charge = :monthOfCharge "
				+ "and pmia.storage_fee_flag = true "
				+ "and splr.k_code = :supplierKcode ";
		if(country.equals("US") || country.equals("CA")) sql += "and amsf.source_warehouse_id = :warehouseId";		
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId",marketplaceId);
		q.addValue("supplierKcode", supplierKcode);		
		q.addValue("monthOfCharge",year+"-"+month);
		q.addValue("country",country);
		if (country.equals("US") || country.equals("CA")) q.addValue("warehouseId", Warehouse.fromCountry(Country.valueOf(country)).getKey());
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1,"No or more than one result found.");
		return resultList.get(0)==null?resultList.get(0):resultList.get(0).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<String> querySupplierKcodeList() {
		String sql = "select com.k_code from company com "
				+ "where com.is_supplier = TRUE and com.k_code not in (:unAvailableComKcodes) "
				+ "order by com.k_code ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		List<String> availableComKcodes = new ArrayList<String>(Arrays.asList("K101","K408","K448","K490", "K493", "K496", "K501", "K505", "K509", "K512"));
		q.addValue("unAvailableComKcodes", availableComKcodes);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryMsdcKcodeCountryMap() {
		String sql = "select msdc.k_code,c.code from company msdc inner join country c on msdc.country_id = c.id  where msdc.k_code != 'K2' and msdc.is_drs_company is TRUE order by msdc.k_code ";
		MapSqlParameterSource q = new MapSqlParameterSource();		
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,String> keyCountryMap = new TreeMap<>();
		for(Object[] columns:columnsList){
			String kcode = (String)columns[0];
			String country = (String)columns[1];
			keyCountryMap.put(kcode, country);
		}
		return keyCountryMap;
	}
	
	@Override
	public Date queryLastSettlementEnd() {
		String sql = "select max(period_end) from bill_statement ";

		return getJdbcTemplate().queryForObject(sql,Date.class);
	}
	
}