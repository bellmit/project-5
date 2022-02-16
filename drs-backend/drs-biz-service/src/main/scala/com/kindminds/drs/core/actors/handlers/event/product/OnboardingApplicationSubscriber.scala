package com.kindminds.drs.core.actors.handlers.event.product

import akka.actor.{Actor, ActorLogging, Props}
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.{OnboardingApplicationDao, OnboardingApplicationLineitemDao}
import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.onboarding.{OnboardingApplicationLineitemViewDao, OnboardingApplicationViewDao}

object OnboardingApplicationSubscriber {

  def props(): Props =
    Props(new OnboardingApplicationSubscriber())

}

class OnboardingApplicationSubscriber() extends Actor with ActorLogging {

  val onboardingApplicationDao = BizCoreCtx.get().getBean(
    classOf[OnboardingApplicationViewDao])
    .asInstanceOf[OnboardingApplicationViewDao]


  val lineItemdao = BizCoreCtx.get().getBean(
    classOf[OnboardingApplicationLineitemViewDao]).asInstanceOf[OnboardingApplicationLineitemViewDao]

  def receive = {


    case oa : OnboardingApplicationSaved =>

      onboardingApplicationDao.saveView(oa.onboardingApplication)




      log.info("")

    case message: Any =>
      log.info(s"OnboardingApplicationSubscriber: received unexpected: $message")
  }


}
