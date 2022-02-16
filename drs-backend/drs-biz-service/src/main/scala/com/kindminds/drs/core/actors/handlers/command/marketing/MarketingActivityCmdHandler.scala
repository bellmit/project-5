package com.kindminds.drs.core.actors.handlers.command.marketing

import java.text.SimpleDateFormat

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.command._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterCommandHandler
import com.kindminds.drs.api.data.access.nosql.mongo.ManageMarketingActivityDao
import com.kindminds.drs.persist.data.access.nosql.mongo.marketing.ManageMarketingActivityDaoImpl



object MarketingActivityCmdHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new MarketingActivityCmdHandler(drsCmdBus))

}

class MarketingActivityCmdHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name, classOf[CreateMarketingActivity].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[UpdateMarketingActivity].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[DeleteMarketingActivity].getName, self)


  val dao = new ManageMarketingActivityDaoImpl
  val om = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

  override def receive: Receive = {


    case create : CreateMarketingActivity =>

      val result = dao.create(create.activityJson)

      //println("createMarketingActivity: " + result.get_id.toString)

      this.sender() ! result.get_id.toString


    case upd : UpdateMarketingActivity =>

      val result = dao.update(upd.activityJson)

      //println("UpdateMarketingActivity id : " + result.get_id.toString)

      this.sender() ! result.get_id.toString

    case del : DeleteMarketingActivity =>

      val result = dao.delete(del.activityId)

      ////println("DeleteMarketingActivity: " + result)
      ////println("wasAcknowledged: " + result.wasAcknowledged)
      ////println("getDeletedCount: " + result.getDeletedCount)

      this.sender() ! result.getDeletedCount



    case message: Any =>
      log.info(s"MarketingActivityViewHandler: received unexpected: $message")
  }



}
