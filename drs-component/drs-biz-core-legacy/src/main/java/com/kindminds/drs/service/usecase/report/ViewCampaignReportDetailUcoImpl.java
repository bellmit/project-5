package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.api.usecase.ViewCampaignReportDetailUco;
import com.kindminds.drs.api.usecase.common.SettlementPeriodListUco;
import com.kindminds.drs.api.v1.model.marketing.CampaignReportDetail;
import com.kindminds.drs.api.data.access.usecase.report.ViewCampaignReportDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewCampaignReportDetailUcoImpl implements ViewCampaignReportDetailUco {

    @Autowired
    ViewCampaignReportDetailDao dao;
    @Autowired
    SettlementPeriodListUco settlementPeriodListUco;

    @Override
    public List<CampaignReportDetail> getCampaignReportDetail() {
        return getCampaignReportDetail(settlementPeriodListUco.getLatestSettlementPeriod().getId());
    }

    @Override
    public List<CampaignReportDetail> getCampaignReportDetail(Integer settlementPeriodId) {
        return dao.generateCampaignReport(settlementPeriodId);
    }
}
