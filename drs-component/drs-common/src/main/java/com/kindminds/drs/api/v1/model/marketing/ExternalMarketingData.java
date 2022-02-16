package com.kindminds.drs.api.v1.model.marketing;

import java.util.List;

public interface ExternalMarketingData {
    String getKCode();
    String getSkuCode();
    String getMarketplace();
    List<ExternalMarketingSalesItem> getSalesData();
    List<ExternalMarketingActivityItem> getActivityData();
}
