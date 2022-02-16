package com.kindminds.drs.api.v1.model.shopify;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;

public interface ShopifyOrderReportOrderInfo {
	String getOrderName();
	Currency getCurrency();
	BigDecimal getSubtotal();
	BigDecimal getShipping();
	BigDecimal getTaxes();
	BigDecimal getTotal();
	BigDecimal getDiscountAmount();
}
