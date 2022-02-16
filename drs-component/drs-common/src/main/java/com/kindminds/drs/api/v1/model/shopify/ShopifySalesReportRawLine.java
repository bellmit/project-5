package com.kindminds.drs.api.v1.model.shopify;

import java.math.BigDecimal;
import java.util.Date;

public interface ShopifySalesReportRawLine {
    String getOrderId();
    String getSalesId();
    Date getDate();
    String getOrderName();
    String getTransactionType();
    String getSaleType();
    String getSalesChannel();
    String getPosLocation();
    String getBillingCountry();
    String getBillingRegion();
    String getBillingCity();
    String getShippingCountry();
    String getShippingRegion();
    String getShippingCity();
    String getProductType();
    String getProductVendor();
    String getProductTitle();
    String getProductVariantTitle();
    String getProductVariantSku();
    int getNetQuantity();
    BigDecimal getGrossSales();
    BigDecimal getLineItemDiscount();
    BigDecimal getDiscounts();
    BigDecimal getReturns();
    BigDecimal getNetSales();
    BigDecimal getShipping();
    BigDecimal getTaxes();
    BigDecimal getTotalSales();
}