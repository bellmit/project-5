package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;


import com.kindminds.drs.data.pipelines.core.accounting.journalentry.JournalEntryItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SellBackItem extends StatementItemImpl {

    public SellBackItem(String statementName, String accountCode,
                        String label,BigDecimal debit , BigDecimal credit, String companyCode) {
//        1. Data source: inquire data from table bill_statementlineitem for given statement name.
//   - Filter: Stlmnt_line_item_type_id in ('23','26','27','28','30')
//   - Columns
//      Stlmnt_line_item_type_id, Sum(Statement_amount)
//2. Get Type Name by given Stlmnt_line_item_type_id from table statement_line_type
//3. Get  Account code for sellback : '1172'
//4. Get Label as Period +' '+ Statement Name +' '+ Type name
//5. Get Debit = 0
//6. Get Credit = Sum(Statement_amount)
//7. Get Partner = Supplier's K code
//8. Get Market = '-'
//9. Get Department = '-'
//10. Get type_1 = '-'
//11. Get type_2 = 'DRS'

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
