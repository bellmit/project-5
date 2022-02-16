package com.kindminds.drs.api.message.command

package Settlement {

  import com.kindminds.drs.api.message.Command

  case class CollectMarketSideTransactions(settlementPeriodId: String , kcode:String,kcodeIndex:Int)  extends Command

  case class StartCollectingMarketSideTransactions(settlementPeriodId: String )  extends Command

  case class CompleteCollectingSupplierMarketSideTransactions(settlementPeriodId: String  ,
                                                              kcode:String,kcodeIndex:Int , result:Int)  extends Command

  case class ProcessMarketSideTransactions(settlementPeriodId: String , kcode:String,kcodeIndex:Int)  extends Command

  case class ProcessMarketSideTransactionById(settlementPeriodId: String , mstId:Int)  extends Command

  case class StartProcessingMarketSideTransactions(settlementPeriodId: String ) extends Command

  case class CompleteProcessingSupplierMarketSideTransactions(settlementPeriodId: String  ,
                                                              kcode:String,kcodeIndex:Int ,result:Int)  extends Command




}


