package com.kindminds.drs.data.pipelines.core.accounting.journalentry.remittance;


import java.math.BigDecimal;

public class BankSaving extends RemittanceItemImpl {

//        -  Account code : '110301SAV'
//        -  Label as Receiver company's K code +' ' + Date_sent + ' '+ 'payment from KMI'
//        -  Debit: 0
//        -  Credit : if remittance fee included ='Yes',  Credit = Amount + Remittance Fee, otherwise Credit = Amount
//            -  Partner = Receiver company's K code
//        -  Market = '-'
//        -  Department = '-'
//        -  type_1 = '-'
//        -  type_2 = 'DRS'
    public BankSaving(String statementName, String label,
                      BigDecimal credit, String companyCode) {

        this.statementName = statementName;
        this.accountCode = "110301SAV";
        this.label = label;
        this.debit = BigDecimal.ZERO;
        this.credit = credit;
        this.companyCode = companyCode;
        this.partner = "";
        this.market = "-";
        this.department = "-";
        this.type1 = "-";
        this.type2 = "DRS";


    }


}
