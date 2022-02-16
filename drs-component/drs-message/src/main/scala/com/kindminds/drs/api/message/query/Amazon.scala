package com.kindminds.drs.api.message.query.amazon

package report{


  import com.kindminds.drs.{Marketplace, MwsReportType}
  import com.kindminds.drs.api.message.Query

  case class GetRequestList(requestId: Option[String] , marketplace: Marketplace , reportType : MwsReportType) extends Query


  case class GetReportListByNextToken(requestId: String , marketplace: Marketplace ,  reportType : MwsReportType) extends Query

}