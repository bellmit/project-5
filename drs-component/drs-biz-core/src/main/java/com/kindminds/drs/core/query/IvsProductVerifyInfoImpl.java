package com.kindminds.drs.core.query;


import com.kindminds.drs.api.data.transfer.logistics.IvsProductVerifyInfo;

import java.math.BigDecimal;

public  class IvsProductVerifyInfoImpl implements IvsProductVerifyInfo {
     
    private String sku;
     
    private String productName;
     
    private String baseProductCode;
     
    private String exportName;
     
    private String productNameLocal;
     
    private String brandEng;
     
    private String countryOfOrigin;
     
    private String marketHsCode;
     
    private String hsCode;
     
    private String exportTariffReq;
     
    private String importTariffReq;
     
    private BigDecimal fcaPrice;
   
    private Integer quantity;
     
    private BigDecimal fcaTotal;
     
    private BigDecimal inventoryPlacementFee;
     
    private BigDecimal netWeight;
     
    private String weightUnit;
     
    private BigDecimal length;
     
    private BigDecimal width;
     
    private BigDecimal height;
     
    private String dimensionUnit;
     
    private BigDecimal boxCbm;
    private boolean isOversized;
   
    private Boolean isDangerousGoods;

     
    public String getSku() {
        return this.sku;
    }

    public void setSku(  String var1) {
        this.sku = var1;
    }

     
    public String getProductName() {
        return this.productName;
    }

    public void setProductName(  String var1) {
        
        this.productName = var1;
    }

     
    public String getBaseProductCode() {
        return this.baseProductCode;
    }

    public void setBaseProductCode(  String var1) {
       
        this.baseProductCode = var1;
    }

     
    public final String getExportName() {
        return this.exportName;
    }

    public final void setExportName(  String var1) {
        
        this.exportName = var1;
    }

     
    public String getProductNameLocal() {
        return this.productNameLocal;
    }

    public void setProductNameLocal(  String var1) {
        
        this.productNameLocal = var1;
    }

     
    public String getBrandEng() {
        return this.brandEng;
    }

    public void setBrandEng(  String var1) {
       
        this.brandEng = var1;
    }

     
    public String getCountryOfOrigin() {
        return this.countryOfOrigin;
    }

    public void setCountryOfOrigin(  String var1) {
      
        this.countryOfOrigin = var1;
    }

     
    public String getMarketHsCode() {
        return this.marketHsCode;
    }

    public void setMarketHsCode(  String var1) {
      
        this.marketHsCode = var1;
    }

     
    public String getHsCode() {
        return this.hsCode;
    }

    public void setHsCode(  String var1) {
       
        this.hsCode = var1;
    }

     
    public String getExportTariffReq() {
        return this.exportTariffReq;
    }

    public void setExportTariffReq(  String var1) {
        
        this.exportTariffReq = var1;
    }

     
    public String getImportTariffReq() {
        return this.importTariffReq;
    }

    public void setImportTariffReq(  String var1) {
        
        this.importTariffReq = var1;
    }

     
    public BigDecimal getFcaPrice() {
        return this.fcaPrice;
    }

    public void setFcaPrice(  BigDecimal var1) {
        
        this.fcaPrice = var1;
    }

   
    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(  Integer var1) {
        this.quantity = var1;
    }

     
    public BigDecimal getFcaTotal() {
        return this.fcaTotal;
    }

    public void setFcaTotal(  BigDecimal var1) {
        
        this.fcaTotal = var1;
    }

     
    public BigDecimal getInventoryPlacementFee() {
        return this.inventoryPlacementFee;
    }

    public void setInventoryPlacementFee(  BigDecimal var1) {
        
        this.inventoryPlacementFee = var1;
    }

     
    public BigDecimal getNetWeight() {
        return this.netWeight;
    }

    public void setNetWeight(  BigDecimal var1) {
        
        this.netWeight = var1;
    }

     
    public String getWeightUnit() {
        return this.weightUnit;
    }

    public void setWeightUnit(  String var1) {
        
        this.weightUnit = var1;
    }

     
    public BigDecimal getLength() {
        return this.length;
    }

    public void setLength(  BigDecimal var1) {
        
        this.length = var1;
    }

     
    public BigDecimal getWidth() {
        return this.width;
    }

    public void setWidth(  BigDecimal var1) {
        
        this.width = var1;
    }

     
    public BigDecimal getHeight() {
        return this.height;
    }

    public void setHeight(  BigDecimal var1) {
        
        this.height = var1;
    }

     
    public String getDimensionUnit() {
        return this.dimensionUnit;
    }

    public void setDimensionUnit(  String var1) {
        
        this.dimensionUnit = var1;
    }

     
    public BigDecimal getBoxCbm() {
        return this.boxCbm;
    }

    public void setBoxCbm(  BigDecimal var1) {
        
        this.boxCbm = var1;
    }

    public boolean isOversized() {
        return this.isOversized;
    }

    public void setOversized(boolean var1) {
        this.isOversized = var1;
    }

   
    public Boolean isDangerousGoods() {
        return this.isDangerousGoods;
    }

    public void setDangerousGoods(  Boolean var1) {
        this.isDangerousGoods = var1;
    }

