package com.kindminds.drs.data.pipelines.core.accounting.journalentry;

import java.math.BigDecimal;

public interface JournalEntryItem {

    String getStatementName();
    String getAccountCode();
    String getLabel();
    BigDecimal getDebit();
    BigDecimal getCredit();
    String getCompanyCode();
    String getPartner();
    String getMarket();
    String getDepartment();
    String getType1();
    String getType2();
}
