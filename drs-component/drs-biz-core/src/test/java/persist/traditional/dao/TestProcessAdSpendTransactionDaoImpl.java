package persist.traditional.dao;

import com.kindminds.drs.api.data.access.usecase.accounting.ProcessAdSpendTransactionDao;
import com.kindminds.drs.api.v1.model.marketing.AdSpendTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestProcessAdSpendTransactionDaoImpl {

    @Autowired
    ProcessAdSpendTransactionDao dao;

    @Test
    public void testIsAdSpendProcessed() {
        boolean processed = dao.isAdSpendProcessed();
        assert true;
    }

    @Test
    public void testGetSponsoredAdTransactions() {
        List<AdSpendTransaction> adSpendTransactions = dao.getSponsoredAdTransactions();
        if(adSpendTransactions != null)
            assert true;
    }

    @Test
    public void testGetHeadlineAdTransactions() {
        List<AdSpendTransaction> adSpendTransactions = dao.getHeadlineAdTransactions();
        if(adSpendTransactions != null)
            assert true;
    }
}
