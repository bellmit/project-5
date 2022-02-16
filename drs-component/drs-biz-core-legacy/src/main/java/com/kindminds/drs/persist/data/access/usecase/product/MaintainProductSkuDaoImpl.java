package com.kindminds.drs.persist.data.access.usecase.product;

import java.util.List;




import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.product.MaintainProductSkuDao;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.product.SKU.EAN_PROVIDER;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductSkuImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductMarketplaceInfoImpl;

@Repository
public class MaintainProductSkuDaoImpl extends Dao implements MaintainProductSkuDao {


	
	@Override @SuppressWarnings("unchecked")
	public SKU query(String skuCodeByDrs) {
		String sql = "select                 ps.id as id, "
				+ "                    splr.k_code as supplier_company_kcode, "
				+ "                 pb.code_by_drs as product_base_code, "
				+ "           pps.code_by_supplier as code_by_supplier, "
				+ "                 ps.code_by_drs as code_by_drs, "
				+ "           pps.name_by_supplier as name_by_supplier, "
				+ "                pps.name_by_drs as name_by_drs, "
				+ "                         ps.ean as ean, "
				+ "                ps.ean_provider as ean_provider, "
				+ "                       pss.name as status, "
				+ "ps.manufacturing_lead_time_days as manufacturing_lead_time_days, "
				+ " ps.contain_lithium_ion_battery as contain_lithium_ion_battery, "
				+ "             pps.internal_notes as internal_notes "
				+ "from product_sku ps "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join product ppb on ppb.id = pb.product_id "
				+ "inner join product pps on pps.id = ps.product_id "
				+ "inner join product_sku_status pss on pss.id = ps.status_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where ps.code_by_drs = :skuCodeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);


		List<ProductSkuImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductSkuImpl(
				rs.getInt("id"),rs.getString("supplier_company_kcode"),
				rs.getString("product_base_code"),rs.getString("code_by_supplier"),rs.getString("code_by_drs"),
				rs.getString("name_by_supplier"),rs.getString("name_by_drs"),rs.getString("ean"),
				rs.getString("ean_provider"),rs.getString("status"),
				rs.getString("manufacturing_lead_time_days"),rs.getBoolean("contain_lithium_ion_battery"),
				rs.getString("internal_notes")
		));

		if ((resultList == null) || (resultList.size() == 0)) return null;
		Assert.isTrue(resultList.size()==1);
		ProductSkuImpl result = resultList.get(0);
		if(this.querySupplierKcodeOfProductSku(skuCodeByDrs).equals("K101")){
			result.setRegionInfoList(this.queryProductK101SkuMarketInfoList(skuCodeByDrs));
		}else{
			result.setRegionInfoList(this.queryProductSkuMarketInfoList(skuCodeByDrs));			
		}						
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<ProductMarketplaceInfo> queryProductSkuMarketInfoList(String productCodeByDrs){
		String sql = "select    pmi.id as id, "
				+ "               NULL as product_code_by_drs, "
				+ "               NULL as product_name_by_supplier, "
				+ "               m.id as marketplace_id, "
				+ "pmi.marketplace_sku as marketplace_sku, "
				+ "               NULL as currency_name, "
				+ "         pmi.status as status, "
				+ "               NULL as msrp, "
				+ "               NULL as supplier_suggested_base_retail_price, "
				+ "               NULL as current_base_retail_price, "
				+ "               NULL as estimated_drs_retainment, "
				+ "               NULL as referral_rate, "
				+ "               NULL as estimated_marketplace_fees, "
				+ "               NULL as estimated_fulfillment_fees, "
				+ "               NULL as estimated_import_duty, "
				+ "               NULL as estimated_freight_charge, "
				+ "               NULL as approx_supplier_net_revenue, "
				+ "               NULL as max_potential_sales_per_week "
				+ "from product_marketplace_info pmi "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join marketplace m on m.id = pmi.marketplace_id "
				+ "where ps.code_by_drs = :productCodeByDrs ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productCodeByDrs", productCodeByDrs);


