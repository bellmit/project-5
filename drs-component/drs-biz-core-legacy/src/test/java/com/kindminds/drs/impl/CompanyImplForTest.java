package com.kindminds.drs.impl;

import com.kindminds.drs.Currency;

import com.kindminds.drs.api.v1.model.Company;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.util.TestUtil;

import java.util.List;

;

public class CompanyImplForTest implements Company {
	
	private String kCode;
	private String nameEnUs;
	private String nameLocal;
	private String shortNameEnUs;
	private String shortNameLocal;
	private String address;
	private String phoneNumber;
	private List<String> serviceEmailList=null;
	private List<String> productEmailList=null;
	private String currencyName;
	private String countryCode;
	private String officialRegistrationNumber;
	private String bankName;
	private String bankBranchName;
	private String bankAccountCode;
	private String bankAccountName;
	private String notes;
	private String handlerKcode;
	private Boolean isDrsCompany;
	private Boolean isSupplier;
	private Boolean activated;
	
	private List<SKU> skuList=null;
	private List<String> couponList=null;
	
	public CompanyImplForTest (
			String kCode, 
			String nameEnUs, 
			String nameLocal,
			String shortNameEnUs, 
			String shortNameLocal,
			String address,
			String phoneNumber,
			List<String> serviceEmailList,
			List<String> couponList,
			List<String> productEmailList,
			String currencyName, 
			String countryCode,
			String officialRegistrationNumber,
			String bankName,
			String bankBranchName,
			String bankAccountCode,
			String bankAccountName,
			String notes,
			String handlerCompanyKCode, 
			Boolean isDrsCompany, 
			Boolean isSupplier
			){
		this.kCode = kCode;
		this.nameEnUs = nameEnUs;
		this.nameLocal = nameLocal;
		this.shortNameEnUs = shortNameEnUs;
		this.shortNameLocal = shortNameLocal;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.serviceEmailList = serviceEmailList;
		this.productEmailList = productEmailList;
		this.currencyName = currencyName;
		this.countryCode = countryCode;
		this.officialRegistrationNumber = officialRegistrationNumber;
		this.bankName = bankName;
		this.bankBranchName = bankBranchName;
		this.bankAccountCode = bankAccountCode;
		this.bankAccountName = bankAccountName ;
		this.notes = notes;
		this.handlerKcode = handlerCompanyKCode;
		this.isDrsCompany = isDrsCompany;
		this.isSupplier = isSupplier;
		this.couponList = couponList;
	}
	
	public void setSkuList(List<SKU> list){
		this.skuList = list;
	}
	
	@Override
	public boolean equals(Object obj){
		if ( obj instanceof Company){
			Company c = ((Company) obj);
			return this.getKcode().equals(c.getKcode())
				&& TestUtil.nullableEquals(this.getNameEnUs(),c.getNameEnUs())
				&& TestUtil.nullableEquals(this.getNameLocal(),c.getNameLocal())
				&& TestUtil.nullableEquals(this.getShortNameEnUs(),c.getShortNameEnUs())
				&& this.getShortNameLocal().equals(c.getShortNameLocal())
				&& TestUtil.nullableEquals(this.getAddress(),c.getAddress())
				&& TestUtil.nullableEquals(this.getPhoneNumber(),c.getPhoneNumber())
				&& TestUtil.nullableEquals(this.getServiceEmailList(),c.getServiceEmailList())
				&& TestUtil.nullableEquals(this.getProductEmailList(),c.getProductEmailList())
				&& TestUtil.nullableEquals(this.getCurrency(),c.getCurrency())
				&& TestUtil.nullableEquals(this.getCountryCode(),c.getCountryCode())
				&& TestUtil.nullableEquals(this.getOfficialRegistrationNo(),c.getOfficialRegistrationNo())
				&& TestUtil.nullableEquals(this.getBankName(),c.getBankName())
				&& TestUtil.nullableEquals(this.getBankBranchName(),c.getBankBranchName())
				&& TestUtil.nullableEquals(this.getAccountNumber(),c.getAccountNumber())
				&& TestUtil.nullableEquals(this.getAccountName(),c.getAccountName())
				&& TestUtil.nullableEquals(this.getNotes(),c.getNotes())
				&& TestUtil.nullableEquals(this.getHandlerKcode(),c.getHandlerKcode())
				&& TestUtil.nullableEquals(this.getIsDrsCompany(),c.getIsDrsCompany())
				&& TestUtil.nullableEquals(this.getIsSupplier(),c.getIsSupplier())
				&& this.skuListEqual(this.getSkus(),c.getSkus());
		}
		else {
	      return false;
	    }
	}
	
