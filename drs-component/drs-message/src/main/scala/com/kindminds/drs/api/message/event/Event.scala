package com.kindminds.drs.api.message.event

import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.{OnboardingApplication, OnboardingApplicationLineitem}
import com.kindminds.drs.api.v2.biz.domain.model.product.{MarketingMaterialEditingRequest, ProductEditingStatusType, ProductMarketingMaterial}
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest
import com.kindminds.drs.api.v2.biz.domain.model.product.Product

trait Event{
  val topic : Events.Topic
}

object Events extends Enumeration {
  type Topic = Value
  val Product, OnboardingApplication , OnboardingApplicationLineitem,
    MarketingMaterialEditingRequest , ProductMarketingMaterial , QuoteRequest , CustomercareCase ,
   Settlement, P2MApplication = Value
}



case class ProductSaved(product : Product,
                        topic:Events.Topic = Events.Product) extends Event

case class ProductsSaved(products : java.util.List[Product],
                         topic:Events.Topic = Events.Product) extends Event



case class OnboardingApplicationSaved(onboardingApplication : OnboardingApplication,
                                      topic:Events.Topic = Events.OnboardingApplication) extends Event

case class OnboardingApplicationLineitemSaved(onboardingApplicationLineitem :
                                               OnboardingApplicationLineitem,
                                              topic:Events.Topic = Events.OnboardingApplicationLineitem) extends Event


case class MarketingMaterialEditingRequestSaved( supplierKcode : String , productBaseCode:String,
                                                 marketingMaterialEditingRequest : MarketingMaterialEditingRequest ,
                                                 productMarketingMaterial: ProductMarketingMaterial, topic:Events.Topic = Events.MarketingMaterialEditingRequest) extends Event


case class ProductMarketingMaterialSaved(supplierKcode : String, productBaseCode:String,
                                         status : ProductEditingStatusType, productMarketingMaterial : ProductMarketingMaterial,
                                         topic:Events.Topic = Events.ProductMarketingMaterial) extends Event

case class ProductMarketingMaterialStatusChanged(status : ProductEditingStatusType, productMarketingMaterial : ProductMarketingMaterial,
                                                 topic:Events.Topic = Events.ProductMarketingMaterial) extends Event


case class ServiceFeeQuotation4OnboardingProdRequested(quoteRequest : QuoteRequest,
                                        topic:Events.Topic = Events.QuoteRequest) extends Event


case class ServiceFeeQuotationConfirmed(quoteRequest : QuoteRequest,
                                        topic:Events.Topic = Events.QuoteRequest) extends Event


case class ServiceFeeQuotationModified(quoteRequest : QuoteRequest ,
                                       topic:Events.Topic = Events.QuoteRequest) extends Event

case class ServiceFeeQuotationRejected(quoteRequest : QuoteRequest ,
                                       topic:Events.Topic = Events.QuoteRequest) extends Event

case class ServiceFeeQuotationAccepted(quoteRequest : QuoteRequest,
                                       topic:Events.Topic = Events.QuoteRequest) extends Event

case class CustomercareCaseSaved(caseId : Int,
                        topic:Events.Topic = Events.CustomercareCase) extends Event



case class CustomercareCaseUpdated(caseId : Int,
                                 topic:Events.Topic = Events.CustomercareCase) extends Event


case class CustomercareCaseDeleted(caseId : Int,
                                 topic:Events.Topic = Events.CustomercareCase) extends Event


case class CustomercareCaseIssueSaved(issueId : Int,
                                 topic:Events.Topic = Events.CustomercareCase) extends Event



case class CustomercareCaseIssueUpdated(issueId : Int,
                                   topic:Events.Topic = Events.CustomercareCase) extends Event


case class CustomercareCaseIssueDeleted(issueId : Int,
                                   topic:Events.Topic = Events.CustomercareCase) extends Event


case class SettlementCommitted(topic:Events.Topic = Events.Settlement) extends Event

case class MarketSideTransactionsCollected(result:String , topic:Events.Topic = Events.Settlement) extends Event

case class MarketSideTransactionsProcessed(result:String ,topic:Events.Topic = Events.Settlement) extends Event


//case class P2mApplicationCreated(p2mApplication:String,topic:Events.Topic = Events.p2mApplicationCreated) extends Event

case class P2mApplicationCreated(p2mId :String , productId:String ,topic:Events.Topic = Events.P2MApplication) extends Event

case class P2mApplicationSubmitted(p2mId:String , productId:String , kcode:String , topic:Events.Topic = Events.P2MApplication) extends Event

case class P2mApplicationSaveProduct(selectedProduct:String , kcode:String , createdBy : Int , topic:Events.Topic = Events.P2MApplication) extends Event

case class P2mApplicationApproved(p2mId:String , productNameEN:String , kcode:String , productId:String, createdBy : Int , topic:Events.Topic = Events.P2MApplication) extends Event

case class P2mApplicationRejected(p2mId:String , productNameEN:String , kcode:String , createdBy : Int , topic:Events.Topic = Events.P2MApplication) extends Event

case class P2mApplicationApproveToRemove(p2mId:String , productNameEN:String , topic:Events.Topic = Events.P2MApplication) extends Event


