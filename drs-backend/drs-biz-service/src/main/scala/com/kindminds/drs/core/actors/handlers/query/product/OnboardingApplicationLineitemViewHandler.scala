package com.kindminds.drs.core.actors.handlers.query.product

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.onboardingApplication._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.OnboardingApplicationDao
import com.kindminds.drs.api.data.cqrs.store.read.queries.{OnboardingApplicationLineitemViewQueries, OnboardingApplicationViewQueries}
import com.kindminds.drs.api.data.es.queries.productV2.onboarding.OnboardingApplicationDetailQueries

import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType
import com.kindminds.drs.util.Encryptor


object OnboardingApplicationLineitemViewHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new OnboardingApplicationLineitemViewHandler(drsQueryBus))

}

class OnboardingApplicationLineitemViewHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetTrialList].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetEvalSample].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetPresentSample].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetGiveFeedback].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetProvideComment].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetCheckInsurance].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetCheckComplianceAndCertAvailability].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetCheckProfitability].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetApproveSample].getName, self)

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetOnboardingApplicationLineitem].getName, self)


  val vQueries = BizCoreCtx.get().getBean(classOf[OnboardingApplicationDetailQueries])
    .asInstanceOf[OnboardingApplicationDetailQueries]

  val lineItemQueries = BizCoreCtx.get().getBean(classOf[OnboardingApplicationLineitemViewQueries])
    .asInstanceOf[OnboardingApplicationLineitemViewQueries]

  val om = new ObjectMapper()

  def receive = {
    case t : GetTrialList =>

      val opJson = vQueries.getTrialList(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")

    case t : GetEvalSample =>

      val opJson = vQueries.getEvalSample(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")

    case t : GetPresentSample =>

      val opJson = vQueries.getPresentSample(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")

    case t : GetApproveSample =>

      val opJson = vQueries.getApproveSample(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")

    case t : GetGiveFeedback =>

      val opJson = vQueries.getGiveFeedback(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")
    case t : GetProvideComment =>

      val opJson = vQueries.getProvideComment(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")

    case t : GetCheckProfitability =>

      val opJson = vQueries.getCheckProfitability(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")

    case t : GetCheckInsurance =>

      val opJson = vQueries.getCheckInsurance(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")

    case t : GetCheckComplianceAndCertAvailability =>

      val opJson = vQueries.getCheckCompliance(t.onboardingApplicationLineitemId)

      this.sender() ! opJson.orElse("")


    case i : GetOnboardingApplicationLineitem =>

      val opItem = lineItemQueries.getOnboardingApplicationLineitem(i.onboardingApplicationLineitemId)

      var itemJson = ""
      if(opItem.isPresent){
        itemJson =  om.writeValueAsString(opItem.get())
      }

      sender() ! itemJson


    case message: Any =>
      log.info(s"OnboardingApplicationLineitemViewHandler: received unexpected: $message")
  }



}
