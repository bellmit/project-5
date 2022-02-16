package com.kindminds.drs.api.v1.model.sales;

public interface PurchaseOrderInfo {

    Integer getShipmentId();

    String getNameLocal();

    String getPhoneNumber();

    String getAddress();

    String getPoNumber();

    String getPrintingDate();

    String getFcaDeliveryDate();

    String getFcaShipmentLocation();

    String getSubTotal();

    String getAmountTax();

    String getAmountTotal();

    String getSalesTaxRate();

}
