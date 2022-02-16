package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.Date;

public interface ManageFbaInventoryReportItem {

    String getId();
    Date getSnapshotDate();
    String getCountryCode();
    String getSku();
    String getFnsku();
    String getAsin();
    String getProductName();
    String getCondition();
    BigDecimal getYourPrice();
    Boolean getMfnListingExists();
    Integer getMfnFulfillableQty();
    Boolean getAfnListingExists();
    Integer getAfnWarehouseQty();
    Integer getAfnFulfillableQty();
    Integer getAfnUnsellableQty();
    Integer getAfnReservedQty();
    Integer getAfnTotalQty();
    BigDecimal getPerUnitVolume();
    Integer getAfnInboundWorkingQty();
    Integer getAfnInboundShippedQty();
    Integer getAfnInboundReceivingQty();
    Integer getAfnResearchingQty();
    Integer getAfnReservedFutureSupply();
    Integer getAfnFutureSupplyBuyable();
    String getRegion();
    String getCodeByDrs();

}
