package com.kindminds.drs.api.v2.biz.domain.model.logistics;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;



public interface Ivs {

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


    OffsetDateTime getDateCreated();

    
    BigDecimal getPaidTotal();

    
    String getUnsName();

    
    String getDatePurchased();


    String getInternalNote();

    
    String getSpecialRequest();

    
    String getBoxesNeedRepackaging();

    
    String getRepackagingFee();

    
    Boolean getRequiredPO();

    
    String getPoNumber();


    BigDecimal getSalesTaxRate();


    BigDecimal getSubtotal();


    BigDecimal getSalesTax();


    BigDecimal getTotal();

    
    Currency getHandlerCurrency();

    
    Integer getSerialId();

    
    List<IvsLineItem> getLineItems();


    BigDecimal getFreightFeeOriginal();


    BigDecimal getFreightFeeNonMerged();

    int getFreightFeeRateIdOriginal();

    int getFreightFeeRateIdNonMerged();


    BigDecimal getTruckCost();


    BigDecimal getInventoryPlacementFee();


    BigDecimal getHsCodeFee();


    BigDecimal getOtherExpense();


    BigDecimal getShippingCostOriginal();


    BigDecimal getShippingCostNonMerged();

    void processDraft( SaveIvsRequest var1);

    void processDraftForKcode( SaveIvsRequest var1,  String var2,  String var3);

    void update( SaveIvsRequest var1);

    void submit( String var1);


    String accept( String var1);


    String confirm( String var1);

    boolean isAllConfirmed();

    boolean isAllInitiallyVerified();

    void changeStatus( ShipmentStatus var1);


    String getShippingCosts( String var1);
}



  /*
interface IvsProductDocumentReq {

    val g3Invoice: Boolean?
    val msds: Boolean?
    val dangerousGoods: Boolean?
    val un383: Boolean?
    val ppq505: Boolean?
    val usFDA: Boolean?
    val usMID: Boolean?
    val fatContentSpecification: Boolean?
    val exportTariffRequirement: Boolean?
    val additionalRemarks: Boolean?
}
*/