	private boolean skuListEqual(List<SKU> list1,List<SKU> list2 ){
		if(list1==null&&list2==null) return true;
		else{
			if(list1.size()==list2.size()){
				for(int i=0;i<list1.size();i++){
					if(!list1.get(i).getCodeByDrs().equals(list2.get(i).getCodeByDrs())) return false;
					if(!list1.get(i).getNameByDrs().equals(list2.get(i).getNameByDrs())) return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "CompanyImplForTest [getKcode()=" + getKcode() + ", getNameEnUs()=" + getNameEnUs() + ", getNameLocal()="
				+ getNameLocal() + ", getShortNameEnUs()=" + getShortNameEnUs() + ", getShortNameLocal()="
				+ getShortNameLocal() + ", getAddress()=" + getAddress() + ", getPhoneNumber()=" + getPhoneNumber()
				+ ", getServiceEmailList()=" + getServiceEmailList() + ", getCouponList()=" + getCouponList()
				+ ", getProductEmailList()=" + getProductEmailList() + ", getCurrency()=" + getCurrency()
				+ ", getCountryCode()=" + getCountryCode() + ", getOfficialRegistrationNo()="
				+ getOfficialRegistrationNo() + ", getBankName()=" + getBankName() + ", getBankBranchName()="
				+ getBankBranchName() + ", getAccountNumber()=" + getAccountNumber() + ", getAccountName()="
				+ getAccountName() + ", getNotes()=" + getNotes() + ", getHandlerKcode()=" + getHandlerKcode()
				+ ", getIsDrsCompany()=" + getIsDrsCompany() + ", getIsSupplier()=" + getIsSupplier() + ", getSkus()="
				+ getSkus() + "]";
	}

	@Override
	public String getKcode() {
		return this.kCode;
	}
	
	@Override
	public String getNameEnUs() {
		return this.nameEnUs;
	}
	
	@Override
	public String getNameLocal() {
		return this.nameLocal;
	}
	
	@Override
	public String getShortNameEnUs() {
		return this.shortNameEnUs;
	}
	
	@Override
	public String getShortNameLocal() {
		return this.shortNameLocal;
	}
	
	@Override
	public String getAddress() {
		return this.address;
	}
	
	@Override
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	@Override
	public List<String> getServiceEmailList() {
		return this.serviceEmailList;
	}

	@Override
	public List<String> getCouponList() {
		return this.couponList;
	}
	
	@Override
	public List<String> getProductEmailList() {
		return this.productEmailList;
	}
	
	@Override
	public String getCurrency() {
		if(this.currencyName==null) return null;
		return Currency.valueOf(this.currencyName).name();
	}
	
	@Override
	public String getCountryCode() {
		return this.countryCode;
	}
	
	@Override
	public String getOfficialRegistrationNo() {
		return this.officialRegistrationNumber;
	}
	
	@Override
	public String getBankName() {
		return this.bankName;
	}
	
	@Override
	public String getBankBranchName() {
		return this.bankBranchName;
	}
	
	@Override
	public String getAccountNumber() {
		return this.bankAccountCode;
	}
	
	@Override
	public String getAccountName() {
		return this.bankAccountName;
	}
	
	@Override
	public String getNotes() {
		return this.notes;
	}
	
	@Override
	public String getHandlerKcode() {
		return this.handlerKcode;
	}
	
	@Override
	public Boolean getIsDrsCompany() {
		return this.isDrsCompany;
	}
	
	@Override
	public Boolean getIsSupplier() {
		return this.isSupplier;
	}
	
	@Override
	public List<SKU> getSkus() {
		return this.skuList;
	}

	@Override
	public Boolean getActivated() {
		return this.activated;
	}

}