package com.kindminds.drs.data.pipelines.core.accounting.journalentry


case class CurrentBalanceRow(var currentBalance: java.math.BigDecimal)

case class IVSPaymentRefundRow(var reference: String , var sumAmount: java.math.BigDecimal)

case class KMISupplierPaymentRow(var dateSent:String , var sender:String ,
                                 var receiver:String , var amount:java.math.BigDecimal)

case class PreviousBalanceRow(var sequence:Int , var periodStart:String,
                              var periodEnd:String , var previousBalance: java.math.BigDecimal)

case class ProfitCostShareRow(var countryCode:String , var statementAmountUntaxed: java.math.BigDecimal)

case class ProfitCostShareTaxRow(var statementAmount: java.math.BigDecimal )


case class SellBackRow(var statementLineType:String , var sumAmount: java.math.BigDecimal , var ivaName :
String , var sku :String , var salesTaxRate: java.math.BigDecimal)


case class ServiceExpenseRow(var transactionLineType:String , var lineAmount: java.math.BigDecimal )

case class ServiceExpenseTaxRow(var amountTax: java.math.BigDecimal = java.math.BigDecimal.ZERO)

case class AccountCodeInfo(accountId : Int, accountInternalType : String,
                           accountRootId:Int, classify1:String,
                           classify2:String, classify3:String, accountName:String
                          )

case class CompanyCodeStatement(companyCode:String, statementName:String)

case class RemittanceInfo(dateSent:java.util.Date, companyCode:String, amount:java.math.BigDecimal,
                          reference:String, feeAmount:java.math.BigDecimal, feeIncluded:java.lang.Boolean,
                          bankPayment:java.math.BigDecimal, statementName:String)
