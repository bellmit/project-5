package com.kindminds.drs.web.view.product;

import java.util.List;

public class BaseProductOnboarding   {
	
		
	private String productBaseCode;
	
	private String supplierKcode;	
	
	private String jsonData;		
	
	
	private BaseProductOnboardingDetail productInfoSource=null;
	
	private BaseProductOnboardingDetail productMarketingMaterialSource=null;	
	
	private List<BaseProductOnboardingDetail> productInfoMarketSide=null;
	
	private List<BaseProductOnboardingDetail> productMarketingMaterialMarketSide=null;
	

	public String getProductBaseCode() {		
		return this.productBaseCode;
	}
		
	
	public String getSupplierKcode() {
		return this.supplierKcode;
	}
	
	
	public String getJsonData() {
		return this.jsonData;
	}
	
	
	public BaseProductOnboardingDetail getProductInfoSource() {
		return this.productInfoSource;
	}
		
	public void setProductInfoSource(BaseProductOnboardingDetail productInfoSource) {
		this.productInfoSource = productInfoSource; 		
	}
	
	
	public BaseProductOnboardingDetail getProductMarketingMaterialSource() {		
		return this.productMarketingMaterialSource;
	}
	
	public void setProductMarketingMaterialSource(BaseProductOnboardingDetail productMarketingMaterialSource) {
		this.productMarketingMaterialSource = productMarketingMaterialSource;		
	}
		
	
	public List<BaseProductOnboardingDetail> getProductInfoMarketSide() {
		return this.productInfoMarketSide;
	}
	
	public void setProductInfoMarketSide(List<BaseProductOnboardingDetail> productInfoMarketSide) {
		this.productInfoMarketSide = productInfoMarketSide;		
	}
	
	
	public List<BaseProductOnboardingDetail> getProductMarketingMaterialMarketSide() {
		return this.productMarketingMaterialMarketSide;
	}
	
	public void setProductMarketingMaterialMarketSide(List<BaseProductOnboardingDetail> productMarketingMaterialMarketSide) {
		this.productMarketingMaterialMarketSide = productMarketingMaterialMarketSide;		
	}
	
		
	
	
}
