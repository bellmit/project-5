package com.kindminds.drs.v1.model.impl.amazon;

import com.kindminds.drs.api.v1.model.amazon.AmazonReimbursementInfo;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonReimbursementInfoImpl implements AmazonReimbursementInfo {

    private Date approvalDate;
    private String reimbursementId;
    private String amazonOrderId;
    private String reason;
    private String sku;
    private String currencyUnit;
    private BigDecimal amountPerUnit;
    private BigDecimal amountTotal;
    private BigDecimal quantityReimbursementCash;
    private BigDecimal quantityReimbursementInventory;
    private String originalReimbursementId;
    private String originalReimbursementType;

    public AmazonReimbursementInfoImpl() {
    }


    public AmazonReimbursementInfoImpl(Date approvalDate, String reimbursementId,
                                       String amazonOrderId, String reason,
                                       String sku, String currencyUnit,
                                       BigDecimal amountPerUnit, BigDecimal amountTotal,
                                       BigDecimal quantityReimbursementCash, BigDecimal quantityReimbursementInventory,
                                       String originalReimbursementId, String originalReimbursementType) {
        this.approvalDate = approvalDate;
        this.reimbursementId = reimbursementId;
        this.amazonOrderId = amazonOrderId;
        this.reason = reason;
        this.sku = sku;
        this.currencyUnit = currencyUnit;
        this.amountPerUnit = amountPerUnit;
        this.amountTotal = amountTotal;
        this.quantityReimbursementCash = quantityReimbursementCash;
        this.quantityReimbursementInventory = quantityReimbursementInventory;
        this.originalReimbursementId = originalReimbursementId;
        this.originalReimbursementType = originalReimbursementType;
    }

    public AmazonReimbursementInfoImpl(Object[] reimbursementInfo) {

        this.approvalDate = (Date) reimbursementInfo[0];
        this.reimbursementId = (String) reimbursementInfo[1];
        this.amazonOrderId = (String) reimbursementInfo[2];
        this.reason = (String) reimbursementInfo[3];
        this.sku = (String) reimbursementInfo[4];
        this.currencyUnit = (String) reimbursementInfo[5];
        this.amountPerUnit = (BigDecimal) reimbursementInfo[6];
        this.amountTotal = (BigDecimal) reimbursementInfo[7];
        this.quantityReimbursementCash = (BigDecimal) reimbursementInfo[8];

        this.originalReimbursementId = (String) reimbursementInfo[10];
        this.originalReimbursementType = (String) reimbursementInfo[11];

        this.quantityReimbursementInventory =  reimbursementInfo[9] == null ?
                (BigDecimal) reimbursementInfo[12] : (BigDecimal) reimbursementInfo[9] ;


    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public String getReimbursementId() {
        return reimbursementId;
    }

    public String getSku() {
        return sku;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public String getReason() {
        return reason;
    }

    public BigDecimal getAmountPerUnit() {
        return amountPerUnit;
    }

    public BigDecimal getAmountTotal() {
        return amountTotal;
    }

    public BigDecimal getQuantityReimbursementCash() {
        return quantityReimbursementCash;
    }

    public BigDecimal getQuantityReimbursementInventory() {
        return quantityReimbursementInventory;
    }

    public String getOriginalReimbursementId() {
        return originalReimbursementId;
    }

    public String getOriginalReimbursementType() {
        return originalReimbursementType;
    }
}
