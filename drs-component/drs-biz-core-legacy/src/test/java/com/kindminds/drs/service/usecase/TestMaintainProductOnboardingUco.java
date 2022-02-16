package com.kindminds.drs.service.usecase;

import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kindminds.drs.api.usecase.product.MaintainProductOnboardingUco;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainProductOnboardingUco {

	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private MaintainProductOnboardingUco uco;
	
	/*
	@Test @Transactional
	public void testSaveBaseProductOnboarding() throws IOException{		
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");		
		String jsonData = JsonHelper.toJson(map);		
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		BaseProductOnboardingImplForTest expected = new BaseProductOnboardingImplForTest(
				"bp-test",
				"K488",
				null,
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",productInfoSource,"Pending supplier action"),
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonData,"Pending supplier action"),
				new ArrayList<BaseProductOnboardingDetail>(),
				new ArrayList<BaseProductOnboardingDetail>());
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonData,"Pending supplier action"));
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonData,"Pending supplier action"));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonData,null));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonData,null));				
		assertEquals(expected,this.uco.getDetailOfBaseProductOnboarding("bp-test"));		
		MockAuth.logout();		
	}
	
	@Test @Transactional
	public void testSaveDraftBaseProductOnboarding() throws IOException{		
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		
		Map<String,String> mapToUpdated = new HashMap<String,String>();
		mapToUpdated.put("nickname","building");		
		String jsonDataToUpdated = JsonHelper.toJson(mapToUpdated);
		Map<String,String> USDataMapToUpdated = this.generateUSDataMapToUpdated();
		Map<String,String> UKDataMapToUpdated = this.generateUKDataMapToUpdated();				
		Map<String,String> USDataMapToSaveDraft = this.generateUSDataMapToSaveDraft();
		Map<String,String> UKDataMapToSaveDraft = this.generateUKDataMapToSaveDraft();
		String productInfoSourceToUpdated = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productInfoMarketSideListToUpdated.add(USDataMapToSaveDraft);
		productInfoMarketSideListToUpdated.add(UKDataMapToSaveDraft);
		String productInfoMarketSideToUpdated = JsonHelper.toJson(productInfoMarketSideListToUpdated);	
		String productMarketingMaterialSourceToSaveDraft = this.generateProductMarketingMaterialSourceToSaveDraft();
		List<Map<String,String>> productMarketingMaterialMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideListToUpdated.add(USDataMapToUpdated);
		productMarketingMaterialMarketSideListToUpdated.add(UKDataMapToUpdated);
		String productMarketingMaterialMarketSideToUpdated = JsonHelper.toJson(productMarketingMaterialMarketSideListToUpdated);				
		this.uco.saveDraftForProductInfoSource(productInfoSourceToUpdated, productInfoMarketSideToUpdated, productMarketingMaterialSourceToSaveDraft, productMarketingMaterialMarketSideToUpdated);		
		BaseProductOnboardingImplForTest expected = new BaseProductOnboardingImplForTest(
				"bp-test",
				"K488",
				null,
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",productInfoSourceToUpdated,"Pending supplier action"),
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"),
				new ArrayList<BaseProductOnboardingDetail>(),
				new ArrayList<BaseProductOnboardingDetail>());
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));				
		assertEquals(expected,this.uco.getDetailOfBaseProductOnboarding("bp-test"));				
		MockAuth.logout();		
	}
	
	@Test @Transactional
	public void testSubmitProductInfoSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");		
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		
		Map<String,String> mapToUpdated = new HashMap<String,String>();
		mapToUpdated.put("nickname","building");		
		String jsonDataToUpdated = JsonHelper.toJson(mapToUpdated);
		Map<String,String> USDataMapToUpdated = this.generateUSDataMapToUpdated();
		Map<String,String> UKDataMapToUpdated = this.generateUKDataMapToUpdated();				
		Map<String,String> USDataMapToSaveDraft = this.generateUSDataMapToSaveDraft();
		Map<String,String> UKDataMapToSaveDraft = this.generateUKDataMapToSaveDraft();
		String productInfoSourceToUpdated = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productInfoMarketSideListToUpdated.add(USDataMapToSaveDraft);
		productInfoMarketSideListToUpdated.add(UKDataMapToSaveDraft);
		String productInfoMarketSideToUpdated = JsonHelper.toJson(productInfoMarketSideListToUpdated);	
		String productMarketingMaterialSourceToSaveDraft = this.generateProductMarketingMaterialSourceToSaveDraft();
		List<Map<String,String>> productMarketingMaterialMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideListToUpdated.add(USDataMapToUpdated);
		productMarketingMaterialMarketSideListToUpdated.add(UKDataMapToUpdated);
		String productMarketingMaterialMarketSideToUpdated = JsonHelper.toJson(productMarketingMaterialMarketSideListToUpdated);				
		this.uco.submitProductInfoSource(productInfoSourceToUpdated, productInfoMarketSideToUpdated, productMarketingMaterialSourceToSaveDraft, productMarketingMaterialMarketSideToUpdated);		
		BaseProductOnboardingImplForTest expected = new BaseProductOnboardingImplForTest(
				"bp-test",
				"K488",
				null,
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",productInfoSourceToUpdated,"Pending DRS review"),
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"),
				new ArrayList<BaseProductOnboardingDetail>(),
				new ArrayList<BaseProductOnboardingDetail>());
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));				
		assertEquals(expected,this.uco.getDetailOfBaseProductOnboarding("bp-test"));				
		MockAuth.logout();				
	}
	
	@Test @Transactional
	public void testApproveProductInfoSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		
		Map<String,String> mapToUpdated = new HashMap<String,String>();
		mapToUpdated.put("nickname","building");		
		String jsonDataToUpdated = JsonHelper.toJson(mapToUpdated);
		Map<String,String> USDataMapToUpdated = this.generateUSDataMapToUpdated();
		Map<String,String> UKDataMapToUpdated = this.generateUKDataMapToUpdated();				
		Map<String,String> USDataMapToSaveDraft = this.generateUSDataMapToSaveDraft();
		Map<String,String> UKDataMapToSaveDraft = this.generateUKDataMapToSaveDraft();
		String productInfoSourceToUpdated = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productInfoMarketSideListToUpdated.add(USDataMapToSaveDraft);
		productInfoMarketSideListToUpdated.add(UKDataMapToSaveDraft);
		String productInfoMarketSideToUpdated = JsonHelper.toJson(productInfoMarketSideListToUpdated);	
		String productMarketingMaterialSourceToSaveDraft = this.generateProductMarketingMaterialSourceToSaveDraft();
		List<Map<String,String>> productMarketingMaterialMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideListToUpdated.add(USDataMapToUpdated);
		productMarketingMaterialMarketSideListToUpdated.add(UKDataMapToUpdated);
		String productMarketingMaterialMarketSideToUpdated = JsonHelper.toJson(productMarketingMaterialMarketSideListToUpdated);				
		this.uco.submitProductInfoSource(productInfoSourceToUpdated, productInfoMarketSideToUpdated, productMarketingMaterialSourceToSaveDraft, productMarketingMaterialMarketSideToUpdated);		
		MockAuth.logout();	
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		this.uco.approveProductInfoSource(productInfoSourceToUpdated, productInfoMarketSideToUpdated, productMarketingMaterialSourceToSaveDraft, productMarketingMaterialMarketSideToUpdated);		
		BaseProductOnboardingImplForTest expected = new BaseProductOnboardingImplForTest(
				"bp-test",
				"K488",
				null,
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",productInfoSourceToUpdated,"Finalized"),
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"),
				new ArrayList<BaseProductOnboardingDetail>(),
				new ArrayList<BaseProductOnboardingDetail>());
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));				
		assertEquals(expected,this.uco.getDetailOfBaseProductOnboarding("bp-test"));				
		MockAuth.logout();	
	}
	
	@Test @Transactional
	public void testReturnProductInfoSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		
		Map<String,String> mapToUpdated = new HashMap<String,String>();
		mapToUpdated.put("nickname","building");		
		String jsonDataToUpdated = JsonHelper.toJson(mapToUpdated);
		Map<String,String> USDataMapToUpdated = this.generateUSDataMapToUpdated();
		Map<String,String> UKDataMapToUpdated = this.generateUKDataMapToUpdated();				
		Map<String,String> USDataMapToSaveDraft = this.generateUSDataMapToSaveDraft();
		Map<String,String> UKDataMapToSaveDraft = this.generateUKDataMapToSaveDraft();
		String productInfoSourceToUpdated = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productInfoMarketSideListToUpdated.add(USDataMapToSaveDraft);
		productInfoMarketSideListToUpdated.add(UKDataMapToSaveDraft);
		String productInfoMarketSideToUpdated = JsonHelper.toJson(productInfoMarketSideListToUpdated);	
		String productMarketingMaterialSourceToSaveDraft = this.generateProductMarketingMaterialSourceToSaveDraft();
		List<Map<String,String>> productMarketingMaterialMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideListToUpdated.add(USDataMapToUpdated);
		productMarketingMaterialMarketSideListToUpdated.add(UKDataMapToUpdated);
		String productMarketingMaterialMarketSideToUpdated = JsonHelper.toJson(productMarketingMaterialMarketSideListToUpdated);				
		this.uco.submitProductInfoSource(productInfoSourceToUpdated, productInfoMarketSideToUpdated, productMarketingMaterialSourceToSaveDraft, productMarketingMaterialMarketSideToUpdated);		
		MockAuth.logout();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		this.uco.returnProductInfoSource(productInfoSourceToUpdated, productInfoMarketSideToUpdated, productMarketingMaterialSourceToSaveDraft, productMarketingMaterialMarketSideToUpdated);		
		BaseProductOnboardingImplForTest expected = new BaseProductOnboardingImplForTest(
				"bp-test",
				"K488",
				null,
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",productInfoSourceToUpdated,"Pending supplier action"),
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"),
				new ArrayList<BaseProductOnboardingDetail>(),
				new ArrayList<BaseProductOnboardingDetail>());
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));				
		assertEquals(expected,this.uco.getDetailOfBaseProductOnboarding("bp-test"));				
		MockAuth.logout();		
	}
		
	@Test @Transactional
	public void testUpdateProductInfoSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		
		Map<String,String> mapToUpdated = new HashMap<String,String>();
		mapToUpdated.put("nickname","building");		
		String jsonDataToUpdated = JsonHelper.toJson(mapToUpdated);
		Map<String,String> USDataMapToUpdated = this.generateUSDataMapToUpdated();
		Map<String,String> UKDataMapToUpdated = this.generateUKDataMapToUpdated();				
		Map<String,String> USDataMapToSaveDraft = this.generateUSDataMapToSaveDraft();
		Map<String,String> UKDataMapToSaveDraft = this.generateUKDataMapToSaveDraft();
		String productInfoSourceToUpdated = this.generateProductInfoSourceToUpdate();
		List<Map<String,String>> productInfoMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productInfoMarketSideListToUpdated.add(USDataMapToSaveDraft);
		productInfoMarketSideListToUpdated.add(UKDataMapToSaveDraft);
		String productInfoMarketSideToUpdated = JsonHelper.toJson(productInfoMarketSideListToUpdated);	
		String productMarketingMaterialSourceToSaveDraft = this.generateProductMarketingMaterialSourceToSaveDraft();
		List<Map<String,String>> productMarketingMaterialMarketSideListToUpdated = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideListToUpdated.add(USDataMapToUpdated);
		productMarketingMaterialMarketSideListToUpdated.add(UKDataMapToUpdated);
		String productMarketingMaterialMarketSideToUpdated = JsonHelper.toJson(productMarketingMaterialMarketSideListToUpdated);				
		this.uco.submitProductInfoSource(productInfoSourceToUpdated, productInfoMarketSideToUpdated, productMarketingMaterialSourceToSaveDraft, productMarketingMaterialMarketSideToUpdated);		
		MockAuth.logout();	
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");				
		this.uco.updateProductInfoSource(productInfoSourceToUpdated, productInfoMarketSideToUpdated, productMarketingMaterialSourceToSaveDraft, productMarketingMaterialMarketSideToUpdated);		
		BaseProductOnboardingImplForTest expected = new BaseProductOnboardingImplForTest(
				"bp-test",
				"K488",
				null,
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",productInfoSourceToUpdated,"Pending DRS review"),
				new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"),
				new ArrayList<BaseProductOnboardingDetail>(),
				new ArrayList<BaseProductOnboardingDetail>());
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductInfoMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,"Pending supplier action"));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));
		expected.addProductMarketingMaterialMarketSide(new BaseProductOnboardingDetailImplForTest("bp-test","K488",jsonDataToUpdated,null));				
		assertEquals(expected,this.uco.getDetailOfBaseProductOnboarding("bp-test"));				
		MockAuth.logout();	
	}
		
	@Test @Transactional
	public void testSaveDraftForProductInfoMarketSide() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);		
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.saveDraftForProductInfoMarketSide(null, "bp-test", "US", jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Pending supplier action"				
		);
		assertEquals(expected,this.uco.getProductInfoMarketSide(null,"bp-test","US"));		
		MockAuth.logout();		
	}
	
	@Test @Transactional
	public void testSubmitProductInfoMarketSide() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.submitProductInfoMarketSide("K488", "bp-test", "US", jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Pending DRS review"				
		);
		assertEquals(expected,this.uco.getProductInfoMarketSide("K488","bp-test","US"));						
		MockAuth.logout();		
	}
	
	@Test @Transactional
	public void testApproveProductInfoMarketSide() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);				
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.submitProductInfoMarketSide(null, "bp-test", "US", jsonDataToUpdate);
		MockAuth.logout();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");		
		this.uco.approveProductInfoMarketSide("K488", "bp-test", "US", jsonDataToUpdate);		
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Finalized"				
		);
		assertEquals(expected,this.uco.getProductInfoMarketSide("K488","bp-test","US"));						
		MockAuth.logout();	
	}
	
	@Test @Transactional
	public void testReturnProductInfoMarketSide() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);				
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.submitProductInfoMarketSide(null, "bp-test", "US", jsonDataToUpdate);
		MockAuth.logout();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");		
		this.uco.returnProductInfoMarketSide("K488", "bp-test", "US", jsonDataToUpdate);		
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Pending supplier action"				
		);
		assertEquals(expected,this.uco.getProductInfoMarketSide("K488","bp-test","US"));						
		MockAuth.logout();				
	}
		
	@Test @Transactional
	public void testUpdateProductInfoMarketSide() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);		
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.submitProductInfoMarketSide(null, "bp-test", "US", jsonDataToUpdate);
		MockAuth.logout();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		this.uco.updateProductInfoMarketSide("K488", "bp-test", "US", jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Pending DRS review"				
		);
		assertEquals(expected,this.uco.getProductInfoMarketSide("K488","bp-test","US"));		
		MockAuth.logout();
	}
		
	@Test @Transactional
	public void testSaveDraftForProductMarketingMaterialSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.saveDraftForProductMarketingMaterialSource(null, "bp-test",jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Pending supplier action"				
		);
		assertEquals(expected,this.uco.getProductMarketingMaterialSource(null,"bp-test"));		
		MockAuth.logout();		
	}
	
	@Test @Transactional
	public void testSubmitProductMarketingMaterialSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.submitProductMarketingMaterialSource(null, "bp-test",jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Pending DRS review"				
		);
		assertEquals(expected,this.uco.getProductMarketingMaterialSource(null,"bp-test"));				
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testApproveProductMarketingMaterialSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.submitProductMarketingMaterialSource(null, "bp-test",jsonDataToUpdate);	
		MockAuth.logout();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");	
		this.uco.approveProductMarketingMaterialSource("K488", "bp-test", jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Finalized"				
		);		
		assertEquals(expected,this.uco.getProductMarketingMaterialSource("K488","bp-test"));	
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testReturnProductMarketingMaterialSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.submitProductMarketingMaterialSource(null, "bp-test",jsonDataToUpdate);	
		MockAuth.logout();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");	
		this.uco.returnProductMarketingMaterialSource("K488", "bp-test", jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Pending supplier action"				
		);		
		assertEquals(expected,this.uco.getProductMarketingMaterialSource("K488","bp-test"));	
		MockAuth.logout();		
	}
		
	@Test @Transactional
	public void testUpdateProductMarketingMaterialSource() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.submitProductMarketingMaterialSource(null, "bp-test",jsonDataToUpdate);	
		MockAuth.logout();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		this.uco.updateProductMarketingMaterialSource("K488", "bp-test", jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				"Pending DRS review"				
		);
		assertEquals(expected,this.uco.getProductMarketingMaterialSource("K488","bp-test"));					
		MockAuth.logout();
	}
		
	@Test @Transactional
	public void testUpdateProductMarketingMaterialMarketSide() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);
		MockAuth.logout();
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");	
		String jsonDataToUpdate = "{\"nickname\": \"building\" }";
		this.uco.updateProductMarketingMaterialMarketSide("K488","bp-test","US",jsonDataToUpdate);
		BaseProductOnboardingDetailImplForTest expected = new BaseProductOnboardingDetailImplForTest(
				"bp-test",
				"K488",
				jsonDataToUpdate,
				null				
		);
		assertEquals(expected,this.uco.getProductMarketingMaterialMarketSide("K488","bp-test","US"));
		MockAuth.logout();		
	}
	
	@Test @Transactional
	public void testRetrieveBaseProductOnboardingList() throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","house");				
		Map<String,String> USDataMap = this.generateUSDataMap();
		Map<String,String> UKDataMap = this.generateUKDataMap();						
		String productInfoSource = this.generateProductInfoSource();
		List<Map<String,String>> productInfoMarketSideList = new ArrayList<Map<String,String>>();
		productInfoMarketSideList.add(USDataMap);
		productInfoMarketSideList.add(UKDataMap);
		String productInfoMarketSide = JsonHelper.toJson(productInfoMarketSideList);				
		String productMarketingMaterialSource = this.generateProductMarketingMaterialSource();		
		List<Map<String,String>> productMarketingMaterialMarketSideList = new ArrayList<Map<String,String>>();
		productMarketingMaterialMarketSideList.add(USDataMap);
		productMarketingMaterialMarketSideList.add(UKDataMap);
		String productMarketingMaterialMarketSide = JsonHelper.toJson(productMarketingMaterialMarketSideList);				
		MockAuth.login(authenticationManager, "linlin@nextdrive.io", "6hEkUqut");
		this.uco.saveDevelopingBaseProduct(productInfoSource, productInfoMarketSide, productMarketingMaterialSource, productMarketingMaterialMarketSide);		
		BaseProductOnboardingImplForTest expected = new BaseProductOnboardingImplForTest(
				"bp-test",
				null,
				productInfoSource,
				null,
				null,
				null,
				null);
		assertEquals(expected,this.getBaseProductOnboardingInDtoList("K488","bp-test"));	
		MockAuth.logout();
	}
	
	@Test @Transactional @Ignore
	public void testUpdateBaseProductOnboardingWithSKU() throws IOException{		
		MockAuth.login(authenticationManager, "hanchor.kmi@tw.drs.network", "9nKRNBC4");		
		BaseProductOnboardingWithSKUImplForTest bpWithSKU = new BaseProductOnboardingWithSKUImplForTest();
		bpWithSKU.setProductBaseCode("BP-K486-HL");
		bpWithSKU.setSupplierKcode("K486");
		bpWithSKU.setStatus("Finalized");		
		List<BaseProductOnboardingSKUItem> SKULineItems = new ArrayList<BaseProductOnboardingSKUItem>();
		SKULineItems.add(new BaseProductOnboardingSKUItemImplForTest(0,"HLB","HLB-test","","","","","","",null));
		SKULineItems.add(new BaseProductOnboardingSKUItemImplForTest(1,"HLK","HLK-test","","","","","","",null));
		SKULineItems.add(new BaseProductOnboardingSKUItemImplForTest(2,"HLP","HLP-test","","","","","","",null));
		bpWithSKU.setSKULineItems(SKULineItems);		
		this.uco.updateBaseProductOnboardingWithSKU(bpWithSKU);		
		MockAuth.logout();		
	}
		
	private BaseProductOnboarding getBaseProductOnboardingInDtoList(String kcode,String baseProductCode){
		DtoList<BaseProductOnboarding> dtoList = this.uco.retrieveBaseProductOnboardingList(kcode,1);
		List<BaseProductOnboarding> list = dtoList.getItems();
		for(BaseProductOnboarding dp:list){
			if(dp.getProductBaseCode().equals(baseProductCode)){
				return dp;
			}
		}
		for(int i=2;i<=dtoList.getPager().getTotalPages();i++){
			dtoList = uco.retrieveBaseProductOnboardingList(kcode,i);
			list = dtoList.getItems();
			for(BaseProductOnboarding dp:list){
				if(dp.getProductBaseCode().equals(baseProductCode)){
					return dp;
				}
			}
		}		
		return null;				
	}
											
	private String generateProductInfoSource(){		
		Map<String,String> productInfoSourceMap = new HashMap<String,String>();
		productInfoSourceMap.put("supplierKcode", "K488");
		productInfoSourceMap.put("baseProductCode", "bp-test");
		return JsonHelper.toJson(productInfoSourceMap);		
	}
	
	private String generateProductInfoSourceToUpdate(){
		Map<String,String> productInfoSourceMap = new HashMap<String,String>();
		productInfoSourceMap.put("supplierKcode", "K488");
		productInfoSourceMap.put("baseProductCode", "bp-test");
		productInfoSourceMap.put("status", "Pending DRS review");
		return JsonHelper.toJson(productInfoSourceMap);		
	}
		
	private String generateProductMarketingMaterialSource(){
		Map<String,String> productMarketingMaterialSourceMap = new HashMap<String,String>();
		productMarketingMaterialSourceMap.put("supplierKcode", "K488");
		productMarketingMaterialSourceMap.put("productBaseCode", "bp-test");
		productMarketingMaterialSourceMap.put("jsonData", "{\"name\":\"house\"}");		
		return JsonHelper.toJson(productMarketingMaterialSourceMap);		
	}
		
	private String generateProductMarketingMaterialSourceToSaveDraft(){
		Map<String,String> productMarketingMaterialSourceMapToSaveDraft = new HashMap<String,String>();
		productMarketingMaterialSourceMapToSaveDraft.put("supplierKcode", "K488");
		productMarketingMaterialSourceMapToSaveDraft.put("productBaseCode", "bp-test");
		productMarketingMaterialSourceMapToSaveDraft.put("jsonData", "{\"nickname\":\"building\"}");		
		productMarketingMaterialSourceMapToSaveDraft.put("status", "Pending supplier action");		
		return JsonHelper.toJson(productMarketingMaterialSourceMapToSaveDraft);		
	}
	
	private Map<String,String> generateUSDataMap(){
		Map<String,String> USDataMap = new HashMap<String,String>();
		USDataMap.put("supplierKcode", "K488");
		USDataMap.put("productBaseCode", "bp-test");
		USDataMap.put("country", "US");
		USDataMap.put("jsonData", "{\"name\":\"house\"}");
		return USDataMap;		
	}
	
	private Map<String,String> generateUSDataMapToUpdated(){
		Map<String,String> USDataMapToUpdated = new HashMap<String,String>();
		USDataMapToUpdated.put("supplierKcode", "K488");
		USDataMapToUpdated.put("productBaseCode", "bp-test");
		USDataMapToUpdated.put("country", "US");
		USDataMapToUpdated.put("jsonData", "{\"nickname\":\"building\"}");
		return USDataMapToUpdated;		
	}
	
	private Map<String,String> generateUSDataMapToSaveDraft(){
		Map<String,String> USDataMapToSaveDraft = new HashMap<String,String>();
		USDataMapToSaveDraft.put("supplierKcode", "K488");
		USDataMapToSaveDraft.put("productBaseCode", "bp-test");
		USDataMapToSaveDraft.put("country", "US");
		USDataMapToSaveDraft.put("jsonData", "{\"nickname\":\"building\"}");
		USDataMapToSaveDraft.put("status", "Pending supplier action");
		return  USDataMapToSaveDraft;
	}
	
	private Map<String,String> generateUKDataMap(){
		Map<String,String> UKDataMap = new HashMap<String,String>();
		UKDataMap.put("supplierKcode", "K488");
		UKDataMap.put("productBaseCode", "bp-test");
		UKDataMap.put("country", "UK");
		UKDataMap.put("jsonData", "{\"name\":\"house\"}");
		return UKDataMap;		
	}
	
	private Map<String,String> generateUKDataMapToUpdated(){
		Map<String,String> UKDataMapToUpdated = new HashMap<String,String>();
		UKDataMapToUpdated.put("supplierKcode", "K488");
		UKDataMapToUpdated.put("productBaseCode", "bp-test");
		UKDataMapToUpdated.put("country", "US");
		UKDataMapToUpdated.put("jsonData", "{\"nickname\":\"building\"}");
		return UKDataMapToUpdated;		
	}
	
	private Map<String,String> generateUKDataMapToSaveDraft(){
		Map<String,String> UKDataMapToSaveDraft = new HashMap<String,String>();
		UKDataMapToSaveDraft.put("supplierKcode", "K488");
		UKDataMapToSaveDraft.put("productBaseCode", "bp-test");
		UKDataMapToSaveDraft.put("country", "US");
		UKDataMapToSaveDraft.put("jsonData", "{\"nickname\":\"building\"}");
		UKDataMapToSaveDraft.put("status", "Pending supplier action");
		return  UKDataMapToSaveDraft;
	}
	
	@Test
	public void TestCalculateDangerousGoodsCodeLithiumIon() throws JsonParseException, JsonMappingException, IOException {
		String productInfoSource = "{\"batteries\": \"[{\\\"batteryType\\\":\\\"LithiumIon\\\",\\\"rechargeable\\\":false,\\\"cellsNumber\\\":\\\"2\\\",\\\"votage\\\":\\\"1.5\\\",\\\"capacity\\\":\\\"66.6666666\\\",\\\"weight\\\":\\\"9\\\",\\\"batteryFileLineItems\\\":[{\\\"file\\\":\\\"R PIDS..pdf\\\"}],\\\"appliedVariationProduct\\\":[\\\"85U06B01R0-Blue\\\",\\\"85U06P01R0-Pink\\\",\\\"85U06R01R0-Orange\\\"], \\\"packingWay\\\":\\\"packedWithEquipment\\\"}]\"}";
		String jsonString = uco.updateDangerousGoodsCode(productInfoSource);
	
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNodePIS = mapper.readValue(jsonString, JsonNode.class);
		String batteries2 = jsonNodePIS.get("batteries").asText();
		JsonNode batteriesNode2 = mapper.readValue(batteries2, JsonNode.class);
		JsonNode batteryNode = batteriesNode2.get(0);
		System.out.println("packingWay: " + batteryNode.get("packingWay").asText());
		System.out.println("voltage: " + batteryNode.get("votage").asDouble());
		System.out.println("capacity: " + batteryNode.get("capacity").asDouble());
		System.out.println("dangerousGoodsCode: " + batteryNode.get("dangerousGoodsCode").asText());
		assertEquals("UN3481-P1966", batteryNode.get("dangerousGoodsCode").asText());
	}
	
	@Test
	public void TestCalculateDangerousGoodsCodeLithiumMetal() throws JsonParseException, JsonMappingException, IOException {
		String productInfoSource = "{\"batteries\": \"[{\\\"batteryType\\\":\\\"LithiumMetal\\\",\\\"rechargeable\\\":false,\\\"cellsNumber\\\":\\\"2\\\",\\\"votage\\\":\\\"1.5\\\",\\\"capacity\\\":\\\"66.6666666\\\",\\\"weight\\\":\\\"2.0000\\\",\\\"batteryFileLineItems\\\":[{\\\"file\\\":\\\"R PIDS..pdf\\\"}],\\\"appliedVariationProduct\\\":[\\\"85U06B01R0-Blue\\\",\\\"85U06P01R0-Pink\\\",\\\"85U06R01R0-Orange\\\"], \\\"packingWay\\\":\\\"packedInEquipment\\\"}]\"}";
		String jsonString = uco.updateDangerousGoodsCode(productInfoSource);
	
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNodePIS = mapper.readValue(jsonString, JsonNode.class);
		String batteries2 = jsonNodePIS.get("batteries").asText();
		JsonNode batteriesNode2 = mapper.readValue(batteries2, JsonNode.class);
		JsonNode batteryNode = batteriesNode2.get(0);
		System.out.println("packingWay: " + batteryNode.get("packingWay").asText());
		System.out.println("weight: " + batteryNode.get("weight").asDouble());
		System.out.println("dangerousGoodsCode: " + batteryNode.get("dangerousGoodsCode").asText());
		assertEquals("UN3091-P1967", batteryNode.get("dangerousGoodsCode").asText());
	}
	
	@Test
	public void TestCalculateDangerousGoodsCodeLithiumMetalNone() throws JsonParseException, JsonMappingException, IOException {
		String productInfoSource = "{\"batteries\": \"[{\\\"batteryType\\\":\\\"LithiumMetal\\\",\\\"rechargeable\\\":false,\\\"cellsNumber\\\":\\\"2\\\",\\\"votage\\\":\\\"1.5\\\",\\\"capacity\\\":\\\"66.6666666\\\",\\\"weight\\\":\\\"2.00000001\\\",\\\"batteryFileLineItems\\\":[{\\\"file\\\":\\\"R PIDS..pdf\\\"}],\\\"appliedVariationProduct\\\":[\\\"85U06B01R0-Blue\\\",\\\"85U06P01R0-Pink\\\",\\\"85U06R01R0-Orange\\\"], \\\"packingWay\\\":\\\"packedInEquipment\\\"}]\"}";
		String jsonString = uco.updateDangerousGoodsCode(productInfoSource);
	
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNodePIS = mapper.readValue(jsonString, JsonNode.class);
		String batteries2 = jsonNodePIS.get("batteries").asText();
		JsonNode batteriesNode2 = mapper.readValue(batteries2, JsonNode.class);
		JsonNode batteryNode = batteriesNode2.get(0);
		System.out.println("packingWay: " + batteryNode.get("packingWay").asText());
		System.out.println("weight: " + batteryNode.get("weight").asDouble());
		System.out.println("dangerousGoodsCode: " + batteryNode.get("dangerousGoodsCode").asText());
		assertEquals("none", batteryNode.get("dangerousGoodsCode").asText());
	}
	*/
	
}