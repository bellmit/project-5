package com.kindminds.drs.core.biz;

import com.kindminds.drs.Currency;




import java.util.List;


public  class CompanyImpl {

    private final int id;
    private final String kCode;
    private final String nameEnUs;
    private final String nameLocal;
    private final String shortNameEnUs;
    private final String shortNameLocal;
    private final String address;
    private final String phoneNumber;
    private final String currencyName;
    private final String countryCode;
    private final String officialRegistrationNumber;
    private final String bankName;
    private final String bankBranchName;
    private final String bankAccountCode;
    private final String bankAccountName;
    private final String notes;
    private final String handlerKcode;
    private final Boolean isDrsCompany;
    private final Boolean isSupplier;
    private List serviceEmailList;

    public CompanyImpl(int id, String kCode, String nameEnUs, String nameLocal, String shortNameEnUs, String shortNameLocal, String address, String phoneNumber, String currencyName, String countryCode, String officialRegistrationNumber, String bankName, String bankBranchName, String bankAccountCode, String bankAccountName, String notes, String handlerKcode, Boolean isDrsCompany, Boolean isSupplier) {
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
    }

    public final void setServiceEmailList( List list) {
        this.serviceEmailList = list;
    }

    
    public final String getKcode() {
        return this.kCode;
    }

    
    public final String getNameEnUs() {
        return this.nameEnUs;
    }

    
    public final String getNameLocal() {
        return this.nameLocal;
    }

    
    public final String getShortNameEnUs() {
        return this.shortNameEnUs;
    }

    
    public final String getShortNameLocal() {
        return this.shortNameLocal;
    }

    
    public final String getAddress() {
        return this.address;
    }

    
    public final String getPhoneNumber() {
        return this.phoneNumber;
    }

    
    public final List getServiceEmailList() {
        return this.serviceEmailList;
    }

    
    public final String getCurrency() {
        return this.currencyName == null ? null : Currency.valueOf(this.currencyName).name();
    }

    
    public final String getCountryCode() {
        return this.countryCode;
    }

    
    public final String getOfficialRegistrationNo() {
        return this.officialRegistrationNumber;
    }

    
    public final String getBankName() {
        return this.bankName;
    }

    
    public final String getBankBranchName() {
        return this.bankBranchName;
    }

    
    public final String getAccountNumber() {
        return this.bankAccountCode;
    }

    
    public final String getAccountName() {
        return this.bankAccountName;
    }

    
    public final String getNotes() {
        return this.notes;
    }

    
    public final String getHandlerKcode() {
        return this.handlerKcode;
    }

    
    public final Boolean getIsDrsCompany() {
        return this.isDrsCompany;
    }

    
    public final Boolean getIsSupplier() {
        return this.isSupplier;
    }
}
