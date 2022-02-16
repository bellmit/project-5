package persist.repo;

import com.kindminds.drs.api.data.access.rdb.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestUserRepo {
	
	@Autowired private UserDao repo;
	
	@Test
	public void testQueryUserMail(){
		assertEquals("roger.chen@tw.drs.network",this.repo.queryUserMail(16));
	}
	
	@Test
	public void testIsDrsUser(){
		assertEquals(true,this.repo.isDrsUser(16));
		assertEquals(false,this.repo.isDrsUser(28));
	}

}
