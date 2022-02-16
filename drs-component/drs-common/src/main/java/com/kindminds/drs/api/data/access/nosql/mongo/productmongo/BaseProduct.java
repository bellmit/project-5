package com.kindminds.drs.api.data.access.nosql.mongo.productmongo;

import java.util.List;

//todo arthur someone create this package interface , but don't implement it
// ?
// refactor this package
public interface BaseProduct {

    String getId();

    void setId(String id);

    Integer getSupplierId();

    void setSupplierId(Integer supplierId);

    String getCategory();

    void setCategory(String category);

    String getBrandNameCH();

    void setBrandNameCH(String brandNameCH);

    String getBrandNameEN();

    void setBrandNameEN(String brandNameEN);

    String getProductNameCH();

    void setProductNameCH(String productNameCH);

    String getProductNameEN();

    void setProductNameEN(String productNameEN);

    String getVariationTheme();

    void setVariationTheme(String variationTheme);

    Integer getTotalSkus();

    void setTotalSkus(Integer totalSkus);

    Integer getPageSize();

    void setPageSize(Integer pageSize);

    Integer getCurrentIndex();

    void setCurrentIndex(Integer currentIndex);

    List<Sku> getSkus();

    void setSkus(List<Sku> skus);

    String getBatteryCategory();

    void setBatteryCategory(String batteryCategory);

    String getChargeable() ;

    void setChargeable(String chargeable);

    String getUn38();

    void setUn38(String un38);

    String getMsds();

    void setMsds(String msds);

    String getDroptest();

    void setDroptest(String droptest);

    String getAllergic();

    void setAllergic(String allergic);

    String getDangerous() ;

    void setDangerous(String dangerous);

    String getMedical() ;

    void setMedical(String medical);

    String getBattery() ;

    void setBattery(String battery) ;

    String getMaterialSelected();

    void setMaterialSelected(String materialSelected);

    String getAircargo() ;

    void setAircargo(String aircargo) ;

    String getSeafreight() ;

    void setSeafreight(String seafreight) ;

    String getCertification() ;

    void setCertification(String certification);

    String getFormatSelected() ;

    void setFormatSelected(String formatSelected) ;

    String getPackageSelected() ;

    void setPackageSelected(String packageSelected) ;

    String getWirelessSelected() ;

    void setWirelessSelected(String wirelessSelected) ;

    String getShowOthersInput() ;

    void setShowOthersInput(String showOthersInput) ;

    String getStartDate() ;

    void setStartDate(String startDate);

    String getAllFiles();

    void setAllFiles(String allFiles);

    String getBatteryQuantity();

    void setBatteryQuantity(String batteryQuantity);

    String getBatteryVoltage();

    void setBatteryVoltage(String batteryVoltage);

    String getBatteryCapacitance();

    void setBatteryCapacitance(String batteryCapacitance);

    String getBatteryNetWeight();

    void setBatteryNetWeight(String batteryNetWeight);
}
