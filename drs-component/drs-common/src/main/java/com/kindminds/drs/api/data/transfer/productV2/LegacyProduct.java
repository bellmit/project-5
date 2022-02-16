package com.kindminds.drs.api.data.transfer.productV2;

import com.kindminds.drs.enums.OnboardingApplicationStatusType;



import java.util.List;

public final class LegacyProduct {
    
    private String productBaseCode;
    
    private String supplierKcode;
    
    private ProductDetail productInfoSource;
    
    private LegacyProductDetail productMarketingMaterialSource;
    
    private List<LegacyProductDetail> productInfoMarketSide;
    
    private List<LegacyProductDetail> productMarketingMaterialMarketSide;
    
    private OnboardingApplicationStatusType status;
    
    private String serialNumber;

    
    public final String getProductBaseCode() {
        return this.productBaseCode;
    }

    public final void setProductBaseCode( String var1) {
        this.productBaseCode = var1;
    }

    
    public final String getSupplierKcode() {
        return this.supplierKcode;
    }

    public final void setSupplierKcode( String var1) {
        this.supplierKcode = var1;
    }

    
    public final ProductDetail getProductInfoSource() {
        return this.productInfoSource;
    }

    public final void setProductInfoSource( ProductDetail var1) {
        this.productInfoSource = var1;
    }

    
    public final LegacyProductDetail getProductMarketingMaterialSource() {
        return this.productMarketingMaterialSource;
    }

    public final void setProductMarketingMaterialSource( LegacyProductDetail var1) {
        this.productMarketingMaterialSource = var1;
    }

    
    public final List<LegacyProductDetail> getProductInfoMarketSide() {
        return this.productInfoMarketSide;
    }

    public final void setProductInfoMarketSide( List var1) {
        this.productInfoMarketSide = var1;
    }

    
    public final List<LegacyProductDetail> getProductMarketingMaterialMarketSide() {
        return this.productMarketingMaterialMarketSide;
    }

    public final void setProductMarketingMaterialMarketSide( List var1) {
        this.productMarketingMaterialMarketSide = var1;
    }

    
    public final OnboardingApplicationStatusType getStatus() {
        return this.status;
    }

    public final void setStatus( OnboardingApplicationStatusType var1) {
        this.status = var1;
    }

    
    public final String getSerialNumber() {
        return this.serialNumber;
    }

    public final void setSerialNumber( String var1) {
        this.serialNumber = var1;
    }

    public LegacyProduct() {
        this("", "", (ProductDetail)null, (LegacyProductDetail)null, (List)null, (List)null, (OnboardingApplicationStatusType)null, "");
    }

    public LegacyProduct( String productBaseCode,  String supplierKcode,  ProductDetail productInfoSource,  LegacyProductDetail productMarketingMaterialSource,  List productInfoMarketSide,  List productMarketingMaterialMarketSide,  OnboardingApplicationStatusType status,  String serialNumber) {
        this.productBaseCode = productBaseCode;
        this.supplierKcode = supplierKcode;
        this.productInfoSource = productInfoSource;
        this.productMarketingMaterialSource = productMarketingMaterialSource;
        this.productInfoMarketSide = productInfoMarketSide;
        this.productMarketingMaterialMarketSide = productMarketingMaterialMarketSide;
        this.status = status;
        this.serialNumber = serialNumber;
    }
}
