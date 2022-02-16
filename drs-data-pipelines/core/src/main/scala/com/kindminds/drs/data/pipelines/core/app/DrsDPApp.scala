package com.kindminds.drs.data.pipelines.core.app



import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId, ZoneOffset, ZonedDateTime}
import akka.actor.{ActorSystem, Props}
import akka.stream.{ActorMaterializer, Materializer}
import com.kindminds.drs.{Marketplace, MwsReportType}
import com.kindminds.drs.api.message.command.amazon.report.{DownloadReport, RequestReport}
import com.kindminds.drs.api.message.command.{DeleteESCustomercareCase, RefreshESCustomercareCase, RequestBiweeklyStatementJournalEntries, RequestRemittanceJournalEntries}
import com.kindminds.drs.api.message.query.amazon.report.GetRequestList
import com.kindminds.drs.data.pipelines.api.message._
import com.kindminds.drs.data.pipelines.core.DrsDPCmdBus
import com.kindminds.drs.data.pipelines.core.accounting.RequestJournalEntriesHandler
import com.kindminds.drs.data.pipelines.core.es.{CCCaseES, CCCaseIssueES, OrderES}
import com.kindminds.drs.data.pipelines.core.inventory.ImportManageFbaInventoryRptHandler
import com.kindminds.drs.data.pipelines.core.inventory.etl.{AmzCustomerShipmentSalesHandler, AmzDailyInventoryHistoryHandler, AmzInventoryEventHandler, AmzInventoryLedgerHandler, AmzReceivedInventoryHandler, AmzReplacementsHandler, AmzReservedInventoryEtl}
import com.kindminds.drs.data.pipelines.core.mws.ReportHandler
import com.kindminds.drs.data.pipelines.core.reimbursements.ReimbursementsEtl
import com.kindminds.drs.data.pipelines.core.sales.{AllOrders, DailySalesHandler}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

object DrsDPApp {

  def main(args: Array[String]): Unit = {

    val awsConfig = com.typesafe.config.ConfigFactory.load("application_52.conf")

    implicit val system = ActorSystem("drsDP" , awsConfig)
    implicit val materializer: Materializer = ActorMaterializer()

    val cmdBus = system.actorOf(Props(classOf[DrsDPCmdBus]), "drsDPCmdBus")
    val scheduler = QuartzSchedulerExtension(system)

    println(scheduler.schedules)

    val rh =  system.actorOf(ReportHandler.props(cmdBus) , "reportHandler")

//      val inventoryLedger=system.actorOf(AmzInventoryLedgerHandler.props(cmdBus),"amzInventoryLedgerHandler")

    system.actorOf(ImportManageFbaInventoryRptHandler.props(cmdBus) , "importManageFbaInventoryRptHandler")
    system.actorOf(AmzCustomerShipmentSalesHandler.props(cmdBus),"amzCustomerShipmentSalesHandler")
    system.actorOf(AmzDailyInventoryHistoryHandler.props(cmdBus),"amzDailyInventoryHistoryHandler")
    system.actorOf(AmzInventoryEventHandler.props(cmdBus),"amzInventoryEventHandler")
    system.actorOf(AmzReceivedInventoryHandler.props(cmdBus),"amzReceivedInventoryHandler")
    system.actorOf(AmzReplacementsHandler.props(cmdBus),"amzReplacementsHandler")
    system.actorOf(AmzReservedInventoryEtl.props(cmdBus),"amzReservedInventoryEtl")

    //system.actorOf(ReimbursementsEtl.props(cmdBus),"reimbursementsEtl")


    scheduler.schedule("cronManageFBAInventoryUS" , rh , RequestReport(Marketplace.AMAZON_COM,
      MwsReportType.FBA_Manage_Inventory))
    scheduler.schedule("cronManageFBAInventoryCA" , rh , RequestReport(Marketplace.AMAZON_CA,
      MwsReportType.FBA_Manage_Inventory))
    scheduler.schedule("cronManageFBAInventoryUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK,
      MwsReportType.FBA_Manage_Inventory))
    scheduler.schedule("cronManageFBAInventoryDE" , rh , RequestReport(Marketplace.AMAZON_DE,
      MwsReportType.FBA_Manage_Inventory))



