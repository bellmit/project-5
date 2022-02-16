package com.kindminds.drs.api.usecase.logistics;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsSearchCondition;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsRequest;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;

import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;

public interface MaintainShipmentIvsUco {

    String createDraft(SaveIvsRequest request);
    String update(SaveIvsRequest request);

    String submit(String name);
    String accept(String name);
    String confirm(String shipment);
    String delete(String shipmentName);

    String methodForTestToGetPickupRequesterEmail(String shipmentName);

    String saveGuiInvoiceFile(String supplierKcode , String fileName, byte[] bytes);

    File getGuiInvoiceFile(String shipmentName, String fileName);
    Boolean removeGuiInvoiceFile(String supplierKcode , String fileName);

    String importRetailIVS(byte[] fileBytes);
    String importDCPIVS(byte[] fileBytes, String kCode);


    //============================================================
    //Query

    String getFcaDeliveryDate(String destinationCountry, String shippingMethod, int FCADeliveryLocationId ,String expectedExportDate);
    Currency getSupplierCurrency();

    ShipmentIvs get(String name);
    int getDaysToPrepare(String shippingMethod);
    Map<String,String> getSellerKcodeToNameMap();
    List<String> getDestinationCountries();

    List<String> getApprovedDestinationCountries(String kcode);

    List<String> getActiveAndOnboardingSkuCode(String kcode, String selectedCountry);

    List<ShippingMethod> getShippingMethods(String countryStr);
    List<ShippingMethod> getShippingMethods();
    List<ShipmentStatus> getShipmentStatusList();

    String getDefaultSalesTaxPercentage();
    ProductSkuStock getProductSkuStock(String shipmentName,String skuCodeByDrs);


    Boolean isGuiInvoiceRequired(String skuCode);
    PurchaseOrderInfo getPurchaseOrderInfo(String shipmentId);
    List<PurchaseOrderSkuInfo> getPurchaseOrderInfoList(String shipmentId);


    DtoList<ShipmentIvs> retrieveList(IvsSearchCondition condition, int pageIndex);
    Map<String,String> getActiveAndOnboardingSkuCodeToSupplierNameMap();
    Map<String,String> getActiveAndOnboardingSkuCodeToSupplierNameMap(String supplierKcode);
    Map<String,String> getAllCompanyKcodeToNameMap();
    Map<Integer,String> getFcaDeliveryLocationIdToLocationMap();


    ShipmentIvs.ShipmentIvsLineItem getShipmentLineItem(String ivsName , Integer boxNum ,Integer mixedBoxLineSeq);
}