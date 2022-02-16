package com.kindminds.drs.web.view.onboardingApplication;

import java.io.Serializable;

public class OnboardingApplicationDetail implements Serializable{

	private static final long serialVersionUID = -4578638608084669448L;
	private String id;
	private String baseProductCode;
	private String supplierKcode;
	private String country;
	private String jsonData;
	private String status;
		
	public String getId(){
		return this.id;		
	};
	
	public void setId(String id){
		this.id = id;		
	};
		
	public String getBaseProductCode(){
		return this.baseProductCode;			
	};
	
	public void setBaseProductCode(String baseProductCode){
		this.baseProductCode = baseProductCode;			
	};
		
	public String getSupplierKcode(){
		return this.supplierKcode;		
	};
	
	public void setSupplierKcode(String supplierKcode){
		this.supplierKcode = supplierKcode;		
	};
		
	public String getCountry(){
		return this.country;		
	};
		
	public void setCountry(String country){
		this.country = country;		
	};
		
	public String getJsonData(){
		return this.jsonData;		
	};
	
	public void setJsonData(String jsonData){
		this.jsonData = jsonData;		
	};
		
	public String getStatus(){
		return this.status;		
	};
	
	public void setStatus(String status){
		this.status = status;		
	};
			
}
