package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.report.ManageFbaInventoryReportItem;

import java.math.BigDecimal;
import java.util.Date;

public class ManageFbaInventoryReportItemImpl implements ManageFbaInventoryReportItem {

    private String id;
    private Date snapshotDate;
    private String countryCode;
    private String sku;
    private String fnsku;
    private String asin;
    private String productName;
    private String condition;
    private BigDecimal yourPrice;
    private Boolean mfnListingExists;
    private Integer mfnFulfillableQty;
    private Boolean afnListingExists;
    private Integer afnWarehouseQty;
    private Integer afnFulfillableQty;
    private Integer afnUnsellableQty;
    private Integer afnReservedQty;
    private Integer afnTotalQty;
    private BigDecimal perUnitVolume;
    private Integer afnInboundWorkingQty;
    private Integer afnInboundShippedQty;
    private Integer afnInboundReceivingQty;
    private Integer afnResearchingQty;
    private Integer afnReservedFutureSupply;
    private Integer afnFutureSupplyBuyable;
    private String region;
    private String codeByDrs;

    public ManageFbaInventoryReportItemImpl() {
    }

    public ManageFbaInventoryReportItemImpl(Date snapshotDate, String countryCode,
                                            String sku, String fnsku, String asin, String productName,
                                            String condition, BigDecimal yourPrice, Boolean mfnListingExists,
                                            Integer mfnFulfillableQty, Boolean afnListingExists,
                                            Integer afnWarehouseQty, Integer afnFulfillableQty,
                                            Integer afnUnsellableQty, Integer afnReservedQty,
                                            Integer afnTotalQty, BigDecimal perUnitVolume,
                                            Integer afnInboundWorkingQty, Integer afnInboundShippedQty,
                                            Integer afnInboundReceivingQty, Integer afnResearchingQty,
                                            Integer afnReservedFutureSupply, Integer afnFutureSupplyBuyable,
                                            String region, String codeByDrs) {
        this.snapshotDate = snapshotDate;
        this.countryCode = countryCode;
        this.sku = sku;
        this.fnsku = fnsku;
        this.asin = asin;
        this.productName = productName;
        this.condition = condition;
        this.yourPrice = yourPrice;
        this.mfnListingExists = mfnListingExists;
        this.mfnFulfillableQty = mfnFulfillableQty;
        this.afnListingExists = afnListingExists;
        this.afnWarehouseQty = afnWarehouseQty;
        this.afnFulfillableQty = afnFulfillableQty;
        this.afnUnsellableQty = afnUnsellableQty;
        this.afnReservedQty = afnReservedQty;
        this.afnTotalQty = afnTotalQty;
        this.perUnitVolume = perUnitVolume;
        this.afnInboundWorkingQty = afnInboundWorkingQty;
        this.afnInboundShippedQty = afnInboundShippedQty;
        this.afnInboundReceivingQty = afnInboundReceivingQty;
        this.afnResearchingQty = afnResearchingQty;
        this.afnReservedFutureSupply = afnReservedFutureSupply;
        this.afnFutureSupplyBuyable = afnFutureSupplyBuyable;
        this.region = region;
        this.codeByDrs = codeByDrs;
    }

    public String getId() {
        return id;
    }

    public Date getSnapshotDate() {
        return snapshotDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getSku() {
        return sku;
    }

    public String getFnsku() {
        return fnsku;
    }

    public String getAsin() {
        return asin;
    }

    public String getProductName() {
        return productName;
    }

    public String getCondition() {
        return condition;
    }

    public BigDecimal getYourPrice() {
        return yourPrice;
    }

    public Boolean getMfnListingExists() {
        return mfnListingExists;
    }

    public Integer getMfnFulfillableQty() {
        return mfnFulfillableQty;
    }

    public Boolean getAfnListingExists() {
        return afnListingExists;
    }

    public Integer getAfnWarehouseQty() {
        return afnWarehouseQty;
    }

    public Integer getAfnFulfillableQty() {
        return afnFulfillableQty;
    }

    public Integer getAfnUnsellableQty() {
        return afnUnsellableQty;
    }

    public Integer getAfnReservedQty() {
        return afnReservedQty;
    }

    public Integer getAfnTotalQty() {
        return afnTotalQty;
    }

    public BigDecimal getPerUnitVolume() {
        return perUnitVolume;
    }

    public Integer getAfnInboundWorkingQty() {
        return afnInboundWorkingQty;
    }

    public Integer getAfnInboundShippedQty() {
        return afnInboundShippedQty;
    }

    public Integer getAfnInboundReceivingQty() {
        return afnInboundReceivingQty;
    }

    public Integer getAfnResearchingQty() {
        return afnResearchingQty;
    }

    public Integer getAfnReservedFutureSupply() {
        return afnReservedFutureSupply;
    }

    public Integer getAfnFutureSupplyBuyable() {
        return afnFutureSupplyBuyable;
    }

    public String getRegion() {
        return region;
    }

    public String getCodeByDrs() {
        return codeByDrs;
    }
}
