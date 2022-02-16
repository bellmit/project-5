package com.kindminds.drs.core.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.query.prdocut.dashboard.GetDetailPageSalesTraffic
import com.kindminds.drs.api.message.viewKeyProductStatsUco.{GetKeyProductBaseRetailPrice, GetKeyProductInventoryStats, GetKeyProductLastSevenDaysOrder, GetKeyProductSinceLastSettlementOrder, GetKeyProductStatsReport, GetKeyProductTotalFbaInStock}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.actors.handlers.query.ViewKeyProductStatsHandler
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import com.kindminds.drs.core.actors.handlers.query.product.ProductDashboardViewHandler
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class TestGetKeyReport extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus

  val kps = TestActorRef(ViewKeyProductStatsHandler.props(drsQueryBus))

  val pdb = TestActorRef(ProductDashboardViewHandler.props(drsQueryBus))
  BizCoreCtx.get

  BizCoreCtx.get

  "Actor" must{

//    kps ! GetKeyProductStatsReport(false , "K618")
//    kps ! GetKeyProductTotalFbaInStock(true , "K644")

    pdb ! GetKeyProductInventoryStats(Some("K488"),None,None,None)
    pdb ! GetKeyProductSinceLastSettlementOrder(Some("K618"),None,None,None)
    pdb ! GetKeyProductLastSevenDaysOrder(Some("K618"),None,None,None)
    pdb ! GetKeyProductBaseRetailPrice(Some("K618"),None,None,None)

    //Detail Page Sales Traffic
//    pdb ! GetDetailPageSalesTraffic(Some("K644"),Some("Amazon.com"),None,None)



  }


}
