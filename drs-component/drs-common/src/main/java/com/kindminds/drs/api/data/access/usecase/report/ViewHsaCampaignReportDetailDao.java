package com.kindminds.drs.api.data.access.usecase.report;

import com.kindminds.drs.api.v1.model.report.HsaCampaignReportDetail;

import java.util.Date;
import java.util.List;

public interface ViewHsaCampaignReportDetailDao {
    List<HsaCampaignReportDetail> generateHsaCampaignReport(Integer settlementPeriodId);

    List<String> queryCampaignNames(String supplierKcode, Integer marketplaceId, Date start, Date end);

    List<HsaCampaignReportDetail> queryReport(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName);
}