package com.kindminds.drs.api.v1.model.sales;

public interface ListCustomerOrderCondition {
	
	String getSupplierKcode();
	String getOrderDateStart();
	String getOrderDateEnd();
	String getTransactionDateStart();
	String getTransactionDateEnd();
	String getRelatedBaseProductCode();
	String getRelatedSkuCode();
	String getAmazonMerchantSKU();
	String getSalesChannelId();
	String getMarketplaceOrderId();		
	String getCustomerName();	
	String getBuyerEmail();
	String getPromotionId();
	String getAsin();
	String getOrderStatus();
	String getSearchTerms();
	String getIsSearch();
		
}