package com.kindminds.drs.persist.v1.model.mapping;

import java.util.List;






import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.Company;
import com.kindminds.drs.api.v1.model.product.SKU;


public class CompanyImpl implements Company {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name = "k_code")
	private String kCode;
	//@Column(name = "name_en_us")
	private String nameEnUs;
	//@Column(name = "name_local")
	private String nameLocal;
	//@Column(name = "short_name_en_us")
	private String shortNameEnUs;
	//@Column(name = "short_name_local")
	private String shortNameLocal;
	//@Column(name = "address")
	private String address;
	//@Column(name = "phone_number")
	private String phoneNumber;
	//@Column(name = "currency_name")
	private String currencyName;
	//@Column(name = "country_code")
	private String countryCode;
	//@Column(name = "official_registration_number")
	private String officialRegistrationNumber;
	//@Column(name="bank_name")
	private String bankName;
	//@Column(name="bank_branch_name")
	private String bankBranchName;
	//@Column(name="bank_account_code")
	private String bankAccountCode;
	//@Column(name="bank_account_name")
	private String bankAccountName;
	//@Column(name = "notes")
	private String notes;
	//@Column(name = "handler_kcode")
	private String handlerKcode;
	//@Column(name = "is_drs_company")
	private Boolean isDrsCompany ;
	//@Column(name = "is_supplier")
	private Boolean isSupplier;

	private List<String> serviceEmailList = null;

	private List<SKU> skuList=null;

	private List<String> couponList=null;

	private List<String> productEmailList = null;

	private Boolean activated;


	public CompanyImpl() {
	}

	public CompanyImpl(int id, String kCode, String nameEnUs, String nameLocal, String shortNameEnUs, String shortNameLocal, String address, String phoneNumber, String currencyName, String countryCode, String officialRegistrationNumber, String bankName, String bankBranchName, String bankAccountCode, String bankAccountName, String notes, String handlerKcode, Boolean isDrsCompany, Boolean isSupplier, Boolean activated) {
		this.id = id;
		this.kCode = kCode;
		this.nameEnUs = nameEnUs;
		this.nameLocal = nameLocal;
		this.shortNameEnUs = shortNameEnUs;
		this.shortNameLocal = shortNameLocal;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.currencyName = currencyName;
		this.countryCode = countryCode;
		this.officialRegistrationNumber = officialRegistrationNumber;
		this.bankName = bankName;
		this.bankBranchName = bankBranchName;
		this.bankAccountCode = bankAccountCode;
		this.bankAccountName = bankAccountName;
		this.notes = notes;
		this.handlerKcode = handlerKcode;
		this.isDrsCompany = isDrsCompany;
		this.isSupplier = isSupplier;
		this.activated =activated;
	}

	public void setServiceEmailList(List<String> list){
		this.serviceEmailList=list;
	}

	public void setProductEmailList(List<String> list) {
		this.productEmailList = list;
	}

	public void setSkuList(List<SKU> list){
		this.skuList=list;
	}

	public void setCouponList(List<String> list){
		this.couponList=list;
	}

	@Override
	public String toString() {
		return "CompanyImpl [id=" + id + ", kCode=" + kCode + ", nameEnUs=" + nameEnUs + ", nameLocal=" + nameLocal
				+ ", shortNameEnUs=" + shortNameEnUs + ", shortNameLocal=" + shortNameLocal + ", address=" + address
				+ ", phoneNumber=" + phoneNumber + ", currencyName=" + currencyName + ", countryCode=" + countryCode
				+ ", officialRegistrationNumber=" + officialRegistrationNumber + ", bankName=" + bankName
				+ ", bankBranchName=" + bankBranchName + ", bankAccountCode=" + bankAccountCode + ", bankAccountName="
				+ bankAccountName + ", notes=" + notes + ", handlerKcode=" + handlerKcode + ", isDrsCompany="
				+ isDrsCompany + ", isSupplier=" + isSupplier + ", serviceEmailList=" + serviceEmailList + ", skuList="
				+ skuList + ", couponList=" + couponList + "]";
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

	@Override
	public List<String> getCouponList() {
		return this.couponList;
	}
}
