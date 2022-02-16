package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.util.Date;

public class AmazonImagingServicesFeeEvent {
    private int id;
    private String imagingRequestBillingItemID;
    private String asin;
    private Date postedDate;
    private int feeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagingRequestBillingItemID() {
        return imagingRequestBillingItemID;
    }

    public void setImagingRequestBillingItemID(String imagingRequestBillingItemID) {
        this.imagingRequestBillingItemID = imagingRequestBillingItemID;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public int getFeeId() {
        return feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }
}
