package com.kindminds.drs.service.usecase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.report.HistoryLine;

import com.kindminds.drs.service.security.MockAuth;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.powermock.reflect.Whitebox;


import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewSalesAndPageTrafficReportUco;
import com.kindminds.drs.api.usecase.ViewSalesAndPageTrafficReportUco.ChartData;

import com.kindminds.drs.api.v1.model.report.SalesAndPageTrafficReport;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.util.DateHelper;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestViewSalesAndPageTrafficReportUco {
	private static final String DAY = "1";
	private static final String WEEK = "2";
	private static final String MONTH = "3";
	
	
	@Autowired private ViewSalesAndPageTrafficReportUco uco;
	@Autowired private AuthenticationManager authenticationManager;

	@Test
	public void testPrivateMethod(){
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			assertEquals(
				Marketplace.AMAZON_COM,
				Whitebox.invokeMethod(uco, "getMarketplace"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSupplierKcodeNamesBySupplier(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		assertNull(this.uco.getSupplierKcodeNames());
		MockAuth.logout();
	}
	
	@Test
	public void testGetSupplierKcodeNamesByDrsUser(){
		MockAuth.login(authenticationManager,"ted.hwang@tw.drs.network", "NsFp6Ax5");
		assertNotNull(this.uco.getSupplierKcodeNames());
		MockAuth.logout();
	}
	
	@Test
	public void testGetProductBasesByDrsUser(){
		MockAuth.login(authenticationManager,"ted.hwang@tw.drs.network", "NsFp6Ax5");
		Map<String, String> productBases = this.uco.getProductBases("K520");
		for(String productBase:productBases.keySet()){
			assertTrue(productBase,productBase.startsWith("BP-K520"));
		}
		MockAuth.logout();
	}
	
	@Test
	public void testGetProductBasesBySupplier(){
		MockAuth.login(authenticationManager,"junping@hanchor.com", "HNKbY5Qs");
		for(String productBase:this.uco.getProductBases(    "").keySet()) assertTrue(productBase,productBase.startsWith("BP-K486"));
		for(String productBase:this.uco.getProductBases("K520").keySet()) assertTrue(productBase,productBase.startsWith("BP-K486"));
		MockAuth.logout();
	}
	
	@Test
	public void testGetProductBasesEmpty(){
		MockAuth.login(authenticationManager,"ted.hwang@tw.drs.network", "NsFp6Ax5");
		Map<String, String> productBases = this.uco.getProductBases("");
		assertTrue(productBases.isEmpty());
		MockAuth.logout();
	}
	
	@Test
	public void testGetProductSku(){
		Map<String, String> productSkus = this.uco.getProductSkus("BP-K520-TF002P-S-1A");
		for(String productSku:productSkus.keySet()){
			assertTrue(productSku,productSku.startsWith("K520-TF002P-S-1A"));
		}
	}
	
	@Test
	public void testGetProductSkuEmpty(){
		Map<String, String> productSkus = this.uco.getProductSkus("");
		assertTrue(productSkus.isEmpty());
	}
	
	@Test
	public void testSetMarketplaceNull(){
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "");
			assertNull(
				Whitebox.invokeMethod(uco, "getMarketplace"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSetMarketplaceValid(){
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			assertEquals(
				Marketplace.AMAZON_COM,
				Whitebox.invokeMethod(uco, "getMarketplace"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateAsinsEmptyProduct() {
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			List<String> asins = Whitebox.invokeMethod(uco, "generateAsins", "", 
					"K510-85U06B01R0-Blue");
			assertNull(asins);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateAsinsSingleProduct() {
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			List<String> asins = Whitebox.invokeMethod(uco, "generateAsins", "BP-K510-85U05001R0", 
					"");
			assertEquals(asins.toString(), "[B01M68UK38, B01M68UK38, B06XYMLHJ2]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateAsinsMultipleProduct() {
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			List<String> asins = Whitebox.invokeMethod(uco, "generateAsins", "BP-K510-85U05001R0,BP-K510-85U09001R0,BP-K510-85U13B01R0", 
					"");
			assertEquals(asins.toString(), "[B01M68UK38, B06XXP46X2, B01M68UK38, B06XY2DFKG, B06XYMLHJ2]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateAsinsMultipleProductNullSKU() {
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			List<String> asins = Whitebox.invokeMethod(uco, "generateAsins", "BP-K510-85U05001R0,BP-K510-85U09001R0,BP-K510-85U13B01R0", 
					null);
			assertEquals(asins.toString(), "[B01M68UK38, B06XXP46X2, B01M68UK38, B06XY2DFKG, B06XYMLHJ2]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateAsinsSingleSKU() {
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			List<String> asins = Whitebox.invokeMethod(uco, "generateAsins", "BP-K510-85U06B01R0", 
					"K510-85U06B01R0-Blue");
			assertEquals(asins.toString(), "[B01LPT8JY8]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenerateAsinsMultipleSKU() {
		try {
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			List<String> asins = Whitebox.invokeMethod(uco, "generateAsins", "BP-K510-85U06B01R0", 
					"K510-85U06B01R0-Blue,K510-85U06R01R0-Orange,K510-85U06P01R0-Pink");
			assertEquals(asins.toString(), "[B01LPT8K10, B01LPT8KQA, B01LPT8JY8]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculateDateRange() {
		try {
			Date start = DateHelper.toDate("2016-02-28 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2017-02-27 UTC", "yyyy-MM-dd z");
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			Whitebox.invokeMethod(uco, "setAsins", "BP-K510-85U06B01R0", 
					"K510-85U06B01R0-Blue,K510-85U06R01R0-Orange,K510-85U06P01R0-Pink");
			Whitebox.invokeMethod(uco, "calculateDateRange", "1", start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetReportByProductSku(){
		Date start = DateHelper.toDate("2016-02-28 UTC", "yyyy-MM-dd z");
		Date end = DateHelper.toDate("2017-02-27 UTC", "yyyy-MM-dd z");
		SalesAndPageTrafficReport report = this.uco.getReport("1",
				"BP-K520-TF002P-S-1A", "K520-TF002P-S-1AB", "6", start, end);
		assertEquals(report.getCurrency(), Currency.USD);
		Assert.assertEquals(report.getStartDate(), "2016-02-28");
		Assert.assertEquals(report.getEndDate(), "2017-02-27");
		Assert.assertEquals(report.getTotalOrderItems(), "27");
		Assert.assertEquals(report.getTotalOrderedProductSales(), "1338.64");
		Assert.assertEquals(report.getTotalSessions(), "4618");
	}
	
	@Test
	public void testGetReportByProductBase(){
		Date start = DateHelper.toDate("2018-01-01 UTC", "yyyy-MM-dd z");
		Date end = DateHelper.toDate("2018-06-02 UTC", "yyyy-MM-dd z");
		SalesAndPageTrafficReport report = this.uco.getReport("1",
				"BP-K520-TF002P-S-1A", "", "6", start, end);
		assertEquals(report.getCurrency(), Currency.USD);
		Assert.assertEquals(report.getStartDate(), "2018-01-01");
		Assert.assertEquals(report.getEndDate(), "2018-06-02");
		Assert.assertEquals(report.getTotalOrderItems(), "14");
		Assert.assertEquals(report.getTotalOrderedProductSales(), "684.86");
		Assert.assertEquals(report.getTotalSessions(), "2689");
	}
	
	@Test
	public void testCreateChartDataPointsDAY(){
		try {
			Date start = DateHelper.toDate("2016-02-28 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2017-02-27 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", DAY, start, end);
			assertEquals(dataPoints.size(), 365);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateChartDataPointsWEEK(){
		try {
			Date start = DateHelper.toDate("2016-02-28 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2017-02-27 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", WEEK, start, end);
			assertEquals(dataPoints.size(), 53);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateChartDataPointsMONTH(){
		try {
			Date start = DateHelper.toDate("2016-02-28 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2017-02-27 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", MONTH, start, end);
			assertEquals(dataPoints.size(), 13);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChartDatasUsingScaleDay(){
		try {
			Date start = DateHelper.toDate("2017-02-03 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2018-04-27 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", DAY, start, end);
			assertEquals(448, dataPoints.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChartDatasUsingDayLastWeek(){
		try {
			Date start = DateHelper.toDate("2018-04-29 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2018-05-06 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", DAY, start, end);
			assertEquals(7, dataPoints.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChartDatasUsingWeekLastMonth(){
		try {
			Date start = DateHelper.toDate("2018-04-01 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2018-04-30 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", WEEK, start, end);
			assertEquals(5, dataPoints.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChartDatasUsingWeekYearToDate(){
		try {
			Date start = DateHelper.toDate("2018-01-01 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2018-05-17 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", WEEK, start, end);
			assertEquals(20, dataPoints.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChartDatasUsingWeekLifetime(){
		try {
			Date start = DateHelper.toDate("2016-09-25 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2018-05-17 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", WEEK, start, end);
			assertEquals(86, dataPoints.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChartDatasUsingMonthLifetime(){
		try {
			Date start = DateHelper.toDate("2016-09-25 UTC", "yyyy-MM-dd z");
			Date end = DateHelper.toDate("2018-05-17 UTC", "yyyy-MM-dd z");
			List<ChartData> dataPoints = Whitebox.invokeMethod(uco, "createChartDataPoints", MONTH, start, end);
			assertEquals(21, dataPoints.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSalesHistoryWeekAvgLifetime() {
		try {
			Date start = DateHelper.toDate("2016-09-25", "yyyy-MM-dd");
			Date end = DateHelper.toDate("2018-05-26", "yyyy-MM-dd");
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			Whitebox.invokeMethod(uco, "setAsins", "BP-K510-85U06B01R0", 
					"K510-85U06B01R0-Blue,K510-85U06R01R0-Orange,K510-85U06P01R0-Pink");
			Whitebox.invokeMethod(uco, "calculateDateRange", "6", start, end);
			DtoList<HistoryLine> pageHistory = uco.getSalesHistory(2, WEEK, 2);
			Assert.assertEquals(pageHistory.getItems().size(), 37);
			Assert.assertEquals(pageHistory.getItems().get(0).getOrderedProductSales(), "85.69");
			Assert.assertEquals(pageHistory.getItems().get(30).getSessions(), "103.57");
			Assert.assertEquals(pageHistory.getItems().get(30).getOrderedProductSales(), "148.53");
			Assert.assertEquals(pageHistory.getItems().get(36).getOrderedProductSales(), "85.69");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSalesHistoryDayLifetimePage4() {
		try {
			Date start = DateHelper.toDate("2016-09-25", "yyyy-MM-dd");
			Date end = DateHelper.toDate("2018-05-26", "yyyy-MM-dd");
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			Whitebox.invokeMethod(uco, "setAsins", "BP-K510-85U06B01R0", 
					"K510-85U06B01R0-Blue,K510-85U06R01R0-Orange,K510-85U06P01R0-Pink");
			Whitebox.invokeMethod(uco, "calculateDateRange", "6", start, end);
			DtoList<HistoryLine> pageHistory = uco.getSalesHistory(4, DAY, 1);
			Assert.assertEquals(pageHistory.getItems().size(), 50);
			Assert.assertEquals(pageHistory.getItems().get(0).getOrderedProductSales(), "429.00");
			Assert.assertEquals(pageHistory.getItems().get(49).getOrderedProductSales(), "117.00");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSalesHistoryDayYearToDate() {
		try {
			Date start = DateHelper.toDate("2018-01-01", "yyyy-MM-dd");
			Date end = DateHelper.toDate("2018-05-26", "yyyy-MM-dd");
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			Whitebox.invokeMethod(uco, "setAsins",
					"BP-K510-85U05001R0,BP-K510-85U09001R0,BP-K510-85U13B01R0", "");
			Whitebox.invokeMethod(uco, "calculateDateRange", "6", start, end);
			DtoList<HistoryLine> pageHistory = uco.getSalesHistory(2, DAY, 1);
			Assert.assertEquals(pageHistory.getItems().size(), 50);
			Assert.assertEquals(pageHistory.getItems().get(0).getOrderedProductSales(), "349.95");
			Assert.assertEquals(pageHistory.getItems().get(49).getOrderedProductSales(), "362.96");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSalesHistoryMonthAvgLifetime() {
		try {
			Date start = DateHelper.toDate("2016-12-10", "yyyy-MM-dd");
			Date end = DateHelper.toDate("2018-05-26", "yyyy-MM-dd");
			Whitebox.invokeMethod(uco, "setMarketplace", "1");
			Whitebox.invokeMethod(uco, "setAsins",
					"BP-K510-85U05001R0,BP-K510-85U09001R0,BP-K510-85U13B01R0", "");
			Whitebox.invokeMethod(uco, "calculateDateRange", "6", start, end);
			DtoList<HistoryLine> pageHistory = uco.getSalesHistory(1, MONTH, 2);
			Assert.assertEquals(pageHistory.getItems().size(), 18);
			Assert.assertEquals(pageHistory.getItems().get(0).getOrderedProductSales(), "31.36");
			Assert.assertEquals(pageHistory.getItems().get(17).getOrderedProductSales(), "172.78");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
