package com.kindminds.drs.api.v1.model.accounting;

public interface AmazonSettlementReportRawLine {
	String getSettlementId();
	String getCurrency();
	String getTransactionType();
	String getOrderId();
	String getMerchantOrderId();
	String getAdjustmentId();
	String getShipmentId();
	String getMarketplaceName();
	String getAmountType();
	String getAmountDescription();
	String getAmount();
	String getFulfillmentId();
	String getPostedDate();
	String getPostedDateTime();
	String getOrderItemCode();
	String getMerchantOrderItemId();
	String getMerchantAdjustmentItemId();
	String getSku();
	String getQuantityPurchased();
	String getPromotionId();
}
