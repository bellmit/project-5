package com.kindminds.drs.core.actors.handlers.command.product

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.onboardingApplication._
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.core.biz.repo.product.ProductMarketingMaterialRepoImpl
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType


object ProductMarketingMaterialHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new ProductMarketingMaterialHandler(drsCmdBus ,drsEventBus))

}

class ProductMarketingMaterialHandler (drsCmdBus: ActorRef ,drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[SaveProductMarketingMaterial].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[SubmitProductMarketingMaterial].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[ApproveProductMarketingMaterial].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RejectProductMarketingMaterial].getName ,self)

  val repo = new ProductMarketingMaterialRepoImpl



  def receive = {

    case r : RejectProductMarketingMaterial =>

      repo.add(r.productMarketingMaterial)

      drsEventBus.publish(
        ProductMarketingMaterialStatusChanged(
          ProductEditingStatusType.PENDING_SUPPLIER_ACTION , r.productMarketingMaterial))

    case a : ApproveProductMarketingMaterial =>
      repo.add(a.productMarketingMaterial)

      drsEventBus.publish(
        ProductMarketingMaterialStatusChanged(
          ProductEditingStatusType.FINALIZED , a.productMarketingMaterial))

    case sb : SubmitProductMarketingMaterial =>

      repo.add(sb.productMarketingMaterial)

      drsEventBus.publish(
        ProductMarketingMaterialStatusChanged(
          ProductEditingStatusType.PENDING_DRS_REVIEW , sb.productMarketingMaterial))


    case s : SaveProductMarketingMaterial =>

      repo.add(s.productMarketingMaterial)

      drsEventBus.publish(
        ProductMarketingMaterialSaved(s.supplierKcode,s.productBaseCode,
          s.status, s.productMarketingMaterial))

    // log.info("Save Product " + s.product.getId)

    case message: Any =>
      log.info(s"ProductMarketingMaterialHandler: received unexpected: $message")
  }

}