package com.kindminds.drs.api.v2.biz.domain.model.request;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaveIvsRequest {

    private String name ;
    private ShipmentType type = null;
    private String expectedExportDate = null;
    private String fcaDeliveryLocationId = null;
    private String fcaDeliveryDate = null;
    private String specialRequest = null;
    private String boxesNeedRepackaging = null;

    public void setType(ShipmentType type) {
        this.type = type;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public void setBoxesNeedRepackaging(String boxesNeedRepackaging) {
        this.boxesNeedRepackaging = boxesNeedRepackaging;
    }

    public void setRepackagingFee(String repackagingFee) {
        this.repackagingFee = repackagingFee;
    }

    private String repackagingFee = null;
    private Boolean requiredPO = null;
    private String PONumber = null;
    private String invoiceNumber = "";
    private String sellerCompanyKcode = null;
    private String buyerCompanyKcode = null;
    private Currency currency = null;
    private String salesTaxPercentage = null;
    private ShipmentStatus status = ShipmentStatus.SHPT_DRAFT;
    private String destinationCountry = "";
    private ShippingMethod shippingMethod = null;
    private String dateCreated = null;


    private List<SaveIvsLineitemRequest> lineItem = new ArrayList<SaveIvsLineitemRequest>();

    private String subtotal = null;
    private String salesTax = null;
    private String total = null;
    private String paidTotal  = "";
    private String unsName = "";
    private String datePurchased = null;
    private String internalNote  = "";


    /*
    var lineItem: MutableList<SaveIvsLineitemRequest>
        get() = this.lineItems
        set(lineItem) {
            this.lineItems = lineItem
        }
*/

    public SaveIvsRequest(){}


    //Import Retail IVS
   public SaveIvsRequest(String marketRegion, ShippingMethod shippingMethod,String exportDate,
                         String fcaDeliveryLocationId,String fcaDeliveryDate) {

        this.setType(ShipmentType.INVENTORY);
        this.setDestinationCountry(marketRegion);
        this.setShippingMethod(shippingMethod);
        this.setExpectedExportDate(exportDate);
        this.setFcaDeliveryLocationId(fcaDeliveryLocationId);
        this.setFcaDeliveryDate(fcaDeliveryDate);
        this.setRequiredPO(false);
        this.setSellerCompanyKcode("K101");
        this.setBuyerCompanyKcode("K2");
        this.setCurrency(Currency.TWD);
        this.setSalesTaxPercentage(BigDecimal.valueOf(5).stripTrailingZeros().toPlainString());
        this.setLineItem(new ArrayList<SaveIvsLineitemRequest>());

    }

    //Import DCP IVS
    public SaveIvsRequest(String marketRegion,ShippingMethod shippingMethod,String exportDate,
                          String fcaDeliveryLocationId,String fcaDeliveryDate,String specialRequest,
                          Boolean  requirePO, String companyCode) {

        this.setType(ShipmentType.INVENTORY);
        this.setDestinationCountry(marketRegion);
        this.setShippingMethod(shippingMethod);
        this.setExpectedExportDate(exportDate);
        this.setFcaDeliveryLocationId(fcaDeliveryLocationId);
        this.setFcaDeliveryDate(fcaDeliveryDate);
        this.setSpecialRequest(specialRequest);
        this.setRequiredPO(requirePO);
        this.setSellerCompanyKcode(companyCode);
        this.setBuyerCompanyKcode("K2");
        this.setCurrency(Currency.TWD);
        this.setSalesTaxPercentage(BigDecimal.valueOf(5).stripTrailingZeros().toPlainString());
        this.setLineItem(new  ArrayList<SaveIvsLineitemRequest>());

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShipmentType getType() {
        return type;
    }

    public String getExpectedExportDate() {
        return expectedExportDate;
    }

    public void setExpectedExportDate(String expectedExportDate) {
        this.expectedExportDate = expectedExportDate;
    }

    public String getFcaDeliveryLocationId() {
        return fcaDeliveryLocationId;
    }

    public void setFcaDeliveryLocationId(String fcaDeliveryLocationId) {
        this.fcaDeliveryLocationId = fcaDeliveryLocationId;
    }

    public String getFcaDeliveryDate() {
        return fcaDeliveryDate;
    }

    public void setFcaDeliveryDate(String fcaDeliveryDate) {
        this.fcaDeliveryDate = fcaDeliveryDate;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getBoxesNeedRepackaging() {
        return boxesNeedRepackaging;
    }

    public String getRepackagingFee() {
        return repackagingFee;
    }

    public Boolean getRequiredPO() {
        return requiredPO;
    }

    public void setRequiredPO(Boolean requiredPO) {
        this.requiredPO = requiredPO;
    }

    public String getPONumber() {
        return PONumber;
    }

    public void setPONumber(String PONumber) {
        this.PONumber = PONumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSellerCompanyKcode() {
        return sellerCompanyKcode;
    }

    public void setSellerCompanyKcode(String sellerCompanyKcode) {
        this.sellerCompanyKcode = sellerCompanyKcode;
    }

    public String getBuyerCompanyKcode() {
        return buyerCompanyKcode;
    }

    public void setBuyerCompanyKcode(String buyerCompanyKcode) {
        this.buyerCompanyKcode = buyerCompanyKcode;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getSalesTaxPercentage() {
        return salesTaxPercentage;
    }

    public void setSalesTaxPercentage(String salesTaxPercentage) {
        this.salesTaxPercentage = salesTaxPercentage;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<SaveIvsLineitemRequest> getLineItem() {
        return lineItem;
    }

    public void setLineItem(List<SaveIvsLineitemRequest> lineItem) {
        this.lineItem = lineItem;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(String salesTax) {
        this.salesTax = salesTax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(String paidTotal) {
        this.paidTotal = paidTotal;
    }

    public String getUnsName() {
        return unsName;
    }

    public void setUnsName(String unsName) {
        this.unsName = unsName;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getInternalNote() {
        return internalNote;
    }

    public void setInternalNote(String internalNote) {
        this.internalNote = internalNote;
    }



    /*
    override fun toString(): String {
        return ("InventoryShipmentImpl [getName()=" + name + ", getType()=" + type
                + ", getExpectedExportDate()=" + expectedExportDate + ", getFcaDeliveryLocationId()="
                + fcaDeliveryLocationId + ", getFcaDeliveryDate()=" + fcaDeliveryDate
                + ", getSpecialRequest()=" + specialRequest + ", getBoxesNeedRepackaging()="
                + boxesNeedRepackaging + ", getRepackagingFee()=" + repackagingFee + ", getRequiredPO()="
                + requiredPO + ", getPONumber()=" + poNumber + ", getInvoiceNumber()=" + invoiceNumber
                + ", getSellerCompanyKcode()=" + sellerCompanyKcode + ", getBuyerCompanyKcode()="
                + buyerCompanyKcode + ", getCurrency()=" + currency + ", getSalesTaxPercentage()="
                + salesTaxPercentage + ", getStatus()=" + status + ", getDestinationCountry()="
                + destinationCountry + ", getShippingMethod()=" + shippingMethod + ", getDateCreated()="
                + dateCreated + ", getLineItems()=" + getLineItems()
                + ", getSubtotal()=" + subtotal + ", getSalesTax()=" + salesTax + ", getTotal()=" + total
                + ", getPaidTotal()=" + paidTotal + ", getUnsName()=" + unsName + ", getDatePurchased()="
                + datePurchased + ", getInternalNote()=" + internalNote + "]")
    }
    */



    /*
    fun getLineItems(): List<SaveIvsLineitemRequest> {
        if (lineItemList.isEmpty()) {
            for (item in this.lineItems) {
                if (item.getSkuCode() != null) {
                    lineItemList.add(item)
                }
            }
        }
        return this.lineItemList
    }*/



}

