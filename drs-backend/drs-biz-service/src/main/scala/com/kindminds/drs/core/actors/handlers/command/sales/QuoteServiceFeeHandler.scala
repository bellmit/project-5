package com.kindminds.drs.core.actors.handlers.command.sales

import java.lang.String
import java.util.Locale

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.quotation._
import com.kindminds.drs.api.message.event._
import com.kindminds.drs.api.message.util
import com.kindminds.drs.api.message.util.{MailMessage, MailMessageWithBCC}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.biz.repo.product.OnboardingApplicationRepoImpl
import com.kindminds.drs.core.biz.repo.sales.{QuotationRepoImpl, QuoteRequestImpl, QuoteRequestRepoImpl}
import com.kindminds.drs.core.{DrsEventBus, RegisterCommandHandler}
import com.kindminds.drs.api.data.access.rdb.UserDao
import com.kindminds.drs.api.v2.biz.domain.model.sales.Quotation
import com.kindminds.drs.api.v2.biz.domain.model.sales.service.FeeType
import com.kindminds.drs.api.v1.model.Company
import com.kindminds.drs.service.util.MailUtil
import com.kindminds.drs.service.util.MailUtil.SignatureType
import org.springframework.context.MessageSource




object QuoteServiceFeeHandler {

  def props(drsCmdBus: ActorRef , drsEventBus: DrsEventBus ): Props =
    Props(new QuoteServiceFeeHandler(drsCmdBus ,drsEventBus))

}

