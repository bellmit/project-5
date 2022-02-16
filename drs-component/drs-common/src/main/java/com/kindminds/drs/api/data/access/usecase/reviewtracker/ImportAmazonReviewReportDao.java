package com.kindminds.drs.api.data.access.usecase.reviewtracker;


import com.kindminds.drs.api.v1.model.amazon.AmazonReviewNotification;
import com.kindminds.drs.api.v1.model.amazon.AmazonReviewReportItem;

import java.util.List;

public interface ImportAmazonReviewReportDao {
    /**
     *
     * @param amazonReviews
     * @return New Amazon Reviews
     */
    List<AmazonReviewReportItem> insertAmazonReviews(List<AmazonReviewReportItem> amazonReviews);
    Boolean deleteAmazonReview(String id, int marketplaceId);
    AmazonReviewNotification queryAmazonReviewNotification(int productId);
}