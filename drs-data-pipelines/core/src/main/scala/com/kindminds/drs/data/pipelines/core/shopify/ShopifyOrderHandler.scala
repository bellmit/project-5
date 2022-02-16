package com.kindminds.drs.data.pipelines.core.shopify


import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.amazon.order.RequestAmazonOrder
import com.kindminds.drs.api.message.command.shopify.order.RequestShopifyOrder
import com.kindminds.drs.api.message.viewKeyProductStatsUco.TransformData
import com.kindminds.drs.data.pipelines.api.message.RegisterCommandHandler
import com.kindminds.drs.data.pipelines.core.BizCoreCtx
import com.kindminds.drs.api.usecase.report.shopify.ImportShopifyOrderUco
import com.kindminds.drs.api.schedule.job.ImportAmazonOrderUco
import com.kindminds.drs.service.schedule.job.ImportShopifyOrderUcoImpl





object ShopifyOrderHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new ShopifyOrderHandler(drsCmdBus))

}

class ShopifyOrderHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {


  val name = self.path.name

  val springCtx = BizCoreCtx.get

  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestShopifyOrder].getName ,self)

  val uco = springCtx.getBean(classOf[ImportShopifyOrderUco]).asInstanceOf[ImportShopifyOrderUco]


  def receive = {

    case s : RequestShopifyOrder =>


      val ucoImpl = uco.asInstanceOf[ImportShopifyOrderUcoImpl]
      ucoImpl.importOrders()

    case message: Any =>
      log.info(s"ShopifyOrderHandler: received unexpected: $message")
  }

}
