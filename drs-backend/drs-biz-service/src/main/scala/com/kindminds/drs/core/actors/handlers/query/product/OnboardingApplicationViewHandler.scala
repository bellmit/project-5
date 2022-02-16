package com.kindminds.drs.core.actors.handlers.query.product

import java.util
import java.util.Optional

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.kindminds.drs.api.message.query.onboardingApplication._
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterQueryHandler
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding.OnboardingApplicationDao
import com.kindminds.drs.api.data.cqrs.store.read.queries.OnboardingApplicationViewQueries
import com.kindminds.drs.api.data.transfer.productV2.onboarding.OnboardingApplication
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType




object OnboardingApplicationViewHandler {

  def props(drsQueryBus: ActorRef): Props =
    Props(new OnboardingApplicationViewHandler(drsQueryBus))

}


class OnboardingApplicationViewHandler(drsQueryBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  drsQueryBus ! RegisterQueryHandler(name, classOf[GetSupplierKcodeToShortEnUsNameMap].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[IsExecutable].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetApplicationSerialNumbersBySupplier].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetOnboardingApplicationList].getName, self)
  drsQueryBus ! RegisterQueryHandler(name, classOf[GetOnboardingApplicationListBySupplier].getName, self)

  val onboardingApplicationDao = BizCoreCtx.get().getBean(classOf[OnboardingApplicationDao])
    .asInstanceOf[OnboardingApplicationDao]

  val vQueries = BizCoreCtx.get().getBean(classOf[OnboardingApplicationViewQueries])
    .asInstanceOf[OnboardingApplicationViewQueries]

  val companyDao = BizCoreCtx.get().getBean(classOf[com.kindminds.drs.api.data.access.rdb.CompanyDao])
    .asInstanceOf[com.kindminds.drs.api.data.access.rdb.CompanyDao]

  val om = new ObjectMapper()

  def receive = {
    case ie : IsExecutable =>

     val b =  checkExecutable(ie.companyKcode,ie.status)

      this.sender() ! b

    case sn : GetSupplierKcodeToShortEnUsNameMap =>

      val snMap = companyDao.querySupplierKcodeToShortEnUsNameMap()

      val s = om.writeValueAsString(snMap)

      this.sender() ! s

    case snbs : GetApplicationSerialNumbersBySupplier =>

      val sn: Optional[util.List[String]] = vQueries.getSerialNumbersBySupplier(snbs.companyKcode)
      val s = om.writeValueAsString(sn.get())
      this.sender() ! s

    case gal : GetOnboardingApplicationList =>

      val al: util.List[OnboardingApplication] = vQueries.getOnboardingApplications();

      val s = om.writeValueAsString(al)

      this.sender() ! s

    case galby : GetOnboardingApplicationListBySupplier =>

      val al = vQueries.getOnboardingApplications(galby.companyKcode)

      val s = om.writeValueAsString(al)

      this.sender() ! s

    case message: Any =>
      log.info(s"ApplyOnboardingHandler: received unexpected: $message")
  }

  def checkExecutable(userKcode:String , status : ProductEditingStatusType): Boolean ={
    val isSupplier: Boolean = this.companyDao.isSupplier(userKcode)
    if (isSupplier)  {
      status match  {
        case ProductEditingStatusType.PENDING_SUPPLIER_ACTION =>
          true
        case ProductEditingStatusType.PENDING_DRS_REVIEW =>
          false
        case ProductEditingStatusType.FINALIZED =>
          false
        case _ =>
          false
      }
    } else  {
      status match  {
        case ProductEditingStatusType.PENDING_SUPPLIER_ACTION =>
          false
        case ProductEditingStatusType.PENDING_DRS_REVIEW =>
          true
        case ProductEditingStatusType.FINALIZED =>
          true
        case _ =>
          false
      }
    }
  }

  /*
* import com.kindminds.drs.Context
def isExecutable(status: Nothing): Boolean =  { val userKcode: String = Context.getCurrentUser.getCompanyKcode
v

}
}
*
* */


}
