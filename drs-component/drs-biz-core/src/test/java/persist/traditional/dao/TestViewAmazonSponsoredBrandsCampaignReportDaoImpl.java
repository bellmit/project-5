package persist.traditional.dao;

import com.kindminds.drs.api.data.access.usecase.report.ViewAmazonSponsoredBrandsCampaignReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestViewAmazonSponsoredBrandsCampaignReportDaoImpl {

    @Autowired
    ViewAmazonSponsoredBrandsCampaignReportDao dao;

    @Test
    public void testQueryCampaignNamesNoExceptions() {
        List<String> campaignNames = dao.queryCampaignNames("K510", 1, null, null);
        assert true;
    }

    @Test
    public void testQueryReportNoExceptions() {
        List<AmazonHeadlineSearchAdReportItem> amzItem = dao.queryReportDaily("K510", 1,null,null, "Headline Search Ads New");
        assert true;
    }
}
