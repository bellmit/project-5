package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueTemplateUco;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueUco;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueStatus;
import com.kindminds.drs.impl.CustomerCareCaseIssueImpl;
import com.kindminds.drs.impl.CustomerCareCaseIssueTemplateImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainCustomerCareCaseIssueTemplateUco {
	
	@Autowired private MaintainCustomerCareCaseIssueTemplateUco uco;
	@Autowired private MaintainCustomerCareCaseIssueUco issueUco;

	private static List<String> relatedBseList;
	private static List<String> relatedSkuList;
	
	@BeforeClass
	public static void prepareTestData(){
		relatedBseList = new ArrayList<String>(Arrays.asList("BP-K486-BAL", "BP-K486-HL", "BP-K486-KN"));
		relatedSkuList = new ArrayList<String>(Arrays.asList("K486-BAL", "K486-HLB", "K486-HLK", "K486-HLP", "K486-KNB", "K486-KNK", "K486-KNP"));
	}
	
	@Test
	public void testGetMarketplaceIdToNameMap(){
		List<Marketplace> marketplaceList = this.uco.getMarketplaceList();
		assertTrue(marketplaceList.contains(Marketplace.AMAZON_COM));
		assertTrue(marketplaceList.contains(Marketplace.TRUETOSOURCE));
		assertTrue(marketplaceList.contains(Marketplace.EBAY_COM));
	}
	
	@Test @Transactional
	public void testCreate(){
		int [] issueIds = this.createIssuesForTestAndReturnIds();
		List<String> applicableLocales = new ArrayList<String>(Arrays.asList("zh_TW","en_US"));
		List<String> applicableCaseTypes = new ArrayList<String>(Arrays.asList("CUSTOMER_REVIEW","CUSTOMER_EMAIL"));
		List<String> applicableMarketRegions = new ArrayList<String>(Arrays.asList("US","UK"));
		List<Marketplace> applicableMarketplaceIdList = new ArrayList<Marketplace>(Arrays.asList(Marketplace.AMAZON_COM,Marketplace.TRUETOSOURCE));
		String content = "How do you turn this on?";
		CustomerCareCaseIssueTemplateImpl toCreate = new CustomerCareCaseIssueTemplateImpl( 0,issueIds[0], applicableLocales, applicableCaseTypes, applicableMarketRegions, applicableMarketplaceIdList, content);
		int id = this.uco.create(toCreate);
		CustomerCareCaseIssueTemplateImpl expected = new CustomerCareCaseIssueTemplateImpl(id,issueIds[0], applicableLocales, applicableCaseTypes, applicableMarketRegions, applicableMarketplaceIdList, content);
		assertEquals(expected,this.uco.get(id));
		Assert.assertEquals(CustomerCareCaseIssueStatus.RESPONSE_TEMPLATE_DONE.name(),this.issueUco.getIssue(issueIds[0]).getStatus());
	}
	
	@Test @Transactional
	public void testUpdate(){
		int [] issueIds = this.createIssuesForTestAndReturnIds();
		List<String> applicableLocales = new ArrayList<String>(Arrays.asList("zh_TW","en_US"));
		List<String> applicableCaseTypes = new ArrayList<String>(Arrays.asList("CUSTOMER_REVIEW","CUSTOMER_EMAIL"));
		List<String> applicableMarketRegions = new ArrayList<String>(Arrays.asList("US","UK"));
		List<Marketplace> applicableMarketplaceList = new ArrayList<Marketplace>(Arrays.asList(Marketplace.AMAZON_COM,Marketplace.TRUETOSOURCE));
		String content = "How do you turn this on?";
		CustomerCareCaseIssueTemplateImpl toCreate = new CustomerCareCaseIssueTemplateImpl(0,issueIds[0], applicableLocales, applicableCaseTypes, applicableMarketRegions, applicableMarketplaceList, content);
		int id = this.uco.create(toCreate);
		List<String> applicableLocalesNew = new ArrayList<String>(Arrays.asList("zh_TW"));
		List<String> applicableCaseTypesNew = new ArrayList<String>(Arrays.asList("CUSTOMER_REVIEW"));
		List<String> applicableMarketRegionsNew = new ArrayList<String>(Arrays.asList("US","UK"));
		List<Marketplace> applicableMarketplaceListNew = new ArrayList<Marketplace>(Arrays.asList(Marketplace.TRUETOSOURCE));
		String contentNew = "How do you turn this off?";
		CustomerCareCaseIssueTemplateImpl toUpdate = new CustomerCareCaseIssueTemplateImpl(id,issueIds[0], applicableLocalesNew, applicableCaseTypesNew, applicableMarketRegionsNew, applicableMarketplaceListNew, contentNew);
		assertEquals(toUpdate,this.uco.update(toUpdate));
	}
	
	@Test @Transactional
	public void testDelete(){
		int [] issueIds = this.createIssuesForTestAndReturnIds();
		List<String> applicableLocales = new ArrayList<String>(Arrays.asList("zh_TW","en_US"));
		List<String> applicableCaseTypes = new ArrayList<String>(Arrays.asList("CUSTOMER_REVIEW","CUSTOMER_EMAIL"));
		List<String> applicableMarketRegions = new ArrayList<String>(Arrays.asList("US","UK"));
		List<Marketplace> applicableMarketplaceList = new ArrayList<Marketplace>(Arrays.asList(Marketplace.AMAZON_COM,Marketplace.TRUETOSOURCE));
		String content = "How do you turn this on?";
		CustomerCareCaseIssueTemplateImpl toCreate = new CustomerCareCaseIssueTemplateImpl(0,issueIds[0], applicableLocales, applicableCaseTypes, applicableMarketRegions, applicableMarketplaceList, content);
		int id = this.uco.create(toCreate);
		this.uco.delete(id);
		Assert.assertEquals(null,this.uco.get(id));
	}
	
	private int [] createIssuesForTestAndReturnIds(){
		Map<String,String> langToNameMap = new HashMap<String,String>();
		langToNameMap.put("zh_TW", "NAME_ZH_TW");
		langToNameMap.put("en_US", "NAME_EN_US");
		CustomerCareCaseIssueImpl toCreate1 = new CustomerCareCaseIssueImpl(0, 1, 1, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", relatedBseList, null, null, null, null, null);
		CustomerCareCaseIssueImpl toCreate2 = new CustomerCareCaseIssueImpl(0, 1, 1, langToNameMap, "PENDING_SUPPLIER_ACTION", "K486", null, relatedSkuList, null, null, null, null);
		int id1 = this.issueUco.createIssue(toCreate1);
		int id2 = this.issueUco.createIssue(toCreate2);
		int [] ids = {id1,id2};
		return ids;
	}
	
}
