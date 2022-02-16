package com.kindminds.drs.api.v1.model.sales;

public interface PurchaseOrderSkuInfo {

    Integer getShipmentId();

    String getSkuCode();

    String getSkuName();

    Integer getNumberUnits();

    String getUnitPrice();

    String getSubTotal();
}
