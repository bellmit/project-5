package persist.repo;

import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseIssueDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestMaintainCustomerCareCaseIssueDao {
	
	@Autowired private MaintainCustomerCareCaseIssueDao dao;
	
	@Test
	public void testQueryOneUserMailAddressOfIssueSupplier(){
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(18),"K486");
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(22),"K488");
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(23),"K488");
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(24),"K488");
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(27),"K489");
	}
	
	@Test
	public void testQuerySupplierKcodeOfIssue(){
		assertEquals(this.dao.querySupplierKcodeOfIssue(18),"K486");
		assertEquals(this.dao.querySupplierKcodeOfIssue(22),"K488");
		assertEquals(this.dao.querySupplierKcodeOfIssue(23),"K488");
		assertEquals(this.dao.querySupplierKcodeOfIssue(24),"K488");
		assertEquals(this.dao.querySupplierKcodeOfIssue(27),"K489");
	}
	
	@Test
	public void testQueryEmailOfNewestDrsCommenter(){
		assertEquals(this.dao.queryEmailOfNewestDrsCommenter(23),"rosalie.chen@us.drs.network");
		assertEquals(this.dao.queryEmailOfNewestDrsCommenter(27),"rosalie.chen@tw.drs.network");
		assertEquals(this.dao.queryEmailOfNewestDrsCommenter(28),"rosalie.chen@us.drs.network");
		assertEquals(this.dao.queryEmailOfNewestDrsCommenter(29),"ruby.chen@tw.drs.network");
	}
}
