package com.kindminds.drs.core.actors.handlers.command.product

import java.io.IOException
import java.util

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.kindminds.drs.Country
import com.kindminds.drs.api.message.command.onboardingApplication._
import com.kindminds.drs.api.message.command.product.{ApproveProduct, RejectProduct, SaveProduct}
import com.kindminds.drs.api.message.event.{OnboardingApplicationLineitemSaved, OnboardingApplicationSaved}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.biz.product.ProductImpl
import com.kindminds.drs.core.biz.product.onboarding.OnboardingApplicationCreator
import com.kindminds.drs.core.biz.repo.product.OnboardingApplicationLineitemRepoImpl
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.OnboardingApplicationLineitemDao
import com.kindminds.drs.api.v2.biz.domain.model.product.Product
import com.kindminds.drs.enums.OnboardingApplicationStatusType
import org.springframework.util.Assert



object ApplyOnboardingLineitemHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new ApplyOnboardingLineitemHandler(drsCmdBus ,drsEventBus))

}

class ApplyOnboardingLineitemHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {


  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[DoTrialList].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[EvaluateSample].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[PresentSample].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[GiveFeedback].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[ProvideComment].getName ,self)

  drsCmdBus ! RegisterCommandHandler(name,classOf[ApproveSample].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[AcceptCompleteness].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RejectCompleteness].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CompleteProductDetail].getName ,self)

  drsCmdBus ! RegisterCommandHandler(name,classOf[ConfirmInsurance].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CheckInsurance].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CheckComplianceAndCertificationAvailability].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CheckReverseLogistics].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CheckMarketability].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[CheckProfitability].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[EvaluatePackageContentAndManual].getName ,self)




  /*
    drsCmdBus ! RegisterCommandHandler(name,classOf[ApproveApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[RejectApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[SaveApplicationLineitem].getName ,self)


    drsCmdBus ! RegisterCommandHandler(name,classOf[SampleEvaluatedApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[SamplePresentedApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[SampleFeedbackBySCApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[SampleFeedbackByMSApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[ApplicationInfoReturnedApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[ApplicationInfoResubmittedApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[ApplicationInfoCompletedApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[ProductInfoEvaluatingApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[ProductInfoEvaluatedApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[FeasibilityReviewedApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[FeasibilityRejectedApplicationLineitem].getName ,self)
    drsCmdBus ! RegisterCommandHandler(name,classOf[FeasibilityApprovedApplicationLineitem].getName ,self)
    */



  val CompanyDao = BizCoreCtx.get().getBean(classOf[com.kindminds.drs.api.data.access.rdb.CompanyDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.rdb.CompanyDao]

  val onboardingApplicationLineitemDao = BizCoreCtx.get().getBean(classOf[OnboardingApplicationLineitemDao]).asInstanceOf[OnboardingApplicationLineitemDao]


  def receive = {

    case r : RejectApplicationLineitem =>

      processRejectApplicationLineitem(r)

    case a : ApproveApplicationLineitem =>

      processApproveApplicationLineitem(a)


    case sli : SaveApplicationLineitem =>

      processSaveApplicationLineitem(sli)

    case dt : DoTrialList =>
      processDoTrailList(dt)

    case es : EvaluateSample =>
       processEvaluateSample(es)

    case ps : PresentSample =>
      processPresentSample(ps)

    case as : ApproveSample =>
      processApproveSample(as)

    case pc: ProvideComment =>
      processProvideComment(pc)

    case gf : GiveFeedback =>
      processGiveFeedback(gf)

    case ac : AcceptCompleteness =>
      processAcceptCompleteness(ac)

    case rc : RejectCompleteness =>
      processRejectCompleteness(rc)

    case cp : CompleteProductDetail =>
      processCompleteProductDetail(cp)

    case cri : ConfirmInsurance =>
      processConfirmInsurance(cri)

    case ci : CheckInsurance =>
      processCheckInsurance(ci)

    case cm : CheckMarketability =>
      processCheckMarketability(cm)

    case cp : CheckProfitability =>
      processCheckProfitability(cp)

    case crl : CheckReverseLogistics =>
      processCheckReverseLogistics(crl)

    case cc : CheckComplianceAndCertificationAvailability =>
      processCheckComplianceAndCertificationAvailability(cc)

    case ep : EvaluatePackageContentAndManual =>
      processEvaluatePackageContentAndManual(ep)

    case message: Any =>
      log.info(s"ApplyOnboardingLineitemHandler: received unexpected: $message")
  }


  def processDoTrailList(d : DoTrialList) ={

    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(d.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.doTrialList(d.trialListJson,d.submit)
      repo.add(item)
    }


  }

  def processEvaluateSample(e : EvaluateSample) ={

    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(e.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.evaluateSample(e.evalProductJson,e.submit)
      repo.add(item)
    }

  }

  def processPresentSample(p : PresentSample) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(p.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.presentSample(p.presentSampleJson,p.submit)
      repo.add(item)
    }


  }

  def processProvideComment(p : ProvideComment) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(p.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.provideComment(p.provideCommentJson,p.submit)
      repo.add(item)
    }


  }

  def processGiveFeedback(g : GiveFeedback) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(g.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.giveFeedback(g.giveFeedbackJson,g.submit)
      repo.add(item)
    }


  }

  def processApproveSample(a : ApproveSample) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(a.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.approveSample(a.approveSampleJson,a.submit)
      repo.add(item)
    }


  }

  def processAcceptCompleteness(a : AcceptCompleteness) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(a.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.acceptCompleteness()
      repo.add(item)
      item.evaluateProductInfo()
      repo.add(item)
    }


  }


  def processRejectCompleteness(r : RejectCompleteness) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(r.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.rejectCompleteness()
      repo.add(item)
    }


  }


  def processCompleteProductDetail(c : CompleteProductDetail) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(c.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.completeProductDetail()
      repo.add(item)
    }


  }

  def processConfirmInsurance(c : ConfirmInsurance) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(c.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.confirmInsurance(c.confirmInsuranceJson,c.submit)
      repo.add(item)

      if(item.getStatus == OnboardingApplicationStatusType.PRODUCT_INFO_EVALUATED){
        drsCmdBus ! CheckEvalProductInfoStatus
      }

    }


  }

  def processCheckInsurance(c : CheckInsurance) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(c.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.checkInsurance(c.checkInsuranceJson,c.submit)
      repo.add(item)

      if(item.getStatus == OnboardingApplicationStatusType.PRODUCT_INFO_EVALUATED){
        drsCmdBus ! CheckEvalProductInfoStatus(item.getOnboardingApplicationId)
      }

    }


  }

  def processCheckMarketability(c : CheckMarketability) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(c.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.checkMarketability()
      repo.add(item)

      if(item.getStatus == OnboardingApplicationStatusType.PRODUCT_INFO_EVALUATED){
        drsCmdBus ! CheckEvalProductInfoStatus(item.getOnboardingApplicationId)
      }

    }


  }

  def processCheckProfitability(c : CheckProfitability) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(c.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.checkProfitability(c.checkProfitabilityJson,c.submit)
      repo.add(item)

      if(item.getStatus == OnboardingApplicationStatusType.PRODUCT_INFO_EVALUATED){
        drsCmdBus ! CheckEvalProductInfoStatus(item.getOnboardingApplicationId)
      }

    }


  }

  def processCheckComplianceAndCertificationAvailability(c : CheckComplianceAndCertificationAvailability) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(c.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.checkComplianceAndCertAvailability(c.checkCpCaJson,c.submit)
      repo.add(item)

      if(item.getStatus == OnboardingApplicationStatusType.PRODUCT_INFO_EVALUATED){
        drsCmdBus ! CheckEvalProductInfoStatus(item.getOnboardingApplicationId)
      }

    }


  }

  def processCheckReverseLogistics(c : CheckReverseLogistics) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(c.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.checkReverseLogistics()
      repo.add(item)

      if(item.getStatus == OnboardingApplicationStatusType.PRODUCT_INFO_EVALUATED){
        drsCmdBus ! CheckEvalProductInfoStatus(item.getOnboardingApplicationId)
      }

    }


  }

  def processEvaluatePackageContentAndManual(e : EvaluatePackageContentAndManual) ={


    val repo = new OnboardingApplicationLineitemRepoImpl
    val optionalItem = repo.findById(e.onboardingApplicationLineitemId)
    if(optionalItem.isPresent){
      val item = optionalItem.get()
      item.evaluatePackageContentAndManual()
      repo.add(item)

      if(item.getStatus == OnboardingApplicationStatusType.PRODUCT_INFO_EVALUATED){
        drsCmdBus ! CheckEvalProductInfoStatus(item.getOnboardingApplicationId)
      }

    }


  }




  def processSaveApplicationLineitem(s : SaveApplicationLineitem) ={


    val objectMapper: ObjectMapper = new ObjectMapper

    var bpCode: String =  ""
    var supplierKcode: String =  ""


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

    var product : Product = null
    if(s.marketSide == Country.CORE){
      product = this.createProduct(supplierKcode, bpCode ,
        Seq(s.source.core.get, s.source.coreMarketingMaterial.get))
    }else{
      product = this.createProduct(supplierKcode, bpCode , s.marketSide , s.source.marketSide.get)

    }


    drsCmdBus ! SaveProduct(product)

    val oa = OnboardingApplicationCreator.create(supplierKcode, s.serialNumber.orNull, product, OnboardingApplicationStatusType.DRAFT)
    val oaItem = oa.getLineitems.get(0)

    //onboardingApplicationDao.save(oa)
    onboardingApplicationLineitemDao.save(oaItem)

    drsEventBus.publish(OnboardingApplicationSaved(oa))
    drsEventBus.publish(OnboardingApplicationLineitemSaved(oaItem))


  }



  def processApproveApplicationLineitem(a : ApproveApplicationLineitem) ={


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


    drsCmdBus ! ApproveProduct(product)

    val oa = OnboardingApplicationCreator.create(supplierKcode, null, product, OnboardingApplicationStatusType.APPROVED)

    oa.getLineitems.forEach(oaItem => {if(oaItem.getId().equals(a.lineItemId)){
      oaItem.approve()
      onboardingApplicationLineitemDao.save(oaItem)
      drsEventBus.publish(OnboardingApplicationLineitemSaved(oaItem))
    }})

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
