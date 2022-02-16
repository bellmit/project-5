package com.kindminds.drs.web.data.dto;

import java.util.List;

import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.SKU;

public class SKUImpl implements SKU{
	
	private String supplierKcode;
	private String baseProductCode;
	private String codeBySupplier;
	private String codeByDrs;
	private String nameBySupplier;
	private String nameByDrs;
	private String productEAN;
	private String eanProvider;
	private String status;
	private String manufacturingLeadTimeDays;
	private boolean containLithiumIonBattery;
	private String internalNote;
	private List<ProductMarketplaceInfo> productMarketplaceInfo;
		
	@Override
	public String getSupplierKcode(){
		return this.supplierKcode;		
	}
	
	public void setSupplierKcode(String supplierKcode){
		this.supplierKcode = supplierKcode;				
	}
	
	@Override
	public String getBaseProductCode(){
		return this.baseProductCode;		
	}
	
	public void setBaseProductCode(String baseProductCode){
		this.baseProductCode = baseProductCode;		
	}
	
	@Override
	public String getCodeBySupplier(){
		return this.codeBySupplier;				
	}
	
	public void setCodeBySupplier(String codeBySupplier){
		this.codeBySupplier = codeBySupplier;		
	}
	
	@Override
	public String getCodeByDrs(){
		return this.codeByDrs;		
	}
	
	public void setCodeByDrs(String codeByDrs){
		this.codeByDrs = codeByDrs;		
	}
	
	@Override
	public String getNameBySupplier(){
		return this.nameBySupplier;
	}
	
	public void setNameBySupplier(String nameBySupplier){
		this.nameBySupplier = nameBySupplier;		
	}
	
	@Override
	public String getNameByDrs(){
		return this.nameByDrs;		
	}
	
	public void setNameByDrs(String nameByDrs){
		this.nameByDrs = nameByDrs;	
	}
	
	@Override
	public String getProductEAN(){
		return this.productEAN;		
	}
	
	public void setProductEAN(String productEAN){
		this.productEAN = productEAN;		
	}
	
	@Override
	public String getEanProvider() {		
		return this.eanProvider;
	}
	
	public void setEanProvider(String eanProvider) {
		this.eanProvider = eanProvider;		
	}
	
	@Override
	public String getManufacturingLeadTimeDays() {		
		return this.manufacturingLeadTimeDays;
	}

	public void setManufacturingLeadTimeDays(String manufacturingLeadTimeDays) {
		this.manufacturingLeadTimeDays = manufacturingLeadTimeDays;		
	}
		
	@Override
	public boolean getContainLithiumIonBattery() {		
		return this.containLithiumIonBattery;
	}
	
	public void setContainLithiumIonBattery(boolean containLithiumIonBattery) {
		this.containLithiumIonBattery = containLithiumIonBattery;
	}
			
	@Override
	public String getStatus(){		
		return this.status;		
	}
	
	public void setStatus(String status){
		this.status = status;		
	}
	
	@Override
	public String getInternalNote(){
		return this.internalNote;		
	}
	
    public void setInternalNote(String internalNote){
    	this.internalNote = internalNote;   	
    }

	@Override
	public List<ProductMarketplaceInfo> getRegionInfoList() {		
		return this.productMarketplaceInfo;
	}
		
}