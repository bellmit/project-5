package com.kindminds.drs.core.actors.handlers.query.sales

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.quotation.GetQuoteRequestView
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.core.biz.repo.sales.QuoteRequestRepoImpl
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequestView


object QuoteServiceFeeViewHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new QuoteServiceFeeViewHandler(drsQueryBus))

}



class QuoteServiceFeeViewHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetQuoteRequestView].getName, self)

  val quoteRequestRepo = new QuoteRequestRepoImpl

  val om = new ObjectMapper()



  def receive = {

    case qrv : GetQuoteRequestView =>

      val view: QuoteRequestView = quoteRequestRepo.findViewById(qrv.requestId)

      val jsonView = om.writeValueAsString(view)
      //System.out.//println("JSON quoteRequest view: " + jsonView)

      this.sender() ! jsonView

    case message: Any =>
      log.info(s"ApplyOnboardingHandler: received unexpected: $message")
  }






}
