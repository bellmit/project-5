package com.kindminds.drs.core.actors.handlers.command.product


import java.util
import java.util.List
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.kindminds.drs.api.message.command.manageProduct._
import com.kindminds.drs.api.message.command.product.{VerifyEANCode, VerifySku}
import com.kindminds.drs.api.usecase.product.{MaintainProductBaseUco, MaintainProductMarketplaceInfoUco, MaintainProductSkuUco}
import com.kindminds.drs.api.v1.model.product.SKU.Status
import com.kindminds.drs.api.v1.model.product.{BaseProduct, SKU}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao
import com.kindminds.drs.persist.v1.model.mapping.product.{ProductBaseImpl, ProductMarketplaceInfoImpl, ProductSkuImpl}
import org.bson.Document

import java.math.BigDecimal



object ManageProductCmdHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new ManageProductCmdHandler(drsCmdBus ,drsEventBus))

}

class ManageProductCmdHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[CreateBaseProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[UpdateBaseProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[DeleteBaseProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[SaveApprovedP2MProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[ChangeSkuStatusToApplying].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[ChangeSkuStatusToSelling].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RemoveSkuApplyingStatus].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RemoveSkuSellingStatus].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[VerifyEANCode].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[VerifySku].getName,self)

  val productDao = new ProductDao
  val p2mDao = new P2MApplicationDao
  val om = new ObjectMapper()//.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

  val baseProductUco = BizCoreCtx.get().getBean(classOf[MaintainProductBaseUco]).asInstanceOf[MaintainProductBaseUco]

  val productSkuUco = BizCoreCtx.get().getBean(classOf[MaintainProductSkuUco]).asInstanceOf[MaintainProductSkuUco]

  val productMarketplaceInfoUco = BizCoreCtx.get().getBean(classOf[MaintainProductMarketplaceInfoUco]).asInstanceOf[MaintainProductMarketplaceInfoUco]

//  val productBaseImpl =  new ProductBaseImpl()
//
//  val productSkuImpl = new ProductSkuImpl()

  private def createApplicationName(kcode:String ,serialNum:Int) : String = {

    "BP-" + kcode + "-" + serialNum
  }


  override def receive: Receive = {

    case crt : CreateBaseProduct =>

      val doc = Document.parse(crt.productJson)

      val timestamp = System.currentTimeMillis

      doc.put("supplierId", crt.kcode)

      val serialNum = productDao.findMaxSerialNumber(crt.kcode) + 1

      doc.put("serial_num", serialNum)

      doc.put("baseCode", createApplicationName(crt.kcode , serialNum))

      doc.append("createdDateTime",timestamp)

      val skus: util.List[Document] = doc.get("skus").asInstanceOf[util.List[Document]]
      skus.forEach(s=>{
        var productId = s.get("productId").toString
//        var ean =""
//        printf(productId.get(1).toString)
//        productId.forEach(p=>{
//          ean = p.get("value").toString
//
//
//        })
//        printf(ean)
        productId=productId.replace("Document{{name=Product ID, value=","")
        productId=productId.replace("}}","")
        //println(productId)
        //println("111111")
      })

      this.productDao.createProduct(doc)

      this.sender() ! "done"

    case upd : UpdateBaseProduct =>

      val doc = Document.parse(upd.productJson)

      doc.append("updatedDateTime",System.currentTimeMillis())

      val productJson = doc.toJson

      this.productDao.updateProduct(upd.productId, productJson)



      this.sender() ! "done"

    case del : DeleteBaseProduct =>

      this.productDao.deleteProduct(del.kcode, del.productId)

      this.sender() ! "done"

    case sap : SaveApprovedP2MProduct =>

      val doc =  this.productDao.findByProductNameEN(sap.productNameEN)

      val docc = p2mDao.findById(sap.p2mId)

      val marketplace = docc.get("selectedPlatform").toString

      var marketplaceId = 0

      var currency = ""

      if(marketplace.equals("Amazon.com")){
        marketplaceId = 1
        currency = "USD"
      }else if(marketplace.equals("TrueToSource")){
        marketplaceId = 2
        currency = "USD"
      }else if(marketplace.equals("Amazon.co.uk")){
        marketplaceId = 4
        currency = "GBP"
      }else if(marketplace.equals("Amazon.ca")){
        marketplaceId = 5
        currency = "CAD"
      }else if(marketplace.equals("Amazon.de")){
        marketplaceId = 6
        currency = "EUR"
      }else if(marketplace.equals("Amazon.fr")){
        marketplaceId = 7
        currency = "EUR"
      }else if(marketplace.equals("Amazon.it")){
        marketplaceId = 8
        currency = "EUR"
      }else if(marketplace.equals("Amazon.es")){
        marketplaceId = 9
        currency = "EUR"
      }

      val supplierKode = doc.get("supplierId").toString

//      val serialNum = doc.get("serial_num").toString

      val baseCode = doc.get("baseCode").toString

      val codeBySupplier = baseCode

      val nameBySupplier = sap.productNameEN

      val product = new ProductBaseImpl(supplierKode, null, codeBySupplier, baseCode, nameBySupplier, "", null)

      if (this.baseProductUco.getBaseProduct(baseCode) == null) {
        this.baseProductUco.insert(product,baseCode)
      }else{
        this.baseProductUco.updateBaseProduct(product)
      }

      val appliedSkuCode =  docc.get("appliedSkuCode").asInstanceOf[util.List[String]]

      val skus: util.List[Document] = doc.get("skus").asInstanceOf[util.List[Document]]



      skus.forEach(s=>{

        var skuCode = supplierKode + "-" + s.get("sellerSku").toString

        val codeBySupplier = s.get("sellerSku").toString

        val nameBySupplier = sap.productNameEN

        val product = s.get("productId").asInstanceOf[Document]

        val ean = product.get("value").toString

        val bigDecimal = new BigDecimal("0")

        val sku = new ProductSkuImpl(supplierKode, baseCode, codeBySupplier, skuCode, nameBySupplier, nameBySupplier, ean, "SUPPLIER", "SKU_DRAFT", "30", false, "")

        if (this.productSkuUco.getProductSku(skuCode) == null) {
          this.productSkuUco.insert(sku,skuCode)
        }


        appliedSkuCode.forEach(a=>{

          if(a.equals(s.get("sellerSku").toString)){

            if (this.productSkuUco.getProductSku(skuCode).getStatus.equals("SKU_DRAFT")){
              val sku = new ProductSkuImpl(supplierKode, baseCode, codeBySupplier, skuCode, nameBySupplier, nameBySupplier, ean, "SUPPLIER", "SKU_ACTIVE", "30", false, "")

              this.productSkuUco.updateSku(sku)
            }

            if (marketplaceId == 5){
              skuCode = skuCode  + "-" + "CA" ;
            }

            val productMarketplaceInfo = new ProductMarketplaceInfoImpl(skuCode, nameBySupplier, marketplaceId, skuCode, currency, "Live", bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal)

            if (this.productMarketplaceInfoUco.getProductMarketplace(skuCode,marketplaceId) == null) {
              this.productMarketplaceInfoUco.insert(productMarketplaceInfo)
            }
          }



        })

      })


//      skus.forEach(s=>{
//        val skuCode = supplierKode + "-" + serialNum + "-" + s.get("sellerSku").toString
//
//        val nameBySupplier = s.get("sellerSku").toString
//
//        val bigDecimal = new BigDecimal("0")
//
//        val productMarketplaceInfo = new ProductMarketplaceInfoImpl(skuCode, nameBySupplier, marketplaceId, skuCode, currency, "Live", bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal)
//
//        this.productMarketplaceInfoUco.insert(productMarketplaceInfo)
//
//
//      })

      this.sender() ! "done"

    case chSt : ChangeSkuStatusToApplying =>
      val p2mDoc =  this.p2mDao.findById(chSt.p2mId)
      val skuCodes =  p2mDoc.get("appliedSkuCode").asInstanceOf[util.List[String]]
      val marketplace = p2mDoc.get("selectedPlatform").toString
      try {
        val doc =  this.productDao.findById(chSt.productId)
        val skus = doc.get("skus").asInstanceOf[util.List[Document]]
        skus.forEach(s=>{
          if(skuCodes.contains(s.get("sellerSku"))){
            val applyingMp = s.get("applying").asInstanceOf[util.List[String]]
            if (!applyingMp.contains(marketplace)) applyingMp.add(marketplace)
            s.put("applying", applyingMp)
          }

        })

        doc.put("bpStatus" ,"applied")
        this.productDao.updateProduct(chSt.productId,doc.toJson())

      } catch {
        case i: IllegalArgumentException => println("Couldn't find that productId.")
      }





    case vec :VerifyEANCode =>
      val productId = vec.ean
      val sku = this.productDao.findSkuByEAN(productId)
      val result = sku.equals("")
      //System.out.//println(result)
      this.sender() ! result

    case vs :VerifySku =>
      val skuCode = vs.sku
      val sku = this.productDao.findBySku(skuCode)
      val result = sku.equals("")
      //System.out.//println(result)
      this.sender() ! result

    case chStSelling : ChangeSkuStatusToSelling =>

      val p2mDoc =  this.p2mDao.findById(chStSelling.p2mId)
      val skuCodes =  p2mDoc.get("appliedSkuCode").asInstanceOf[List[String]]
      val marketplace = p2mDoc.get("selectedPlatform").toString

      val doc =  this.productDao.findById(chStSelling.productId)

      val skus: util.List[Document] = doc.get("skus").asInstanceOf[util.List[Document]]
      skus.forEach(s=>{
        if(skuCodes.contains(s.get("sellerSku"))){
          val sellingMp: util.List[String] = s.get("selling").asInstanceOf[util.List[String]]
          val applyingMp: util.List[String] = s.get("applying").asInstanceOf[util.List[String]]
          if (!sellingMp.contains(marketplace)){
            sellingMp.add(marketplace)
          }
          if (applyingMp.contains(marketplace)){
            applyingMp.remove(marketplace)
          }
          s.put("selling", sellingMp)
          s.put("applying",applyingMp)
        }

      })

      this.productDao.updateProduct(chStSelling.productId,doc.toJson())

    case reStApplying : RemoveSkuApplyingStatus =>

      val p2mDoc =  this.p2mDao.findById(reStApplying.p2mId)
      val appliedSkuCode =  p2mDoc.get("appliedSkuCode").asInstanceOf[List[String]]
      val marketplace = p2mDoc.get("selectedPlatform").toString
      val pdoc = productDao.findByProductNameEN(reStApplying.productNameEN)
      val skus: util.List[Document] = pdoc.get("skus").asInstanceOf[util.List[Document]]
      skus.forEach(s=>{
        if(appliedSkuCode.contains(s.get("sellerSku"))){
          val applyingMp: util.List[String] = s.get("applying").asInstanceOf[util.List[String]]
          if (applyingMp.contains(marketplace)){
            applyingMp.remove(marketplace)
          }
          s.put("applying",applyingMp)
        }
      })

      this.productDao.updateProduct(pdoc)

    case reStSelling : RemoveSkuSellingStatus =>

      val p2mDoc =  this.p2mDao.findById(reStSelling.p2mId)
      val appliedSkuCode =  p2mDoc.get("appliedSkuCode").asInstanceOf[List[String]]
      val marketplace = p2mDoc.get("selectedPlatform").toString
      val pdoc = productDao.findByProductNameEN(reStSelling.productNameEN)
      val skus: util.List[Document] = pdoc.get("skus").asInstanceOf[util.List[Document]]

      skus.forEach(s=>{
        appliedSkuCode.forEach(a=>{
          if(a.equals(s.get("sellerSku").toString)){
            val selling =  s.get("selling").asInstanceOf[util.List[String]]
            if(selling.contains(marketplace)){
              selling.remove(marketplace)
            }
            s.append("selling",selling)
          }
        })
      })

      this.productDao.updateProduct(pdoc)



    case message: Any =>
      log.info(s"ManageProductHandler: received unexpected: $message")

  }



}
