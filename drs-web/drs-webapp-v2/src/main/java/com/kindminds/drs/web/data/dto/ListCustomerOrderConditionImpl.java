package com.kindminds.drs.web.data.dto;

import org.springframework.util.StringUtils;

import com.kindminds.drs.api.v1.model.sales.ListCustomerOrderCondition;

public class ListCustomerOrderConditionImpl implements ListCustomerOrderCondition {

	private String supplierKcode;
	private String orderDateStart;
	private String orderDateEnd;
	private String transactionDateStart;
	private String transactionDateEnd;
	private String relatedBaseProductCode;
	private String relatedSkuCode;
	private String amazonMerchantSKU;
	private String salesChannelId;	
	private String marketplaceOrderId;
	private String customerName;
	private String buyerEmail;
	private String promotionId;
	private String asin;
	private String orderStatus;
	private String searchTerms;
	private String isSearch;
		
	@Override
	public String getSupplierKcode() {		
		return StringUtils.isEmpty(this.supplierKcode)?null:this.supplierKcode;		
	}

	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode; 		
	}
	
	@Override
	public String getOrderDateStart() {		
		return StringUtils.isEmpty(this.orderDateStart)?null:this.orderDateStart;
	}

	public void setOrderDateStart(String orderDateStart) {
		this.orderDateStart = orderDateStart;		
	}
	
	@Override
	public String getOrderDateEnd() {		
		return StringUtils.isEmpty(this.orderDateEnd)?null:this.orderDateEnd;
	}

	public void setOrderDateEnd(String orderDateEnd){
		this.orderDateEnd = orderDateEnd;
	}

	@Override
	public String getTransactionDateStart() {
		return StringUtils.isEmpty(this.transactionDateStart)?null:this.transactionDateStart;
	}

	public void setTransactionDateStart(String transactionDateStart) {
		this.transactionDateStart = transactionDateStart;		
	}
	
	@Override
	public String getTransactionDateEnd() {
		return StringUtils.isEmpty(this.transactionDateEnd)?null:this.transactionDateEnd;
	}
	
	public void setTransactionDateEnd(String transactionDateEnd) {
		this.transactionDateEnd = transactionDateEnd;		
	}
		
	@Override
	public String getRelatedBaseProductCode() {		
		return StringUtils.isEmpty(this.relatedBaseProductCode)?null:this.relatedBaseProductCode;
	}

	public void setRelatedBaseProductCode(String relatedBaseProductCode) {
		this.relatedBaseProductCode = relatedBaseProductCode; 		
	}
		
	@Override
	public String getRelatedSkuCode() {		
		return StringUtils.isEmpty(this.relatedSkuCode)?null:this.relatedSkuCode;
	}

	public void setRelatedSkuCode(String relatedSkuCode) {
		this.relatedSkuCode = relatedSkuCode;		
	}
	
	@Override
	public String getAmazonMerchantSKU() {		
		return StringUtils.isEmpty(this.amazonMerchantSKU)?null:this.amazonMerchantSKU;
	}
	
	public void setAmazonMerchantSKU(String amazonMerchantSKU) {
		this.amazonMerchantSKU = amazonMerchantSKU; 		
	}
	
	@Override
	public String getSalesChannelId() {
		return StringUtils.isEmpty(this.salesChannelId)?null:this.salesChannelId;
	}

	public void setSalesChannelId(String salesChannelId) {
		this.salesChannelId = salesChannelId;		
	}
			
	@Override
	public String getMarketplaceOrderId() {		
		return StringUtils.isEmpty(this.marketplaceOrderId)?null:this.marketplaceOrderId;
	}

	public void setMarketplaceOrderId(String marketplaceOrderId) {
		this.marketplaceOrderId = marketplaceOrderId; 		
	}
		
	@Override
	public String getCustomerName() {		
		return StringUtils.isEmpty(this.customerName)?null:this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;		
	}
		
	@Override
	public String getBuyerEmail() {		
		return StringUtils.isEmpty(this.buyerEmail)?null:this.buyerEmail;
	}
	
	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail; 		
	}

	@Override
	public String getPromotionId() {		
		return StringUtils.isEmpty(this.promotionId)?null:this.promotionId;		
	}
	
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId; 		
	}

	@Override
	public String getAsin() {		
		return StringUtils.isEmpty(this.asin)?null:this.asin;
	}
		
	public void setAsin(String asin){
		this.asin = asin;		
	}

	@Override
	public String getOrderStatus() {
		return StringUtils.isEmpty(orderStatus) ? null : this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String getSearchTerms() {
		return StringUtils.isEmpty(searchTerms) ? null : this.searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	@Override
	public String getIsSearch() {
		return isSearch;
	}

	public void setIsSearch(String isSearch) {
		this.isSearch = isSearch;
	}

	public void makeFilterFieldsNull() {
		supplierKcode = null;
		transactionDateStart = null;
		transactionDateEnd = null;
		relatedBaseProductCode = null;
		relatedSkuCode = null;
		amazonMerchantSKU = null;
		salesChannelId = null;
		marketplaceOrderId = null;
		customerName = null;
		buyerEmail = null;
		promotionId = null;
		asin = null;
		orderStatus = null;
	}

}