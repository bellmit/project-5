package com.kindminds.drs.data.pipelines.core.accounting.journalentry.remittance;


import com.kindminds.drs.data.pipelines.core.accounting.journalentry.JournalEntryItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RemittanceFee extends RemittanceItemImpl {

//        -  Account code : '7530'
//        -  Label as '手續費'
//        -  Debit: remittance fee
//        -  Credit: 0
//        -  Partner = '銀行手續費'
//        -  Market = '-'
//        -  Department = 'F&A'
//        -  type_1 = 'SQ&A'
//        -  type_2 = '-'
    public RemittanceFee(String statementName,
                         BigDecimal feeAmount, String companyCode) {

        this.statementName = statementName;
        this.accountCode = "7530";
        this.label = "手續費";
        this.debit = feeAmount;
        this.credit = BigDecimal.ZERO;
        this.companyCode = companyCode;
        this.partner = "銀行手續費";
        this.market = "-";
        this.department = "F&A";
        this.type1 = "SQ&A";
        this.type2 = "-";


    }


}
