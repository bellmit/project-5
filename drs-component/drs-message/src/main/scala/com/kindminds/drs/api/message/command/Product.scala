package com.kindminds.drs.api.message.command

import com.kindminds.drs.api.message.{Command}


case class ProductSource (core: Option[String] , coreMarketingMaterial :Option[String],
                          marketSide : Option[String] , marketSideMarketingMaterial : Option[String] )



package manageProduct{


  import com.kindminds.drs.Country
  import com.kindminds.drs.api.data.transfer.productV2.LegacyProduct
  import com.kindminds.drs.api.v1.model.product.BaseProduct

  //  import com.kindminds.drs.data.dto.productV2.{LegacyProduct, ProductDetail}



  //todo temp
  case class CreateProduct(isDrsUser:Boolean, userKcode:String , productInfoSource: String,
                           productInfoMarketSide: String,
                           productMarketingMaterialSource: String,
                           productMarketingMaterialMarketSide: String,
                          ) extends Command
  //todo temp
  case class SaveProduct(isDrsUser:Boolean, userKcode:String ,
                         supplierKcode:Option[String],
                         marketSide: Country ,
                         source : ProductSource ,
                         productBaseCode: Option[String]) extends Command

  //todo temp
  case class SubmitProduct(isDrsUser:Boolean, userKcode:String ,
                               supplierKcode:Option[String],
                               marketSide: Country ,
                               source : ProductSource ,
                               productBaseCode: Option[String]) extends Command


  //todo temp
  case class ApproveProduct(supplierKcode:Option[String]  , marketSide: Country ,
                                        source : ProductSource ,
                                        productBaseCode: Option[String]) extends Command

  //todo temp
  case class RejectProduct(supplierKcode:Option[String]  , marketSide: Country ,
                                       source : ProductSource ,
                                       productBaseCode: Option[String]) extends Command

  case class TransformProductV2ToProductV1(product : com.kindminds.drs.api.v2.biz.domain.model.product.Product) extends Command

  case class TransformProductsV2ToProductV1(product : java.util.List[com.kindminds.drs.api.v2.biz.domain.model.product.Product]) extends Command

  case class MigrateLegacyDraftProductToProductV2(isDrsUser:Boolean, userKcode:String ,
                                                legacyProduct:LegacyProduct) extends Command

  case class MigrateProducts(products : java.util.List[com.kindminds.drs.api.v2.biz.domain.model.product.Product]) extends Command

  case class TransformProduct(productId : String) extends Command


  //1.2.x
  case class CreateBaseProduct(kcode: String, productJson : String) extends Command

  case class UpdateBaseProduct(kcode: String, productId: String, productJson : String) extends Command

  case class DeleteBaseProduct(kcode: String, productId: String) extends Command

  case class ChangeSkuStatusToApplying(productId:String , p2mId:String) extends Command

  case class ChangeSkuStatusToSelling(productId:String , p2mId:String) extends Command

  case class RemoveSkuApplyingStatus(productNameEN:String , p2mId:String) extends Command

  case class RemoveSkuSellingStatus(productNameEN:String , p2mId:String) extends Command

  case class SaveApprovedP2MProduct(productNameEN:String , p2mId:String) extends Command


}

package  product{

  case class SaveProduct(product : com.kindminds.drs.api.v2.biz.domain.model.product.Product) extends Command

  case class RejectProduct(product : com.kindminds.drs.api.v2.biz.domain.model.product.Product) extends Command

  case class ApproveProduct(product : com.kindminds.drs.api.v2.biz.domain.model.product.Product) extends Command

  case class SubmitProduct(product : com.kindminds.drs.api.v2.biz.domain.model.product.Product) extends Command

  case class SaveProducts(products : java.util.List[com.kindminds.drs.api.v2.biz.domain.model.product.Product]) extends Command

  case class VerifyEANCode(ean : String) extends Command

  case class VerifySku(sku : String) extends  Command


}

