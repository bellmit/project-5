package com.kindminds.drs.core.actors.handlers.query

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.customer.GetReturns
import com.kindminds.drs.api.message.query.notification.GetNotifications
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.persist.data.access.nosql.mongo.notification.NotificationDao


object NotificationQueryHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new NotificationQueryHandler(drsQueryBus))

}


class NotificationQueryHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetNotifications].getName, self)

  val om = new ObjectMapper()

  val dao = new NotificationDao



  override def receive: Receive = {

    case g : GetNotifications =>

      val size = dao.countByUserId(g.userId)
      val totalPages = BigDecimal(size.toDouble/5).setScale(0, BigDecimal.RoundingMode.UP)

      val s = dao.findByUserId(g.userId ,g.pageIndex)
      val result = "{\"notifications\":"+ s +" , \"totalPages\":"+ totalPages+" , \"pageIndex\":"+ g.pageIndex +"}"

      this.sender() ! result



  }

}