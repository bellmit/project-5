package com.kindminds.drs.core.actors.handlers.query.marketing

import java.text.SimpleDateFormat

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.Marketplace
import com.kindminds.drs.api.message.query.marketing._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.api.data.access.nosql.mongo.ManageMarketingActivityDao
import com.kindminds.drs.api.data.access.rdb.CompanyDao
import com.kindminds.drs.persist.data.access.nosql.mongo.dto.MarketingActivityImpl
import com.kindminds.drs.persist.data.access.nosql.mongo.marketing.ManageMarketingActivityDaoImpl


object MarketingActivityQueryHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new MarketingActivityQueryHandler(drsQueryBus))

}

class MarketingActivityQueryHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetMarketingActivityList].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetMarketingActivityByFilters].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetMarketingActivityById].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetMarketplacesAndIds].getName, self)


  val dao = new ManageMarketingActivityDaoImpl

  val om = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

  override def receive: Receive = {

    case mal :  GetMarketingActivityList =>

      val result = dao.find(mal.pageIndex)

      val jResult = om.writeValueAsString(result)

      ////println(jResult)

      this.sender() ! jResult


    case filter : GetMarketingActivityByFilters =>

      val result = dao.find(filter.country, filter.skuCode,
        filter.startDate, filter.endDate, filter.pageIndex)

      val jResult = om.writeValueAsString(result)

      //println(jResult)

      this.sender() ! jResult

    case aid : GetMarketingActivityById =>

      val result = dao.findActivityById(aid.activityId)

      val jResult = om.writeValueAsString(result)

      //println(jResult)

      this.sender() ! jResult

    case mpId : GetMarketplacesAndIds =>

      val result = Marketplace.getCountryToMarketplaceIdMap

      val jResult = om.writeValueAsString(result)

      //println(jResult)

      this.sender() ! jResult


    case message: Any =>
      log.info(s"MarketingActivityViewHandler: received unexpected: $message")
  }



}
