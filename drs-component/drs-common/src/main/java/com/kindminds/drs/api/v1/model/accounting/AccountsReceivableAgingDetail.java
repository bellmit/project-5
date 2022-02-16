package com.kindminds.drs.api.v1.model.accounting;

import java.math.BigDecimal;
import java.util.Date;

public interface AccountsReceivableAgingDetail {

    Date getReportDate();
    String getkCode();
    String getSupplierName();
    String getStatementName();
    String getCurrencyCode();
    Date getPeriodStart();
    Date getPeriodEnd();
    BigDecimal getAccountsReceivableOnReportDate();
    BigDecimal getOriginalAccountsReceivable();
    Date getInvoiceDate();
    BigDecimal getAging();
    String getAgingRange();
    String getAccountManagerInCharge();
    String getSpecialPaymentTerm();
    BigDecimal getAccountsReceivableOnReportDateUsd();
    Integer getSplrCompanyId();
}
