package com.kindminds.drs.core.actors.handlers.event.product

import akka.actor.{Actor, ActorLogging, Props}
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.OnboardingApplicationLineitemDao
import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.onboarding.OnboardingApplicationLineitemViewDao


object OnboardingApplicationLineitemSubscriber {

  def props(): Props =
    Props(new OnboardingApplicationSubscriber())

}

class OnboardingApplicationLineitemSubscriber() extends Actor with ActorLogging {

  val dao = BizCoreCtx.get().getBean(
    classOf[OnboardingApplicationLineitemDao]).asInstanceOf[OnboardingApplicationLineitemViewDao]

  def receive = {


    /*
    case oa : OnboardingApplicationSaved =>
      dao.deleteView(oa.onboardingApplication.getId)
      oa.onboardingApplication.getLineitems.forEach(item => {
        dao.saveView(item)
      })

      log.info("")
      */

    case message: Any =>
      log.info(s"OnboardingApplicationSubscriber: received unexpected: $message")
  }


}