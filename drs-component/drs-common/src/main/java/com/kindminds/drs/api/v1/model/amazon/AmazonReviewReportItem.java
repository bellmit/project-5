package com.kindminds.drs.api.v1.model.amazon;


public interface AmazonReviewReportItem {
    String getId();
    int getProductId();
    String getTitle();
    String getDateCreated();
    double getStarRating();
    int getMarketplaceId();
    String getComment();
    String getCustomerName();
    Integer getHelpful();
    void setDateCreated(String dateCreated);
}