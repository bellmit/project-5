package com.kindminds.drs.core.actors.handlers.event.product

import akka.actor.{Actor, ActorLogging, Props}
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx

import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.{ProductMarketingMaterialViewDao, ProductVariationViewDao}
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType


object ProductMarketingMaterialSubscriber {

  def props(): Props =
    Props(new ProductMarketingMaterialSubscriber())

}

class ProductMarketingMaterialSubscriber () extends Actor with ActorLogging {


  private val dao = BizCoreCtx.get.getBean(classOf[ProductMarketingMaterialViewDao])
    .asInstanceOf[ProductMarketingMaterialViewDao]


  private val pvDao = BizCoreCtx.get.getBean(classOf[ProductVariationViewDao])
    .asInstanceOf[ProductVariationViewDao]


  def receive = {

    case s : ProductMarketingMaterialStatusChanged =>

      dao.updateView(s.productMarketingMaterial ,s.status)

    case p : ProductMarketingMaterialSaved =>

      val pvViewList = pvDao.findView(p.productMarketingMaterial.getProductVariationId)

      if(pvViewList.size()> 0){
        val pv = pvViewList.get(0)
        val pmmViewList = dao.findView(p.productMarketingMaterial.getId)

        if(pmmViewList.size() == 0){
          dao.insertView(p.supplierKcode,p.productBaseCode,
            p.productMarketingMaterial, pv ,
            ProductEditingStatusType.PENDING_SUPPLIER_ACTION)
        }else{
          dao.updateView(p.productMarketingMaterial ,p.status)
        }

      }


      log.info("")

    case message: Any =>
      log.info(s"ProductSubscriber: received unexpected: $message")
  }

}
