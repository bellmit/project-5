package persist.repo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseIssueTemplateDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestMaintainCustomerCareCaseIssueTemplateDao {
	
	@Autowired private MaintainCustomerCareCaseIssueTemplateDao dao;
	
	@Test
	public void testQueryOneUserMailAddressOfSupplierInIssue(){
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(18),"K486");
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(22),"K488");
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(23),"K488");
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(24),"K488");
		assertEquals(this.dao.queryKcodeOfSupplierInIssue(27),"K489");
	}
	
	@Test
	public void testQueryEnUsIssueName(){
		assertEquals(this.dao.queryEnUsIssueName(18),"type of chalk to use (loose or ball)");
		assertEquals(this.dao.queryEnUsIssueName(22),"Pass customer to supplier");
		assertEquals(this.dao.queryEnUsIssueName(23),"Unable to get support from manufacturer");
		assertEquals(this.dao.queryEnUsIssueName(24),"Customer offers to write a review in exchange for product");
		assertEquals(this.dao.queryEnUsIssueName(27),"What metrics are available?");
	}
	
}
