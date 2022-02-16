package com.kindminds.drs.data.pipelines.core.inventory.uns

import java.time.{OffsetDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.slick.javadsl.SlickSession
import com.kindminds.drs.Country
import com.kindminds.drs.data.pipelines.api.message.StartInitSkuSerialNo
import com.kindminds.drs.data.pipelines.core.DrsDPCmdBus
import com.kindminds.drs.data.pipelines.core.dto.{Order, RetainmentRate}
import com.kindminds.drs.data.pipelines.core.inventory.uns.ShipmentStorageLocationEtl.{session, timeout}
import com.kindminds.drs.data.pipelines.core.sales.AllOrders
import com.kindminds.drs.data.pipelines.core.settlement.IntTran.timeout
import com.typesafe.akka.`extension`.quartz.QuartzSchedulerExtension
import slick.jdbc.GetResult

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, Future}



object ShipmentIvsSkuSerialNoEtl {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("drsDP")
    implicit val materializer: Materializer = ActorMaterializer()


    val cmdBus = system.actorOf(Props(classOf[DrsDPCmdBus]), "drsDPCmdBus")

    //val ao = system.actorOf(InitShipmentSkuSerialNo.props(cmdBus), "InitShipmentSkuSerialNo")
    //cmdBus ! StartInitSkuSerialNo()
    //t ! TransformSkuSerialNo("K598")
    //t ! UpdateSkuSerialNo("K598")
     // t ! DoSkuSerialNoRefund("K598")
    //t ! DoSkuSerialNoException("K598")
    //t ! DoSkuSerialNoSellBack("K598")
    //t ! DoSkuSerialNoOther("K598")

    val worker = system.actorOf(InitShipmentSkuSerialNoWorker.props(cmdBus) , "InitShipmentSkuSerialNoWorker")

    Thread.sleep(5000)
    cmdBus ! StartInitSkuSerialNo()
    //cmdBus ! TransformSkuSerialNo("K598")



  }


}




