package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.Country;
import com.kindminds.drs.RevenueGrade;
import com.kindminds.drs.api.usecase.CalculateRetainmentRateUco;
import com.kindminds.drs.api.v1.model.accounting.RetainmentRate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestCalculateRetainmentRateUco {
	
	@Autowired private CalculateRetainmentRateUco uco;
	
	@Test @Transactional @Ignore
	public void testCalculate(){
		String result = this.uco.calculate();
		assertTrue(result.contains("successfully calculated"));
	}
	
	@Test @Transactional
	public void testCalculateAndAccessFromDb(){
		String start="2015-11-01",end="2016-04-30";
		this.uco.calculateForTest(start,end);
		Assert.assertEquals("0.147606",this.uco.getRate(start,end,Country.US,"K151"));
		Assert.assertEquals("0.137550",this.uco.getRate(start,end,Country.US,"K486"));
		Assert.assertEquals("0.126237",this.uco.getRate(start,end,Country.US,"K488"));
		Assert.assertEquals("0.131906",this.uco.getRate(start,end,Country.US,"K489"));
		Assert.assertEquals("0.150000",this.uco.getRate(start,end,Country.US,"K491"));
	}
	
	@Test @Transactional
	public void testCalculateAndAccessFromDbEbayUsDe(){
		String start="2017-05-29",end="2017-06-10";
		this.uco.calculateForTest(start,end);
		Assert.assertEquals("0.150000",this.uco.getRate(start,end,Country.US,"K526"));
		Assert.assertEquals("0.150000",this.uco.getRate(start,end,Country.CA,"K526"));
		Assert.assertEquals("0.150000",this.uco.getRate(start,end,Country.DE,"K526"));
		Assert.assertEquals("0.150000",this.uco.getRate(start,end,Country.FR,"K526"));
		Assert.assertEquals("169.0000",this.uco.getRevenueInOriginalCurrency(start,end,Country.US,"K526"));
		Assert.assertEquals("468.0000",this.uco.getRevenueInOriginalCurrency(start,end,Country.CA,"K526"));
		Assert.assertEquals("334.4538",this.uco.getRevenueInOriginalCurrency(start,end,Country.DE,"K526"));
		Assert.assertEquals("140.8300",this.uco.getRevenueInOriginalCurrency(start,end,Country.FR,"K526"));
	}
	
	@Test @Transactional
	public void testGetList(){
		List<RetainmentRate> resultList = this.uco.getList();
		assertTrue(resultList.size()>=7);
	}
	
	@Test @Transactional
	public void testDelete(){
		String start="2015-11-01",end="2016-04-30";
		this.uco.calculateForTest(start,end);
		List<RetainmentRate> resultList = this.uco.getList();
		int originalListSize = resultList.size();
		int rateIdToDelete = resultList.get(0).getId();
		this.uco.delete(rateIdToDelete);
		resultList = this.uco.getList();
		assertEquals(originalListSize-1,resultList.size());
	}
	
	@Test
	public void testCalculatorBoundary(){
		assertEquals(new BigDecimal("0.15"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal(    "0")));
		assertEquals(new BigDecimal("0.150000"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal( "1000")));
		assertEquals(new BigDecimal("0.144545"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal( "2200")));
		assertEquals(new BigDecimal("0.136957"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal( "4600")));
		assertEquals(new BigDecimal("0.127800"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal("10000")));
		assertEquals(new BigDecimal("0.121533"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal("15000")));
		assertEquals(new BigDecimal("0.116900"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal("20000")));
		assertEquals(new BigDecimal("0.113120"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal("25000")));
		assertEquals(new BigDecimal("0.107438"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal("32000")));
		assertEquals(new BigDecimal("0.100950"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal("40000")));
		assertEquals(new BigDecimal("0.097178"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal("45000")));
		assertEquals(new BigDecimal("0.088133"),
				RevenueGrade.calculateEffectiveRetainmentRate(new BigDecimal("60000")));
	}
}
