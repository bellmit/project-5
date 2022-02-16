package persist.traditional.dao;

import com.kindminds.drs.api.data.access.rdb.accounting.AccountsReceivableAgingDao;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestAccountReceivableAgingReportDaoImpl {


    @Autowired
    private AccountsReceivableAgingDao dao;


    @Test
    public void testGetStatementList(){
        Instant reportDate = Instant.now();

//        reportDate = LocalDateTime.parse("2019-07-03 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                .atZone(ZoneId.of("UTC")).toInstant();
        Instant periodEnd = dao.queryLastSettlementPeriodEnd(reportDate);
        System.out.println("periodEnd: " + periodEnd);
        List<BillStatement> listStatements = dao.queryStatementListBySupplier(periodEnd);
        for (BillStatement statement : listStatements) {
            System.out.println(statement.getReceiverKcode() + ": " + statement.getTotal());
        }
    }

    @Test @Transactional
    public void testCalculateOriginalAccounts(){
//        dao.calculateOriginalAccountsReceivable();
    }

    @Test
    public void testViewARAgingList(){
        System.out.println("aging List size: " + dao.queryAccountReceivableAgingList().size());
    }

    @Test @Transactional
    public void testGenerateAccountsReceivableAgingReport() {
//        dao.generateAccountsReceivableAgingReport();
    }


}
