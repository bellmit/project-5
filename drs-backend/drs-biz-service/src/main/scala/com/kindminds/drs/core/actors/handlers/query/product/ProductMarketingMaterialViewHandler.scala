package com.kindminds.drs.core.actors.handlers.query.product

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.onboardingApplication._
import com.kindminds.drs.api.message.query.product.GetProductMarketingMaterial
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductMarketingMaterialViewQueries
import com.kindminds.drs.api.data.transfer.productV2.ProductDetail




object ProductMarketingMaterialViewHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new ProductMarketingMaterialViewHandler(drsQueryBus))

}


class ProductMarketingMaterialViewHandler (drsQueryBus: ActorRef) extends Actor with ActorLogging {



  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name,classOf[GetProductMarketingMaterial].getName ,self)


  private val sizePerPage = 20

  val productMarketingMaterialViewQueries = BizCoreCtx.get().getBean(classOf[ProductMarketingMaterialViewQueries])
    .asInstanceOf[ProductMarketingMaterialViewQueries]

  val CompanyDao = BizCoreCtx.get().getBean(classOf[com.kindminds.drs.api.data.access.rdb.CompanyDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.rdb.CompanyDao]

  val om = new ObjectMapper()

  def receive = {


    case m: GetProductMarketingMaterial =>

      val b: ProductDetail = this.productMarketingMaterialViewQueries.
        findProductMarketingMaterial(m.productBaseCode , m.productVariationCode,
          m.marketSide)

      val s = om.writeValueAsString(b)

      this.sender() ! s


    case message: Any =>
      log.info(s"ProductMarketingMaterialViewHandler: received unexpected: $message")
  }



}
