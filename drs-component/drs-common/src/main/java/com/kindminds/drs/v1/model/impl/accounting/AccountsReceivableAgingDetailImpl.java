package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.api.v1.model.accounting.AccountsReceivableAgingDetail;

import java.math.BigDecimal;
import java.util.Date;

public class AccountsReceivableAgingDetailImpl implements AccountsReceivableAgingDetail {

    private Date reportDate;
    private String kCode;
    private String supplierName;
    private String statementName;
    private String currencyCode;
    private Date periodStart;
    private Date periodEnd;
    private BigDecimal accountsReceivableOnReportDate;
    private BigDecimal originalAccountsReceivable;
    private Date invoiceDate;
    private BigDecimal aging;
    private String agingRange;
    private String accountManagerInCharge;
    private String specialPaymentTerm;
    private BigDecimal accountsReceivableOnReportDateUsd;
    private Integer splrCompanyId;

    public AccountsReceivableAgingDetailImpl() {

    }

    public AccountsReceivableAgingDetailImpl(Object[] result) {
        this.reportDate = (Date) result[0];
        this.kCode = (String) result[1];
        this.supplierName = (String) result[2];
        this.statementName = (String) result[3];
        this.currencyCode = (String) result[4];
        this.periodStart = (Date) result[5];
        this.periodEnd = (Date) result[6];
        this.accountsReceivableOnReportDate = (BigDecimal) result[7];
        this.originalAccountsReceivable = (BigDecimal) result[8];
        this.invoiceDate = (Date) result[9];
        this.aging = (BigDecimal) result[10];
        this.agingRange = (String) result[11];
        this.accountManagerInCharge = (String) result[12];
        this.specialPaymentTerm = (String) result[13];
        this.accountsReceivableOnReportDateUsd = (BigDecimal) result[14];
        this.splrCompanyId = (Integer) result[15];
    }

    public AccountsReceivableAgingDetailImpl(Date reportDate, String kCode, String supplierName,
                                             String statementName, String currencyCode,
                                             Date periodStart, Date periodEnd,
                                             BigDecimal accountsReceivableOnReportDate,
                                             BigDecimal originalAccountsReceivable, Date invoiceDate,
                                             BigDecimal aging, String agingRange,
                                             String accountManagerInCharge, String specialPaymentTerm,
                                             BigDecimal accountsReceivableOnReportDateUsd, Integer splrCompanyId) {
        this.reportDate = reportDate;
        this.kCode = kCode;
        this.supplierName = supplierName;
        this.statementName = statementName;
        this.currencyCode = currencyCode;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.accountsReceivableOnReportDate = accountsReceivableOnReportDate;
        this.originalAccountsReceivable = originalAccountsReceivable;
        this.invoiceDate = invoiceDate;
        this.aging = aging;
        this.agingRange = agingRange;
        this.accountManagerInCharge = accountManagerInCharge;
        this.specialPaymentTerm = specialPaymentTerm;
        this.accountsReceivableOnReportDateUsd = accountsReceivableOnReportDateUsd;
        this.splrCompanyId = splrCompanyId;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public String getkCode() {
        return kCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getStatementName() {
        return statementName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public BigDecimal getAccountsReceivableOnReportDate() {
        return accountsReceivableOnReportDate;
    }

    public BigDecimal getOriginalAccountsReceivable() {
        return originalAccountsReceivable;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public BigDecimal getAging() {
        return aging;
    }

    public String getAgingRange() {
        return agingRange;
    }

    public String getAccountManagerInCharge() {
        return accountManagerInCharge;
    }

    public String getSpecialPaymentTerm() {
        return specialPaymentTerm;
    }

    public BigDecimal getAccountsReceivableOnReportDateUsd() {
        return accountsReceivableOnReportDateUsd;
    }

    public Integer getSplrCompanyId() {
        return splrCompanyId;
    }
}
