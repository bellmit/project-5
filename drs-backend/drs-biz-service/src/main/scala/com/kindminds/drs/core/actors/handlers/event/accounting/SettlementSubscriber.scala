package com.kindminds.drs.core.actors.handlers.event.accounting

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorPath, ActorRef, Props}
import akka.util.Timeout
import com.kindminds.drs.api.message.command._
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.actors.handlers.command.AccountsReceivableAgingHandler
import com.kindminds.drs.core.actors.handlers.event.customercare.CaseSubscriber
import com.kindminds.drs.api.data.access.rdb.EventDao

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt


object SettlementSubscriber {

  def props(drsCmdBus: ActorRef): Props =
    Props(new SettlementSubscriber(drsCmdBus))

}

class SettlementSubscriber(drsCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val resolveTimeout = Timeout(5 ,TimeUnit.SECONDS)

  private val  eventDao  =
    BizCoreCtx.get().getBean(classOf[EventDao]).asInstanceOf[EventDao]


  def receive = {


    case sc : SettlementCommitted =>

      //println("SettlementCommitted!!")

      val path = ActorPath.fromString("akka://drs@localhost:5003/user/allorders")
      val rjPath = ActorPath.fromString("akka://drs@localhost:5003/user/requestJournalEntriesHandler")
      val ar = context.system.actorOf(AccountsReceivableAgingHandler.props(drsCmdBus), "accountsReceivableAgingHandler")

      val ao: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
        resolveTimeout.duration)

      val rj: ActorRef = Await.result(context.system.actorSelection(rjPath).resolveOne(),
        resolveTimeout.duration)


      ar ! CalculateOriginalAccountsReceivable()

      ao ! RefreshAllOrdersTransaction()

      context.system.scheduler.scheduleOnce(10.minute, rj, RequestBiweeklyStatementJournalEntries())

      log.info("SettlementCommitted")

    case mtc : MarketSideTransactionsCollected =>

      val result = "{\"result\":\""  +  mtc.result + " transaction(s) has been generated. \""
      eventDao.insert("MarketSideTransactionsCollected" , result)

    case mtp : MarketSideTransactionsProcessed =>

      val result = "{\"result\":\""  +  mtp.result + " drs transaction(s) has been generated. \""

        eventDao.insert("MarketSideTransactionsProcessed",result)

    case message: Any =>
      log.info(s"SettlementSubscriber: received unexpected: $message")
  }


}