package  onboardingApplication{

  import com.kindminds.drs.Country
  import com.kindminds.drs.api.v2.biz.domain.model.product.{ProductMarketingMaterial, ProductEditingStatusType}





  case class CreateApplicationLineitem(isDrsUser:Boolean, userKcode:String , productInfoSource: String,
                               productInfoMarketSide: String,
                               productMarketingMaterialSource: String,
                               productMarketingMaterialMarketSide: String,
                               serialNumber : Option[String]) extends Command




  case class SaveApplicationLineitem(isDrsUser:Boolean, userKcode:String ,
                                     supplierKcode:Option[String],
                                     marketSide: Country ,
                                     source : ProductSource ,
                                     productBaseCode: Option[String],
                                     serialNumber : Option[String],
                                     lineItemId : String) extends Command

  case class SubmitApplication(isDrsUser:Boolean, userKcode:String ,
                               supplierKcode:Option[String],
                               marketSide: Country ,
                               source : ProductSource ,
                               productBaseCode: Option[String],
                               serialNumber : Option[String]) extends Command

  case class ApplyApplication(onboardingApplicationId:String ) extends Command




  case class AcceptApplication(onboardingApplicationId:String) extends Command

  case class StartQuoteRequest(onboardingApplicationId:String) extends Command

  case class CompleteQuoteRequest(onboardingApplicationId:String) extends Command




  case class CheckEvalProductInfoStatus(onboardingApplicationId : String) extends Command

  case class ReviewFinalFeasibility(onboardingApplicationId : String) extends Command

  case class ApproveProductFeasibility(onboardingApplicationId : String) extends Command

  case class RejectProductFeasibility(onboardingApplicationId : String) extends Command





  case class ApproveApplicationLineitem(supplierKcode:Option[String]  , marketSide: Country ,
                                        source : ProductSource ,
                                        productBaseCode: Option[String],
                                        lineItemId : String) extends Command


  case class DoTrialList(onboardingApplicationLineitemId : String , trialListJson : String , submit: Boolean) extends Command

  case class EvaluateSample(onboardingApplicationLineitemId : String ,evalProductJson : String , submit: Boolean) extends Command

  case class PresentSample(onboardingApplicationLineitemId : String ,presentSampleJson : String , submit: Boolean) extends Command

  case class ProvideComment(onboardingApplicationLineitemId : String , provideCommentJson : String , submit: Boolean) extends Command

  case class GiveFeedback(onboardingApplicationLineitemId : String , giveFeedbackJson : String , submit: Boolean) extends Command

  case class ApproveSample(onboardingApplicationLineitemId : String , approveSampleJson : String , submit: Boolean) extends Command

  case class AcceptCompleteness(onboardingApplicationLineitemId : String) extends Command

  case class RejectCompleteness(onboardingApplicationLineitemId : String) extends Command

  case class CompleteProductDetail(onboardingApplicationLineitemId : String) extends Command

  case class ConfirmInsurance(onboardingApplicationLineitemId : String , confirmInsuranceJson : String , submit: Boolean) extends Command

  case class CheckInsurance(onboardingApplicationLineitemId : String ,checkInsuranceJson : String , submit: Boolean) extends Command

  case class CheckComplianceAndCertificationAvailability(onboardingApplicationLineitemId : String ,checkCpCaJson : String , submit: Boolean) extends Command

  case class CheckReverseLogistics(onboardingApplicationLineitemId : String) extends Command

  case class CheckProfitability(onboardingApplicationLineitemId : String ,checkProfitabilityJson : String , submit: Boolean) extends Command

  case class CheckMarketability(onboardingApplicationLineitemId : String) extends Command

  case class EvaluatePackageContentAndManual(onboardingApplicationLineitemId : String) extends Command

  case class CheckLineitemEvalProductInfoStatus(onboardingApplicationLineitemId : String) extends Command






  case class RejectApplicationLineitem(supplierKcode:Option[String]  , marketSide: Country ,
                                       source : ProductSource ,
                                       productBaseCode: Option[String],
                                       lineItemId : String) extends Command




  case class CreateMarketingMaterialEditingRequests(products : java.util.List[com.kindminds.drs.api.v2.biz.domain.model.product.Product]) extends Command

  case class SaveMarketingMaterialEditingRequest(supplierKcode:String,
                                                 productBaseCode:String ,
                                                 marketSide:Country,
                                                 productMarketingMaterialId:String ,
                                                 productMarketingMaterialSource:String) extends Command

  case class SubmitMarketingMaterialEditingRequest(supplierKcode:String,
                                                   productBaseCode:String ,
                                                   productMarketingMaterialId:String ,
                                                   productMarketingMaterialSource:String) extends Command

  case class ApproveMarketingMaterialEditingRequest(supplierKcode:String,
                                                    productBaseCode:String ,
                                                    productMarketingMaterialId:String ,
                                                    productMarketingMaterialSource:String) extends Command

  case class RejectMarketingMaterialEditingRequest(supplierKcode:String,
                                                   productBaseCode:String ,
                                                   productMarketingMaterialId:String ,
                                                   productMarketingMaterialSource:String) extends Command



  case class RejectProductMarketingMaterial(productMarketingMaterial : ProductMarketingMaterial) extends Command

  case class ApproveProductMarketingMaterial(productMarketingMaterial : ProductMarketingMaterial) extends Command

  case class SubmitProductMarketingMaterial(productMarketingMaterial : ProductMarketingMaterial) extends Command

  case class SaveProductMarketingMaterial(supplierKcode:String,
                                          productBaseCode:String,
                                          marketSide:Country,
                                          productMarketingMaterial : ProductMarketingMaterial,
                                          status:ProductEditingStatusType) extends Command



}
