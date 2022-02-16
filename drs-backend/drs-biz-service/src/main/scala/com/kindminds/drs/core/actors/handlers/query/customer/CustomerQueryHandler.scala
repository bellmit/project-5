package com.kindminds.drs.core.actors.handlers.query.customer

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.customer.GetReturns
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler

object CustomerQueryHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new CustomerQueryHandler(drsQueryBus))

}


class CustomerQueryHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetReturns].getName, self)

  val om = new ObjectMapper()

  val rDao = BizCoreCtx.get().getBean(classOf[
    com.kindminds.drs.api.data.access.rdb.customer.ReturnDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.rdb.customer.ReturnDao]

  override def receive: Receive = {

    case r : GetReturns =>

      //println(r.companyKcode)
      val list = rDao.getReturns(r.companyKcode.getOrElse("All") , r.marketPlace.getOrElse(null) ,
        r.productBaseCode.getOrElse(null)
        , r.productSkuCode.getOrElse(null))

      val s = om.writeValueAsString(list)

      this.sender() ! s

  }

}