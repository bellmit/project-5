package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingData;
import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingSkuItem;

import java.util.List;

public interface ViewExternalMarketingActivityUco {
    List<ExternalMarketingSkuItem> getListOfSkus();
    ExternalMarketingData getMarketingData(
            String kCode, String skuCode, String marketplace);
}
