package com.kindminds.drs.api.v1.model.accounting;

public interface AmazonOrderDetail {
	
	String getShippingMethod();
	String getShippingCountry();
	String getShippingState();
	String getShippingCity();
	String getShippingPostalCode();
	String getSaleTax();
	String getShippingPrice();
	String getShippingTax();
	String getGiftWrapPrice();
	String getGiftWrapTax();
	String getItemPromotionDiscount();
	String getShippingPromotionDiscount();
	String getRefundReason();
	String getRefundDetailedDisposition();
		
}