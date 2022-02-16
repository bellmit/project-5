package com.kindminds.drs.core.actors.handlers.command.productCategory

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.command.productCategory.SaveProductCategory
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.{DrsCmdBus, RegisterCommandHandler}
import com.kindminds.drs.api.data.access.nosql.mongo.productCategory.ProductCategoryDao
import com.kindminds.drs.persist.data.access.nosql.mongo.productCategory.ProductCategoryDaoImpl

object ProductCategoryCmdHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new ProductCategoryCmdHandler(drsCmdBus))

}

class ProductCategoryCmdHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name


val om = new ObjectMapper()//.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

drsCmdBus ! RegisterCommandHandler(name, classOf[SaveProductCategory].getName, self)

val productCategoryDao = new ProductCategoryDaoImpl()

override def receive: Receive = {

  case spc : SaveProductCategory =>

    productCategoryDao.save(spc.productCategory)

    //todo arthur
    this.sender() ! "Done"

  case message: Any =>
    log.info(s"ProductCategoryCmdHandler: received unexpected: $message")

}

}
