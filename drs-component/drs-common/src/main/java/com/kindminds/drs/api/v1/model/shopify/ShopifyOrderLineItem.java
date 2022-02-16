package com.kindminds.drs.api.v1.model.shopify;

import java.math.BigDecimal;

public interface ShopifyOrderLineItem {
	String getLineItemId();
	String getVariantId();
	String getTitle();
	Integer getQuantity();
	BigDecimal getPrice();
	BigDecimal getGrams();
	String getSku();
	String getVariantTitle();
	String getVendor();
	String getFulfillmentService();
	String getProductId();
	Boolean getRequiresShipping();
	Boolean getTaxable();
	Boolean getGiftCard();
	String getName();
	String getVariantInventoryManagement();
	String getJsonProperties();
	Boolean getProductExists();
	Integer getFulfillableQuantity();
	BigDecimal getTotalDiscount();
	String getFulfillmentStatus();
	String getJsonPriceSet();
	String getJsonTotalDiscountSet();
	String getJsonDiscountAllocations();
	String getAdminGraphqlApiId();
	String getJsonTaxLines();
	String getJsonOriginLocation();
	String getJsonDestinationLocation();
}
