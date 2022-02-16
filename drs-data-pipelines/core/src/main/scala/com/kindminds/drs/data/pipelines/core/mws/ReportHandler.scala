package com.kindminds.drs.data.pipelines.core.mws

import java.io._
import java.text.SimpleDateFormat
import java.util.Calendar

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.adapter.AmazonMwsReportAdapter
import com.kindminds.drs.{Marketplace, MwsReportType}
import com.kindminds.drs.api.message.command.amazon.report.{DownloadReport, RequestReport}
import com.kindminds.drs.data.pipelines.api.message.{DownloadManageFBAInventoryRpt, ImportAmzDailyInventoryHistoryRpt, ImportAmzInventoryEventRpt, ImportCustomerShipmentSalesRpt, ImportFullManageFBAInventoryRpt, ImportManageFBAInventoryRpt, ImportReceivedInventoryRpt, ImportReimbursementRpt, ImportReplacementsRpt, ImportReservedInventoryRpt, RegisterCommandHandler, WriteToHdfs}
import com.kindminds.drs.data.pipelines.core.BizCoreCtx
import com.kindminds.drs.api.message.query.amazon.report.GetRequestList
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataOutputStream, FileSystem, Path}
import org.apache.hadoop.io.IOUtils

import scala.concurrent.duration._
import com.kindminds.drs.data.pipelines.core.mws
import org.apache.commons.io.FileUtils


object ReportHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new ReportHandler(drsCmdBus))

}

class ReportHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  val springCtx = BizCoreCtx.get

  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestReport].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[DownloadReport].getName ,self)


  val mws = springCtx.getBean(classOf[AmazonMwsReportAdapter]).asInstanceOf[AmazonMwsReportAdapter]
  import context.dispatcher

  def receive = {

    case r: RequestReport =>

      import java.time.ZoneId
      import java.time.ZonedDateTime


     var requestId = ""
      r.reportType match {
        case MwsReportType.FBA_Storage_Fees =>

          val sd = ZonedDateTime.of(2020, 12, 1, 0, 0, 0, 0, ZoneId.of("UTC"))
          val ed = ZonedDateTime.of(2020, 12, 31, 0, 0, 0, 0, ZoneId.of("UTC"))

          println(sd)
          println(ed)
          requestId = mws.requestReport(r.reportType , r.marketplace , sd , ed)

        case MwsReportType.FBA_Manage_Inventory =>

          requestId = mws.requestReport(r.reportType , r.marketplace)

        case MwsReportType.FBA_Customer_Return =>

          val sd = ZonedDateTime.of(2001, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"))
          val ed = ZonedDateTime.of(2020, 8, 18, 0, 0, 0, 0, ZoneId.of("UTC"))

          requestId = mws.requestReport(r.reportType , r.marketplace , sd , ed)


        case MwsReportType.FBA_Inventory_Event_Detail =>

          val ed = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
          //val sd = ZonedDateTime.now().minusMonths(60)
          val sd = ZonedDateTime.now().minusDays(30)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)

          println(sd)
          println(ed)
          requestId = mws.requestReport(r.reportType , r.marketplace, sd, ed)

        case MwsReportType.FBA_Customer_Shipment_Sales =>

          val ed = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
          val sd = ZonedDateTime.now().minusDays(30)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)

          //        val ed = ZonedDateTime.of(2020, 8, 31, 0, 0, 0, 0, ZoneId.of("UTC"))
          println(sd)
          println(ed)
          requestId = mws.requestReport(r.reportType , r.marketplace, sd, ed)


        case MwsReportType.FBA_Daily_Inventory_History =>

          val ed = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
          //val sd = ZonedDateTime.now().minusMonths(18)
          val sd = ZonedDateTime.now().minusDays(30)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)

          println(sd)
          println(ed)
          requestId = mws.requestReport(r.reportType , r.marketplace, sd, ed)

        case MwsReportType.FBA_Received_Inventory =>

          val ed = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
          val sd = ZonedDateTime.now().minusMonths(60)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)

          //        val ed = ZonedDateTime.of(2020, 8, 31, 0, 0, 0, 0, ZoneId.of("UTC"))
          println(sd)
          println(ed)
          requestId = mws.requestReport(r.reportType , r.marketplace, sd, ed)


        case MwsReportType.FBA_Replacements =>

          val ed = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
          val sd = ZonedDateTime.now().minusMonths(120)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)

          requestId = mws.requestReport(r.reportType , r.marketplace, sd, ed)

        case MwsReportType.FBA_Reserved_Inventory =>

          val ed = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
          val sd = ZonedDateTime.now().minusMonths(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
          //val ed = ZonedDateTime.of(2020, 8, 31, 0, 0, 0, 0, ZoneId.of("UTC"))
          println(sd)
          println(ed)
          requestId = mws.requestReport(r.reportType , r.marketplace, sd, ed)

        case MwsReportType.FBA_Reimbursements =>
          val sd = ZonedDateTime.now().minusDays(549)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
          val ed = sd.plusDays(549)

        case MwsReportType.FBA_Inventory_Ledger =>
          val sd = ZonedDateTime.now().minusDays(60)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
          val ed = sd.plusDays(60)

        case _ =>
          println("Nothing")
         // requestReportPreviousWeek(r.reportType, r.marketplace)


      }

      /*
      def requestReportPreviousWeek(reportType: MwsReportType, marketplace: Marketplace) = {
        val sd = ZonedDateTime.now().minusDays(9)
          .withHour(0).withMinute(0).withSecond(0).withNano(0)
        val ed = sd.plusDays(7)
//        val ed = ZonedDateTime.of(2020, 8, 31, 0, 0, 0, 0, ZoneId.of("UTC"))
        println(sd)
        println(ed)
        requestId = mws.requestReport(r.reportType , r.marketplace, sd, ed)
      }
      */


      println(requestId)
      context.system.scheduler.scheduleOnce(1.minute, self , GetRequestList(Some(requestId),r.marketplace, r.reportType))


    case rl: GetRequestList =>
      // 14f50210-6645-4f65-b392-a0d51af4453d
      //use request id to find reprot id

      if(rl.reportType == MwsReportType.Settlement_V2){

        val rptIds = mws.getReportIds( rl.reportType , rl.marketplace)

        //eu will get all marketpakce report
        context.system.scheduler.scheduleOnce(1.minute, self , DownloadReport(rptIds.get(0),rl.marketplace, rl.reportType ))

       // if(rl.marketplace == Marketplace.AMAZON_COM)

        /*
        context.system.scheduler.scheduleOnce(1.minute, self , DownloadReport(rptIds.get(1),rl.marketplace, rl.reportType ))
        context.system.scheduler.scheduleOnce(1.minute, self , DownloadReport(rptIds.get(2),rl.marketplace, rl.reportType ))
        context.system.scheduler.scheduleOnce(1.minute, self , DownloadReport(rptIds.get(3),rl.marketplace, rl.reportType ))
        context.system.scheduler.scheduleOnce(1.minute, self , DownloadReport(rptIds.get(4),rl.marketplace, rl.reportType ))
        */
      }else{

        val rptId = mws.getReportId(rl.requestId.getOrElse(""), rl.reportType , rl.marketplace)
        //val rptId = ""
        //  mws.getReportListByNextToken("U7hb5tvk5Bz2jpZedg0wbVuY6vtoszFEEmr7gIOPIIAEvQvVw7OzPGG2xIoZKAgnChSsxA3AJ74qVtOYt5i3YVBgjvy5fUeBFayuHDbFdAAl1ga0FeA+FuUZjf9me/DQwq99YqfAdSV2G3EsNc6FTMBdNVr6HfrxPxuuVZAjoAG5Hd34Twm1igafEPREmauvQPEfQK/OReIi+vlCGYHCroIZs/Wnb2QWrtBPailCzaVJA92XVbvVV0klzBpD+x31+WLSVHNJuquXCSI6tJ1pqlCk1JLMYDYn1NWWdcMbYZ3b4gIocQyV8CDaO94JXrB4egWSRmo0po56icLAQqF38cWbTIOcQQV/Y636oP43yYfFiG8kmVBembF18LmYQo/GVXpfAhXxIZRxJsS6/Yb0eXbMfK7ExJp3Ar82vKfpVub91gMlcv/5dAP/+IGPVB+bTjx/A3mSHGImXxov5aCtJRdHLQ6B5YXpk+JCX6x+f6QMQorkTSlQnBLO70bpvuot"
        //  ,"",r.marketplace)
        println("GetRequestList REPORTID: " + rptId)

        if(rptId != ""){
          println("DownloadReport")
          context.system.scheduler.scheduleOnce(1.minute, self , DownloadReport(rptId,rl.marketplace, rl.reportType ))
        }else{
          println("GetRequestList" + rl.requestId + rl.marketplace + rl.reportType)
          context.system.scheduler.scheduleOnce(3.minute, self , GetRequestList(rl.requestId,rl.marketplace , rl.reportType))
        }

      }



    case d: DownloadReport =>
      //use report id to download

      val os: OutputStream = mws.getReport( d.reportId , d.marketplace)

      val bos : ByteArrayOutputStream = os.asInstanceOf[ByteArrayOutputStream]

      val data = bos.toByteArray


      d.reportType match {

        case MwsReportType.FBA_Manage_Inventory =>
          val path = manageFBAInventoryPath.get(d.marketplace).get + "/ManageFBAInventory_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)

          //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportFullManageFBAInventoryRpt(path))

        case MwsReportType.FBA_Storage_Fees =>
          val path = monthlyStorageFeePath.get(d.marketplace).get + "/MonthlyStorageFee" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)
          //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportFullManageFBAInventoryRpt(path))

        case MwsReportType.Settlement_V2 =>
          //todo report name
          val path = allStatementsPath.get(d.marketplace).get + "/AllStatements" + d.marketplace.getCountry + "_" + d.reportId + ".txt"
          write(path , data)

        case MwsReportType.FBA_Customer_Return =>
          val path = customerReturnPath.get(d.marketplace).get + "/CustomerReturn_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)
          //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportFullManageFBAInventoryRpt(path))

        case MwsReportType.FBA_Inventory_Event_Detail=>
          val path =
            inventoryEventDetailRptPath.get(d.marketplace).get + "/InventoryEventDetail_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)
          //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportAmzInventoryEventRpt(path))


        case MwsReportType.FBA_Customer_Shipment_Sales=>
          val path =
            customerShipmentSalesRptPath.get(d.marketplace).get + "/CustomerShipmentSales_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)

         //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportCustomerShipmentSalesRpt(path))

        case MwsReportType.FBA_Daily_Inventory_History =>
          val path =
            dailyInventoryRptPath.get(d.marketplace).get + "/DailyInventoryHist_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)
        //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportAmzDailyInventoryHistoryRpt(path))


        case MwsReportType.FBA_Received_Inventory =>
          val path =
            receivedInventoryRptPath.get(d.marketplace).get + "/ReceivedInventory_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)
          //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportReceivedInventoryRpt(path))


        case MwsReportType.FBA_Reserved_Inventory =>
          val path =
            reservedInventoryRptPath.get(d.marketplace).get + "/ReservedInventory_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)
          //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportReservedInventoryRpt(path))


        case MwsReportType.FBA_Replacements =>
          val path =
           replacementsRptPath.get(d.marketplace).get + "/Replacements_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)
          //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportReplacementsRpt(path))

        case MwsReportType.FBA_Reimbursements =>
          val path =
            reimbursementsRptPath.get(d.marketplace).get + "/Reimbursements_" + d.marketplace.getCountry + "_" + today + ".txt"
          write(path , data)
          //context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportReimbursementRpt(path))

