package com.kindminds.drs.data.pipelines.core.sales


import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.util.Timeout
import com.kindminds.drs.data.pipelines.api.message.{CompleteRefreshDailySales, CreateDailySales, ProcessDailySales, RegisterCommandHandler, StartRefreshDailySales}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success

object InitDailySalesWorker {

  def props(drsCmdBus: ActorRef ): Props =
    Props(new InitDailySalesWorker(drsCmdBus))

}

class InitDailySalesWorker(drsCmdBus: ActorRef) extends Actor with ActorLogging {

  implicit val timeout = Timeout(60 seconds)
  implicit val ec =  scala.concurrent.ExecutionContext.global

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[StartRefreshDailySales].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CompleteRefreshDailySales].getName ,self)

  var count = 0
  var index = 0


  val initDs = context.system.actorOf(InitDailySales.props(drsCmdBus) , "initDs")

  val dsReloader = context.system.actorOf(DailySalesReloader.props(drsCmdBus) , "dsReloader")

  var kcodeList :  Vector[String] = null

  def receive = {

    case st : StartRefreshDailySales =>

      implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")
      import session.profile.api._
      val timeout = scala.concurrent.duration.FiniteDuration(10, TimeUnit.SECONDS)

      //val kcode = "K151"

      val kcodeQuery = sql""" select distinct com_code  from sales.all_orders
                order by com_code """.as[String]

      Await.result(
        session.db.run(kcodeQuery).map { res =>
          // res is a Vector[String]
          kcodeList = res
        }, timeout)


      //initDs ! CreateDailySales(kcodeList(0))
      dsReloader ! ProcessDailySales("K510")

      session.close()



    case s : CompleteRefreshDailySales =>

      println("AAA")
      count += 1
      index += 1

      if(index < kcodeList.size){
        Thread.sleep(3000)
        //initDs ! CreateDailySales(kcodeList(index))
        dsReloader ! ProcessDailySales(kcodeList(index))
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

