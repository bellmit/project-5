package com.kindminds.drs.api.v1.model.amazon;

public interface AmazonReviewNotification {
    int getProductId();
    String getSupplierKcode();
    String getSupplierName();
    String getProductName();
    String getAsin();
}
