package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;

public class AmazonPerformanceBondRefundEvent {
    private int id;
    private String marketplaceCountry;
    private String currency;
    private BigDecimal amount;
    private String productGroupList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarketplaceCountry() {
        return marketplaceCountry;
    }

    public void setMarketplaceCountry(String marketplaceCountry) {
        this.marketplaceCountry = marketplaceCountry;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(String productGroupList) {
        this.productGroupList = productGroupList;
    }
}
