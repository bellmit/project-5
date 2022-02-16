package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;


import java.math.BigDecimal;

public class CurrentBalanceItem extends StatementItemImpl {
//    1. Data source: inquire data from table bill_statement for given statement name.
//      - Columns: Balance
//2. Get  Account code for previous balance : If Balance>=0, Account code ='2171', otherwise '1172'
//            3. Get Label as Period +' '+ Statement Name
//4. Get Debit: If Balance>=0, Debit = 0, otherwise Debit = Balance
//5. Get Credit = If Balance>=0, Credit = Balance, otherwise Credit= 0
//6. Get Partner = Supplier's K code
//            7. Get Market = '-'
//8. Get Department = '-'
//9. Get type_1 = '-'
//10. Get type_2 = 'DRS'

    public CurrentBalanceItem(String statementName,
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


}
