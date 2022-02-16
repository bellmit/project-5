package com.kindminds.drs.data.pipelines.core.app

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId, ZoneOffset, ZonedDateTime}

import akka.actor.{ActorSystem, Props}
import akka.stream.{ActorMaterializer, Materializer}
import com.kindminds.drs.Country
import com.kindminds.drs.api.message.command.amazon.order.{RequestAmazonFulfillmentOrder, RequestAmazonOrder}
import com.kindminds.drs.api.message.command.shopify.order.RequestShopifyOrder
import com.kindminds.drs.api.message.command.{DeleteESCustomercareCase, RefreshAllOrdersTransaction, RefreshESCustomercareCase, RefreshESCustomercareCaseIssue, RequestBiweeklyStatementJournalEntries, RequestRemittanceJournalEntries}
import com.kindminds.drs.data.pipelines.api.message._
import com.kindminds.drs.data.pipelines.core.DrsDPCmdBus
import com.kindminds.drs.data.pipelines.core.accounting.RequestJournalEntriesHandler
import com.kindminds.drs.data.pipelines.core.es.{CCCaseES, CCCaseIssueES, OrderES}
import com.kindminds.drs.data.pipelines.core.inventory.ImportManageFbaInventoryRptHandler
import com.kindminds.drs.data.pipelines.core.mws.{FulfillmentOrderHandler, OrderHandler}
import com.kindminds.drs.data.pipelines.core.product.ProductDataLoader
import com.kindminds.drs.data.pipelines.core.product.ProductRetailPriceLoader
import com.kindminds.drs.data.pipelines.core.sales.{AllOrders, DailySalesHandler}
import com.kindminds.drs.data.pipelines.core.shopify.ShopifyOrderHandler
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

object DrsDPAwsApp {

  def main(args: Array[String]): Unit = {

    val awsConfig = com.typesafe.config.ConfigFactory.load("application_aws.conf")

    implicit val system = ActorSystem("drs" , awsConfig)

    implicit val materializer: Materializer = ActorMaterializer()

    val cmdBus = system.actorOf(Props(classOf[DrsDPCmdBus]), "drsDPCmdBus")
    val scheduler = QuartzSchedulerExtension(system)
    println(scheduler.schedules)

    val amzOrderHandlder = system.actorOf(OrderHandler.props(cmdBus), "amzOrderHandlder")
    val amzFOrderHandlder = system.actorOf(FulfillmentOrderHandler.props(cmdBus), "amzFOrderHandlder")
    val shopifyOrderHandler = system.actorOf(ShopifyOrderHandler.props(cmdBus), "shopifyOrderHandler")
    val productDataLoader = system.actorOf(ProductDataLoader.props(cmdBus), "productDataLoader")
    val productRetailPriceLoader = system.actorOf(ProductRetailPriceLoader.props(cmdBus), "productRetailPriceLoader")

    val cccEs = system.actorOf(CCCaseES.props() , "cccEs")
    val cccaseIssueES = system.actorOf(CCCaseIssueES.props() , "cccaseIssueES")

    val ao =  system.actorOf(AllOrders.props(cmdBus) , "allorders")
    val oes =  system.actorOf(OrderES.props(cmdBus) , "orderEs")
    val ds =  system.actorOf(DailySalesHandler.props(cmdBus) , "dailySalesHandler")
    val rj = system.actorOf(RequestJournalEntriesHandler.props(), "requestJournalEntriesHandler")



    scheduler.schedule("cronReqAmzOrderNA", amzOrderHandlder,  RequestAmazonOrder(Country.NA))
    scheduler.schedule("cronReqAmzOrderEU", amzOrderHandlder,  RequestAmazonOrder(Country.EU))
    scheduler.schedule("cronReqFAmzOrderNA", amzFOrderHandlder,  RequestAmazonFulfillmentOrder("NA"))
    scheduler.schedule("cronReqFAmzOrderUK", amzFOrderHandlder,  RequestAmazonFulfillmentOrder("UK"))
    scheduler.schedule("cronReqShopifyOrder", shopifyOrderHandler,  RequestShopifyOrder())


    scheduler.schedule("cronRefreshAllOrders" , ao , StartRefreshAllOrders())
    scheduler.schedule("cronRefreshMissingAllOrders" , ao , StartRefreshMissingOrders())
    //scheduler.schedule("cronRefreshNotUpdatedOrders" , ao , StartRefreshNotUpdatedOrders())
    scheduler.schedule("cronDailySales" , ds , RefreshDailySales())
    scheduler.schedule("cronGenerateRemittance", rj, RequestRemittanceJournalEntries())
    scheduler.schedule("cronLoadProductData", productDataLoader, RefreshSkuStatistics())
    scheduler.schedule("cronLoadProductRetailPrice", productRetailPriceLoader, RefreshSkuRetailPrice())




    Thread.sleep(3000)

    //amzOrderHandlder ! RequestAmazonOrder(Country.EU)


    //ao ! StartRefreshAllOrders()
    //ao! StartRefreshMissingOrders()

    //productDataLoader ! RefreshSkuStatistics()


    //cccaseIssueES ! RefreshESCustomercareCaseIssue(1760)
    //ao ! RefreshAllOrdersTransaction

    //productRetailPriceLoader ! RefreshSkuRetailPrice()


    //rj ! RequestBiweeklyStatementJournalEntries()
    // rj ! RequestRemittanceJournalEntries()
    //mfir ! ImportFullManageFBAInventoryRpt()




  }

}