    public IvsProductVerifyInfoImpl() {
        this.sku = "";
        this.productName = "";
        this.baseProductCode = "";
        this.exportName = "";
        this.productNameLocal = "";
        this.brandEng = "";
        this.countryOfOrigin = "";
        this.marketHsCode = "";
        this.hsCode = "";
        this.exportTariffReq = "";
        this.importTariffReq = "";
        this.fcaPrice = new BigDecimal(0);
        this.quantity = 0;
        this.fcaTotal = new BigDecimal(0);
        this.inventoryPlacementFee = new BigDecimal(0);
        this.netWeight = new BigDecimal(0);
        this.weightUnit = "";
        this.length = new BigDecimal(0);
        this.width = new BigDecimal(0);
        this.height = new BigDecimal(0);
        this.dimensionUnit = "";
        this.boxCbm = new BigDecimal(0);
    }

    public IvsProductVerifyInfoImpl(  String sku,   String productName,   String baseProductCode,   String exportName,   String productNameLocal,   String brandEng,   String countryOfOrigin,   String marketHsCode,   String hsCode,   String exportTariffReq,   String importTariffReq,   BigDecimal fcaPrice,   Integer quantity,   BigDecimal fcaTotal,   BigDecimal inventoryPlacementFee,   BigDecimal netWeight,   String weightUnit,   BigDecimal length,   BigDecimal width,   BigDecimal height,   String dimensionUnit,   BigDecimal boxCbm, boolean oversized,   Boolean dangerousGoods) {

        this.sku = "";
        this.productName = "";
        this.baseProductCode = "";
        this.exportName = "";
        this.productNameLocal = "";
        this.brandEng = "";
        this.countryOfOrigin = "";
        this.marketHsCode = "";
        this.hsCode = "";
        this.exportTariffReq = "";
        this.importTariffReq = "";
        this.fcaPrice = new BigDecimal(0);
        this.quantity = 0;
        this.fcaTotal = new BigDecimal(0);
        this.inventoryPlacementFee = new BigDecimal(0);
        this.netWeight = new BigDecimal(0);
        this.weightUnit = "";
        this.length = new BigDecimal(0);
        this.width = new BigDecimal(0);
        this.height = new BigDecimal(0);
        this.dimensionUnit = "";
        this.boxCbm = new BigDecimal(0);
        this.setSku(sku);
        this.setProductName(productName);
        this.setBaseProductCode(baseProductCode);
        this.exportName = exportName;
        this.setProductNameLocal(productNameLocal);
        this.setBrandEng(brandEng);
        this.setCountryOfOrigin(countryOfOrigin);
        this.setMarketHsCode(marketHsCode);
        this.setHsCode(hsCode);
        this.setExportTariffReq(exportTariffReq);
        this.setImportTariffReq(importTariffReq);
        this.setFcaPrice(fcaPrice);
        this.setQuantity(quantity);
        this.setFcaTotal(fcaTotal);
        this.setInventoryPlacementFee(inventoryPlacementFee);
        this.setNetWeight(netWeight);
        this.setWeightUnit(weightUnit);
        this.setLength(length);
        this.setWidth(width);
        this.setHeight(height);
        this.setDimensionUnit(dimensionUnit);
        this.setBoxCbm(boxCbm);
        this.setOversized(oversized);
        this.setDangerousGoods(dangerousGoods);
    }

    public IvsProductVerifyInfoImpl(  String sku,   String productName,   String baseProductCode,   String exportName,   String productNameLocal,   String brandEng,   String countryOfOrigin,   String marketHsCode,   String hsCode,   BigDecimal fcaPrice,   BigDecimal netWeight,   String weightUnit,   BigDecimal length,   BigDecimal width,   BigDecimal height,   String dimensionUnit) {

        this.sku = "";
        this.productName = "";
        this.baseProductCode = "";
        this.exportName = "";
        this.productNameLocal = "";
        this.brandEng = "";
        this.countryOfOrigin = "";
        this.marketHsCode = "";
        this.hsCode = "";
        this.exportTariffReq = "";
        this.importTariffReq = "";
        this.fcaPrice = new BigDecimal(0);
        this.quantity = 0;
        this.fcaTotal = new BigDecimal(0);
        this.inventoryPlacementFee = new BigDecimal(0);
        this.netWeight = new BigDecimal(0);
        this.weightUnit = "";
        this.length = new BigDecimal(0);
        this.width = new BigDecimal(0);
        this.height = new BigDecimal(0);
        this.dimensionUnit = "";
        this.boxCbm = new BigDecimal(0);
        this.setSku(sku);
        this.setProductName(productName);
        this.setBaseProductCode(baseProductCode);
        this.exportName = exportName;
        this.setProductNameLocal(productNameLocal);
        this.setBrandEng(brandEng);
        this.setCountryOfOrigin(countryOfOrigin);
        this.setMarketHsCode(marketHsCode);
        this.setHsCode(hsCode);
        this.setFcaPrice(fcaPrice);
        this.setNetWeight(netWeight);
        this.setWeightUnit(weightUnit);
        this.setLength(length);
        this.setWidth(width);
        this.setHeight(height);
        this.setDimensionUnit(dimensionUnit);
    }
}