    scheduler.schedule("cronFBADailyInventoryHistoryUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
      MwsReportType.FBA_Daily_Inventory_History))
    scheduler.schedule("cronFBADailyInventoryHistoryCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
      MwsReportType.FBA_Daily_Inventory_History))
    scheduler.schedule("cronFBADailyInventoryHistoryUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
      MwsReportType.FBA_Daily_Inventory_History))
    scheduler.schedule("cronFBADailyInventoryHistoryDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
      MwsReportType.FBA_Daily_Inventory_History))

    scheduler.schedule("cronCustomerShipmentSalesUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
      MwsReportType.FBA_Customer_Shipment_Sales))
    scheduler.schedule("cronCustomerShipmentSalesCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
      MwsReportType.FBA_Customer_Shipment_Sales))
    scheduler.schedule("cronCustomerShipmentSalesUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
      MwsReportType.FBA_Customer_Shipment_Sales))
    scheduler.schedule("cronCustomerShipmentSalesDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
      MwsReportType.FBA_Customer_Shipment_Sales))

    scheduler.schedule("cronInventoryEventDetailUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
      MwsReportType.FBA_Inventory_Event_Detail))
    scheduler.schedule("cronInventoryEventDetailCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
      MwsReportType.FBA_Inventory_Event_Detail))
    scheduler.schedule("cronInventoryEventDetailUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
      MwsReportType.FBA_Inventory_Event_Detail))
    scheduler.schedule("cronInventoryEventDetailDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
      MwsReportType.FBA_Inventory_Event_Detail))

    scheduler.schedule("cronReceivedInventoryUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
      MwsReportType.FBA_Received_Inventory))
    scheduler.schedule("cronReceivedInventoryCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
      MwsReportType.FBA_Received_Inventory))
    scheduler.schedule("cronReceivedInventoryUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
      MwsReportType.FBA_Received_Inventory))
    scheduler.schedule("cronReceivedInventoryDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
      MwsReportType.FBA_Received_Inventory))

    scheduler.schedule("cronReservedInventoryUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
      MwsReportType.FBA_Reserved_Inventory))
    scheduler.schedule("cronReservedInventoryCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
      MwsReportType.FBA_Reserved_Inventory))
    scheduler.schedule("cronReservedInventoryUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
      MwsReportType.FBA_Reserved_Inventory))
    scheduler.schedule("cronReservedInventoryDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
      MwsReportType.FBA_Reserved_Inventory))


    scheduler.schedule("cronReplacementsUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
      MwsReportType.FBA_Replacements))
    scheduler.schedule("cronReplacementsCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
      MwsReportType.FBA_Replacements))

//
//    scheduler.schedule("cronFBADailyInventoryHistoryUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
//      MwsReportType.FBA_Daily_Inventory_History))
//    scheduler.schedule("cronFBADailyInventoryHistoryCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
//      MwsReportType.FBA_Daily_Inventory_History))
//    scheduler.schedule("cronFBADailyInventoryHistoryUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
//      MwsReportType.FBA_Daily_Inventory_History))
//    scheduler.schedule("cronFBADailyInventoryHistoryDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
//      MwsReportType.FBA_Daily_Inventory_History))
//
//    scheduler.schedule("cronCustomerShipmentSalesUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
//      MwsReportType.FBA_Customer_Shipment_Sales))
//    scheduler.schedule("cronCustomerShipmentSalesCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
//      MwsReportType.FBA_Customer_Shipment_Sales))
//    scheduler.schedule("cronCustomerShipmentSalesUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
//      MwsReportType.FBA_Customer_Shipment_Sales))
//    scheduler.schedule("cronCustomerShipmentSalesDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
//      MwsReportType.FBA_Customer_Shipment_Sales))
//
//    scheduler.schedule("cronInventoryEventDetailUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
//      MwsReportType.FBA_Inventory_Event_Detail))
//    scheduler.schedule("cronInventoryEventDetailCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
//      MwsReportType.FBA_Inventory_Event_Detail))
//    scheduler.schedule("cronInventoryEventDetailUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
//      MwsReportType.FBA_Inventory_Event_Detail))
//    scheduler.schedule("cronInventoryEventDetailDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
//      MwsReportType.FBA_Inventory_Event_Detail))
//
//    scheduler.schedule("cronReceivedInventoryUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
//      MwsReportType.FBA_Received_Inventory))
//    scheduler.schedule("cronReceivedInventoryCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
//      MwsReportType.FBA_Received_Inventory))
//    scheduler.schedule("cronReceivedInventoryUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
//      MwsReportType.FBA_Received_Inventory))
//    scheduler.schedule("cronReceivedInventoryDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
//      MwsReportType.FBA_Received_Inventory))
//
//    scheduler.schedule("cronReservedInventoryUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
//      MwsReportType.FBA_Reserved_Inventory))
//    scheduler.schedule("cronReservedInventoryCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
//      MwsReportType.FBA_Reserved_Inventory))
//    scheduler.schedule("cronReservedInventoryUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
//      MwsReportType.FBA_Reserved_Inventory))
//    scheduler.schedule("cronReservedInventoryDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
//      MwsReportType.FBA_Reserved_Inventory))
//
//
//    scheduler.schedule("cronReplacementsUS" , rh , RequestReport(Marketplace.AMAZON_COM ,
//      MwsReportType.FBA_Replacements))
//    scheduler.schedule("cronReplacementsCA" , rh , RequestReport(Marketplace.AMAZON_CA ,
//      MwsReportType.FBA_Replacements))

