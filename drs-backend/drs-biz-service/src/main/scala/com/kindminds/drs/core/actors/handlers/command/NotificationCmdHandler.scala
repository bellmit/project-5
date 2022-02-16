package com.kindminds.drs.core.actors.handlers.command

import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorPath, ActorRef, Props}
import akka.util.Timeout
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.command.notification.{DeleteNotification, MarkAllNotificationsAsRead, MarkNotificationAsRead, MarkNotificationAsUnRead, SendNotification, SendNotificationToWebFE, SendUnreadNotificationCount}
import com.kindminds.drs.api.message.command.productCategory.SaveProductCategory
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterCommandHandler
import com.kindminds.drs.core.actors.handlers.command.productCategory.ProductCategoryCmdHandler
import com.kindminds.drs.api.data.access.nosql.mongo.productCategory.ProductCategoryDao
import com.kindminds.drs.persist.data.access.nosql.mongo.notification.NotificationDao
import org.bson.Document

import scala.concurrent.Await

object NotificationCmdHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new NotificationCmdHandler(drsCmdBus))


}


class NotificationCmdHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name


  drsCmdBus ! RegisterCommandHandler(name, classOf[SendNotification].getName, self)
  //drsCmdBus ! RegisterCommandHandler(name, classOf[SendUnreadNotificationCount].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[MarkAllNotificationsAsRead].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[DeleteNotification].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[MarkNotificationAsRead].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[MarkNotificationAsUnRead].getName, self)

  //akka://drs/user/NotificationHandler
  val path = ActorPath.fromString("akka://drs@localhost:5002/user/NotificationHandler")
  val dao = new NotificationDao


  override def receive: Receive = {

    case sn : SendNotification =>

      implicit val resolveTimeout = Timeout(30 ,TimeUnit.SECONDS)
      val feNotification: ActorRef = Await.result(context.system.actorSelection(path).resolveOne(),
        resolveTimeout.duration)

      //todo arthur
      val doc = new Document
      doc.append("topic",sn.topic)
      doc.append("content", sn.content)
      doc.append("timestamp", System.currentTimeMillis())
      doc.append("refUrl" , sn.refUrl)
      doc.append("read",false)
      doc.append("userId" , sn.userId)

      dao.save(doc)
      feNotification ! SendNotificationToWebFE(doc.toJson,sn.userId)

    case m : MarkNotificationAsRead =>

      val doc = dao.findById(m.id)
      doc.put("read" , true)
      dao.save(doc)

      sender ! "done"

    case mu : MarkNotificationAsUnRead =>

      val doc = dao.findById(mu.id)
      doc.put("read" , false)
      dao.save(doc)

      sender ! "done"

    case d : DeleteNotification =>

      dao.delete(d.id)
      sender ! "done"

    case ma : MarkAllNotificationsAsRead =>

    val resultList =  dao.findUnread(ma.userId)
      resultList.forEach( d =>{
        d.put("read" , true)
        dao.save(d)
      })

      sender ! "done"

    case message: Any =>
      log.info(s"NotificationCmdHandler: received unexpected: $message")

  }

}
