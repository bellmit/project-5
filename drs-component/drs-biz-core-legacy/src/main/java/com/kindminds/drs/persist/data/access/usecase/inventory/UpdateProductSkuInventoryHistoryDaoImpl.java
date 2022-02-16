package com.kindminds.drs.persist.data.access.usecase.inventory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.inventory.UpdateProductSkuInventoryHistoryDao;
import com.kindminds.drs.api.v1.model.product.ProductSkuInventoryHistory;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderStatus;

@Repository
public class UpdateProductSkuInventoryHistoryDaoImpl  extends Dao implements UpdateProductSkuInventoryHistoryDao {
	

	
	@Override @SuppressWarnings("unchecked")
	public List<Integer> queryMarketplaceIds(int warehouseId) {
		String sql = "select id from marketplace m where m.warehouse_id = :warehouseId order by id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("warehouseId",warehouseId);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Integer> queryProductSkuIds(List<Integer> marketplaceIds){
		String sql = "select distinct ps.id "
				+ "from product_sku ps "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join product_marketplace_info pmi on pmi.product_id = p.id "
				+ "where pmi.marketplace_id in (:marketplaceIds) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceIds",marketplaceIds);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Integer, Integer> queryFbaQtyInStockPlusFbaQtyTransfer(Marketplace marketplace) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("ps.id, ")
				.append("ais.detail_quantity_instock+ais.detail_quantity_transfer ")
				.append("from amazon_inventory_supply ais ")
				.append("inner join product_marketplace_info pmi on (pmi.marketplace_id=ais.marketplace_id and pmi.marketplace_sku=ais.marketplace_sku) ")
				.append("inner join product_sku ps on ps.product_id = pmi.product_id ")
				.append("inner join product_sku_status pssts on pssts.id = ps.status_id ")
				.append("where pmi.marketplace_id = :marketplaceKey ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceKey", marketplace.getKey());
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Map<Integer,Integer> skuQty = new HashMap<Integer,Integer>();
		for(Object[] items:result){
			skuQty.put((Integer)items[0],(Integer)items[1]);
		}
		return skuQty;
	}

	@Override
	public Date queryLatestStatementPeriodEnd(String supplierKcode) {
		String sql = "select max(period_end) "
				+ "from bill_statement bs "
				+ "inner join company splr on splr.id = bs.receiving_company_id "
				+ "where splr.k_code = :supplierKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		Date result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
		Assert.notNull(result);
		return result;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Integer,Integer> queryAmazonQtyOrdered(Marketplace marketplace, Date purchaseDateFrom, Date purchaseDateTo) {
		Assert.isTrue(marketplace.isAmazonMarketplace());
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("ps.id, ")
				.append("coalesce(sum(aoi.quantity_ordered),0) ")
				.append("from product_marketplace_info pmi ")
				.append("inner join marketplace m on m.id = pmi.marketplace_id ")
				.append("inner join product_sku ps on ps.product_id = pmi.product_id ")
				.append("inner join product_sku_status pss on pss.id = ps.status_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("inner join amazon_order_item aoi on aoi.seller_sku = pmi.marketplace_sku ")
				.append("inner join amazon_order ao on (ao.id = aoi.order_db_id and ao.sales_channel = m.name) ")
				.append("where true ")
				.append("and pmi.marketplace_id = :marketplaceId ")
				.append("and pss.name = :activeStatusName ")
				.append("and ao.sales_channel = :marketplaceName ")
				.append("and ao.order_status != :canceledStatus ");
		if(purchaseDateFrom!=null) sqlSb.append("and ao.purchase_date >= :purchaseDateFrom ");
		if(purchaseDateTo!=null) sqlSb.append("and ao.purchase_date < :purchaseDateTo ");
		sqlSb.append("group by ps.id ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplace.getKey());
		q.addValue("canceledStatus", AmazonOrderStatus.CANCELED.getStrValue());
		q.addValue("activeStatusName",SKU.Status.SKU_ACTIVE.name() );
		q.addValue("marketplaceName",marketplace.getName());
		if(purchaseDateFrom!=null) q.addValue("purchaseDateFrom", purchaseDateFrom);
		if(purchaseDateTo!=null) q.addValue("purchaseDateTo", purchaseDateTo);
		Map<Integer,Integer> serviceItemKeyToDrsNameMap = new HashMap<Integer,Integer>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			serviceItemKeyToDrsNameMap.put((Integer)items[0], ((Long)items[1]).intValue());
		}
		return serviceItemKeyToDrsNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Integer,Integer> queryShopifyQtyOrdered(Marketplace marketplace, Date createDateFrom, Date createDateTo) {
		Assert.isTrue(marketplace.isShopifyMarketplace());
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("ps.id, ")
				.append("coalesce(sum(soli.quantity),0) ")
				.append("from product_marketplace_info pmi ")
				.append("inner join product pps on pps.id = pmi.product_id ")
				.append("inner join product_sku ps on ps.product_id = pps.id ")
				.append("inner join product_sku_status pss on pss.id = ps.status_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("inner join shopify_order_line_item soli on soli.sku = pmi.marketplace_sku ")
				.append("inner join shopify_order so on so.id = soli.shopify_order_id ")
				.append("where true ")
				.append("and pmi.marketplace_id = :marketplaceId ")
				.append("and pss.name = :activeStatusName ")
				.append("and so.financial_status = :paidStatus ");
		if(createDateFrom!=null) sqlSb.append("and so.created_at >= :createDateFrom ");
		if(createDateTo!=null) sqlSb.append("and so.created_at < :createDateTo ");
		sqlSb.append("group by ps.id ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplace.getKey());
		q.addValue("paidStatus", "paid");
		q.addValue("activeStatusName",SKU.Status.SKU_ACTIVE.name() );
		if(createDateFrom!=null) q.addValue("createDateFrom", createDateFrom);
		if(createDateTo!=null) q.addValue("createDateTo", createDateTo);
		Map<Integer,Integer> serviceItemKeyToDrsNameMap = new HashMap<Integer,Integer>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			serviceItemKeyToDrsNameMap.put((Integer)items[0], ((Long)items[1]).intValue());
		}
		return serviceItemKeyToDrsNameMap;
	}

	@Override @Transactional("transactionManager")
	public void insertHistoryList(List<ProductSkuInventoryHistory> histories) {
		String sql = "insert into product_sku_inventory_history "
				+ "(  warehouse_id,  product_sku_id,  snapshot_date,  qty_in_stock,  qty_sold ) values "
				+ "( :warehouse_id, :product_sku_id, :snapshot_date, :qty_in_stock, :qty_sold )";
		for(ProductSkuInventoryHistory history:histories){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("warehouse_id", history.getWarehouseId());
			q.addValue("product_sku_id", history.getProductSkuId());
			q.addValue("snapshot_date", history.getSnapshotDate());
			q.addValue("qty_in_stock", history.getQtyInStock());
			q.addValue("qty_sold", history.getQtySold());
			int insertedRows = getNamedParameterJdbcTemplate().update(sql,q);
			Assert.isTrue(insertedRows==1);
		}
	}

}
