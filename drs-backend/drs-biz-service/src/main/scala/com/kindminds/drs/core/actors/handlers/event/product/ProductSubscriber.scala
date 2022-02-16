package com.kindminds.drs.core.actors.handlers.event.product

import akka.actor.{Actor, ActorLogging, Props}
import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.ProductViewDao
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx




object ProductSubscriber {

  def props(): Props =
    Props(new ProductSubscriber())

}


class ProductSubscriber() extends Actor with ActorLogging {

  val productDao = BizCoreCtx.get().getBean(classOf[ProductViewDao]).asInstanceOf[ProductViewDao]

  def receive = {

    case pss : ProductsSaved =>

     // productDao.deleteView(pss.products.get(0).getId)
      pss.products.forEach(p=>{
        productDao.saveView(p)
      })


    case ps : ProductSaved =>

      //productDao.deleteView(ps.product.getId , ps.product.getMarketside)

      productDao.saveView(ps.product)

      log.info("")

    case message: Any =>
      log.info(s"ProductSubscriber: received unexpected: $message")
  }


}
