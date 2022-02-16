package com.kindminds.drs.v1.model.impl.marketing;

import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingSkuItem;

public class ExternalMarketingSkuItemImpl implements ExternalMarketingSkuItem {

    private String marketplace;
    private String kCode;
    private String skuCode;

    public ExternalMarketingSkuItemImpl() {}

    public ExternalMarketingSkuItemImpl(String marketplace, String kCode, String skuCode) {
        this.marketplace = marketplace;
        this.kCode = kCode;
        this.skuCode = skuCode;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public String getKCode() {
        return kCode;
    }

    public void setKCode(String kCode) {
        this.kCode = kCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
}
