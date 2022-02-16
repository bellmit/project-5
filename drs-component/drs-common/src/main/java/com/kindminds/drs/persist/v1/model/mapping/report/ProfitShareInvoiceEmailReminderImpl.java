package com.kindminds.drs.persist.v1.model.mapping.report;

import com.kindminds.drs.api.v1.model.report.ProfitShareInvoiceEmailReminder;




import java.io.Serializable;

/**
 * @author Colum.Brolly
 * <h1>ProfitShareInvoiceEmailReminder DTO</h1>
 * <p>
 * Object to store list of suppliers that will receive an email reminder to create invoice
 * </p>
 */

public class ProfitShareInvoiceEmailReminderImpl implements ProfitShareInvoiceEmailReminder, Serializable {

    private static final long serialVersionUID = 1L;

    //@Id //@Column(name = "statement_name")
    private String statementName;
    //@Id //@Column(name = "user_name")
    private String userName;
    //@Column(name = "period_start_utc")
    private String periodStartUtc;
    //@Column(name = "period_end_utc")
    private String periodEndUtc;

    public ProfitShareInvoiceEmailReminderImpl() {
    }

    public ProfitShareInvoiceEmailReminderImpl(String statementName, String userName, String periodStartUtc, String periodEndUtc) {
        this.statementName = statementName;
        this.userName = userName;
        this.periodStartUtc = periodStartUtc;
        this.periodEndUtc = periodEndUtc;
    }


    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getStatementName() {
        return this.statementName;
    }

    @Override
    public String getPeriodStartUtc() {
        return this.periodStartUtc;
    }

    @Override
    public String getPeriodEndUtc() {
        return this.periodEndUtc;
    }

    public void setStatementName(String statementName) {
        this.statementName = statementName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPeriodStartUtc(String periodStartUtc) {
        this.periodStartUtc = periodStartUtc;
    }

    public void setPeriodEndUtc(String periodEndUtc) {
        this.periodEndUtc = periodEndUtc;
    }
}
