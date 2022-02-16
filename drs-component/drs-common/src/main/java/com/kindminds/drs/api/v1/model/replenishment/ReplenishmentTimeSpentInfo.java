package com.kindminds.drs.api.v1.model.replenishment;

public interface ReplenishmentTimeSpentInfo {
    int getWarehouseId();
    String getWarehouseName();
    Integer getDaysSpentForAmazonReceiving();
    Integer getDaysSpentForSpwCalculation();
    Integer getDaysSpentForCourier();
    Integer getDaysSpentForAirFreight();
    Integer getDaysSpentForSurfaceFreight();
}