package com.kindminds.drs.service.control;

import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionAdvertisingCostReport;
import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionMarketingActivityExpenseReport;
import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionOtherRefundReport;


import com.kindminds.drs.service.usecase.accounting.ViewSs2spStatementUcoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestViewSs2spStatementUcoImpl {

    @Autowired
    private ViewSs2spStatementUcoImpl uco;

    @Test
    public void testGetSs2spAdvertisingCostReport() {
        ProfitShareSubtractionAdvertisingCostReport report = uco.getSs2spAdvertisingCostReport("STM-K600-8", "US");
        assert(report != null);
    }

    @Test
    public void testGetSs2spOtherRefundReport() {
        ProfitShareSubtractionOtherRefundReport report = uco.getSs2spOtherRefundReport("STM-K600-8", "US");
        assert(report != null);
    }

    @Test
    public void testGetSs2spMarketingActivityExpenseReport() {
        ProfitShareSubtractionMarketingActivityExpenseReport report = uco.getSs2spMarketingActivityExpenseReport("STM-K600-8", "US");
        assert(report != null);
    }
}
