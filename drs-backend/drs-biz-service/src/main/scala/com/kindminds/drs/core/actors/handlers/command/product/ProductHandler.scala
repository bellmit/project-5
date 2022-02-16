package com.kindminds.drs.core.actors.handlers.command.product

import java.io.IOException
import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.kindminds.drs.Country
import com.kindminds.drs.api.data.access.rdb.CompanyDao
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.api.message.command.onboardingApplication._
import com.kindminds.drs.api.message.command.manageProduct.{ApproveProduct => _, RejectProduct => _, SaveProduct => _, SubmitProduct => _, _}
import com.kindminds.drs.api.message.command.product._
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.biz.product.ProductImpl
import com.kindminds.drs.api.v2.biz.domain.model.product.Product
import com.kindminds.drs.core.biz.product.onboarding.{OnboardingApplicationCreator, OnboardingApplicationImpl}
import com.kindminds.drs.core.biz.repo.product.{ProductRepoImpl, ProductVariationRepoImpl}
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import org.springframework.util.Assert





object ProductHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new ProductHandler(drsCmdBus ,drsEventBus))

}

class ProductHandler (drsCmdBus: ActorRef ,drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {

  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[SaveProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[SaveProducts].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[SubmitProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[ApproveProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RejectProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CreateProduct].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[TransformProduct].getName ,self)

  drsCmdBus ! RegisterCommandHandler(name,classOf[com.kindminds.drs.api.message.command.
  manageProduct.SubmitProduct].getName ,self)

  drsCmdBus ! RegisterCommandHandler(name,classOf[com.kindminds.drs.api.message.command.
  manageProduct.SaveProduct].getName ,self)

  drsCmdBus ! RegisterCommandHandler(name,classOf[com.kindminds.drs.api.message.command.
  manageProduct.ApproveProduct].getName ,self)


  drsCmdBus ! RegisterCommandHandler(name,classOf[com.kindminds.drs.api.message.command.
  manageProduct.RejectProduct].getName ,self)

  drsCmdBus ! RegisterCommandHandler(name,classOf[MigrateProducts].getName ,self)

  val CompanyDao = BizCoreCtx.get().getBean(classOf[CompanyDao])
    .asInstanceOf[CompanyDao]


  def receive = {

    case c : CreateProduct =>
      processCreateProduct(c)

    case sp : com.kindminds.drs.api.message.command.manageProduct.SaveProduct =>

      processSaveProduct(sp)

    case sb : com.kindminds.drs.api.message.command.manageProduct.SubmitProduct =>
      processSubmitProduct(sb)

    case a : com.kindminds.drs.api.message.command.manageProduct.ApproveProduct =>
      processApproveProduct(a)

    case r : com.kindminds.drs.api.message.command.manageProduct.RejectProduct =>
      processRejectProduct(r)

    case r : RejectProduct =>
      val repo = new ProductRepoImpl
      r.product.edit(ProductEditingStatusType.PENDING_SUPPLIER_ACTION)
      repo.add(r.product)

      drsEventBus.publish(ProductSaved(r.product))

    case a : ApproveProduct =>

      val repo = new ProductRepoImpl
      a.product.edit(ProductEditingStatusType.FINALIZED)
      repo.add(a.product)

      drsEventBus.publish(ProductSaved(a.product))

      drsCmdBus ! TransformProductV2ToProductV1(a.product);

    case sb : SubmitProduct =>

      val repo = new ProductRepoImpl
      sb.product.edit(ProductEditingStatusType.PENDING_DRS_REVIEW)
      repo.add(sb.product)

      drsEventBus.publish(ProductSaved(sb.product))

    case sps : SaveProducts =>
      val repo = new ProductRepoImpl

      sps.products.forEach(p =>{
        p.edit(ProductEditingStatusType.PENDING_SUPPLIER_ACTION)
        repo.add(p)
        drsEventBus.publish(ProductSaved(p))
      })


      drsCmdBus ! CreateMarketingMaterialEditingRequests(sps.products)


    case s : SaveProduct =>

      val repo = new ProductRepoImpl

      val p = repo.find(s.product.getId , s.product.getMarketside)

      if(!p.isPresent){
        s.product.edit(ProductEditingStatusType.PENDING_SUPPLIER_ACTION)
      }else{
        s.product.edit(p.get().getEditingStatus)
      }

      repo.add(s.product)

      drsEventBus.publish(ProductSaved(s.product))

      log.info("Save Product " + s.product.getId)



    case mps : MigrateProducts =>

      val repo = new ProductRepoImpl
      mps.products.forEach(p =>{
        repo.add(p)
        drsEventBus.publish(ProductSaved(p))
      })


      drsCmdBus ! CreateMarketingMaterialEditingRequests(mps.products)

    case t : TransformProduct =>

      val repo = new ProductRepoImpl

      val pList = repo.findAllMarketSide(t.productId)

      drsCmdBus ! TransformProductsV2ToProductV1(pList)

    case message: Any =>
      log.info(s"ProductHandler: received unexpected: $message")
  }



  def processCreateProduct(s : CreateProduct) ={


    val objectMapper: ObjectMapper = new ObjectMapper
    val productInfoSourceJsonNode: JsonNode = objectMapper.readValue(s.productInfoSource, classOf[JsonNode])
    val bpCode: String = productInfoSourceJsonNode.get("baseProductCode").asText
    var supplierKcode = productInfoSourceJsonNode.get("supplierKcode").asText


    if (s.isDrsUser) Assert.notNull(supplierKcode)
    else {
      Assert.isTrue(this.CompanyDao.isSupplier(s.userKcode))
      supplierKcode = s.userKcode
    }


    val products = this.createProducts(supplierKcode, bpCode , Seq(s.productInfoSource,
      s.productMarketingMaterialSource,s.productInfoMarketSide,s.productMarketingMaterialMarketSide))

    //todo need refactor
    val repo = new ProductRepoImpl

    products.forEach(p=>{
      val op = repo.find(p.getId,p.getMarketside)
      if(p.getMarketside == Country.CORE || !op.isPresent){
        drsCmdBus ! SaveProduct(p)
      } else{

        val existedProd = op.get()
        var addNewVar = false
        p.getProductVariations.forEach(v=>{
          val isExisted = existedProd.getProductVariations.contains(v.getId)

          if(!isExisted){
           existedProd.getProductVariations.add(v)
            addNewVar  = true
          }
        })

        if(addNewVar){
          existedProd.clone(p)
          drsCmdBus ! SaveProduct(existedProd)
        }
      }
    })

    


    //drsCmdBus ! SaveProducts(products)



  }


  private def createProducts(supplierKcode : String, bpCode :String, productRawData : Seq[String]): util.List[Product] = {

    val productList = new util.ArrayList[Product]
    val om = new ObjectMapper

    try {
      val pNode = om.readValue(productRawData(0), classOf[JsonNode])
      val pmNode = om.readValue(productRawData(1), classOf[JsonNode])

      val coreProdcut = ProductImpl.createCoreProduct(supplierKcode, bpCode ,pNode, pmNode)
      productList.add(coreProdcut)

      val applicableRegionBP = pNode.get("applicableRegionBP")
      if (applicableRegionBP.isArray) if (applicableRegionBP.size > 0) {
        val pmsNode = om.readValue(productRawData(2), classOf[JsonNode])
        val pmsmNode = om.readValue(productRawData(3), classOf[JsonNode])

        applicableRegionBP.forEach(x=>{
          import scala.util.control.Breaks._

          breakable { for (i <- 0 to pmsNode.size() -1) {

            val marketSide =  pmsNode.get(i).get("country").asText()
            if (marketSide == x.asText()) {
              var marketingJsonNode : JsonNode = null
              breakable { for (j <- 0 to pmsmNode.size() -1) {
                if (pmsmNode.get(j).get("country").asText == marketSide) {
                  marketingJsonNode = pmsmNode.get(j)
                  break
                }}
              }


              val marketSideJsonNode = om.readValue(pmsNode.get(i).get("jsonData").asText() ,
                classOf[JsonNode])

              val msProd = ProductImpl.createMarketSideProduct(supplierKcode, bpCode,
                coreProdcut.getId, coreProdcut.getCreateTime,
                marketSideJsonNode , marketingJsonNode, Country.valueOf(marketSide))

          
              productList.add(msProd)

              break
            }
          } }

        })


      }
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }

    productList

  }



  private def createCoreProduct(supplierKcode : String, bpCode :String , prodcutRawData : Seq[String]
                           ): Product = {

    val om = new ObjectMapper
    var pNode : JsonNode = null
    var pmNode : JsonNode = null

    try {
      pNode  = om.readValue(prodcutRawData(0), classOf[JsonNode])
      pmNode = om.readValue(prodcutRawData(1), classOf[JsonNode])

    } catch {
      case e: IOException =>
        e.printStackTrace()
    }

    ProductImpl.createCoreProduct(supplierKcode, bpCode ,pNode, pmNode)

  }


  private def createProduct(supplierKcode : String, bpCode :String , marketSide: Country ,
                            prodcutRawData : String
                           ): Product = {

    val om = new ObjectMapper
    var pNode : JsonNode = null


    try {
      pNode  = om.readValue(prodcutRawData, classOf[JsonNode])

    } catch {
      case e: IOException =>
        e.printStackTrace()
    }

    ProductImpl.valueOf(supplierKcode, bpCode , marketSide ,pNode)

  }



  def processSaveProduct(s : com.kindminds.drs.api.message.command.manageProduct.SaveProduct) ={


    val objectMapper: ObjectMapper = new ObjectMapper

    var bpCode: String =  ""
    var supplierKcode: String =  ""


    //println("Aaaaa")
    //println(s.productBaseCode)

    if(s.marketSide == Country.CORE){
      val productInfoSourceJsonNode: JsonNode = objectMapper.readValue(s.source.core.get,
        classOf[JsonNode])
      bpCode = productInfoSourceJsonNode.get("baseProductCode").asText
      supplierKcode = productInfoSourceJsonNode.get("supplierKcode").asText

    }else{
      bpCode = s.productBaseCode.get
      supplierKcode = s.supplierKcode.get
    }


    if (s.isDrsUser)
      Assert.notNull(supplierKcode)
    else {
      Assert.isTrue(this.CompanyDao.isSupplier(s.userKcode))
      supplierKcode = s.userKcode
    }

    //todo need refactor

    if(s.marketSide == Country.CORE){
      //product = this.createProduct(supplierKcode, bpCode ,
       // Seq(s.source.core.get, s.source.coreMarketingMaterial.get))

      val products = this.createProducts(supplierKcode, bpCode , Seq(s.source.core.get,
        s.source.coreMarketingMaterial.get,s.source.marketSide.get,s.source.marketSideMarketingMaterial.get))

      //todo need refactor
      val repo = new ProductRepoImpl

      products.forEach(p=>{
        val op = repo.find(p.getId,p.getMarketside)
        if(p.getMarketside == Country.CORE || !op.isPresent){
          drsCmdBus ! SaveProduct(p)
        } else{

          val existedProd = op.get()
          var addNewVar = false
          p.getProductVariations.forEach(v=>{
            val isExisted = existedProd.getProductVariations.contains(v.getId)

            if(!isExisted){
              existedProd.getProductVariations.add(v)
              addNewVar  = true
            }
          })

          if(addNewVar){
            existedProd.clone(p)
            drsCmdBus ! SaveProduct(existedProd)
          }
        }
      })

    }else{

      val product = this.createProduct(supplierKcode, bpCode , s.marketSide , s.source.marketSide.get)

      drsCmdBus ! SaveProduct(product)

    }



  }



  def processSubmitProduct(sb : com.kindminds.drs.api.message.command.manageProduct.SubmitProduct) ={
    try{

      val objectMapper: ObjectMapper = new ObjectMapper
      var bpCode: String =  ""
      var supplierKcode: String =  ""


      if(sb.marketSide == Country.CORE){
        val productInfoSourceJsonNode: JsonNode = objectMapper.readValue(sb.source.core.get,
          classOf[JsonNode])
        bpCode = productInfoSourceJsonNode.get("baseProductCode").asText
        supplierKcode = productInfoSourceJsonNode.get("supplierKcode").asText
      }else{
        bpCode = sb.productBaseCode.get
        supplierKcode = sb.supplierKcode.get
      }


      if (sb.isDrsUser) Assert.notNull(supplierKcode)
      else {
        Assert.isTrue(this.CompanyDao.isSupplier(sb.userKcode))
        supplierKcode = sb.userKcode
      }


      var product : Product = null
      if(sb.marketSide == Country.CORE){

        val products = this.createProducts(supplierKcode, bpCode , Seq(sb.source.core.get,
          sb.source.coreMarketingMaterial.get,sb.source.marketSide.get,
          sb.source.marketSideMarketingMaterial.get))

        val repo = new ProductRepoImpl

        products.forEach(p=>{
          val op = repo.find(p.getId,p.getMarketside)
          if(p.getMarketside == Country.CORE || !op.isPresent){
            drsCmdBus ! SaveProduct(p)
          } else{

            val existedProd = op.get()
            var addNewVar = false
            p.getProductVariations.forEach(v=>{
              val isExisted = existedProd.getProductVariations.contains(v.getId)

              if(!isExisted){
                existedProd.getProductVariations.add(v)
                addNewVar  = true
              }
            })

            if(addNewVar){
              existedProd.clone(p)
              drsCmdBus ! SaveProduct(existedProd)
            }
          }
        })


        product = this.createCoreProduct(supplierKcode, bpCode ,
          Seq(sb.source.core.get, sb.source.coreMarketingMaterial.get))


      }else{
        product = this.createProduct(supplierKcode, bpCode ,sb.marketSide
          , sb.source.marketSide.get)

      }

      drsCmdBus ! SubmitProduct(product)



    } catch {
      case  e : Exception => {
        log.error(e.getStackTraceString)
      }

    }


  }

  def processApproveProduct(a : com.kindminds.drs.api.message.command.manageProduct.ApproveProduct) ={


    val objectMapper: ObjectMapper = new ObjectMapper

    var bpCode: String =  ""
    var supplierKcode: String =  ""
    var product : Product = null
    if(a.marketSide == Country.CORE){

      val productInfoSourceJsonNode: JsonNode = objectMapper.readValue(a.source.core.get,
        classOf[JsonNode])

      bpCode  = productInfoSourceJsonNode.get("baseProductCode").asText
      supplierKcode = productInfoSourceJsonNode.get("supplierKcode").asText
      product = this.createCoreProduct(supplierKcode, bpCode ,
        Seq(a.source.core.get, a.source.coreMarketingMaterial.get))

    }else{

      bpCode  = a.productBaseCode.get
      supplierKcode = a.supplierKcode.get
      product = this.createProduct(supplierKcode, bpCode ,a.marketSide, a.source.marketSide.get)

    }


    drsCmdBus ! ApproveProduct(product)



  }


  def processRejectProduct(r : com.kindminds.drs.api.message.command.manageProduct.RejectProduct) ={


    val objectMapper: ObjectMapper = new ObjectMapper
    var bpCode: String =  ""
    var supplierKcode: String =  ""
    var product : Product = null
    if(r.marketSide == Country.CORE){

      val productInfoSourceJsonNode: JsonNode = objectMapper.readValue(r.source.core.get,
        classOf[JsonNode])

      bpCode  = productInfoSourceJsonNode.get("baseProductCode").asText
      supplierKcode = productInfoSourceJsonNode.get("supplierKcode").asText
      product = this.createCoreProduct(supplierKcode, bpCode ,
        Seq(r.source.core.get, r.source.coreMarketingMaterial.get))

    }else{


      bpCode  = r.productBaseCode.get
      supplierKcode = r.supplierKcode.get
      product = this.createProduct(supplierKcode, bpCode ,
        r.marketSide ,r.source.marketSide.get)

    }

    drsCmdBus ! new RejectProduct(product)



  }

}
