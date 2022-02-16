package com.kindminds.drs.web.view.product;



import java.util.ArrayList;
import java.util.List;


public class BaseProductOnboardingWithSKU {
	
	private String productBaseCode;
	private String supplierKcode;
	private String status;
	private String variationType1;
	private String variationType2;
	private String productWithVariation;	
	
	private List<BaseProductOnboardingSKUItem> SKULineItems = new ArrayList<BaseProductOnboardingSKUItem>();; 
	
	public BaseProductOnboardingWithSKU(){}
	
	public BaseProductOnboardingWithSKU(BaseProductOnboardingWithSKU original){
		this.productBaseCode = original.getProductBaseCode();
		this.supplierKcode = original.getSupplierKcode();
		this.status = original.getStatus();
		this.variationType1 = original.getVariationType1();
		this.variationType2 = original.getVariationType2();
		this.productWithVariation = original.getProductWithVariation();
		for(BaseProductOnboardingSKUItem origItem:original.getSKULineItems()){
			this.SKULineItems.add(new BaseProductOnboardingSKUItem(origItem));			
		}		
	}
		
	@Override
	public String toString() {
		return "BaseProductOnboardingWithSKUImpl [getProductBaseCode()=" + getProductBaseCode()
				+ ", getSupplierKcode()=" + getSupplierKcode() + ", getStatus()=" + getStatus()
				+ ", getVariationType1()=" + getVariationType1() + ", getVariationType2()=" + getVariationType2()
				+ ", getProductWithVariation()=" + getProductWithVariation() + ", getSKULineItems()="
				+ getSKULineItems() + ", getSKULineItem()=" + getSKULineItem() + "]";
	}

	
	public String getProductBaseCode() {
		return this.productBaseCode;
	}

	public void setProductBaseCode(String productBaseCode) {
		this.productBaseCode = productBaseCode;		
	}
		
	
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode; 				
	}
		
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;		
	}
	
	
	public String getVariationType1() {
		return this.variationType1;
	}

	public void setVariationType1(String variationType1) {
		this.variationType1 = variationType1;		
	}
		
	
	public String getVariationType2() {
		return this.variationType2;
	}

	public void setVariationType2(String variationType2) {
		this.variationType2 = variationType2;
	}
	
	
	public String getProductWithVariation() {
		return this.productWithVariation;
	}
	
	public void setProductWithVariation(String productWithVariation) {
		this.productWithVariation = productWithVariation;
	}
	
	
	public List<BaseProductOnboardingSKUItem> getSKULineItems() {
		List<BaseProductOnboardingSKUItem> items = new ArrayList<BaseProductOnboardingSKUItem>();
		for (BaseProductOnboardingSKUItem item : this.SKULineItems) {			
			if(item.getOriginalSKU()!= null){			
			items.add(item);			
			}
		}
		return items;
	}

	public List<BaseProductOnboardingSKUItem> getSKULineItem(){
		return this.SKULineItems;				
	}
	
	public void setSKULineItem(List<BaseProductOnboardingSKUItem> lineItem){
		this.SKULineItems = lineItem;				
	}

}
