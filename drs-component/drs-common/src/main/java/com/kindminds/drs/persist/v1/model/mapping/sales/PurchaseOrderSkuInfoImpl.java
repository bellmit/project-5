package com.kindminds.drs.persist.v1.model.mapping.sales;






import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;

import java.math.BigDecimal;
import java.util.Objects;


public class PurchaseOrderSkuInfoImpl implements PurchaseOrderSkuInfo {

    ////@Column(name="id")
    private Integer shipmentId;

    //@Id
    //@Column(name="code_by_drs")
    private String skuCode;

    //@Column(name="name_by_supplier")
    private String skuName;

    //@Column(name="unit_amount")
    private BigDecimal unitPrice;

    //@Column(name="qty_order")
    private Integer numberUnits;

    //@Column(name="subtotal")
    private BigDecimal subTotal;

    public PurchaseOrderSkuInfoImpl() {
    }

    public PurchaseOrderSkuInfoImpl(Integer shipmentId, String skuCode,
                                    String skuName, Integer numberUnits,
                                    String unitPrice, String subTotal) {
        this.shipmentId = shipmentId;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.unitPrice = new BigDecimal(unitPrice);
        this.numberUnits = numberUnits;
        this.subTotal = new BigDecimal(subTotal);
    }

    public PurchaseOrderSkuInfoImpl(Integer shipmentId, String skuCode, String skuName, BigDecimal unitPrice, Integer numberUnits, BigDecimal subTotal) {
        this.shipmentId = shipmentId;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.unitPrice = unitPrice;
        this.numberUnits = numberUnits;
        this.subTotal = subTotal;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public Integer getNumberUnits() {
        return numberUnits;
    }

    public String getUnitPrice() {
        return unitPrice.stripTrailingZeros().toPlainString();
    }

    public String getSubTotal() {
        return subTotal.stripTrailingZeros().toPlainString();
    }

    @Override
    public String toString() {
        return "PurchaseOrderSkuInfoImpl{" +
                "shipmentId=" + shipmentId +
                ", skuCode='" + skuCode + '\'' +
                ", skuName='" + skuName + '\'' +
                ", numberUnits=" + numberUnits +
                ", unitPrice=" + getUnitPrice() +
                ", subTotal=" + getSubTotal() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderSkuInfoImpl that = (PurchaseOrderSkuInfoImpl) o;
        return Objects.equals(getShipmentId(), that.getShipmentId()) &&
                Objects.equals(getSkuCode(), that.getSkuCode()) &&
                Objects.equals(getSkuName(), that.getSkuName()) &&
                Objects.equals(getUnitPrice(), that.getUnitPrice()) &&
                Objects.equals(getNumberUnits(), that.getNumberUnits()) &&
                Objects.equals(getSubTotal(), that.getSubTotal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShipmentId(), getSkuCode(), getSkuName());
    }
}
