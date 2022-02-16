package com.kindminds.drs.data.pipelines.core.actor


import java.text.SimpleDateFormat
import java.time.{ZoneOffset, ZonedDateTime}
import java.util.{Calendar, Date, Locale, TimeZone}

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSelection, Props}
import akka.util.Timeout
import com.kindminds.drs.api.data.access.rdb.ec.AutoProcessECReportDao
import com.kindminds.drs.data.pipelines.api.common.ReportType
import com.kindminds.drs.data.pipelines.api.message._
import com.kindminds.drs.data.pipelines.api.util.DateHelper
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import scala.concurrent.Await
import scala.concurrent.duration._

case class CheckReport()
case class CheckCampaignPerformance()
case class CheckSearchTerm()
case class CheckPageTraffic()
case class CheckBiWeeklyPageTraffic()
case class CheckHSAKeyword()
case class CheckHSACampaign()

case class CheckPaymentsAllStatementsV2()
case class CheckCustomerReturn()
case class CheckManageFBAInventory()
case class CheckInventory()
case class CheckPaymentsDateRange()




object ReportBot {

  def props(springCtx: AnnotationConfigApplicationContext): Props = Props(new ReportBot(springCtx))

}

class ReportBot(springCtx: AnnotationConfigApplicationContext) extends Actor with ActorLogging {

  val dao =   springCtx.getBean(classOf[AutoProcessECReportDao])
    .asInstanceOf[AutoProcessECReportDao]


  implicit val ec = scala.concurrent.ExecutionContext.global

  implicit val resolveTimeout = Timeout(10 seconds)


  val mail: ActorRef = Await.result(context.system.actorSelection("user/mail").resolveOne(),
    resolveTimeout.duration)


  def receive = {

    case cp : CheckCampaignPerformance =>

      checkCampaignPerformance()
    case st : CheckSearchTerm =>
      checkSearchTerm()
    case hk : CheckHSACampaign =>
      checkHSACampaign()
    case hc : CheckHSAKeyword =>
      checkHSAKeyword()
    case pt : CheckPageTraffic =>
      checkSearchTerm()
    case bpt : CheckBiWeeklyPageTraffic =>
      checkBiWeeklyPageTraffic()

    case pv : CheckPaymentsAllStatementsV2 =>
      checkPaymentsAllStatementsV2()
    case pd : CheckPaymentsDateRange =>
      checkPaymentsDateRange()
    case fba : CheckManageFBAInventory =>
      checkManageFBAInventory()
    case i : CheckInventory =>
      checkInventory()
    case c : CheckCustomerReturn =>
      checkCustomerReturn()

    case message: Any =>
      log.info(s"ReportBot: received unexpected: $message")

  }


  def checkCampaignPerformance(): Unit ={

    //need do hbase

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)

    val dcpList  =dao.getProcessResultList(ReportType.DailyCampaignPerformance.id ,
      st , et)

