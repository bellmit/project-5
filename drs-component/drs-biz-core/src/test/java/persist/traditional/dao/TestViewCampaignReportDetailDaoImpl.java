package persist.traditional.dao;

import com.kindminds.drs.api.data.access.usecase.report.ViewCampaignReportDetailDao;
import com.kindminds.drs.api.v1.model.marketing.CampaignReportDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestViewCampaignReportDetailDaoImpl {

    @Autowired
    ViewCampaignReportDetailDao dao;

    @Test
    public void testNoSqlError() {
        List<CampaignReportDetail> campaignReportDetails = dao.generateCampaignReport(87);
        if(campaignReportDetails != null)
            assert true;
    }
}
