package com.kindminds.drs.api.v1.model.sales;

public interface ShopifyOrderItemDetail {
	
	public String getShippingMethod();
	public String getShippingCountry();
	public String getShippingState();
	public String getShippingCity();
	public String getShippingPostalCode();
	public String getFulfillmentService();
	public String getTax();
	public String getTotalDiscount();
	
}