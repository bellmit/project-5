package com.kindminds.drs.api.v1.model.shopify;

import java.math.BigDecimal;
import java.util.Date;

public interface ShopifyOrderReportRawLine {
	public String getName();
	public String getEmail();
	public String getFinancialStatus();
	public Date getPaidAt();
	public String getFulfillmentStatus();
	public Date getFulfilledAt();
	public String getAcceptsMarketing();
	public String getCurrency();
	public BigDecimal getSubtotal();
	public BigDecimal getShipping();
	public BigDecimal getTaxes();
	public BigDecimal getTotal();
	public String getDiscountCode();
	public BigDecimal getDiscountAmount();
	public String getShippingMethod();
	public Date getCreatedAt();
	public int getLineitemQuantity();
	public String getLineitemName();
	public BigDecimal getLineitemPrice();
	public BigDecimal getLineitemCompareAtPrice();
	public String getLineitemSku();
	public boolean getLineitemRequiresShipping();
	public boolean getLineitemTaxable();
	public String getLineitemFulfillmentStatus();
	public String getBillingName();
	public String getBillingStreet();
	public String getBillingAddress1();
	public String getBillingAddress2();
	public String getBillingCompany();
	public String getBillingCity();
	public String getBillingZip();
	public String getBillingProvince();
	public String getBillingCountry();
	public String getBillingPhone();
	public String getShippingName();
	public String getShippingStreet();
	public String getShippingAddress1();
	public String getShippingAddress2();
	public String getShippingCompany();
	public String getShippingCity();
	public String getShippingZip();
	public String getShippingProvince();
	public String getShippingCountry();
	public String getShippingPhone();
	public String getNotes();
	public String getNoteAttributes();
	public Date getCancelledAt();
	public String getPaymentMethod();
	public String getPaymentReference();
	public BigDecimal getRefundedAmount();
	public String getVendor();
	public String getId();
	public String getTags();
	public String getRiskLevel();
	public String getSource();
	public BigDecimal getLineitemDiscount();
	public String getTax1Name();
	public BigDecimal getTax1Value();
	public String getTax2Name();
	public BigDecimal getTax2Value();
	public String getTax3Name();
	public BigDecimal getTax3Value();
	public String getTax4Name();
	public BigDecimal getTax4Value();
	public String getTax5Name();
	public BigDecimal getTax5Value();
	public String getPhone();
	public String getDuties();
	public String getBillingProvinceName();
	public String getShippingProvinceName();
}
