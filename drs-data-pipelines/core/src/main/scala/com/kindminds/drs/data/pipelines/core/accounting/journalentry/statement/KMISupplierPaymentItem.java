package com.kindminds.drs.data.pipelines.core.accounting.journalentry.statement;



import java.math.BigDecimal;

public class KMISupplierPaymentItem extends StatementItemImpl {

    public KMISupplierPaymentItem(String statementName,
                                  String accountCode, String label, BigDecimal debit,
                                  BigDecimal credit, String companyCode) {

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
