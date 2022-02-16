package com.kindminds.drs.persist.data.access.usecase.report.shopify;

import java.util.Date;
import java.util.List;




import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.report.shopify.ImportShopifyOrderDao;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrder;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderLineItem;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderShippingLine;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class ImportShopifyOrderDaoImpl extends Dao implements ImportShopifyOrderDao {



	@Override
	public boolean isExist(String orderId) {
		String sql = "select exists(select 1 from shopify_order so where so.order_id = :order_id )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("order_id", orderId);
		Boolean result = null;
		try{
			result =  getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		} catch (EmptyResultDataAccessException e) {
		}

		Assert.notNull(result);
		return result.booleanValue();
	}
	
	@Override @Transactional("transactionManager")
	public void insert(ShopifyOrder order) {
		String tableName = "shopify_order";
		StringBuilder sqlSb = new StringBuilder()
				.append("insert into ").append(tableName)
				.append("( id,  order_id,  email,  closed_at,  created_at,  updated_at,  number,  note,  token,  gateway,  test,  total_price,  subtotal_price,  total_weight,  total_tax,  taxes_included,  currency_id,  financial_status,  confirmed,  total_discounts,  total_line_items_price,  cart_token,  buyer_accepts_marketing,  name,  referring_site,  landing_site,  cancelled_at,  cancel_reason,  total_priceUsd,  checkout_token,  reference,  userId,  location_id,  source_identifier,  source_url,  processed_at,  device_id,  phone,  customer_locale,  app_id,  browser_ip,  landing_site_ref,  order_number,  json_discount_applications,  json_discount_codes,  json_note_attributes,  json_payment_gatewayNames,  processing_method,  checkout_id,  source_name,  fulfillment_status,  json_tax_lines,  tags,  contact_email,  order_status_url,  presentment_currency,   json_total_line_items_price_set,   json_total_discounts_set,  json_total_shipping_price_set,  json_subtotal_price_set,   json_total_price_set,   json_total_tax_set,   total_tip_received,  admin_graphql_api_id,  json_billing_address,  json_shipping_address,  json_fulfillments,  json_client_details,  json_refunds,  json_payment_details,  json_customer) values ")
				.append("(:id, :order_id, :email, :closed_at, :created_at, :updated_at, :number, :note, :token, :gateway, :test, :total_price, :subtotal_price, :total_weight, :total_tax, :taxes_included, :currency_id, :financial_status, :confirmed, :total_discounts, :total_line_items_price, :cart_token, :buyer_accepts_marketing, :name, :referring_site, :landing_site, :cancelled_at, :cancel_reason, :total_priceUsd, :checkout_token, :reference, :userId, :location_id, :source_identifier, :source_url, :processed_at, :device_id, :phone, :customer_locale, :app_id, :browser_ip, :landing_site_ref, :order_number, :json_discount_applications, :json_discount_codes, :json_note_attributes, :json_payment_gatewayNames, :processing_method, :checkout_id, :source_name, :fulfillment_status, :json_tax_lines, :tags, :contact_email, :order_status_url, :presentment_currency,  :json_total_line_items_price_set,  :json_total_discounts_set, :json_total_shipping_price_set, :json_subtotal_price_set,  :json_total_price_set,  :json_total_tax_set,  :total_tip_received, :admin_graphql_api_id, :json_billing_address, :json_shipping_address, :json_fulfillments, :json_client_details, :json_refunds, :json_payment_details, :json_customer) ");
		MapSqlParameterSource q = new MapSqlParameterSource();

		int orderDbId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(), tableName, "id");
		q.addValue("id",orderDbId);
		q.addValue("order_id",order.getOrderId());
		q.addValue("email",order.getEmail());
		q.addValue("closed_at",order.getClosedAt());
		q.addValue("created_at",order.getCreatedAt());
		q.addValue("updated_at",order.getUpdatedAt());
		q.addValue("number",order.getNumber());
		q.addValue("note",order.getNote());
		q.addValue("token",order.getToken());
		q.addValue("gateway",order.getGateway());
		q.addValue("test",order.getTest());
		q.addValue("total_price",order.getTotalPrice());
		q.addValue("subtotal_price",order.getSubtotalPrice());
		q.addValue("total_weight",order.getTotalWeight());
		q.addValue("total_tax",order.getTotalTax());
		q.addValue("taxes_included",order.getTaxesIncluded());
		q.addValue("currency_id",order.getCurrency().getKey());
		q.addValue("financial_status",order.getFinancialStatus());
		q.addValue("confirmed",order.getConfirmed());
		q.addValue("total_discounts",order.getTotalDiscounts());
		q.addValue("total_line_items_price",order.getTotalLineItemsPrice());
		q.addValue("cart_token",order.getCartToken());
		q.addValue("buyer_accepts_marketing",order.getBuyerAcceptsMarketing());
		q.addValue("name",order.getName());
		q.addValue("referring_site",order.getReferringSite());
		q.addValue("landing_site",order.getLandingSite());
		q.addValue("cancelled_at",order.getCancelledAt());
		q.addValue("cancel_reason",order.getCancelReason());
		q.addValue("total_priceUsd",order.getTotalPriceUsd());
		q.addValue("checkout_token",order.getCheckoutToken());
		q.addValue("reference",order.getReference());
		q.addValue("userId",order.getUserId());
		q.addValue("location_id",order.getLocationId());
		q.addValue("source_identifier",order.getSourceIdentifier());
		q.addValue("source_url",order.getSourceUrl());
		q.addValue("processed_at",order.getProcessedAt());
		q.addValue("device_id",order.getDeviceId());
		q.addValue("phone",order.getPhone());
		q.addValue("customer_locale",order.getCustomerLocale());
		q.addValue("app_id",order.getAppId());
		q.addValue("browser_ip",order.getBrowserIp());
		q.addValue("landing_site_ref",order.getLandingSiteRef());
		q.addValue("order_number",order.getOrderNumber());
		q.addValue("json_discount_applications",order.getJsonDiscountApplications());
		q.addValue("json_discount_codes",order.getJsonDiscountCodes());
		q.addValue("json_note_attributes",order.getJsonNoteAttributes());
		q.addValue("json_payment_gatewayNames",order.getJsonPaymentGatewayNames());
		q.addValue("processing_method",order.getProcessingMethod());
		q.addValue("checkout_id",order.getCheckoutId());
		q.addValue("source_name",order.getSourceName());
		q.addValue("fulfillment_status",order.getFulfillmentStatus());
		q.addValue("json_tax_lines",order.getJsonTaxLines());
		q.addValue("tags",order.getTags());
		q.addValue("contact_email",order.getContactEmail());
		q.addValue("order_status_url",order.getOrderStatusUrl());
		q.addValue("presentment_currency",order.getPresentmentCurrency());
		q.addValue("json_total_line_items_price_set",order.getJsonTotalLineItemsPriceSet());
		q.addValue("json_total_discounts_set",order.getJsonTotalDiscountsSet());
		q.addValue("json_total_shipping_price_set",order.getJsonTotalShippingPriceSet());
		q.addValue("json_subtotal_price_set",order.getJsonSubtotalPriceSet());
		q.addValue("json_total_price_set",order.getJsonTotalPriceSet());
		q.addValue("json_total_tax_set",order.getJsonTotalTaxSet());
		q.addValue("total_tip_received", order.getTotalTipReceived());
		q.addValue("admin_graphql_api_id", order.getAdminGraphqlApiId());
		q.addValue("json_billing_address",order.getJsonBillingAddress());
		q.addValue("json_shipping_address",order.getJsonShippingAddress());
		q.addValue("json_fulfillments",order.getJsonFulfillments());
		q.addValue("json_client_details",order.getJsonClientDetails());
		q.addValue("json_refunds",order.getJsonRefunds());
		q.addValue("json_payment_details",order.getJsonPaymentDetails());
		q.addValue("json_customer",order.getJsonCustomer());
		int affectedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
		Assert.isTrue(affectedRows==1);
		this.insertLineItems(orderDbId,order.getLineItems());
		this.insertShippingLines(orderDbId, order.getShippingLines());
	}
	
	private void insertLineItems(int orderDbId, List<ShopifyOrderLineItem> lineItems){
		String tableName = "shopify_order_line_item";
		StringBuilder sqlSb = new StringBuilder()
				.append("insert into ").append(tableName)
				.append("( id, shopify_order_id,  line_item_id,  variant_id,  title,  quantity,  price,  grams,  sku,  variant_title,  vendor,  fulfillment_service,  product_id,  requires_shipping,  taxable,  gift_card,  name,  variant_inventory_management,  json_properties,  product_exists,  fulfillable_quantity,  total_discount,  fulfillment_status,   json_price_set,  json_total_discount_set,  json_discount_allocations,  admin_graphql_api_id,  json_tax_lines,  json_origin_location,  json_destination_location) values ")
				.append("(:id,:shopify_order_id, :line_item_id, :variant_id, :title, :quantity, :price, :grams, :sku, :variant_title, :vendor, :fulfillment_service, :product_id, :requires_shipping, :taxable, :gift_card, :name, :variant_inventory_management, :json_properties, :product_exists, :fulfillable_quantity, :total_discount, :fulfillment_status,  :json_price_set, :json_total_discount_set, :json_discount_allocations, :admin_graphql_api_id, :json_tax_lines, :json_origin_location, :json_destination_location) ");
		for(ShopifyOrderLineItem lineItem:lineItems){
			MapSqlParameterSource q = new MapSqlParameterSource();
			int lineItemDbId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),tableName,"id");
			q.addValue("id",lineItemDbId);
			q.addValue("shopify_order_id",orderDbId);
			q.addValue("line_item_id",lineItem.getLineItemId());
			q.addValue("variant_id",lineItem.getVariantId());
			q.addValue("title",lineItem.getTitle());
			q.addValue("quantity",lineItem.getQuantity());
			q.addValue("price",lineItem.getPrice());
			q.addValue("grams",lineItem.getGrams());
			q.addValue("sku",lineItem.getSku());
			q.addValue("variant_title",lineItem.getVariantTitle());
			q.addValue("vendor",lineItem.getVendor());
			q.addValue("fulfillment_service",lineItem.getFulfillmentService());
			q.addValue("product_id",lineItem.getProductId());
			q.addValue("requires_shipping",lineItem.getRequiresShipping());
			q.addValue("taxable",lineItem.getTaxable());
			q.addValue("gift_card",lineItem.getGiftCard());
			q.addValue("name",lineItem.getName());
			q.addValue("variant_inventory_management",lineItem.getVariantInventoryManagement());
			q.addValue("json_properties",lineItem.getJsonProperties());
			q.addValue("product_exists",lineItem.getProductExists());
			q.addValue("fulfillable_quantity",lineItem.getFulfillableQuantity());
			q.addValue("total_discount",lineItem.getTotalDiscount());
			q.addValue("fulfillment_status",lineItem.getFulfillmentStatus());
			q.addValue("json_price_set",lineItem.getJsonPriceSet());
			q.addValue("json_total_discount_set",lineItem.getJsonTotalDiscountSet());
			q.addValue("json_discount_allocations",lineItem.getJsonDiscountAllocations());
			q.addValue("admin_graphql_api_id",lineItem.getAdminGraphqlApiId());
			q.addValue("json_tax_lines",lineItem.getJsonTaxLines());
			q.addValue("json_origin_location",lineItem.getJsonOriginLocation());
			q.addValue("json_destination_location",lineItem.getJsonDestinationLocation());
			int affectedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
			Assert.isTrue(affectedRows==1);
		}
	}
	
	private void insertShippingLines(int orderDbId, List<ShopifyOrderShippingLine> shippingLines){
		String tableName = "shopify_order_shipping_line";
		StringBuilder sqlSb = new StringBuilder()
				.append("insert into ").append(tableName)
				.append("( id,  shopify_order_id,  shipping_line_id,  title,  price,  code,  source,  phone,  requested_fulfillment_service_id,  delivery_category,  carrier_identifier,  discounted_price,  json_price_set,  json_discounted_price_set,  json_discount_allocations,  json_tax_lines) values ")
				.append("(:id, :shopify_order_id, :shipping_line_id, :title, :price, :code, :source, :phone, :requested_fulfillment_service_id, :delivery_category, :carrier_identifier, :discounted_price, :json_price_set, :json_discounted_price_set, :json_discount_allocations, :json_tax_lines) ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		for(ShopifyOrderShippingLine shippingLine:shippingLines){
			 q = new MapSqlParameterSource();
			int shippingLineDbId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),tableName,"id");
			q.addValue("id",shippingLineDbId);
			q.addValue("shopify_order_id",orderDbId);
			q.addValue("shipping_line_id",shippingLine.getShippingLineId());
			q.addValue("title",shippingLine.getTitle());
			q.addValue("price",shippingLine.getPrice());
			q.addValue("code",shippingLine.getCode());
			q.addValue("source",shippingLine.getSource());
			q.addValue("phone",shippingLine.getPhone());
			q.addValue("requested_fulfillment_service_id",shippingLine.getRequestedFulfillmentServiceId());
			q.addValue("delivery_category",shippingLine.getDeliveryCategory());
			q.addValue("carrier_identifier",shippingLine.getCarrierIdentifier());
			q.addValue("discounted_price",shippingLine.getDiscountedPrice());
			q.addValue("json_price_set",shippingLine.getJsonPriceSet());
			q.addValue("json_discounted_price_set",shippingLine.getJsonDiscountedPriceSet());
			q.addValue("json_discount_allocations",shippingLine.getJsonDiscountAllocations());
			q.addValue("json_tax_lines",shippingLine.getJsonTaxLines());
			int affectedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
			Assert.isTrue(affectedRows==1);
		}
	}

	@Override
	public Date queryInitialDate(Marketplace marketplace,String serviceName) {
		String sql = "select date_initial from shopify_import_info sii where sii.marketplace_id = :marketplaceKey and sii.service_name = :serviceName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceKey", marketplace.getKey());
		q.addValue("serviceName", serviceName);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}
	
	@Override
	public Date queryLastUpdateDate(Marketplace marketplace,String serviceName) {
		String sql = "select date_last_update from shopify_import_info sii where sii.marketplace_id = :marketplaceKey and sii.service_name = :serviceName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceKey", marketplace.getKey());
		q.addValue("serviceName", serviceName);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override @Transactional("transactionManager")
	public void setImportingStatus(Marketplace marketplace,String serviceName,String status) {
		String sql = "update shopify_import_info sii set status = :status where sii.marketplace_id = :marketplaceKey and sii.service_name = :serviceName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status", status);
		q.addValue("marketplaceKey", marketplace.getKey());
		q.addValue("serviceName", serviceName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void update(ShopifyOrder order) {
		int orderDbId = this.queryOrderDbId(order.getOrderId());
		this.deleteLineItems(orderDbId);
		this.deleteShippingLines(orderDbId);
		StringBuilder sqlSb = new StringBuilder()
				.append("update shopify_order so set ")
				.append("( email,  closed_at,  created_at,  updated_at,  number,  note,  token,  gateway,  test,  total_price,  subtotal_price,  total_weight,  total_tax,  taxes_included,  currency_id,  financial_status,  confirmed,  total_discounts,  total_line_items_price,  cart_token,  buyer_accepts_marketing,  name,  referring_site,  landing_site,  cancelled_at,  cancel_reason,  total_priceUsd,  checkout_token,  reference,  userId,  location_id,  source_identifier,  source_url,  processed_at,  device_id,  phone,  customer_locale,  app_id,  browser_ip,  landing_site_ref,  order_number,   json_discount_applications,   json_discount_codes,  json_note_attributes,  json_payment_gatewayNames,  processing_method,  checkout_id,  source_name,  fulfillment_status,  json_tax_lines,  tags,  contact_email,  order_status_url,  presentment_currency,   json_total_line_items_price_set,   json_total_discounts_set,  json_total_shipping_price_set,  json_subtotal_price_set,   json_total_price_set,   json_total_tax_set,   total_tip_received,  admin_graphql_api_id,  json_billing_address,  json_shipping_address,  json_fulfillments,  json_client_details,  json_refunds,  json_payment_details,  json_customer) = ")
				.append("(:email, :closed_at, :created_at, :updated_at, :number, :note, :token, :gateway, :test, :total_price, :subtotal_price, :total_weight, :total_tax, :taxes_included, :currency_id, :financial_status, :confirmed, :total_discounts, :total_line_items_price, :cart_token, :buyer_accepts_marketing, :name, :referring_site, :landing_site, :cancelled_at, :cancel_reason, :total_priceUsd, :checkout_token, :reference, :userId, :location_id, :source_identifier, :source_url, :processed_at, :device_id, :phone, :customer_locale, :app_id, :browser_ip, :landing_site_ref, :order_number,  :json_discount_applications,  :json_discount_codes, :json_note_attributes, :json_payment_gatewayNames, :processing_method, :checkout_id, :source_name, :fulfillment_status, :json_tax_lines, :tags, :contact_email, :order_status_url, :presentment_currency,  :json_total_line_items_price_set,  :json_total_discounts_set, :json_total_shipping_price_set, :json_subtotal_price_set,  :json_total_price_set,  :json_total_tax_set,  :total_tip_received, :admin_graphql_api_id, :json_billing_address, :json_shipping_address, :json_fulfillments, :json_client_details, :json_refunds, :json_payment_details, :json_customer) ")
				.append("where so.order_id = :order_id ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("order_id",order.getOrderId());
		q.addValue("email",order.getEmail());
		q.addValue("closed_at",order.getClosedAt());
		q.addValue("created_at",order.getCreatedAt());
		q.addValue("updated_at",order.getUpdatedAt());
		q.addValue("number",order.getNumber());
		q.addValue("note",order.getNote());
		q.addValue("token",order.getToken());
		q.addValue("gateway",order.getGateway());
		q.addValue("test",order.getTest());
		q.addValue("total_price",order.getTotalPrice());
		q.addValue("subtotal_price",order.getSubtotalPrice());
		q.addValue("total_weight",order.getTotalWeight());
		q.addValue("total_tax",order.getTotalTax());
		q.addValue("taxes_included",order.getTaxesIncluded());
		q.addValue("currency_id",order.getCurrency().getKey());
		q.addValue("financial_status",order.getFinancialStatus());
		q.addValue("confirmed",order.getConfirmed());
		q.addValue("total_discounts",order.getTotalDiscounts());
		q.addValue("total_line_items_price",order.getTotalLineItemsPrice());
		q.addValue("cart_token",order.getCartToken());
		q.addValue("buyer_accepts_marketing",order.getBuyerAcceptsMarketing());
		q.addValue("name",order.getName());
		q.addValue("referring_site",order.getReferringSite());
		q.addValue("landing_site",order.getLandingSite());
		q.addValue("cancelled_at",order.getCancelledAt());
		q.addValue("cancel_reason",order.getCancelReason());
		q.addValue("total_priceUsd",order.getTotalPriceUsd());
		q.addValue("checkout_token",order.getCheckoutToken());
		q.addValue("reference",order.getReference());
		q.addValue("userId",order.getUserId());
		q.addValue("location_id",order.getLocationId());
		q.addValue("source_identifier",order.getSourceIdentifier());
		q.addValue("source_url",order.getSourceUrl());
		q.addValue("processed_at",order.getProcessedAt());
		q.addValue("device_id",order.getDeviceId());
		q.addValue("phone",order.getPhone());
		q.addValue("customer_locale",order.getCustomerLocale());
		q.addValue("app_id",order.getAppId());
		q.addValue("browser_ip",order.getBrowserIp());
		q.addValue("landing_site_ref",order.getLandingSiteRef());
		q.addValue("order_number",order.getOrderNumber());
		q.addValue("json_discount_applications",order.getJsonDiscountApplications());
		q.addValue("json_discount_codes",order.getJsonDiscountCodes());
		q.addValue("json_note_attributes",order.getJsonNoteAttributes());
		q.addValue("json_payment_gatewayNames",order.getJsonPaymentGatewayNames());
		q.addValue("processing_method",order.getProcessingMethod());
		q.addValue("checkout_id",order.getCheckoutId());
		q.addValue("source_name",order.getSourceName());
		q.addValue("fulfillment_status",order.getFulfillmentStatus());
		q.addValue("json_tax_lines",order.getJsonTaxLines());
		q.addValue("tags",order.getTags());
		q.addValue("contact_email",order.getContactEmail());
		q.addValue("order_status_url",order.getOrderStatusUrl());
		q.addValue("presentment_currency",order.getPresentmentCurrency());
		q.addValue("json_total_line_items_price_set",order.getJsonTotalLineItemsPriceSet());
		q.addValue("json_total_discounts_set",order.getJsonTotalDiscountsSet());
		q.addValue("json_total_shipping_price_set",order.getJsonTotalShippingPriceSet());
		q.addValue("json_subtotal_price_set",order.getJsonSubtotalPriceSet());
		q.addValue("json_total_price_set",order.getJsonTotalPriceSet());
		q.addValue("json_total_tax_set",order.getJsonTotalTaxSet());
		q.addValue("total_tip_received", order.getTotalTipReceived());
		q.addValue("admin_graphql_api_id",order.getAdminGraphqlApiId());
		q.addValue("json_billing_address",order.getJsonBillingAddress());
		q.addValue("json_shipping_address",order.getJsonShippingAddress());
		q.addValue("json_fulfillments",order.getJsonFulfillments());
		q.addValue("json_client_details",order.getJsonClientDetails());
		q.addValue("json_refunds",order.getJsonRefunds());
		q.addValue("json_payment_details",order.getJsonPaymentDetails());
		q.addValue("json_customer",order.getJsonCustomer());
		int affectedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
		Assert.isTrue(affectedRows>=1);
		this.insertLineItems(orderDbId,order.getLineItems());
		this.insertShippingLines(orderDbId, order.getShippingLines());
	}
	
	private int queryOrderDbId(String orderId){
		String sql = "select id from shopify_order so where so.order_id = :order_id  ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("order_id",orderId);
		List<Integer> result = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		Assert.notNull(result);
		return result.get(0);
	}
	
	private void deleteShippingLines(int orderDbId){
		String sql = "delete from shopify_order_shipping_line where shopify_order_id = :shopify_order_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shopify_order_id",orderDbId);
		getNamedParameterJdbcTemplate().update(sql.toString(),q);
	}
	
	private void deleteLineItems(int orderDbId){
		String sql = "delete from shopify_order_line_item where shopify_order_id = :shopify_order_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shopify_order_id",orderDbId);
		getNamedParameterJdbcTemplate().update(sql.toString(),q);
	}

	@Override @Transactional("transactionManager")
	public void updateLastUpdateDate(Marketplace marketplace, String serviceName, Date date) {
		String sql = "update shopify_import_info sii set date_last_update = :date where sii.marketplace_id = :marketplaceKey and sii.service_name = :serviceName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", date);
		q.addValue("marketplaceKey", marketplace.getKey());
		q.addValue("serviceName", serviceName);
		int affectedRows = getNamedParameterJdbcTemplate().update(sql.toString(),q);
		Assert.isTrue(affectedRows==1);
	}

}
