package com.kindminds.drs.v1.model.impl.marketing;

import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingActivityItem;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingData;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingSalesItem;

import java.util.List;

public class ExternalMarketingDataImpl implements ExternalMarketingData {
    private String kCode;
    private String skuCode;
    private String marketplace;
    private List<ExternalMarketingSalesItem> salesData;
    private List<ExternalMarketingActivityItem> activityData;

    public ExternalMarketingDataImpl() {}

    public ExternalMarketingDataImpl(String kCode, String skuCode, String marketplace,
                                     List<ExternalMarketingSalesItem> salesData,
                                     List<ExternalMarketingActivityItem> activityData) {
        this.kCode = kCode;
        this.skuCode = skuCode;
        this.marketplace = marketplace;
        this.salesData = salesData;
        this.activityData = activityData;
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

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public List<ExternalMarketingSalesItem> getSalesData() {
        return salesData;
    }

    public void setSalesData(List<ExternalMarketingSalesItem> salesData) {
        this.salesData = salesData;
    }

    public List<ExternalMarketingActivityItem> getActivityData() {
        return activityData;
    }

    public void setActivityData(List<ExternalMarketingActivityItem> activityData) {
        this.activityData = activityData;
    }
}
