package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonTDSReimbursementEvent {
    private int id;
    private Date postedDate;
    private String tdsOrderId;
    private String reimbursedCurrency;
    private BigDecimal reimbursedAmount;

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

    public String getTdsOrderId() {
        return tdsOrderId;
    }

    public void setTdsOrderId(String tdsOrderId) {
        this.tdsOrderId = tdsOrderId;
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
}
