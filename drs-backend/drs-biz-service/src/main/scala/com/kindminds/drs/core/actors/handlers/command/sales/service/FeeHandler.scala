package com.kindminds.drs.core.actors.handlers.command.sales.service

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.fee.ActivateNewServiceFee
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.core.biz.sales.service.FeeImpl
import com.kindminds.drs.core.biz.repo.sales.QuotationRepoImpl
import com.kindminds.drs.core.biz.repo.sales.service.FeeRepoImpl
import com.kindminds.drs.api.v2.biz.domain.model.sales.service.FeeType




object FeeHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new FeeHandler(drsCmdBus ,drsEventBus))

}


class FeeHandler (drsCmdBus: ActorRef, drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name, classOf[ActivateNewServiceFee].getName, self)



  def receive = {

    case acs: ActivateNewServiceFee =>

      val qRepo = new QuotationRepoImpl
      val opQuotation = qRepo.findById(acs.quotationId)
      if(opQuotation.isPresent){
        val quotation = opQuotation.get()
        val fee = FeeImpl.valueOf(quotation.getSupplierCompanyId,FeeType.MONTHLY_SERVICE_FEE,quotation.getFinalPrice)
        fee.activate()
        val feeRepo = new FeeRepoImpl
        feeRepo.add(fee)
      }

    case message: Any =>
      log.info(s"FeeHandler: received unexpected: $message")
  }

}
