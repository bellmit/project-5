package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;


import java.math.BigDecimal;

public class PreviousBalanceItem extends StatementItemImpl {
//    ta source: inquire data from table bill_statement for given statement name.
//      - Columns: Previous_balance
//2. Get Previous_period and Previous_statement
//     - Previous_period = Period -14 days
//     - Previous_statement_name = get statement name from table bill_statement by Previous_period and Supplier's K code
//            3. Get  Account code for previous balance : If Previous_balance>=0, Account code ='2171', otherwise '1172'
//            4. Get Label as Previous_period + ' ' +Previous_statement_name
//5. Get Debit: If Previous_balance>=0, Debit = Previous_balance, otherwise Debit = 0
//6. Get Credit = If Previous_balance>=0, Credit = 0, otherwise Credit= Previous_balance
//7. Get Partner = Supplier's K code
//            8. Get Market = '-'
//9. Get Department = '-'
//10. Get type_1 = '-'
//11. Get type_

    public PreviousBalanceItem(String statementName,
                               String accountCode, String label,
                               BigDecimal debit, BigDecimal credit, String companyCode) {

        this.statementName = statementName;
        this.accountCode = accountCode;
        this.label = label;
        this.debit = debit;
        this.credit = credit;
        this.companyCode = companyCode;
        this.market = "-";
        this.department = "-";
        this.type1 = "-";
        this.type2 = "DRS";

    }

    @Override
    public String toString() {
        return "PreviousBalanceItem{" +
                ", statementName='" + statementName + '\'' +
                ", accountCode='" + accountCode + '\'' +
                ", label='" + label + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", companyCode='" + companyCode + '\'' +
                '}';
    }
}
