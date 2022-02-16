package persist.traditional.dao;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonCampaignPerformanceReportDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestImportAmazonCampaignPerformanceReportDao {


    @Autowired
    private ImportAmazonCampaignPerformanceReportDao dao;


    @Test
    public void test(){
            dao.updateFromStagingAreaByInfo(Marketplace.AMAZON_COM ,
                    null);
    }
}
