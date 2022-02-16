package com.drs.sys.service.control;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewAmazonSponsoredBrandsCampaignReportUco;
import com.kindminds.drs.api.v1.model.report.ViewAmazonSponsoredBrandsCampaignReport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestViewAmazonSponsoredBrandsCampaignReportUcoImpl {

    @Autowired
    ViewAmazonSponsoredBrandsCampaignReportUco uco;

    @Test
    public void testQueryCampaignNamesNoExceptions() {
        List<String> campaignNames = uco.queryCampaignNames("K510", 1, null, null);
        assert(campaignNames != null);
    }

    @Test
    public void testQueryCampaignNamesDateFilter() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse("2018-06-01");
        Date endDate = simpleDateFormat.parse("2018-10-01");
        List<String> campaignNames = uco.queryCampaignNames("K510", 1, startDate, endDate);
        assert(campaignNames != null);
    }

    @Test
    public void testMarketPlaceNoExceptions() {
        Map<Integer,String> marketplaces = uco.getMarketplaces();
        assert(marketplaces != null);
        assert(!marketplaces.isEmpty());
    }

    @Test
    public void testQueryReportNoExceptions() {
        ViewAmazonSponsoredBrandsCampaignReport report = uco.queryReport("K510", 1, null, null, null);
        assert true;
    }

    @Test
    public void testSupplierKcodeToShortEnNameMap() {
        Map<String,String> supplierMap = uco.getSupplierKcodeToShortEnNameMap();
        assert(supplierMap != null);
        assert(!supplierMap.isEmpty());
    }
}
