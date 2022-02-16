package com.kindminds.drs.api.message

package CampaignReport {

  /**
    * Uses the latest settlement period
    *
    * Return - List<CampaignReportDetail>
    */
  case class GetCampaignReportDetail() extends Query

  /**
    * @param settlementPeriodId - Settlement Period ID
    *
    * Return - List<CampaignReportDetail>
    */
  case class GetCampaignReportDetailBySettlementPeriod(settlementPeriodId: Integer) extends Query

}