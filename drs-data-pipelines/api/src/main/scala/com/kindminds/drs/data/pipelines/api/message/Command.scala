package com.kindminds.drs.data.pipelines.api.message

import java.time.ZonedDateTime

import akka.actor.ActorRef
import org.joda.time.LocalDateTime

sealed  trait Command

/*
case class ParseAmazonDateRangeRpt( marketPlaceId:Int , filePath : String) extends Command

case class SaveAmazonDateRangeRptItem(item : AmazonDateRangeReportItem) extends Command
case class GetParsingAmazonDateRangeRptState(workId:String) extends Command
case class GetParsingAmazonDateRangeRptResult() extends Command
*/

case class CommandHandler(name: String, handler: ActorRef)
case class QueryHandler(name: String, handler: ActorRef)

case class RegisterCommandHandler(name: String, commandId: String, handler: ActorRef)
case class RegisterQueryHandler(name: String, queryId: String, handler: ActorRef)



sealed trait DownloadReport extends Command


case class  DownloadBizReport() extends DownloadReport

case class DownloadNAReport(
                             usRptID :( Seq[Option[String]], Seq[Option[String]],Option[String] , Option[String]),
                             caRptId :( Seq[Option[String]],Seq[Option[String]],Option[String] , Option[String])
                             , scheduledDate : Option[String] )
  extends DownloadReport

case class DownloadNAPageTrafficReport(date : Option[String] = None) extends DownloadReport

case class DownloadECMPageTrafficReport(kcode:String , date : Option[String] = None) extends DownloadReport

case class DownloadECMReport(kcode:String,
                             rptIds :(Seq[Option[String]], Seq[Option[String]]), scheduledDate : Option[String] )
  extends DownloadReport

case class DownloadEUReport(
                             ukRptID : (Seq[Option[String]],Seq[Option[String]] ,Option[String] ,Option[String]),
                             deRptId : (Seq[Option[String]],Seq[Option[String]],Option[String] , Option[String]),
                             frRptID : (Seq[Option[String]],Seq[Option[String]],Option[String] , Option[String]),
                             itRptId :(Seq[Option[String]],Seq[Option[String]],Option[String] , Option[String]),
                             esRptId :(Seq[Option[String]],Seq[Option[String]],Option[String] ,Option[String])
                             , scheduledDate : Option[String]  )
  extends DownloadReport

case class DownloadEUPageTrafficReport() extends DownloadReport


case class DownloadNAAdvMonthlyReport(usRptID :(Option[String], Option[String]),
                            caRptId :(Option[String],Option[String] )) extends DownloadReport


case class DownloadEUAdvMonthlyReport(ukRptID : (Option[String],Option[String]) ,
                                      deRptId : (Option[String],Option[String]) ,
                            frRptID :(Option[String],Option[String]),
                                      itRptId :(Option[String],Option[String]) ,
                            esRptId :(Option[String],Option[String]) )  extends DownloadReport



sealed trait RequestReport extends Command{
  def scheduledDate : Option[String]
}

case class RequestAdvReport(scheduledDate : Option[String] = None) extends  RequestReport

case class RequestECMAdvReport(kcode:String , scheduledDate : Option[String] = None) extends  RequestReport

case class RequestAdvMonthlyReport(scheduledDate : Option[String] = None) extends  RequestReport
case class RequestInventoryReport(scheduledDate : Option[String] = None) extends  RequestReport



case class RequestCampaignPerformanceRpt(marketPlace:String, scheduledDate : Option[String]  ) extends  RequestReport
case class RequestDailyCampaignPerformanceRpt(marketPlace:String, scheduledDate : Option[String] ) extends  RequestReport
case class RequestSalesNumCampaignPerformanceRpt(marketPlace:String, scheduledDate : Option[String] ) extends  RequestReport

case class RequestSearchTermRpt(marketPlace:String, scheduledDate : Option[String] ) extends  RequestReport
case class RequestDailySearchTermRpt(marketPlace:String, scheduledDate : Option[String] ) extends  RequestReport
case class RequestTotalSearchTermRpt(marketPlace:String, scheduledDate : Option[String] ) extends  RequestReport

case class RequestSPKeywordRpt(marketPlace:String, scheduledDate : Option[String] = None) extends  RequestReport
case class RequestPurchasedProductRpt(marketPlace:String, scheduledDate : Option[String] = None) extends  RequestReport

case class RequestHSAKeywordRpt(marketPlace:String, scheduledDate : Option[String]) extends  RequestReport
case class RequestHSACampaignRpt(marketPlace:String, scheduledDate : Option[String]) extends  RequestReport


case class RequestMonthlyStorageFeesRpt(marketPlace:Option[String], scheduledDate : Option[String] = None) extends  RequestReport
case class RequestLongTermStorageFeeChargesRpt(marketPlace:Option[String], scheduledDate : Option[String] = None) extends  RequestReport
case class RequestCustomerReturnRpt(marketPlace:Option[String], scheduledDate : Option[String] = None) extends  RequestReport

