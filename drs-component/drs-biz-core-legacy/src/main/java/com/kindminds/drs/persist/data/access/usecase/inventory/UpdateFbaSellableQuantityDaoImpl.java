package com.kindminds.drs.persist.data.access.usecase.inventory;

import java.util.Date;



import javax.xml.datatype.XMLGregorianCalendar;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.inventory.UpdateFbaSellableQuantityDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonSkuInventorySupply;

@Repository
public class UpdateFbaSellableQuantityDaoImpl extends Dao implements UpdateFbaSellableQuantityDao {
	

	
	@Override @Transactional("transactionManager")
	public void setImportingStatus(String serviceName, String marketplace, String status) {
		String sql = "update amazon_import_info aii "
				+ "set status = :status "
				+ "where aii.region = :region "
				+ "and service_name = :svcName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status", status);
		q.addValue("svcName", serviceName);
		q.addValue("region", marketplace);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override
	public Date queryInitialDate(String serviceName,String marketplace) {
		String sql = "select date_initial "
				+ "from amazon_import_info aii "
				+ "where aii.service_name = :serviceName "
				+ "and aii.region = :region ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", serviceName);
		q.addValue("region",marketplace);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override
	public Date queryLastUpdateDate(String serviceName,String marketplace) {
		String sql = "select date_last_update "
				+ "from amazon_import_info aii "
				+ "where aii.service_name = :serviceName "
				+ "and aii.region = :region ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", serviceName);
		q.addValue("region", marketplace);
		return getNamedParameterJdbcTemplate().queryForObject(sql, q, Date.class);
	}
	@Override @Transactional("transactionManager")
	public void updateLastUpdateDate(String serviceName,String marketplace, XMLGregorianCalendar date) {
		String sql = "update amazon_import_info aii set date_last_update = :date "
				+ "where service_name = :serviceName "
				+ "and aii.region = :region ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", serviceName);
		q.addValue("date", date.toGregorianCalendar().getTime());
		q.addValue("region",marketplace);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void updateAmazonInventorySupply(int marketplaceId,AmazonSkuInventorySupply amazonSkuInventorySupply, XMLGregorianCalendar date) {
		String sql = "update amazon_inventory_supply set "
				+ "   total_supply_quantity = :total_supply_quantity, "
				+ "in_stock_supply_quantity = :in_stock_supply_quantity, "
				+ " detail_quantity_inbound = :detail_quantity_inbound, "
				+ " detail_quantity_instock = :detail_quantity_instock, "
				+ "detail_quantity_transfer = :detail_quantity_transfer, "
				+ "        date_last_update = :date_last_update "
				+ "where marketplace_id = :marketplace_id "
				+ "and marketplace_sku = :marketplaceSku ";

	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplaceId);
		q.addValue("total_supply_quantity", amazonSkuInventorySupply.getTotalSupplyQuantity());
		q.addValue("in_stock_supply_quantity", amazonSkuInventorySupply.getInStockSupplyQuantity());
		q.addValue("detail_quantity_inbound", amazonSkuInventorySupply.getDetailQuantityInbound());
		q.addValue("detail_quantity_instock", amazonSkuInventorySupply.getDetailQuantityInStock());
		q.addValue("detail_quantity_transfer", amazonSkuInventorySupply.getDetailQuantityTransfer());
		q.addValue("date_last_update", date.toGregorianCalendar().getTime());
		q.addValue("marketplaceSku", amazonSkuInventorySupply.getMarketplaceSku());
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public void insertAmazonInventorySupply(int marketplaceId, AmazonSkuInventorySupply amazonSkuInventorySupply, XMLGregorianCalendar date) {
		String sql = "insert into amazon_inventory_supply "
				+ "(  marketplace_id,  marketplace_sku,  total_supply_quantity,  in_stock_supply_quantity,  detail_quantity_inbound,  detail_quantity_instock,  detail_quantity_transfer,  date_last_update) values "
				+ "( :marketplace_id, :marketplace_sku, :total_supply_quantity, :in_stock_supply_quantity, :detail_quantity_inbound, :detail_quantity_instock, :detail_quantity_transfer,  :date_last_update) ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplaceId);
		q.addValue("marketplace_sku", amazonSkuInventorySupply.getMarketplaceSku());
		q.addValue("total_supply_quantity", amazonSkuInventorySupply.getTotalSupplyQuantity());
		q.addValue("in_stock_supply_quantity", amazonSkuInventorySupply.getInStockSupplyQuantity());
		q.addValue("detail_quantity_inbound", amazonSkuInventorySupply.getDetailQuantityInbound());
		q.addValue("detail_quantity_instock", amazonSkuInventorySupply.getDetailQuantityInStock());
		q.addValue("detail_quantity_transfer", amazonSkuInventorySupply.getDetailQuantityTransfer());
		q.addValue("date_last_update", date.toGregorianCalendar().getTime());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override
	public boolean selectExist(int marketplaceKey, String marketplaceSku) {
		String sql = "select exists ( select 1 from amazon_inventory_supply where marketplace_id = :marketplace_id and marketplace_sku = :marketplaceSku ) ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplaceKey);
		q.addValue("marketplaceSku", marketplaceSku);
		Boolean result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		Assert.notNull(result);
		return (boolean)result;
	}

}
