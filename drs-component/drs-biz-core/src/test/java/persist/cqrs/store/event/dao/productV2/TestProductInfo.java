package persist.cqrs.store.event.dao.productV2;

import com.kindminds.drs.Country;
import com.kindminds.drs.Filter;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductVariationDao;
import com.kindminds.drs.api.data.cqrs.store.read.queries.ProductViewQueries;

import com.kindminds.drs.api.data.transfer.productV2.ProductDto;


import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.ProductRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestProductInfo {

    @Autowired
    ProductDao pd;

    @Autowired
    ProductViewQueries pvq;

    @Autowired
    ProductVariationDao pvd;

    ProductRepo pr = new ProductRepo() {


        @Override
        public Optional<Product> find(String id, Country marketSide) {
            return Optional.empty();
        }

        @Override
        public List<Product> findAllMarketSide(String id) {
            return null;
        }

        @Override
        public void add(Product item) {

        }

        @Override
        public void add(List<Product> items) {

        }

        @Override
        public void edit(Product item) {

        }

        @Override
        public void remove(Product item) {

        }

        @Override
        public Optional<Product> findById(String id) {

            List<Object[]> resultList = pd.get(id , Country.CORE);
            if(resultList.size() >0 ){

                Object [] obj = resultList.get(0);
                String pid = obj[0].toString();
                OffsetDateTime ct =
                        OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

                Country pms =Country.fromKey(Integer.parseInt(obj[2].toString()));


                String baseCode = obj[3].toString();
                String data = obj[4].toString();
                Boolean activiate = obj[5].toString() == "1" ? true:false;


                ProductEditingStatusType status =
                        ProductEditingStatusType.fromKey(Integer.parseInt(obj[6].toString()));

//                ProductImpl p = new ProductImpl(pid , ct , pms ,baseCode,data,activiate  , status);

                System.out.println(pid);
                System.out.println(ct);
                System.out.println(pms);
                System.out.println(baseCode);
                System.out.println(data);
                System.out.println(activiate);
                System.out.println(status);
            }

            return Optional.empty();
        }

        @Override
        public Optional<Product> findOne(Filter filter) {
            return Optional.empty();
        }

        @Override
        public List<Product> find(Filter filter) {
            return null;
        }
    };

    @Test
    public void testgetid() {

        Optional<String> result = pd.getId("BP-K612-8BSMAP0118D0700", Country.CORE);
        System.out.println(result);
        String productId = result.get();
        System.out.println(productId);
        System.out.println("11111");

        List<Object[]> resultList = pd.get(productId, Country.CORE);
        System.out.println(resultList);
        if(resultList.size() >0 ){

            Object [] obj = resultList.get(0);
            String pid = obj[0].toString();
            OffsetDateTime ct =
                    OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

            Country pms =Country.fromKey(Integer.parseInt(obj[2].toString()));

            String baseCode = obj[3].toString();
            String data = obj[4].toString();
            Boolean activiate = obj[5].toString() == "1" ? true:false;


            ProductEditingStatusType status =
                    ProductEditingStatusType.fromKey(Integer.parseInt(obj[6].toString()));

            List<Object[]> variation = pvd.get(productId,ct,Country.CORE);
            System.out.println(variation);
                Object [] obj1 = variation.get(0);
                String vid = obj1[0].toString();
                String variationCode = obj1[2].toString();
                String variationdata = obj1[3].toString();

                System.out.println(vid);
                System.out.println(variationCode);
                System.out.println(variationdata);

            System.out.println(variation.size());
            System.out.println("product");
            System.out.println(pid);
            System.out.println(ct);
            System.out.println(pms);
            System.out.println(baseCode);
            System.out.println(data);
            System.out.println(activiate);
            System.out.println(status);

            System.out.println("variation");

        }

    }

    @Test
    public void testgetActivateBaseProductOnboarding(){
        ProductDto gapo = pvq.getActivateBaseProductOnboarding("BP-K612-8BSMAP0118D0700");
        System.out.println(gapo);
        System.out.println("22222");
    }

    @Test
    public void testfind(){
        Optional<Product> ff = pr.findById("ba226621-68d2-4e1c-a266-2168d27e1cf4");
        System.out.println(ff);
        System.out.println("33333");
    }

    @Test
    public void testgetIds(){
        Optional<List<String>> Variationid = pvd.getIds("ba226621-68d2-4e1c-a266-2168d27e1cf4",Country.CORE);
        System.out.println(Variationid);
        List<String> obj1 = Variationid.get();
        System.out.println(obj1);
        System.out.println("66666");

//        List<Object[]> Variation = pvd.get("ba226621-68d2-4e1c-a266-2168d27e1cf4","2019-08-31T23:4905.629+08:00",Country.CORE);
    }

}

