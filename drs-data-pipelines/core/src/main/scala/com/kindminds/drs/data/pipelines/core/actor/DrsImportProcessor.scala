package com.kindminds.drs.data.pipelines.core.actor

import java.text.SimpleDateFormat
import java.util
import java.util.Calendar

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.Timeout
import com.kindminds.drs.api.usecase.AddProductSkuAsinUco
import com.kindminds.drs.api.usecase.report.amazon.{ImportAmazonDateRangeReportUco, ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco, ImportAmazonDetailPageSalesTrafficByChildItemReportUco, ImportAmazonHeadlineSearchAdReportUco, ImportAmazonMonthlyStorageFeeReportUco, ImportAmazonReturnReportUco, ImportAmazonSettlementReportUco, ImportAmazonSponsoredProductsAdvertisedProductReportUco, ImportAmazonSponsoredProductsSearchTermReportUco}
import com.kindminds.drs.api.usecase.settlement.AddSettlementPeriodUco
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod
import com.kindminds.drs.data.pipelines.api.common.ReportType
import com.kindminds.drs.data.pipelines.api.message._
import com.kindminds.drs.data.pipelines.api.util.Amazon
import com.kindminds.drs.data.pipelines.core.DrsMarketingEventBus
import com.kindminds.drs.data.pipelines.core.util.HdfsHelper
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import scala.concurrent.Await
import scala.concurrent.duration._


object DrsImportProcessor {

  def props(eventBus: DrsMarketingEventBus ,springCtx:AnnotationConfigApplicationContext): Props =
    Props(new DrsImportProcessor(eventBus ,springCtx))

}


class DrsImportProcessor(eventBus: DrsMarketingEventBus , springCtx:AnnotationConfigApplicationContext) extends Actor with ActorLogging {

  //val springCtx = com.kindminds.drs.service.util.SpringAppCtx.get()


  /*
  implicit val resolveTimeout = Timeout(5 seconds)

  val drsProcessor: ActorRef = Await.result(context.system.actorSelection("user/drsProcessor").resolveOne(),
    resolveTimeout.duration)
    */


  def publishEvent(marketPlace:Int , scheduledDate:java.time.LocalDate , rptType : ReportType.Value): Unit ={

    eventBus.publish(ReportImportedToDRSDB(marketPlace, scheduledDate, rptType))

  }

  def publishFailedEvent(marketPlace:Int , scheduledDate:java.time.LocalDate ,rptType : ReportType.Value): Unit ={

    eventBus.publish(ReportImportToDRSDBFailed(marketPlace, scheduledDate, rptType))

  }

  def receive = {
    case s:String =>
      println("test")
    case cp : ImportCampaignPerformanceRpt =>
      //todo need independent actor
      processCampaignPerformance(cp)

    case st : ImportSearchTermRpt =>
      processSearchTerm(st)
    case t : ImportTrafficRpt =>
      processTraffic(t)
    case bt : ImportBiWeeklyTrafficRpt =>
      processBiWeeklyTraffic(bt)
    case as : ImportAllStatementsRpt =>
      processAllStatements(as)
    case cr : ImportCustomerReturnRpt =>
      processCustomerReturn(cr)
    case msf : ImportMonthlyStorageFeeRpt =>
      processMonthlyStorageFee(msf)
    case mfi : ImportManageFBAInventoryRpt =>
      processManageFBAInventoryRpt(mfi)
    case ir : ImportInventoryRpt =>
      processInventoryRpt(ir)
    case pd : ImportPaymentsDateRangeRpt =>
      processPaymentsDateRangeRpt(pd)
    case hc : ImportHSACampaignRpt =>
      processHSACampaignRpt(hc)
    case hk : ImportHSAKeywordRpt =>
      processHSAKeywordRpt(hk)
  }


  def processCampaignPerformance(cp : ImportCampaignPerformanceRpt): Unit =
  {


    val mkId = Amazon.drsMarketPlaceId(cp.marketPlace)

    try{

      val cpUco = springCtx.getBean(classOf[ImportAmazonSponsoredProductsAdvertisedProductReportUco])
        .asInstanceOf[ImportAmazonSponsoredProductsAdvertisedProductReportUco]

      val arr: Array[Byte] = HdfsHelper.getFile(cp.filePath)

      cpUco.importFile(mkId , arr)

      if(cp.filePath.contains("Sales")){
        publishEvent(mkId.toInt , cp.scheduledDate , ReportType.SalesNumCampaignPerformance)
      }else{
        publishEvent(mkId.toInt , cp.scheduledDate , ReportType.DailyCampaignPerformance)
      }

      log.info("{} Campaign performance report import to drs is done " , cp.marketPlace)

    }
    catch {
    case  e : Exception => {
      //e.printStackTrace()

      log.error(e.getMessage())

      if(cp.filePath.contains("Sales")){
        publishFailedEvent(mkId.toInt , cp.scheduledDate , ReportType.SalesNumCampaignPerformance)
      }else{
        publishFailedEvent(mkId.toInt , cp.scheduledDate , ReportType.DailyCampaignPerformance)
      }

      //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")

      //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
    }
  }




  }


