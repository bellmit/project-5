package com.kindminds.drs.core.actors.handlers.event.sales

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.fee.ActivateNewServiceFee
import com.kindminds.drs.api.message.command.onboardingApplication.{CompleteQuoteRequest, StartQuoteRequest}
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.core.biz.repo.sales.QuoteRequestRepoImpl
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest


object QuoteRequestSubscriber {

  def props(drsCmdBus: ActorRef): Props =
    Props(new QuoteRequestSubscriber(drsCmdBus))

}

class QuoteRequestSubscriber(drsCmdBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name


  val qrRepo = new QuoteRequestRepoImpl

  def receive = {

    case rq : ServiceFeeQuotation4OnboardingProdRequested =>
      saveView(rq.quoteRequest)

      drsCmdBus ! StartQuoteRequest

    case c : ServiceFeeQuotationConfirmed =>
      saveView(c.quoteRequest)

      drsCmdBus ! ActivateNewServiceFee

      drsCmdBus ! CompleteQuoteRequest

    case v : ServiceFeeQuotationModified =>
      saveView(v.quoteRequest)


    case a : ServiceFeeQuotationAccepted =>
      saveView(a.quoteRequest)


    case r : ServiceFeeQuotationRejected =>
      saveView(r.quoteRequest)


    case message: Any =>
      log.info(s"QuoteRequestSubscriber: received unexpected: $message")
  }

  def saveView(quoteRequest : QuoteRequest): Unit = {

    qrRepo.deleteView(quoteRequest.getId)

    qrRepo.addView(quoteRequest)

  }

}
