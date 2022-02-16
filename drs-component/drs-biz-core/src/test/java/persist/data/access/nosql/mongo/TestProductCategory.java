package persist.data.access.nosql.mongo;

import com.kindminds.drs.api.data.access.nosql.mongo.productCategory.ProductCategoryDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestProductCategory {

    @Autowired
    private ProductCategoryDao dao;

    String testParent = "Arts & Crafts";
    //Toys & Games,Collectible Toys,Collectible Display & Storage,Card Storage & Display,Protective Sleeves

    @Test
    public void testfindByParent(){
        String result = this.dao.findByParent(testParent);
        System.out.println(result);
    }

    @Test
    public void testfindList(){
        String result = this.dao.findList(testParent);
        System.out.println(result);
    }

}
