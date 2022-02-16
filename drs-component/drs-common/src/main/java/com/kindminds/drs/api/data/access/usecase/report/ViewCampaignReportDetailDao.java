package com.kindminds.drs.api.data.access.usecase.report;

import com.kindminds.drs.api.v1.model.marketing.CampaignReportDetail;

import java.util.List;

public interface ViewCampaignReportDetailDao {
    List<CampaignReportDetail> generateCampaignReport(Integer settlementPeriodId);
}