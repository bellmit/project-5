package com.kindminds.drs.api.message.query

import com.kindminds.drs.api.message.Query

package marketing {


  import java.util.Date

  import com.kindminds.drs.api.message.Query

  /*
    returns DtoList<MarketingActivity> in JSON
   */
  case class GetMarketingActivityList(country : String , pageIndex:Int) extends Query

  case class GetMarketingActivityByFilters(country : String , skuCode : String,
                                           startDate : Date, endDate : Date, pageIndex:Int) extends Query

  case class GetMarketingActivityById(activityId : String) extends Query

  /*
    returns Map<String, Integer> in JSON
    where String is marketplace abbreviation
    and Integer is marketplace Id
   */
  case class GetMarketplacesAndIds() extends Query


}

package marketing.amazonSponsoredBrandsCampaign {

  /**
    * return - List<String>
    */
  case class GetCampaignNames(supplierKcode : String, marketplaceId : Integer, startDate : String, endDate : String) extends Query

  /**
    * return - List<Marketplace>
    */
  case class GetMarketplaceNames() extends Query

  /**
    * return - Map<String,String>
    */
  case class GetAmazonSponsoredBrandsCampaignReport(supplierKcode : String, marketplaceId : Integer, startDate : String, endDate : String, campaignName : String) extends Query

  /**
    * return - Map<String,String>
    */
  case class GetSupplierKcodeToShortEnNameMap() extends Query

}



package marketing.hsaCampaignReport {

  /**
    * Uses the latest settlement period
    *
    * Return - List<HsaCampaignReportDetail>
    */
  case class GetHsaCampaignReportDetail() extends Query

  /**
    * @param settlementPeriodId - Settlement Period ID
    *
    * Return - List<HsaCampaignReportDetail>
    */
  case class GetHsaCampaignReportDetailBySettlementPeriod(settlementPeriodId: Integer) extends Query


  case class GetHsaCampaignReport(supplierKcode : String, marketplaceId : Integer, startDate : String, endDate : String, campaignName : String) extends Query

  case class GetCampaignNames(supplierKcode : String, marketplaceId : Integer, startDate : String, endDate : String) extends Query

}


