package com.kindminds.drs.persist.v1.model.mapping.product;

import com.kindminds.drs.api.data.transfer.productV2.GsProductData;

public class GsProductDataImpl extends GsProductData {

    //@Column(name="base_product_code")
    private String baseProductCode ;
    //@Column(name="sku")
    private String sku ;
    //@Column(name="gtin")
    private String gtin ;
    //@Column(name="product_name")
    private String productName ;
    //@Column(name="product_category")
    private String productCategory ;
    //@Column(name="supplier_company")
    private String supplierCompany  ;
    //@Column(name="brand_eng")
    private String brandEng  ;
    //@Column(name="country_of_origin")
    private String countryOfOrigin  ;
    //@Column(name="hs_code")
    private String hsCode  ;
    //@Column(name="manufacturer_lead_time")
    private String manufacturerLeadTime  ;
    //@Column(name="fca_price")
    private String fcaPrice  ;
    //@Column(name="product_dimension_l")
    private String productDimensionL  ;
    //@Column(name="product_dimension_w")
    private String productDimensionW  ;
    //@Column(name="product_dimension_h")
    private String productDimensionH  ;
    //@Column(name="product_dimension_unit")
    private String productDimensionUnit  ;
    //@Column(name="product_material_percent")
    private String productMaterialPercent  ;
    //@Column(name="product_weight")
    private String productWeight  ;
    //@Column(name="product_weight_unit")
    private String productWeightUnit  ;

    private String country ;

    private String salesPrice ;

    private String note ;

    private String packageDimensionL ;

    private String packageDimensionW ;

    private String packageDimensionH ;

    private String packageDimensionUnit ;

    private String packageWeight ;

    private String packageWeightUnit ;

    private String quantityPerCarton ;

    private String cartonDimensionL ;

    private String cartonDimensionW ;

    private String cartonDimensionH ;

    private String cartonDimensionUnit ;

    private String cartonWeight ;

    private String cartonWeightUnit ;

    private String importDutyRate ;

    private String salesSideHsCode ;

    private String unfulFillableInventoryRemovalSettings ;

    private String ddpPrice ;


    public GsProductDataImpl(){}

    public GsProductDataImpl(String baseProductCode, String sku, String gtin, String productName, String productCategory, String supplierCompany, String brandEng, String countryOfOrigin, String hsCode, String manufacturerLeadTime, String fcaPrice, String productDimensionL, String productDimensionW, String productDimensionH, String productDimensionUnit, String productMaterialPercent, String productWeight, String productWeightUnit, String country, String salesPrice, String note, String packageDimensionL, String packageDimensionW, String packageDimensionH, String packageDimensionUnit, String packageWeight, String packageWeightUnit, String quantityPerCarton, String cartonDimensionL, String cartonDimensionW, String cartonDimensionH, String cartonDimensionUnit, String cartonWeight, String cartonWeightUnit, String importDutyRate, String salesSideHsCode, String unfulFillableInventoryRemovalSettings, String ddpPrice) {
        this.baseProductCode = baseProductCode;
        this.sku = sku;
        this.gtin = gtin;
        this.productName = productName;
        this.productCategory = productCategory;
        this.supplierCompany = supplierCompany;
        this.brandEng = brandEng;
        this.countryOfOrigin = countryOfOrigin;
        this.hsCode = hsCode;
        this.manufacturerLeadTime = manufacturerLeadTime;
        this.fcaPrice = fcaPrice;
        this.productDimensionL = productDimensionL;
        this.productDimensionW = productDimensionW;
        this.productDimensionH = productDimensionH;
        this.productDimensionUnit = productDimensionUnit;
        this.productMaterialPercent = productMaterialPercent;
        this.productWeight = productWeight;
        this.productWeightUnit = productWeightUnit;
        this.country = country;
        this.salesPrice = salesPrice;
        this.note = note;
        this.packageDimensionL = packageDimensionL;
        this.packageDimensionW = packageDimensionW;
        this.packageDimensionH = packageDimensionH;
        this.packageDimensionUnit = packageDimensionUnit;
        this.packageWeight = packageWeight;
        this.packageWeightUnit = packageWeightUnit;
        this.quantityPerCarton = quantityPerCarton;
        this.cartonDimensionL = cartonDimensionL;
        this.cartonDimensionW = cartonDimensionW;
        this.cartonDimensionH = cartonDimensionH;
        this.cartonDimensionUnit = cartonDimensionUnit;
        this.cartonWeight = cartonWeight;
        this.cartonWeightUnit = cartonWeightUnit;
        this.importDutyRate = importDutyRate;
        this.salesSideHsCode = salesSideHsCode;
        this.unfulFillableInventoryRemovalSettings = unfulFillableInventoryRemovalSettings;
        this.ddpPrice = ddpPrice;
    }

