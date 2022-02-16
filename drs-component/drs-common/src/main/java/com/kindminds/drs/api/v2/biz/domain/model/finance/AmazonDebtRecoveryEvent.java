package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;

public class AmazonDebtRecoveryEvent {
    private int id;
    private String debtRecoveryType;
    private String currency;
    private BigDecimal recoveryAmount;
    private BigDecimal overPaymentCredit;
    private int debtRecoveryItemId;
    private int chargeInstrumentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDebtRecoveryType() {
        return debtRecoveryType;
    }

    public void setDebtRecoveryType(String debtRecoveryType) {
        this.debtRecoveryType = debtRecoveryType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRecoveryAmount() {
        return recoveryAmount;
    }

    public void setRecoveryAmount(BigDecimal recoveryAmount) {
        this.recoveryAmount = recoveryAmount;
    }

    public BigDecimal getOverPaymentCredit() {
        return overPaymentCredit;
    }

    public void setOverPaymentCredit(BigDecimal overPaymentCredit) {
        this.overPaymentCredit = overPaymentCredit;
    }

    public int getDebtRecoveryItemId() {
        return debtRecoveryItemId;
    }

    public void setDebtRecoveryItemId(int debtRecoveryItemId) {
        this.debtRecoveryItemId = debtRecoveryItemId;
    }

    public int getChargeInstrumentId() {
        return chargeInstrumentId;
    }

    public void setChargeInstrumentId(int chargeInstrumentId) {
        this.chargeInstrumentId = chargeInstrumentId;
    }
}
