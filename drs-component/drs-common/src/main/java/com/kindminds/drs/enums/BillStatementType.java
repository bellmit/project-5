package com.kindminds.drs.enums;

 public enum BillStatementType{
    DRAFT("draft_bill_statement", "draft_bill_statementlineitem",
            "draft_bill_statement_profitshare_item", "draft_bill_statementlineitem_customercase_exemption_info"),
    OFFICIAL("bill_statement", "bill_statementlineitem", "bill_statement_profitshare_item", "bill_statementlineitem_customercase_exemption_info");
    private String dbTableStatement;
    private String dbTableStatementLineItem;
    private String dbTableStatementProfitShareItem;
    private String dbTableStatementCustomerCaseExemption;
    BillStatementType(String dbTableStatement, String dbTableStatementLineItem, String dbTableStatementProfitShareItem, String dbTableStatementCustomerCaseExemption) {
        this.dbTableStatement=dbTableStatement;
        this.dbTableStatementLineItem=dbTableStatementLineItem;
        this.dbTableStatementProfitShareItem=dbTableStatementProfitShareItem;
        this.dbTableStatementCustomerCaseExemption=dbTableStatementCustomerCaseExemption;
    }
    public String getDbTableStatement(){ return this.dbTableStatement; }
    public String getDbTableStatementLineItem(){ return this.dbTableStatementLineItem; }
    public String getDbTableStatementProfitShareItem(){ return this.dbTableStatementProfitShareItem; }
    public String getDbTableStatementCustomerCaseExemption(){ return this.dbTableStatementCustomerCaseExemption; }
}