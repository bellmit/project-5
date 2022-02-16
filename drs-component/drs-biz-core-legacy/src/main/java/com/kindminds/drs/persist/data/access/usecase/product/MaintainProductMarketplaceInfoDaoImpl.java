package com.kindminds.drs.persist.data.access.usecase.product;

import java.math.BigDecimal;
import java.util.List;




import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.product.AmazonAsin;
import com.kindminds.drs.persist.v1.model.mapping.product.AmazonAsinImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.product.MaintainProductMarketplaceInfoDao;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo.ProductMarketStatus;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductMarketplaceInfoImpl;

@Repository
public class MaintainProductMarketplaceInfoDaoImpl extends Dao implements MaintainProductMarketplaceInfoDao {



	@Override @SuppressWarnings("unchecked")
	public ProductMarketplaceInfo query(String skuCodeByDrs,Integer marketplaceId) {
		String sql = "select                         pmi.id as id, "
				+ "                          ps.code_by_drs as product_code_by_drs, "
				+ "                      p.name_by_supplier as product_name_by_supplier, "
				+ "                                    m.id as marketplace_id, "
				+ "                     pmi.marketplace_sku as marketplace_sku, "
				+ "                                  c.name as currency_name, "
				+ "                              pmi.status as status, "
				+ "                                pmi.msrp as msrp, "
				+ "pmi.supplier_suggested_base_retail_price as supplier_suggested_base_retail_price, "
				+ "           pmi.current_base_retail_price as current_base_retail_price, "
				+ "            pmi.estimated_drs_retainment as estimated_drs_retainment, "
				+ "                       pmi.referral_rate as referral_rate, "
				+ "          pmi.estimated_marketplace_fees as estimated_marketplace_fees, "
				+ "          pmi.estimated_fulfillment_fees as estimated_fulfillment_fees, "
				+ "               pmi.estimated_import_duty as estimated_import_duty, "
				+ "            pmi.estimated_freight_charge as estimated_freight_charge, "
				+ "         pmi.approx_supplier_net_revenue as approx_supplier_net_revenue "
				+ "from product_marketplace_info pmi "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join marketplace m on m.id = pmi.marketplace_id "
				+ "inner join currency c on c.id = m.currency_id "
				+ "where ps.code_by_drs = :skuCodeByDrs and m.id = :marketplaceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		q.addValue("marketplaceId", marketplaceId);
		//this.entityManager.clear();


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
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @Transactional("transactionManager")
	public ProductMarketplaceInfo insert(ProductMarketplaceInfo info) {
		String sql = "insert into product_marketplace_info "
				+ "(product_id, marketplace_id, marketplace_sku,  status, msrp, supplier_suggested_base_retail_price, current_base_retail_price, estimated_drs_retainment, referral_rate, estimated_marketplace_fees, estimated_fulfillment_fees, estimated_import_duty, estimated_freight_charge, approx_supplier_net_revenue ) "
				+ "select "
				+ "       p.id,           m.id, :marketplaceSku, :status,:msrp,    :supplierSuggestedBaseRetailPrice,   :currentBaseRetailPrice,  :estimatedDrsRetainment, :referralRate,  :estimatedMarketplaceFees,  :estimatedFulfillmentFees,  :estimatedImportDuty,  :estimatedFreightCharge,   :approxSupplierNetRevenue "
				+ "from product p "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join marketplace m on m.id = :marketplaceId "
				+ "where ps.code_by_drs = :skuCodeByDrs "
				+ "and m.id = :marketplaceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceSku",info.getMarketplaceSku());
		q.addValue("status",ProductMarketStatus.valueOf(info.getStatus()).getDbValue());
		q.addValue("msrp",new BigDecimal(info.getMSRP()));
		q.addValue("supplierSuggestedBaseRetailPrice",new BigDecimal(info.getSupplierSuggestedBaseRetailPrice()));
		q.addValue("currentBaseRetailPrice",new BigDecimal(info.getCurrentBaseRetailPrice()));
		q.addValue("estimatedDrsRetainment",new BigDecimal(info.getEstimatedDrsRetainment()));
		q.addValue("referralRate",new BigDecimal(info.getReferralRate()));
		q.addValue("estimatedMarketplaceFees",new BigDecimal(info.getEstimatedMarketplaceFees()));
		q.addValue("estimatedFulfillmentFees",new BigDecimal(info.getEstimatedFulfillmentFees()));
		q.addValue("estimatedImportDuty",new BigDecimal(info.getEstimatedImportDuty()));
		q.addValue("estimatedFreightCharge",new BigDecimal(info.getEstimatedFreightCharge()));
		q.addValue("approxSupplierNetRevenue",new BigDecimal(info.getApproxSupplierNetRevenue()));
		q.addValue("skuCodeByDrs", info.getProductCodeByDrs());
		q.addValue("marketplaceId", info.getMarketplace().getKey());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku inserted, affected rows: "+affectedRows);
		return null;
	}

