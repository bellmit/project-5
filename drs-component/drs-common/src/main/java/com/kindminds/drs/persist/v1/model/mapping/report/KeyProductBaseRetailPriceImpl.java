package com.kindminds.drs.persist.v1.model.mapping.report;

import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductBaseRetailPrice;

public class KeyProductBaseRetailPriceImpl implements KeyProductBaseRetailPrice {

    private String marketplace;
    private String bpCode;
    private String sku;
    private int BaseRetailPrice;

    public KeyProductBaseRetailPriceImpl(String marketplace, String bpCode, String sku, int baseRetailPrice) {
        this.marketplace = marketplace;
        this.bpCode = bpCode;
        this.sku = sku;
        BaseRetailPrice = baseRetailPrice;
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
    public int getBaseRetailPrice() {
        return BaseRetailPrice;
    }

    @Override
    public String toString() {
        return "KeyProductBaseRetailPriceImpl{" +
                "marketplace='" + marketplace + '\'' +
                ", bpCode='" + bpCode + '\'' +
                ", sku='" + sku + '\'' +
                ", BaseRetailPrice=" + BaseRetailPrice +
                '}';
    }
}
