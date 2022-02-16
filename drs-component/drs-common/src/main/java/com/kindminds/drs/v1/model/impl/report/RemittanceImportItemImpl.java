package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.RemittanceImportItem;

import java.math.BigDecimal;

public class RemittanceImportItemImpl implements RemittanceImportItem {
    private String dateSent;
    private String dateReceived;
    private Integer sender;
    private Integer receiver;
    private BigDecimal amount;
    private Integer currency;
    private String reference;
    private BigDecimal feeAmount;
    private Boolean feeIncluded;
    private String statementName;
    private BigDecimal bankPayment;

    public RemittanceImportItemImpl() {}

    public RemittanceImportItemImpl(String dateSent, String dateReceived, Integer sender,
                                    Integer receiver, BigDecimal amount,
                                    Integer currency, String reference, BigDecimal feeAmount,
                                    Boolean feeIncluded, String statementName, BigDecimal bankPayment) {
        this.dateSent = dateSent;
        this.dateReceived = dateReceived;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.currency = currency;
        this.reference = reference;
        this.feeAmount = feeAmount;
        this.feeIncluded = feeIncluded;
        this.statementName = statementName;
        this.bankPayment = bankPayment;
    }

    @Override
    public String getDateSent() {
        return dateSent;
    }

    @Override
    public String getDateReceived() {
        return dateReceived;
    }

    @Override
    public Integer getSender() {
        return sender;
    }

    @Override
    public Integer getReceiver() {
        return receiver;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public Integer getCurrency() {
        return currency;
    }

    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    @Override
    public Boolean getFeeIncluded() {
        return feeIncluded;
    }

    @Override
    public String getStatementName() {
        return statementName;
    }

    @Override
    public BigDecimal getBankPayment() {
        return bankPayment;
    }

    @Override
    public String toString() {
        return "RemittanceImportItemImpl{" +
                "dateSent='" + dateSent + '\'' +
                ", dateReceived='" + dateReceived + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", amount=" + amount +
                ", currency=" + currency +
                ", reference='" + reference + '\'' +
                '}';
    }


}
