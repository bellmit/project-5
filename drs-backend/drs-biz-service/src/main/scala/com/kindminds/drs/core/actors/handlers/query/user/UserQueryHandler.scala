package com.kindminds.drs.core.actors.handlers.query.user

import java.text.SimpleDateFormat

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.supplier.GetSupplierCodeList
import com.kindminds.drs.api.message.query.user.GetUserRoles
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.core.actors.handlers.query.supplier.SupplierViewHandler
import com.kindminds.drs.api.data.access.rdb.UserDao



object UserQueryHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new UserQueryHandler(drsQueryBus))

}

class UserQueryHandler ( drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetUserRoles].getName, self)

  val om = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

  val userDao = BizCoreCtx.get().getBean(classOf[UserDao])
    .asInstanceOf[UserDao]

  override def receive: Receive = {

    case u : GetUserRoles =>

      val result = userDao.getUserRoles(u.userName)

      val jResult = om.writeValueAsString(result)

      this.sender() ! jResult


  }
}
