package com.kindminds.drs.core.actors


import java.util.Optional

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}

import com.kindminds.drs.api.message.command.onboardingApplication._
import com.kindminds.drs.api.message.query.customer.GetReturns
import com.kindminds.drs.api.message.query.onboardingApplication._
import com.kindminds.drs.api.message.query.onboardingApplication.GetTrialList
import com.kindminds.drs.api.message.query.prdocut.dashboard.{GetDailySalesQtyAndRev, GetMTDCampaignSpendAndAcos, GetMTDCustomerCareCases}
import com.kindminds.drs.api.message.query.product.{GetAmazonProductReview, GetProductBaseCode, GetProductSkuCode}
import com.kindminds.drs.core.actors.handlers.command.product._
import com.kindminds.drs.core.actors.handlers.query.product.{OnboardingApplicationLineitemViewHandler, OnboardingApplicationViewHandler, ProductDashboardViewHandler, ProductViewHandler}
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.actors.handlers.command.logistics.VerifyIvsProductInfoHandler
import com.kindminds.drs.core.actors.handlers.query.customer.CustomerQueryHandler
import org.apache.poi.ss.formula.functions.T
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.reflect.ClassTag

class TestVerifyProductInfo  extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus


  val  ivsVp = TestActorRef(VerifyIvsProductInfoHandler.props(drsCmdBus ))

  "Actor" must {

    within(30000 millis) {

    //  BizCoreCtx.get


     // ivsVp ! SubmitIvsProductInfo("")


    }
  }



}
