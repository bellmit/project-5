package com.kindminds.drs.data.pipelines.core.mws


import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.amazon.order.RequestAmazonOrder
import com.kindminds.drs.api.message.viewKeyProductStatsUco.TransformData
import com.kindminds.drs.data.pipelines.api.message.RegisterCommandHandler
import com.kindminds.drs.data.pipelines.core.BizCoreCtx
import com.kindminds.drs.api.schedule.job.ImportAmazonOrderUco
import com.kindminds.drs.service.schedule.job.ImportAmazonOrderUcoImpl





object OrderHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new OrderHandler(drsCmdBus))

}

class OrderHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {


  val name = self.path.name

  val springCtx = BizCoreCtx.get

  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestAmazonOrder].getName ,self)

  val uco = springCtx.getBean(classOf[ImportAmazonOrderUco]).asInstanceOf[ImportAmazonOrderUco]


  def receive = {

    case s : RequestAmazonOrder =>

      val ucoImpl = uco.asInstanceOf[ImportAmazonOrderUcoImpl]
      ucoImpl.setCountry(s.country)
      ucoImpl.importOrders()

    case message: Any =>
      log.info(s"OrderHandler: received unexpected: $message")
  }

}
