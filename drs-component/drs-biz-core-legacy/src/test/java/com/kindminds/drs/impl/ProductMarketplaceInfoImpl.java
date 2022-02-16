package com.kindminds.drs.impl;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;

public class ProductMarketplaceInfoImpl implements ProductMarketplaceInfo {
	
	private String skuCodeByDrs;
	private String skuNameBySupplier;
	private Marketplace marketplace;
	private String marketplaceSku;
	private String currency;
	private String status;
	private String msrp;
	private String supplierSuggestedActualRetailPrice;
	private String currentActualRetailPrice;
	private String estimatedDrsRetainment;
	private String referralRate;
	private String estimatedMarketplaceFees;
	private String estimatedFulfillmentFees;
	private String estimatedImportDuty;
	private String estimatedFreightCharge;
	private String approxSupplierNetRevenue;
	
	public ProductMarketplaceInfoImpl(
			String skuCodeByDrs,
			String skuNameBySupplier,
			Marketplace marketplace,
			String marketplaceSku,
			String currency,
			String status,
			String msrp,
			String supplierSuggestedActualRetailPrice,
			String currentActualRetailPrice, 
			String estimatedDrsRetainment,
			String referralRate, 
			String estimatedMarketplaceFees,
			String estimatedFulfillmentFees, 
			String estimatedImportDuty,
			String estimatedFreightCharge, 
			String approxSupplierNetRevenue,
			String maxPotentialSalesPerWeek) {
		super();
		this.skuCodeByDrs = skuCodeByDrs;
		this.skuNameBySupplier = skuNameBySupplier;
		this.marketplace = marketplace;
		this.marketplaceSku = marketplaceSku;
		this.currency = currency;
		this.status = status;
		this.msrp = msrp;
		this.supplierSuggestedActualRetailPrice = supplierSuggestedActualRetailPrice;
		this.currentActualRetailPrice = currentActualRetailPrice;
		this.estimatedDrsRetainment = estimatedDrsRetainment;
		this.referralRate = referralRate;
		this.estimatedMarketplaceFees = estimatedMarketplaceFees;
		this.estimatedFulfillmentFees = estimatedFulfillmentFees;
		this.estimatedImportDuty = estimatedImportDuty;
		this.estimatedFreightCharge = estimatedFreightCharge;
		this.approxSupplierNetRevenue = approxSupplierNetRevenue;
	}
	
	@Override
	public String toString() {
		return "ProductMarketplaceInfoImpl [getProductCodeByDrs()=" + getProductCodeByDrs()
				+ ", getProductNameBySupplier()=" + getProductNameBySupplier() + ", getMarketplace()="
				+ getMarketplace() + ", getMarketplaceSku()=" + getMarketplaceSku() + ", getCurrency()=" + getCurrency()
				+ ", getStatus()=" + getStatus() + ", getMSRP()=" + getMSRP()
				+ ", getSupplierSuggestedBaseRetailPrice()=" + getSupplierSuggestedBaseRetailPrice()
				+ ", getCurrentBaseRetailPrice()=" + getCurrentBaseRetailPrice() + ", getEstimatedDrsRetainment()="
				+ getEstimatedDrsRetainment() + ", getReferralRate()=" + getReferralRate()
				+ ", getEstimatedMarketplaceFees()=" + getEstimatedMarketplaceFees()
				+ ", getEstimatedFulfillmentFees()=" + getEstimatedFulfillmentFees() + ", getEstimatedImportDuty()="
				+ getEstimatedImportDuty() + ", getEstimatedFreightCharge()=" + getEstimatedFreightCharge()
				+ ", getApproxSupplierNetRevenue()=" + getApproxSupplierNetRevenue() + "]";
	}

	@Override
	public boolean equals(Object obj){
		if ( obj instanceof ProductMarketplaceInfo ){
			ProductMarketplaceInfo info = ((ProductMarketplaceInfo) obj);
			return this.getProductCodeByDrs().equals(info.getProductCodeByDrs())
				&& this.getProductNameBySupplier().equals(info.getProductNameBySupplier())
				&& this.getMarketplace().equals(info.getMarketplace())
				&& this.getMarketplaceSku().equals(info.getMarketplaceSku())
				&& this.getCurrency().equals(info.getCurrency())
				&& this.getStatus().equals(info.getStatus())
				&& this.getMSRP().equals(info.getMSRP())
				&& this.getSupplierSuggestedBaseRetailPrice().equals(info.getSupplierSuggestedBaseRetailPrice())
				&& this.getCurrentBaseRetailPrice().equals(info.getCurrentBaseRetailPrice())
				&& this.getEstimatedDrsRetainment().equals(info.getEstimatedDrsRetainment())
				&& this.getReferralRate().equals(info.getReferralRate())
				&& this.getEstimatedMarketplaceFees().equals(info.getEstimatedMarketplaceFees())
				&& this.getEstimatedFulfillmentFees().equals(info.getEstimatedFulfillmentFees())
				&& this.getEstimatedImportDuty().equals(info.getEstimatedImportDuty())
				&& this.getEstimatedFreightCharge().equals(info.getEstimatedFreightCharge())
				&& this.getApproxSupplierNetRevenue().equals(info.getApproxSupplierNetRevenue());
		}
		else {
	      return false;
	    }
	}
	
	

	@Override
	public String getProductCodeByDrs() {
		return this.skuCodeByDrs;
	}

	@Override
	public String getProductNameBySupplier() {
		return this.skuNameBySupplier;
	}

	@Override
	public Marketplace getMarketplace() {
		return this.marketplace;
	}

	@Override
	public String getMarketplaceSku() {
		return this.marketplaceSku;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}
	
	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public String getMSRP() {
		return this.msrp;
	}

	@Override
	public String getSupplierSuggestedBaseRetailPrice() {
		return this.supplierSuggestedActualRetailPrice;
	}

	@Override
	public String getCurrentBaseRetailPrice() {
		return this.currentActualRetailPrice;
	}

	@Override
	public String getEstimatedDrsRetainment() {
		return this.estimatedDrsRetainment;
	}

	@Override
	public String getReferralRate() {
		return this.referralRate;
	}

	@Override
	public String getEstimatedMarketplaceFees() {
		return this.estimatedMarketplaceFees;
	}

	@Override
	public String getEstimatedFulfillmentFees() {
		return this.estimatedFulfillmentFees;
	}

	@Override
	public String getEstimatedImportDuty() {
		return this.estimatedImportDuty;
	}

	@Override
	public String getEstimatedFreightCharge() {
		return this.estimatedFreightCharge;
	}

	@Override
	public String getApproxSupplierNetRevenue() {
		return this.approxSupplierNetRevenue;
	}

}
