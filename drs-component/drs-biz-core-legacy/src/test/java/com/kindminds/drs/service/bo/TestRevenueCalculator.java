package com.kindminds.drs.service.bo;

import com.kindminds.drs.Country;
import com.kindminds.drs.core.biz.RevenueCalculator;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestRevenueCalculator {
	
	@Test
	public void test(){
		RevenueCalculator revenueCalculator = (RevenueCalculator)SpringAppCtx.get().getBean("revenueCalculator");
		Date start = this.fromUtcTimeToDate("2017-07-11 08:40:56");
		Date end   = this.fromUtcTimeToDate("2017-07-11 22:11:39");
		Map<Country,Map<String,BigDecimal>> countryProductBaseRevenue = revenueCalculator.getRevenueGroupByCountryAndProductBase(start,end,"K510");
		assertEquals(3,countryProductBaseRevenue.size());
		assertEquals(2,countryProductBaseRevenue.get(Country.US).size());
		assertEquals(1,countryProductBaseRevenue.get(Country.UK).size());
		assertEquals(1,countryProductBaseRevenue.get(Country.CA).size());
		assertEquals(new BigDecimal("471.18"),countryProductBaseRevenue.get(Country.US).get("BP-K510-85U05001R0"));
		assertEquals(new BigDecimal("119.97"),countryProductBaseRevenue.get(Country.US).get("BP-K510-85U06B01R0"));
		assertEquals(new BigDecimal( "97.50"),countryProductBaseRevenue.get(Country.UK).get("BP-K510-85U06B01R0"));
		assertEquals(new BigDecimal("-51.00"),countryProductBaseRevenue.get(Country.CA).get("BP-K510-85U06B01R0"));
	}
	
	@Test
	public void testWarehouseLostExcluded() {
		RevenueCalculator revenueCalculator = (RevenueCalculator)SpringAppCtx.get().getBean("revenueCalculator");
		Date start = this.fromUtcTimeToDate("2017-07-02 10:15:50");
		Date end   = this.fromUtcTimeToDate("2017-07-03 23:31:52");
		Map<Country,Map<String,BigDecimal>> countryProductBaseRevenue = revenueCalculator.getRevenueGroupByCountryAndProductBase(start,end,"K520");
		assertEquals(2,countryProductBaseRevenue.size());
		assertEquals(1,countryProductBaseRevenue.get(Country.US).size());
		assertEquals(2,countryProductBaseRevenue.get(Country.UK).size());
		assertEquals(new BigDecimal("69.98"),countryProductBaseRevenue.get(Country.US).get("BP-K520-TF002C-SC-1A"));
		assertEquals(new BigDecimal("67.50"),countryProductBaseRevenue.get(Country.UK).get("BP-K520-TF002C-SC-1A"));
		assertEquals(new BigDecimal("75.00"),countryProductBaseRevenue.get(Country.UK).get("BP-K520-TF002H-S-1A"));
	}
	
	@Test
	public void testEbayIncluded() {
		RevenueCalculator revenueCalculator = (RevenueCalculator)SpringAppCtx.get().getBean("revenueCalculator");
		Date start = this.fromUtcTimeToDate("2017-05-28 00:00:00");
		Date end   = this.fromUtcTimeToDate("2017-06-11 00:00:00");
		Map<Country,Map<String,BigDecimal>> countryProductBaseRevenue = revenueCalculator.getRevenueGroupByCountryAndProductBase(start,end,"K526");
		assertEquals(countryProductBaseRevenue.toString(),4,countryProductBaseRevenue.size());
		assertEquals(1,countryProductBaseRevenue.get(Country.US).size());
		assertEquals(1,countryProductBaseRevenue.get(Country.CA).size());
		assertEquals(1,countryProductBaseRevenue.get(Country.DE).size());
		assertEquals(1,countryProductBaseRevenue.get(Country.FR).size());
		assertEquals(new BigDecimal("169.000000"),countryProductBaseRevenue.get(Country.US).get("BP-K526-PawboPlus"));
		assertEquals(new BigDecimal("468.00"),    countryProductBaseRevenue.get(Country.CA).get("BP-K526-PawboPlus"));
		assertEquals(new BigDecimal("334.453782"),countryProductBaseRevenue.get(Country.DE).get("BP-K526-PawboPlus"));
		assertEquals(new BigDecimal("140.83"),    countryProductBaseRevenue.get(Country.FR).get("BP-K526-PawboPlus"));
	}
	
	@Test
	public void testShopifyIncluded() {
		RevenueCalculator revenueCalculator = (RevenueCalculator) SpringAppCtx.get().getBean("revenueCalculator");
		Date start = this.fromUtcTimeToDate("2017-02-05 00:00:00");
		Date end   = this.fromUtcTimeToDate("2017-02-19 00:00:00");
		Map<Country,Map<String,BigDecimal>> countryProductBaseRevenue = revenueCalculator.getRevenueGroupByCountryAndProductBase(start,end,"K504");
		assertEquals(countryProductBaseRevenue.toString(),2,countryProductBaseRevenue.size());
		assertEquals(1,countryProductBaseRevenue.get(Country.US).size());
		assertEquals(1,countryProductBaseRevenue.get(Country.UK).size());
		assertEquals(new BigDecimal("3058.000000"),countryProductBaseRevenue.get(Country.US).get("BP-K504-TD-3140"));
		assertEquals(new BigDecimal( "109.99"),    countryProductBaseRevenue.get(Country.UK).get("BP-K504-TD-3140"));
	}
	
	private Date fromUtcTimeToDate(String dateStr) {
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd HH:mm:ss Z");
	}
}
