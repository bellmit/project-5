package persist.traditional.dao;

import com.kindminds.drs.api.data.access.usecase.accounting.ProcessMarketSideTransactionDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestProcessMarketSideTransactionDaoImpl {

    @Autowired
    ProcessMarketSideTransactionDao dao;

    @Test
    public void testMarketSideTransactionException() {
        dao.deleteMarketSideTransactionException(1);
        dao.insertMarketSideTransactionException(1, "Test Message", "Test Stacktrace");
        dao.deleteMarketSideTransactionException(1);
    }
}
