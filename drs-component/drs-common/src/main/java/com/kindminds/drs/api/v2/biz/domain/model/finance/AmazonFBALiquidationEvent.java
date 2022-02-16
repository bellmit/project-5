package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonFBALiquidationEvent {
    private int id;
    private Date postedDate;
    private String originalRemovalOrderId;
    private String liquidationProceedsCurrency;
    private BigDecimal liquidationProceedsAmount;
    private String liquidationFeeCurrency;
    private BigDecimal liquidationFeeAmount;

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

    public String getOriginalRemovalOrderId() {
        return originalRemovalOrderId;
    }

    public void setOriginalRemovalOrderId(String originalRemovalOrderId) {
        this.originalRemovalOrderId = originalRemovalOrderId;
    }

    public String getLiquidationProceedsCurrency() {
        return liquidationProceedsCurrency;
    }

    public void setLiquidationProceedsCurrency(String liquidationProceedsCurrency) {
        this.liquidationProceedsCurrency = liquidationProceedsCurrency;
    }

    public BigDecimal getLiquidationProceedsAmount() {
        return liquidationProceedsAmount;
    }

    public void setLiquidationProceedsAmount(BigDecimal liquidationProceedsAmount) {
        this.liquidationProceedsAmount = liquidationProceedsAmount;
    }

    public String getLiquidationFeeCurrency() {
        return liquidationFeeCurrency;
    }

    public void setLiquidationFeeCurrency(String liquidationFeeCurrency) {
        this.liquidationFeeCurrency = liquidationFeeCurrency;
    }

    public BigDecimal getLiquidationFeeAmount() {
        return liquidationFeeAmount;
    }

    public void setLiquidationFeeAmount(BigDecimal liquidationFeeAmount) {
        this.liquidationFeeAmount = liquidationFeeAmount;
    }
}
