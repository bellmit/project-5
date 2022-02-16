package com.kindminds.drs.web.data.dto;

import java.util.List;
import java.util.Map;

public class DevelopingProductListWeb {

	private String baseProduct;
	private String productName;
	private String supplierKcode;
	private List<Map<String,Object>> variationProducts;
		
	public String getBaseProduct(){
		return this.baseProduct;		
	}
	
	public void setBaseProduct(String baseProduct){
		this.baseProduct = baseProduct;		
	}

	public String getProductName(){
		return this.productName;		
	}
	
	public void setProductName(String productName){
		this.productName = productName;		
	}
	
	public String getSupplierKcode(){
		return this.supplierKcode;		
	}
	
	public void setSupplierKcode(String supplierKcode){
		this.supplierKcode = supplierKcode;		
	}
		
	public List<Map<String,Object>> getVariationProducts(){
		return this.variationProducts;		
	}
	
	public void setVariationProducts(List<Map<String,Object>> variationProducts){
		this.variationProducts = variationProducts;		
	}
			
}