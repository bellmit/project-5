package com.kindminds.drs.api.v1.model.shopify;

import java.math.BigDecimal;

public interface ShopifyOrderShippingLine {
	String getShippingLineId();
	String getTitle();
	BigDecimal getPrice();
	String getCode();
	String getSource();
	String getPhone();
	String getRequestedFulfillmentServiceId();
	String getDeliveryCategory();
	String getCarrierIdentifier();
	BigDecimal getDiscountedPrice();
	String getJsonPriceSet();
	String getJsonDiscountedPriceSet();
	String getJsonDiscountAllocations();
	String getJsonTaxLines();
}