	@Override @Transactional("transactionManager")
	public ProductMarketplaceInfo update(ProductMarketplaceInfo info) {
		String sql = "update product_marketplace_info pmi set "
				+ "(  status, msrp, supplier_suggested_base_retail_price, current_base_retail_price, estimated_drs_retainment, referral_rate, estimated_marketplace_fees, estimated_fulfillment_fees, estimated_import_duty, estimated_freight_charge, approx_supplier_net_revenue ) "
				+ "= "
				+ "( :status,:msrp,    :supplierSuggestedBaseRetailPrice,   :currentBaseRetailPrice,  :estimatedDrsRetainment, :referralRate,  :estimatedMarketplaceFees,  :estimatedFulfillmentFees,  :estimatedImportDuty,  :estimatedFreightCharge,   :approxSupplierNetRevenue )"
				+ "from product p, product_sku ps, marketplace m "
				+ "where p.id = ps.product_id and ps.code_by_drs = :skuCodeByDrs "
				+ "and pmi.marketplace_id = m.id and m.id = :marketplaceId "
				+ "and p.id = pmi.product_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status",ProductMarketStatus.valueOf(info.getStatus()).getDbValue());
		q.addValue("msrp",new BigDecimal(info.getMSRP()));
		q.addValue("supplierSuggestedBaseRetailPrice",new BigDecimal(info.getSupplierSuggestedBaseRetailPrice()));
		q.addValue("currentBaseRetailPrice",new BigDecimal(info.getCurrentBaseRetailPrice()));
		q.addValue("estimatedDrsRetainment",new BigDecimal(info.getEstimatedDrsRetainment()));
		q.addValue("referralRate",new BigDecimal(info.getReferralRate()));
		q.addValue("estimatedMarketplaceFees",new BigDecimal(info.getEstimatedMarketplaceFees()));
		q.addValue("estimatedFulfillmentFees",new BigDecimal(info.getEstimatedFulfillmentFees()));
		q.addValue("estimatedImportDuty",new BigDecimal(info.getEstimatedImportDuty()));
		q.addValue("estimatedFreightCharge",new BigDecimal(info.getEstimatedFreightCharge()));
		q.addValue("approxSupplierNetRevenue",new BigDecimal(info.getApproxSupplierNetRevenue()));
		q.addValue("skuCodeByDrs", info.getProductCodeByDrs());
		q.addValue("marketplaceId", info.getMarketplace().getKey());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one region info updated, affected rows: "+affectedRows);
		return this.query(info.getProductCodeByDrs(), info.getMarketplace().getKey());
	}

	@Override @Transactional("transactionManager")
	public boolean delete(String skuCodeByDrs,Integer marketplaceId) {
		String sql = "delete from product_marketplace_info pmi using product p, product_sku ps, marketplace m "
				+ "where p.id = ps.product_id and ps.code_by_drs = :skuCodeByDrs "
				+ "and m.id = pmi.marketplace_id and m.id = :marketplaceId "
				+ "and p.id = pmi.product_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		q.addValue("marketplaceId", marketplaceId);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one region will be deleted, affected rows: "+affectedRows);
		return true;
	}

	@Override @SuppressWarnings("unchecked")
	public String querySupplierKcodeOfProductSku(String skuCodeByDrs) {
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
	public List<Integer> queryMarketplaceIdListOfExistingMarketplaceInfo(String skuCodeByDrs) {
		String sql = "select pmi.marketplace_id from product_marketplace_info pmi "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "where ps.code_by_drs = :skuCodeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		return  getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}

	@Override @SuppressWarnings("unchecked")
	public ProductMarketplaceInfo queryForK101(String skuCodeByDrs, Integer marketplaceId) {
		String sql = "select                         pmi.id as id, "
				+ "                          ps.code_by_drs as product_code_by_drs, "
				+ "                      p.name_by_supplier as product_name_by_supplier, "
				+ "                                    m.id as marketplace_id, "
				+ "                     pmi.marketplace_sku as marketplace_sku, "
				+ "                                  c.name as currency_name, "
				+ "                              pmi.status as status, "
				+ "                                pmi.msrp as msrp, "
				+ "pmi.supplier_suggested_base_retail_price as supplier_suggested_base_retail_price, "
				+ "           pmi.current_base_retail_price as current_base_retail_price, "
				+ "            pmi.estimated_drs_retainment as estimated_drs_retainment, "
				+ "                       pmi.referral_rate as referral_rate, "
				+ "          pmi.estimated_marketplace_fees as estimated_marketplace_fees, "
				+ "          pmi.estimated_fulfillment_fees as estimated_fulfillment_fees, "
				+ "               pmi.estimated_import_duty as estimated_import_duty, "
				+ "            pmi.estimated_freight_charge as estimated_freight_charge, "
				+ "         pmi.approx_supplier_net_revenue as approx_supplier_net_revenue "
				+ "from product_k101_marketplace_info pmi "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join marketplace m on m.id = pmi.marketplace_id "
				+ "inner join currency c on c.id = m.currency_id "
				+ "where ps.code_by_drs = :skuCodeByDrs and m.id = :marketplaceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		q.addValue("marketplaceId", marketplaceId);
		//this.entityManager.clear();
		List<ProductMarketplaceInfo> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNUm)-> 	new ProductMarketplaceInfoImpl(
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
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @Transactional("transactionManager")
	public ProductMarketplaceInfo insertForK101(ProductMarketplaceInfo info) {
		String sql = "insert into product_k101_marketplace_info "
				+ "(product_id, marketplace_id, marketplace_sku,  status, msrp, supplier_suggested_base_retail_price, current_base_retail_price, estimated_drs_retainment, referral_rate, estimated_marketplace_fees, estimated_fulfillment_fees, estimated_import_duty, estimated_freight_charge, approx_supplier_net_revenue ) "
				+ "select "
				+ "       p.id,           m.id, :marketplaceSku, :status,:msrp,    :supplierSuggestedBaseRetailPrice,   :currentBaseRetailPrice,  :estimatedDrsRetainment, :referralRate,  :estimatedMarketplaceFees,  :estimatedFulfillmentFees,  :estimatedImportDuty,  :estimatedFreightCharge,   :approxSupplierNetRevenue "
				+ "from product p "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join marketplace m on m.id = :marketplaceId "
				+ "where ps.code_by_drs = :skuCodeByDrs "
				+ "and m.id = :marketplaceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceSku",info.getMarketplaceSku());
		q.addValue("status",ProductMarketStatus.valueOf(info.getStatus()).getDbValue());
		q.addValue("msrp",new BigDecimal(info.getMSRP()));
		q.addValue("supplierSuggestedBaseRetailPrice",new BigDecimal(info.getSupplierSuggestedBaseRetailPrice()));
		q.addValue("currentBaseRetailPrice",new BigDecimal(info.getCurrentBaseRetailPrice()));
		q.addValue("estimatedDrsRetainment",new BigDecimal(info.getEstimatedDrsRetainment()));
		q.addValue("referralRate",new BigDecimal(info.getReferralRate()));
		q.addValue("estimatedMarketplaceFees",new BigDecimal(info.getEstimatedMarketplaceFees()));
		q.addValue("estimatedFulfillmentFees",new BigDecimal(info.getEstimatedFulfillmentFees()));
		q.addValue("estimatedImportDuty",new BigDecimal(info.getEstimatedImportDuty()));
		q.addValue("estimatedFreightCharge",new BigDecimal(info.getEstimatedFreightCharge()));
		q.addValue("approxSupplierNetRevenue",new BigDecimal(info.getApproxSupplierNetRevenue()));
		q.addValue("skuCodeByDrs", info.getProductCodeByDrs());
		q.addValue("marketplaceId", info.getMarketplace().getKey());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one sku inserted, affected rows: "+affectedRows);
		return null;
	}

	@Override @Transactional("transactionManager")
	public ProductMarketplaceInfo updateForK101(ProductMarketplaceInfo info) {
		String sql = "update product_k101_marketplace_info pmi set "
				+ "(  status, msrp, supplier_suggested_base_retail_price, current_base_retail_price, estimated_drs_retainment, referral_rate, estimated_marketplace_fees, estimated_fulfillment_fees, estimated_import_duty, estimated_freight_charge, approx_supplier_net_revenue ) "
				+ "= "
				+ "( :status,:msrp,    :supplierSuggestedBaseRetailPrice,   :currentBaseRetailPrice,  :estimatedDrsRetainment, :referralRate,  :estimatedMarketplaceFees,  :estimatedFulfillmentFees,  :estimatedImportDuty,  :estimatedFreightCharge,   :approxSupplierNetRevenue )"
				+ "from product p, product_sku ps, marketplace m "
				+ "where p.id = ps.product_id and ps.code_by_drs = :skuCodeByDrs "
				+ "and pmi.marketplace_id = m.id and m.id = :marketplaceId "
				+ "and p.id = pmi.product_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status",ProductMarketStatus.valueOf(info.getStatus()).getDbValue());
		q.addValue("msrp",new BigDecimal(info.getMSRP()));
		q.addValue("supplierSuggestedBaseRetailPrice",new BigDecimal(info.getSupplierSuggestedBaseRetailPrice()));
		q.addValue("currentBaseRetailPrice",new BigDecimal(info.getCurrentBaseRetailPrice()));
		q.addValue("estimatedDrsRetainment",new BigDecimal(info.getEstimatedDrsRetainment()));
		q.addValue("referralRate",new BigDecimal(info.getReferralRate()));
		q.addValue("estimatedMarketplaceFees",new BigDecimal(info.getEstimatedMarketplaceFees()));
		q.addValue("estimatedFulfillmentFees",new BigDecimal(info.getEstimatedFulfillmentFees()));
		q.addValue("estimatedImportDuty",new BigDecimal(info.getEstimatedImportDuty()));
		q.addValue("estimatedFreightCharge",new BigDecimal(info.getEstimatedFreightCharge()));
		q.addValue("approxSupplierNetRevenue",new BigDecimal(info.getApproxSupplierNetRevenue()));
		q.addValue("skuCodeByDrs", info.getProductCodeByDrs());
		q.addValue("marketplaceId", info.getMarketplace().getKey());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one region info updated, affected rows: "+affectedRows);
		return this.queryForK101(info.getProductCodeByDrs(), info.getMarketplace().getKey());
	}

	@Override @Transactional("transactionManager")
	public boolean deleteForK101(String skuCodeByDrs, Integer marketplaceId) {
		String sql = "delete from product_k101_marketplace_info pmi using product p, product_sku ps, marketplace m "
				+ "where p.id = ps.product_id and ps.code_by_drs = :skuCodeByDrs "
				+ "and m.id = pmi.marketplace_id and m.id = :marketplaceId "
				+ "and p.id = pmi.product_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		q.addValue("marketplaceId", marketplaceId);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"No or More than one region will be deleted, affected rows: "+affectedRows);
		return true;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Integer> queryMarketplaceIdListOfExistingMarketplaceInfoForK101(String skuCodeByDrs) {
		String sql = "select pmi.marketplace_id from product_k101_marketplace_info pmi "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "where ps.code_by_drs = :skuCodeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCodeByDrs", skuCodeByDrs);
		return  getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}

	@Override
	public List<AmazonAsin> queryLiveAmazonAsins() {
		String sql = "select pmia.id, pmi.product_id, m.id as marketplace_id, m.\"name\" as marketplace_name, pmia.asin " +
				"from pv.product_all_marketplace_info_amazon pmia " +
				"inner join pv.product_all_marketplace_info pmi " +
				" on pmia.product_marketplace_info_id = pmi.id and pmi.supplier = pmia.supplier  " +
				"inner join marketplace m on pmi.marketplace_id = m.id " +
				"inner join product_sku ps on pmi.product_id = ps.product_id " +
				"inner join product_base pb on ps.product_base_id = pb.id " +
				" inner join company c on c.id = pb.supplier_company_id " +
		 		" where pmi.status = 'Live' and c.activated = true  and c.id  <> 44 ";


		return (List) getJdbcTemplate().query(sql, (rs,rowNUm) -> new AmazonAsinImpl(
				rs.getInt("id"),rs.getInt("product_id"),rs.getInt("marketplace_id"),
				rs.getString("marketplace_name"),rs.getString("asin")
		));
	}

	@Override
	public List<AmazonAsin> queryLiveK101AmazonAsins() {
		String sql = "select pmia.id, pmi.product_id, m.id as marketplace_id, m.\"name\" as marketplace_name, pmia.asin " +
				"from product_k101_marketplace_info_amazon pmia " +
				"inner join product_k101_marketplace_info pmi " +
				" on pmia.product_marketplace_info_id = pmi.id   " +
				"inner join marketplace m on pmi.marketplace_id = m.id " +
				"inner join product_sku ps on pmi.product_id = ps.product_id " +
				"inner join product_base pb on ps.product_base_id = pb.id " +
				" inner join company c on c.id = pb.supplier_company_id " +
				" where pmi.status = 'Live' and c.activated = true ";

		return (List) getJdbcTemplate().query(sql, (rs,rowNUm) -> new AmazonAsinImpl(
				rs.getInt("id"),rs.getInt("product_id"),rs.getInt("marketplace_id"),
				rs.getString("marketplace_name"),rs.getString("asin")
		));
	}


}