    @Override
    public String getBaseProductCode() {
        return this.baseProductCode;
    }

    @Override
    public String getSku() {
        return this.sku;
    }

    @Override
    public String getGtin() {
        return this.gtin;
    }

    @Override
    public String getProductName() {
        return this.productName;
    }

    @Override
    public String getProductCategory() {
        return this.productCategory;
    }

    @Override
    public String getSupplierCompany() {
        return this.supplierCompany;
    }

    @Override
    public String getBrandEng() {
        return this.brandEng;
    }

    @Override
    public String getCountryOfOrigin() {
        return this.countryOfOrigin;
    }

    @Override
    public String getHsCode() {
        return this.hsCode;
    }

    @Override
    public String getManufacturerLeadTime() {
        return this.manufacturerLeadTime;
    }

    @Override
    public String getFcaPrice() {
        return this.fcaPrice;
    }

    @Override
    public String getProductDimensionL() {
        return this.productDimensionL;
    }

    @Override
    public String getProductDimensionW() {
        return this.productDimensionW;
    }

    @Override
    public String getProductDimensionH() {
        return this.productDimensionH;
    }

    @Override
    public String getProductDimensionUnit() {
        return this.productDimensionUnit;
    }

    @Override
    public String getProductMaterialPercent() {
        return this.productMaterialPercent;
    }

    @Override
    public String getProductWeight() {
        return this.productWeight;
    }

    @Override
    public String getProductWeightUnit() {
        return this.productWeightUnit;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public String getSalesPrice() {
        return this.salesPrice;
    }

    @Override
    public String getNote() {
        return this.note;
    }

    @Override
    public String getPackageDimensionL() {
        return this.packageDimensionL;
    }

    @Override
    public String getPackageDimensionW() {
        return this.packageDimensionW;
    }
    @Override
    public String getPackageDimensionH() {
        return this.packageDimensionH;
    }

    @Override
    public String getPackageDimensionUnit() {
        return this.packageDimensionUnit;
    }

    @Override
    public String getPackageWeight() {
        return this.packageWeight;
    }

    @Override
    public String getPackageWeightUnit() {
        return this.packageWeightUnit;
    }

    @Override
    public String getQuantityPerCarton() {
        return this.quantityPerCarton;
    }

    @Override
    public String getCartonDimensionL() {
        return this.cartonDimensionL;
    }

    @Override
    public String getCartonDimensionW() {
        return this.cartonDimensionW;
    }

    @Override
    public String getCartonDimensionH() {
        return this.cartonDimensionH;
    }

    @Override
    public String getCartonDimensionUnit() {
        return this.cartonDimensionUnit;
    }

    @Override
    public String getCartonWeight() {
        return this.cartonWeight;
    }

    @Override
    public String getCartonWeightUnit() {
        return this.cartonWeightUnit;
    }

    @Override
    public String getImportDutyRate() {
        return this.importDutyRate;
    }

    @Override
    public String getSalesSideHsCode() {
        return this.salesSideHsCode;
    }

    @Override
    public String getUnfulFillableInventoryRemovalSettings() {
        return this.unfulFillableInventoryRemovalSettings;
    }

    @Override
    public String getDdpPrice() {
        return this.ddpPrice;
    }
}