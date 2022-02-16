package com.kindminds.drs.api.data.row.logistics;

import java.math.BigDecimal;

public  class IvsShippingCostsRow{

    private BigDecimal freightFeeOriginal;
    private BigDecimal freightFeeNonMerged;
    private int freightFeeRateIdOriginal;
    private int freightFeeRateIdNonMerged;
    private BigDecimal truckCost;
    private BigDecimal inventoryPlacementFee;
    private BigDecimal hsCodeFee;
    private BigDecimal otherExpense;
    private BigDecimal shippingCostOriginal;
    private BigDecimal shippingCostNonMerged;

    public IvsShippingCostsRow(){}


    public IvsShippingCostsRow(BigDecimal freightFeeOriginal, BigDecimal freightFeeNonMerged,
                               int freightFeeRateIdOriginal, int freightFeeRateIdNonMerged,
                               BigDecimal truckCost, BigDecimal inventoryPlacementFee,
                               BigDecimal hsCodeFee, BigDecimal otherExpense,
                               BigDecimal shippingCostOriginal, BigDecimal shippingCostNonMerged) {
        this.setFreightFeeOriginal(freightFeeOriginal);
        this.setFreightFeeNonMerged(freightFeeNonMerged);
        this.setFreightFeeRateIdOriginal(freightFeeRateIdOriginal);
        this.setFreightFeeRateIdNonMerged(freightFeeRateIdNonMerged);
        this.setTruckCost(truckCost);
        this.setInventoryPlacementFee(inventoryPlacementFee);
        this.setHsCodeFee(hsCodeFee);
        this.setOtherExpense(otherExpense);
        this.setShippingCostOriginal(shippingCostOriginal);
        this.setShippingCostNonMerged(shippingCostNonMerged);
    }

    public BigDecimal getFreightFeeOriginal() {
        return freightFeeOriginal;
    }

    public void setFreightFeeOriginal(BigDecimal freightFeeOriginal) {
        this.freightFeeOriginal = freightFeeOriginal;
    }

    public BigDecimal getFreightFeeNonMerged() {
        return freightFeeNonMerged;
    }

    public void setFreightFeeNonMerged(BigDecimal freightFeeNonMerged) {
        this.freightFeeNonMerged = freightFeeNonMerged;
    }

    public int getFreightFeeRateIdOriginal() {
        return freightFeeRateIdOriginal;
    }

    public void setFreightFeeRateIdOriginal(int freightFeeRateIdOriginal) {
        this.freightFeeRateIdOriginal = freightFeeRateIdOriginal;
    }

    public int getFreightFeeRateIdNonMerged() {
        return freightFeeRateIdNonMerged;
    }

    public void setFreightFeeRateIdNonMerged(int freightFeeRateIdNonMerged) {
        this.freightFeeRateIdNonMerged = freightFeeRateIdNonMerged;
    }

    public BigDecimal getTruckCost() {
        return truckCost;
    }

    public void setTruckCost(BigDecimal truckCost) {
        this.truckCost = truckCost;
    }

    public BigDecimal getInventoryPlacementFee() {
        return inventoryPlacementFee;
    }

    public void setInventoryPlacementFee(BigDecimal inventoryPlacementFee) {
        this.inventoryPlacementFee = inventoryPlacementFee;
    }

    public BigDecimal getHsCodeFee() {
        return hsCodeFee;
    }

    public void setHsCodeFee(BigDecimal hsCodeFee) {
        this.hsCodeFee = hsCodeFee;
    }

    public BigDecimal getOtherExpense() {
        return otherExpense;
    }

    public void setOtherExpense(BigDecimal otherExpense) {
        this.otherExpense = otherExpense;
    }

    public BigDecimal getShippingCostOriginal() {
        return shippingCostOriginal;
    }

    public void setShippingCostOriginal(BigDecimal shippingCostOriginal) {
        this.shippingCostOriginal = shippingCostOriginal;
    }

    public BigDecimal getShippingCostNonMerged() {
        return shippingCostNonMerged;
    }

    public void setShippingCostNonMerged(BigDecimal shippingCostNonMerged) {
        this.shippingCostNonMerged = shippingCostNonMerged;
    }
}
