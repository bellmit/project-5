package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;


import java.math.BigDecimal;

public class ServiceExpense extends StatementItemImpl {

    public ServiceExpense(String statementName,
                          String accountCode, String label,
                          BigDecimal credit, String companyCode) {
//        . Data source: inquire data from table domestic_transaction and domestic_transaction_line_item for given period,  supplier.
//        - Columns:
//        Amount, Type_id from domestic_transaction_line_item
//        2. Get Type Name by given transaction type id from table transactionlinetype
//        3. Get  Account code for service expense
//        a. if type name is 'INVENTORY_SHIPMENT', account code is '4882'
//        b. otherwise account code is '4883'
//        4. Get Label as Period + ' ' +Statement Name + ' ' + type name
//        5. Get Debit = 0
//        6. Get Credit = Amount
//        7. Get Partner = Supplier's K code
//        8. Get Market = '-'
//        9. Get Department = '-'
//        10. Get type_1 = '-'
//        11. Get type_2 = 'DRS'

        this.statementName = statementName;
        this.accountCode = accountCode;
        this.label = label;
        this.debit = BigDecimal.ZERO;
        this.credit = credit;
        this.companyCode = companyCode;
        this.market = "-";
        this.department = "-";
        this.type1 = "-";
        this.type2 = "DRS";


    }


}
