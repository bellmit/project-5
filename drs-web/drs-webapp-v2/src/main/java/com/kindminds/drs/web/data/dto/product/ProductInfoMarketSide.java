package com.kindminds.drs.web.data.dto.product;

public class ProductInfoMarketSide {
	
	private String supplierKcode;	
	private String productBaseCode;
	private String serialNumber;
	private String country;
	private String jsonData;
	
	public String getSupplierKcode() {		
		return this.supplierKcode;
	}

	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode;		
	}
		
	public String getProductBaseCode() {		
		return this.productBaseCode;
	}
	
	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setProductBaseCode(String productBaseCode) {
		this.productBaseCode = productBaseCode;		
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getCountry(){
		return this.country;		
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