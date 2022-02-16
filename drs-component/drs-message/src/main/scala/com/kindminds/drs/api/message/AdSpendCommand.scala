package com.kindminds.drs.api.message


  /**
    * Uses the latest settlement period
    *
    * Return - List<InternationalTransaction>
    */
  case class ProcessAdSpendTransaction(userCompanyKcode : String) extends Command