class QuoteServiceFeeHandler(drsCmdBus: ActorRef, drsEventBus: DrsEventBus)
  extends Actor with ActorLogging {


  val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name, classOf[RequestServiceFeeQuotation4OnboardingProd].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[CreateServiceFeeQuotation].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ModifyServiceFeeQuotation].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[AcceptServiceFeeQuotation].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[RejectServiceFeeQuotation].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[ConfirmServiceFeeQuotation].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[MailMessage].getName, self)
  drsCmdBus ! RegisterCommandHandler(name, classOf[MailMessageWithBCC].getName, self)



  val maintainCompanyDao = BizCoreCtx.get().getBean(classOf[com.kindminds.drs.api.data.access.usecase.MaintainCompanyDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.usecase.MaintainCompanyDao]


  val messageSource = BizCoreCtx.get().getBean(classOf[MessageSource]).asInstanceOf[MessageSource]

  val UserDao = BizCoreCtx.get().getBean(classOf[UserDao]).asInstanceOf[UserDao]

  val mailUtil = BizCoreCtx.get().getBean(classOf[MailUtil]).asInstanceOf[MailUtil]

  val quoteRequestRepo = new QuoteRequestRepoImpl

  val quotationRepo = new QuotationRepoImpl

  val accountManagers = "account.managers@tw.drs.network"
  val testMailTo = "robert.lee@drs.network"


  def receive = {
    case rq: RequestServiceFeeQuotation4OnboardingProd =>
      processRequestServiceFeeQuotation(rq)

    case crt: CreateServiceFeeQuotation =>
      processCreateServiceFeeQuotation(crt)

    case mod: ModifyServiceFeeQuotation =>
      processModifyServiceFeeQuotation(mod)

    case acc: AcceptServiceFeeQuotation =>
      processAcceptServiceFeeQuotation(acc)

    case rej: RejectServiceFeeQuotation =>
      processRejectServiceFeeQuotation(rej)

    case conf: ConfirmServiceFeeQuotation =>
      processConfirmServiceFeeQuotation(conf)


    case message: Any =>
      log.info(s"QuoteServiceFeeHandler: received unexpected: $message")
  }

  def processRequestServiceFeeQuotation(rq: RequestServiceFeeQuotation4OnboardingProd): Unit = {
    val oaRepo = new OnboardingApplicationRepoImpl
    val opOa = oaRepo.findById(rq.onboardingApplicationId)
    if(opOa.isPresent){
      val oa = opOa.get()

      val quoteRequest = QuoteRequestImpl.createInitialRequest(
        oa.getSupplierCompanyId, FeeType.MONTHLY_SERVICE_FEE)

      val quotation = quoteRequest.generateQuotation()

      quotationRepo.add(quotation)

      quoteRequestRepo.add(quoteRequest)

      quoteRequestRepo.addQuoteRequestOnboardingApp(quoteRequest.getId, rq.onboardingApplicationId)

      sender ! quotation

      drsEventBus.publish(ServiceFeeQuotation4OnboardingProdRequested(quoteRequest))
    }
  }

  def processCreateServiceFeeQuotation(crt: CreateServiceFeeQuotation) ={

    val request = quoteRequestRepo.findById(crt.quoteRequestId)

    if(request.isPresent){
      val quoteRequest = request.get()

      quoteRequest.create(crt.reducedPrice)

      quotationRepo.add(quoteRequest.getQuotation.get())

      quoteRequestRepo.add(quoteRequest)

      val currentUser = testMailTo//crt.currentUser

      sendQuotationMail(quoteRequest.getSupplierCompanyId,
        currentUser, quoteRequest.getQuotation.get())

      drsEventBus.publish(ServiceFeeQuotationModified(quoteRequest))
    }
  }

  def processModifyServiceFeeQuotation(mod: ModifyServiceFeeQuotation) ={

    val request = quoteRequestRepo.findById(mod.quoteRequestId)

    if(request.isPresent) {
      val quoteRequest = request.get()

      quoteRequest.modify(mod.reducedPrice)

      quotationRepo.add(quoteRequest.getQuotation.get())

      quoteRequestRepo.add(quoteRequest)

      val currentUser = testMailTo//mod.currentUser

      sendQuotationMail(quoteRequest.getSupplierCompanyId,
        currentUser, quoteRequest.getQuotation.get())

      drsEventBus.publish(ServiceFeeQuotationModified(quoteRequest))

    }
  }

  def sendQuotationMail(supplierId : Integer, currentUser : String, quotation : Quotation): Unit = {
    val companyInfo = getCompanyInfo(supplierId)

    val userName = currentUser
    val userInfo = UserDao.findUserByUserID(userName)
    val userLocale = userInfo.getLocale
    val to = testMailTo//userName
    val cc = testMailTo
    val subjectArgs = Array[Object](companyInfo.getKcode, companyInfo.getShortNameLocal, quotation.getSerialNumber)
    val subject = messageSource.getMessage("serviceFeeQuotation.subject", subjectArgs, userLocale)
    val bodyArgs = Array[Object](quotation.getSerialNumber, userInfo.getUserDisplayName, userName)
    var body = messageSource.getMessage("serviceFeeQuotation.body", bodyArgs, userLocale)
    val signature = this.mailUtil.getSignature(SignatureType.NO_REPLY)
    body += "<br><br>" + signature

    drsCmdBus ! util.MailMessageWithBCC(to, cc, subject, body)
  }

  def getCompanyInfo(supplierId : Integer): Company = {
    val companyCode = quoteRequestRepo.getCompanyCode(supplierId)
    maintainCompanyDao.query(companyCode)
  }


  def processRejectServiceFeeQuotation(rsfq : RejectServiceFeeQuotation) ={

    val request = quoteRequestRepo.findById(rsfq.quoteRequestId)

    if(request.isPresent){
      val quoteRequest = request.get()
      quoteRequest.reject()

      quotationRepo.add(quoteRequest.getQuotation.get())

      quoteRequestRepo.add(quoteRequest)

      sendRejectionMail(quoteRequest.getSupplierCompanyId, quoteRequest.getQuotation.get())

      drsEventBus.publish(ServiceFeeQuotationRejected(quoteRequest))
    }
  }

  def sendRejectionMail(supplierId : Integer, quotation : Quotation): Unit = {
    val companyInfo = getCompanyInfo(supplierId)

    val to = testMailTo//"robert.lee@drs.network"
    val subjectArgs = Array[Object](companyInfo.getKcode, companyInfo.getShortNameLocal, quotation.getSerialNumber)
    val subject = messageSource.getMessage("serviceFeeReject.subject", subjectArgs, Locale.US)
    val bodyArgs = Array[Object](quotation.getSerialNumber, companyInfo.getKcode, companyInfo.getServiceEmailList)
    val body = messageSource.getMessage("serviceFeeReject.body", bodyArgs, Locale.US)

    drsCmdBus ! util.MailMessage(to, subject, body)
  }

  def processAcceptServiceFeeQuotation(asfq : AcceptServiceFeeQuotation) ={

    val request = quoteRequestRepo.findById(asfq.quoteRequestId)

    if(request.isPresent){
      val quoteRequest = request.get()
      quoteRequest.accept()

      quotationRepo.add(quoteRequest.getQuotation.get())

      quoteRequestRepo.add(quoteRequest)

      sendAcceptanceMail(quoteRequest.getSupplierCompanyId, quoteRequest.getQuotation.get())

      drsEventBus.publish(ServiceFeeQuotationAccepted(quoteRequest))
    }
  }

  def sendAcceptanceMail(supplierId : Integer, quotation : Quotation): Unit = {
    val companyInfo = getCompanyInfo(supplierId)

    val to = testMailTo//"robert.lee@drs.network"
    val subjectArgs = Array[Object](companyInfo.getKcode, companyInfo.getShortNameLocal, quotation.getSerialNumber)
    val subject = messageSource.getMessage("serviceFeeAccept.subject", subjectArgs, Locale.US)
    val bodyArgs = Array[Object](quotation.getSerialNumber)
    val body = messageSource.getMessage("serviceFeeAccept.body", bodyArgs, Locale.US)

    drsCmdBus ! util.MailMessage(to, subject, body)
  }

  def processConfirmServiceFeeQuotation(csfq : ConfirmServiceFeeQuotation) ={

    val request = quoteRequestRepo.findById(csfq.quoteRequestId)

    if(request.isPresent){
      val quoteRequest = request.get()
      quoteRequest.confirm()

      quotationRepo.add(quoteRequest.getQuotation.get())

      quoteRequestRepo.add(quoteRequest)

      drsEventBus.publish(ServiceFeeQuotationConfirmed(quoteRequest))

    }
  }

}