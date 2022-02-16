package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonRetrochargeEvent {
    private String retrochargeEventType;
    private String amazonOrderId;
    private Date postedDate;
    private String baseTaxCurrency;
    private BigDecimal baseTaxAmount;
    private String shippingTaxCurrency;
    private BigDecimal shippingTaxAmount;
    private String marketplaceName;
    private int taxWithheldComponentId;

    public String getRetrochargeEventType() {
        return retrochargeEventType;
    }

    public void setRetrochargeEventType(String retrochargeEventType) {
        this.retrochargeEventType = retrochargeEventType;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getBaseTaxCurrency() {
        return baseTaxCurrency;
    }

    public void setBaseTaxCurrency(String taxCurrency) {
        this.baseTaxCurrency = taxCurrency;
    }

    public BigDecimal getBaseTaxAmount() {
        return baseTaxAmount;
    }

    public void setBaseTaxAmount(BigDecimal baseTaxAmount) {
        this.baseTaxAmount = baseTaxAmount;
    }

    public String getShippingTaxCurrency() {
        return shippingTaxCurrency;
    }

    public void setShippingTaxCurrency(String shippingTaxCurrency) {
        this.shippingTaxCurrency = shippingTaxCurrency;
    }

    public BigDecimal getShippingTaxAmount() {
        return shippingTaxAmount;
    }

    public void setShippingTaxAmount(BigDecimal shippingTaxAmount) {
        this.shippingTaxAmount = shippingTaxAmount;
    }

    public String getMarketplaceName() {
        return marketplaceName;
    }

    public void setMarketplaceName(String marketplaceName) {
        this.marketplaceName = marketplaceName;
    }

    public int getTaxWithheldComponentId() {
        return taxWithheldComponentId;
    }

    public void setTaxWithheldComponentId(int taxWithheldComponentId) {
        this.taxWithheldComponentId = taxWithheldComponentId;
    }
}
