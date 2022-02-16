package com.kindminds.drs.product;

import com.kindminds.drs.persist.data.access.nosql.mongo.ProductDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestGetProductStats {

    private ProductDao productDao = new ProductDao();

    private P2MApplicationDao p2MApplicationDao = new P2MApplicationDao();

    @Test
    public void test(){
        String supplierKcode ="K486";
        int tpn = productDao.findTotalProductNumber(supplierKcode);
        int tsn = productDao.findTotalSkuNumber(supplierKcode);
        int tasn = p2MApplicationDao.findTotalAppliedSkuNumber(supplierKcode);
        int tossn = p2MApplicationDao.findTotalOnSaleSkuNumber(supplierKcode);

        System.out.println("totalProductNumber : " + tpn);
        System.out.println("totalSkuNumber : " + tsn);
        System.out.println("totalAppliedSkuNumber : " + tasn);
        System.out.println("totalOnSaleSkuNumber : " + tossn);
    }

}
