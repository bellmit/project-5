package com.kindminds.drs.core.actors.handlers.query

import java.util
import java.util.Optional

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestActors, TestKit, TestProbe}
import com.kindminds.drs.api.message.query.onboardingApplication._
import com.kindminds.drs.core.DrsQueryBus
import com.kindminds.drs.core.actors.handlers.query.product.OnboardingApplicationViewHandler
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication
import org.apache.poi.ss.formula.functions.T
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.reflect.ClassTag

class TestOnboardingApplicationViewHandler extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val myActor = TestActorRef(new OnboardingApplicationViewHandler(drsQueryBus))

  "Actor" must {

    "send back message of serial numbers" in {
      myActor ! GetApplicationSerialNumbersBySupplier("K489")
      expectMsgClass(implicitly[ClassTag[Optional[T]]].runtimeClass);
    }
  }

  "Actor" must {

    "send back message of OnboardingApplications" in {
      myActor ! GetOnboardingApplicationList()
      expectMsgClass(implicitly[ClassTag[util.ArrayList[OnboardingApplication]]].runtimeClass);
    }
  }

  "Actor" must {

    "send back message of OnboardingApplications based on Supplier KCode" in {
      myActor ! GetOnboardingApplicationListBySupplier("K486")
      expectMsgClass(implicitly[ClassTag[util.ArrayList[OnboardingApplication]]].runtimeClass);
    }
  }
}