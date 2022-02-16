package persist.traditional.dao;

import com.kindminds.drs.api.data.access.rdb.shipment.ShipmentSkuIdentificationDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestShipmentSkuIdentificationDaoImpl {


    @Autowired
    private ShipmentSkuIdentificationDao dao;


    @Test
    @Transactional("transactionManager")
    public void test() throws Exception {

        //List<Object []> resultList = dao.query();


        //dao.udpate();

        // resultList = dao.query2();

         Thread.sleep(5000);
        throw new Exception();

       // resultList = dao.query();

       // System.out.println(resultList.get(0)[0]);


    }


}
