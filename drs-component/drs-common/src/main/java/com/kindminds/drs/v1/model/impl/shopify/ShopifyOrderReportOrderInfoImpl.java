package com.kindminds.drs.v1.model.impl.shopify;

import java.math.BigDecimal;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderReportOrderInfo;

public class ShopifyOrderReportOrderInfoImpl implements ShopifyOrderReportOrderInfo {

	private String orderName;
	private Currency currency;
	private BigDecimal subtotal;
	private BigDecimal shipping;
	private BigDecimal taxes;
	private BigDecimal total;
	private BigDecimal discountAmount;

	public ShopifyOrderReportOrderInfoImpl(
			String orderName,
			String currency,
			BigDecimal subtotal,
			BigDecimal shipping,
			BigDecimal taxes,
			BigDecimal total,
			BigDecimal discountAmount) {
		super();
		this.orderName = orderName;
		this.currency = Currency.valueOf(currency);
		this.subtotal = subtotal;
		this.shipping = shipping;
		this.taxes = taxes;
		this.total = total;
		this.discountAmount = discountAmount;
	}

	@Override
	public String getOrderName() {
		return this.orderName;
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public BigDecimal getSubtotal() {
		return this.subtotal;
	}

	@Override
	public BigDecimal getShipping() {
		return this.shipping;
	}

	@Override
	public BigDecimal getTaxes() {
		return this.taxes;
	}

	@Override
	public BigDecimal getTotal() {
		return this.total;
	}

	@Override
	public BigDecimal getDiscountAmount() {
		return this.discountAmount;
	}

}