//        case MwsReportType.FBA_Inventory_Ledger =>
//          val path ="X"
//          reimbursementsRptPath.get(d.marketplace).get + "/Reimbursements_" + d.marketplace.getCountry + "_" + today + ".txt"
//          write(path , data)
//        context.system.scheduler.scheduleOnce(1.minute, drsCmdBus , ImportReimbursementRpt(path))

        case _ =>
          if (d.reportType.getType.equals("FBA")) {
            val path = countryFbaReportsPath.get(d.marketplace).get + d.reportType.name() +
              "/" + d.reportType.name() + "_" + d.marketplace.getCountry + "_" + today + ".txt"

            write(path , data)

          } else if (d.reportType.getType.equals("Inventory")) {
            val path = countryInventoryReportsPath.get(d.marketplace).get + d.reportType.name() +
              "/" + d.reportType.name() + "_" + d.marketplace.getCountry + "_" + today + ".txt"

            write(path , data)
          }

      }


    //System.out.println( request.getReportOutputStream().toString() );
    //System.out.println();

  }



  def write(path:String ,  data: Array[Byte]) : String = {
//    println("download path: " + path)
//    FileUtils.writeByteArrayToFile(new File(path), data)

   var hconf = new org.apache.hadoop.conf.Configuration()
    hconf.set("mapreduce.app-submission.cross-platform", "true")
    hconf.set("mapreduce.input.fileinputformat.input.dir.recursive", "true")
//    //hconf.set("fs.default.name", "hdfs://devx-virtual-machine:8020")
    hconf.set("fs.default.name", "hdfs://kindminds01:9000")
//
    var hpath: Path = new Path(path)
//    //var hpath: Path = new Path("")
    var fs: FileSystem = FileSystem.get(hconf)
//
    doWrite(fs,hpath,hconf ,data)

/*
    if(fs.exists(hpath)){

        fs.delete(hpath,true)
        doWrite(fs,hpath,hconf)


    }else{
      doWrite(fs,hpath,hconf)
    }
  */

    ""

  }


  def doWrite(fs: FileSystem , hpath : Path , hconf : Configuration , data: Array[Byte] ): Unit ={

    val os: FSDataOutputStream = fs.create(hpath)

    //val in = new BufferedInputStream(bi)

    //log.info("Write to Hdfs file available read size: {}",  in.available())

    val in = new BufferedInputStream(new ByteArrayInputStream(data))
    IOUtils.copyBytes(in , os , hconf, true)

    log.info("Write to Hdfs: {}", hpath)

    fs.getFileStatus(hpath).getModificationTime.toString


  }


  def today(): String = {
    val calendar = Calendar.getInstance
    val sdf = new SimpleDateFormat("yyyyMMdd")
    sdf.format(calendar.getTime)
  }




}
