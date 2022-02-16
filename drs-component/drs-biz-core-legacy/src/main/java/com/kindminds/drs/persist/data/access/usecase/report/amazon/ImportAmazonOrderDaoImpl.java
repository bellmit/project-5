package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


import com.kindminds.drs.Country;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonOrderDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderItem;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderPaymentExecDetail;
import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonOrderImpl;
import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonOrderItemImpl;

@Repository
public class ImportAmazonOrderDaoImpl extends Dao implements ImportAmazonOrderDao {

	

	@Override @Transactional("transactionManager")
	public void setImportingStatus(Country country, String status) {
		String sql = "update amazon_import_info aii set status = :status  "
				+ "where aii.region = :region "
				+ "and service_name = :svcName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status", status);
		q.addValue("svcName", "Orders");
		q.addValue("region", country.name());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void insertOrUpdateOrders(List<AmazonOrder> orders) {

		for(AmazonOrder order:orders){
			if(!this.orderExist(order.getAmazonOrderId())){
				this.insertOrder(order);
			}
			else {
				this.updateOrder(order);
			}
		}
	}
	
	private Boolean orderExist(String amazonOrderId){
		String sql = "select count(1) from amazon_order where amazon_order_id = :amazonOrderId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amazonOrderId", amazonOrderId);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) return null;

		Assert.isTrue( o<=1 );
		return o==1;
	}

	@Transactional("transactionManager")
	private void insertOrder(AmazonOrder order){
		String sql = "insert into amazon_order "
				+ "(  id, amazon_order_id, purchase_date, last_update_date, order_status, fulfillment_channel, sales_channel, number_of_items_shipped, number_of_items_unshipped, seller_order_id, ship_service_level, payment_method, marketplace_id, buyer_email, buyer_name, shipment_service_level_category, order_type, earliest_shipDate, latest_shipDate, tfm_shipment_status, shipped_by_amazon_tfm, cba_displayable_shipping_label, earliest_delivery_date, latest_delivery_date, order_channel, address_countryCode, address_line1, address_line2, address_line3, address_stateOrRegion, address_postalCode, address_phone, address_city, address_county, address_district, address_name, order_total_currency_code, order_total_amount ) values "
				+ "( :id,  :amazonOrderId, :purchaseDate,  :lastUpdateDate, :orderStatus, :fulfillmentChannel, :salesChannel, " +
				"  :numberOfItemsShipped,   :numberOfItemsUnshipped,  :sellerOrderId,  :shipServiceLevel, " +
				":paymentMethod, :marketplaceId, :buyerEmail, :buyerName,   :shipmentServiceLevelCategory, " +
				":orderType, :earliestShipDate, :latestShipDate,  :tfmShipmentStatus,   :shippedByAmazonTfm,   :cbaDisplayableShippingLabel,  :earliestDeliveryDate,  :latestDeliveryDate, :orderChannel, :addressCountryCode, :addressLine1, :addressLine2, :addressLine3, :addressStateOrRegion, :addressPostalCode, :addressPhone, :addressCity, :addressCounty, :addressDistrict, :addressName,   :orderTotalCurrencyCode,  :orderTotalAmount ) ";
	MapSqlParameterSource q = new MapSqlParameterSource();

		int orderDbId = this.getNextVal("amazon_order", "id");
		q.addValue("id",orderDbId);
		q.addValue("amazonOrderId",order.getAmazonOrderId());
		q.addValue("purchaseDate",order.getPurchaseDate());
		q.addValue("lastUpdateDate",order.getLastUpdateDate());
		q.addValue("orderStatus",order.getOrderStatus());
		q.addValue("fulfillmentChannel",order.getFulfillmentChannel());
		q.addValue("salesChannel",order.getSalesChannel());
		q.addValue("numberOfItemsShipped",order.getNumberOfItemsShipped());
		q.addValue("numberOfItemsUnshipped",order.getNumberOfItemsUnshipped());
		q.addValue("sellerOrderId",order.getSellerOrderId());
		q.addValue("shipServiceLevel",order.getShipServiceLevel());
		q.addValue("paymentMethod",order.getPaymentMethod());
		q.addValue("marketplaceId",order.getMarketplaceId());
		q.addValue("buyerEmail",order.getBuyerEmail());
		q.addValue("buyerName",order.getBuyerName());
		q.addValue("shipmentServiceLevelCategory",order.getShipmentServiceLevelCategory());
		q.addValue("orderType",order.getOrderType());
		q.addValue("earliestShipDate",order.getEarliestShipDate());
		q.addValue("latestShipDate",order.getLatestShipDate());
		q.addValue("tfmShipmentStatus",order.getTFMShipmentStatus());
		q.addValue("shippedByAmazonTfm",order.getShippedByAmazonTFM());
		q.addValue("cbaDisplayableShippingLabel",order.getCbaDisplayableShippingLabel());
		q.addValue("earliestDeliveryDate",order.getEarliestDeliveryDate());
		q.addValue("latestDeliveryDate",order.getLatestDeliveryDate());
		q.addValue("orderChannel",order.getOrderChannel());
		q.addValue("addressCountryCode",order.getAddressCountryCode());
		q.addValue("addressLine1",order.getAddressLine1());
		q.addValue("addressLine2",order.getAddressLine2());
		q.addValue("addressLine3",order.getAddressLine3());
		q.addValue("addressStateOrRegion",order.getAddressStateOrRegion());
		q.addValue("addressPostalCode",order.getAddressPostalCode());
		q.addValue("addressPhone",order.getAddressPhone());
		q.addValue("addressCity",order.getAddressCity());
		q.addValue("addressCounty",order.getAddressCounty());
		q.addValue("addressDistrict",order.getAddressDistrict());
		q.addValue("addressName",order.getAddressName());
		q.addValue("orderTotalCurrencyCode",order.getOrderTotalCurrencyCode()==null?null:order.getOrderTotalCurrencyCode().name());
		q.addValue("orderTotalAmount",order.getOrderTotalAmount());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		for(AmazonOrderPaymentExecDetail detail:order.getPaymentExecutionDetail()){
			this.insertOrderPaymentExecDetail(orderDbId, order.getAmazonOrderId(), detail);
		}
		for(AmazonOrderItem item:order.getItems()){
			this.insertOrderItems(orderDbId, order.getAmazonOrderId(), item);
		}
	}

	@Transactional("transactionManager")
	private void insertOrderPaymentExecDetail(int orderDbId,String amazonOrderId,AmazonOrderPaymentExecDetail item ){
		String sql = "insert into amazon_order_payment_exec_detail "
				+ "( order_db_id, amazon_order_id, payment_method, currency_code,  amount ) values "
				+ "(  :orderDbId,  :amazonOrderId, :paymentMethod, :currencyCode, :amount ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("orderDbId",orderDbId);
		q.addValue("amazonOrderId",orderDbId);
		q.addValue("paymentMethod", item.getPaymentMethod());
		q.addValue("currencyCode", item.getCurrency().name());
		q.addValue("amount", item.getAmount());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Transactional("transactionManager")
	private void insertOrderItems(int orderDbId,String amazonOrderId,AmazonOrderItem item ){
		String sql = "insert into amazon_order_item "
				+ "(  id, order_db_id, amazon_order_id, order_item_id, quantity_ordered, quantity_shipped,  title, seller_sku,  asin, item_price_currency_code, item_price_amount, shipping_price_currency_code, shipping_price_amount, gift_wrap_price_currency_code, gift_wrap_price_amount, item_tax_currency_code, item_tax_amount, shipping_tax_currency_code, shipping_tax_amount, giftWrap_tax_currency_code, giftWrap_tax_amount, shipping_discount_currency_code, shipping_discount_amount, promotion_discount_currency_code, promotion_discount_amount, cod_fee_currency_code, cod_fee_amount, cod_fee_discount_currency_code, cod_fee_discount_amount, gift_message_text, gift_wrap_level, invoice_requirement, invoice_category_buyer_selected, invoice_title, invoice_information, condition_note, condition_id, condition_subtype_id, scheduled_delivery_start_date, scheduled_delivery_end_date ) values "
				+ "( :id,  :orderDbId,  :amazonOrderId,  :orderItemId, :quantityOrdered, :quantityShipped, :title, :sellerSku, :asin,   :itemPriceCurrencyCode,  :itemPriceAmount,   :shippingPriceCurrencyCode,  :shippingPriceAmount, :giftWrapPriceCurrencyCode, :giftWrapPriceAmount, :itemTaxCurrencyCode, :itemTaxAmount, :shippingTaxCurrencyCode, :shippingTaxAmount, :giftWrapTaxCurrencyCode, :giftWrapTaxAmount, :shippingDiscountCurrencyCode, :shippingDiscountAmount, :promotionDiscountCurrencyCode, :promotionDiscountAmount, :codFeeCurrencyCode, :codFeeAmount, :codFeeDiscountCurrencyCode, :codFeeDiscountAmount, :giftMessageText, :giftWrapLevel, :invoiceRequirement, :invoiceCategoryBuyerSelected, :invoiceTitle, :invoiceInformation, :conditionNote, :conditionId, :conditionSubtypeId, :scheduledDeliveryStartDate, :scheduledDeliveryEndDate ) ";
	MapSqlParameterSource q = new MapSqlParameterSource();

		int orderItemDbId = this.getNextVal("amazon_order_item", "id");
		q.addValue("id",orderItemDbId);
		q.addValue("orderDbId",orderDbId);
		q.addValue("amazonOrderId",amazonOrderId);
		q.addValue("orderItemId",item.getOrderItemId());
		q.addValue("quantityOrdered",item.getQuantityOrdered());
		q.addValue("quantityShipped",item.getQuantityShipped());
		q.addValue("title",item.getTitle());
		q.addValue("sellerSku",item.getSellerSKU());
		q.addValue("asin",item.getASIN());
		q.addValue("itemPriceCurrencyCode",item.getItemPriceCurrency()==null?null:item.getItemPriceCurrency().name());
		q.addValue("itemPriceAmount",item.getItemPriceAmount());
		q.addValue("shippingPriceCurrencyCode",item.getShippingPriceCurrency()==null?null:item.getShippingPriceCurrency().name());
		q.addValue("shippingPriceAmount",item.getShippingPriceAmount());
		q.addValue("giftWrapPriceCurrencyCode",item.getGiftWrapPriceCurrency()==null?null:item.getGiftWrapPriceCurrency().name());
		q.addValue("giftWrapPriceAmount",item.getGiftWrapPriceAmount());
		q.addValue("itemTaxCurrencyCode",item.getItemTaxCurrency()==null?null:item.getItemTaxCurrency().name());
		q.addValue("itemTaxAmount",item.getItemTaxAmount());
		q.addValue("shippingTaxCurrencyCode",item.getShippingTaxCurrency()==null?null:item.getShippingTaxCurrency().name());
		q.addValue("shippingTaxAmount",item.getShippingTaxAmount());
		q.addValue("giftWrapTaxCurrencyCode",item.getGiftWrapTaxCurrency()==null?null:item.getGiftWrapTaxCurrency().name());
		q.addValue("giftWrapTaxAmount",item.getGiftWrapTaxAmount());
		q.addValue("shippingDiscountCurrencyCode",item.getShippingDiscountCurrency()==null?null:item.getShippingDiscountCurrency().name());
		q.addValue("shippingDiscountAmount",item.getShippingDiscountAmount());
		q.addValue("promotionDiscountCurrencyCode",item.getPromotionDiscountCurrency()==null?null:item.getPromotionDiscountCurrency().name());
		q.addValue("promotionDiscountAmount",item.getPromotionDiscountAmount());
		q.addValue("codFeeCurrencyCode",item.getCODFeeCurrency()==null?null:item.getCODFeeCurrency().name());
		q.addValue("codFeeAmount",item.getCODFeeAmount());
		q.addValue("codFeeDiscountCurrencyCode",item.getCODFeeDiscountCurrency()==null?null:item.getCODFeeDiscountCurrency().name());
		q.addValue("codFeeDiscountAmount",item.getCODFeeDiscountAmount());
		q.addValue("giftMessageText",item.getGiftMessageText());
		q.addValue("giftWrapLevel",item.getGiftWrapLevel());
		q.addValue("invoiceRequirement",item.getInvoiceRequirement());
		q.addValue("invoiceCategoryBuyerSelected",item.getInvoiceCategoryBuyerSelected());
		q.addValue("invoiceTitle",item.getInvoiceTitle());
		q.addValue("invoiceInformation",item.getInvoiceInformation());
		q.addValue("conditionNote",item.getConditionNote());
		q.addValue("conditionId",item.getConditionId());
		q.addValue("conditionSubtypeId",item.getConditionSubtypeId());
		q.addValue("scheduledDeliveryStartDate",item.getScheduledDeliveryStartDate());
		q.addValue("scheduledDeliveryEndDate",item.getScheduledDeliveryEndDate());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		for(String promotionId:item.getPromotionIds()){
			this.insertPromotionId(orderItemDbId, item.getOrderItemId(), promotionId);
		}
	}

	@Transactional("transactionManager")
	private void insertPromotionId(int orderItemDbId,String orderItemId,String item ){
		String sql = "insert into amazon_order_item_promotion_id "
				+ "( order_item_db_id, order_item_id, promotion_id ) values "
				+ "(   :orderItemDbId,  :orderItemId, :promotionId ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("orderItemDbId",orderItemDbId);
		q.addValue("orderItemId",orderItemId);
		q.addValue("promotionId", item);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Transactional("transactionManager")
	private void updateOrder(AmazonOrder order){
		String sql = "update amazon_order set "
				+ "(   amazon_order_id, purchase_date, last_update_date, order_status, fulfillment_channel, sales_channel, number_of_items_shipped, number_of_items_unshipped, seller_order_id, ship_service_level, payment_method, marketplace_id, buyer_email, buyer_name, shipment_service_level_category, order_type, earliest_shipDate, latest_shipDate, tfm_shipment_status, shipped_by_amazon_tfm, cba_displayable_shipping_label, earliest_delivery_date, latest_delivery_date, order_channel, address_countryCode, address_line1, address_line2, address_line3, address_stateOrRegion, address_postalCode, address_phone, address_city, address_county, address_district, address_name, order_total_currency_code, order_total_amount ) = "
				+ "(    :amazonOrderId, :purchaseDate,  :lastUpdateDate, :orderStatus, :fulfillmentChannel, :salesChannel,   :numberOfItemsShipped,   :numberOfItemsUnshipped,  :sellerOrderId,  :shipServiceLevel, :paymentMethod, :marketplaceId, :buyerEmail, :buyerName,   :shipmentServiceLevelCategory, :orderType, :earliestShipDate, :latestShipDate,  :tfmShipmentStatus,   :shippedByAmazonTfm,   :cbaDisplayableShippingLabel,  :earliestDeliveryDate,  :latestDeliveryDate, :orderChannel, :addressCountryCode, :addressLine1, :addressLine2, :addressLine3, :addressStateOrRegion, :addressPostalCode, :addressPhone, :addressCity, :addressCounty, :addressDistrict, :addressName,   :orderTotalCurrencyCode,  :orderTotalAmount ) "
				+ "where amazon_order_id = :amazonOrderId ";
	MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("amazonOrderId",order.getAmazonOrderId());
		q.addValue("purchaseDate",order.getPurchaseDate());
		q.addValue("lastUpdateDate",order.getLastUpdateDate());
		q.addValue("orderStatus",order.getOrderStatus());
		q.addValue("fulfillmentChannel",order.getFulfillmentChannel());
		q.addValue("salesChannel",order.getSalesChannel());
		q.addValue("numberOfItemsShipped",order.getNumberOfItemsShipped());
		q.addValue("numberOfItemsUnshipped",order.getNumberOfItemsUnshipped());
		q.addValue("sellerOrderId",order.getSellerOrderId());
		q.addValue("shipServiceLevel",order.getShipServiceLevel());
		q.addValue("paymentMethod",order.getPaymentMethod());
		q.addValue("marketplaceId",order.getMarketplaceId());
		q.addValue("buyerEmail",order.getBuyerEmail());
		q.addValue("buyerName",order.getBuyerName());
		q.addValue("shipmentServiceLevelCategory",order.getShipmentServiceLevelCategory());
		q.addValue("orderType",order.getOrderType());
		q.addValue("earliestShipDate",order.getEarliestShipDate());
		q.addValue("latestShipDate",order.getLatestShipDate());
		q.addValue("tfmShipmentStatus",order.getTFMShipmentStatus());
		q.addValue("shippedByAmazonTfm",order.getShippedByAmazonTFM());
		q.addValue("cbaDisplayableShippingLabel",order.getCbaDisplayableShippingLabel());
		q.addValue("earliestDeliveryDate",order.getEarliestDeliveryDate());
		q.addValue("latestDeliveryDate",order.getLatestDeliveryDate());
		q.addValue("orderChannel",order.getOrderChannel());
		q.addValue("addressCountryCode",order.getAddressCountryCode());
		q.addValue("addressLine1",order.getAddressLine1());
		q.addValue("addressLine2",order.getAddressLine2());
		q.addValue("addressLine3",order.getAddressLine3());
		q.addValue("addressStateOrRegion",order.getAddressStateOrRegion());
		q.addValue("addressPostalCode",order.getAddressPostalCode());
		q.addValue("addressPhone",order.getAddressPhone());
		q.addValue("addressCity",order.getAddressCity());
		q.addValue("addressCounty",order.getAddressCounty());
		q.addValue("addressDistrict",order.getAddressDistrict());
		q.addValue("addressName",order.getAddressName());
		q.addValue("orderTotalCurrencyCode",order.getOrderTotalCurrencyCode()==null?null:order.getOrderTotalCurrencyCode().name());
		q.addValue("orderTotalAmount",order.getOrderTotalAmount());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		
		this.deleteExpiredData(order.getAmazonOrderId());

		int orderDbId = this.queryOrderDbId(order.getAmazonOrderId());
		for(AmazonOrderPaymentExecDetail detail:order.getPaymentExecutionDetail()){
			this.insertOrderPaymentExecDetail(orderDbId, order.getAmazonOrderId(), detail);
		}
		for(AmazonOrderItem item:order.getItems()){
			this.insertOrderItems(orderDbId, order.getAmazonOrderId(), item);
		}
	}
	
	@Transactional("transactionManager")
	private void deleteExpiredData(String amazonOrderId){
		String sql = "delete from amazon_order_payment_exec_detail where amazon_order_id = :amazonOrderId ; "
				+ "   delete from amazon_order_item_promotion_id aoipi "
				+ "       USING amazon_order_item aoi, amazon_order ao "
				+ "       where aoipi.order_item_id = aoi.order_item_id "
				+ "       and aoi.amazon_order_id = :amazonOrderId ; "
				+ "   delete from amazon_order_item where amazon_order_id = :amazonOrderId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amazonOrderId",amazonOrderId);
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
//	@SuppressWarnings("unchecked")
//	private List<String> queryPromotionIds(String orderItemId){
//		String sql = "SELECT promotion_id from amazon_order_item_promotion_id where order_item_id = :orderItemId ";
//		Query query = entityManager.createNativeQuery(sql);
//		query.addValue("orderItemId", orderItemId);
//		return (List<String>)query.getResultList();
//	}

	private int queryOrderDbId(String amazonOrderId){
		String sql = "select id from amazon_order where amazon_order_id = :amazonOrderId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amazonOrderId",amazonOrderId);
		List<Integer> o = null;
		try{
			 o = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		}catch (EmptyResultDataAccessException ex){

		}

		if (o == null)
			return 0;
		return o.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<AmazonOrder> selectOrders(Date lastUpdatedAfter, Date lastUpdatedBefore) {
		String sql = "select               ao.id as id, "
				+ "           ao.amazon_order_id as amazon_order_id, "
				+ "           ao.seller_order_id as seller_order_id, "
				+ "             ao.purchase_date as purchase_date, "
				+ "          ao.last_update_date as last_update_date, "
				+ "              ao.order_status as order_status, "
				+ "       ao.fulfillment_channel as fulfillment_channel, "
				+ "             ao.sales_channel as sales_channel, "
				+ "   ao.number_of_items_shipped as number_of_items_shipped, "
				+ " ao.number_of_items_unshipped as number_of_items_unshipped "
				+ "from amazon_order ao "
				+ "where ao.last_update_date >= :lastUpdatedAfter "
				+ "and   ao.last_update_date <= :lastUpdatedBefore ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("lastUpdatedAfter", lastUpdatedAfter);
		q.addValue("lastUpdatedBefore", lastUpdatedBefore);


		List<AmazonOrder> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new AmazonOrderImpl(
				rs.getInt("id"), rs.getString("amazon_order_id"),  rs.getString("seller_order_id"),
				rs.getTimestamp("purchase_date"),rs.getTimestamp("last_update_date"),
				rs.getString("orderStatus"),rs.getString("fulfillment_channel"),
				rs.getString("sales_channel"),rs.getInt("number_of_items_shipped"),rs.getInt("number_of_items_unshipped")
				));

		for(AmazonOrder order:resultList){
			AmazonOrderImpl o = (AmazonOrderImpl)order;
			o.setItems(this.selectOrderItems(o.getDbId()));
		}
		if(resultList.size()==0) return null;
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	private List<AmazonOrderItem> selectOrderItems(int parentDbId){
		String sql = "select      id as id, "
				+ "             asin as asin, "
				+ "       seller_sku as seller_sku, "
				+ "    order_item_id as order_item_id, "
				+ " quantity_ordered as quantity_ordered, "
				+ " quantity_shipped as quantity_shipped "
				+ "from amazon_order_item aoi "
				+ "where aoi.parent_id = :parentDbId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("parentDbId", parentDbId);


		List<AmazonOrderItem> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)->
				new AmazonOrderItemImpl(rs.getInt("id"),rs.getString("asin"),rs.getString("seller_sku"),
						rs.getString("order_item_id"),
				rs.getInt("quantity_ordered"),rs.getInt("quantity_shipped")));
		if(resultList.size()==0) return null;
		return resultList;
	}
	
	private int getNextVal(String tableName, String serialName) {
		String sql = "select nextval(pg_get_serial_sequence( :tableName, :serialName ))";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tableName", tableName);
		q.addValue("serialName", serialName);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null)
			return 0;
		return o;
	}
	
	public Date fromAmazonDateFormat(String dateStr) {
		if (!StringUtils.hasText(dateStr))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date date = null;
		try {
			date = sdf.parse(dateStr.replaceAll("Z$", "+0000"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	@Override @Transactional("transactionManager")
	public void updateLastUpdateDate(Country country, XMLGregorianCalendar date) {
		String sql = "update amazon_import_info aii set date_last_update = :date "
				+ "where service_name = :serviceName "
				+ "and aii.region = :region ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", "Orders");
		q.addValue("date", date.toGregorianCalendar().getTime());
		q.addValue("region",country.name());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override
	public XMLGregorianCalendar queryInitialDateUtc(Country country) {
		String sql = "select date_initial "
				+ "from amazon_import_info aii "
				+ "where aii.service_name = :serviceName "
				+ "and aii.region = :region ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", "Orders");
		q.addValue("region",country.name());
		Date date = getNamedParameterJdbcTemplate().queryForObject(sql,q, Date.class);
		return this.toXMLGregorianCalendar(date,TimeZone.getTimeZone("UTC"));
	}

	@Override
	public XMLGregorianCalendar queryLastUpdateDateUtc(Country country) {
		String sql = "select date_last_update "
				+ "from amazon_import_info aii "
				+ "where aii.service_name = :serviceName "
				+ "and aii.region = :region ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serviceName", "Orders");
		q.addValue("region",country.name());
		Date date =  getNamedParameterJdbcTemplate().queryForObject(sql,q, Date.class);
		if(date==null) return null;
		return this.toXMLGregorianCalendar(date,TimeZone.getTimeZone("UTC"));
	}
	
	private XMLGregorianCalendar toXMLGregorianCalendar(Date date, TimeZone timezone){
		try {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(date);
			c.setTimeZone(timezone);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
}
