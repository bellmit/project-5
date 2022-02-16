package com.kindminds.drs.api.data.access.usecase.marketing;

import java.util.List;

public interface ViewExternalMarketingActivityDao {
    List<Object []> querySkuList();
    List<Object []> queryActivityData(String marketplace, String skuCode);
    List<Object []> queryAmazonAdData(String marketplace, String skuCode);
    List<Object []> querySalesData(String marketplace, String skuCode);

}
