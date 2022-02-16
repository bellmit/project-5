package com.kindminds.drs.api.usecase;


import com.kindminds.drs.api.data.report.HsaCampaignReport;
import com.kindminds.drs.api.v1.model.report.HsaCampaignReportDetail;

import java.util.Date;
import java.util.List;

public interface ViewHsaCampaignReportDetailUco {
    /**
     * Uses latest settlement period
     * @return List<HSACampaignReportDetail>
     */
    List<HsaCampaignReportDetail> getHsaCampaignReportDetail();
    List<HsaCampaignReportDetail> getHsaCampaignReportDetail(Integer settlementPeriodId);

    List<String> queryCampaignNames(String supplierKcode, Integer marketplaceId, Date start, Date end);
    HsaCampaignReport queryReport(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName);
}