package persist.data.access.nosql.mongo;

import com.kindminds.drs.api.data.access.nosql.mongo.productCategory.ProductCategoryDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.RegionalDao;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.print.Doc;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestRegional {


    private RegionalDao dao = new RegionalDao();



    @Test
    public void test(){

        //ObjectId("60b6e67187cc6e7c8851e447")
        String json = dao.findByP2MApplicationId("60b6e67187cc6e7c8851e447");

        System.out.println(json);

        Document doc = Document.parse(json);

        List<Document> list = (List<Document>) doc.get("certificateFile");

        System.out.println(list.size());

        list.get(0).put("test2","test");


        //list.get(0).append("url" , "test");

        //doc.put("certificateFile",list);

        //System.out.println(list.get(0).toJson());

        //System.out.println(doc.toJson());

    }

}
