package com.kindminds.drs.core.actors.handlers.event.product

import akka.actor.{Actor, ActorLogging, Props}
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx

import com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.{MarketingMaterialEditingRequestViewDao, ProductVariationViewDao}



object EditingMarketingMaterialRequestSubscriber {

  def props(): Props =
    Props(new EditingMarketingMaterialRequestSubscriber())

}

class EditingMarketingMaterialRequestSubscriber() extends Actor with ActorLogging {

  private val dao = BizCoreCtx.get.getBean(classOf[MarketingMaterialEditingRequestViewDao])
    .asInstanceOf[MarketingMaterialEditingRequestViewDao]

  private val pvViewDao = BizCoreCtx.get.getBean(classOf[ProductVariationViewDao])
    .asInstanceOf[ProductVariationViewDao]

  def receive = {


    case request : MarketingMaterialEditingRequestSaved =>


      val pvViewList = pvViewDao.findView(request.productMarketingMaterial.getProductVariationId)

      if(pvViewList.size()>0){
        val pv = pvViewList.get(0)
        val reqViewList = dao.findView(request.marketingMaterialEditingRequest.getId)

        if(reqViewList.size() == 0){
          dao.insertView(request.supplierKcode ,request.marketingMaterialEditingRequest,pv)
        }else{
          dao.updateView(request.marketingMaterialEditingRequest)
        }

      }

      log.info("")

    case message: Any =>
      log.info(s"EditingMarketingMaterialRequestSubscriber: received unexpected: $message")
  }


}
