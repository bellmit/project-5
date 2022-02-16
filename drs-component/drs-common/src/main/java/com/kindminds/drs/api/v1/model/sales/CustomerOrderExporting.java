package com.kindminds.drs.api.v1.model.sales;

public interface CustomerOrderExporting {
	
	String getOrderTimeLocal();
	String getOrderTimeUTC();
	String getTransactionTimeUTC();
	String getMarketplaceOrderId();
	String getShopifyOrderId();
    String getOrderStatus(); 
    String getSKUCode();
    String getProductName();
    String getItemPrice();
    String getActualRetailPrice();
    String getQty();
    String getBuyer();
    String getSalesChannel();
    String getFulfillmentCenter();
    String getPromotionId();
	String getCity();
	String getState();
	String getCountry();
	
}
