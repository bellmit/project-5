package com.kindminds.drs.core.actors.handlers.command

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.viewKeyProductStatsUco.TransformData
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.api.usecase.ViewKeyProductStatsUco
import com.kindminds.drs.core.RegisterCommandHandler
import org.springframework.context.annotation.AnnotationConfigApplicationContext


object ViewKeyProductStatusHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new ViewKeyProductStatusHandler(drsCmdBus))

}

class ViewKeyProductStatusHandler (drsCmdBus: ActorRef)
  extends Actor with ActorLogging {

  val name = self.path.name

  val springCtx = BizCoreCtx.get

  drsCmdBus ! RegisterCommandHandler(name,classOf[TransformData].getName ,self)

  val uco = springCtx.getBean(classOf[ViewKeyProductStatsUco]).asInstanceOf[ViewKeyProductStatsUco]

  val dao = springCtx.getBean(classOf[com.kindminds.drs.api.data.access.usecase.product.ViewKeyProductStatsDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.usecase.product.ViewKeyProductStatsDao]


  def receive = {

    case s : TransformData =>

      val rpt = uco.getKeyProductStatsReport(false,"K2")
      dao.save(rpt)

    case message: Any =>
      log.info(s"ViewKeyProductStatusHandler: received unexpected: $message")
  }

}