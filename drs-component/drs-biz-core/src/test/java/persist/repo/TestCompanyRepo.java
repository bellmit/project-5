package persist.repo;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestCompanyRepo {
	
	@Autowired private CompanyDao repo;
	
	@Test
	public void testQueryAllCompanyKcodeList(){
		List<String> result = this.repo.queryAllCompanyKcodeList();
		assertTrue(result.contains("K2"));
		assertTrue(result.contains("K3"));
		assertTrue(result.contains("K408"));
		assertTrue(result.contains("K448"));
	}
	
	@Test
	public void testQuerySupplierKcodeList(){
		List<String> result = this.repo.querySupplierKcodeList();
		assertTrue(result.contains("K408"));
		assertTrue(result.contains("K448"));
	}
	
	@Test
	public void testQueryDrsCompanyKcodeList(){
		List<String> result = this.repo.queryDrsCompanyKcodeList();
		assertTrue(result.size()==2);
		assertTrue(result.contains("K2"));
		assertTrue(result.contains("K3"));
	}
	
	@Test
	public void testQueryAllCompanyKcodeToNameMap(){
		Map<String,String> result = this.repo.queryAllCompanyKcodeToShortEnUsNameMap();
		assertTrue(result.containsKey("K2"));
		assertTrue(result.containsKey("K3"));
		assertTrue(result.containsKey("K408"));
		assertTrue(result.containsKey("K448"));
	}
	
	@Test
	public void testQuerySupplierKcodeToNameMap(){
		Map<String,String> result = this.repo.querySupplierKcodeToShortLocalNameMap();
		assertTrue(result.containsKey("K408"));
		assertTrue(result.containsKey("K448"));
	}
	
	@Test
	public void testQueryDrsCompanyKcodeToNameMap(){
		Map<String,String> result = this.repo.queryDrsCompanyKcodeToShortEnUsNameMap();
		assertTrue(result.size()==2);
		assertTrue(result.containsKey("K3"));
		assertTrue(result.containsKey("K2"));
	}
	
	@Test
	public void testQueryDrsCompanyKcodeToCountryCodeMap(){
		Map<String,String> result = this.repo.queryDrsCompanyKcodeToCountryCodeMap();
		assertTrue(result.size()==2);
		assertTrue(result.containsKey("K2"));
		assertEquals("TW",result.get("K2"));
		assertTrue(result.containsKey("K3"));
		assertEquals("US",result.get("K3"));
	}
	
	@Test
	public void testQuerySupplierServiceEmailAddress(){
		assertEquals(Arrays.asList("junping@hanchor.com"),this.repo.querySupplierServiceEmailAddressList("K486"));
		assertEquals(Arrays.asList("irene@ecopl.com.tw"),this.repo.querySupplierServiceEmailAddressList("K151"));
		assertEquals(Arrays.asList("morris.ho@cpss.com.tw"),this.repo.querySupplierServiceEmailAddressList("K489"));
		assertEquals(Arrays.asList("peter.yang@nextdrive.io"),this.repo.querySupplierServiceEmailAddressList("K488"));
		assertEquals(Arrays.asList("cherry@seehot.com.tw"),this.repo.querySupplierServiceEmailAddressList("K490"));
		assertEquals(Arrays.asList("frank.liu@alfheimtec.com"),this.repo.querySupplierServiceEmailAddressList("K491"));
		assertEquals(Arrays.asList("susan.chen@serafim-tech.com"),this.repo.querySupplierServiceEmailAddressList("K492"));
	}

}
