package com.kindminds.drs.api.v2.biz.domain.model.inventory;

public interface ShipmentStockAvailableInfo{
    String getUnsName();
    String getIvsName();
    Integer getQuantity();
    void setQuantity(Integer value);
    Integer getIvsSkuIdentificationId();
    String getIvsSkuIdentificationSerialNo();
    String getIvsSkuIdentificationRemark();;
    Integer getIvsSkuIdentificationDrsTranId();
    String getIvsSkuIdentificationStatus();
}