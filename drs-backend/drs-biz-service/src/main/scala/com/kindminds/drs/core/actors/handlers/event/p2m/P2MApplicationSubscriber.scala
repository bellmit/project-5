package com.kindminds.drs.core.actors.handlers.event.p2m

import akka.actor.{Actor, ActorLogging, ActorPath, ActorRef, Props}
import akka.util.Timeout
import com.kindminds.drs.api.message.command.RefreshAllOrdersTransaction
import com.kindminds.drs.api.message.command.p2m.{CreateApplicationNumber, CreateP2MApplication}
import com.kindminds.drs.api.message.event.{P2mApplicationApproveToRemove, P2mApplicationApproved, P2mApplicationCreated, P2mApplicationRejected, P2mApplicationSubmitted}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.api.data.access.rdb.EventDao
import com.kindminds.drs.api.message.command.manageProduct.{ChangeSkuStatusToApplying, ChangeSkuStatusToSelling, SaveApprovedP2MProduct, RemoveSkuSellingStatus, RemoveSkuApplyingStatus}
import com.kindminds.drs.api.message.command.notification.SendNotification
import com.kindminds.drs.api.usecase.product.{MaintainProductBaseUco, MaintainProductSkuUco}
import com.kindminds.drs.api.v1.model.product.BaseProduct
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao
import com.kindminds.drs.persist.data.access.nosql.mongo.process.UserTaskDaoImpl
import com.kindminds.drs.util.Encryptor

//import org.elasticsearch.search.DocValueFormat.DateTime
import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, OffsetDateTime, ZoneId, ZoneOffset, ZonedDateTime}
import java.util.Date
import java.util.concurrent.TimeUnit

import com.kindminds.drs.{ UserTaskType}
import com.kindminds.drs.api.message.process.CreateUserTask

import scala.concurrent.Await


object P2MApplicationSubscriber {

  def props(drsCmdBus: ActorRef): Props =
    Props(new P2MApplicationSubscriber(drsCmdBus))


}
class P2MApplicationSubscriber(drsCmdBus: ActorRef) extends Actor with ActorLogging {


  implicit val resolveTimeout = Timeout(5 ,TimeUnit.SECONDS)

  private val  eventDao  =
    BizCoreCtx.get().getBean(classOf[EventDao]).asInstanceOf[EventDao]

  val springCtx = BizCoreCtx.get


  //val notificationdao = new NotificationDao

  val userTaskDao = new UserTaskDaoImpl
  val p2mDao = new P2MApplicationDao



  def receive = {

    case p2mac : P2mApplicationCreated =>

//      drsCmdBus ! ChangeSkuStatusToApplying(p2mac.productId , p2mac.p2mId)
      this.sender() ! "Done"

    case p2mas : P2mApplicationSubmitted =>

      //todo arthur update product status
      drsCmdBus ! ChangeSkuStatusToApplying(p2mas.productId , p2mas.p2mId)
      drsCmdBus ! CreateUserTask(UserTaskType.ReviewP2MApplication ,p2mas.p2mId,p2mas.kcode)

    case r : P2mApplicationRejected =>

      //todo arthur update product status
      //todo arthur content
      val refUrl = "/p2m/p/a?i=" + Encryptor.encrypt(r.p2mId)
      val topic = "DRS rejected the P2M application."

      val doc = p2mDao.findById(r.p2mId)

      drsCmdBus ! SendNotification( topic , doc.get("name") + " is rejected.",refUrl ,r.createdBy)

      drsCmdBus! RemoveSkuApplyingStatus(r.productNameEN,r.p2mId)

    case a : P2mApplicationApproved =>

      //todo arthur content
      val refUrl = "/p2m/p/a?i=" + Encryptor.encrypt(a.p2mId)
      val topic = "DRS approved the P2M application."
      val doc = p2mDao.findById(a.p2mId)

      drsCmdBus ! SendNotification(topic , doc.get("name") + " is approved.",refUrl,a.createdBy)

      drsCmdBus ! ChangeSkuStatusToSelling(a.productId,a.p2mId)

      drsCmdBus ! SaveApprovedP2MProduct(a.productNameEN,a.p2mId)

    case atr : P2mApplicationApproveToRemove =>

      drsCmdBus ! RemoveSkuSellingStatus(atr.productNameEN,atr.p2mId)


    case message: Any =>
      log.info(s"P2MApplicationSubscriber: received unexpected: $message")



  }


}
