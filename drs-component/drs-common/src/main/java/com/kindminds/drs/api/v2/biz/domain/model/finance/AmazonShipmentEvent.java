package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.util.Date;

public class AmazonShipmentEvent {
    private int id;
    private String amazonOrderId;
    private String sellerOrderId;
    private String marketplaceName;
    private String eventType;
    private int orderChargeId;
    private int orderChargeAdjustmentId;
    private int shipmentFeeId;
    private int shipmentFeeAdjustmentId;
    private int orderFeeId;
    private int orderFeeAdjustmentId;
    private int directPaymentId;
    private Date postedDate;
    private int shipmentItemId;
    private int shipmentItemAdjustmentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public String getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(String sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public String getMarketplaceName() {
        return marketplaceName;
    }

    public void setMarketplaceName(String marketplaceName) {
        this.marketplaceName = marketplaceName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getOrderChargeId() {
        return orderChargeId;
    }

    public void setOrderChargeId(int orderChargeId) {
        this.orderChargeId = orderChargeId;
    }

    public int getOrderChargeAdjustmentId() {
        return orderChargeAdjustmentId;
    }

    public void setOrderChargeAdjustmentId(int orderChargeAdjustmentId) {
        this.orderChargeAdjustmentId = orderChargeAdjustmentId;
    }

    public int getShipmentFeeId() {
        return shipmentFeeId;
    }

    public void setShipmentFeeId(int shipmentFeeId) {
        this.shipmentFeeId = shipmentFeeId;
    }

    public int getShipmentFeeAdjustmentId() {
        return shipmentFeeAdjustmentId;
    }

    public void setShipmentFeeAdjustmentId(int shipmentFeeAdjustmentId) {
        this.shipmentFeeAdjustmentId = shipmentFeeAdjustmentId;
    }

    public int getOrderFeeId() {
        return orderFeeId;
    }

    public void setOrderFeeId(int orderFeeId) {
        this.orderFeeId = orderFeeId;
    }

    public int getOrderFeeAdjustmentId() {
        return orderFeeAdjustmentId;
    }

    public void setOrderFeeAdjustmentId(int orderFeeAdjustmentId) {
        this.orderFeeAdjustmentId = orderFeeAdjustmentId;
    }

    public int getDirectPaymentId() {
        return directPaymentId;
    }

    public void setDirectPaymentId(int directPaymentId) {
        this.directPaymentId = directPaymentId;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public int getShipmentItemId() {
        return shipmentItemId;
    }

    public void setShipmentItemId(int shipmentItemId) {
        this.shipmentItemId = shipmentItemId;
    }

    public int getShipmentItemAdjustmentId() {
        return shipmentItemAdjustmentId;
    }

    public void setShipmentItemAdjustmentId(int shipmentItemAdjustmentId) {
        this.shipmentItemAdjustmentId = shipmentItemAdjustmentId;
    }
}
