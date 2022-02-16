package com.kindminds.drs.persist.v1.model.mapping.report;

import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductInventorySupplyStats;

public class KeyProductInventorySupplyStatsImpl implements KeyProductInventorySupplyStats {

    private String marketplace;
    private String bpCode;
    private String sku;
    private int inBound;
    private int inStock;
    private int transfer;

    public KeyProductInventorySupplyStatsImpl(String marketplace, String bpCode, String sku, int inBound, int inStock, int transfer) {
        this.marketplace = marketplace;
        this.bpCode = bpCode;
        this.sku = sku;
        this.inBound = inBound;
        this.inStock = inStock;
        this.transfer = transfer;
    }

    @Override
    public String getMarketplace() {
        return marketplace;
    }

    @Override
    public String getBpCode() {
        return bpCode;
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public int getInBound() {
        return inBound;
    }

    @Override
    public int getInStock() {
        return inStock;
    }

    @Override
    public int getTransfer() {
        return transfer;
    }

    @Override
    public String toString() {
        return "KeyProductInventorySupplyStatsImpl{" +
                "sku='" + sku + '\'' +
                ", inBound=" + inBound +
                ", inStock=" + inStock +
                ", transfer=" + transfer +
                '}';
    }
}