    dcpList.forEach(x=>{

      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)

      if(!x.getImportToDRSDB()){
        mail ! SendReportFailedMail(mp.toString + " " + ReportType.DailyCampaignPerformance.name)
      }else{
        mail ! SendReportOKMail(mp.toString + " " + ReportType.DailyCampaignPerformance.name)
      }

      /*
      if(!x.getImportToHbase()){
          mail ! SendReportFailedMail(mp.toString + " " + ReportType.DailyCampaignPerformance.name)
      }else{
         mail ! SendReportOKMail(mp.toString + " " + ReportType.DailyCampaignPerformance.name)
      }
      */
    })

    val scpList =  dao.getProcessResultList(ReportType.SalesNumCampaignPerformance.id ,
      st , et)

    scpList.forEach(x=>{

     val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)

      if(!x.getImportToDRSDB()){
         mail ! SendReportFailedMail(mp.toString + " " + ReportType.SalesNumCampaignPerformance.name)
      }else{
        mail ! SendReportOKMail(mp.toString + " " + ReportType.SalesNumCampaignPerformance.name)
      }
    })



  }

  def checkSearchTerm(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    val dstList  =dao.getProcessResultList(ReportType.DailySearchTerm.id ,
      st ,et)


    dstList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)

      if(!x.getImportToDRSDB()){
        mail ! SendReportFailedMail(mp.toString + " " +ReportType.DailySearchTerm.name)
      }else{
        mail ! SendReportOKMail(mp.toString + " " +ReportType.DailySearchTerm.name)
      }
    })


    val tstList =  dao.getProcessResultList(ReportType.TotalSearchTerm.id ,
      st , et)

    tstList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){
        mail ! SendReportFailedMail(mp.toString + " " +ReportType.TotalSearchTerm.name)
      }else{
        mail ! SendReportOKMail(mp.toString + " " +ReportType.TotalSearchTerm.name)
      }
    })


  }


  def checkHSAKeyword(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    val dcpList  =dao.getProcessResultList(ReportType.HSAKeyword.id ,
      st , et)

    dcpList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){
               mail ! SendReportFailedMail(mp.toString + " " +ReportType.HSAKeyword.name)
      }else{
               mail ! SendReportOKMail(mp.toString + " " +ReportType.HSAKeyword.name)
      }
    })


  }

  def checkHSACampaign(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    val dcpList  =dao.getProcessResultList(ReportType.HSACampaign.id ,
      st , et)

    dcpList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){
               mail ! SendReportFailedMail(mp.toString + " " +ReportType.HSACampaign.name)
      }else{
               mail ! SendReportOKMail(mp.toString + " " +ReportType.HSACampaign.name)
      }
    })



  }

  def checkBiWeeklyPageTraffic(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    val dcpList  =dao.getProcessResultList(ReportType.BiWeeklyPageTraffic.id ,
      st , et)

    dcpList.forEach(x=>{

      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){
               mail ! SendReportFailedMail(mp.toString + " " +ReportType.BiWeeklyPageTraffic.name)
      }else{
               mail ! SendReportOKMail(mp.toString + " " +ReportType.BiWeeklyPageTraffic.name)
      }
    })


  }

  def checkDailyPageTraffic(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    //todo here how to check
    val dcpList  =dao.getProcessResultList(ReportType.DailyPageTraffic.id ,
      st ,et )


    dcpList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){

               mail ! SendReportFailedMail(mp.toString + " " +ReportType.DailyPageTraffic.name)
      }else{
               mail ! SendReportOKMail(mp.toString + " " +ReportType.DailyPageTraffic.name)
      }
    })





  }

  def checkPaymentsAllStatementsV2(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    //todo here how to check
    val dcpList  =dao.getProcessResultList(ReportType.PaymentsAllStatementsV2.id ,
      st , et)

    dcpList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){
               mail ! SendReportFailedMail(mp.toString + " " +ReportType.PaymentsAllStatementsV2.name)
      }else{
               mail ! SendReportOKMail(mp.toString + " " +ReportType.PaymentsAllStatementsV2.name)
      }
    })



  }

  def checkCustomerReturn(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    val dcpList  =dao.getProcessResultList(ReportType.CustomerReturn.id ,
      st ,et )

    dcpList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){
               mail ! SendReportFailedMail(mp.toString + " " +ReportType.CustomerReturn.name)
      }else{
               mail ! SendReportOKMail(mp.toString + " " +ReportType.CustomerReturn.name)
      }
    })




  }

  def checkManageFBAInventory(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    val dcpList  =dao.getProcessResultList(ReportType.ManageFBAInventory.id ,
      st , et)

    dcpList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){
               mail ! SendReportFailedMail(mp.toString + " " +ReportType.ManageFBAInventory.name)
      }else{
               mail ! SendReportOKMail(mp.toString + " " +ReportType.ManageFBAInventory.name)
      }
    })




  }

  def checkInventory(): Unit ={
    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)

    val dcpList  =dao.getProcessResultList(ReportType.Inventory.id ,
      st ,et )

    dcpList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){
               mail ! SendReportFailedMail(mp.toString + " " +ReportType.Inventory.name)
      }else{
               mail ! SendReportOKMail(mp.toString + " " +ReportType.Inventory.name)
      }
    })


  }

  def checkPaymentsDateRange(): Unit ={

    val today = ZonedDateTime.now(ZoneOffset.UTC)
    val st = today.withHour(0).withMinute(0).withSecond(0).withNano(0)
    val et = today.withHour(23).withMinute(59).withSecond(59).withNano(999)


    val dcpList  =dao.getProcessResultList(ReportType.PaymentsDateRange.id ,
      st , et)


    dcpList.forEach(x=>{
      val mp =  com.kindminds.drs.Marketplace.fromKey(x.getMarketPlaceId)
      if(!x.getImportToDRSDB()){

        mail ! SendReportFailedMail(mp.toString + " " +ReportType.PaymentsDateRange.name)
      }else{
        mail ! SendReportOKMail(mp.toString + " " +ReportType.PaymentsDateRange.name)
      }
    })



  }





  /*
  def checkId(path:String , modificationTime :Long): Boolean ={

    val pAry = path.split("/")
    val fileName = pAry(pAry.length -1)
    val tableName = TableName.valueOf("amazon_report_import_result")
    val table = DAL.conn.getTable(tableName)
    val key =  fileName + "#" + modificationTime
    val hashKey = MD5.hash(key)
    val g = new Get(Bytes.toBytes(hashKey))


    val result = table.get(g)

    if(!result.isEmpty){
      // Reading values from Result class object
      val value = result.getValue(Bytes.toBytes("result"),Bytes.toBytes("status"))
      val status = Bytes.toString(value)
      table.close()
      if(status != "1")
        false
      else
        true
    }else{
      table.close()
      false
    }



  }

  def checkReport(): Unit ={

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val sdf = new SimpleDateFormat("yyyy/MM/dd")
    val today = sdf.parse(sdf.format(calendar.getTime()))

    implicit val resolveTimeout = Timeout(5 seconds)

    val rptTransformer: ActorSelection = context.system.actorSelection("user/reportTransformer")

    doCampaignPerformance(calendar,sdf.parse("2018/07/18") ,rptTransformer)
    doSearchTerm(calendar,sdf.parse("2018/07/18"),rptTransformer)

    //todo need chagne here
    //doPageTraffic(calendar,today,rptTransformer)


  }*/







}
