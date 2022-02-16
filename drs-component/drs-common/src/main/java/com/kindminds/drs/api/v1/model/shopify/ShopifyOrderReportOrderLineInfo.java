package com.kindminds.drs.api.v1.model.shopify;

import java.math.BigDecimal;

public interface ShopifyOrderReportOrderLineInfo {
	String getSku();
	int getLineItemQuantity();
	BigDecimal getUnitPrice();
}
