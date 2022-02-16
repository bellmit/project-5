package com.kindminds.drs.impl;



import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;

import java.util.List;


public class BaseProductOnboardingImplForTest {
	
	private String productBaseCode;
	private String supplierKcode;		
	private String jsonData;			
	private ProductDetail productInfoSource;
	private ProductDetail productMarketingMaterialSource;	
	private List<ProductDetail> productInfoMarketSideList;
	private List<ProductDetail> productMarketingMaterialMarketSideList;
		
	public BaseProductOnboardingImplForTest(
			String productBaseCode,
			String supplierKcode,
			String jsonData,
			ProductDetail productInfoSource,
			ProductDetail productMarketingMaterialSource,
			List<ProductDetail> productInfoMarketSide,
			List<ProductDetail> productMarketingMaterialMarketSide){
		this.productBaseCode = productBaseCode;
		this.supplierKcode = supplierKcode;
		this.jsonData = jsonData;
		this.productInfoSource = productInfoSource;
		this.productMarketingMaterialSource = productMarketingMaterialSource; 
		this.productInfoMarketSideList = productInfoMarketSide; 
		this.productMarketingMaterialMarketSideList = productMarketingMaterialMarketSide; 		
	}
	
	public void addProductInfoMarketSide(ProductDetail productInfoMarketSide){
		this.productInfoMarketSideList.add(productInfoMarketSide);				
	}
	
	public void addProductMarketingMaterialMarketSide(ProductDetail productMarketingMaterialMarketSide){
		this.productMarketingMaterialMarketSideList.add(productMarketingMaterialMarketSide);		
	}
	
	@Override
	public String toString() {
		return "BaseProductOnboardingImpl [getProductBaseCode()=" + getProductBaseCode()
				+ ", getSupplierKcode()=" + getSupplierKcode() + ", getJsonData()=" + getJsonData()
				+ ", getProductInfoSource()=" + getProductInfoSource() + ", getProductMarketingMaterialSource()="
				+ getProductMarketingMaterialSource() + ", getProductInfoMarketSide()=" + getProductInfoMarketSide()
				+ ", getProductMarketingMaterialMarketSide()=" + getProductMarketingMaterialMarketSide() + "]";
	}


	public boolean equals(Object obj){
		/*
		if(obj instanceof ProductDto){
			ProductDto baseProductOnboarding = (ProductDto) obj;
			return this.getProductBaseCode().equals(baseProductOnboarding.getProductBaseCode())			
			&& TestUtil.nullableEquals(this.getSupplierKcode(),baseProductOnboarding.getSupplierKcode())
			&& TestUtil.nullableEquals(this.getProductInfoSource(),baseProductOnboarding.getProductInfoSource())
			&& TestUtil.nullableEquals(this.getProductMarketingMaterialSource(),baseProductOnboarding.getProductMarketingMaterialSource())
			&& TestUtil.nullableEquals(this.getProductInfoMarketSide(),baseProductOnboarding.getProductInfoMarketSide())
			&& TestUtil.nullableEquals(this.getProductMarketingMaterialMarketSide(),baseProductOnboarding.getProductMarketingMaterialMarketSide());			
		}	
		*/	
		return false;
	}
	
	
	public String getProductBaseCode() {	
		return this.productBaseCode;
	}

	
	public String getSupplierKcode() {		
		return this.supplierKcode;
	}

	
	public String getJsonData() {		
		return this.jsonData;
	}

	
	public ProductDetail getProductInfoSource() {
		return this.productInfoSource;
	}

	
	public ProductDetail getProductMarketingMaterialSource() {
		return this.productMarketingMaterialSource;
	}

	
	public List<ProductDetail> getProductInfoMarketSide() {
		return this.productInfoMarketSideList;
	}

	
	public List<ProductDetail> getProductMarketingMaterialMarketSide() {
		return this.productMarketingMaterialMarketSideList;
	}

}