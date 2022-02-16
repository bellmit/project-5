package com.kindminds.drs.api.v1.model;



import com.kindminds.drs.api.v1.model.product.SKU;

import java.util.List;

public interface Company {
	public String getKcode();
	public String getNameEnUs();
	public String getNameLocal();
	public String getShortNameEnUs();
	public String getShortNameLocal();
	public String getAddress();
	public String getPhoneNumber();
	public List<String> getServiceEmailList();
	public List<String> getCouponList();
	public List<String> getProductEmailList();
	public String getCurrency();
	public String getCountryCode();	
	public String getOfficialRegistrationNo();
	public String getBankName();
	public String getBankBranchName();
	public String getAccountNumber();
	public String getAccountName();	
	public String getNotes();
	public String getHandlerKcode();
	public Boolean getIsDrsCompany();
	public Boolean getIsSupplier();
	public List<SKU> getSkus();
	public Boolean getActivated();
	    
}