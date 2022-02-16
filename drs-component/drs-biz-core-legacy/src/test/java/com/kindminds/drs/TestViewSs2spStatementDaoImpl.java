package com.kindminds.drs;

import com.kindminds.drs.api.data.access.usecase.accounting.ViewSs2spStatementDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestViewSs2spStatementDaoImpl {

    @Autowired
    ViewSs2spStatementDao viewSs2spStatementDao;

    @Test
    public void testQueryAdvertisingCostReportItems() {

        /*
        List<ProfitShareSubtractionAdvertisingCostReport.ProfitShareSubtractionAdvertisingCostReportLineItem> items =
                viewSs2spStatementDao.queryAdvertisingCostReportItems("STM-K151-79", "US");
        assert(items != null);
        */
    }

    @Test
    public void testQueryOtherRefundReportItems() {

        /*
        List<ProfitShareSubtractionOtherRefundReport.ProfitShareSubtractionOtherRefundReportLineItem> items =
                viewSs2spStatementDao.queryOtherRefundReportItems("STM-K151-79", "US");
        assert(items != null);
        */
    }

    @Test
    public void testQueryMarketingActivityExpenseReportItems() {
        /*
        List<ProfitShareSubtractionMarketingActivityExpenseReport.ProfitShareSubtractionMarketingActivityExpenseReportLineItem> items =
                viewSs2spStatementDao.queryMarketingActivityExpenseReportItems("STM-K151-79", "US");
        assert(items != null);
        */
    }
}
