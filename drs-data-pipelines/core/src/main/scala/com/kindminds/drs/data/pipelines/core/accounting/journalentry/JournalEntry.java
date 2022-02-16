package com.kindminds.drs.data.pipelines.core.accounting.journalentry;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface JournalEntry {

    String getPeriod();
    String getCompanyCode();
    Date getTransactionDate();
    String getStatementName();
    BigDecimal getTotalDebit();
    List<JournalEntryItem> getJournalEntryItems();

    void generateEntryRawData();

}
