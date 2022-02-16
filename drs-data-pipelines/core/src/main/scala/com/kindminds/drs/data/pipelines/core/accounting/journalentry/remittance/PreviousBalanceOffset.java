package com.kindminds.drs.data.pipelines.core.accounting.journalentry.remittance;


import java.math.BigDecimal;

public class PreviousBalanceOffset extends RemittanceItemImpl {

    //        1. Get Receiver company's K code, Amount, Statement name from remittance data.
//        2. Get Period by Statement. For example, Statement name is 'STM-K598-45', and the period_start is '2020-01-19 08:00:00+08', period_end is '2020-02-02 08:00:00+08', thus Period is '2020119-20200201'
//        4. Set journal entry item.
//        -  Account code : '2171'
//                -  Label as Period + ' '+Statement name
//                -  Debit: Amount
//                -  Credit: 0
//                -  Partner = Receiver company's K code
//                -  Market = '-'
//                -  Department = '-'
//                -  type_1 = '-'
//                -  type_2 = 'DRS'
//        5. Generate journal entry item to offset previous balance
//        {id, Account Code, Label, Debit, Credit, Partner, Market, Department, type_1, type_2}
    public PreviousBalanceOffset(String statementName, String label,
                                 BigDecimal debit, String companyCode) {

        this.statementName = statementName;
        this.accountCode = "2171";
        this.label = label;
        this.debit = debit;
        this.credit = BigDecimal.ZERO;
        this.companyCode = companyCode;
        this.partner = "";
        this.market = "-";
        this.department = "-";
        this.type1 = "-";
        this.type2 = "DRS";


    }


}
