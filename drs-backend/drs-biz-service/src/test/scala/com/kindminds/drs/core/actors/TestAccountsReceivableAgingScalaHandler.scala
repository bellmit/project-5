package com.kindminds.drs.core.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.command.{CalculateOriginalAccountsReceivable, GenerateAccountsReceivableAgingReport}
import com.kindminds.drs.core.actors.handlers.command.AccountsReceivableAgingHandler
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class TestAccountsReceivableAgingScalaHandler  extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
//  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus


  val  arAging = TestActorRef(AccountsReceivableAgingHandler.props(drsCmdBus ))

//  "Actor" must {
//
//    within(60000 millis) {
//
//    //  BizCoreCtx.get
//
//
//      arAging ! CalculateOriginalAccountsReceivable()
//
//
//    }
//  }

//  "Actor" must {
//
//    within(60000 millis) {
//
//      //  BizCoreCtx.get
//
//
//      arAging ! GenerateAccountsReceivableAgingReport()
//
//
//    }
//  }



}