		List<ProductMarketplaceInfo> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) ->
				new ProductMarketplaceInfoImpl(
						rs.getInt("id"),rs.getString("product_code_by_drs"),
						rs.getString("product_name_by_supplier"),rs.getInt("marketplace_id"),
						rs.getString("marketplace_sku"),rs.getString("currency_name"),rs.getString("status"),
						rs.getBigDecimal("msrp"),rs.getBigDecimal("supplier_suggested_base_retail_price"),
						rs.getBigDecimal("current_base_retail_price"),
						rs.getBigDecimal("estimated_drs_retainment"),rs.getBigDecimal("referral_rate"),
						rs.getBigDecimal("estimated_marketplace_fees"),
						rs.getBigDecimal("estimated_fulfillment_fees"),rs.getBigDecimal("estimated_import_duty"),
						rs.getBigDecimal("estimated_freight_charge"),
						rs.getBigDecimal("approx_supplier_net_revenue")
				));

		if ((resultList == null) || (resultList.size() == 0)) return null;
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private List<ProductMarketplaceInfo> queryProductK101SkuMarketInfoList(String productCodeByDrs){
		String sql = "select    pmi.id as id, "
				+ "               NULL as product_code_by_drs, "
				+ "               NULL as product_name_by_supplier, "
				+ "               m.id as marketplace_id, "
				+ "pmi.marketplace_sku as marketplace_sku, "
				+ "               NULL as currency_name, "
				+ "         pmi.status as status, "
				+ "               NULL as msrp, "
				+ "               NULL as supplier_suggested_base_retail_price, "
				+ "               NULL as current_base_retail_price, "
				+ "               NULL as estimated_drs_retainment, "
				+ "               NULL as referral_rate, "
				+ "               NULL as estimated_marketplace_fees, "
				+ "               NULL as estimated_fulfillment_fees, "
				+ "               NULL as estimated_import_duty, "
				+ "               NULL as estimated_freight_charge, "
				+ "               NULL as approx_supplier_net_revenue, "
				+ "               NULL as max_potential_sales_per_week "
				+ "from product_k101_marketplace_info pmi "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join marketplace m on m.id = pmi.marketplace_id "
				+ "where ps.code_by_drs = :productCodeByDrs ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productCodeByDrs", productCodeByDrs);

		List<ProductMarketplaceInfo> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) ->
				new ProductMarketplaceInfoImpl(
						rs.getInt("id"),rs.getString("product_code_by_drs"),
						rs.getString("product_name_by_supplier"),rs.getInt("marketplace_id"),
						rs.getString("marketplace_sku"),rs.getString("currency_name"),rs.getString("status"),
						rs.getBigDecimal("msrp"),rs.getBigDecimal("supplier_suggested_base_retail_price"),
						rs.getBigDecimal("current_base_retail_price"),
						rs.getBigDecimal("estimated_drs_retainment"),rs.getBigDecimal("referral_rate"),
						rs.getBigDecimal("estimated_marketplace_fees"),
						rs.getBigDecimal("estimated_fulfillment_fees"),rs.getBigDecimal("estimated_import_duty"),
						rs.getBigDecimal("estimated_freight_charge"),
						rs.getBigDecimal("approx_supplier_net_revenue")
				));

		if ((resultList == null) || (resultList.size() == 0)) return null;
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	private String querySupplierKcodeOfProductSku(String skuCodeByDrs) {
		String sql = "select com.k_code from company com "
				+ "inner join product_base pb on pb.supplier_company_id = com.id "
				+ "inner join product_sku ps on ps.product_base_id = pb.id "
				+ "where ps.code_by_drs = :skuCodeByDrs ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}
		
	@Override @SuppressWarnings("unchecked")
	public List<String> queryBaseCodeList(String supplierKcode) {
		String sql = "select pb.code_by_drs "
				+ "from product_base pb "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where splr.k_code = :supplierKcode "
				+ "order by pb.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}
	
	@Override
	public boolean isProductSkuSupplierCodeExist(String supplierKcode, String skuCodeBySupplier) {
		String sql = "select exists("
				+ "    select 1 from product_sku ps "
				+ "    inner join product pps on pps.id = ps.product_id "
				+ "    inner join product_base pb on pb.id = ps.product_base_id "
				+ "    inner join company splr on splr.id = pb.supplier_company_id "
				+ "    where splr.k_code = :supplierKcode "
				+ "    and pps.code_by_supplier = :skuCodeBySupplier "
				+ ")";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("skuCodeBySupplier", skuCodeBySupplier.trim());
		Boolean o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		return o;
	}

	@Override @Transactional("transactionManager")
	public String insert(SKU sku,String drsSkuCode) {
		int pId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"product","id");
		String sql = "insert into product "
				+ "( id, name_by_drs, code_by_supplier, name_by_supplier,  internal_notes ) values "
				+ "(:id,  :nameByDrs,  :codeBySupplier,  :nameBySupplier,  :internalNotes )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", pId);
		q.addValue("nameByDrs", sku.getNameByDrs().trim());
		q.addValue("codeBySupplier", sku.getCodeBySupplier().trim());
		q.addValue("nameBySupplier", sku.getNameBySupplier().trim());
		q.addValue("internalNotes", sku.getInternalNote());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		
		int psId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"product_sku","id");
		sql = "insert into product_sku "
			+ "( id, product_base_id, code_by_drs,  ean, ean_provider, status_id, manufacturing_lead_time_days, contain_lithium_ion_battery, product_id ) "
			+ "select "
			+ " :id,           pb.id,  :codeByDrs, :ean, :eanProvider,    pss.id,   :manufacturingLeadTimeDays,   :containLithiumIonBattery, :productId "
			+ "from product_base pb "
			+ "inner join product ppb on ppb.id = pb.product_id "
			+ "inner join product_sku_status pss on pss.name = :status "
			+ "inner join company c on c.id = pb.supplier_company_id "
			+ "where pb.code_by_drs = :baseCode ";
		q = new MapSqlParameterSource();
		q.addValue("id", psId);
		q.addValue("codeByDrs", drsSkuCode.trim());
		q.addValue("ean", sku.getProductEAN().trim());
		q.addValue("eanProvider", EAN_PROVIDER.valueOf(sku.getEanProvider()).name());
		q.addValue("status", SKU.Status.valueOf(sku.getStatus()).name());
		q.addValue("manufacturingLeadTimeDays", Integer.valueOf(sku.getManufacturingLeadTimeDays()));
		q.addValue("containLithiumIonBattery", sku.getContainLithiumIonBattery());
		q.addValue("baseCode", sku.getBaseProductCode());
		q.addValue("productId", pId);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku inserted, affected rows: "+affectedRows);
		return drsSkuCode;
	}
	
	@Override @Transactional("transactionManager")
	public String update(String origDrsCode,SKU sku,String newDrsCode) {
		
		String sql = "update product p set "
			+ "( name_by_drs, code_by_supplier, name_by_supplier, internal_notes ) = "
			+ "(  :nameByDrs,  :codeBySupplier,  :nameBySupplier, :internalNotes ) "
			+ "from product_sku ps "
			+ "where p.id = ps.product_id "
			+ "and ps.code_by_drs = :originCodeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		
		q.addValue("codeBySupplier", sku.getCodeBySupplier().trim());
		q.addValue("nameByDrs", sku.getNameByDrs().trim());
		q.addValue("nameBySupplier", sku.getNameBySupplier().trim());
		q.addValue("internalNotes", sku.getInternalNote());
		q.addValue("originCodeByDrs", origDrsCode);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);
		
		sql = "update product_sku ps set "
				+ "( code_by_drs,  ean, ean_provider, manufacturing_lead_time_days, contain_lithium_ion_battery, status_id ) = "
				+ "(  :codeByDrs, :ean, :eanProvider,   :manufacturingLeadTimeDays,   :containLithiumIonBattery,    pss.id ) "
				+ "from product_sku_status pss "
				+ "where ps.code_by_drs = :originCodeByDrs "
				+ "and pss.name = :status ";
		q = new MapSqlParameterSource();
		q.addValue("codeByDrs", newDrsCode.trim());
		q.addValue("ean", sku.getProductEAN().trim());
		q.addValue("eanProvider", EAN_PROVIDER.valueOf(sku.getEanProvider()).name());
		q.addValue("manufacturingLeadTimeDays", Integer.valueOf(sku.getManufacturingLeadTimeDays()));
		q.addValue("containLithiumIonBattery", sku.getContainLithiumIonBattery());
		q.addValue("originCodeByDrs", origDrsCode);
		q.addValue("status", SKU.Status.valueOf(sku.getStatus()).name());
		affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1);

		return newDrsCode;
	}

	@Override @Transactional("transactionManager")
	public String updateSkuMltAndContainLithium(SKU sku) {
		String sql = "update product_sku ps set "
				+ "( manufacturing_lead_time_days, contain_lithium_ion_battery ) = "
				+ "(   :manufacturingLeadTimeDays,   :containLithiumIonBattery ) "
				+ "where ps.code_by_drs = :codeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("manufacturingLeadTimeDays", Integer.valueOf(sku.getManufacturingLeadTimeDays()));
		q.addValue("containLithiumIonBattery", sku.getContainLithiumIonBattery());
		q.addValue("codeByDrs", sku.getCodeByDrs());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);
		return sku.getCodeByDrs();
	}

	@Override @Transactional("transactionManager")
	public String updateSku(String drsSkuCode,SKU sku) {

		String sql = "update product p set "
				+ "( name_by_drs, code_by_supplier, name_by_supplier, internal_notes ) = "
				+ "(  :nameByDrs,  :codeBySupplier,  :nameBySupplier, :internalNotes ) "
				+ "from product_sku ps "
				+ "where p.id = ps.product_id "
				+ "and ps.code_by_drs = :drsSkuCode ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("codeBySupplier", sku.getCodeBySupplier().trim());
		q.addValue("nameByDrs", sku.getNameByDrs().trim());
		q.addValue("nameBySupplier", sku.getNameBySupplier().trim());
		q.addValue("internalNotes", sku.getInternalNote());
		q.addValue("drsSkuCode", drsSkuCode);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
//		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);

		sql = "update product_sku ps set "
				+ "( code_by_drs,  ean, ean_provider, manufacturing_lead_time_days, contain_lithium_ion_battery, status_id ) = "
				+ "(  :codeByDrs, :ean, :eanProvider,   :manufacturingLeadTimeDays,   :containLithiumIonBattery,    pss.id ) "
				+ "from product_sku_status pss "
				+ "where ps.code_by_drs = :drsSkuCode "
				+ "and pss.name = :status ";
		q = new MapSqlParameterSource();
		q.addValue("codeByDrs", drsSkuCode.trim());
		q.addValue("ean", sku.getProductEAN().trim());
		q.addValue("eanProvider", EAN_PROVIDER.valueOf(sku.getEanProvider()).name());
		q.addValue("manufacturingLeadTimeDays", Integer.valueOf(sku.getManufacturingLeadTimeDays()));
		q.addValue("containLithiumIonBattery", sku.getContainLithiumIonBattery());
		q.addValue("drsSkuCode", drsSkuCode);
		q.addValue("status", SKU.Status.valueOf(sku.getStatus()).name());
		affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
//		Assert.isTrue(affectedRows==1);

		return drsSkuCode;
	}

	@Override @Transactional("transactionManager")
	public String delete(String skuCodeByDrs) {
		
		Integer productId = this.queryProductIdByProductSku(skuCodeByDrs);

		String sql = "delete from product_sku_inventory_history where product_sku_id = (select id from product_sku where code_by_drs = :skuCodeByDrs ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);

		sql = "delete from product_sku ps where ps.code_by_drs = :skuCodeByDrs ";
		q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);
		
		sql = "delete from product p where p.id = :productId ";
		q = new MapSqlParameterSource();
		q.addValue("productId", productId);
		affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku updated, affected rows: "+affectedRows);
		return skuCodeByDrs;
	}
	
	@SuppressWarnings("unchecked")
	private Integer queryProductIdByProductSku(String skuCodeByDrs){
		String sql = "select p.id from product p inner join product_sku ps on ps.product_id = p.id where ps.code_by_drs = :codeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("codeByDrs", skuCodeByDrs);
		List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		Assert.isTrue(resultList.size()==1||resultList.size()==0);
		if(resultList.size()==0) return null;
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String querySupplierKcode(String skuCodeByDrs) {
		String sql = "select com.k_code from company com "
				+ "inner join product_base pb on pb.supplier_company_id = com.id "
				+ "inner join product_sku ps on ps.product_base_id = pb.id "
				+ "where ps.code_by_drs = :skuCodeByDrs ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}
}
