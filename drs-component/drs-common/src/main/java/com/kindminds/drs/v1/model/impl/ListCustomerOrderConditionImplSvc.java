package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.sales.ListCustomerOrderCondition;

public class ListCustomerOrderConditionImplSvc implements ListCustomerOrderCondition {
	
	private ListCustomerOrderCondition origin=null;
	private String supplierKcode;
	
	public ListCustomerOrderConditionImplSvc(ListCustomerOrderCondition origin,String userCompanyKcode) {
		this.origin=origin;
		this.supplierKcode = userCompanyKcode;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	@Override
	public String getOrderDateStart() {
		return this.origin.getOrderDateStart();
	}

	@Override
	public String getOrderDateEnd() {
		return this.origin.getOrderDateEnd();
	}

	@Override
	public String getTransactionDateStart() {
		return this.origin.getTransactionDateStart();
	}

	@Override
	public String getTransactionDateEnd() {
		return this.origin.getTransactionDateEnd();
	}

	@Override
	public String getRelatedBaseProductCode() {
		return this.origin.getRelatedBaseProductCode();
	}

	@Override
	public String getRelatedSkuCode() {
		return this.origin.getRelatedSkuCode();
	}

	@Override
	public String getAmazonMerchantSKU() {
		return this.origin.getAmazonMerchantSKU();
	}

	@Override
	public String getSalesChannelId() {
		return this.origin.getSalesChannelId();
	}

	@Override
	public String getMarketplaceOrderId() {
		return this.origin.getMarketplaceOrderId();
	}

	@Override
	public String getCustomerName() {
		return this.origin.getCustomerName();
	}

	@Override
	public String getBuyerEmail() {
		return this.origin.getBuyerEmail();
	}

	@Override
	public String getPromotionId() {
		return this.origin.getPromotionId();
	}

	@Override
	public String getAsin() {
		return this.origin.getAsin();
	}

	@Override
	public String getOrderStatus() {
		return this.origin.getOrderStatus();
	}

	@Override
	public String getSearchTerms() {
		return this.origin.getSearchTerms();
	}

	@Override
	public String getIsSearch() {
		return this.origin.getIsSearch();
	}

}
