package com.kindminds.drs.data.pipelines.core.mws




import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.amazon.order.{RequestAmazonFulfillmentOrder, RequestAmazonOrder}
import com.kindminds.drs.data.pipelines.api.message.RegisterCommandHandler
import com.kindminds.drs.data.pipelines.core.BizCoreCtx
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonFulfillmentOrderUco
import com.kindminds.drs.api.schedule.job.ImportAmazonOrderUco
import com.kindminds.drs.service.schedule.job.ImportAmazonFulfillmentOrderUcoImpl




object FulfillmentOrderHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new FulfillmentOrderHandler(drsCmdBus))

}

class FulfillmentOrderHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {


  val name = self.path.name

  val springCtx = BizCoreCtx.get

  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestAmazonFulfillmentOrder].getName ,self)

  val uco = springCtx.getBean(classOf[ImportAmazonFulfillmentOrderUco])
    .asInstanceOf[ImportAmazonFulfillmentOrderUco]


  def receive = {

    case s : RequestAmazonFulfillmentOrder =>

      val ucoImpl = uco.asInstanceOf[ImportAmazonFulfillmentOrderUcoImpl]
      ucoImpl.setRegion(s.serviceRegion)
      ucoImpl.importOrders()

    case message: Any =>
      log.info(s"OrderHandler: received unexpected: $message")
  }

}

