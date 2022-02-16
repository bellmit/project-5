package com.kindminds.drs.core.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.command.product.{VerifyEANCode, VerifySku}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.actors.handlers.command.p2m.P2MCmdHandler
import com.kindminds.drs.core.actors.handlers.command.product.ManageProductCmdHandler
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class TestProductCmdHandler  extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus

  //  val p2MApplication = TestActorRef(P2MCmdHandler.props(drsCmdBus,drsEventBus))
  val productCmdHandler = TestActorRef(ManageProductCmdHandler.props(drsCmdBus,drsEventBus))

  "Actor" must {

    within(30000 millis) {


      BizCoreCtx.get

      //productCmdHandler ! VerifyEANCode("112233333")
      productCmdHandler ! VerifySku("K408-8A")


    }
  }

}
