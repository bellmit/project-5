package com.kindminds.drs.web.data.dto.product;

import java.math.BigDecimal;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;

public class ProductMarketplaceInfoImpl implements ProductMarketplaceInfo{

	private String productCodeByDrs;
	private Marketplace marketplace;
	private String currency;
	private String status;
	private String marketplaceSku;	
	private String MSRP;
	private String supplierSuggestedBaseRetailPrice;
	private String currentBaseRetailPrice;
	private String estimatedDrsRetainment;
	private String referralRate;
	private String estimatedMarketplaceFees;
	private String estimatedFulfillmentFees;
	private String estimatedImportDuty;
	private String estimatedFreightCharge;
	private String approxSupplierNetRevenue;
	private String productNameBySupplier;
	
	public ProductMarketplaceInfoImpl(){};
	
	public ProductMarketplaceInfoImpl(ProductMarketplaceInfo original){
		this.productCodeByDrs = original.getProductCodeByDrs();
		this.marketplace = original.getMarketplace();
		this.currency = original.getCurrency();
		this.status = original.getStatus();
		this.marketplaceSku = original.getMarketplaceSku();
		this.MSRP = original.getMSRP();
		this.supplierSuggestedBaseRetailPrice = original.getSupplierSuggestedBaseRetailPrice();
		this.currentBaseRetailPrice = original.getCurrentBaseRetailPrice();
		this.estimatedDrsRetainment = original.getEstimatedDrsRetainment();
		BigDecimal referralRate = new BigDecimal(original.getReferralRate());
		BigDecimal referralRateToDisplay = referralRate.multiply(new BigDecimal("100")).stripTrailingZeros();
		this.referralRate = String.valueOf(referralRateToDisplay.toPlainString());
		this.estimatedMarketplaceFees = original.getEstimatedMarketplaceFees();
		this.estimatedFulfillmentFees = original.getEstimatedFulfillmentFees();
		this.estimatedImportDuty = original.getEstimatedImportDuty();
		this.estimatedFreightCharge = original.getEstimatedFreightCharge();
		this.approxSupplierNetRevenue = original.getApproxSupplierNetRevenue();
		this.productNameBySupplier = original.getProductNameBySupplier();		
	};
		
	@Override
	public String getProductCodeByDrs() {		
		return this.productCodeByDrs;
	}

	public void setProductCodeByDrs(String productCodeByDrs) {
		this.productCodeByDrs = productCodeByDrs;				
	}
		
	@Override
	public Marketplace getMarketplace() {		
		return this.marketplace;
	}

	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;		
	}
				
	@Override
	public String getCurrency() {		
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;		
	}

	@Override
	public String getStatus() {		
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;		
	}
	
	@Override
	public String getMarketplaceSku() {		
		return this.marketplaceSku;
	}
	
	public void setMarketplaceSku(String marketplaceSku) {
		this.marketplaceSku = marketplaceSku;		
	}
		
	@Override
	public String getMSRP() {		
		return this.MSRP;
	}

	public void setMSRP(String MSRP) {
		this.MSRP = MSRP;		
	}
	
	@Override
	public String getSupplierSuggestedBaseRetailPrice() {		
		return this.supplierSuggestedBaseRetailPrice;
	}
	
	public void setSupplierSuggestedBaseRetailPrice(String supplierSuggestedBaseRetailPrice) {
		this.supplierSuggestedBaseRetailPrice = supplierSuggestedBaseRetailPrice;		
	}
	
	@Override
	public String getCurrentBaseRetailPrice() {		
		return this.currentBaseRetailPrice;
	}

	public void setCurrentBaseRetailPrice(String currentBaseRetailPrice) {
		this.currentBaseRetailPrice = currentBaseRetailPrice;		
	}
	
	@Override
	public String getEstimatedDrsRetainment() {		
		return this.estimatedDrsRetainment;
	}

	public void setEstimatedDrsRetainment(String estimatedDrsRetainment) {
		this.estimatedDrsRetainment = estimatedDrsRetainment; 		
	}
	
	@Override
	public String getReferralRate() {		
		return this.referralRate;
	}

	public void setReferralRate(String referralRate) {
		this.referralRate = referralRate; 		
	}
	
	@Override
	public String getEstimatedMarketplaceFees() {		
		return this.estimatedMarketplaceFees;
	}

	public void setEstimatedMarketplaceFees(String estimatedMarketplaceFees) {
		this.estimatedMarketplaceFees = estimatedMarketplaceFees;		
	}
	
	@Override
	public String getEstimatedFulfillmentFees() {		
		return this.estimatedFulfillmentFees;
	}

	public void setEstimatedFulfillmentFees(String estimatedFulfillmentFees) {
		this.estimatedFulfillmentFees = estimatedFulfillmentFees; 				
	}
	
	@Override
	public String getEstimatedImportDuty() {		
		return this.estimatedImportDuty;
	}

	public void setEstimatedImportDuty(String estimatedImportDuty) {
		this.estimatedImportDuty = estimatedImportDuty; 		
	}
		
	@Override
	public String getEstimatedFreightCharge() {		
		return this.estimatedFreightCharge;
	}
	
	public void setEstimatedFreightCharge(String estimatedFreightCharge) {
		this.estimatedFreightCharge = estimatedFreightCharge; 		
	}

	@Override
	public String getApproxSupplierNetRevenue() {
		return this.approxSupplierNetRevenue;
	}

	public void setApproxSupplierNetRevenue(String approxSupplierNetRevenue) {
		this.approxSupplierNetRevenue = approxSupplierNetRevenue;				
	}

	@Override
	public String getProductNameBySupplier() {		
		return this.productNameBySupplier;
	}

	public void setProductNameBySupplier(String productNameBySupplier) {
		this.productNameBySupplier = productNameBySupplier;		
	}
	
}