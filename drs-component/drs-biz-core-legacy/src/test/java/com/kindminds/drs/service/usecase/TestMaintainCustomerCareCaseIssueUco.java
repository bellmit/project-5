package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.service.security.MockAuth;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueTemplateUco;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueUco;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueComment;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueStatus;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.CustomerCareCaseIssueCommentImpl;
import com.kindminds.drs.impl.CustomerCareCaseIssueImpl;
import com.kindminds.drs.impl.CustomerCareCaseIssueTemplateImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainCustomerCareCaseIssueUco {
	
	@Autowired private MaintainCustomerCareCaseIssueUco uco;
	@Autowired private MaintainCustomerCareCaseIssueTemplateUco templateUco;
	@Autowired private AuthenticationManager authenticationManager;
	
	private static String issueType1;
	private static String issueType2;
	private static String issueType3;
	private static String issueType4;
	private static List<String> relatedBseList;
	private static List<String> relatedSkuList;
	
	@BeforeClass
	public static void prepareTestData(){
		issueType1 = "TestIssueType1";
		issueType2 = "TestIssueType2";
		issueType3 = "TestIssueType3";
		issueType4 = "TestIssueType4";
		relatedBseList = new ArrayList<String>(Arrays.asList("BP-K486-BAL", "BP-K486-HL", "BP-K486-KN"));
		relatedSkuList = new ArrayList<String>(Arrays.asList("K486-BAL", "K486-HLB", "K486-HLK", "K486-HLP", "K486-KNB", "K486-KNK", "K486-KNP"));
	}
	
	@Test @Transactional
	public void testGetIssueListWithGeneralIssue(){
		
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		assertTrue(this.inList(52));
		assertTrue(this.inList(46));
		MockAuth.logout();
		
		MockAuth.login(authenticationManager, "peter.yang@nextdrive.io", "craka2AS");
		assertTrue(this.inList(52));
		assertTrue(!this.inList(50));
		assertTrue(!this.inList(46));
		MockAuth.logout();
	}
	
	private boolean inList(int issueId){
		DtoList<CustomerCareCaseIssue> dtoList = this.uco.getList(null, null, 1);
		for(CustomerCareCaseIssue issue:dtoList.getItems()){
			if(issue.getId()==issueId) return true;
		}
		for(int i=2; i<=dtoList.getPager().getTotalPages();i++){
			dtoList = this.uco.getList(null, null, i);
			for(CustomerCareCaseIssue issue:dtoList.getItems()){
				if(issue.getId()==issueId) return true;
			}
		}
		return false;
	}
	
	@Test @Transactional
	public void testCreateIssueWithNullSupplierKcodeForGeneralIssue(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", null, null, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(id, 1, 2, langToNameMap, "ARCHIVE", null, null, null, "0", null, null, null);
		CustomerCareCaseIssue result = this.uco.getIssue(id);
		assertEquals(expect,result);
	}
	
	@Test @Transactional
	public void testCreateIssueWithProductSkuList(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", null, relatedSkuList, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(id, 1, 2, langToNameMap, "ARCHIVE", "K486", null, relatedSkuList, "0", null, null, null);
		CustomerCareCaseIssue result = this.uco.getIssue(id);
		assertEquals(expect,result);
	}
	
	@Test @Transactional
	public void testCreateIssueWithProductBaseList(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(id, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		CustomerCareCaseIssue result = this.uco.getIssue(id);
		assertEquals(expect,result);
	}
	
	@Test @Transactional
	public void testCreateIssueWithDeutschName(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("en_US", "NAME_EN_US");
		langToNameMap.put("de_DE", "NAME_DE_DE");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(id, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		CustomerCareCaseIssue result = this.uco.getIssue(id);
		assertEquals(expect,result);
	}
	
	@Test @Transactional
	public void testGetIssueWithTemplates(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		CustomerCareCaseIssueTemplateImpl templateForTest1 = this.createTemplateObject1(0,id);
		CustomerCareCaseIssueTemplateImpl templateForTest2 = this.createTemplateObject2(0,id);
		int templateId1 = this.templateUco.create(templateForTest1);
		int templateId2 = this.templateUco.create(templateForTest2);
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(id, 1, 2, langToNameMap, "RESPONSE_TEMPLATE_DONE", "K486", relatedBseList, null, "0", null, new ArrayList<CustomerCareCaseIssueTemplate>(), null);
		expect.addTemplate(this.createTemplateObject1(templateId1,id));
		expect.addTemplate(this.createTemplateObject2(templateId2,id));
		CustomerCareCaseIssue result = this.uco.getIssue(id);
		assertEquals(expect,result);
	}
	
	private CustomerCareCaseIssueTemplateImpl createTemplateObject1(int id,int issueId){
		List<String> applicableLocales = new ArrayList<String>(Arrays.asList("zh_TW","en_US"));
		List<String> applicableCaseTypes = new ArrayList<String>(Arrays.asList("CUSTOMER_EMAIL","CUSTOMER_REVIEW"));
		List<String> applicableMarketRegions = new ArrayList<String>(Arrays.asList("US","UK"));
		List<Marketplace> applicableMarketplaceList = new ArrayList<Marketplace>(Arrays.asList(Marketplace.AMAZON_COM,Marketplace.TRUETOSOURCE));
		String content = "How do you turn this on?";
		return new CustomerCareCaseIssueTemplateImpl(id, issueId, applicableLocales, applicableCaseTypes, applicableMarketRegions, applicableMarketplaceList, content);
	}
	
	private CustomerCareCaseIssueTemplateImpl createTemplateObject2(int id,int issueId){
		List<String> applicableLocales = new ArrayList<String>(Arrays.asList("en_US"));
		List<String> applicableCaseTypes = new ArrayList<String>(Arrays.asList("QNA","SALES_FEEDBACK"));
		List<String> applicableMarketRegions = new ArrayList<String>(Arrays.asList("US"));
		List<Marketplace> applicableMarketplaceList = new ArrayList<Marketplace>(Arrays.asList(Marketplace.AMAZON_COM,Marketplace.TRUETOSOURCE));
		String content = "How do you turn this on?";
		return new CustomerCareCaseIssueTemplateImpl(id, issueId, applicableLocales, applicableCaseTypes, applicableMarketRegions, applicableMarketplaceList, content);
	}
	
	@Test @Transactional
	public void testAddCommentByDrsUserWithStatusChange(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		this.uco.addComment(id, true, new CustomerCareCaseIssueCommentImpl(1, "2015", "Roger Chen", "Content For Comment"));
		MockAuth.logout();
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(id, 1, 2, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", relatedBseList, null, "0", null, null, new ArrayList<CustomerCareCaseIssueComment>());
		expect.addComment(new CustomerCareCaseIssueCommentImpl(1, "2015", "Joanna Lee", "Content For Comment") );
		CustomerCareCaseIssue result = this.uco.getIssue(id);
		assertEquals(expect,result);
	}
	
	@Test @Transactional
	public void testAddCommentByDrsUserWithOutStatusChange(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		this.uco.addComment(id, false, new CustomerCareCaseIssueCommentImpl(1, "2015", "Roger Chen", "Content For Comment"));
		MockAuth.logout();
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(id, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, new ArrayList<CustomerCareCaseIssueComment>());
		expect.addComment(new CustomerCareCaseIssueCommentImpl(1, "2015", "Joanna Lee", "Content For Comment") );
		CustomerCareCaseIssue result = this.uco.getIssue(id);
		assertEquals(expect,result);
	}
	
	@Test @Transactional
	public void testAddCommentBySupplierWithStatusChange(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		MockAuth.login(authenticationManager, "hanchor.kmi@tw.drs.network", "9nKRNBC4");
		this.uco.addComment(id, null, new CustomerCareCaseIssueCommentImpl(1, "2015", "Roger Chen", "Content For Comment"));
		MockAuth.logout();
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(id, 1, 2, langToNameMap, "PENDING_DRS_ACTION", "K486", relatedBseList, null, "0", null, null, new ArrayList<CustomerCareCaseIssueComment>());
		expect.addComment(new CustomerCareCaseIssueCommentImpl(1, "2015", "HC view by KMI", "Content For Comment") );
		CustomerCareCaseIssue result = this.uco.getIssue(id);
		assertEquals(expect,result);
	}
	
	@Test @Transactional
	public void testUpdateIssueType(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		Map<String,String> langToNameMapNew = new HashMap<String,String>();
		langToNameMapNew.put("zh_TW", "NAME_TW");
		langToNameMapNew.put("en_US", "NAME_US");
		CustomerCareCaseIssueImpl toUpdate = new CustomerCareCaseIssueImpl(id, 2, 5, langToNameMapNew, "ARCHIVE", "K486", null, relatedSkuList, "0", null, null, null);
		this.uco.update(toUpdate);
		assertEquals(toUpdate,this.uco.getIssue(id));
	}
	
	@Test @Transactional
	public void testUpdateSupplierWithNoChange(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0,  1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		CustomerCareCaseIssueImpl toUpdate = new CustomerCareCaseIssueImpl(id, 2, 5, langToNameMap, "ARCHIVE", "K488", null, relatedSkuList, "0", null, null, null);
		this.uco.update(toUpdate);
		CustomerCareCaseIssueImpl expected = new CustomerCareCaseIssueImpl(id, 2, 5, langToNameMap, "ARCHIVE", "K486", null, relatedSkuList, "0", null, null, null);
		assertEquals(expected,this.uco.getIssue(id));
	}
	
	@Test @Transactional
	public void testDeleteIssue(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl expect = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "ARCHIVE", "K486", relatedBseList, null, null, null, null, null);
		int id = this.uco.createIssue(expect);
		this.uco.deleteIssue(id);
		Assert.assertEquals(null,this.uco.getIssue(id));
	}
	
	@Test @Transactional
	public void testGetIssueTypeToIssuesMapByCategory(){
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.uco.getIssueTypeToIssuesMap(null,null,3);
		assertTrue(issueTypeToIssuesMap.containsKey("International shipping"));
		assertTrue(issueTypeToIssuesMap.get("International shipping").containsKey(42));
		assertTrue(issueTypeToIssuesMap.get("International shipping").get(42).equals("Shipping to Asia"));
	}
	
	@Test @Transactional
	public void testGetIssueTypeToIssuesMapByBaseList(){
		List<String> baseList = new ArrayList<String>(Arrays.asList("BP-K486-KN"));
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.uco.getIssueTypeToIssuesMap(baseList,null,null);
		assertTrue(issueTypeToIssuesMap.containsKey("Product details"));
		assertTrue(issueTypeToIssuesMap.get("Product details").get(18).equals("type of chalk to use (loose or ball)"));
	}

	@Test @Transactional
	public void testGetIssueTypeToIssuesMapBySkuList(){
		List<String> skuList = new ArrayList<String>(Arrays.asList("K488-PLUG100"));
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.uco.getIssueTypeToIssuesMap(null,skuList,null);
		assertTrue(issueTypeToIssuesMap.containsKey("International shipping"));
		assertTrue(issueTypeToIssuesMap.get("International shipping").get(42).equals("Shipping to Asia"));

	}
	
	@Test @Transactional
	public void testGetGeneralIssueTypeToIssuesMapWithSkuList(){
		List<String> skuList1ToSearch = new ArrayList<String>(Arrays.asList("K486-BAL"));
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.uco.getIssueTypeToIssuesMap(null,skuList1ToSearch,2);
		assertTrue(issueTypeToIssuesMap.get("Return & replace").get(40).equals("Amazon | Past 30 days | Ship to our warehouse"));
		assertTrue(issueTypeToIssuesMap.get("Return for refund").get(37).equals("Amazon | Within 30 Days"));
	}
	
	@Test @Transactional
	public void testGetCategoryIdToNameMap(){
		Map<Integer,String> categoryIdToNameMap = this.uco.getCategoryIdToNameMap();
		assertEquals("PRODUCT",categoryIdToNameMap.get(1));
	}
	
	@Test @Transactional
	public void testCreateIssueType(){
		String issueTypeName = "testIssueType1";
		int typeId = this.uco.createIssueType(1, issueTypeName);
		Map<Integer,Map<Integer,String>> issueCategoryToIssueTypeAndIdMap = this.uco.getCategoryIdToIssueTypeAndIdMap();
		assertTrue(issueCategoryToIssueTypeAndIdMap.get(1).get(typeId).equals(issueTypeName));
	}
	
	@Test
	public void testGetIssueStatusList(){
		List<String> result = this.uco.getIssueStatusList();
		for(CustomerCareCaseIssueStatus status:CustomerCareCaseIssueStatus.values()){
			assertTrue(result.contains(status.name()));
		}
	}
	
	@Test @Transactional
	public void testGetEnUsIssueName(){
		this.createIssueTypeAndCategoryForTest();
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate = new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", null, relatedSkuList, "0", null, null, null);
		int id = this.uco.createIssue(toCreate);
		Assert.assertEquals("NAME_EN_US",this.uco.getEnUsIssueName(id));
	}
	
	@Test
	public void testGetIssueCategoryToIssueTypeAndIdMap(){
		Map<Integer,Map<Integer,String>> resultMap = this.uco.getCategoryIdToIssueTypeAndIdMap();
		assertEquals("Bulk order discount",resultMap.get(4).get(13));
		assertEquals("Faulty unit discount",resultMap.get(4).get(14));
	}
	
	@Test @Transactional
	public void testUpdateType(){
		this.uco.updateType(13, "GOGOFIGHTER");
		Map<Integer,Map<Integer,String>> resultMap = this.uco.getCategoryIdToIssueTypeAndIdMap();
		assertEquals("GOGOFIGHTER",resultMap.get(4).get(13));
	}
	
	@Test @Transactional
	public void testGetAllTypeIdToNameMap(){
		Map<Integer,String> resultMap = this.uco.getTypeIdToNameMap();
		assertEquals("Alternative products",resultMap.get(16));
		assertEquals("Wholesale",resultMap.get(17));
	}
	
	@Test @Transactional
	public void testGetTypeIdToNameMap(){
		Map<Integer,String> resultMap = this.uco.getTypeIdToNameMap(1);
		assertEquals(4,resultMap.size());
		assertEquals("Product details",resultMap.get(1));
		assertEquals("Product usage",resultMap.get(2));
		assertEquals("Faulty product",resultMap.get(3));
		assertEquals("Recommend improvements",resultMap.get(4));
	}
	
	private void createIssueTypeAndCategoryForTest(){
		this.uco.createIssueType(1,issueType1);
		this.uco.createIssueType(1,issueType2);
		this.uco.createIssueType(2,issueType3);
		this.uco.createIssueType(2,issueType4);
	}
	
	@Test
	public void testGetListElasticSearchNoMatch() {
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		DtoList<CustomerCareCaseIssue> list = uco.getListElastic("1v22fd#$%2ab", 1);
		Assert.assertEquals(0, list.getTotalRecords());
		Assert.assertEquals(0, list.getItems().size());
		MockAuth.logout();
	}
	
	@Test
	public void testGetListElasticEmptySearch() {
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		DtoList<CustomerCareCaseIssue> list = uco.getListElastic("", 1);
		System.out.println("total records: " + list.getTotalRecords());
		assertTrue(list.getTotalRecords() > 1000);
		MockAuth.logout();
	}
	
	@Test
	public void testGetListElasticSearchByStatus() {
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		DtoList<CustomerCareCaseIssue> list = uco.getListElastic("response templ", 1);
		System.out.println("total records: " + list.getTotalRecords());
		assertTrue(list.getTotalRecords() > 700);
		MockAuth.logout();
	}
	
}
