package com.kindminds.drs.v1.model.impl.shopify;

import java.math.BigDecimal;

import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderReportOrderLineInfo;

public class ShopifyOrderReportOrderLineInfoImpl implements ShopifyOrderReportOrderLineInfo {

	private String sku;
	private int lineItemQuantity;
	private BigDecimal unitPrice;

	public ShopifyOrderReportOrderLineInfoImpl(
			String sku,
			int lineItemQuantity,
			BigDecimal unitPrice) {
		this.sku = sku;
		this.lineItemQuantity = lineItemQuantity;
		this.unitPrice = unitPrice;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public int getLineItemQuantity() {
		return this.lineItemQuantity;
	}

	@Override
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

}
