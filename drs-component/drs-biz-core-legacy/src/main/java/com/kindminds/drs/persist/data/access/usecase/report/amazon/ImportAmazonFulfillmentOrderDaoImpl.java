package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonFulfillmentOrderDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderItem;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class ImportAmazonFulfillmentOrderDaoImpl extends Dao implements ImportAmazonFulfillmentOrderDao {
	

	
	private final String serviceName = "Fulfillment Outbound Shipment";

	@Override
	public Date queryInitialDate(String region) {
		String sql = "select date_initial "
				+ "from amazon_import_info aii "
				+ "where aii.service_name = :serviceName "
				+ "and aii.region = :region ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", this.serviceName);
		q.addValue("region",region);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override @Transactional("transactionManager")
	public void updateLastUpdateDate(String region, Date lastUpdateDate) {
		String sql = "update amazon_import_info aii set date_last_update = :date "
				+ "where service_name = :serviceName "
				+ "and aii.region = :region ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", this.serviceName);
		q.addValue("date", lastUpdateDate);
		q.addValue("region",region);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override
	public Date queryLastUpdateDate(String region) {
		String sql = "select date_last_update "
				+ "from amazon_import_info aii "
				+ "where aii.service_name = :serviceName "
				+ "and aii.region = :region ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", this.serviceName);
		q.addValue("region",region);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override @Transactional("transactionManager")
	public void setImportingStatus(String marketplace, String status) {
		String sql = "update amazon_import_info aii set status = :status  "
				+ "where aii.region = :region "
				+ "and service_name = :svcName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status", status);
		q.addValue("svcName",this.serviceName);
		q.addValue("region", marketplace);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override
	public boolean isOrderExist(String sellerOrderId) {
		String sql = "select exists( "
				+ "    select 1 from amazon_order where amazon_order_id = :sellerOrderId "
				+ ")";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sellerOrderId", sellerOrderId);
		Boolean o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		if (o == null) return false;
		return o;
	}

	@Override @SuppressWarnings("unchecked")
	public boolean isSalesChannelNull(String amazonOrderId) {
		String sql = "select sales_channel is null from amazon_order where amazon_order_id = :amazonOrderId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amazonOrderId",amazonOrderId);
		List<Boolean> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Boolean.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @Transactional("transactionManager")
	public void insert(AmazonOrder order) {
		String sql = "insert into amazon_order "
				+ "(  id,  amazon_order_id,  purchase_date,  last_update_date ,  order_status,   seller_order_id,  marketplace_id ) values "
				+ "( :id, :amazon_order_id, :purchase_date, :last_update_date , :order_status,  :seller_order_id, :marketplace_id ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		int orderDbId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"amazon_order","id");
		q.addValue("id",orderDbId);
		q.addValue("amazon_order_id",order.getAmazonOrderId());
		q.addValue("purchase_date",order.getPurchaseDate());
		q.addValue("last_update_date",order.getLastUpdateDate());
		q.addValue("order_status",order.getOrderStatus());
		q.addValue("seller_order_id",order.getSellerOrderId());
		q.addValue("marketplace_id",order.getMarketplaceId());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		for(AmazonOrderItem item:order.getItems()){
			this.insertOrderItems(orderDbId,order.getAmazonOrderId(),item);
		}
		
	}
	
	@Transactional("transactionManager")
	private void insertOrderItems(int orderDbId,String amazonOrderId,AmazonOrderItem item ){
		String sql = "insert into amazon_order_item "
				+ "(  id,  order_db_id,  amazon_order_id,  order_item_id,  quantity_ordered,  seller_sku,  gift_message_text ) values "
				+ "( :id, :order_db_id, :amazon_order_id, :order_item_id, :quantity_ordered, :seller_sku, :gift_message_text ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		int orderItemDbId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"amazon_order_item","id");
		q.addValue("id",orderItemDbId);
		q.addValue("order_db_id",orderDbId);
		q.addValue("amazon_order_id",amazonOrderId);
		q.addValue("order_item_id",item.getOrderItemId());
		q.addValue("quantity_ordered",item.getQuantityOrdered());
		q.addValue("seller_sku",item.getSellerSKU());
		q.addValue("gift_message_text",item.getGiftMessageText());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void update(AmazonOrder order) {
		String sql = "update amazon_order set "
				+ "(  purchase_date, last_update_date ,  order_status,  seller_order_id,  marketplace_id ) = "
				+ "( :purchase_date,  :last_update_date ,  :order_status, :seller_order_id, :marketplace_id ) "
				+ "where amazon_order_id = :amazonOrderId ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("amazonOrderId",order.getAmazonOrderId());
		q.addValue("purchase_date",order.getPurchaseDate());
		q.addValue("last_update_date",order.getLastUpdateDate());
		q.addValue("order_status",order.getOrderStatus());
		q.addValue("seller_order_id",order.getSellerOrderId());
		q.addValue("marketplace_id",order.getMarketplaceId());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.deleteOrderItems(order.getAmazonOrderId());
		int orderDbId = this.queryOrderDbId(order.getAmazonOrderId());
		for(AmazonOrderItem item:order.getItems()){
			this.insertOrderItems(orderDbId, order.getAmazonOrderId(),item);
		}
	}
	
	private int queryOrderDbId(String amazonOrderId){
		String sql = "select id from amazon_order where amazon_order_id = :amazonOrderId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amazonOrderId",amazonOrderId);
		List<Integer> o = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		if (o == null)
			return 0;
		return o.get(0);
	}
	
	private void deleteOrderItems(String orderId){
		String sql = " delete from amazon_order_item where amazon_order_id = :amazonOrderId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amazonOrderId",orderId);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

}
