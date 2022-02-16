package com.kindminds.drs.core.actors.handlers.command.product

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.api.message.command.onboardingApplication._
import com.kindminds.drs.core.biz.product.MarketingMaterialEditingRequestImpl
import com.kindminds.drs.core.biz.repo.product.{MarketingMaterialEditingRequestRepoImpl, ProductMarketingMaterialRepoImpl}
import com.kindminds.drs.core.biz.repo._
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.v2.biz.domain.model.product.MarketingMaterialEditingRequest




object RequestEditingMarketingMaterialHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new RequestEditingMarketingMaterialHandler(drsCmdBus ,drsEventBus))

}

class RequestEditingMarketingMaterialHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {



  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[SubmitMarketingMaterialEditingRequest].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[ApproveMarketingMaterialEditingRequest].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RejectMarketingMaterialEditingRequest].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[SaveMarketingMaterialEditingRequest].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CreateMarketingMaterialEditingRequests].getName ,self)

  val repo =  new MarketingMaterialEditingRequestRepoImpl

  val productMarketingMaterialRepo =  new ProductMarketingMaterialRepoImpl


  def receive = {

    case r : RejectMarketingMaterialEditingRequest =>

      val pmm = productMarketingMaterialRepo.findById(r.productMarketingMaterialId).get()
      pmm.update(r.productMarketingMaterialSource)
      drsCmdBus ! RejectProductMarketingMaterial(pmm)

      val opReq = repo.findById(r.productMarketingMaterialId)
      val mmeRequest = opReq.get()
      mmeRequest.update(pmm)
      mmeRequest.reject()
      repo.add(mmeRequest)

      drsEventBus.publish(MarketingMaterialEditingRequestSaved(r.supplierKcode ,
        r.productBaseCode, mmeRequest,pmm))

    case a : ApproveMarketingMaterialEditingRequest =>

      val pmm = productMarketingMaterialRepo.findById(a.productMarketingMaterialId).get()
      pmm.update(a.productMarketingMaterialSource)
      drsCmdBus ! ApproveProductMarketingMaterial(pmm)

      val opReq = repo.findById(a.productMarketingMaterialId)
      val mmeRequest = opReq.get()
      mmeRequest.update(pmm)
      mmeRequest.approve()
      repo.add(mmeRequest)

      drsEventBus.publish(MarketingMaterialEditingRequestSaved(a.supplierKcode ,
        a.productBaseCode, mmeRequest,pmm))

    case sb : SubmitMarketingMaterialEditingRequest =>


      val pmm = productMarketingMaterialRepo.findById(sb.productMarketingMaterialId).get()
      pmm.update(sb.productMarketingMaterialSource)
      drsCmdBus ! SubmitProductMarketingMaterial(pmm)

      val opReq = repo.findById(sb.productMarketingMaterialId)
      var mmeRequest : MarketingMaterialEditingRequest  = null
      if(opReq.isPresent){
        mmeRequest = opReq.get()
        mmeRequest.update(pmm)
      }else {
        mmeRequest = MarketingMaterialEditingRequestImpl.createRequest(pmm)
      }


      mmeRequest.submit()
      repo.add(mmeRequest)

      drsEventBus.publish(MarketingMaterialEditingRequestSaved(sb.supplierKcode ,
        sb.productBaseCode, mmeRequest,pmm))

    case c : CreateMarketingMaterialEditingRequests =>

      c.products.forEach(p =>{
          p.getProductVariations.forEach(v=>{
            v.getProductMarketingMaterials.forEach(m=>{
              val mmeRequest =  MarketingMaterialEditingRequestImpl.createRequest(m)
              repo.add(mmeRequest)

              drsEventBus.publish(MarketingMaterialEditingRequestSaved(p.getSupplierKcode ,
                p.getProductBaseCode, mmeRequest , m))
            })
          })
        }
      )


    case s : SaveMarketingMaterialEditingRequest =>

      val pmm = productMarketingMaterialRepo.findById(s.productMarketingMaterialId).get()
      pmm.update(s.productMarketingMaterialSource)

      val opReq = repo.findById(s.productMarketingMaterialId)
      var mmeRequest : MarketingMaterialEditingRequest  = null
      if(opReq.isPresent){
        mmeRequest = opReq.get()
        mmeRequest.update(pmm)
      }else {
        mmeRequest = MarketingMaterialEditingRequestImpl.createRequest(s.marketSide , pmm)
      }


      drsCmdBus ! SaveProductMarketingMaterial(s.supplierKcode,
        s.productBaseCode, s.marketSide,pmm , mmeRequest.getStatus)


      repo.add(mmeRequest)

      drsEventBus.publish(MarketingMaterialEditingRequestSaved(s.supplierKcode ,
        s.productBaseCode, mmeRequest,pmm))


    case message: Any =>
      log.info(s"RequestEditingMarketingMaterialHandler: received unexpected: $message")
  }

}
