package com.kindminds.drs.web.data.dto.product;

public class ProductVersion {

	private String supplierKcode;
	private String productBaseCode;
	private String sourcejsonData;
	private String marketSide;	
	private String marketMaterial;
	
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
	
	
	public String getSourcejsonData() {		
		return this.sourcejsonData;
	}

	public void setSourcejsonData(String sourcejsonData) {
		this.sourcejsonData = sourcejsonData; 		
	}
	
	public String getMarketSide() {
		return this.marketSide;		
	}
	
	public void setMarketSide(String marketSide) {
		this.marketSide = marketSide; 		
	}
	
	public String getMarketMaterial() {
		return this.marketMaterial;		
	}
	
	public void setMarketMaterial(String marketMaterial) {
		this.marketMaterial = marketMaterial; 		
	}
		
}