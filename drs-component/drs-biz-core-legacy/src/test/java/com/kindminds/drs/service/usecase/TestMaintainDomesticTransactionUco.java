package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
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


import com.kindminds.drs.api.usecase.accounting.MaintainDomesticTransactionUco;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.DomesticTransactionImpl;
import com.kindminds.drs.impl.DomesticTransactionLineItemImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainDomesticTransactionUco {
	
	@Autowired private MaintainDomesticTransactionUco uco;
	@Autowired private AuthenticationManager authenticationManager;
	
	private static String todayInUtcDate;
	private static String tomorrowInUtcDate;
	
	@BeforeClass
	public static void setTodayAndTomorrowInUtcDate() {
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.format("%02d", calendar.get(Calendar.MONTH)+1);
		String date = String.format("%02d", calendar.get(Calendar.DATE));
		todayInUtcDate = year+"-"+month+"-"+date;
		calendar.add(Calendar.DATE, 1);
		year = String.valueOf(calendar.get(Calendar.YEAR));
		month = String.format("%02d", calendar.get(Calendar.MONTH)+1);
		date = String.format("%02d", calendar.get(Calendar.DATE));
		tomorrowInUtcDate = year+"-"+month+"-"+date;
	}
	
	@Test
	public void testGetDefaultSalesTaxPercentage(){
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Assert.assertEquals("5", this.uco.getDefaultSalesTaxPercentage());
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testUpdateDateBeforeEarliestAvailableDate(){
		DomesticTransactionImpl expected = new DomesticTransactionImpl(null,todayInUtcDate,"K2","KindMinds","K510","Sound Land","inv","5","TWD","888","44","932",new ArrayList<>());
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","111"));
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","777"));
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Integer id = this.uco.create(expected);
		expected.setId(id);
		expected.setUtcDate("2017-02-04");
		assertNull(this.uco.update(expected));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testUpdate(){
		DomesticTransactionImpl expected = new DomesticTransactionImpl(null,todayInUtcDate,"K2","KindMinds","K510","Sound Land","inv","5","TWD","888","44","932",new ArrayList<>());
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","111"));
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","777"));
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Integer id = this.uco.create(expected);
		expected.getLineItems().clear();
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","125"));
		expected.setId(id);
		expected.setSplrKcode("K488");
		expected.setUtcDate(tomorrowInUtcDate);
		expected.setTaxPercentage("3");
		expected.setInvoiceNumber("invoice");
		this.uco.update(expected);
		expected.setSplrName("NextDrive");
		expected.setAmountSubtotal("125");
		expected.setAmountTax("4");
		expected.setAmountTotal("129");
		expected.setIsEditable(true);
		MockAuth.logout();
		assertEquals(expected, this.uco.get(id));
	}
	
	@Test @Transactional
	public void testDelete(){
		DomesticTransactionImpl expected = new DomesticTransactionImpl(null,todayInUtcDate,"K2","KindMinds","K510","Sound Land","inv","5","TWD","888","44","932",new ArrayList<>());
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","111"));
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGES_PREPARATION","","777"));
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Integer id = this.uco.create(expected);
		MockAuth.logout();
		this.uco.delete(id);
		assertTrue(!this.isInDtoList(expected));
	}
	
	@Test @Transactional
	public void testGetLineItemTypeKeyName(){
		Map<Integer,String> result = this.uco.getLineItemTypeKeyName();
		assertEquals(16,result.size());
		assertEquals(result.get(18), "COPYWRITING");
		assertEquals(result.get(19), "IMAGE_PREPARATION");
	}
	
	@Test @Transactional
	public void testCreateBeforeEarliestAvailableDate(){
		DomesticTransactionImpl expected = new DomesticTransactionImpl(null,"2016-11-17","K2","KindMinds","K510","Sound Land","inv","5","TWD","888","44","932",new ArrayList<>());
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","111"));
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGE_PREPARATION","","777"));
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		assertNull(this.uco.create(expected));
		MockAuth.logout();
	}
	
	@Test @Transactional
	public void testCreate(){
		DomesticTransactionImpl expected = new DomesticTransactionImpl(null,todayInUtcDate,"K2","KindMinds","K510","Sound Land","inv","5","TWD","888","44","932",new ArrayList<>());
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","111"));
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGE_PREPARATION","","777"));
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Integer id = this.uco.create(expected);
		MockAuth.logout();
		expected.setId(id);
		expected.setIsEditable(true);
		assertEquals(expected,this.uco.get(id));
	}
	
	@Test @Transactional
	public void testGetList(){
		DomesticTransactionImpl expected = new DomesticTransactionImpl(null,todayInUtcDate,"K2","KindMinds","K510","Sound Land","inv","5","TWD","888","44","932",new ArrayList<>());
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(1,18,"COPYWRITING","note","111"));
		expected.getLineItems().add(new DomesticTransactionLineItemImpl(2,19,"IMAGE_PREPARATION","","777"));
		MockAuth.login(authenticationManager, "joanna.lee@tw.drs.network", "9HfPBqdR");
		Integer id = this.uco.create(expected);
		expected.setId(id);
		expected.setIsEditable(true);
		MockAuth.logout();
		assertTrue(this.isInDtoList(expected));
	}
	
	private boolean isInDtoList(DomesticTransaction expected){
		int pageIndex = 1;
		int totalPages = 0;
		do {
			DtoList<DomesticTransaction> dtoList = this.uco.getList(pageIndex);
			totalPages = dtoList.getPager().getTotalPages();
			for(DomesticTransaction dt:dtoList.getItems()){
				if(dt.getId().equals(expected.getId())&&expected.isEditable().equals(dt.isEditable())) return true;
			}
			pageIndex+=1;
		} while (pageIndex<totalPages);
		return false;
	}

}
