package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonSolutionProviderCreditEvent {
    private int id;
    private String providerTransactionType;
    private String sellerOrderId;
    private String marketplaceId;
    private String marketplaceCountry;
    private String sellerId;
    private String sellerStoreName;
    private String providerId;
    private String providerStoreName;
    private String transactionCurrency;
    private BigDecimal transactionAmount;
    private Date transactionCreationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProviderTransactionType() {
        return providerTransactionType;
    }

    public void setProviderTransactionType(String providerTransactionType) {
        this.providerTransactionType = providerTransactionType;
    }

    public String getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(String sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    public void setMarketplaceId(String marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    public String getMarketplaceCountry() {
        return marketplaceCountry;
    }

    public void setMarketplaceCountry(String marketplaceCountry) {
        this.marketplaceCountry = marketplaceCountry;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerStoreName() {
        return sellerStoreName;
    }

    public void setSellerStoreName(String sellerStoreName) {
        this.sellerStoreName = sellerStoreName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderStoreName() {
        return providerStoreName;
    }

    public void setProviderStoreName(String providerStoreName) {
        this.providerStoreName = providerStoreName;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getTransactionCreationDate() {
        return transactionCreationDate;
    }

    public void setTransactionCreationDate(Date transactionCreationDate) {
        this.transactionCreationDate = transactionCreationDate;
    }
}
