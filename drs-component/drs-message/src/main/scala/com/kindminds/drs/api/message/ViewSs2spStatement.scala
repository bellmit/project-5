package com.kindminds.drs.api.message

/**
  *
  * @param statementName
  * @param countryCode
  *
  * @return ProfitShareSubtractionAdvertisingCostReport
  */
  case class GetSs2spAdvertisingCostReport(statementName : String, countryCode : String) extends Query

/**
  *
  * @param statementName
  * @param countryCode
  *
  * @return ProfitShareSubtractionOtherRefundReport
  */
  case class GetSs2spOtherRefundReport(statementName : String, countryCode : String) extends Query

/**
  *
  * @param statementName
  * @param countryCode
  *
  * @return ProfitShareSubtractionMarketingActivityExpenseReport
  */
case class GetSs2spMarketingActivityExpenseReport(statementName : String, countryCode : String) extends Query



