package com.kindminds.drs.api.v2.biz.domain.model.finance;


import java.util.Date;


public class AmazonPayWithAmazonEvent {
    private int id;
    private String sellerOrderId;
    private Date transactionPostedDate;
    private String businessObjectType;
    private String salesChannel;
    private int chargeComponentId;
    private int feeId;
    private String paymentAmountType;
    private String amountDescription;
    private String fulfillmentChannel;
    private String storeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(String sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public Date getTransactionPostedDate() {
        return transactionPostedDate;
    }

    public void setTransactionPostedDate(Date transactionPostedDate) {
        this.transactionPostedDate = transactionPostedDate;
    }

    public String getBusinessObjectType() {
        return businessObjectType;
    }

    public void setBusinessObjectType(String businessObjectType) {
        this.businessObjectType = businessObjectType;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public int getChargeComponentId() {
        return chargeComponentId;
    }

    public void setChargeComponentId(int chargeComponentId) {
        this.chargeComponentId = chargeComponentId;
    }

    public int getFeeId() {
        return feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public String getPaymentAmountType() {
        return paymentAmountType;
    }

    public void setPaymentAmountType(String paymentAmountType) {
        this.paymentAmountType = paymentAmountType;
    }

    public String getAmountDescription() {
        return amountDescription;
    }

    public void setAmountDescription(String amountDescription) {
        this.amountDescription = amountDescription;
    }

    public String getFulfillmentChannel() {
        return fulfillmentChannel;
    }

    public void setFulfillmentChannel(String fulfillmentChannel) {
        this.fulfillmentChannel = fulfillmentChannel;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