//    scheduler.schedule("cronReplacementsUK" , rh , RequestReport(Marketplace.AMAZON_CO_UK ,
//      MwsReportType.FBA_Replacements))
//    scheduler.schedule("cronReplacementsDE" , rh , RequestReport(Marketplace.AMAZON_DE ,
//      MwsReportType.FBA_Replacements))

//    inventoryLedger ! ImportInventoryLedgerRpt("x")

    Thread.sleep(3000)
    //rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Daily_Inventory_History)


//    rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Daily_Inventory_History)
//    rh ! RequestReport( Marketplace.AMAZON_DE , MwsReportType.FBA_Daily_Inventory_History)
//    rh ! RequestReport( Marketplace.AMAZON_CA , MwsReportType.FBA_Daily_Inventory_History)
//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.FBA_Daily_Inventory_History)

//    rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Customer_Shipment_Sales)
//    rh ! RequestReport( Marketplace.AMAZON_DE , MwsReportType.FBA_Customer_Shipment_Sales)
//    rh ! RequestReport( Marketplace.AMAZON_CA , MwsReportType.FBA_Customer_Shipment_Sales)
//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.FBA_Customer_Shipment_Sales)
//
//    rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Inventory_Event_Detail)
//    rh ! RequestReport( Marketplace.AMAZON_DE , MwsReportType.FBA_Inventory_Event_Detail)
//    rh ! RequestReport( Marketplace.AMAZON_CA , MwsReportType.FBA_Inventory_Event_Detail)
//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.FBA_Inventory_Event_Detail)
//
//    rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Received_Inventory)
//    rh ! RequestReport( Marketplace.AMAZON_DE , MwsReportType.FBA_Received_Inventory)
//    rh ! RequestReport( Marketplace.AMAZON_CA , MwsReportType.FBA_Received_Inventory)
//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.FBA_Received_Inventory)
//
//    rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Reserved_Inventory)
//    rh ! RequestReport( Marketplace.AMAZON_DE , MwsReportType.FBA_Reserved_Inventory)
//    rh ! RequestReport( Marketplace.AMAZON_CA , MwsReportType.FBA_Reserved_Inventory)
//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.FBA_Reserved_Inventory)
//
//    rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Replacements)
//    rh ! RequestReport( Marketplace.AMAZON_DE , MwsReportType.FBA_Replacements)
//    rh ! RequestReport( Marketplace.AMAZON_CA , MwsReportType.FBA_Replacements)
//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.FBA_Replacements)


    Thread.sleep(3000)


    //
    // rj ! RequestBiweeklyStatementJournalEntries()
    //
    //    rj ! RequestRemittanceJournalEntries()

//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.Amazon_Fulfilled_shipments)
//    rh ! GetRequestList(Some("275497018534") , Marketplace.AMAZON_DE , MwsReportType.Amazon_Fulfilled_shipments)

//    rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Customer_Shipment_Sales)
//    Thread.sleep(3000)
//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.FBA_Customer_Shipment_Sales)

//    rh ! RequestReport( Marketplace.AMAZON_COM , MwsReportType.FBA_Amazon_Fulfilled_Inventory)

    /*
    for (reportType <- MwsReportType.values()) {

      if (!reportType.getType.isEmpty) {

        if (reportType.isAvailableNA) {

          Thread.sleep(90000)

          rh ! RequestReport( Marketplace.AMAZON_COM , reportType)
        }

        if (reportType.isAvailableEU) {

          Thread.sleep(90000)

          rh ! RequestReport( Marketplace.AMAZON_CO_UK , reportType)
        }

      }

    }
  */

//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.AFN_Inventory_By_Country)=
//    rh ! RequestReport( Marketplace.AMAZON_CO_UK , MwsReportType.FBA_Storage_Fees)

    //Thread.sleep(3000)

   // rh ! RequestReport(Marketplace.AMAZON_COM)

   // rh ! RequestReport(Marketplace.AMAZON_CO_UK)

    /*
   cmdBus ! ImportFullManageFBAInventoryRpt(
     "hdfs://kindminds01:9000/user/Amazon/US/Inventory/Manage FBA Inventory/" +
       "ManageFBAInventory_US_20200606.txt")
       */


   //rh ! GetRequestList("",Marketplace.AMAZON_COM)

    //rh ! DownloadReport("")
  }

}
