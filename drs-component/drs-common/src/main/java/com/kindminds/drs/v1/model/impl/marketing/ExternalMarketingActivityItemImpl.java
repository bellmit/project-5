package com.kindminds.drs.v1.model.impl.marketing;

import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingActivityItem;

public class ExternalMarketingActivityItemImpl implements ExternalMarketingActivityItem {
    private String activityType;
    private String date;
    private String activityName;

    private static final String ADS = "ads";

    public ExternalMarketingActivityItemImpl() {}

    public ExternalMarketingActivityItemImpl(String activityType, String date, String activityName) {
        this.activityType = activityType;
        this.date = date;
        this.activityName = activityName;
    }

    public ExternalMarketingActivityItemImpl(String activityName, String date) {
        this.activityType = ADS;
        this.date = date;
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