case class RequestManageFBAInventoryRpt(marketPlace:Option[String], scheduledDate : Option[String] = None) extends  RequestReport
case class RequestInventoryRpt(marketPlace:Option[String], scheduledDate : Option[String] = None) extends  RequestReport
case class RequestPaymentsDateRangeRpt(marketPlace:Option[String], scheduledDate : Option[String] = None) extends  RequestReport


case class DownloadCampaignPerformanceRpt(marketPlace:String,rptIds:Seq[Option[String]] , date : Option[String] ) extends  DownloadReport
case class DownloadDailyCampaignPerformanceRpt(marketPlace:String,rptId:String , date : Option[String] ) extends  DownloadReport
case class DownloadSalesNumCampaignPerformanceRpt(marketPlace:String,rptId:String , date : Option[String] ) extends  DownloadReport


case class DownloadSearchTermRpt(marketPlace:String,rptIds:Seq[Option[String]] , date : Option[String] ) extends  DownloadReport
case class DownloadDailySearchTermRpt(marketPlace:String,rptId:String , date : Option[String] ) extends  DownloadReport
case class DownloadTotalSearchTermRpt(marketPlace:String,rptId:String , date : Option[String] ) extends  DownloadReport

case class DownloadPurchasedProductRpt(marketPlace:String,rptId:String) extends  DownloadReport
case class DownloadSPKeywordRpt(marketPlace:String,rptId:String) extends  DownloadReport

case class DownloadHSAKeywordRpt(marketPlace:String,rptId:String, date : Option[String] ) extends  DownloadReport
case class DownloadHSACampaignRpt(marketPlace:String,rptId:String, date : Option[String] ) extends  DownloadReport


case class DownloadMonthlyStorageFeesRpt(marketPlace:Option[String]) extends  DownloadReport
case class DownloadLongTermStorageFeeChargesRpt(marketPlace:Option[String]) extends  DownloadReport
case class DownloadPaymentsAllStatementsV2Rpt(marketPlace:Option[String]) extends  DownloadReport
case class DownloadCustmoerReturnRpt(marketPlace:Option[String]) extends  DownloadReport

case class DownloadPageTrafficRpt(marketPlace:Option[String] , Date : Option[Seq[String]] ) extends  DownloadReport
case class DownloadDailyPageTrafficRpt(marketPlace:Option[String] , date : String) extends  DownloadReport
case class DownloadBiWeekPageTrafficRpt(marketPlace:Option[String],biWeekDate : Tuple2[String,String]) extends  DownloadReport

case class DownloadManageFBAInventoryRpt(marketPlace:Option[String] , rptId: Option[String]) extends  DownloadReport
case class DownloadInventoryRpt(marketPlace:Option[String],rptId:String) extends  DownloadReport

case class DownloadPaymentsDateRangeRpt(marketPlace:Option[String]) extends  DownloadReport


case class WriteToHdfs(marketPlace:String, scheduledDate: java.time.LocalDate, from :String, to: String)

case class RemoveFile(location:String)

case class SendScrapeAmzRptFailedMail(marketPlace:Option[String] , message:String , stackTrace: String)
case class SendReportOKMail(report:String)
case class SendReportFailedMail(report:String)


sealed trait ImportReportToDrs{
  def filePath:String
  def marketPlace:String
  def modificationTime: Option[String]
}

case class ImportCampaignPerformanceRpt(scheduledDate:java.time.LocalDate,
                                         val filePath:String , val marketPlace:String ,val modificationTime: Option[String] =None) extends  ImportReportToDrs
case class ImportSearchTermRpt(scheduledDate:java.time.LocalDate,
                                val filePath:String ,val marketPlace:String ,val modificationTime: Option[String] =None) extends  ImportReportToDrs
case class ImportTrafficRpt(scheduledDate:java.time.LocalDate,
                             val filePath:String ,val marketPlace:String ,val modificationTime: Option[String] =None) extends  ImportReportToDrs
