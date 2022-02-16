package com.kindminds.drs.core.actors.handlers.query.supplier

import java.text.SimpleDateFormat

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.supplier.GetSupplierCodeList
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.api.data.access.rdb.CompanyDao




object SupplierViewHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new SupplierViewHandler(drsQueryBus))

}

class SupplierViewHandler ( drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetSupplierCodeList].getName, self)

  val om = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

  val companyDao = BizCoreCtx.get().getBean(classOf[CompanyDao])
    .asInstanceOf[CompanyDao]

  override def receive: Receive = {

    case kcode : GetSupplierCodeList =>

      val result = companyDao.querySupplierKcodeList

      val jResult = om.writeValueAsString(result)

      this.sender() ! jResult


  }
}
