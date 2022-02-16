package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;


import com.kindminds.drs.data.pipelines.core.accounting.journalentry.JournalEntryItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ServiceExpenseTax extends StatementItemImpl {

    public ServiceExpenseTax(String statementName, String label,
                             BigDecimal credit, String companyCode) {
//        1. Data source: inquire data from table domestic_transaction for given period,  supplier.
//   - Columns:
//      Amount_tax from domestic_transaction_line_item
//2. Get  Account code for service expense tax
//    Account code = '2214'
//3. Get Label as Period + ' ' +Statement Name
//4. Get Debit = 0
//5. Get Credit = Amount_tax
//6. Get Partner = Supplier's K code
//7. Get Market = '-'
//8. Get Department = '-'
//9. Get type_1 = '-'
//10. Get type_2 = 'DRS'

        this.statementName = statementName;
        this.accountCode = "2214";
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
