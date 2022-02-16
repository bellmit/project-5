package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;


import java.math.BigDecimal;

public class ProfitCostShareTaxItem extends StatementItemImpl {

    public ProfitCostShareTaxItem(String statementName,
                                  String accountCode, String label,
                                  BigDecimal debit, BigDecimal credit, String companyCode) {
//        1. Data source: inquire data from table remittance for given period.
//        - Filter: Receiver_company = Supplier's kcode or Sender_company = Supplier's kcode
//        - Columns:
//        Receiver_company's kcode, Sender_company's kcode, Date_sent, Amount
//        2. Get Type Name: if sender_company='K2', Type name is 'payment from KMI', otherwise, Type name is 'payment from SP'
//        3. Get  Account code: if sender_company='K2', Account code is '2171', otherwise Account code is '1172'
//        4. Get Label as Supplier's kcode + ' '+Date_sent +' '+ Type Name
//        5. Get Debit: if sender_company='K2', Debit = 0, otherwise Debit = Amount.
//        6. Get Credit: if sender_company='K2', Credit=Amount, otherwise Credit = 0.
//        7. Get Partner = Supplier's K code
//        8. Get Market = '-'
//        9. Get Department = '-'
//        10. Get type_1 = '-'
//        11. Get type_2 = 'DRS'

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
