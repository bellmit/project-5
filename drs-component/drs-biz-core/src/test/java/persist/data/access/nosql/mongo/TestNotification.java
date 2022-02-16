package persist.data.access.nosql.mongo;


import com.kindminds.drs.persist.data.access.nosql.mongo.notification.NotificationDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestNotification {


    private NotificationDao dao = new NotificationDao();

    //String testParent = "2021-03-31 03:39:34.8554726";
    String testParent = "{\"timestamp\": \"1616566371815\"}";
    @Test
    public void testsave(){
        //this.dao.save(testParent);
    }

    @Test
    public void testFindByUserId(){
       String s = this.dao.findByUserId(389 , 5);
       System.out.println(s);
    }



}