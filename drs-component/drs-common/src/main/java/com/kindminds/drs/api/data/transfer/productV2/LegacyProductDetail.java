package com.kindminds.drs.api.data.transfer.productV2;



public class LegacyProductDetail {
    
    private final String id;
    
    private final String productBaseCode;
    
    private final String supplierKcode;
    
    private final String jsonData;
    
    private final String status;
    
    private final String country;

    
    public String getId() {
        return this.id;
    }

    
    public String getProductBaseCode() {
        return this.productBaseCode;
    }

    
    public String getSupplierKcode() {
        return this.supplierKcode;
    }

    
    public String getJsonData() {
        return this.jsonData;
    }

    
    public String getStatus() {
        return this.status;
    }

    
    public String getCountry() {
        return this.country;
    }

    public LegacyProductDetail() {
        this("", "", "", "", "", "");
    }

    public LegacyProductDetail( String id,  String productBaseCode,  String supplierKcode,  String jsonData,  String status,  String country) {
        this.id = id;
        this.productBaseCode = productBaseCode;
        this.supplierKcode = supplierKcode;
        this.jsonData = jsonData;
        this.status = status;
        this.country = country;
    }
}
