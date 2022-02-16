package persist.repo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestMaintainCustomerCareCaseDao {
	
	@Autowired private MaintainCustomerCareCaseDao dao;
	
	@Test
	public void testQueryOneUserMailAddressOfSupplierInOneCase(){
		assertEquals(this.dao.querySupplierKcodeOfCase(1),"K486");
		assertEquals(this.dao.querySupplierKcodeOfCase(3),"K488");
		assertEquals(this.dao.querySupplierKcodeOfCase(4),"K488");
		assertEquals(this.dao.querySupplierKcodeOfCase(6),"K488");
		assertEquals(this.dao.querySupplierKcodeOfCase(7),"K489");
	}
}
