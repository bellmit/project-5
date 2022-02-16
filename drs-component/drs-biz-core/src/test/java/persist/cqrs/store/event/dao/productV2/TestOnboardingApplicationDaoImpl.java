package persist.cqrs.store.event.dao.productV2;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductVariationDao;
import com.kindminds.drs.api.data.cqrs.store.read.queries.OnboardingApplicationViewQueries;
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries;

import com.kindminds.drs.api.data.access.rdb.sales.DailySalesDao;

import com.kindminds.drs.api.data.transfer.productV2.onboarding.OnboardingApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestOnboardingApplicationDaoImpl {

    @Autowired
    ProductViewQueries queries;

    @Autowired
    OnboardingApplicationViewQueries oaQueries;

    @Autowired
    ProductDao pDao;

    @Autowired
    ProductVariationDao vDao;

    @Autowired
    DailySalesDao dsDao;

    @Test
    public void testDs() {

        /*
        List<DailySalesRow> rList = dsDao.getTodayDailySales("K510");
        System.out.println(rList.size());

        System.out.println(rList.get(0).getId());
        System.out.println(rList.get(0).getRevenueUsd());
        */
    }

    @Test
    public void querySerialNumbersBySupplier() {
        Optional<List<String>> serialNumbers = oaQueries.getSerialNumbersBySupplier("K486");
    }

    @Test
    public void queryOnboardingApplications() {
        List<OnboardingApplication> oal = oaQueries.getOnboardingApplications();
    }

    @Test
    public void queryOnboardingApplicationsBySupplier() {
        List<OnboardingApplication> oal = oaQueries.getOnboardingApplications("K486");
    }

    @Test
    public void testQueryBaseCodesToMarketplacesMap() {
        Map<String, List<String>> baseCodesToMarketplacesMap =
                queries.getBaseCodesToMarketplacesMap("K510");
        System.out.println(baseCodesToMarketplacesMap.size());
        for (String baseCode : baseCodesToMarketplacesMap.keySet()) {
            System.out.println(baseCode + ": " + baseCodesToMarketplacesMap.get(baseCode));
        }
    }

    @Test
    public void testQueryBaseCodesToMarketplacesMap2() {

        List<Object[]> resultList =  pDao.get("3b53136d-45b8-4edc-9313-6d45b80edc7e", Country.CA);
       System.out.println(resultList.size());

        Object [] obj = resultList.get(0);
        String pid = obj[0].toString();
        OffsetDateTime ct =
                OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

        Country pms =Country.fromKey(Integer.parseInt(obj[2].toString()));

        System.out.println(pid);
        System.out.println(ct);
        System.out.println(pms);

        resultList =  vDao.get(pid,ct,pms);
        System.out.println(resultList.size());




    }
}
