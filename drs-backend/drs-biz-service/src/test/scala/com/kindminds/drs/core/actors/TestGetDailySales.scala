package com.kindminds.drs.core.actors

import akka.actor.{Actor, ActorSystem, Props}
import akka.remote.transport.ThrottlerTransportAdapter.Direction.Receive
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.query.prdocut.dashboard.{GetDailyOrder, GetDailySalesQtyAndRev}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.actors.handlers.query.product.ProductDashboardViewHandler
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.collection.immutable

class TestGetDailySales extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus

  val pdb = TestActorRef(ProductDashboardViewHandler.props(drsQueryBus))
  BizCoreCtx.get

  "Actor" must{

//    pdb ! GetDailySalesQtyAndRev(Some("K101"),None,None,None)

      pdb ! GetDailyOrder(Some("K101"),None,None,None)

  }


}
