package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonNetworkComminglingTransactionEvent {
    private int id;
    private String transactionType;
    private Date postedDate;
    private String netCoTransactionID;
    private String swapReason;
    private String asin;
    private String marketplaceId;
    private String currency;
    private BigDecimal taxExclusiveAmount;
    private BigDecimal taxAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getNetCoTransactionID() {
        return netCoTransactionID;
    }

    public void setNetCoTransactionID(String netCoTransactionID) {
        this.netCoTransactionID = netCoTransactionID;
    }

    public String getSwapReason() {
        return swapReason;
    }

    public void setSwapReason(String swapReason) {
        this.swapReason = swapReason;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    public void setMarketplaceId(String marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTaxExclusiveAmount() {
        return taxExclusiveAmount;
    }

    public void setTaxExclusiveAmount(BigDecimal taxExclusiveAmount) {
        this.taxExclusiveAmount = taxExclusiveAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}
