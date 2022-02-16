package com.kindminds.drs.api.v1.model.report;

public interface AmazonCampaignPerformanceReportBriefLineItem {
    String getCampaignName();
    String getAdGroupName();
    String getAdvertisedSku();
    String getKeyword();
    String getMatchType();
    String getStartDate();
    String getEndDate();
    String getClicks();
}