package com.kindminds.drs.core.actors.handlers.command.product

import java.io.IOException
import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.kindminds.drs.Country
import com.kindminds.drs.api.message.command.onboardingApplication._
import com.kindminds.drs.api.message.command.product.{RejectProduct, SaveProducts, SubmitProduct}
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.biz.product.ProductImpl
import com.kindminds.drs.core.biz.product.onboarding.{OnboardingApplicationCreator, OnboardingApplicationImpl}
import com.kindminds.drs.core.biz.repo.product.OnboardingApplicationRepoImpl
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.{OnboardingApplicationDao, OnboardingApplicationLineitemDao}
import com.kindminds.drs.api.v2.biz.domain.model.product.Product
import com.kindminds.drs.enums.OnboardingApplicationStatusType
import org.springframework.util.Assert
import com.kindminds.drs.util.Encryptor





object ApplyOnboardingHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new ApplyOnboardingHandler(drsCmdBus ,drsEventBus))

}

class ApplyOnboardingHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {


  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[CreateApplicationLineitem].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[SubmitApplication].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[ApplyApplication].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[AcceptApplication ].getName ,self)

  drsCmdBus ! RegisterCommandHandler(name,classOf[StartQuoteRequest ].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CompleteQuoteRequest].getName ,self)

  drsCmdBus ! RegisterCommandHandler(name,classOf[ReviewFinalFeasibility ].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[ApproveProductFeasibility ].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RejectProductFeasibility ].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CheckEvalProductInfoStatus ].getName ,self)


  val onboardingApplicationDao = BizCoreCtx.get().getBean(classOf[OnboardingApplicationDao]).asInstanceOf[OnboardingApplicationDao]

  val CompanyDao = BizCoreCtx.get().getBean(classOf[com.kindminds.drs.api.data.access.rdb.CompanyDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.rdb.CompanyDao]

  val onboardingApplicationLineitemDao = BizCoreCtx.get().getBean(classOf[OnboardingApplicationLineitemDao]).asInstanceOf[OnboardingApplicationLineitemDao]


  def receive = {

    case a : ApplyApplication =>

      processApplyApplication(a)

    case sb : SubmitApplication =>

      processSubmitApplication(sb)

    case c : CreateApplicationLineitem =>

      processCreateApplicationLineitem(c)

    case accept : AcceptApplication =>
      processAcceptApplication(accept)


    case sqr : StartQuoteRequest =>
      processStartQuoteRequest(sqr)

    case cqr : CompleteQuoteRequest =>
      processCompleteQuoteRequest(cqr)

    case rf : ReviewFinalFeasibility =>
      processReviewFinalFeasibility(rf)
    case rjf: RejectProductFeasibility =>
      processRejectProductFeasibility(rjf)
    case apf : ApproveProductFeasibility =>
      processApproveProductFeasibility(apf)

    case ckEvalPIStatus : CheckEvalProductInfoStatus =>
      processCheckEvalProdcutStatus(ckEvalPIStatus)


    case message: Any =>
      log.info(s"ApplyOnboardingHandler: received unexpected: $message")
  }


  def processCreateApplicationLineitem(s : CreateApplicationLineitem) ={


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
    drsCmdBus ! SaveProducts(products)

    val oa = OnboardingApplicationCreator.create(supplierKcode, s.serialNumber.orNull, products)

    onboardingApplicationDao.save(oa)

    drsEventBus.publish(OnboardingApplicationSaved(oa))

  }


  def processApplyApplication(a : ApplyApplication) ={

    val repo = new OnboardingApplicationRepoImpl

    val optionalOA =  repo.findById(a.onboardingApplicationId)
    if(optionalOA.isPresent){
      val oa = optionalOA.get()
      oa.submit()
      repo.add(oa)

      drsEventBus.publish(OnboardingApplicationSaved(oa))
    }

  }


  def processSubmitApplication(sb : SubmitApplication) ={
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
      product = this.createProduct(supplierKcode, bpCode ,
        Seq(sb.source.core.get, sb.source.coreMarketingMaterial.get))
    }else{
      product = this.createProduct(supplierKcode, bpCode ,sb.marketSide
        , sb.source.marketSide.get)
    }

    drsCmdBus ! SubmitProduct(product)

    val oa = OnboardingApplicationImpl.submitOnboardingApplication(supplierKcode, sb.serialNumber.orNull,
      product, OnboardingApplicationStatusType.SUBMITTED)
    oa.getLineitems.forEach(oaItem => {
      oaItem.initialReviewing()
      onboardingApplicationLineitemDao.save(oaItem)
      drsEventBus.publish(OnboardingApplicationLineitemSaved(oaItem))
    })

    onboardingApplicationDao.save(oa)
    drsEventBus.publish(OnboardingApplicationSaved(oa))

    } catch {
        case  e : Exception => {
          log.error(e.getStackTraceString)
        }

    }


  }


  def processAcceptApplication(a : AcceptApplication) ={

    val repo = new OnboardingApplicationRepoImpl

     val optionalOA =  repo.findById(a.onboardingApplicationId)
     if(optionalOA.isPresent){
       val oa = optionalOA.get()
       oa.accept()
       repo.add(oa)
     }




    /*
    val objectMapper: ObjectMapper = new ObjectMapper

    var bpCode: String =  ""
    var supplierKcode: String =  ""
    var product : Product = null
    if(a.marketSide == Country.CORE){

      val productInfoSourceJsonNode: JsonNode = objectMapper.readValue(a.source.core.get,
        classOf[JsonNode])

      bpCode  = productInfoSourceJsonNode.get("baseProductCode").asText
      supplierKcode = productInfoSourceJsonNode.get("supplierKcode").asText
      product = this.createProduct(supplierKcode, bpCode ,
        Seq(a.source.core.get, a.source.coreMarketingMaterial.get))

    }else{

      bpCode  = a.productBaseCode.get
      supplierKcode = a.supplierKcode.get
      product = this.createProduct(supplierKcode, bpCode ,a.marketSide, a.source.marketSide.get)

    }

    val oa = OnboardingApplicationImpl.createOnboardingApplication(supplierKcode, bpCode, null, product, OnboardingApplicationStatusType.ACCEPTED)
    onboardingApplicationDao.save(oa)

    drsEventBus.publish(com.kindminds.drs.api.message.OnboardingApplicationSaved(oa))
    */




  }

  def processStartQuoteRequest(sqr : StartQuoteRequest) ={

    val repo = new OnboardingApplicationRepoImpl

    val optionalOA =  repo.findById(sqr.onboardingApplicationId)
    if(optionalOA.isPresent){
      val oa = optionalOA.get()
      oa.startQuoteRequest()
      repo.add(oa)
    }
  }

  def processCompleteQuoteRequest(cqr : CompleteQuoteRequest) ={

    val repo = new OnboardingApplicationRepoImpl

    val optionalOA =  repo.findById(cqr.onboardingApplicationId)
    if(optionalOA.isPresent){
      val oa = optionalOA.get()
      oa.completeQuoteRequest()
      repo.add(oa)
    }
  }

  def processReviewFinalFeasibility(rf : ReviewFinalFeasibility) ={

    val repo = new OnboardingApplicationRepoImpl

    val optionalOA =  repo.findById(rf.onboardingApplicationId)
    if(optionalOA.isPresent){
      val oa = optionalOA.get()
      oa.reviewFinalFeasibility()
      repo.add(oa)
    }
  }

  def processApproveProductFeasibility(a : ApproveProductFeasibility) ={

    val repo = new OnboardingApplicationRepoImpl

    val optionalOA =  repo.findById(a.onboardingApplicationId)
    if(optionalOA.isPresent){
      val oa = optionalOA.get()
      oa.approveProductFeasibility()
      repo.add(oa)

    }
  }

  def processRejectProductFeasibility(r : RejectProductFeasibility) ={

    val repo = new OnboardingApplicationRepoImpl

    val optionalOA =  repo.findById(r.onboardingApplicationId)
    if(optionalOA.isPresent){
      val oa = optionalOA.get()
      oa.rejectProductFeasibility()
      repo.add(oa)

    }
  }

  def processCheckEvalProdcutStatus(c : CheckEvalProductInfoStatus) ={


  }

  def processRejectApplicationLineitem(r : RejectApplicationLineitem) ={


    val objectMapper: ObjectMapper = new ObjectMapper
    var bpCode: String =  ""
    var supplierKcode: String =  ""
    var product : Product = null
    if(r.marketSide == Country.CORE){

      val productInfoSourceJsonNode: JsonNode = objectMapper.readValue(r.source.core.get,
        classOf[JsonNode])

      bpCode  = productInfoSourceJsonNode.get("baseProductCode").asText
      supplierKcode = productInfoSourceJsonNode.get("supplierKcode").asText
      product = this.createProduct(supplierKcode, bpCode ,
        Seq(r.source.core.get, r.source.coreMarketingMaterial.get))

    }else{


      bpCode  = r.productBaseCode.get
      supplierKcode = r.supplierKcode.get
      product = this.createProduct(supplierKcode, bpCode ,
        r.marketSide ,r.source.marketSide.get)

    }

    drsCmdBus ! new RejectProduct(product)

    val oa = OnboardingApplicationCreator.create(supplierKcode, null, product, OnboardingApplicationStatusType.DRAFT)

    oa.getLineitems.forEach(oaItem => {if(oaItem.getId().equals(r.lineItemId)){
      oaItem.reject()
      onboardingApplicationLineitemDao.save(oaItem)
      drsEventBus.publish(OnboardingApplicationLineitemSaved(oaItem))
    }})


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

              productList.add(ProductImpl.createMarketSideProduct(supplierKcode, bpCode,
                coreProdcut.getId, coreProdcut.getCreateTime,
                marketSideJsonNode , marketingJsonNode, Country.valueOf(marketSide)))

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

  private def createProduct(supplierKcode : String, bpCode :String , prodcutRawData : Seq[String]
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


  private def createProduct(supplierKcode : String, bpCode :String , marketSide: Country ,  prodcutRawData : String
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



}
