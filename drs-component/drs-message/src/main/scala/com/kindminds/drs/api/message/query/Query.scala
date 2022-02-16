package com.kindminds.drs.api.message

trait Query




package  viewKeyProductStatsUco{

  import java.util.Date

  case class GetKeyProductStatsReport(isSupplier:Boolean , companyKcode : String) extends Query

  case class GetKeyProductTotalFbaInStock(isSupplier:Boolean , companyKcode : String) extends Query

  case class GetKeyProductStats(companyKcode : Option[String] , marketPlace : Option[String]
                                ,  productBaseCode :  Option[String] ,
                                productSkuCode : Option[String] ) extends Query

  case class GetKeyProductInventoryStats(companyKcode : Option[String] , marketPlace : Option[String]
                                         ,  productBaseCode :  Option[String] ,
                                         productSkuCode : Option[String] ) extends Query


  case  class GetKeyProductSinceLastSettlementOrder(companyKcode : Option[String] , marketPlace : Option[String]
                                                    ,  productBaseCode :  Option[String] ,
                                                    productSkuCode : Option[String]) extends Query

  case class GetKeyProductLastSevenDaysOrder(companyKcode : Option[String] , marketPlace : Option[String]
                                             ,  productBaseCode :  Option[String] ,
                                             productSkuCode : Option[String]) extends Query

  case class GetKeyProductBaseRetailPrice(companyKcode : Option[String] , marketPlace : Option[String]
                                          ,  productBaseCode :  Option[String] ,
                                          productSkuCode : Option[String]) extends Query

}

package  viewHomePageUco{

  case class GetSs2spStatementListReport(userCompanyKcode : String ) extends Query

}

package viewExternalMarketingActivityUco {
  /**
   * Return - String JSON of List<ExternalMarketingSkuItem>
   */
  case class GetListOfSkus() extends Query

  /**
   * @param kCode
   * @param skuCode
   * @param marketplace
   *
   * Return - String JSON of ExternalMarketingData
   */
  case class GetMarketingData(kCode: String, skuCode: String, marketplace: String) extends Query

}

package viewProductMarketplaceInfo {

  case class GetLiveAmazonAsins() extends Query

  case class GetLiveK101AmazonAsins() extends Query

}

package logistics {

  import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs

  //todo need to refactor here arthur

  case class ViewIvsProductInfo(shipment : Ivs, sku : String,
                                boxNum: Integer, mixedBoxLineSeq: Integer) extends Query

  case class ViewIvsProductDocuments(shipment : Ivs, sku : String,
                                     boxNum: Integer, mixedBoxLineSeq: Integer) extends Query

}

