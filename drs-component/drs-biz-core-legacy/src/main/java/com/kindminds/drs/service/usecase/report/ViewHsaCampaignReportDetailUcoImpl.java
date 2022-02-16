package com.kindminds.drs.service.usecase.report;


import com.kindminds.drs.api.data.report.HsaCampaignReport;
import com.kindminds.drs.service.dto.HsaCampaignRptImpl;
import com.kindminds.drs.api.usecase.ViewHsaCampaignReportDetailUco;
import com.kindminds.drs.api.usecase.common.SettlementPeriodListUco;
import com.kindminds.drs.api.v1.model.report.HsaCampaignReportDetail;
import com.kindminds.drs.api.data.access.usecase.report.ViewHsaCampaignReportDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class ViewHsaCampaignReportDetailUcoImpl implements ViewHsaCampaignReportDetailUco {

    @Autowired
    ViewHsaCampaignReportDetailDao dao;
    @Autowired
    SettlementPeriodListUco settlementPeriodListUco;

    @Override
    public List<HsaCampaignReportDetail> getHsaCampaignReportDetail() {
        return getHsaCampaignReportDetail(settlementPeriodListUco.getLatestSettlementPeriod().getId());
    }

    @Override
    public List<HsaCampaignReportDetail> getHsaCampaignReportDetail(Integer settlementPeriodId) {
        return dao.generateHsaCampaignReport(settlementPeriodId);
    }

    @Override
    public List<String> queryCampaignNames(String supplierKcode, Integer marketplaceId, Date start, Date end) {
        return dao.queryCampaignNames(supplierKcode,marketplaceId,start,end);
    }

    @Override
    public HsaCampaignReport queryReport(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName) {

        HsaCampaignRptImpl rpt = new HsaCampaignRptImpl();

        List<HsaCampaignReportDetail> items = dao.queryReport(supplierKcode, marketplaceId, start, end, campaignName);

        if(items != null && !items.isEmpty()) {
            Integer totalImpressions = 0;
            Integer totalClicks = 0;
            BigDecimal totalClickThruRate = null;
            BigDecimal totalCostPerClick = new BigDecimal(0);
            BigDecimal totalSpend = new BigDecimal(0);
            BigDecimal totalSales14days = new BigDecimal(0);
            Integer totalOrders14days = 0;
            Integer totalUnits14days = 0;
            BigDecimal totalConversionRate14days = new BigDecimal(0);

            for (HsaCampaignReportDetail it : items) {

                totalImpressions += it.getImpressions();
                totalClicks += it.getClicks();

                totalSpend = totalSpend.add(it.getTotalSpend());
                totalSales14days = totalSales14days.add((it.getTotalSales14days()));
                totalOrders14days += it.getTotalOrders14days();
                totalUnits14days += it.getTotalUnits14days();
            }
            if(totalClicks > 0) {
                totalClickThruRate = BigDecimal.valueOf(totalClicks).divide(BigDecimal.valueOf(totalImpressions),2, RoundingMode.HALF_UP);
                totalClickThruRate = totalClickThruRate.multiply(BigDecimal.valueOf(100));
            }
            if(totalSpend.intValue() > 0) {
                totalCostPerClick = (totalSpend.divide(new BigDecimal(totalClicks), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                totalSpend = totalSpend.setScale(2, RoundingMode.HALF_UP);
            }
            totalSales14days = totalSales14days.setScale(2, RoundingMode.HALF_UP);
            if(totalSales14days.intValue() > 0)
                totalConversionRate14days = totalSales14days.divide(new BigDecimal(totalClicks),2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);

            rpt.setTotalImpressions(totalImpressions);
            rpt.setTotalClicks(totalClicks);
            rpt.setTotalClickThruRate(totalClickThruRate);
            rpt.setTotalCostPerClick(totalCostPerClick);
            rpt.setTotalSpend(totalSpend);
            rpt.setTotalSales14days(totalSales14days);
            rpt.setTotalOrders14days(totalOrders14days);
            rpt.setTotalUnits14days(totalUnits14days);
            rpt.setTotalConversionRate14days(totalConversionRate14days);
            rpt.setHsaCampaignReportItems(items);
            rpt.setCurrency(items.get(0).getCurrencyName());
        }

        return rpt;
    }
}