case class ImportBiWeeklyTrafficRpt(scheduledDate:java.time.LocalDate,
                                     val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs
case class ImportAllStatementsRpt(scheduledDate:java.time.LocalDate,
                                   val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs
case class ImportCustomerReturnRpt(scheduledDate:java.time.LocalDate,
                                    val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs

case class ImportMonthlyStorageFeeRpt(scheduledDate:java.time.LocalDate,
                                       val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs

case class ImportManageFBAInventoryRpt(scheduledDate:java.time.LocalDate,
                                        val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs
case class ImportInventoryRpt(scheduledDate:java.time.LocalDate,
                               val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs
case class ImportPaymentsDateRangeRpt(scheduledDate:java.time.LocalDate,
                                       val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs

case class ImportHSACampaignRpt(scheduledDate:java.time.LocalDate,
                                 val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs
case class ImportHSAKeywordRpt(scheduledDate:java.time.LocalDate,
                                val filePath:String ,val marketPlace:String , val modificationTime: Option[String] =None) extends  ImportReportToDrs

case class CalcMonthlyStorageFeeRpt(val filePath:String)

case class ImportFullManageFBAInventoryRpt(val path:String) extends Command



sealed trait TransformReport{
  def filePath: Option[String]
  def modificationTime: Option[String]

}


case class TransformCampaignPerformanceRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                            val filePath: Option[String] =None ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformSearchTermRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                   val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformSearchTermRptRawData(marketPlace:String , scheduledDate:java.time.LocalDate,
                                          val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformTrafficRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                val filePath: Option[String] =None ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformBiWeeklyTrafficRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                        val filePath: Option[String] =None  ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformAllStatementsRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                      val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformCustomerReturnRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                       val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport

case class TransformMonthlyStorageFeeRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                          val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport

case class TransformManageFBAInventoryRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                           val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformInventoryRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                  val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformPaymentsDateRangeRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                          val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport

case class TransformHSACampaignRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                    val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport
case class TransformHSAKeywordRpt(marketPlace:String , scheduledDate:java.time.LocalDate,
                                   val filePath: Option[String] = None ,val modificationTime: Option[String] =None) extends  TransformReport





sealed trait Refresh extends Command

case class StartRefreshAllOrders() extends Refresh

case class StartRefreshMissingOrders() extends Refresh

case class StartRefreshOrdersByDate(startTime : ZonedDateTime) extends Refresh

case class RefreshAllOrders(allOrderIds : List[Int], startTime : ZonedDateTime, endTime : ZonedDateTime) extends Refresh

case class RefreshMissingAllOrders(missingMarketPlaceOrderIds : List[String]) extends Refresh

case class RefreshESAllOrders(allOrderIds : List[Int] ,  startTime : ZonedDateTime , endTime : ZonedDateTime) extends  Refresh

case class RefreshMissingESAllOrders(missingMarketPlaceOrderIds : List[String]  ) extends  Refresh


case class StartRefreshNotUpdatedOrders() extends Refresh




case class RefreshESAllOrdersTransaction() extends  Refresh

case class RefreshOrder(marketPlaceOrderId : String ) extends Refresh

case class RefreshESOrder(marketPlaceOrderId :String , sourceIds:Option[List[Int]] ) extends  Refresh





case class UpdateAllOrdersLocalOrderTime(allOrderIds : List[Int] , lastUpdateDate : String) extends  Refresh

case class StartRefreshDailySales() extends  Refresh

case class CompleteRefreshDailySales() extends  Refresh

case class RefreshDailySales() extends  Refresh

case class RefreshDailySalesExclRefundOrders() extends  Refresh

case class RefreshMissingDailySales(startTime:java.time.LocalDateTime , endTime:java.time.LocalDateTime) extends  Refresh


case class ProcessLocalOrderTime() extends  Refresh

case class ProcessEBayOrderSalesChannel() extends  Refresh

case class ProcessDailySales(kcode : String) extends  Refresh

case class CreateDailySales(kcode : String) extends  Refresh

case class CreateDailyRetainment(kcode : String) extends  Refresh

case class StartRefreshDailyRetainment() extends  Refresh

case class CompleteRefreshDailyRetainment() extends  Refresh


case class StartInitSkuSerialNo()  extends Refresh
case class CompleteInitKcodeSkuSerialNo()  extends Refresh

case class TransformSkuSerialNo(kcode:String) extends Refresh
case class UpdateSkuSerialNo(kcode:String) extends Refresh
case class DoSkuSerialNoRefund(kcode:String) extends Refresh
case class DoSkuSerialNoSellBack(kcode:String) extends Refresh
case class DoSkuSerialNoException(kcode:String) extends Refresh
case class DoSkuSerialNoOther(kcode:String) extends Refresh

case class RefreshSkuStatistics() extends Refresh
case class RefreshSkuRetailPrice() extends Refresh

case class ImportCustomerShipmentSalesRpt(path:String) extends Command
case class ImportAmzDailyInventoryHistoryRpt(path:String) extends Command
case class ImportAmzInventoryEventRpt(path:String) extends Command
case class ImportReceivedInventoryRpt(path:String) extends Command
case class ImportReplacementsRpt(path:String) extends Command
case class ImportReservedInventoryRpt(path:String) extends Command
case class ImportReimbursementRpt(path:String) extends Command
case class ImportInventoryLedgerRpt(path:String) extends Command
case class ImportAllOrderRpt(path:String) extends Command
case class ImportRemovalShipmentRpt(path:String) extends Command
case class ImportRemovalOrderRpt(path:String) extends Command
