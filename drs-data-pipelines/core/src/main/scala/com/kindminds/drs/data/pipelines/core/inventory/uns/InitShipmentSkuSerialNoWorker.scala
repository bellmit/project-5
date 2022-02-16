package com.kindminds.drs.data.pipelines.core.inventory.uns

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.RoundRobinPool
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.util.Timeout
import com.kindminds.drs.data.pipelines.api.message.{CompleteInitKcodeSkuSerialNo, CompleteRefreshDailyRetainment, CreateDailyRetainment, RegisterCommandHandler, StartInitSkuSerialNo, StartRefreshDailyRetainment, TransformSkuSerialNo}
import com.kindminds.drs.data.pipelines.core.sales.InitRetainment

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt




object InitShipmentSkuSerialNoWorker {

  def props(drsCmdBus: ActorRef) = Props(new InitShipmentSkuSerialNoWorker(drsCmdBus))

}

class InitShipmentSkuSerialNoWorker (drsCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val ec =  scala.concurrent.ExecutionContext.global

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[StartInitSkuSerialNo].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CompleteInitKcodeSkuSerialNo].getName ,self)

  var count = 0
  var index = 2


  val router2: ActorRef = context.system.actorOf(new RoundRobinPool(3).props(InitShipmentSkuSerialNo.props(drsCmdBus)), "router2")


  var kcodeList :  Vector[String] = null

  override def receive: Receive = {

    case st : StartInitSkuSerialNo =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val timeout = scala.concurrent.duration.FiniteDuration(30, TimeUnit.SECONDS)

      //val kcode = "K151"

      val kcodeQuery = sql""" select distinct com_code  from sales.all_orders
                order by com_code """.as[String]

      Await.result(
        session.db.run(kcodeQuery).map { res =>
          // res is a Vector[String]
          kcodeList = res
        }, timeout)

      if(kcodeList.size > 0){
        println(kcodeList(0))
        println(kcodeList(1))
        println(kcodeList(2))
        router2 ! TransformSkuSerialNo(kcodeList(0))
        router2 ! TransformSkuSerialNo(kcodeList(1))
        router2 ! TransformSkuSerialNo(kcodeList(2))
      }



      // dsReloader ! ProcessDailySales("K510")

      session.close()



    case s : CompleteInitKcodeSkuSerialNo =>

      println("AAA")
      count += 1
      index += 1

      if(index < kcodeList.size){
        Thread.sleep(3000)

        if(kcodeList(index) == "K598") {
          count += 1
          index += 1
        }

        println(kcodeList(index))

        router2 ! TransformSkuSerialNo(kcodeList(index))

      }


      log.info("============================================================================")
      log.info("Finish processing " + count)
      log.info("Finish processing " + count)
      log.info("Finish processing " + count)
      log.info("Finish processing " + count)
      log.info("Finish processing " + count)
      log.info("============================================================================")

    case message: Any =>
      log.info(s"OrderHandler: received unexpected: $message")
  }

}
