package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportRawLine;

public class AmazonSettlementReportRawLineImpl implements AmazonSettlementReportRawLine {

	private String settlementId;
	private String currency;
	private String transactionType;
	private String orderId;
	private String merchantOrderId;
	private String adjustmentId;
	private String shipmentId;
	private String marketplaceName;
	private String amountType;
	private String amountDescription;
	private String amount;
	private String fulfillmentId;
	private String postedDate;
	private String postedDateTime;
	private String orderItemCode;
	private String merchantOrderItemId;
	private String merchantAdjustmentItemId;
	private String sku;
	private String quantityPurchased;
	private String promotionId;
	
	public AmazonSettlementReportRawLineImpl(
			String settlementId,
			String currency,
			String transactionType,
			String orderId,
			String merchantOrderId,
			String adjustmentId,
			String shipmentId,
			String marketplaceName,
			String amountType,
			String amountDescription,
			String amount,
			String fulfillmentId,
			String postedDate,
			String postedDateTime,
			String orderItemCode,
			String merchantOrderItemId,
			String merchantAdjustmentItemId,
			String sku,
			String quantityPurchased,
			String promotionId) {
		super();
		this.settlementId = settlementId;
		this.currency = currency;
		this.transactionType = transactionType;
		this.orderId = orderId;
		this.merchantOrderId = merchantOrderId;
		this.adjustmentId = adjustmentId;
		this.shipmentId = shipmentId;
		this.marketplaceName = marketplaceName;
		this.amountType = amountType;
		this.amountDescription = amountDescription;
		this.amount = amount;
		this.fulfillmentId = fulfillmentId;
		this.postedDate = postedDate;
		this.postedDateTime = postedDateTime;
		this.orderItemCode = orderItemCode;
		this.merchantOrderItemId = merchantOrderItemId;
		this.merchantAdjustmentItemId = merchantAdjustmentItemId;
		this.sku = sku;
		this.quantityPurchased = quantityPurchased;
		this.promotionId = promotionId;
	}

	@Override
	public String getSettlementId() {
		return this.settlementId;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getTransactionType() {
		return this.transactionType;
	}

	@Override
	public String getOrderId() {
		return this.orderId;
	}

	@Override
	public String getMerchantOrderId() {
		return this.merchantOrderId;
	}

	@Override
	public String getAdjustmentId() {
		return this.adjustmentId;
	}

	@Override
	public String getShipmentId() {
		return this.shipmentId;
	}

	@Override
	public String getMarketplaceName() {
		return this.marketplaceName;
	}

	@Override
	public String getAmountType() {
		return this.amountType;
	}

	@Override
	public String getAmountDescription() {
		return this.amountDescription;
	}

	@Override
	public String getAmount() {
		return this.amount;
	}

	@Override
	public String getFulfillmentId() {
		return this.fulfillmentId;
	}

	@Override
	public String getPostedDate() {
		return this.postedDate;
	}

	@Override
	public String getPostedDateTime() {
		return this.postedDateTime;
	}

	@Override
	public String getOrderItemCode() {
		return this.orderItemCode;
	}

	@Override
	public String getMerchantOrderItemId() {
		return this.merchantOrderItemId;
	}

	@Override
	public String getMerchantAdjustmentItemId() {
		return this.merchantAdjustmentItemId;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getQuantityPurchased() {
		return this.quantityPurchased;
	}

	@Override
	public String getPromotionId() {
		return this.promotionId;
	}

}
