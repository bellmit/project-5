package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.sales.ListCustomerOrderCondition;

public class ListCustomerOrderConditionImpl implements ListCustomerOrderCondition {

	private String supplierKcode;
	private String orderDateStart;
	private String orderDateEnd;
	private String transactionDateStart;
	private String transactionDateEnd;
	private String relatedProductBaseCode;
	private String relatedProductSkuCode;
	private String amazonMerchantSKU;
	private String salesChannelId;
	private String marketplaceOrderId;
	private String customerName;
	private String buyerEmail;
	private String promotionId;
	private String asin;
	private String orderStatus;
	private String elasticSearch;
	private String isElasticSearch;
	
	public ListCustomerOrderConditionImpl(
			String supplierKcode,
			String orderDateStart,
			String orderDateEnd,
			String transactionDateStart,
			String transactionDateEnd,
			String relatedProductBaseCode,
			String relatedProductSkuCode,
			String amazonMerchantSKU,
			String salesChannelId,
			String marketplaceOrderId,
			String customerName,
			String buyerEmail,
			String promotionId,
			String asin){
		this.supplierKcode=supplierKcode;
		this.orderDateStart=orderDateStart;
		this.orderDateEnd=orderDateEnd;
		this.transactionDateStart = transactionDateStart;
		this.transactionDateEnd = transactionDateEnd;
		this.relatedProductBaseCode=relatedProductBaseCode;
		this.relatedProductSkuCode=relatedProductSkuCode;
		this.amazonMerchantSKU=amazonMerchantSKU;
		this.salesChannelId=salesChannelId;
		this.marketplaceOrderId=marketplaceOrderId;
		this.customerName=customerName;
		this.buyerEmail=buyerEmail;
		this.promotionId=promotionId;
		this.asin=asin;
	}

	public ListCustomerOrderConditionImpl(
			String supplierKcode,
			String orderDateStart,
			String orderDateEnd,
			String transactionDateStart,
			String transactionDateEnd,
			String relatedProductBaseCode,
			String relatedProductSkuCode,
			String amazonMerchantSKU,
			String salesChannelId,
			String marketplaceOrderId,
			String customerName,
			String buyerEmail,
			String promotionId,
			String asin,
			String orderStatus){
		this.supplierKcode=supplierKcode;
		this.orderDateStart=orderDateStart;
		this.orderDateEnd=orderDateEnd;
		this.transactionDateStart = transactionDateStart;
		this.transactionDateEnd = transactionDateEnd;
		this.relatedProductBaseCode=relatedProductBaseCode;
		this.relatedProductSkuCode=relatedProductSkuCode;
		this.amazonMerchantSKU=amazonMerchantSKU;
		this.salesChannelId=salesChannelId;
		this.marketplaceOrderId=marketplaceOrderId;
		this.customerName=customerName;
		this.buyerEmail=buyerEmail;
		this.promotionId=promotionId;
		this.asin=asin;
		this.orderStatus = orderStatus;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	@Override
	public String getOrderDateStart() {
		return this.orderDateStart;
	}

	@Override
	public String getOrderDateEnd() {
		return this.orderDateEnd;
	}

	@Override
	public String getTransactionDateStart() {
		return this.transactionDateStart;
	}

	@Override
	public String getTransactionDateEnd() {
		return this.transactionDateEnd;
	}

	@Override
	public String getRelatedBaseProductCode() {
		return this.relatedProductBaseCode;
	}

	@Override
	public String getRelatedSkuCode() {
		return this.relatedProductSkuCode;
	}

	@Override
	public String getAmazonMerchantSKU() {
		return this.amazonMerchantSKU;
	}

	@Override
	public String getSalesChannelId() {
		return this.salesChannelId;
	}

	@Override
	public String getMarketplaceOrderId() {
		return this.marketplaceOrderId;
	}

	@Override
	public String getCustomerName() {
		return this.customerName;
	}

	@Override
	public String getBuyerEmail() {
		return this.buyerEmail;
	}

	@Override
	public String getPromotionId() {
		return this.promotionId;
	}

	@Override
	public String getAsin() {
		return this.asin;
	}

	@Override
	public String getOrderStatus() {
		return orderStatus;
	}

	@Override
	public String getSearchTerms() {
		return elasticSearch;
	}

	@Override
	public String getIsSearch() {
		return isElasticSearch;
	}

	@Override
	public String toString() {
		return "ListCustomerOrderConditionImpl{" +
				"supplierKcode='" + supplierKcode + '\'' +
				", orderDateStart='" + orderDateStart + '\'' +
				", orderDateEnd='" + orderDateEnd + '\'' +
				", transactionDateStart='" + transactionDateStart + '\'' +
				", transactionDateEnd='" + transactionDateEnd + '\'' +
				", relatedProductBaseCode='" + relatedProductBaseCode + '\'' +
				", relatedProductSkuCode='" + relatedProductSkuCode + '\'' +
				", amazonMerchantSKU='" + amazonMerchantSKU + '\'' +
				", salesChannelId='" + salesChannelId + '\'' +
				", marketplaceOrderId='" + marketplaceOrderId + '\'' +
				", customerName='" + customerName + '\'' +
				", buyerEmail='" + buyerEmail + '\'' +
				", promotionId='" + promotionId + '\'' +
				", asin='" + asin + '\'' +
				", orderStatus='" + orderStatus + '\'' +
				", elasticSearch='" + elasticSearch + '\'' +
				", isElasticSearch='" + isElasticSearch + '\'' +
				'}';
	}
}
