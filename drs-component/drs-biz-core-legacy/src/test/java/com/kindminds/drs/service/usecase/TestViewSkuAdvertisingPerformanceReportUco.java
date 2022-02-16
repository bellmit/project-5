package com.kindminds.drs.service.usecase;

import java.util.ArrayList;
import java.util.TreeMap;


import com.kindminds.drs.impl.SkuAdvPerformanceReportImpl;
import com.kindminds.drs.impl.SkuAdvertisingPerformanceReportLineItemImpl;
import com.kindminds.drs.service.security.MockAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.kindminds.drs.Currency;
import com.kindminds.drs.api.usecase.ViewSkuAdvertisingPerformanceReportUco;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReport;



import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestViewSkuAdvertisingPerformanceReportUco {
	
	@Autowired private ViewSkuAdvertisingPerformanceReportUco uco;
	@Autowired private AuthenticationManager authenticationManager;
	
	@Test
	public void testGetReport(){
		String supplierKcode = "K151";
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		//SkuAdvertisingPerformanceReport report = this.uco.getReport(supplierKcode,"1","2016-05-01","2016-05-31");
		SkuAdvertisingPerformanceReport report = this.uco.getReport(supplierKcode,"1","2016-05-01","2016-05-31","Hsumao: Futura Soap Dispenser - Auto");
		MockAuth.logout();
		SkuAdvPerformanceReportImpl expect = new SkuAdvPerformanceReportImpl("Amazon.com","USD","5,767","22","0.38%","3.04","0.14","3","115.85","2.62%",new TreeMap<>());
		String campaignName = "Hsumao: Futura Soap Dispenser - Auto";
		expect.getCampaignItems().put(campaignName, new ArrayList<>(2));
		expect.getCampaignItems().get(campaignName).add(new SkuAdvertisingPerformanceReportLineItemImpl(Currency.USD,"K151-DH-500CP","2,036","17","0.84%","2.45","0.14","3","115.85","2.11%"));
		expect.getCampaignItems().get(campaignName).add(new SkuAdvertisingPerformanceReportLineItemImpl(Currency.USD,"K151-DH-500W", "3,731", "5","0.13%","0.59","0.12","0",  "0.00",  "N/A"));
		assertEquals(expect,report);
	}
	
	@Test
	public void testGetReportWithNoLineItem(){
		String supplierKcode = "K151";
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		//SkuAdvertisingPerformanceReport report = this.uco.getReport(supplierKcode,"1","2016-04-24","2016-04-24");
		SkuAdvertisingPerformanceReport report = this.uco.getReport(supplierKcode,"1","2016-04-24","2016-04-24","Hsumao: Futura Soap Dispenser - Auto");
		MockAuth.logout();
		SkuAdvPerformanceReportImpl expect = new SkuAdvPerformanceReportImpl("Amazon.com","USD","710","2","0.28%","0.09","0.05","0","0.00","N/A",new TreeMap<>());
		String campaignName = "Hsumao: Futura Soap Dispenser - Auto";
		expect.getCampaignItems().put(campaignName, new ArrayList<>());
		expect.getCampaignItems().get(campaignName).add(new SkuAdvertisingPerformanceReportLineItemImpl(Currency.USD,"K151-DH-500CP","216","2","0.93%","0.09","0.05","0","0.00","N/A"));
		expect.getCampaignItems().get(campaignName).add(new SkuAdvertisingPerformanceReportLineItemImpl(Currency.USD,"K151-DH-500W", "494","0","0.00%","0.00", "N/A","0","0.00","N/A"));
		assertEquals(expect,report);
	}
}
