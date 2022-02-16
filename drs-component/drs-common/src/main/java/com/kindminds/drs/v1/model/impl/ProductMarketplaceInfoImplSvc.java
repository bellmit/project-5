package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;

public class ProductMarketplaceInfoImplSvc implements ProductMarketplaceInfo {
	
	private ProductMarketplaceInfo orig = null;
	private String estimatedDrsRetainment = null;
	private String estimatedMarketplaceFees = null;
	private String approxSupplierNetRevenue = null;

	public ProductMarketplaceInfoImplSvc(ProductMarketplaceInfo orig){
		this.orig = orig;
	}
	
	public void setEstimatedDrsRetainment(String estimatedDrsRetainment) {
		this.estimatedDrsRetainment = estimatedDrsRetainment;
	}

	public void setEstimatedMarketplaceFees(String estimatedMarketplaceFees) {
		this.estimatedMarketplaceFees = estimatedMarketplaceFees;
	}

	public void setApproxNetOperatingIncome(String approxSupplierNetRevenue) {
		this.approxSupplierNetRevenue = approxSupplierNetRevenue;
	}	
	
	@Override
	public String getProductCodeByDrs() {
		return this.orig.getProductCodeByDrs();
	}

	@Override
	public String getProductNameBySupplier() {
		return this.orig.getProductNameBySupplier();
	}

	@Override
	public Marketplace getMarketplace() {
		return this.orig.getMarketplace();
	}

	@Override
	public String getMarketplaceSku() {
		return this.orig.getMarketplaceSku().trim();
	}

	@Override
	public String getCurrency() {
		return Currency.valueOf(this.orig.getCurrency()).name();
	}
	
	@Override
	public String getStatus() {
		return this.orig.getStatus();
	}
	
	@Override
	public String getMSRP() {
		return this.orig.getMSRP();
	}

	@Override
	public String getSupplierSuggestedBaseRetailPrice() {
		return this.orig.getSupplierSuggestedBaseRetailPrice();
	}

	@Override
	public String getCurrentBaseRetailPrice() {
		return this.orig.getCurrentBaseRetailPrice();
	}

	@Override
	public String getEstimatedDrsRetainment() {
		return this.estimatedDrsRetainment;
	}

	@Override
	public String getReferralRate() {
		return this.orig.getReferralRate();
	}

	@Override
	public String getEstimatedMarketplaceFees() {
		return this.estimatedMarketplaceFees;
	}

	@Override
	public String getEstimatedFulfillmentFees() {
		return this.orig.getEstimatedFulfillmentFees();
	}

	@Override
	public String getEstimatedImportDuty() {
		return this.orig.getEstimatedImportDuty();
	}

	@Override
	public String getEstimatedFreightCharge() {
		return this.orig.getEstimatedFreightCharge();
	}

	@Override
	public String getApproxSupplierNetRevenue() {
		return this.approxSupplierNetRevenue;
	}

}
