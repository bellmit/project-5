package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.product.BaseProductOnboardingWithSKU;

import java.util.ArrayList;
import java.util.List;

public class BaseProductOnboardingWithSKUImplForTest implements BaseProductOnboardingWithSKU{

	private String productBaseCode;
	private String supplierKcode;
	private String status;
	private String variationType1;
	private String variationType2;
	private String productWithVariation;
	
	private List<BaseProductOnboardingSKUItem> SKULineItems = new ArrayList<BaseProductOnboardingSKUItem>(); 
	
	public BaseProductOnboardingWithSKUImplForTest(){};
	
	public BaseProductOnboardingWithSKUImplForTest(
			String productBaseCode,
			String supplierKcode,
			String status,
			String variationType1,
			String variationType2,
			String productWithVariation,
			List<BaseProductOnboardingSKUItem> SKULineItems){
		this.productBaseCode = productBaseCode;
		this.supplierKcode = supplierKcode;
		this.status = status;
		this.variationType1 = variationType1;
		this.variationType2 = variationType2;
		this.productWithVariation = productWithVariation; 
		this.SKULineItems = SKULineItems;
	}
		
	@Override
	public String getProductBaseCode() {
		return this.productBaseCode;
	}

	public void setProductBaseCode(String productBaseCode) {
		this.productBaseCode = productBaseCode;		
	}
		
	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode; 				
	}

	@Override
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;		
	}
	
	@Override
	public String getVariationType1() {
		return this.variationType1;
	}

	public void setVariationType1(String variationType1) {
		this.variationType1 = variationType1;
	}
	
	@Override
	public String getVariationType2() {
		return this.variationType2;
	}

	public void setVariationType2(String variationType2) {
		this.variationType2 = variationType2;
	}
		
	@Override
	public String getProductWithVariation() {
		return this.productWithVariation;
	}
	
	public void setProductWithVariation(String productWithVariation) {
		this.productWithVariation = productWithVariation;
	}
	
	@Override
	public List<BaseProductOnboardingSKUItem> getSKULineItems() {
		return this.SKULineItems;
	}

	public void setSKULineItems(List<BaseProductOnboardingSKUItem> SKULineItems) {
		this.SKULineItems = SKULineItems;		
	}
	
}
