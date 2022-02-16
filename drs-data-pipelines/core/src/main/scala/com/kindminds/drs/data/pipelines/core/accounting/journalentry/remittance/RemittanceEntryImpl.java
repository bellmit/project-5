package com.kindminds.drs.data.pipelines.core.accounting.journalentry.remittance;

import com.kindminds.drs.data.pipelines.core.accounting.journalentry.*;
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal.DrsJournalEntryDao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemittanceEntryImpl implements JournalEntry {

    private Date transactionDate;
    private String period;
    private String companyCode;
    private String statementName;
    private BigDecimal totalDebit;
    private List<JournalEntryItem> journalEntryItems;
    private List<RemittanceInfo> remittanceList;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private DrsJournalEntryDao jooqDao = new DrsJournalEntryDao() ;

    public RemittanceEntryImpl(Date transactionDate, String period, List<RemittanceInfo> remittances) {

        this.transactionDate = transactionDate;
        this.period = period;
        this.remittanceList = remittances;
        this.journalEntryItems = new ArrayList<>();
    }

    @Override
    public String getPeriod() {
        return period;
    }

    @Override
    public String getCompanyCode() {
        return companyCode;
    }

    @Override
    public Date getTransactionDate() {
        return transactionDate;
    }

    @Override
    public String getStatementName() {
        return statementName;
    }

    @Override
    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    @Override
    public List<JournalEntryItem> getJournalEntryItems() {
        return journalEntryItems;
    }

    @Override
    public void generateEntryRawData() {

        generateJournalEntryItems();

        calculateTotalDebit();

    }

    private void generateJournalEntryItems() {

        for (RemittanceInfo remittance : remittanceList) {

            journalEntryItems.add(generatePreviousBalanceOffset(remittance));

        }

        for (RemittanceInfo remittance : remittanceList) {

            JournalEntryItem remittanceFee = generateRemittanceFee(remittance);

            if (remittanceFee != null) {
                journalEntryItems.add(remittanceFee);
            }

            journalEntryItems.add(generateBankSaving(remittance));

        }


    }

    private JournalEntryItem generateRemittanceFee(RemittanceInfo r) {
        if (r.feeIncluded()) {
            return null;
        }

        return new RemittanceFee(r.statementName(), r.feeAmount(), r.companyCode());
    }

    private JournalEntryItem generateBankSaving(RemittanceInfo r) {

        String label = r.companyCode() + " " + sdf.format(r.dateSent()) + " " + "payment from KMI";

        BigDecimal credit = r.feeIncluded() ? r.amount() : r.amount().add(r.feeAmount());

        return new BankSaving(r.statementName(), label, credit, r.companyCode());

    }

    private JournalEntryItem generatePreviousBalanceOffset(RemittanceInfo r) {
        String label = period + " " + r.statementName();

        return new PreviousBalanceOffset(r.statementName(), label, r.amount(), r.companyCode());
    }

    private void calculateTotalDebit() {
        BigDecimal totalDebit = BigDecimal.ZERO;

        for (JournalEntryItem item : journalEntryItems) {
            totalDebit = totalDebit.add(item.getDebit());
        }
        this.totalDebit = totalDebit;
    }


    @Override
    public String toString() {
        return "JournalEntry{" +
                "transactionDate=" + transactionDate +
                ", period='" + period + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", statementName='" + statementName + '\'' +
                ", totalDebit=" + totalDebit +
                '}';
    }
}
