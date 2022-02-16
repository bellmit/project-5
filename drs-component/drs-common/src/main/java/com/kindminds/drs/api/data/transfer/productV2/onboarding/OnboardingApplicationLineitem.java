package com.kindminds.drs.data.transfer.productV2.onboarding;

import com.kindminds.drs.api.data.transfer.productV2.ProductDto;

import com.kindminds.drs.util.Encryptor;

public final class OnboardingApplicationLineitem {
    
    private final String id;
    
    private final String productBaseCode;
    
    private final String supplierKcode;
    
    private final ProductDto product;
    
    private final Integer statusKey;
    
    private final String serialNumber;

    
    public final String getId() {
        return this.id;
    }

    
    public final String getProductBaseCode() {
        return this.productBaseCode;
    }

    
    public final String getSupplierKcode() {
        return this.supplierKcode;
    }

    
    public final ProductDto getProduct() {
        return this.product;
    }

    
    public final Integer getStatusKey() {
        return this.statusKey;
    }

    
    public final String getSerialNumber() {
        return this.serialNumber;
    }

    public OnboardingApplicationLineitem() {
        this("", "", "", (ProductDto)null, (Integer)null, "");
    }

    public OnboardingApplicationLineitem( String id,  String productBaseCode,  String supplierKcode,  ProductDto product,  Integer statusKey,  String serialNumber) {
        this.id = Encryptor.encrypt(id);
        this.productBaseCode = productBaseCode;
        this.supplierKcode = supplierKcode;
        this.product = product;
        this.statusKey = statusKey;
        this.serialNumber = serialNumber;
    }
}
