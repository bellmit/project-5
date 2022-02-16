package com.kindminds.drs.web.data.dto;

import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.api.v1.model.Company;
import com.kindminds.drs.api.v1.model.product.SKU;

public class CompanyImpl implements Company{
		
	private String kCode;
	private String nameEnUs;
	private String nameLocal;
	private String shortNameEnUs;
	private String shortNameLocal;
	private String currency;
	private String countryCode;
	private String address;
	private String phoneNumber;
	private List<String> serviceEmailList;
	private List<String> couponKeywordList;
	private List<String> productEmailList;
	private String officialRegistrationNo;
	private String bankName;
	private String bankBranchName;
	private String accountNumber;
	private String accountName;	
	private String notes;
	private String handlerCompanyKCode;
	private Boolean isDrsCompany;
	private Boolean isSupplier;
	private List<SKU> skus = new ArrayList<SKU>();
	private Boolean activated;
		
	@Override
	public String getKcode(){
		return this.kCode;		
	}
	
	public void setKcode(String kCode){
		 this.kCode = kCode;		
	}
		
	@Override
	public String getNameEnUs(){
		return this.nameEnUs;
	}

	public void setNameEnUs(String nameEnUs){
		this.nameEnUs = nameEnUs;				
	}
		
	@Override
	public String getNameLocal(){
		return this.nameLocal;
	}

	public void setNameLocal(String nameLocal){
		this.nameLocal = nameLocal; 				
	}
		
	@Override
	public String getShortNameEnUs(){
		return this.shortNameEnUs;
	}

	public void setShortNameEnUs(String shortNameEnUs) {
		this.shortNameEnUs = shortNameEnUs;		
	}
		
	@Override
	public String getShortNameLocal(){
		return this.shortNameLocal;
	}
	
	public void setShortNameLocal(String shortNameLocal){
		this.shortNameLocal = shortNameLocal;				
	}
	
	@Override
	public String getCurrency(){
		return this.currency;
	}
	
	public void setCurrency(String currency){
		this.currency = currency;
	}
	
	@Override
	public String getCountryCode(){
		return this.countryCode;
	}
	
	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;		
	}
	
	@Override
	public String getAddress() {		
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String getPhoneNumber() {		
		return this.phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public List<String> getServiceEmailList() {		
		return this.serviceEmailList;
	}
	
	public void setServiceEmailList(List<String> serviceEmailList) {
		this.serviceEmailList = serviceEmailList;		
	}

	@Override
	public List<String> getCouponList() {
		return this.couponKeywordList;
	}

	public void setCouponList(List<String> couponKeywordList) {
		this.couponKeywordList = couponKeywordList;
	}


	@Override
	public List<String> getProductEmailList() {
		return this.productEmailList;
	}

	public void setProductEmailList(List<String> productEmailList) {
		this.productEmailList = productEmailList;
	}

	@Override
	public String getOfficialRegistrationNo() {		
		return this.officialRegistrationNo;
	}
	
	public void setOfficialRegistrationNo(String officialRegistrationNo) {
		this.officialRegistrationNo = officialRegistrationNo;		
	}
	
	@Override
	public String getBankName() {		
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName; 		
	}
		
	@Override
	public String getBankBranchName() {		
		return this.bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;		
	}
	
	@Override
	public String getAccountNumber() {		
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	@Override
	public String getAccountName() {		
		return this.accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName; 		
	}
	
	@Override
	public String getNotes(){
		return this.notes;		
	}
	
	public void setNotes(String notes){
		this.notes = notes; 				
	}
	
	@Override
	public String getHandlerKcode() {
		return this.handlerCompanyKCode;
	}
	
	public void setHandlerKcode(String handlerCompanyKCode) {
		this.handlerCompanyKCode = handlerCompanyKCode;				
	}
		
	@Override
	public Boolean getIsDrsCompany() {
		return this.isDrsCompany;
	}
	
	public void setIsDrsCompany(Boolean isDrsCompany) {
		this.isDrsCompany = isDrsCompany; 		
	}
		
	@Override
	public Boolean getIsSupplier() {
		return this.isSupplier;
	}
	
	public void setIsSupplier(Boolean isSupplier) {
		this.isSupplier = isSupplier; 		
	}
	
	@Override
	public List<SKU> getSkus() {
		return this.skus;
	}

	public void setSkus(List<SKU> skus){
		this.skus = skus;		
	}

	@Override
	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
}