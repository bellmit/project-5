package persist.traditional.dao;

import com.kindminds.drs.api.data.access.rdb.marketing.BuyBoxReminderDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestBuyBoxReminderDaoImpl {


    @Autowired
    private BuyBoxReminderDao dao;


    @Test
    public void testGetStatementList(){

        Instant reportDate = dao.queryLatestReportDate();

        reportDate = reportDate.minus(2, ChronoUnit.DAYS);
        System.out.println("reportDate: " + reportDate);

        List<Object []> resultList = dao.queryLowBuyBoxSkus(reportDate);

        /*
        List<BuyBoxReminderData> buyBoxList = new ArrayList<>();
        for (Object[] result : resultList) {
            buyBoxList.add(new BuyBoxReminderDataImpl((Date) result[0],
                    (String) result[1], (String) result[2], (String) result[3],
                    (String) result[4], (String) result[5], (BigDecimal) result[6]));
        }*/

        System.out.println("results size: " + resultList.size());
    }


}
