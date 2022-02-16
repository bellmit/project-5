package com.kindminds.drs.persist.v1.model.mapping.amazon;


import com.kindminds.drs.api.v1.model.amazon.AmazonReviewNotification;

public class AmazonReviewNotificationImpl implements AmazonReviewNotification {
    //@Id
    ////@Column(name="id")
    private int productId;
    //@Column(name="supplier_kcode")
    private String supplierKcode;
    //@Column(name="supplier_name")
    private String supplierName;
    //@Column(name="product_name")
    private String productName;
    //@Column(name="asin")
    private String asin;

    public AmazonReviewNotificationImpl(int productId, String supplierKcode, String supplierName, String productName, String asin) {
        this.productId = productId;
        this.supplierKcode = supplierKcode;
        this.supplierName = supplierName;
        this.productName = productName;
        this.asin = asin;
    }

    @Override
    public int getProductId() {
        return this.productId;
    }

    @Override
    public String getSupplierKcode() {
        return this.supplierKcode;
    }

    @Override
    public String getSupplierName() {
        return this.supplierName;
    }

    @Override
    public String getProductName() {
        return this.productName;
    }

    @Override
    public String getAsin() {
        return this.asin;
    }
}