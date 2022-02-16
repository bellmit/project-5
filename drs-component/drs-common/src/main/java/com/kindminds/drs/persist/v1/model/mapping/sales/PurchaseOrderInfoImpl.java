package com.kindminds.drs.persist.v1.model.mapping.sales;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.util.DateHelper;




import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class PurchaseOrderInfoImpl implements PurchaseOrderInfo {

    //@Id
    ////@Column(name="id")
    private Integer shipmentId;

    //@Column(name="name_local")
    private String nameLocal;

    //@Column(name="phone_number")
    private String phoneNumber;

    //@Column(name="address")
    private String address;

    //@Column(name="po_number")
    private String poNumber;

    //@Column(name="fca_delivery_date")
    private Date fcaDeliveryDate;

    //@Column(name="fca_shipment_location")
    private String fcaShipmentLocation;

    //@Column(name="subtotal")
    private BigDecimal subTotal;

    //@Column(name="amount_tax")
    private BigDecimal amountTax;

    //@Column(name="amount_total")
    private BigDecimal amountTotal;

    //@Column(name="currency")
    private String currency;

    //@Column(name="sales_tax_rate")
    private BigDecimal salesTaxRate;

    public PurchaseOrderInfoImpl() {

    }

    public PurchaseOrderInfoImpl(Integer shipmentId, String nameLocal,
                                 String phoneNumber, String address,
                                 String poNumber, String fcaDeliveryDate,
                                 String fcaShipmentLocation, String subTotal,
                                 String amountTax, String amountTotal,
                                 String currency, String salesTaxRate) throws ParseException {
        this.shipmentId = shipmentId;
        this.nameLocal = nameLocal;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.poNumber = poNumber;
        this.fcaDeliveryDate = new SimpleDateFormat("yyyy-MM-dd")
                .parse(fcaDeliveryDate);
        this.fcaShipmentLocation = fcaShipmentLocation;
        this.subTotal = new BigDecimal(subTotal);
        this.amountTax = new BigDecimal(amountTax);
        this.amountTotal = new BigDecimal(amountTotal);
        this.currency = currency;
        this.salesTaxRate = new BigDecimal(salesTaxRate);
    }

    public PurchaseOrderInfoImpl(Integer shipmentId, String nameLocal, String phoneNumber, String address, String poNumber, Date fcaDeliveryDate, String fcaShipmentLocation, BigDecimal subTotal, BigDecimal amountTax, BigDecimal amountTotal, String currency, BigDecimal salesTaxRate) {
        this.shipmentId = shipmentId;
        this.nameLocal = nameLocal;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.poNumber = poNumber;
        this.fcaDeliveryDate = fcaDeliveryDate;
        this.fcaShipmentLocation = fcaShipmentLocation;
        this.subTotal = subTotal;
        this.amountTax = amountTax;
        this.amountTotal = amountTotal;
        this.currency = currency;
        this.salesTaxRate = salesTaxRate;
    }


    private Currency getCurrency() {
        return Currency.valueOf(currency);
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public String getNameLocal() {
        return nameLocal;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public String getPrintingDate() {
        return DateHelper.toString(new Date(), "yyyy-MM-dd");
    }

    public String getFcaDeliveryDate() {
        return DateHelper.toString(fcaDeliveryDate, "yyyy-MM-dd");
    }

    public String getFcaShipmentLocation() {
        return fcaShipmentLocation;
    }

    public String getSubTotal() {
        return subTotal.stripTrailingZeros().toPlainString();
    }

    public String getAmountTax() {
        return amountTax.setScale(getCurrency().getScale(), RoundingMode.HALF_UP).toString();
    }

    public String getAmountTotal() {
        return amountTotal.setScale(getCurrency().getScale(), RoundingMode.HALF_UP).toString();
    }

    public String getSalesTaxRate() {
        return this.salesTaxRate.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
    }

    @Override
    public String toString() {
        return "PurchaseOrderInfoImpl{" +
                "shipmentId=" + shipmentId +
                ", nameLocal='" + nameLocal + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", poNumber='" + poNumber + '\'' +
                ", fcaDeliveryDate=" + getFcaDeliveryDate() +
                ", fcaShipmentLocation='" + fcaShipmentLocation + '\'' +
                ", subTotal=" + getSubTotal() +
                ", amountTax=" + getAmountTax() +
                ", amountTotal=" + getAmountTotal() +
                ", printingDate=" + getPrintingDate() +
                ", currency=" + getCurrency() +
                ", salesTaxRate=" + getSalesTaxRate() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderInfoImpl that = (PurchaseOrderInfoImpl) o;
        return Objects.equals(getShipmentId(), that.getShipmentId()) &&
                Objects.equals(getNameLocal(), that.getNameLocal()) &&
                Objects.equals(getPhoneNumber(), that.getPhoneNumber()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getPoNumber(), that.getPoNumber()) &&
                Objects.equals(getFcaDeliveryDate(), that.getFcaDeliveryDate()) &&
                Objects.equals(getFcaShipmentLocation(), that.getFcaShipmentLocation()) &&
                Objects.equals(getSubTotal(), that.getSubTotal()) &&
                Objects.equals(getAmountTax(), that.getAmountTax()) &&
                Objects.equals(getAmountTotal(), that.getAmountTotal()) &&
                Objects.equals(getSalesTaxRate(), that.getSalesTaxRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShipmentId(), getNameLocal());
    }
}
