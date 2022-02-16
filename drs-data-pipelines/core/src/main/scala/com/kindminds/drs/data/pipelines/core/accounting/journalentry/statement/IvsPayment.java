package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;

import java.math.BigDecimal;

public class IvsPayment extends StatementItemImpl {

    public IvsPayment(String statementName, String label, BigDecimal debit, BigDecimal credit, String companyCode) {
//        Data source: inquire data from table bill_statementlineitem for given statement name.
//                - Filter: Stlmnt_line_item_type_id in ('12','13')
//        - Columns:
//        Reference, Stlmnt_line_item_type_id, Sum(Statement_amount)
//        2. Get Type Name by given Stlmnt_line_item_type_id from table statement_line_type
//        3. Get  Account code for sellback : '2171'
//        4. Get Label as Reference +' ' + Period +' '+ Statement Name
//        5. Get Debit: if Sum(Statement_amount)>=0, Debit=Sum(Statement_amount), otherwise Debit=0.
//        6. Get Credit: if Sum(Statement_amount)>=0, Credit=0, otherwise Credit = Sum(Statement_amount)
//        7. Get Partner = Supplier's K code
//        8. Get Market = '-'
//        9. Get Department = '-'
//        10. Get type_1 = '-'
//        11. Get type_2 = 'DRS'

        this.statementName = statementName;
        this.accountCode = "2171";
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
        return "IvsPayment{" +
                ", statementName='" + statementName + '\'' +
                ", accountCode='" + accountCode + '\'' +
                ", label='" + label + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", companyCode='" + companyCode + '\'' +
                '}';
    }
}
