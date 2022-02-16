package com.kindminds.drs.api.data.transfer.productV2;

import java.util.List;

public class ProductDto {
    private final String productBaseCode;

    private final String supplierKcode;

    private final String jsonData;

    private final ProductDetail productInfoSource;

    private final ProductDetail productMarketingMaterialSource;

    private final List productInfoMarketSide;

    private final List productMarketingMaterialMarketSide;


    public String getProductBaseCode() {
        return this.productBaseCode;
    }


    public String getSupplierKcode() {
        return this.supplierKcode;
    }


    public String getJsonData() {
        return this.jsonData;
    }


    public ProductDetail getProductInfoSource() {
        return this.productInfoSource;
    }


    public ProductDetail getProductMarketingMaterialSource() {
        return this.productMarketingMaterialSource;
    }


    public List getProductInfoMarketSide() {
        return this.productInfoMarketSide;
    }


    public List getProductMarketingMaterialMarketSide() {
        return this.productMarketingMaterialMarketSide;
    }

    public ProductDto() {
        this("", "", "", (ProductDetail)null, (ProductDetail)null, (List)null, (List)null);
    }

    public ProductDto( String productBaseCode,  String supplierKcode,  String jsonData,
                       ProductDetail productInfoSource,
                       ProductDetail productMarketingMaterialSource, List productInfoMarketSide,
                       List productMarketingMaterialMarketSide) {
        this.productBaseCode = productBaseCode;
        this.supplierKcode = supplierKcode;
        this.jsonData = jsonData;
        this.productInfoSource = productInfoSource;
        this.productMarketingMaterialSource = productMarketingMaterialSource;
        this.productInfoMarketSide = productInfoMarketSide;
        this.productMarketingMaterialMarketSide = productMarketingMaterialMarketSide;
    }
}
