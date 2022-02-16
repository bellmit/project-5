package com.kindminds.drs.api.v2.biz.domain.model.finance;

public class AmazonServiceFeeEvent {
    private int id;
    private String amazonOrderId;
    private String feeReason;
    private int feeId;
    private String sellerSKU;
    private String fnSKU;
    private String feeDescription;
    private String asin;

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

    public String getFeeReason() {
        return feeReason;
    }

    public void setFeeReason(String feeReason) {
        this.feeReason = feeReason;
    }

    public int getFeeId() {
        return feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public String getSellerSKU() {
        return sellerSKU;
    }

    public void setSellerSKU(String sellerSKU) {
        this.sellerSKU = sellerSKU;
    }

    public String getFnSKU() {
        return fnSKU;
    }

    public void setFnSKU(String fnSKU) {
        this.fnSKU = fnSKU;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }
}
