package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonSAFETReimbursementEvent {
    private int id;
    private Date postedDate;
    private String safetClaimId;
    private String reimbursedCurrency;
    private BigDecimal reimbursedAmount;
    private String reasonCode;
    private int safetReimbursementItemId;

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

    public String getSafetClaimId() {
        return safetClaimId;
    }

    public void setSafetClaimId(String safetClaimId) {
        this.safetClaimId = safetClaimId;
    }

    public String getReimbursedCurrency() {
        return reimbursedCurrency;
    }

    public void setReimbursedCurrency(String reimbursedCurrency) {
        this.reimbursedCurrency = reimbursedCurrency;
    }

    public BigDecimal getReimbursedAmount() {
        return reimbursedAmount;
    }

    public void setReimbursedAmount(BigDecimal reimbursedAmount) {
        this.reimbursedAmount = reimbursedAmount;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public int getSafetReimbursementItemId() {
        return safetReimbursementItemId;
    }

    public void setSafetReimbursementItemId(int safetReimbursementItemId) {
        this.safetReimbursementItemId = safetReimbursementItemId;
    }
}
