package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;

import com.kindminds.drs.data.pipelines.core.accounting.journalentry.*;
import com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal.DrsJournalEntryDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatementEntryImpl implements JournalEntry {
    private Date transactionDate;
    private String period;
    private Date periodStart;
    private Date periodEnd;
    private String companyCode;
    private String statementName;
    private BigDecimal totalDebit;
    private List<JournalEntryItem> journalEntryItems;

    private DrsJournalEntryDao jooqDao = new DrsJournalEntryDao() ;

    public StatementEntryImpl(Date transactionDate, String period, String companyCode,
                              String statementName, Date periodStart, Date periodEnd) {
        this.transactionDate = transactionDate;
        this.period = period;
        this.companyCode = companyCode;
        this.statementName = statementName;
        this.journalEntryItems = new ArrayList<>();
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
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

        journalEntryItems.add(generateCurrentBalanceItem());

        journalEntryItems.addAll(generateIvsPaymentRefundItems());

        journalEntryItems.addAll(generateKmiSupplierPaymentItems());

        JournalEntryItem previousBalance = generatePreviousBalanceItem();
        if (previousBalance != null) {
            journalEntryItems.add(previousBalance);
        }

        JournalEntryItem profitCostTax = generateProfitCostShareTaxItem();
        if (profitCostTax != null) {
            journalEntryItems.add(profitCostTax);
        }

        journalEntryItems.addAll(generateProfitCostShareItems());

        journalEntryItems.addAll(generateServiceExpenseTaxItems());

        journalEntryItems.addAll(generateServiceExpenseItems());

        journalEntryItems.addAll(generateSellBackItems());

    }

    private List<JournalEntryItem> generateServiceExpenseItems() {
        List<JournalEntryItem> serviceExpenseItems = new ArrayList<>();
        //Amount, Type_id from domestic_transaction_line_item for given period,  supplier

        List<ServiceExpenseRow> typeNameAmountList = jooqDao.queryServiceExpenseInfo(
                companyCode, periodStart, periodEnd);


        for (ServiceExpenseRow item : typeNameAmountList) {
            //Get Type Name by given transaction type id from table transactionlinetype
            String typeName = item.transactionLineType();

            String accountCode;

            if (typeName.equals("INVENTORY_SHIPMENT")) {
                accountCode = "4882";
            } else {
                accountCode = "4883";
            }

            String label = period + " " + statementName + " " + typeName;
            BigDecimal amount = item.lineAmount();

            ServiceExpense serviceExpenseItem = new ServiceExpense(
                    statementName, accountCode, label, amount, this.companyCode);

            serviceExpenseItems.add(serviceExpenseItem);
        }

        return serviceExpenseItems;
    }

    private List<JournalEntryItem> generateServiceExpenseTaxItems() {
        List<JournalEntryItem> serviceExpenseTaxItems = new ArrayList<>();

        List<ServiceExpenseTaxRow> domesticAmountTaxLines = jooqDao.queryServiceExpenseTaxInfo(
                companyCode, periodStart, periodEnd);


        for (ServiceExpenseTaxRow expenseTax : domesticAmountTaxLines) {
            String label = period + " " + statementName;

            ServiceExpenseTax serviceExpenseItem = new ServiceExpenseTax(
                    statementName, label, expenseTax.amountTax(), this.companyCode);

            serviceExpenseTaxItems.add(serviceExpenseItem);
        }

        return serviceExpenseTaxItems;
    }

    private List<JournalEntryItem> generateSellBackItems() {
        List<JournalEntryItem> sellBackItems = new ArrayList<>();

        List<SellBackRow> typeNameSumAmountList = jooqDao.querySellBackInfo(statementName);

        for (SellBackRow sellBack : typeNameSumAmountList) {

            String typeName = sellBack.statementLineType();

            String label = period + " " + statementName + " " + typeName;
            BigDecimal sumAmount = sellBack.sumAmount();
            if (sumAmount.compareTo(BigDecimal.ZERO) < 0) {
                sumAmount = sumAmount.negate();
            }
            SellBackItem sellBackItem = null;

            if(typeName.equals("SS2SP_InventorySellBackRecovery")){
                sellBackItem = new SellBackItem(
                        statementName,"2171" , label,sumAmount , BigDecimal.ZERO , this.companyCode);
            }else{
                sellBackItem = new SellBackItem(
                                statementName,"1172" , label, BigDecimal.ZERO ,sumAmount, this.companyCode);
            }

            sellBackItems.add(sellBackItem);
        }


        return sellBackItems;
    }

    private List<JournalEntryItem> generateIvsPaymentRefundItems() {
        List<JournalEntryItem> ivsPaymentItems = new ArrayList<>();
        List<IVSPaymentRefundRow> refSumAmountList = jooqDao.queryIVSPaymentRefundInfo(statementName);


        for (IVSPaymentRefundRow refAmount : refSumAmountList) {
            BigDecimal debit, credit;

            String label = refAmount.reference() + " " + period + " " + statementName;
            BigDecimal sumAmount = refAmount.sumAmount();
            if (sumAmount.compareTo(BigDecimal.ZERO) >= 0) {
                debit = sumAmount;
                credit = BigDecimal.ZERO;
            } else {
                debit = BigDecimal.ZERO;
                credit = sumAmount.negate();
            }

            IvsPayment ivsPayment = new IvsPayment(
                    statementName, label, debit, credit, this.companyCode);

            ivsPaymentItems.add(ivsPayment);
        }

        return ivsPaymentItems;
    }

    private List<JournalEntryItem> generateKmiSupplierPaymentItems() {

        List<JournalEntryItem> kmiSupplierPaymentItems = new ArrayList<>();

        List<KMISupplierPaymentRow> kmiSupplierPaymentRows = jooqDao.queryKMISupplierPaymentInfo(
                companyCode, periodStart, periodEnd);

        for (KMISupplierPaymentRow item : kmiSupplierPaymentRows) {
            String typeName = "payment from SP";
            String accountCode = "1172";
            BigDecimal debit = item.amount();
            BigDecimal credit = BigDecimal.ZERO;

            if (item.sender().equals("K2")) {
                typeName = "payment from KMI";
                accountCode = "2171";
                debit = BigDecimal.ZERO;
                credit = item.amount();
            }

            String label = companyCode + " " + item.dateSent() + " " + typeName;

            KMISupplierPaymentItem kmiSupplierPaymentItem = new KMISupplierPaymentItem(
                    statementName, accountCode, label, debit, credit, this.companyCode);

            kmiSupplierPaymentItems.add(kmiSupplierPaymentItem);
        }

        return kmiSupplierPaymentItems;
    }


    private JournalEntryItem generatePreviousBalanceItem() {
        PreviousBalanceRow previousBalanceInfo = jooqDao.queryPreviousBalanceInfo(statementName);
        if (previousBalanceInfo.sequence() < 2) {
            return null;
        }

        String previousPeriod = previousBalanceInfo.periodStart().substring(0,10).replaceAll("-","")
                + "-" + previousBalanceInfo.periodEnd().substring(0,10).replaceAll("-","");
        String previousStatement = statementName.substring(0, statementName.lastIndexOf("-") + 1)
                + (previousBalanceInfo.sequence() - 1);
        BigDecimal previousBalance = previousBalanceInfo.previousBalance();
        String accountCode;
        BigDecimal debit, credit;

        if (previousBalance.compareTo(BigDecimal.ZERO) >= 0) {
            accountCode = "2171";
            debit = previousBalance;
            credit = BigDecimal.ZERO;
        } else {
            accountCode = "1172";
            debit = BigDecimal.ZERO;
            credit = previousBalance.negate();
        }

        String label = previousPeriod + " " + previousStatement;

        return new PreviousBalanceItem(statementName, accountCode, label, debit, credit, companyCode);
    }

    private JournalEntryItem generateCurrentBalanceItem() {
        CurrentBalanceRow currentBalanceRow = jooqDao.queryCurrentBalanceInfo(statementName);
        if (currentBalanceRow == null) return null;

        BigDecimal balance = currentBalanceRow.currentBalance();

        String accountCode;
        BigDecimal debit, credit;

        if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            accountCode = "2171";
            debit = BigDecimal.ZERO;
            credit = balance;
        } else {
            accountCode = "1172";
            debit = balance.negate();
            credit = BigDecimal.ZERO;
        }

        String label = period + " " + statementName;

        return new CurrentBalanceItem(statementName, accountCode, label, debit, credit, companyCode);
    }

    private List<JournalEntryItem> generateProfitCostShareItems() {
        List<JournalEntryItem> profitCostShareItems = new ArrayList<>();
        List<ProfitCostShareRow> profitCostShareList = jooqDao.queryProfitCostShareInfo(statementName);

        BigDecimal subtotal = BigDecimal.ZERO;
        for (ProfitCostShareRow profitCostItem : profitCostShareList) {
            subtotal = subtotal.add(profitCostItem.statementAmountUntaxed());
        }
        String accountCode;
        if (subtotal.compareTo(BigDecimal.ZERO) >= 0) {
            accountCode = "5802";
        } else {
            accountCode = "4881";
        }

        for (ProfitCostShareRow item : profitCostShareList) {
            BigDecimal debit, credit;

            String label = period + " " + statementName;

            BigDecimal amount = item.statementAmountUntaxed();
            if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                debit = amount;
                credit = BigDecimal.ZERO;
            } else {
                debit = BigDecimal.ZERO;
                credit = amount.negate();
            }

            ProfitCostShareItem profitCostShare = new ProfitCostShareItem(
                    statementName, accountCode, label, debit, credit, this.companyCode);

            profitCostShareItems.add(profitCostShare);
        }

        return profitCostShareItems;
    }

    private JournalEntryItem generateProfitCostShareTaxItem() {
        ProfitCostShareTaxRow profitCostShareTax = jooqDao.queryProfitCostShareTaxInfo(statementName);
        if (profitCostShareTax.statementAmount().compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        String accountCode;
        BigDecimal debit, credit;
        BigDecimal amount = profitCostShareTax.statementAmount();

        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            accountCode = "1423";
            debit = amount;
            credit = BigDecimal.ZERO;
        } else {
            accountCode = "2214";
            debit = BigDecimal.ZERO;
            credit = amount.negate();
        }
        String label = period + " " + statementName;

        return new ProfitCostShareTaxItem(statementName, accountCode, label, debit, credit, companyCode);
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
