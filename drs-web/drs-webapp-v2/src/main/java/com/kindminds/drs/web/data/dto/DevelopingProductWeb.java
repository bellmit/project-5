package com.kindminds.drs.web.data.dto;

public class DevelopingProductWeb {
	
	private String productBaseCode;
	private String productInfoSource;
	private String productInfoMarketSide;
	private String productMarketingMaterialSource;
	private String productMarketingMaterialMarketSide;
	private String serialNumber;
	
	public String getProductBaseCode() {
		return this.productBaseCode;
	}
	
	public void SetProductBaseCode(String productBaseCode) {
		this.productBaseCode = productBaseCode;		
	}
				
	public String getProductInfoSource() {
		return this.productInfoSource;
	}
	
	public void setProductInfoSource(String productInfoSource) {
		this.productInfoSource = productInfoSource;		
	}
		
	public String getProductInfoMarketSide() {
		return this.productInfoMarketSide;
	}
	
	public void setProductInfoMarketSide(String productInfoMarketSide) {
		this.productInfoMarketSide = productInfoMarketSide;		
	}
		
	public String getProductMarketingMaterialSource() {		
		return this.productMarketingMaterialSource;
	}
	
	public String getSerialNumber() {
		return this.serialNumber;
	}
	
	public void setProductMarketingMaterialSource(String productMarketingMaterialSource) {
		this.productMarketingMaterialSource = productMarketingMaterialSource;		
	}
	
	public String getProductMarketingMaterialMarketSide() {
		return this.productMarketingMaterialMarketSide;
	}
	
	public void setProductMarketingMaterialMarketSide(String productMarketingMaterialMarketSide) {
		this.productMarketingMaterialMarketSide = productMarketingMaterialMarketSide;		
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber.isEmpty()?null:serialNumber;
	}
}