package com.kindminds.drs.persist.v1.model.mapping.product;

import java.math.BigDecimal;
import java.math.RoundingMode;





import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;


public class ProductMarketplaceInfoImpl implements ProductMarketplaceInfo {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="product_code_by_drs")
	private String productCodeByDrs;
	//@Column(name="product_name_by_supplier")
	private String procuctNameBySupplier;
	//@Column(name="marketplace_id")
	private Integer marketplaceId;
	//@Column(name="marketplace_sku")
	private String marketplaceSku;
	//@Column(name="currency_name")
	private String currency;
	//@Column(name="status")
	private String status;
	//@Column(name="msrp")
	private BigDecimal msrp;
	//@Column(name="supplier_suggested_base_retail_price")
	private BigDecimal supplierSuggestedActualRetailPrice;
	//@Column(name="current_base_retail_price")
	private BigDecimal currentActualRetailPrice;
	//@Column(name="estimated_drs_retainment")
	private BigDecimal estimatedDrsRetainment;
	//@Column(name="referral_rate")
	private BigDecimal referralRate;
	//@Column(name="estimated_marketplace_fees")
	private BigDecimal estimatedMarketplaceFees;
	//@Column(name="estimated_fulfillment_fees")
	private BigDecimal estimatedFulfillmentFees;
	//@Column(name="estimated_import_duty")
	private BigDecimal estimatedImportDuty;
	//@Column(name="estimated_freight_charge")
	private BigDecimal estimatedFreightCharge;
	//@Column(name="approx_supplier_net_revenue")
	private BigDecimal approxSupplierNetRevenue;

	public ProductMarketplaceInfoImpl() {
	}

	public ProductMarketplaceInfoImpl(
			String productCodeByDrs,
			String procuctNameBySupplier,
			Integer marketplaceId,
			String marketplaceSku,
			String currency,
			String status,
			BigDecimal msrp,
			BigDecimal supplierSuggestedActualRetailPrice,
			BigDecimal currentActualRetailPrice,
			BigDecimal estimatedDrsRetainment,
			BigDecimal referralRate,
			BigDecimal estimatedMarketplaceFees,
			BigDecimal estimatedFulfillmentFees,
			BigDecimal estimatedImportDuty,
			BigDecimal estimatedFreightCharge,
			BigDecimal approxSupplierNetRevenue) {
		this.productCodeByDrs = productCodeByDrs;
		this.procuctNameBySupplier = procuctNameBySupplier;
		this.marketplaceId = marketplaceId;
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

	public ProductMarketplaceInfoImpl(int id, String productCodeByDrs, String procuctNameBySupplier, Integer marketplaceId, String marketplaceSku, String currency, String status, BigDecimal msrp, BigDecimal supplierSuggestedActualRetailPrice, BigDecimal currentActualRetailPrice, BigDecimal estimatedDrsRetainment, BigDecimal referralRate, BigDecimal estimatedMarketplaceFees, BigDecimal estimatedFulfillmentFees, BigDecimal estimatedImportDuty, BigDecimal estimatedFreightCharge, BigDecimal approxSupplierNetRevenue) {
		this.id = id;
		this.productCodeByDrs = productCodeByDrs;
		this.procuctNameBySupplier = procuctNameBySupplier;
		this.marketplaceId = marketplaceId;
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
	public String getProductCodeByDrs() {
		return this.productCodeByDrs;
	}

	@Override
	public String getProductNameBySupplier() {
		return this.procuctNameBySupplier;
	}

	@Override
	public Marketplace getMarketplace() {
		return Marketplace.fromKey(this.marketplaceId);
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
		return ProductMarketStatus.fromDbValue(this.status).name();
	}

	@Override
	public String getMSRP() {
		if(this.msrp==null) return null;
		return this.msrp.setScale(2).toPlainString();
	}

	@Override
	public String getSupplierSuggestedBaseRetailPrice() {
		if(this.supplierSuggestedActualRetailPrice==null) return null;
		return this.supplierSuggestedActualRetailPrice.setScale(2).toPlainString();
	}

	@Override
	public String getCurrentBaseRetailPrice() {
		if(this.currentActualRetailPrice==null) return null;
		return this.currentActualRetailPrice.setScale(2,RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getEstimatedDrsRetainment() {
		if(this.estimatedDrsRetainment==null) return null;
		return this.estimatedDrsRetainment.setScale(2,RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getReferralRate() {
		if(this.referralRate==null) return null;
		return this.referralRate.stripTrailingZeros().toPlainString();
	}

	@Override
	public String getEstimatedMarketplaceFees() {
		if(this.estimatedMarketplaceFees==null) return null;
		return this.estimatedMarketplaceFees.setScale(2,RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String getEstimatedFulfillmentFees() {
		if(this.estimatedFulfillmentFees==null) return null;
		return this.estimatedFulfillmentFees.setScale(2).toPlainString();
	}

	@Override
	public String getEstimatedImportDuty() {
		if(this.estimatedImportDuty==null) return null;
		return this.estimatedImportDuty.setScale(2).toPlainString();
	}

	@Override
	public String getEstimatedFreightCharge() {
		if(this.estimatedFreightCharge==null) return null;
		return this.estimatedFreightCharge.setScale(2).toPlainString();
	}

	@Override
	public String getApproxSupplierNetRevenue() {
		if(this.approxSupplierNetRevenue==null) return null;
		return this.approxSupplierNetRevenue.setScale(2,RoundingMode.HALF_UP).toPlainString();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setProductCodeByDrs(String productCodeByDrs) {
		this.productCodeByDrs = productCodeByDrs;
	}

	public void setProcuctNameBySupplier(String procuctNameBySupplier) {
		this.procuctNameBySupplier = procuctNameBySupplier;
	}

	public void setMarketplaceId(Integer marketplaceId) {
		this.marketplaceId = marketplaceId;
	}

	public void setMarketplaceSku(String marketplaceSku) {
		this.marketplaceSku = marketplaceSku;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMsrp(BigDecimal msrp) {
		this.msrp = msrp;
	}

	public void setSupplierSuggestedActualRetailPrice(BigDecimal supplierSuggestedActualRetailPrice) {
		this.supplierSuggestedActualRetailPrice = supplierSuggestedActualRetailPrice;
	}

	public void setCurrentActualRetailPrice(BigDecimal currentActualRetailPrice) {
		this.currentActualRetailPrice = currentActualRetailPrice;
	}

	public void setEstimatedDrsRetainment(BigDecimal estimatedDrsRetainment) {
		this.estimatedDrsRetainment = estimatedDrsRetainment;
	}

	public void setReferralRate(BigDecimal referralRate) {
		this.referralRate = referralRate;
	}

	public void setEstimatedMarketplaceFees(BigDecimal estimatedMarketplaceFees) {
		this.estimatedMarketplaceFees = estimatedMarketplaceFees;
	}

	public void setEstimatedFulfillmentFees(BigDecimal estimatedFulfillmentFees) {
		this.estimatedFulfillmentFees = estimatedFulfillmentFees;
	}

	public void setEstimatedImportDuty(BigDecimal estimatedImportDuty) {
		this.estimatedImportDuty = estimatedImportDuty;
	}

	public void setEstimatedFreightCharge(BigDecimal estimatedFreightCharge) {
		this.estimatedFreightCharge = estimatedFreightCharge;
	}

	public void setApproxSupplierNetRevenue(BigDecimal approxSupplierNetRevenue) {
		this.approxSupplierNetRevenue = approxSupplierNetRevenue;
	}
}
