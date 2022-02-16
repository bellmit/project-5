package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.util.Date;

public class AmazonRemovalShipmentEvent {
    private int id;
    private Date postedDate;
    private String orderId;
    private String transactionType;
    private int removalShipmentItemId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getRemovalShipmentItemId() {
        return removalShipmentItemId;
    }

    public void setRemovalShipmentItemId(int removalShipmentItemId) {
        this.removalShipmentItemId = removalShipmentItemId;
    }
}
