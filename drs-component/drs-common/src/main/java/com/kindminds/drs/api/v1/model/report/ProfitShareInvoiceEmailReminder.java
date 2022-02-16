package com.kindminds.drs.api.v1.model.report;

public interface ProfitShareInvoiceEmailReminder {
    public String getStatementName();
    public String getUserName();
    public String getPeriodStartUtc();
    public String getPeriodEndUtc();
}