package com.kindminds.drs.core.actors.handlers.query.productCategory

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.api.message.query.productCategory.{GetListByParent, GetProductCategory}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.api.data.access.nosql.mongo.productCategory.ProductCategoryDao
import com.kindminds.drs.persist.data.access.nosql.mongo.productCategory.ProductCategoryDaoImpl


object ProductCategoryQueryHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new ProductCategoryQueryHandler(drsQueryBus))

}

class ProductCategoryQueryHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  val om = new ObjectMapper()//.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetProductCategory].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetListByParent].getName, self)

  val productCategoryDao = new ProductCategoryDaoImpl()

  override def receive: Receive = {

    case pc : GetProductCategory =>

      val result = productCategoryDao.findByParent(pc.parent);

      //println(result)

      this.sender() ! result

    case l : GetListByParent =>

      val result = productCategoryDao.findList(l.parent);

      //println(result)

      this.sender() ! result

    case message: Any =>
      log.info(s"ProductCategoryQueryHandler: received unexpected: $message")

  }

}
