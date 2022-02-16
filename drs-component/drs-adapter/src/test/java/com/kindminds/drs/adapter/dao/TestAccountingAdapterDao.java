package com.kindminds.drs.adapter.dao;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.kindminds.drs.api.adapter.AccountingAdapterDao;
import com.kindminds.drs.v1.model.AccountJournalEntryImplForTest;
import com.kindminds.drs.v1.model.AccountJournalEntryLineItemImplForTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestAccountingAdapterDao {
	
	@Autowired private AccountingAdapterDao dao;
	
	@Test
	public void testCheckConnection(){
		dao.setUserName("admin_kmi");
		dao.setPassword("adminkmi");
		dao.checkConnection();
		
		BigDecimal amnt = new BigDecimal("777");
		BigDecimal zero = BigDecimal.ZERO;
		AccountJournalEntryImplForTest entry = new AccountJournalEntryImplForTest("Stock Journal", "03/2015", "2015-03-05", null);
		entry.addLineItem(new AccountJournalEntryLineItemImplForTest("1st Line", "1211", amnt, zero));
		entry.addLineItem(new AccountJournalEntryLineItemImplForTest("2nd Line", "1213", zero, amnt));
		
		dao.createEntry(entry);
		
		assertEquals(1,1);
	}
}
