package com.kindminds.drs.api.message


  /**
    * Return - List<SettlementPeriod>
    */
  case class GetSettlementPeriodList() extends Query

  /**
    * Return - SettlementPeriod
    */
  case class GetLatestSettlementPeriod() extends Query

  /**
    * Return - Boolean
    */
  case class IsLatestSettlementPeriodSettled() extends Query