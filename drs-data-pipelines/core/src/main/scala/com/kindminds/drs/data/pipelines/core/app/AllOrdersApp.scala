package com.kindminds.drs.data.pipelines.core.app

import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time._
import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}
import com.kindminds.drs.api.message.command.RefreshAllOrdersTransaction
import com.kindminds.drs.data.pipelines.api.message._
import com.kindminds.drs.data.pipelines.core.DrsDPCmdBus
import com.kindminds.drs.data.pipelines.core.es.OrderES
import com.kindminds.drs.data.pipelines.core.sales._
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension


import scala.concurrent.Await

object AllOrdersApp {


  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("drsDP")
    implicit val materializer: Materializer = ActorMaterializer()
    val scheduler = QuartzSchedulerExtension(system)
    println(scheduler.schedules)

    val cmdBus = system.actorOf(Props(classOf[DrsDPCmdBus]), "drsDPCmdBus")

    val ao = system.actorOf(AllOrders.props(cmdBus) , "allorders")
    val oes = system.actorOf(OrderES.props(cmdBus) , "orderEs")
    val ds =  system.actorOf(DailySalesHandler.props(cmdBus) , "dailySalesHandler")

    val so = system.actorOf(SingleOrder.props(cmdBus) , "SingleOrder")


    Thread.sleep(3000)
    //ao ! StartRefreshNotUpdatedOrders()

   // ao ! StartRefreshOrdersByDate(ZonedDateTime.of(2021,6,6,0,0,0,0,
     // ZoneOffset.UTC))


    /*
    113-3011965-1432202
    113-8770214-4501017
    113-5192947-5492228

    113-6894168-2529023
    */

    //so ! RefreshOrder("EM-K488-20201102-01" )

    /*
    'EM-K488-20201102-01',
    'EM-K488-20202309-01',
    'EM-K488-20191206-01',
    'EM-K488-20190212',
    'EM-K488-20181221',
    'EM-K488-20181214'
     */


    //oes ! RefreshESOrder("EM-K488-20181214" , Some(Seq(356469).toList))

    //ao ! RefreshAllOrdersTransaction()

    //val w = system.actorOf(InitDailySalesWorker.props(cmdBus) , "initDailySalesWorker")
   // val initDs =  system.actorOf(InitDailySales.props(cmdBus), "intitDs")
    // w ! StartRefreshDailySales()

    // val initDrW =  system.actorOf(InitDailyRetainmentWorker.props(cmdBus), "intitDrW")
    //val initDr =  system.actorOf(InitRetainment.props(cmdBus), "intitDr")

    //Thread.sleep(3000)

   // initDrW ! StartRefreshDailyRetainment()
   // initDr ! CreateDailyRetainment("K488")

    //val aoRouter2 =  system.actorOf(RoundRobinPool(3).props(InitAllOrdes.props(cmdBus)), "router2")
    // val aoRouter2 =  system.actorOf(InitAllOrdes.props(cmdBus), "router2")
    //aoRouter2 ! ProcessLocalOrderTime()
    //aoRouter2 ! 1

    /*

    val totalCount : BigDecimal = 298625
    val pages : BigDecimal =  (totalCount/ 100).setScale(0, BigDecimal.RoundingMode.UP)
    println(pages)

    //aoRouter2 ! 0

    val source = Source(0 to (pages -1).toInt)
    //val source = Source(0 to 1)
    source.runForeach(x=>{
      Thread.sleep(1000)
      aoRouter2 ! x
    })*/


    /*
    val st =       OffsetDateTime.of(2019,9,11,23,59,59,0,ZoneOffset.UTC)
    val et =       OffsetDateTime.of(2019,9,16,23,59,59,0,ZoneOffset.UTC)
    ds ! RefreshMissingDailySales(st , et)
    */

    //ds ! RefreshDailySales()

    //scheduler.schedule("cronRefreshAllOrders" , ao , StartRefreshAllOrders())
    //scheduler.schedule("cronRefreshMissingAllOrders" , ao , StartRefreshMissingOrders())
    //scheduler.schedule("cronDailySales" , ds , RefreshDailySales())

    // ds ! RefreshDailySales()

    //ao ! RefreshAllOrdersTransaction(
   // ao ! StartRefreshAllOrders()
    //ao ! StartRefreshAllOrders()
    //oes ! RefreshRefundESAllOrders()

   // ds ! ProcessDailySales()
  //println( com.kindminds.drs.Marketplace.fromName("Amazon.de").getCountry.getKey)

    //cccEs ! RefreshESCustomercareCase()


    //aoRouter2 ! ProcessLocalOrderTime()


    //Thread.sleep(3000)

    //ao  !  StartRefreshAllOrders()


    oes ! RefreshESOrder("113-2384496-5136220",Some(Seq(572037).toList))

   //aoRouter2 ! ProcessEBayOrderSalesChannel()

/*

*/



  }



}
