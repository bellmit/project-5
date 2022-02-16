package com.kindminds.drs.api.data.row.logistics;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


public class IvsRow {

    private int id ;
    private String name;
      private String type;
      private String expectedExportDate;
      private String shipmentFcaLocationId ;
      private String fcaDeliveryDate ;
      private String invoiceNumber ;
      private String sellerCompanyKcode ;
      private String buyerCompanyKcode ;
      private String currencyName ;
      private BigDecimal salesTaxRate ;
      private BigDecimal subtotal ;
      private BigDecimal amountTax ;
      private BigDecimal amountTotal ;
      private String destinationCountryCode;
      private String shippingMethod;
      private OffsetDateTime dateCreated;
      private String datePurchased;
    private String internalNote;
      private String specialRequest;
      private String numOfRepackaging;
      private String repackagingFee;
      private Boolean requiredPo;
      private String poNumber;
      private String status;

      public IvsRow(){}

     public   IvsRow(int id, String name, String type, String expectedExportDate, String shipmentFcaLocationId, String fcaDeliveryDate, String invoiceNumber, String sellerCompanyKcode, String buyerCompanyKcode, String currencyName, BigDecimal salesTaxRate, BigDecimal subtotal, BigDecimal amountTax, BigDecimal amountTotal, String destinationCountryCode, String shippingMethod, OffsetDateTime dateCreated, String datePurchased, String internalNote, String specialRequest, String numOfRepackaging, String repackagingFee, Boolean requiredPo, String poNumber, String status) {
        this.setId(id);
        this.setName(name);
        this.setType(type);
        this.setExpectedExportDate(expectedExportDate);
        this.setShipmentFcaLocationId(shipmentFcaLocationId);
        this.setFcaDeliveryDate(fcaDeliveryDate);
        this.setInvoiceNumber(invoiceNumber);
        this.setSellerCompanyKcode(sellerCompanyKcode);
        this.setBuyerCompanyKcode(buyerCompanyKcode);
        this.setCurrencyName(currencyName);
        this.setSalesTaxRate(salesTaxRate);
        this.setSubtotal(subtotal);
        this.setAmountTax(amountTax);
        this.setAmountTotal(amountTotal);
        this.setDestinationCountryCode(destinationCountryCode);
        this.setShippingMethod(shippingMethod);
        this.setDateCreated(dateCreated);
        this.setDatePurchased(datePurchased);
        this.setInternalNote(internalNote);
        this.setSpecialRequest(specialRequest);
        this.setNumOfRepackaging(numOfRepackaging);
        this.setRepackagingFee(repackagingFee);
        this.setRequiredPo(requiredPo);
        this.setPoNumber(poNumber);
        this.setStatus(status);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpectedExportDate() {
        return expectedExportDate;
    }

    public void setExpectedExportDate(String expectedExportDate) {
        this.expectedExportDate = expectedExportDate;
    }

    public String getShipmentFcaLocationId() {
        return shipmentFcaLocationId;
    }

    public void setShipmentFcaLocationId(String shipmentFcaLocationId) {
        this.shipmentFcaLocationId = shipmentFcaLocationId;
    }

    public String getFcaDeliveryDate() {
        return fcaDeliveryDate;
    }

    public void setFcaDeliveryDate(String fcaDeliveryDate) {
        this.fcaDeliveryDate = fcaDeliveryDate;
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

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getSalesTaxRate() {
        return salesTaxRate;
    }

    public void setSalesTaxRate(BigDecimal salesTaxRate) {
        this.salesTaxRate = salesTaxRate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getAmountTax() {
        return amountTax;
    }

    public void setAmountTax(BigDecimal amountTax) {
        this.amountTax = amountTax;
    }

    public BigDecimal getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(BigDecimal amountTotal) {
        this.amountTotal = amountTotal;
    }

    public String getDestinationCountryCode() {
        return destinationCountryCode;
    }

    public void setDestinationCountryCode(String destinationCountryCode) {
        this.destinationCountryCode = destinationCountryCode;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getNumOfRepackaging() {
        return numOfRepackaging;
    }

    public void setNumOfRepackaging(String numOfRepackaging) {
        this.numOfRepackaging = numOfRepackaging;
    }

    public String getRepackagingFee() {
        return repackagingFee;
    }

    public void setRepackagingFee(String repackagingFee) {
        this.repackagingFee = repackagingFee;
    }

    public Boolean getRequiredPo() {
        return requiredPo;
    }

    public void setRequiredPo(Boolean requiredPo) {
        this.requiredPo = requiredPo;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


