package com.kindminds.drs.core.actors


import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.command.quotation._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.actors.handlers.command.MailHandler
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import com.kindminds.drs.core.actors.handlers.command.sales.QuoteServiceFeeHandler
import com.kindminds.drs.core.actors.handlers.command.sales.service.FeeHandler
import com.kindminds.drs.core.actors.handlers.event.sales.QuoteRequestSubscriber
import com.kindminds.drs.core.actors.handlers.query.sales.QuoteServiceFeeViewHandler
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.reflect.ClassTag

class TestQuoteServiceFeeHandler extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val name = self.path.name
  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus

  val cmdActor = TestActorRef(new QuoteServiceFeeHandler(drsCmdBus, drsEventBus))
  val qryActor = TestActorRef(QuoteServiceFeeViewHandler.props(drsQueryBus))
  val feeActor = TestActorRef(FeeHandler.props(drsCmdBus, drsEventBus))
  val mailActor = TestActorRef(new MailHandler(drsCmdBus, BizCoreCtx.get))
  drsEventBus.subscribe(system.actorOf(QuoteRequestSubscriber.props(drsCmdBus), "QuoteRequestSubscriber"), com.kindminds.drs.api.message.event.Events.QuoteRequest)

  val requestId = "4bdf83c6-f8f7-49f4-9f83-c6f8f749f484"

  //run this first test to create requestId
//  "Actor" must {
//
//    "initialize Quote Request" in {
//      cmdActor ! RequestServiceFeeQuotation4OnboardingProd("test-onboarding-app-id2")
//
//      Thread.sleep(1000)
//    }
//
//  }


//  "Actor" must {
//
//    "update status to created" in {
//
//      cmdActor ! CreateServiceFeeQuotation(requestId, "38000", "robert.lee@drs.network")
//
//      Thread.sleep(1000)
//    }
//  }
//
//  "Actor" must {
//
//    "update status to rejected" in {
//      cmdActor ! RejectServiceFeeQuotation(requestId)
//
//      Thread.sleep(1000)
//    }
//  }
//
//  "Actor" must {
//
//    "modify price to 6500" in {
//      cmdActor ! ModifyServiceFeeQuotation(requestId, "35000", "robert.lee@drs.network")
//
//      Thread.sleep(1000)
//    }
//  }
//
//  "Actor" must {
//
//    "update status to accepted" in {
//      cmdActor ! AcceptServiceFeeQuotation(requestId)
//
//      Thread.sleep(1000)
//    }
//  }

  "Actor" must {

    "update status to confirmed" in {
      cmdActor ! ConfirmServiceFeeQuotation(requestId)

      Thread.sleep(1000)
    }
  }

//  "Actor" must {
//
//    "display Quote Request View" in {
//      qryActor ! GetQuoteRequestView(requestId)
//
//      Thread.sleep(1000)
//    }
//
//  }

}
