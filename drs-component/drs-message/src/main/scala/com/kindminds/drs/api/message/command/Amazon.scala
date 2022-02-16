package com.kindminds.drs.api.message.command.amazon

package order {

  import com.kindminds.drs.Country
  import com.kindminds.drs.api.message.Command

  case class RequestAmazonOrder(country:Country) extends Command

  case class RequestAmazonFulfillmentOrder(serviceRegion:String) extends Command

  //ImportShopifyOrderUcoImpl
}

package report{

  import java.time.ZonedDateTime

  import com.kindminds.drs.{Marketplace, MwsReportType}
  import com.kindminds.drs.api.message.Command

  case class RequestReport(marketplace : Marketplace, reportType : MwsReportType)

  case class DownloadReport(reportId:String , marketplace : Marketplace ,
                            reportType : MwsReportType) extends Command


}

package mws.feeds{

  import com.amazonaws.mws.model.{IdList, TypeList}
  import com.kindminds.drs.{Marketplace, MwsFeedType}
  import com.kindminds.drs.api.message.Command


  case class SubmitFeed(path: String, marketplace: Marketplace, feedType: MwsFeedType) extends Command

  case class RequestFeedSubmissionList(marketplace: Marketplace, feedSubmissionIdList: IdList) extends Command

  case class RequestFeedSubmissionListByCount(marketplace: Marketplace,
                                              maxCount: Integer, feedTypeList: TypeList) extends Command

  case class RequestFeedSubmissionCount(marketplace: Marketplace, feedTypeList: TypeList) extends Command

  case class RequestCancelFeedSubmissions(marketplace: Marketplace, feedIdList: IdList) extends Command

  case class RequestFeedSubmissionResult(marketplace: Marketplace, feedSubmissionId: String) extends Command

}