package com.kindminds.drs.core.actors.handlers.command.product

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.Country
import com.kindminds.drs.api.message.command.onboardingApplication._
import com.kindminds.drs.api.message.command.manageProduct.{TransformProductV2ToProductV1, TransformProductsV2ToProductV1}
import com.kindminds.drs.api.usecase.product.{LegacyProductUco, MaintainProductBaseUco, MaintainProductMarketplaceInfoUco, MaintainProductSkuUco}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterCommandHandler
import com.kindminds.drs.core.biz.repo.product.ProductVariationRepoImpl
import com.kindminds.drs.persist.TransformToProductV1



object TransformProductHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new TransformProductHandler(drsCmdBus))

}


class TransformProductHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {
  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[TransformProductV2ToProductV1].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[TransformProductsV2ToProductV1].getName ,self)

  val baseProductUco = BizCoreCtx.get().getBean(classOf[MaintainProductBaseUco]).asInstanceOf[MaintainProductBaseUco]
  val productSkuUco = BizCoreCtx.get().getBean(classOf[MaintainProductSkuUco]).asInstanceOf[MaintainProductSkuUco]
  val productMPUco = BizCoreCtx.get().getBean(classOf[MaintainProductMarketplaceInfoUco]).asInstanceOf[MaintainProductMarketplaceInfoUco]
  val legacyProductUco = BizCoreCtx.get().getBean(classOf[LegacyProductUco]).asInstanceOf[LegacyProductUco]

  def receive = {

    case pl: TransformProductsV2ToProductV1 =>
      log.info("MSG Received: TransformProductsV2ToProductV1")

      var p : com.kindminds.drs.api.v2.biz.domain.model.product.Product = null
      pl.product.forEach(x=>{
        //println(x.getMarketside)
        if(x.getMarketside() == Country.CORE){
          p  = x
        }
      })

      val bp = TransformToProductV1.transform(p)
      //println(p.getSupplierKcode)
      //println(bp.getSupplierKcode)
      //println(p.getProductBaseCode)

      val productId = baseProductUco.insert(bp, p.getProductBaseCode)

      //println("AAAAAAAAAAAA")
      //println(productId)
      //println(p.getProductVariations.size())

      val pvRepo = new ProductVariationRepoImpl()

      pl.product.forEach(y=>{
        if(y.getMarketside != Country.CORE){
          val variations = pvRepo.findByProductId(y.getId , y.getCreateTime , y.getMarketside)

          variations.forEach(pv=>{

            //println(pv.getId)
            //println(pv.getVariationCode)

            val sku = TransformToProductV1.transform(pv, p.getProductBaseCode, p.getSupplierKcode)

            //println(sku.getEanProvider)

            productSkuUco.insert(sku)

            //println(sku.getCodeByDrs)

            val mpInfo = TransformToProductV1.transform(sku , y.getMarketside)
            productMPUco.insert(mpInfo)

          })

        }
      })



      legacyProductUco.createProductRef(p, productId);

    case p: TransformProductV2ToProductV1 =>
      log.info("MSG Received: TransformProductToSettlementProduct")



      val bp = TransformToProductV1.transform(p.product)
      //println(p.product.getSupplierKcode)
      //println(bp.getSupplierKcode)
      //println(p.product.getProductBaseCode)

      val productId = baseProductUco.insert(bp, p.product.getProductBaseCode)

      //println("AAAAAAAAAAAA")
      //println(productId)
      //println(p.product.getProductVariations.size())

      val pvRepo = new ProductVariationRepoImpl()
      val variations = pvRepo.findByProductId(p.product.getId , p.product.getCreateTime , p.product.getMarketside)

      variations.forEach(pv=>{

        //println(pv.getId)
        //println(pv.getVariationCode)

        val sku = TransformToProductV1.transform(pv, p.product.getProductBaseCode, p.product.getSupplierKcode)
        productSkuUco.insert(sku)




        //get marketside variations to create product markeplace info

      })



      legacyProductUco.createProductRef(p.product, productId);

    case message: Any =>
      log.info(s"TransformProductHandler: received unexpected: $message")
  }
}
