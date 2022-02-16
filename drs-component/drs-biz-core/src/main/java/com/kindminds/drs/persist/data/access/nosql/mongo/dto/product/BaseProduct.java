package com.kindminds.drs.persist.data.access.nosql.mongo.dto.product;

import java.util.List;

//todo arthur this calls need to be refactored
//all package need to be refactored
//@Document(collection = "product")
public class BaseProduct {

    //@Id
    private String bpId;

//    @Field(name = "supplier_id")
    private String supplierId;

//    @Field(name= "category")
    private String category;

//    @Field(name = "brand_name_ch")
    private String brandNameCH;

//    @Field(name = "brand_name_en")
    private String  brandNameEN;

//    @Field(name = "product_name_ch")
    private String productNameCH;

//    @Field(name = "product_name_en")
    private String productNameEN;

//    @Field(name = "variation_theme")
    private String variationTheme;

    private List<Theme> multiTheme;

//    @Field(name = "total_skus")
    private Integer totalSkus;

//    @Field(name = "page size")
    private Integer pageSize;

//    @Field(name = "current_index")
    private Integer currentIndex;

    private String bpStatus;

    private String categoryVersion;

//    @Field(name = "child_products")
    private List<Sku> skus;

//    @Field(name = "batteryCategory")
    private String batteryCategory;

//    @Field(name = "chargeable")
    private String chargeable;

//    @Field(name = "un38")
    private String un38;

//    @Field(name = "msds")
    private String msds;

//    @Field(name = "droptest")
    private String droptest;

//    @Field(name = "allergic")
    private String allergic;

//    @Field(name = "dangerous")
    private String dangerous;

//    @Field(name = "medical")
    private String medical;

//    @Field(name = "battery")
    private String battery;

//    @Field(name = "materialSelected")
    private String materialSelected;

//    @Field(name = "aircargo")
    private String aircargo;

//    @Field(name = "seafreight")
    private String seafreight;

//    @Field(name = "certification")
    private String certification;

//    @Field(name = "formatSelected")
    private String formatSelected;

//    @Field(name = "packageSelected")
    private String packageSelected;

//    @Field(name = "wirelessSelected")
    private String wirelessSelected;

//    @Field(name = "showOthersInput")
    private String showOthersInput;

//    @Field(name = "startDate")
    private String startDate;

//    @Field(name = "allFiles")
    private String allFiles;

//    @Field(name = "batteryQuantity")
    private String batteryQuantity;

//    @Field(name = "batteryVoltage")
    private String batteryVoltage;

//    @Field(name = "batteryCapacitance")
    private String batteryCapacitance;

//    @Field(name = "batteryNetWeight")
    private String batteryNetWeight;

