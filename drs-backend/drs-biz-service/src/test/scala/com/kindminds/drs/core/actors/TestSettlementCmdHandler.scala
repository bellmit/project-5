package com.kindminds.drs.core.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.command.Settlement.ProcessMarketSideTransactionById
import com.kindminds.drs.core.actors.handlers.command.accounting.SettlementCmdHandler
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class TestSettlementCmdHandler  extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus


  val  settlement = TestActorRef(SettlementCmdHandler.props(drsCmdBus, drsEventBus))

  "Actor" must {

    within(30000 millis) {

    //  BizCoreCtx.get
     settlement ! ProcessMarketSideTransactionById("180", 139080)

//      settlement ! ProcessMarketSideTransactionById("180", 158589)
//     settlement ! ProcessMarketSideTransactionById("180", 158590)
//      settlement ! ProcessMarketSideTransactionById("180", 158591)
//      settlement ! ProcessMarketSideTransactionById("180", 158592)
//
//     settlement ! ProcessMarketSideTransactionById("180", 158593)
//      settlement ! ProcessMarketSideTransactionById("180", 158594)
//      settlement ! ProcessMarketSideTransactionById("180", 158595)
//      settlement ! ProcessMarketSideTransactionById("180", 158596)
//      settlement ! ProcessMarketSideTransactionById("180", 158597)

//      settlement ! ProcessMarketSideTransactionById("180", 158598)
//      settlement ! ProcessMarketSideTransactionById("180", 158599)
//      settlement ! ProcessMarketSideTransactionById("180", 158600)
//      settlement ! ProcessMarketSideTransactionById("180", 158601)
//      settlement ! ProcessMarketSideTransactionById("180", 158602)
//
//      settlement ! ProcessMarketSideTransactionById("180", 158603)
//      settlement ! ProcessMarketSideTransactionById("180", 158604)




    }
  }



}
