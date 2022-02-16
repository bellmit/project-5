package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;

public interface RemittanceImportItem {
    String getDateSent();
    String getDateReceived();
    Integer getSender();
    Integer getReceiver();
    BigDecimal getAmount();
    Integer getCurrency();
    String getReference();
    BigDecimal getFeeAmount();
    Boolean getFeeIncluded();
    String getStatementName();
    BigDecimal getBankPayment();
}