  def processSearchTerm(st : ImportSearchTermRpt): Unit =
  {
    val mkId = Amazon.drsMarketPlaceId(st.marketPlace)

    try{

      val stUco = springCtx.getBean(classOf[ImportAmazonSponsoredProductsSearchTermReportUco])
        .asInstanceOf[ImportAmazonSponsoredProductsSearchTermReportUco]

      println(st.filePath)
      val arr = HdfsHelper.getFile(st.filePath)

      stUco.importFile(mkId , arr)

      publishEvent(mkId.toInt,st.scheduledDate,ReportType.TotalSearchTerm)

      println("AAAA")

      log.info("{} Search term report import to drs is done " , st.marketPlace)

    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        log.error(e.getMessage())
        publishFailedEvent(mkId.toInt,st.scheduledDate,ReportType.TotalSearchTerm)

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")

        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }

  def processTraffic(t : ImportTrafficRpt) : Unit =
  {
    try{

      val tUco = springCtx.getBean(classOf[ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco])
        .asInstanceOf[ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco]

      val arr = HdfsHelper.getFile(t.filePath)

      println(t.filePath)
      println(arr.length)

      val pAry = t.filePath.split("/")

      val fileNameAry = pAry(pAry.length -1).split(".csv")
      val rptDateAry = fileNameAry(0).split("_")
      val rptDate = rptDateAry(rptDateAry.length -1)

      val sdf = new SimpleDateFormat("yyyyMMdd")
      val sdf2 = new SimpleDateFormat("yyyy-MM-dd")
      val importDate =sdf2.format(sdf.parse(rptDate))

      //ImportHelper.process(pAry(pAry.length-1),t.modificationTime.getOrElse(""))

      val mId  = Amazon.drsMarketPlaceId(t.marketPlace)

      tUco.delete(importDate,mId)

      tUco.importFile( mId, importDate,  arr)

      //ImportHelper.complete(pAry(pAry.length-1),t.modificationTime.getOrElse(""))

      publishEvent(Amazon.drsMarketPlaceId(t.marketPlace).toInt , t.scheduledDate ,
        ReportType.DailyPageTraffic)

      println("123")
      log.info("{} daily traffic report import to drs is done " , t.marketPlace)

    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        log.error(e.getMessage())

        publishFailedEvent(Amazon.drsMarketPlaceId(t.marketPlace).toInt , t.scheduledDate ,
          ReportType.DailyPageTraffic)

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")

        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }

  def processBiWeeklyTraffic(bt : ImportBiWeeklyTrafficRpt): Unit =
  {
    try{

      log.info("{} Start import BiWeeklyTraffic report  to drs  " , bt.marketPlace)

      val btUco = springCtx.getBean(classOf[ImportAmazonDetailPageSalesTrafficByChildItemReportUco])
        .asInstanceOf[ImportAmazonDetailPageSalesTrafficByChildItemReportUco]

      val arr = HdfsHelper.getFile(bt.filePath)

      import scala.collection.JavaConverters._

      val peroidList: util.List[SettlementPeriod] =
        btUco.getUnOccupiedSettlementPeriods(Amazon.drsMarketPlaceId(bt.marketPlace))

      if(peroidList.size()>0){

        println("AAAA")

        val c = Calendar.getInstance
        val lastPeriodEnd = peroidList.get(0).getEndPoint
        val now = c.getTime
        c.setTime(lastPeriodEnd)
        c.add(Calendar.DATE, 14)
        val nextPeriodEnd = c.getTime

        println(nextPeriodEnd)

      //  if(now.after(nextPeriodEnd)||now.compareTo(nextPeriodEnd)==0){

          val aUco = springCtx.getBean(classOf[AddSettlementPeriodUco])
            .asInstanceOf[AddSettlementPeriodUco]

       //  aUco.addPeriod()

          val newPeroidList: util.List[SettlementPeriod] =
              btUco.getUnOccupiedSettlementPeriods(Amazon.drsMarketPlaceId(bt.marketPlace))

        println(newPeroidList.get(0).getStartDate)

         btUco.importFile(Amazon.drsMarketPlaceId(bt.marketPlace), newPeroidList.get(0).getId , arr)

         // publishEvent(Amazon.drsMarketPlaceId(bt.marketPlace).toInt , bt.scheduledDate , ReportType.BiWeeklyPageTraffic)

          log.info("{} BiWeeklyTraffic report import to drs is done " , bt.marketPlace)


        //}

      }


    }
    catch {
      case  e : Exception => {

        log.error("{} BiWeeklyTraffic import failed {}" , bt.marketPlace, e.getMessage)
        log.error(e.getMessage())

        publishEvent(Amazon.drsMarketPlaceId(bt.marketPlace).toInt , bt.scheduledDate , ReportType.BiWeeklyPageTraffic)

      }
    }
  }

  def processAllStatements(as : ImportAllStatementsRpt): Unit =
  {
    try{

      val uco = springCtx.getBean(classOf[ImportAmazonSettlementReportUco])
        .asInstanceOf[ImportAmazonSettlementReportUco]

      println(as.filePath)
      val arr = HdfsHelper.getFile(as.filePath)


      uco.uploadFileAndImport(Amazon.drsMarketPlaceId(as.marketPlace) , arr)

      publishEvent(Amazon.drsMarketPlaceId(as.marketPlace).toInt , as.scheduledDate , ReportType.PaymentsAllStatementsV2)

      log.info("{} All statements report import to drs is done " , as.marketPlace)

    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        log.error(e.getMessage())
        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")

        publishFailedEvent(Amazon.drsMarketPlaceId(as.marketPlace).toInt , as.scheduledDate , ReportType.PaymentsAllStatementsV2)

        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }

  def processCustomerReturn(cr : ImportCustomerReturnRpt): Unit =
  {
    try{

      val uco = springCtx.getBean(classOf[ImportAmazonReturnReportUco])
        .asInstanceOf[ImportAmazonReturnReportUco]

      val arr = HdfsHelper.getFile(cr.filePath)

      uco.importFile(arr)

      publishEvent(Amazon.drsMarketPlaceId(cr.marketPlace).toInt , cr.scheduledDate , ReportType.CustomerReturn)

      log.info("{} Customer Return report import to drs is done " , cr.marketPlace)

    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        log.error(e.getMessage())

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")

        publishFailedEvent(Amazon.drsMarketPlaceId(cr.marketPlace).toInt , cr.scheduledDate , ReportType.CustomerReturn)

        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }

  def processMonthlyStorageFee(msf : ImportMonthlyStorageFeeRpt): Unit =
  {
    try{

      import context.dispatcher
      implicit val resolveTimeout = Timeout(5 seconds)


      val uco = springCtx.getBean(classOf[ImportAmazonMonthlyStorageFeeReportUco])
        .asInstanceOf[ImportAmazonMonthlyStorageFeeReportUco]

      println(msf.filePath)
      val arr = HdfsHelper.getFile(msf.filePath)
      println(arr.length)

      uco.importFile(Amazon.drsWarehouseId(msf.marketPlace), arr)


      val drsProcessor: ActorRef = Await.result(context.system.actorSelection("user/drsProcessor").resolveOne(),
        resolveTimeout.duration)

      if(msf.marketPlace == "us"){
        context.system.scheduler.scheduleOnce(30.minute, drsProcessor,  CalcMonthlyStorageFeeRpt(msf.filePath))
      }


      //drsProcessor ! CalcMonthlyStorageFeeRpt(msf.filePath)

      log.info("{} Monthly Storage Fee report import to drs is done " , msf.marketPlace)


    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()
        log.error(e.getMessage())

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")

        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }

  def processManageFBAInventoryRpt(mfi : ImportManageFBAInventoryRpt): Unit =
  {
    try{

      val uco = springCtx.getBean(classOf[AddProductSkuAsinUco])
        .asInstanceOf[AddProductSkuAsinUco]

      val arr = HdfsHelper.getFile(mfi.filePath)

      uco.addFbaData(arr , Amazon.drsMarketPlaceId.get(mfi.marketPlace).get.toInt)

      publishEvent(Amazon.drsMarketPlaceId(mfi.marketPlace).toInt , mfi.scheduledDate , ReportType.ManageFBAInventory)

      log.info("{} Manage FBA Inventory report import to drs is done " , mfi.marketPlace)

    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        log.error(e.getMessage())

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")

        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))

        publishFailedEvent(Amazon.drsMarketPlaceId(mfi.marketPlace).toInt , mfi.scheduledDate , ReportType.ManageFBAInventory)
      }
    }
  }

  def processInventoryRpt(ir : ImportInventoryRpt): Unit =
  {
    try{

      val uco = springCtx.getBean(classOf[AddProductSkuAsinUco])
        .asInstanceOf[AddProductSkuAsinUco]

      val arr = HdfsHelper.getFile(ir.filePath)

      uco.addInventoryData(arr , Amazon.drsMarketPlaceId.get(ir.marketPlace).get.toInt)

      publishEvent(Amazon.drsMarketPlaceId(ir.marketPlace).toInt , ir.scheduledDate , ReportType.Inventory)

      log.info("{} Inventory report import to drs is done " , ir.marketPlace)

    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        log.error(e.getMessage())
        publishFailedEvent(Amazon.drsMarketPlaceId(ir.marketPlace).toInt , ir.scheduledDate , ReportType.Inventory)
        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")
        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }

  def processPaymentsDateRangeRpt(pd : ImportPaymentsDateRangeRpt): Unit =
  {
    try{

      log.info("{} Payments Date Range report import to drs is starting " , pd.marketPlace)



      val uco = springCtx.getBean(classOf[ImportAmazonDateRangeReportUco])
        .asInstanceOf[ImportAmazonDateRangeReportUco]

      val arr = HdfsHelper.getFile(pd.filePath)

      uco.importReport(Amazon.drsMarketPlaceId.get(pd.marketPlace).get.toInt , arr)

      publishEvent(Amazon.drsMarketPlaceId(pd.marketPlace).toInt , pd.scheduledDate , ReportType.PaymentsDateRange)

      log.info("{} Payments Date Range report import to drs is done " , pd.marketPlace)

    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        log.error(e.getMessage())

        publishFailedEvent(Amazon.drsMarketPlaceId(pd.marketPlace).toInt , pd.scheduledDate , ReportType.PaymentsDateRange)

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")
        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }

  def processHSACampaignRpt(hc : ImportHSACampaignRpt): Unit =
  {
    try{

      log.info("{} HSA Campaign report import to drs is starting " , hc.marketPlace)

      val uco = springCtx.getBean(classOf[ImportAmazonHeadlineSearchAdReportUco])
        .asInstanceOf[ImportAmazonHeadlineSearchAdReportUco]

      val arr = HdfsHelper.getFile(hc.filePath)


      uco.importReport(Amazon.drsMarketPlaceId.get(hc.marketPlace).get.toInt , arr, "campaign")

      publishEvent(Amazon.drsMarketPlaceId(hc.marketPlace).toInt,hc.scheduledDate,ReportType.HSACampaign)

      log.info("{} HSA Campaign report import to drs is done " , hc.marketPlace)

    }
    catch {
      case  e : Exception => {
        e.printStackTrace()

        log.error(e.getMessage())
        publishFailedEvent(Amazon.drsMarketPlaceId(hc.marketPlace).toInt,hc.scheduledDate,ReportType.HSACampaign)

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")
        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }

  def processHSAKeywordRpt(hk : ImportHSAKeywordRpt): Unit =
  {
    try{

      log.info("{} HSA Keyword report import to drs is starting " , hk.marketPlace)

      val uco = springCtx.getBean(classOf[ImportAmazonHeadlineSearchAdReportUco])
        .asInstanceOf[ImportAmazonHeadlineSearchAdReportUco]

      val arr = HdfsHelper.getFile(hk.filePath)

      uco.importReport(Amazon.drsMarketPlaceId.get(hk.marketPlace).get.toInt , arr, "keyword")
      publishEvent(Amazon.drsMarketPlaceId(hk.marketPlace).toInt,hk.scheduledDate,ReportType.HSAKeyword)

      log.info("{} HSA Keyword report import to drs is done " , hk.marketPlace)

    }
    catch {
      case  e : Exception => {
        //e.printStackTrace()

        log.error(e.getMessage())
        publishFailedEvent(Amazon.drsMarketPlaceId(hk.marketPlace).toInt,hk.scheduledDate,ReportType.HSAKeyword)

        //mail ! SendScrapeAmzRptFailedMail(amazonScraper.marketPlace + " Payments All Statements V2 failed")
        //context.system.scheduler.scheduleOnce(5.minute, self,  DownloadSearchTermRpt(marketPlace,rptId))
      }
    }
  }




}


