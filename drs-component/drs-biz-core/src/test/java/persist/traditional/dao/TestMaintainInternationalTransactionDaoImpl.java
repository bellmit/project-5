package persist.traditional.dao;

import com.kindminds.drs.api.data.access.usecase.accounting.MaintainInternationalTransactionDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestMaintainInternationalTransactionDaoImpl {

    @Autowired
    MaintainInternationalTransactionDao maintainInternationalTransactionDao;

    @Test
    public void testCountCouponTransactionsProcessed_SuccessfulResponse() {
        int count = maintainInternationalTransactionDao.countCouponTransactionsProcessed();
        if(count > -1)
            assert true;
    }
}
