package com.kindminds.drs.api.data.transfer.productV2;

public class ProductDetail {


    private final String id;

    private final String productBaseCode;

    private final String supplierKcode;

    private final String data;

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


    public String getData() {
        return this.data;
    }


    public String getStatus() {
        return this.status;
    }


    public String getCountry() {
        return this.country;
    }

    public ProductDetail() {
        this("", "", "", "", "", "");
    }

    public ProductDetail( String id, String productBaseCode, String supplierKcode,
                          String data, String status, String country) {
        this.id = id;
        this.productBaseCode = productBaseCode;
        this.supplierKcode = supplierKcode;
        this.data = data;
        this.status = status;
        this.country = country;
    }
}
