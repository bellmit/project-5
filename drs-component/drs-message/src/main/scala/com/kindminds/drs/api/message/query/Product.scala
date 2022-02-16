package com.kindminds.drs.api.message.query

import com.kindminds.drs.api.message.Query
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType

package product {

  import com.kindminds.drs.Country
  import com.kindminds.drs.api.message.Query

  case class GetProductList(companyKcode : String ,pageIndex:Int) extends Query

  case class GetProduct( isDrsUser:Boolean  , companyKcode : String , productBaseCode:String ) extends Query

  case class GetProductList4DRS(companyKcode : String ,pageIndex:Int) extends Query

  case class GetProductInfoMarketSide( marketSide:String  , companyKcode : String , productBaseCode:String ) extends Query

  case class GetProductWithSKU(productBaseCode:String ) extends Query

  case class GetProductMarketingMaterial(marketSide : Country , companyKcode : String ,
                                         productBaseCode:String  , productVariationCode:String ) extends Query



  case class GetProductBaseCode(companyKcode : String ) extends Query

  case class GetProductSkuCode(productBaseCode : String ) extends Query

  case class GetAmazonProductReview(companyKcode :  Option[String] , marketPlace : Option[String],
                                    productBaseCode :  Option[String] , productSkuCode : Option[String] ) extends Query

  case class GetProductCategory()


}

package manageProduct {

  case class GetSimpleProductList(kcode:String) extends Query

  case class GetBaseProducts(kcode: String, pageIndex: Int) extends Query

  case class GetNextPage(kcode: String, pageIndex: Int) extends Query

  case class GetBaseProductById(kcode: String, productId: String) extends Query

  case class GetTotalProductNumber(kcode:String ) extends Query

  case class GetTotalSkuNumber(kcode:String ) extends Query

}

package prdocut.dashboard {

  import com.kindminds.drs.Country
  import com.kindminds.drs.api.message.Query

  case class GetDailySalesQtyAndRev(companyKcode :  Option[String] , marketPlace : Option[String]
                                    ,  productBaseCode :  Option[String] ,
                                    productSkuCode : Option[String] ) extends Query

  case class GetDailyReturnsQty(companyKcode :  Option[String] ,  productBaseCode :  Option[String] , productSkuCode : Option[String] ) extends Query

  case class GetDailyReviews(companyKcode :  Option[String] ,  productBaseCode :  Option[String] , productSkuCode : Option[String] ) extends Query

  case class GetMTDCustomerCareCases(companyKcode :  Option[String] ,  productBaseCode :  Option[String] , productSkuCode : Option[String] ) extends Query

  case class GetMTDCampaignSpendAndAcos(companyKcode :  Option[String] , marketPlace : Option[String]
                                        ,productBaseCode :  Option[String] , productSkuCode : Option[String] ) extends Query

  case class GetDailyOrder(companyKcode :  Option[String] , marketPlace : Option[String]
                           ,  productBaseCode :  Option[String] ,
                           productSkuCode : Option[String] ) extends Query

  case class GetKeyProductStats(companyKcode : Option[String] , marketPlace : Option[String]
                                ,  productBaseCode :  Option[String] ,
                                productSkuCode : Option[String] ) extends Query

  case class GetDetailPageSalesTraffic(companyKcode :  Option[String] , marketPlace : Option[String]
                                       ,  productBaseCode :  Option[String] ,
                                       productSkuCode : Option[String] ) extends Query


}

package  onboardingApplication {

  import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType


  case class GetSupplierKcodeToShortEnUsNameMap() extends Query

  case class IsExecutable(companyKcode : String ,  status : ProductEditingStatusType) extends Query

  case class GetOnboardingApplicationList() extends Query

  case class GetOnboardingApplicationListBySupplier(companyKcode : String) extends Query

  case class GetProductVariationarketSides(productBaseCode:String , productVariationCode:String) extends Query

  case class GetApplicationSerialNumbersBySupplier(companyKcode : String) extends Query


  case class GetBaseCodesToMarketplacesMap(companyKcode : String) extends Query

  case class GetOnboardingApplicationLineitem(onboardingApplicationLineitemId : String) extends Query


  case class GetTrialList(onboardingApplicationLineitemId : String ) extends Query

  case class GetEvalSample(onboardingApplicationLineitemId : String ) extends Query

  case class GetPresentSample(onboardingApplicationLineitemId : String ) extends Query

  case class GetApproveSample(onboardingApplicationLineitemId : String ) extends Query

  case class GetProvideComment(onboardingApplicationLineitemId : String ) extends Query


  case class GetGiveFeedback(onboardingApplicationLineitemId : String ) extends Query

  case class GetCheckInsurance(onboardingApplicationLineitemId : String ) extends Query

  case class GetConfirmInsurance(onboardingApplicationLineitemId : String ) extends Query

  case class GetCheckProfitability(onboardingApplicationLineitemId : String ) extends Query

  case class GetCheckComplianceAndCertAvailability(onboardingApplicationLineitemId : String ) extends Query






}


