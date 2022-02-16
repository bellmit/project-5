package com.kindminds.drs.api.v1.model.logistics;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;

import java.io.IOException;
import java.util.List;

public interface ShipmentIvs {

    String getName();
    ShipmentType getType();
    String getExpectedExportDate();
    String getFcaDeliveryLocationId();
    String getFcaDeliveryDate();
    String getInvoiceNumber();
    String getSellerCompanyKcode();
    String getBuyerCompanyKcode();
    Currency getCurrency();
    String getSalesTaxPercentage();
    ShipmentStatus getStatus();
    String getDestinationCountry();
    ShippingMethod getShippingMethod();
    String getDateCreated();
    String getSubtotal();
    String getSalesTax();
    String getTotal();
    String getPaidTotal();
    String getUnsName();
    String getDatePurchased();
    String getInternalNote();
    String getSpecialRequest();
    String getBoxesNeedRepackaging();
    String getRepackagingFee();
    Boolean getRequiredPO();
    String getPONumber();
    List<ShipmentIvsLineItem> getLineItems();

    public interface ShipmentIvsLineItem {

        int getId();
        int getLineSeq();
        Integer getBoxNum();
        void setBoxNum(Integer boxNum);
        Integer getMixedBoxLineSeq();
        void setMixedBoxLineSeq(Integer mixedBoxLineSeq);
        String getSkuCode();
        Boolean getRequireRepackaging();
        Integer getCartonNumberStart();
        void setCartonNumberStart(Integer start);
        Integer getCartonNumberEnd();
        void setCartonNumberEnd(Integer end);
        String getNameBySupplier();
        String getPerCartonGrossWeightKg();
        String getPerCartonUnits();
        String getCartonCounts();
        String getQuantity();
        String getUnitAmount();
        String getAmountUntaxed();
        String getCartonDimensionCm1();
        String getCartonDimensionCm2();
        String getCartonDimensionCm3();
        String getGUIInvoiceNumber();
        void setGUIInvoiceNumber(String guiInvoiceNumber);
        String getGUIFileName();

        String getGUIuuid();

        void setGUIuuid(String GUIuuid);

        byte[] getGUIInvoiceFileBytes() throws IOException;
        String getProductVerificationStatus();

    }


}