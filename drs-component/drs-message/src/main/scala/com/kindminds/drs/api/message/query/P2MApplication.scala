package com.kindminds.drs.api.message.query

package p2m {

  import com.kindminds.drs.api.message.Query

  case class GetP2MApplication(id:String) extends Query

  case class GetApplicationList(supplierId:String ,pageIndex:Int ,country:String ,status:String,kcode:String,product:String) extends Query

  case class GetAllApplicationList(supplierId:String,kcode:String,product:String) extends Query

  case class GetAllComment(p2mApplicationId:String) extends Query

  case class GetP2MInsurance(p2mApplicationId:String) extends Query

  case class GetP2MMarketplaceInfo(p2mApplicationId:String) extends Query

  case class GetP2MProductInfo(p2mApplicationId:String) extends Query

  case class GetP2MRegional(p2mApplicationId:String) extends Query

  case class GetP2MShipping(p2mApplicationId:String) extends Query

  case class GetTotalAppliedSkuNumber(kcode:String ) extends Query

  case class GetTotalOnSaleSkuNumber(kcode:String ) extends Query

  case class GetCanBeChangedApplications(kcode:String ) extends Query



}
