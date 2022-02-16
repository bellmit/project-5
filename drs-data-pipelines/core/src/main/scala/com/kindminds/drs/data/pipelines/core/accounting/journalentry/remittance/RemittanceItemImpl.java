package com.kindminds.drs.data.pipelines.core.accounting.journalentry.remittance;


import com.kindminds.drs.data.pipelines.core.accounting.journalentry.JournalEntryItem;

import java.math.BigDecimal;

public abstract class RemittanceItemImpl implements JournalEntryItem {
    String statementName;
    String accountCode;
    String label;
    BigDecimal debit;
    BigDecimal credit;
    String companyCode;
    String partner;
    String market;
    String department;
    String type1;
    String type2;


    public String getStatementName() {
        return statementName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getDebit() {
        return this.debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getPartner() {
        return partner;
    }

    public String getMarket() {
        return market;
    }

    public String getDepartment() {
        return department;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }
}
