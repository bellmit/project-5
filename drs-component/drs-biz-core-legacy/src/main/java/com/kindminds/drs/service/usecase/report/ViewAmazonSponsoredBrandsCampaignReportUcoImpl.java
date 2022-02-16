package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewAmazonSponsoredBrandsCampaignReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;
import com.kindminds.drs.api.v1.model.report.ViewAmazonSponsoredBrandsCampaignReport;
import com.kindminds.drs.api.data.access.usecase.report.ViewAmazonSponsoredBrandsCampaignReportDao;
import com.kindminds.drs.v1.model.impl.ViewAmazonSponsoredBrandsCampaignReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViewAmazonSponsoredBrandsCampaignReportUcoImpl implements ViewAmazonSponsoredBrandsCampaignReportUco {

    @Autowired
    com.kindminds.drs.api.data.access.rdb.CompanyDao CompanyDao;

    @Autowired
    ViewAmazonSponsoredBrandsCampaignReportDao viewAmazonSponsoredBrandsCampaignReportDao;

    @Override
    public Map<String, String> getSupplierKcodeToShortEnNameMap() {
        return this.CompanyDao.querySupplierKcodeToShortEnUsNameMap();
    }

    @Override
    public Map<Integer, String> getMarketplaces() {
        Map<Integer, String> marketplaces = new HashMap<>();
        marketplaces.put(Marketplace.AMAZON_COM.getKey(), Marketplace.AMAZON_COM.getName());
        marketplaces.put(Marketplace.AMAZON_CO_UK.getKey(), Marketplace.AMAZON_CO_UK.getName());
        marketplaces.put(Marketplace.AMAZON_CA.getKey(), Marketplace.AMAZON_CA.getName());
        marketplaces.put(Marketplace.AMAZON_DE.getKey(), Marketplace.AMAZON_DE.getName());
        marketplaces.put(Marketplace.AMAZON_FR.getKey(), Marketplace.AMAZON_FR.getName());
        marketplaces.put(Marketplace.AMAZON_ES.getKey(), Marketplace.AMAZON_ES.getName());
        marketplaces.put(Marketplace.AMAZON_IT.getKey(), Marketplace.AMAZON_IT.getName());
        marketplaces.put(Marketplace.AMAZON_COM_MX.getKey(), Marketplace.AMAZON_COM_MX.getName());
        return marketplaces;
    }

    @Override
    public List<String> queryCampaignNames(String supplierKcode, Integer marketplaceId, Date start, Date end) {
        return viewAmazonSponsoredBrandsCampaignReportDao.queryCampaignNames(supplierKcode, marketplaceId, start, end);
    }

    @Override
    public ViewAmazonSponsoredBrandsCampaignReport queryReport(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName) {
        ViewAmazonSponsoredBrandsCampaignReportImpl viewAmazonSponsoredBrandsCampaignReport = new ViewAmazonSponsoredBrandsCampaignReportImpl();

        List<AmazonHeadlineSearchAdReportItem> amazonHeadlineSearchAdReportItems =
                viewAmazonSponsoredBrandsCampaignReportDao.queryReportByPeriod(supplierKcode, marketplaceId, start, end,
                        campaignName);

        if(amazonHeadlineSearchAdReportItems != null && !amazonHeadlineSearchAdReportItems.isEmpty()) {
            Integer totalImpressions = 0;
            Integer totalClicks = 0;
            BigDecimal totalClickThruRate = null;
            BigDecimal totalCostPerClick = new BigDecimal(0);
            BigDecimal totalSpend = new BigDecimal(0);
            BigDecimal totalSales14days = new BigDecimal(0);
            Integer totalOrders14days = 0;
            Integer totalUnits14days = 0;
            BigDecimal totalConversionRate14days = new BigDecimal(0);

            for (AmazonHeadlineSearchAdReportItem amazonHeadlineSearchAdReportItem : amazonHeadlineSearchAdReportItems) {
                totalImpressions += amazonHeadlineSearchAdReportItem.getImpressions();
                totalClicks += amazonHeadlineSearchAdReportItem.getClicks();

                totalSpend = totalSpend.add(amazonHeadlineSearchAdReportItem.getSpend());
                totalSales14days = totalSales14days.add((amazonHeadlineSearchAdReportItem.getTotalSales14days()));
                totalOrders14days += amazonHeadlineSearchAdReportItem.getTotalOrders14days();
                totalUnits14days += amazonHeadlineSearchAdReportItem.getTotalUnits14days();
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

            viewAmazonSponsoredBrandsCampaignReport.setTotalImpressions(totalImpressions);
            viewAmazonSponsoredBrandsCampaignReport.setTotalClicks(totalClicks);
            viewAmazonSponsoredBrandsCampaignReport.setTotalClickThruRate(totalClickThruRate);
            viewAmazonSponsoredBrandsCampaignReport.setTotalCostPerClick(totalCostPerClick);
            viewAmazonSponsoredBrandsCampaignReport.setTotalSpend(totalSpend);
            viewAmazonSponsoredBrandsCampaignReport.setTotalSales14days(totalSales14days);
            viewAmazonSponsoredBrandsCampaignReport.setTotalOrders14days(totalOrders14days);
            viewAmazonSponsoredBrandsCampaignReport.setTotalUnits14days(totalUnits14days);
            viewAmazonSponsoredBrandsCampaignReport.setTotalConversionRate14days(totalConversionRate14days);
            viewAmazonSponsoredBrandsCampaignReport.setAmazonHeadlineSearchAdReportItems(amazonHeadlineSearchAdReportItems);
            viewAmazonSponsoredBrandsCampaignReport.setCurrency(amazonHeadlineSearchAdReportItems.get(0).getCurrency());
        }

        return viewAmazonSponsoredBrandsCampaignReport;
    }

}
