package com.kindminds.drs.api.v1.model.shopify;

import java.math.BigDecimal;
import java.util.Date;

public interface ShopifyPaymentTransactionReportRawLine {
	public Date getTransactionDate();
	public String getType();
	public String getOrderName();
	public String getCardBrand();
	public String getCardSource();
	public String getPayoutStatus();
	public Date getPayoutDate();
	public BigDecimal getAmount();
	public BigDecimal getFee();
	public BigDecimal getNet();
	public String getCheckout();
	public String getPaymentMethodName();
	public BigDecimal getPresentmentAmount();
	public String getPresentmentCurrency();
	public String getCurrency();
}
