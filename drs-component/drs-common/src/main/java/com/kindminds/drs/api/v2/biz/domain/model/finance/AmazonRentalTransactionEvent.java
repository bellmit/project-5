package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonRentalTransactionEvent {
    private int id;
    private String amazonOrderId;
    private String rentalEventType;
    private Integer extensionLength;
    private Date postedDate;
    private int rentalChargeId;
    private int rentalFeeId;
    private String marketplaceName;
    private String rentalInitialCurrency;
    private BigDecimal rentalInitialValue;
    private String rentalReimbursementCurrency;
    private BigDecimal rentalReimbursementAmount;
    private int rentalTaxWithheldId;

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

    public String getRentalEventType() {
        return rentalEventType;
    }

    public void setRentalEventType(String rentalEventType) {
        this.rentalEventType = rentalEventType;
    }

    public Integer getExtensionLength() {
        return extensionLength;
    }

    public void setExtensionLength(Integer extensionLength) {
        this.extensionLength = extensionLength;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public int getRentalChargeId() {
        return rentalChargeId;
    }

    public void setRentalChargeId(int rentalChargeId) {
        this.rentalChargeId = rentalChargeId;
    }

    public int getRentalFeeId() {
        return rentalFeeId;
    }

    public void setRentalFeeId(int rentalFeeId) {
        this.rentalFeeId = rentalFeeId;
    }

    public String getMarketplaceName() {
        return marketplaceName;
    }

    public void setMarketplaceName(String marketplaceName) {
        this.marketplaceName = marketplaceName;
    }

    public String getRentalInitialCurrency() {
        return rentalInitialCurrency;
    }

    public void setRentalInitialCurrency(String rentalInitialCurrency) {
        this.rentalInitialCurrency = rentalInitialCurrency;
    }

    public BigDecimal getRentalInitialValue() {
        return rentalInitialValue;
    }

    public void setRentalInitialValue(BigDecimal rentalInitialValue) {
        this.rentalInitialValue = rentalInitialValue;
    }

    public String getRentalReimbursementCurrency() {
        return rentalReimbursementCurrency;
    }

    public void setRentalReimbursementCurrency(String rentalReimbursementCurrency) {
        this.rentalReimbursementCurrency = rentalReimbursementCurrency;
    }

    public BigDecimal getRentalReimbursementAmount() {
        return rentalReimbursementAmount;
    }

    public void setRentalReimbursementAmount(BigDecimal rentalReimbursementAmount) {
        this.rentalReimbursementAmount = rentalReimbursementAmount;
    }

    public int getRentalTaxWithheldId() {
        return rentalTaxWithheldId;
    }

    public void setRentalTaxWithheldId(int rentalTaxWithheldId) {
        this.rentalTaxWithheldId = rentalTaxWithheldId;
    }
}
