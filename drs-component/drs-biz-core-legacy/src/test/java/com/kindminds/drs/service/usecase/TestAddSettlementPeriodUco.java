package com.kindminds.drs.service.usecase;

import static org.junit.Assert.assertEquals;

import java.util.Date;


import com.kindminds.drs.service.usecase.accounting.AddSettlementPeriodUcoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.settlement.AddSettlementPeriodUco;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import com.kindminds.drs.util.DateHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestAddSettlementPeriodUco {
	
	@Autowired private AddSettlementPeriodUco uco;
	@Autowired private SettlementPeriodDao settlementPeriodRepo;
	
	private Date getLastPeriodEnd(){
		//return this.settlementPeriodRepo.queryRecentPeriods(1).get(0).getEndPoint();
		return null;
	}
	
	@Test @Transactional
	public void testAddPeriodOnTime(){
		Date lastPeriodEnd = this.getLastPeriodEnd();
		Date expectNewPeriodEnd = DateHelper.getDaysAfter(lastPeriodEnd, 13);
		Date now = DateHelper.getDaysAfter(lastPeriodEnd, 14);
		String result = ((AddSettlementPeriodUcoImpl)this.uco).addPeriod(now);
		String expectNewPeriodStartText = DateHelper.toString(lastPeriodEnd, "yyyyMMdd","UTC");
		String expectNewPeriodEndText = DateHelper.toString(expectNewPeriodEnd, "yyyyMMdd","UTC");
		assertEquals("Period " + expectNewPeriodStartText + " to " +expectNewPeriodEndText+ " has been successfully added.", result);
	}
	
	@Test @Transactional
	public void testAddPeriodLateEnough(){
		Date lastPeriodEnd = this.getLastPeriodEnd();
		Date expectNewPeriodEnd = DateHelper.getDaysAfter(lastPeriodEnd, 13);
		Date now = DateHelper.getDaysAfter(lastPeriodEnd, 20);
		String result = ((AddSettlementPeriodUcoImpl)this.uco).addPeriod(now);
		String expectNewPeriodStartText = DateHelper.toString(lastPeriodEnd, "yyyyMMdd","UTC");
		String expectNewPeriodEndText = DateHelper.toString(expectNewPeriodEnd, "yyyyMMdd","UTC");
		assertEquals("Period " + expectNewPeriodStartText + " to " +expectNewPeriodEndText+ " has been successfully added.", result);
	}
	
	@Test @Transactional
	public void testAddPeriodTooEarly(){
		Date lastPeriodEnd = this.getLastPeriodEnd();
		Date now = DateHelper.getDaysAfter(lastPeriodEnd, 7);
		String result = ((AddSettlementPeriodUcoImpl)this.uco).addPeriod(now);
		Date periodEndDate = DateHelper.getDaysAfter(lastPeriodEnd, -1);
		String end = DateHelper.toString(periodEndDate, "yyyyMMdd", "UTC");
		assertEquals("No period added, current last period end is " + end, result);
	}
}