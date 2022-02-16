package com.kindminds.drs.web.view.onboardingApplication;

import java.io.Serializable;
import java.util.List;

public class OnboardingApplication implements Serializable{
	
	private static final long serialVersionUID = -1549578511059798409L;
	private String serialNumber; 
	private String supplierKcode;
	private String productBaseCode;
	private String status;
	private OnboardingApplicationDetail productInfoSource;
	private OnboardingApplicationDetail productMarketingMaterialSource;
	private List<OnboardingApplicationDetail> productInfoMarketSide = null;
	private List<OnboardingApplicationDetail> productMarketingMaterialMarketSide = null;
			
	public String getSerialNumber(){
		return this.serialNumber;			
	};
	
	public void setSerialNumber(String serialNumber){
		this.serialNumber = serialNumber;			
	}; 
		
	public String getProductBaseCode(){
		return this.productBaseCode;		
	};
	
	public void setProductBaseCode(String productBaseCode){
		this.productBaseCode = productBaseCode;		
	};
		
    public String getSupplierKcode(){
    	return this.supplierKcode; 	
    };
    
    public void setSupplierKcode(String supplierKcode){
    	this.supplierKcode = supplierKcode; 	
    };
        
    public String getStatus(){
    	return this.status;    	
    };
    
    public void setStatus(String status){
    	this.status = status;    	
    };
       
    public OnboardingApplicationDetail getProductInfoSource(){
    	return this.productInfoSource;    	
    };
    
    public void setProductInfoSource(OnboardingApplicationDetail productInfoSource){
    	this.productInfoSource = productInfoSource;    	
    };
     
    public OnboardingApplicationDetail getProductMarketingMaterialSource(){
    	return this.productMarketingMaterialSource;
    };
        
    public void setProductMarketingMaterialSource(OnboardingApplicationDetail productMarketingMaterialSource){
    	this.productMarketingMaterialSource = productMarketingMaterialSource;
    };
        
    public List<OnboardingApplicationDetail> getProductInfoMarketSide(){
    	return this.productInfoMarketSide;
    };
    
    public void setProductInfoMarketSide(List<OnboardingApplicationDetail> productInfoMarketSide){
    	this.productInfoMarketSide = productInfoMarketSide;
    };
       
    public List<OnboardingApplicationDetail> getProductMarketingMaterialMarketSide(){
    	return this.productMarketingMaterialMarketSide;    	
    };
    
    public void setProductMarketingMaterialMarketSide(List<OnboardingApplicationDetail> productMarketingMaterialMarketSide){
    	this.productMarketingMaterialMarketSide = productMarketingMaterialMarketSide;    	
    };
	
}