package com.kindminds.drs.web.data.dto.product;

public class ProductMarketingMaterialMarketSide {

	private String supplierKcode;
	private String productBaseCode;
	private String country;
	private String jsonData;
	private String mmId;
	private String variationCode;
	
	public String getSupplierKcode() {		
		return this.supplierKcode;
	}

	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode;		
	}
	
	public String getProductBaseCode() {		
		return this.productBaseCode;
	}

	public void setProductBaseCode(String productBaseCode) {
		this.productBaseCode = productBaseCode;		
	}
	
	public String getMmId() {		
		return this.mmId;
	}
	
	public String getCountry(){
		return this.country;		
	}
	
	
	public String getVariationCode() {		
		return this.variationCode;
	}

	public void setVariationCode(String variationCode) {
		this.variationCode = variationCode; 		
	}
	
	public void setCountry(String country){
		this.country = country;		
	}
			
	public String getJsonData() {		
		return this.jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData; 		
	}
	
}