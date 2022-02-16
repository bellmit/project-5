package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;

import com.kindminds.drs.service.security.MockAuth;
import com.kindminds.drs.impl.CustomerCareCaseDtoImplForTest;
import com.kindminds.drs.impl.CustomerCareCaseIssueImpl;
import com.kindminds.drs.impl.CustomerCareCaseIssueTemplateImpl;
import com.kindminds.drs.impl.CustomerCareCaseMessageImplForTest;
import com.kindminds.drs.impl.CustomerCareCaseOrderInfoImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseUco;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage.MessageType;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseType;
import com.kindminds.drs.api.v1.model.common.DtoList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainCustomerCareCaseUco {
	
	@Autowired private MaintainCustomerCareCaseUco uco;
	@Autowired private MaintainCustomerCareCaseIssueUco issueUco;
	@Autowired private MaintainCustomerCareCaseIssueTemplateUco templateUco;
	@Autowired private AuthenticationManager authenticationManager;

	private static String issueType1;
	private static String issueType2;
	private static List<String> relatedBseList;
	private static List<String> relatedSkuList;
	
	
	@BeforeClass
	public static void prepareTestData(){
		issueType1 = "TestIssueType1";
		issueType2 = "TestIssueType2";
		relatedBseList = new ArrayList<String>(Arrays.asList("BP-K486-BAL", "BP-K486-HL", "BP-K486-KN"));
		relatedSkuList = new ArrayList<String>(Arrays.asList("K486-BAL", "K486-HLB", "K486-HLK", "K486-HLP", "K486-KNB", "K486-KNK", "K486-KNP"));
	}
	
	@Test @Transactional
	public void testGetTemplateContent(){
		List<Integer> testIssueIds = this.getIssueIdsForTest();
		int templateId1 = this.templateUco.create(this.createTemplateObject1(0, testIssueIds.get(0)));
		int templateId2 = this.templateUco.create(this.createTemplateObject2(0, testIssueIds.get(1)));
		int templateId3 = this.templateUco.create(this.createTemplateObject3(0, testIssueIds.get(2)));
		Assert.assertEquals("How do you turn this on?", this.uco.getTemplateContent(templateId1));
		Assert.assertEquals("cheese steak jimmy's", this.uco.getTemplateContent(templateId2));
		Assert.assertEquals("furious the monkey boy", this.uco.getTemplateContent(templateId3));
	}
	
	@Test @Transactional
	public void testGetIssueTemplateByCustomerCareCaseId(){
		List<Integer> testIssueIds = this.getIssueIdsForTest();
		int templateId1 = this.templateUco.create(this.createTemplateObject1(0, testIssueIds.get(0)));
		int templateId2 = this.templateUco.create(this.createTemplateObject2(0, testIssueIds.get(1)));
		int templateId3 = this.templateUco.create(this.createTemplateObject3(0, testIssueIds.get(2)));
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Map<Integer,String> templateIdtoHeaderMap;
		CustomerCareCaseDtoImplForTest caseToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",testIssueIds,relatedBseList,null,new ArrayList<CustomerCareCaseMessage>());
		caseToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		CustomerCareCaseDtoImplForTest caseToSave2 = new CustomerCareCaseDtoImplForTest(0,"SALES_FEEDBACK",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",testIssueIds,relatedBseList,null,new ArrayList<CustomerCareCaseMessage>());
		caseToSave2.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		CustomerCareCaseDtoImplForTest caseToSave3 = new CustomerCareCaseDtoImplForTest(0,"QNA",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",testIssueIds,relatedBseList,null,new ArrayList<CustomerCareCaseMessage>());
		caseToSave3.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId1 = this.uco.save(caseToSave1);
		int caseId2 = this.uco.save(caseToSave2);
		int caseId3 = this.uco.save(caseToSave3);
		MockAuth.logout();
		templateIdtoHeaderMap = this.uco.getApplicableTemplate(caseId1);
		assertTrue(templateIdtoHeaderMap.containsKey(templateId1));
		templateIdtoHeaderMap = this.uco.getApplicableTemplate(caseId2);
		assertTrue(templateIdtoHeaderMap.containsKey(templateId2)&&templateIdtoHeaderMap.containsKey(templateId3));
		templateIdtoHeaderMap = this.uco.getApplicableTemplate(caseId3);
		assertTrue(templateIdtoHeaderMap.containsKey(templateId2)&&templateIdtoHeaderMap.containsKey(templateId3));
	}
	
	private CustomerCareCaseIssueTemplateImpl createTemplateObject1(int id, int issueId){
		List<String> applicableLocales = new ArrayList<String>(Arrays.asList("zh_TW","en_US"));
		List<String> applicableCaseTypes = new ArrayList<String>(Arrays.asList("CUSTOMER_EMAIL"));
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
		String content = "cheese steak jimmy's";
		return new CustomerCareCaseIssueTemplateImpl(id, issueId, applicableLocales, applicableCaseTypes, applicableMarketRegions, applicableMarketplaceList, content);
	}
	
	private CustomerCareCaseIssueTemplateImpl createTemplateObject3(int id,int issueId){
		List<String> applicableLocales = new ArrayList<String>(Arrays.asList("en_US"));
		List<String> applicableCaseTypes = new ArrayList<String>(Arrays.asList("QNA","SALES_FEEDBACK"));
		List<String> applicableMarketRegions = new ArrayList<String>(Arrays.asList("US"));
		List<Marketplace> applicableMarketplaceList = new ArrayList<Marketplace>(Arrays.asList(Marketplace.AMAZON_COM,Marketplace.TRUETOSOURCE));
		String content = "furious the monkey boy";
		return new CustomerCareCaseIssueTemplateImpl(id, issueId, applicableLocales, applicableCaseTypes, applicableMarketRegions, applicableMarketplaceList, content);
	}
	
	@Test @Transactional
	public void testAccessByDrsUser(){
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		this.uco.get(11);
		MockAuth.logout();
	}
	
	@Test(expected=IllegalArgumentException.class) @Transactional
	public void testAccessUnauthorizedCaseBySupplier(){
		this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "junping@hanchor.com", "HNKbY5Qs");
		this.uco.get(11);
		MockAuth.logout();
		
	}
	
	@Test @Transactional
	public void testSaveWithRelatedProductBaseNullIssues(){
		this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseToSave = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",null,relatedBseList,null,new ArrayList<CustomerCareCaseMessage>());
		caseToSave.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.uco.save(caseToSave);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(caseId,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",null,relatedBseList,null, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(caseId));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testSaveWithRelatedProductBaseWithIssue(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseToSave = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,relatedBseList,null,new ArrayList<CustomerCareCaseMessage>());
		caseToSave.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.uco.save(caseToSave);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(caseId,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,relatedBseList,null, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(caseId));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testSaveWithRelatedProductSkuWithIssue(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseToSave = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseToSave.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.uco.save(caseToSave);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(caseId,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(caseId));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testSaveWithRelatedProductSkuNullIssueTypeCategory(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseToSave = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",null,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseToSave.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.uco.save(caseToSave);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(caseId,"CUSTOMER_EMAIL",null,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(caseId));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testGetList(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		CustomerCareCaseDtoImplForTest caseToSave2 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"222-22222-22222","2014-11-17 11:00:00 +0000","K486","Customer2","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		caseToSave2.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test2",false));
		int id1 = this.uco.save(caseToSave1);
		int id2 = this.uco.save(caseToSave2);
		CustomerCareCaseDtoImplForTest expected1 = new CustomerCareCaseDtoImplForTest(id1,"CUSTOMER_EMAIL",null,"K3",Marketplace.AMAZON_COM,null,null,"K486","Customer1","2014-11-17 00:00:00", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		CustomerCareCaseDtoImplForTest expected2 = new CustomerCareCaseDtoImplForTest(id2,"CUSTOMER_EMAIL",null,"K3",Marketplace.AMAZON_COM,null,null,"K486","Customer2","2014-11-17 00:00:00", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		assertEquals(expected1,this.getCustomerCareCaseInDtoList(id1,null));
		assertEquals(expected2,this.getCustomerCareCaseInDtoList(id2,""));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testGetListByCustomerName(){
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		List<CustomerCareCaseDto> resultList = this.uco.getList(1,"ro").getItems();
		for(CustomerCareCaseDto c:resultList){
			assertTrue(c.getCustomerName().toLowerCase().contains("ro"));
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetListBySupplier(){
		MockAuth.login(authenticationManager, "junping@hanchor.com", "HNKbY5Qs");
		this.uco.getList(1,null);
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testDelete(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.delete(id);
		Assert.assertEquals(null,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager") @Ignore
	public void testDeleteCaseWithSettledMsdcMsg(){
		// this test case was random choosed from current database, it's not always a correct one
//		assertEquals(0,this.uco.delete(67));
	}
	
	@Test @Transactional
	public void testGetMessage(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,  "120", "2", "3",null,"K486-BAL",null,"2","Reply1.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,  "120", "2", "3",null,"K486-BAL",null,"2","Reply2.",false));
		MockAuth.logout();
		CustomerCareCaseMessageImplForTest expectedMsg2 = new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3","4.34","K486-BAL","2.17","2","Reply1.",false);
		CustomerCareCaseMessageImplForTest expectedMsg3 = new CustomerCareCaseMessageImplForTest(3,MessageType.CUSTOMER,"Joanna Lee","2014-11-18 00:00 +0000",                       null,                       null,null,      null,null,null,null,null,null,null,"Question Again.",false);
		assertEquals(expectedMsg2,this.uco.getMessage(id, 2));
		assertEquals(expectedMsg3,this.uco.getMessage(id, 3));
	}
	
	@Test @Transactional
	public void testAddCustomerMessage(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test2",false));
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.CUSTOMER,"Joanna Lee","2014-11-18 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test2",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testAddMsdcMessage(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2014-11-11 10:00:00 +0000",true,"120","2","3",null,"K486-BAL",null,"2","This is content for test2",false));
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3","4.34","K486-BAL","2.17","2","This is content for test2",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"This is content for test",false));				
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}

	@Test @Transactional
	public void testUpdateMarketPlaceOrderIdAndDateCreate(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"BUSINESS_ENQUIRES",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2014-11-11 10:00:00 +0000",true,"120","2","3",null,"K486-BAL",null,"2","This is content for test2",false));
		CustomerCareCaseDtoImplForTest toUpdate = new CustomerCareCaseDtoImplForTest(id,"BUSINESS_ENQUIRES",1,"K3",Marketplace.AMAZON_COM,"111-11111-77777","2014-11-17 00:00:00 +0000","K486","Customer1","2014-11-18 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		toUpdate.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"QUESTION 1",false));
		toUpdate.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120", "2", "3",null,"K486-BAL","2","2","This is content for test2",false));
		this.uco.update(toUpdate);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"BUSINESS_ENQUIRES",1,"K3",Marketplace.AMAZON_COM,"111-11111-77777","2014-11-17 00:00:00 +0000","K486","Customer1","2014-11-18 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3","4.34","K486-BAL","2.17","2","This is content for test2",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"QUESTION 1",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}

	@Test @Transactional
	public void testUpdateCustomerMessageNotLastOne(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120",  "2", "3",null,"K486-BAL",null,"2","Reply1.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120",  "2", "3",null,"K486-BAL",null,"2","Reply2.",false));
		CustomerCareCaseMessageImplForTest msgToUpdate = new CustomerCareCaseMessageImplForTest(3,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content updated.",false);
		this.uco.updateMessage(id, msgToUpdate);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3","4.34","K486-BAL","2.17","2","Reply1.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(4,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3","4.34","K486-BAL","2.17","2","Reply2.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(3,MessageType.CUSTOMER,"Joanna Lee","2014-11-18 00:00 +0000",                       null,                       null,null,      null,null,null,null,null,null,null,"This is content updated.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",                       null,                       null,null,      null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testUpdateLastOneCustomerMessage(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test2",false));
		CustomerCareCaseMessageImplForTest msgToUpdate = new CustomerCareCaseMessageImplForTest(2,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content updated.",false);
		this.uco.updateMessage(id, msgToUpdate);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.CUSTOMER,"Joanna Lee","2014-11-18 00:00 +0000",                    null,                    null,null,null,null,null,null,null,null,null,"This is content updated.",false));		
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",                    null,                    null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testUpdateCustomerMessageWhichIsNotLastOne(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120", "2", "3",null,"K486-BAL",null,"2","Reply1.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120", "2", "3",null,"K486-BAL",null,"2","Reply2.",false));
		CustomerCareCaseMessageImplForTest msgToUpdate = new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,null,null,null,"2014-11-11 10:00:00 +0000",true,"120", "2", "3", null,"K486-BAL",null,"2","This is content updated.",false);
		this.uco.updateMessage(id, msgToUpdate);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(4,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000", "2", "3","4.34","K486-BAL","2.17","2","Reply2.",false));	
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000", "2", "3","4.34","K486-BAL","2.17","2","This is content updated.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(3,MessageType.CUSTOMER,"Joanna Lee","2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));			
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"This is content for test",false));		
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testUpdateLastOneMsdcMessage(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id, new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,null,null,"2014-11-11 10:00:00 +0000",true,"120", "2", "3", null,"K486-BAL",null,"2","This is content for test2",false));
		CustomerCareCaseMessageImplForTest msgToUpdate = new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,null,null,null,"2014-11-11 10:00:00 +0000",true,"120", "2", "3", null,"K486-BAL",null,"2","This is content updated.",false);
		this.uco.updateMessage(id, msgToUpdate);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",null,null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3", "4.34","K486-BAL","2.17","2","This is content updated.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testUpdateRelatedIssues(){
		List<Integer> issueIdsAll = this.getIssueIdsForTest();
		List<Integer> issueIdsOrig = new ArrayList<Integer>(Arrays.asList(issueIdsAll.get(0),issueIdsAll.get(1)));
		List<Integer> issueIdsNew = new ArrayList<Integer>(Arrays.asList(issueIdsAll.get(2)));
		relatedBseList = new ArrayList<String>(Arrays.asList("BP-K486-BAL", "BP-K486-HL", "BP-K486-KN"));
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIdsOrig,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		CustomerCareCaseDtoImplForTest toUpdate = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIdsNew,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		toUpdate.addMessage(new CustomerCareCaseMessageImplForTest(1,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		this.uco.update(toUpdate);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIdsNew,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
//	@Test @Transactional
//	public void testUpdateSettledMessage(){
//		// this test case was random choosed from current database, it's not always a correct one
//		this.createIssueForTest();
//		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
//		int id = this.uco.save(caseWithRelatedProductSkus1);
//		CustomerCareCaseMessageImplForTest msgToAdd = new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,null,"2014-11-18 00:00 +0000","2014-11-11 08:00:00 +0000","2014-11-11 10:00:00 +0000",true,null, "1", "2", "3", null,"K486-BAL",null, "This is content for test2");
//		this.uco.addMessage(id, msgToAdd);
//		CustomerCareCaseMessageImplForTest msgToUpdate = new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,null,"2014-11-18 00:00 +0000","2014-11-11 08:00:00 +0000","2014-11-11 10:00:00 +0000",true,null, "3", "2", "3", null,"K486-BAL",null, "This is content updated.");
//		assertEquals(null,this.uco.updateMessage(67, msgToUpdate));
//		MockAuth.logout();
//	}
	
	@Test @Transactional
	public void testAddMessages(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120",  "2", "3",null,"K486-BAL",null,"2","Reply1.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120",  "2", "3",null,"K486-BAL",null,"2","Reply2.",false));
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                         null,					    null,"2014-11-11 10:00:00 +0000",true,"120.000000", "2", "3","4.34","K486-BAL","2.17","2","Reply1.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(4,MessageType.MSDC,    "Joanna Lee",                         null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000", "2", "3","4.34","K486-BAL","2.17","2","Reply2.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(3,MessageType.CUSTOMER,"Joanna Lee",     "2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee",     "2014-11-17 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"This is content for test",false));
		//expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee",     "2014-11-17 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"This is content for test",false));		
		//expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                         null,					    null,"2014-11-11 10:00:00 +0000",true,"120.000000", "2", "3","4.34","K486-BAL","2.17","2","Reply1.",false));
		//expected.addMessage(new CustomerCareCaseMessageImplForTest(3,MessageType.CUSTOMER,"Joanna Lee",     "2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		//expected.addMessage(new CustomerCareCaseMessageImplForTest(4,MessageType.MSDC,    "Joanna Lee",                         null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000", "2", "3","4.34","K486-BAL","2.17","2","Reply2.",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testDeleteMsdcMessages(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120",  "2", "3",null,"K486-BAL",null,"2","Reply1.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120",  "2", "3",null,"K486-BAL",null,"2","Reply2.",false));
		this.uco.deleteMessage(id,2);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(3,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3","4.34","K486-BAL","2.17","2","Reply2.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.CUSTOMER,"Joanna Lee","2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testDeleteCustomerMessages(){
		List<Integer> issueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseWithSkuToSave1 = new CustomerCareCaseDtoImplForTest(0,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList,new ArrayList<CustomerCareCaseMessage>());
		caseWithSkuToSave1.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int id = this.uco.save(caseWithSkuToSave1);
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120","2", "3",null,"K486-BAL",null,"2","Reply1.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.CUSTOMER,null,"2014-11-18 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"Question Again.",false));
		this.uco.addMessage(id,new CustomerCareCaseMessageImplForTest(null,MessageType.MSDC,    null,                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120","2", "3",null,"K486-BAL",null,"2","Reply2.",false));
		this.uco.deleteMessage(id,3);
		CustomerCareCaseDtoImplForTest expected = new CustomerCareCaseDtoImplForTest(id,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",issueIds,null,relatedSkuList, new ArrayList<CustomerCareCaseMessage>());
		expected.addMessage(new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3","4.34","K486-BAL","2.17","2","Reply1.",false));
		expected.addMessage(new CustomerCareCaseMessageImplForTest(3,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120.000000",  "2", "3","4.34","K486-BAL","2.17","2","Reply2.",false));				
		expected.addMessage(new CustomerCareCaseMessageImplForTest(1,MessageType.CUSTOMER,"Joanna Lee","2014-11-17 00:00 +0000",                       null,                       null,null,null,null,null,null,null,null,null,"This is content for test",false));
		assertEquals(expected,this.uco.get(id));
		MockAuth.logout();
	}
	
	@Test @Transactional("transactionManager") @Ignore
	public void testDeleteSettledMsdcMessages(){
		// this test case was random choosed from current database, it's not always a correct one
//		assertEquals(false,this.uco.deleteMessage(67,2));
	}
	
	@Test @Transactional
	public void testGetProductBaseCodeToSupplierNameMap(){
		Map<String,String> baseCodeToSupplierNameMap = this.uco.getProductBaseCodeToSupplierNameMap("K486");
		//assertEquals(3,baseCodeToSupplierNameMap.size());
		assertTrue(baseCodeToSupplierNameMap.containsKey("BP-K486-KN"));
		assertTrue(baseCodeToSupplierNameMap.containsKey("BP-K486-HL"));
		assertTrue(baseCodeToSupplierNameMap.containsKey("BP-K486-BAL"));
	}
	
	@Test @Transactional
	public void testGetProductSkuCodeToSupplierNameMap(){
		Map<String,String> baseCodeToSupplierNameMap = this.uco.getProductSkuCodeToSupplierNameMap("K486");
		//assertEquals(7,baseCodeToSupplierNameMap.size());
		assertTrue(baseCodeToSupplierNameMap.containsKey("K486-KNB"));
		assertTrue(baseCodeToSupplierNameMap.containsKey("K486-KNK"));
		assertTrue(baseCodeToSupplierNameMap.containsKey("K486-KNP"));
		assertTrue(baseCodeToSupplierNameMap.containsKey("K486-HLB"));
		assertTrue(baseCodeToSupplierNameMap.containsKey("K486-HLK"));
		assertTrue(baseCodeToSupplierNameMap.containsKey("K486-HLP"));
		assertTrue(baseCodeToSupplierNameMap.containsKey("K486-BAL"));
	}
	
	@Test @Transactional
	public void testGetProductSkuCodeToSupplierNameMapUnderBase(){
		List<String> baseCodes = Arrays.asList("BP-K486-KN","BP-K486-HL");
		Map<String,String> resultMap = this.uco.getProductSkuCodeToSupplierNameMapUnderBases(baseCodes);
		assertEquals(6,resultMap.size());
		assertTrue(resultMap.containsKey("K486-KNB"));
		assertTrue(resultMap.containsKey("K486-KNK"));
		assertTrue(resultMap.containsKey("K486-KNP"));
		assertTrue(resultMap.containsKey("K486-HLB"));
		assertTrue(resultMap.containsKey("K486-HLK"));
		assertTrue(resultMap.containsKey("K486-HLP"));
	}
		
	@Test
	public void testGetOrderInfoById(){
		Assert.assertEquals(null,this.uco.getOrderDateById("111"));
		CustomerCareCaseOrderInfoImpl expect = new CustomerCareCaseOrderInfoImpl(1,"Amazon.com","2015-09-13 05:49:54 +0000","Adrian Baudy");
		assertEquals(expect,this.uco.getOrderDateById("114-6743529-0687427"));
	}

	@Test
	public void testGetTypeList(){
		List<String> typeList = this.uco.getTypeList();
		//Assert.isTrue(typeList.size()==CustomerCareCaseType.values().length);
		for(CustomerCareCaseType type:CustomerCareCaseType.values()){
			assertTrue(typeList.contains(type.name()));
		}
	}
	
	@Test @Transactional
	public void testGetIssueIdToEnUsNameMap(){
		List<Integer> testIssueIds = this.getIssueIdsForTest();
		Map<Integer,String> map;
		map = this.uco.getIssueIdToEnUsNameMap(null, null);
		assertTrue(map.containsKey(testIssueIds.get(0)));
		assertTrue(map.containsKey(testIssueIds.get(1)));
		assertTrue(map.containsKey(testIssueIds.get(2)));
		map = this.uco.getIssueIdToEnUsNameMap(1,null);
		assertTrue(map.containsKey(testIssueIds.get(0)));
		assertTrue(map.containsKey(testIssueIds.get(1)));
		assertTrue(map.containsKey(testIssueIds.get(2)));
		map = this.uco.getIssueIdToEnUsNameMap(1,1);
		assertTrue(map.containsKey(testIssueIds.get(0)));
		assertTrue(map.containsKey(testIssueIds.get(1)));
		assertTrue(!map.containsKey(testIssueIds.get(2)));
		map = this.uco.getIssueIdToEnUsNameMap(1,2);
		assertTrue(!map.containsKey(testIssueIds.get(0)));
		assertTrue(!map.containsKey(testIssueIds.get(1)));
		assertTrue(map.containsKey(testIssueIds.get(2)));
	}
	
	private List<Integer> getIssueIdsForTest(){
		this.issueUco.createIssueType(1,issueType1);
		this.issueUco.createIssueType(1,issueType2);
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		int issueId1 = this.issueUco.createIssue(new CustomerCareCaseIssueImpl(0, 1, 1, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", relatedBseList, null, null, null, null, null));
		int issueId2 = this.issueUco.createIssue(new CustomerCareCaseIssueImpl(0, 1, 1, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", relatedBseList, null, null, null, null, null));
		int issueId3 = this.issueUco.createIssue(new CustomerCareCaseIssueImpl(0, 1, 2, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", relatedBseList, null, null, null, null, null));
		return new ArrayList<Integer>(Arrays.asList(issueId1,issueId2,issueId3));
	}
	
	private CustomerCareCaseDto getCustomerCareCaseInDtoList(Integer caseId, String customerName){
		int pageIndex = 1;
		int totalPages = 0;
		do {
			DtoList<CustomerCareCaseDto> dtoList = this.uco.getList(pageIndex,customerName);
			totalPages = dtoList.getPager().getTotalPages();
			for(CustomerCareCaseDto cc:dtoList.getItems()){
				if(cc.getCaseId().equals(caseId)) return cc;
			}
			pageIndex+=1;
		} while(pageIndex<totalPages);
		return null;
	}
	
	@Test @Transactional
	public void testTranslationFee() {
		List<Integer> testIssueIds = this.getIssueIdsForTest();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		CustomerCareCaseDtoImplForTest caseToSave = new CustomerCareCaseDtoImplForTest(null,"CUSTOMER_EMAIL",1,"K3",Marketplace.AMAZON_COM,"111-11111-11111","2014-11-17 11:00:00 +0000","K486","Customer1","2014-11-17 00:00 +0000", "Processing",testIssueIds,relatedBseList,null,new ArrayList<CustomerCareCaseMessage>());
		caseToSave.addMessage(new CustomerCareCaseMessageImplForTest(null,null,null,"2014-11-17 00:00:00 +0000",null,null,null,null,null,null,null,null,null,null,"This is content for test",false));
		int caseId = this.uco.save(caseToSave);
		this.uco.addMessage(caseId, new CustomerCareCaseMessageImplForTest(2,MessageType.MSDC,    "Joanna Lee",                    null,                       null,"2014-11-11 10:00:00 +0000",true,"120","2", null ,null,"K486-BAL",null,"2","One Two",true));
		CustomerCareCaseMessage caseMessage = uco.getMessage(caseId, 2);
		MockAuth.logout();
		Assert.assertEquals(true,caseMessage.getIncludesTranslationFee());
		Assert.assertEquals("0.28",caseMessage.getDRSChargeByWord());
	}
	
	@Test
	public void testGetListElasticEmptySearch() {
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		DtoList<CustomerCareCaseDto> list = uco.getListElastic(1, "");
		assertTrue(list.getTotalRecords() > 2000);
		MockAuth.logout();
	}
	
	@Test
	public void testGetListElasticSearchNoMatch() {
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		DtoList<CustomerCareCaseDto> list = uco.getListElastic(1, "1v22fd#$%2ab");
		Assert.assertEquals(0, list.getTotalRecords());
		Assert.assertEquals(0, list.getItems().size());
		MockAuth.logout();
	}
	
	@Test
	public void testGetListElasticSearchById() {
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		DtoList<CustomerCareCaseDto> list = uco.getListElastic(1, "100");
		int size = list.getItems().size();
		int caseId = list.getItems().get(size-1).getCaseId();
		String customerName = list.getItems().get(size-1).getCustomerName();
		System.out.println("total records: " + list.getTotalRecords());
		System.out.println("case id: " + caseId);
		System.out.println("customer name: " + customerName);
		assertTrue(list.getTotalRecords() > 8);
		assertEquals(100, caseId);
		assertEquals("Mike  Gervais", customerName);
		MockAuth.logout();
	}
	
	@Test
	public void testGetListElasticSearchByPrefix() {
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		DtoList<CustomerCareCaseDto> list = uco.getListElastic(1, "anti");
		int size = list.getItems().size();
		int caseId = list.getItems().get(size-1).getCaseId();
		String customerName = list.getItems().get(size-1).getCustomerName();
		System.out.println("total records: " + list.getTotalRecords());
		System.out.println("case id: " + caseId);
		System.out.println("customer name: " + customerName);
		assertEquals(1312, caseId);
		assertEquals("juuso antinoja", customerName);
		MockAuth.logout();
	}
	
	@Test
	public void testGetListElasticSearchByPhrase() {
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		DtoList<CustomerCareCaseDto> list = uco.getListElastic(1, "tips for how/where to we");
		for (CustomerCareCaseDto item : list.getItems()) {
			System.out.println(item.getRelatedIssueIds().get(0));
			assertEquals(457, (int) item.getRelatedIssueIds().get(0));
		}
		MockAuth.logout();
	}

}
