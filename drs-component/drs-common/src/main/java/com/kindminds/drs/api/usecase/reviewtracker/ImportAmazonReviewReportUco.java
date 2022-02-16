package com.kindminds.drs.api.usecase.reviewtracker;

import com.kindminds.drs.api.v1.model.amazon.AmazonReviewReportItem;

import java.util.List;

public interface ImportAmazonReviewReportUco {
    Integer importReviews(List<AmazonReviewReportItem> amazonReview);
}