    public BaseProduct() {
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrandNameCH() {
        return brandNameCH;
    }

    public void setBrandNameCH(String brandNameCH) {
        this.brandNameCH = brandNameCH;
    }

    public String getBrandNameEN() {
        return brandNameEN;
    }

    public void setBrandNameEN(String brandNameEN) {
        this.brandNameEN = brandNameEN;
    }

    public String getProductNameCH() {
        return productNameCH;
    }

    public void setProductNameCH(String productNameCH) {
        this.productNameCH = productNameCH;
    }

    public String getProductNameEN() {
        return productNameEN;
    }

    public void setProductNameEN(String productNameEN) {
        this.productNameEN = productNameEN;
    }

    public String getVariationTheme() {
        return variationTheme;
    }

    public void setVariationTheme(String variationTheme) {
        this.variationTheme = variationTheme;
    }

    public List<Theme> getMultiTheme() {
        return multiTheme;
    }

    public void setMultiTheme(List<Theme> multiTheme) {
        this.multiTheme = multiTheme;
    }

    public Integer getTotalSkus() {
        return totalSkus;
    }

    public void setTotalSkus(Integer totalSkus) {
        this.totalSkus = totalSkus;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getBpStatus() {
        return bpStatus;
    }

    public void setBpStatus(String bpStatus) {
        this.bpStatus = bpStatus;
    }

    public String getCategoryVersion() {
        return categoryVersion;
    }

    public void setCategoryVersion(String categoryVersion) {
        this.categoryVersion = categoryVersion;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }


    public String getBatteryCategory() {
        return batteryCategory;
    }

    public void setBatteryCategory(String batteryCategory) {
        this.batteryCategory = batteryCategory;
    }

    public String getChargeable() {
        return chargeable;
    }

    public void setChargeable(String chargeable) {
        this.chargeable = chargeable;
    }

    public String getUn38() {
        return un38;
    }

    public void setUn38(String un38) {
        this.un38 = un38;
    }

    public String getMsds() {
        return msds;
    }

    public void setMsds(String msds) {
        this.msds = msds;
    }

    public String getDroptest() {
        return droptest;
    }

    public void setDroptest(String droptest) {
        this.droptest = droptest;
    }

    public String getAllergic() {
        return allergic;
    }

    public void setAllergic(String allergic) {
        this.allergic = allergic;
    }

    public String getDangerous() {
        return dangerous;
    }

    public void setDangerous(String dangerous) {
        this.dangerous = dangerous;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getMaterialSelected() {
        return materialSelected;
    }

    public void setMaterialSelected(String materialSelected) {
        this.materialSelected = materialSelected;
    }

    public String getAircargo() {
        return aircargo;
    }

    public void setAircargo(String aircargo) {
        this.aircargo = aircargo;
    }

    public String getSeafreight() {
        return seafreight;
    }

    public void setSeafreight(String seafreight) {
        this.seafreight = seafreight;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getFormatSelected() {
        return formatSelected;
    }

    public void setFormatSelected(String formatSelected) {
        this.formatSelected = formatSelected;
    }

    public String getPackageSelected() {
        return packageSelected;
    }

    public void setPackageSelected(String packageSelected) {
        this.packageSelected = packageSelected;
    }

    public String getWirelessSelected() {
        return wirelessSelected;
    }

    public void setWirelessSelected(String wirelessSelected) {
        this.wirelessSelected = wirelessSelected;
    }

    public String getShowOthersInput() {
        return showOthersInput;
    }

    public void setShowOthersInput(String showOthersInput) {
        this.showOthersInput = showOthersInput;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getAllFiles() {
        return allFiles;
    }

    public void setAllFiles(String allFiles) {
        this.allFiles = allFiles;
    }

    public String getBatteryQuantity() {
        return batteryQuantity;
    }

    public void setBatteryQuantity(String batteryQuantity) {
        this.batteryQuantity = batteryQuantity;
    }

    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public String getBatteryCapacitance() {
        return batteryCapacitance;
    }

    public void setBatteryCapacitance(String batteryCapacitance) {
        this.batteryCapacitance = batteryCapacitance;
    }

    public String getBatteryNetWeight() {
        return batteryNetWeight;
    }

    public void setBatteryNetWeight(String batteryNetWeight) {
        this.batteryNetWeight = batteryNetWeight;
    }

    @Override
    public String toString() {
        return "Product{" +
                "bpId='" + bpId + '\'' +
                ", brandNameCH='" + brandNameCH + '\'' +
                ", brandNameEN='" + brandNameEN + '\'' +
                ", productNameCH='" + productNameCH + '\'' +
                ", productNameEN='" + productNameEN + '\'' +
                ", variationTheme='" + variationTheme + '\'' +
                ", skus=" + skus +
                ", batteryCategory='" + batteryCategory + '\'' +
                ", chargeable='" + chargeable + '\'' +
                ", un38='" + un38 + '\'' +
                ", msds='" + msds + '\'' +
                ", droptest='" + droptest + '\'' +
                ", allergic='" + allergic + '\'' +
                ", dangerous='" + dangerous + '\'' +
                ", medical='" + medical + '\'' +
                ", battery='" + battery + '\'' +
                ", materialSelected='" + materialSelected + '\'' +
                ", aircargo='" + aircargo + '\'' +
                ", seafreight='" + seafreight + '\'' +
                ", certification='" + certification + '\'' +
                ", formatSelected='" + formatSelected + '\'' +
                ", packageSelected='" + packageSelected + '\'' +
                ", wirelessSelected='" + wirelessSelected + '\'' +
                ", showOthersInput='" + showOthersInput + '\'' +
                ", startDate='" + startDate + '\'' +
                ", allFiles='" + allFiles + '\'' +
                ", batteryQuantity='" + batteryQuantity + '\'' +
                ", batteryVoltage='" + batteryVoltage + '\'' +
                ", batteryCapacitance='" + batteryCapacitance + '\'' +
                ", batteryNetWeight='" + batteryNetWeight + '\'' +
                '}';
    }
}
