package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.marketing.CampaignReportDetail;

import java.util.List;

public interface ViewCampaignReportDetailUco {
    /**
     * Uses latest settlement period
     * @return List<CampaignReportDetail>
     */
    List<CampaignReportDetail> getCampaignReportDetail();
    List<CampaignReportDetail> getCampaignReportDetail(Integer settlementPeriodId);
}