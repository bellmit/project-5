package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.SKU;

import java.util.List;

public class ProductSkuImplForTest implements SKU {
	private String supplierCompanyName;
	private String productBaseCode;
	private String codeBySupplier;
	private String codeByDrs;
	private String nameBySupplier;
	private String nameByDrs;
	private String ean;
	private String eanProvider;
	private Status status;
	private String manufacturingLeadTimeDays;
	private boolean containLithiumIonBattery;
	private String internalNotes;
	private List<ProductMarketplaceInfo> regionInfoList = null;
	
	public ProductSkuImplForTest(
			String supplierCompanyName,
			String productBaseCode,
			String codeBySupplier,
			String codeByDrs,
			String nameBySupplier,
			String nameByDrs, 
			String ean,
			String eanProvider,
			Status status,
			String manufacturingLeadTimeDays,
			boolean containLithiumIonBattery,
			String internalNotes) {
		this.supplierCompanyName = supplierCompanyName;
		this.productBaseCode = productBaseCode;
		this.codeBySupplier = codeBySupplier;
		this.codeByDrs = codeByDrs;
		this.nameBySupplier = nameBySupplier;
		this.nameByDrs = nameByDrs;
		this.ean = ean;
		this.eanProvider = eanProvider;
		this.status = status;
		this.manufacturingLeadTimeDays = manufacturingLeadTimeDays;
		this.containLithiumIonBattery = containLithiumIonBattery;
		this.internalNotes = internalNotes;
	}
	
	public void setRegionInfoList(List<ProductMarketplaceInfo> infoList){
		this.regionInfoList = infoList;
	}
	
	public void addProductSkuRegionInfo(ProductMarketplaceInfo info){
		this.regionInfoList.add(info);
	}
	
	@Override
	public boolean equals(Object obj){
		if ( obj instanceof SKU ){
			SKU sku = ((SKU) obj);
			return this.getSupplierKcode().equals(sku.getSupplierKcode())
				&& this.getBaseProductCode().equals(sku.getBaseProductCode())
				&& this.getCodeBySupplier().equals(sku.getCodeBySupplier())
				&& this.getCodeByDrs().equals(sku.getCodeByDrs())
				&& this.getNameBySupplier().equals(sku.getNameBySupplier())
				&& this.getNameByDrs().equals(sku.getNameByDrs())
				&& this.getProductEAN().equals(sku.getProductEAN())
				&& this.getEanProvider().equals(sku.getEanProvider())
				&& this.getStatus().equals(sku.getStatus())
				&& this.getManufacturingLeadTimeDays().equals(sku.getManufacturingLeadTimeDays())
				&& this.getContainLithiumIonBattery()==sku.getContainLithiumIonBattery()
				&& this.getInternalNote().equals(sku.getInternalNote())
				&& this.marketplaceInfoEquals(this.getRegionInfoList(), sku.getRegionInfoList());
		}
	    return false;
	}
	
	private boolean marketplaceInfoEquals(List<ProductMarketplaceInfo> a,List<ProductMarketplaceInfo> b){
		if(a==null&&b==null) return true;
		else{
			if(a.size()==b.size()){
				for(int i=0;i<a.size();i++){
					if(!a.get(i).getMarketplace().equals(b.get(i).getMarketplace())) return false;
					if(!a.get(i).getStatus().equals(b.get(i).getStatus())) return false;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "ProductSkuImplForTest [getSupplierKcode()=" + getSupplierKcode() + ", getBaseProductCode()="
				+ getBaseProductCode() + ", getCodeBySupplier()=" + getCodeBySupplier() + ", getCodeByDrs()="
				+ getCodeByDrs() + ", getNameBySupplier()=" + getNameBySupplier() + ", getNameByDrs()=" + getNameByDrs()
				+ ", getProductEAN()=" + getProductEAN() + ", getEanProvider()=" + getEanProvider() + ", getStatus()="
				+ getStatus() + ", getManufacturingLeadTimeDays()=" + getManufacturingLeadTimeDays()
				+ ", getContainLithiumIonBattery()=" + getContainLithiumIonBattery() + ", getInternalNote()="
				+ getInternalNote() + ", getRegionInfoList()=" + getRegionInfoList() + "]";
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierCompanyName;
	}
	
	@Override
	public String getBaseProductCode() {
		return this.productBaseCode;
	}
	
	@Override
	public String getCodeBySupplier() {
		return this.codeBySupplier;
	}
	
	@Override
	public String getCodeByDrs() {
		return this.codeByDrs;
	}
	
	@Override
	public String getNameBySupplier() {
		return this.nameBySupplier;
	}
	
	@Override
	public String getNameByDrs() {
		return this.nameByDrs;
	}
	
	@Override
	public String getProductEAN() {
		return this.ean;
	}
	
	@Override
	public String getEanProvider() {
		return this.eanProvider;
	}

	@Override
	public String getStatus() {
		if(this.status==null) return null;
		return this.status.name();
	}
	@Override
	public String getManufacturingLeadTimeDays() {
		return this.manufacturingLeadTimeDays;
	}

	@Override
	public boolean getContainLithiumIonBattery() {
		return this.containLithiumIonBattery;
	}

	@Override
	public String getInternalNote() {
		return this.internalNotes;
	}
	
	@Override
	public List<ProductMarketplaceInfo> getRegionInfoList() {
		return this.regionInfoList;
	}

}

