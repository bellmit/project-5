package com.kindminds.drs.data.pipelines.core.sales

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.util.Timeout
import com.kindminds.drs.data.pipelines.api.message._


import scala.concurrent.Await
import scala.concurrent.duration._


object InitDailyRetainmentWorker {

  def props(drsCmdBus: ActorRef ): Props =
    Props(new InitDailyRetainmentWorker(drsCmdBus))

}

class InitDailyRetainmentWorker(drsCmdBus: ActorRef) extends Actor with ActorLogging {

  implicit val timeout = Timeout(60 seconds)
  implicit val ec =  scala.concurrent.ExecutionContext.global

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[StartRefreshDailyRetainment].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CompleteRefreshDailyRetainment].getName ,self)

  var count = 0
  var index = 0


  val initDr = context.system.actorOf(InitRetainment.props(drsCmdBus) , "initDr")

  //val dsReloader = context.system.actorOf(DailySalesReloader.props(drsCmdBus) , "dsReloader")

  var kcodeList :  Vector[String] = null

  def receive = {

    case st : StartRefreshDailyRetainment =>

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


      println(kcodeList(0))
      initDr ! CreateDailyRetainment(kcodeList(0))
     // dsReloader ! ProcessDailySales("K510")

      session.close()



    case s : CompleteRefreshDailyRetainment =>

      println("AAA")
      count += 1
      index += 1

      if(index < kcodeList.size){
        Thread.sleep(3000)
        initDr ! CreateDailyRetainment(kcodeList(index))
        //dsReloader ! ProcessDailySales(kcodeList(index))
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

