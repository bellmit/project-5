package com.kindminds.drs.v1.model.impl.amazon;

import com.kindminds.drs.api.v1.model.amazon.AmazonReviewReportItem;

import java.util.Objects;

public class AmazonReviewReportItemImpl implements AmazonReviewReportItem {
    private String id;
    private String title;
    private String dateCreated;
    private double starRating;
    private int productId;
    private int marketplaceId;
    private String comment;
    private String customerName;
    private Integer helpful;

    public AmazonReviewReportItemImpl(){}

    public AmazonReviewReportItemImpl(String id, String title, String dateCreated, double starRating, int productId,
                                      int marketplaceId, String comment, String customerName, Integer helpful) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.starRating = starRating;
        this.productId = productId;
        this.marketplaceId = marketplaceId;
        this.comment = comment;
        this.customerName = customerName;
        this.helpful = helpful;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getProductId() {
        return productId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDateCreated() {
        return dateCreated;
    }

    @Override
    public double getStarRating() {
        return starRating;
    }

    @Override
    public int getMarketplaceId() {
        return marketplaceId;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getCustomerName() {
        return customerName;
    }

    @Override
    public Integer getHelpful() {
        return helpful;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setStarRating(double starRating) {
        this.starRating = starRating;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setMarketplaceId(int marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setHelpful(Integer helpful) {
        this.helpful = helpful;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmazonReviewReportItemImpl that = (AmazonReviewReportItemImpl) o;
        return marketplaceId == that.marketplaceId &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marketplaceId);
    }
}