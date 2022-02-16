package com.kindminds.drs.api.data.transfer.productV2;

public class GsProductData {

    private final String baseProductCode ;

    private final String sku ;

    private final String gtin ;

    private final String productName ;

    private final String productCategory ;

    private final String supplierCompany  ;

    private final String brandEng  ;

    private final String countryOfOrigin  ;

    private final String hsCode  ;

    private final String manufacturerLeadTime  ;

    private final String fcaPrice  ;

    private final String productDimensionL  ;

    private final String productDimensionW  ;

    private final String productDimensionH  ;

    private final String productDimensionUnit  ;

    private final String productMaterialPercent  ;

    private final String productWeight  ;

    private final String productWeightUnit  ;

    private final String country ;

    private final String salesPrice ;

    private final String note ;

    private final String packageDimensionL ;

    private final String packageDimensionW ;

    private final String packageDimensionH ;

    private final String packageDimensionUnit ;

    private final String packageWeight ;

    private final String packageWeightUnit ;

    private final String quantityPerCarton ;

    private final String cartonDimensionL ;

    private final String cartonDimensionW ;

    private final String cartonDimensionH ;

    private final String cartonDimensionUnit ;

    private final String cartonWeight ;

    private final String cartonWeightUnit ;

    private final String importDutyRate ;

    private final String salesSideHsCode ;

    private final String unfulFillableInventoryRemovalSettings ;

    private final String ddpPrice ;

    public String getBaseProductCode() {
        return this.baseProductCode;
    }

    public String getSku() {
        return this.sku;
    }

    public String getGtin() {
        return this.gtin;
    }

    public String getProductName() {
        return this.productName;
    }

    public String getProductCategory() {
        return this.productCategory;
    }

    public String getSupplierCompany() {
        return this.supplierCompany;
    }

    public String getBrandEng() {
        return this.brandEng;
    }

    public String getCountryOfOrigin() {
        return this.countryOfOrigin;
    }

    public String getHsCode() {
        return this.hsCode;
    }

    public String getManufacturerLeadTime() {
        return this.manufacturerLeadTime;
    }

    public String getFcaPrice() {
        return this.fcaPrice;
    }

    public String getProductDimensionL() {
        return this.productDimensionL;
    }

    public String getProductDimensionW() {
        return this.productDimensionW;
    }

    public String getProductDimensionH() {
        return this.productDimensionH;
    }

    public String getProductDimensionUnit() {
        return this.productDimensionUnit;
    }

    public String getProductMaterialPercent() {
        return this.productMaterialPercent;
    }

    public String getProductWeight() {
        return this.productWeight;
    }

    public String getProductWeightUnit() {
        return this.productWeightUnit;
    }

    public String getCountry() {
        return this.country;
    }

    public String getSalesPrice() {
        return this.salesPrice;
    }

    public String getNote() {
        return this.note;
    }

    public String getPackageDimensionL() {
        return this.packageDimensionL;
    }

    public String getPackageDimensionW() {
        return this.packageDimensionW;
    }

    public String getPackageDimensionH() {
        return this.packageDimensionH;
    }

    public String getPackageDimensionUnit() {
        return this.packageDimensionUnit;
    }

    public String getPackageWeight() {
        return this.packageWeight;
    }

    public String getPackageWeightUnit() {
        return this.packageWeightUnit;
    }

    public String getQuantityPerCarton() {
        return this.quantityPerCarton;
    }

    public String getCartonDimensionL() {
        return this.cartonDimensionL;
    }

    public String getCartonDimensionW() {
        return this.cartonDimensionW;
    }

    public String getCartonDimensionH() {
        return this.cartonDimensionH;
    }

    public String getCartonDimensionUnit() {
        return this.cartonDimensionUnit;
    }

    public String getCartonWeight() {
        return this.cartonWeight;
    }

    public String getCartonWeightUnit() {
        return this.cartonWeightUnit;
    }

    public String getImportDutyRate() {
        return this.importDutyRate;
    }

    public String getSalesSideHsCode() {
        return this.salesSideHsCode;
    }

    public String getUnfulFillableInventoryRemovalSettings() {
        return this.unfulFillableInventoryRemovalSettings;
    }

    public String getDdpPrice() {
        return this.ddpPrice;
    }

    public GsProductData() {
        this("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    public GsProductData(String baseProductCode, String sku, String gtin, String productName, String productCategory, String supplierCompany, String brandEng, String countryOfOrigin, String hsCode, String manufacturerLeadTime, String fcaPrice, String productDimensionL, String productDimensionW, String productDimensionH, String productDimensionUnit, String productMaterialPercent, String productWeight, String productWeightUnit, String country, String salesPrice, String note, String packageDimensionL, String packageDimensionW, String packageDimensionH, String packageDimensionUnit, String packageWeight, String packageWeightUnit, String quantityPerCarton, String cartonDimensionL, String cartonDimensionW, String cartonDimensionH, String cartonDimensionUnit, String cartonWeight, String cartonWeightUnit, String importDutyRate, String salesSideHsCode, String unfulFillableInventoryRemovalSettings, String ddpPrice) {
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
}
