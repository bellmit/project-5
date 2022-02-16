package com.kindminds.drs.persist.v1.model.mapping.report;

import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductTotalOrder;

public class KeyProductTotalOrderImpl implements KeyProductTotalOrder {

    private String marketplace;
    private String bpCode;
    private String sku;
    private int totalOrder;

    public KeyProductTotalOrderImpl(String marketplace, String bpCode, String sku, int totalOrder) {
        this.marketplace = marketplace;
        this.bpCode = bpCode;
        this.sku = sku;
        this.totalOrder = totalOrder;
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
    public int getTotalOrder() {
        return totalOrder;
    }

    @Override
    public String toString() {
        return "KeyProductTotalOrderImpl{" +
                "marketplace='" + marketplace + '\'' +
                ", bpCode='" + bpCode + '\'' +
                ", sku='" + sku + '\'' +
                ", totalOrder=" + totalOrder +
                '}';
    }
}
