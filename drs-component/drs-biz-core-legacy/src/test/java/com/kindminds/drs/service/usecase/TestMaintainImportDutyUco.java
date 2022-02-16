package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.kindminds.drs.api.usecase.MaintainImportDutyUco;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction.ImportDutyTransactionLineItem;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.ImportDutyTransactionImpl;
import com.kindminds.drs.impl.ImportDutyTransactionLineItemImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainImportDutyUco {
	
	@Autowired private MaintainImportDutyUco uco;
	
	@Test @Transactional
	public void testGetList(){
		DtoList<ImportDutyTransaction> dtoList = this.uco.getList(1);
		Assert.assertEquals(20, dtoList.getItems().size());
	}
	
	@Test @Transactional
	public void testGetUnsNameListWhichStillWithoutImportDuty(){
		String unsName = "UNS-K2-11";
		List<String> unsNameList = this.uco.getUnsNameList();
		assertTrue(unsNameList.size()>0);
		assertTrue(!unsNameList.contains(unsName));
	}
	
	@Test @Transactional
	public void testGetCountry(){
		String unsName = "UNS-K2-11";
		Assert.assertEquals("US",this.uco.getCountry(unsName));
	}
	
	@Test @Transactional
	public void testGetCurrency(){
		Assert.assertEquals("USD",this.uco.getCurrency("US"));
	}
	
	@Test @Transactional
	public void testGetLineItemInfoForCreate(){
		String unsName = "UNS-K2-11";
		List<ImportDutyTransactionLineItemImpl> expectList = new ArrayList<ImportDutyTransactionLineItemImpl>();
		expectList.add(new ImportDutyTransactionLineItemImpl("IVS-K488-2","K488-PLUG100",306,null));
		expectList.add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70056-01", 25,null));
		expectList.add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70057-01", 15,null));
		Assert.assertEquals(expectList,this.uco.getLineItemInfoForCreate(unsName));
	}
	
	@Test @Transactional @Ignore("too few UNS to be test, reenable if need ")
	public void testCreate(){
		String unsName = "UNS-K2-11";
		ImportDutyTransactionImpl dutyTran = new ImportDutyTransactionImpl(unsName,"2015-11-17", null, null, null, null, new ArrayList<ImportDutyTransactionLineItem>());
		dutyTran.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K488-2","K488-PLUG100",   null,"322.99"));
		dutyTran.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70056-01",null, "11.17"));
		dutyTran.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70057-01",null, "22.99"));
		unsName = this.uco.create(dutyTran);
		ImportDutyTransaction result = this.uco.get(unsName);
		ImportDutyTransactionImpl expect = new ImportDutyTransactionImpl(unsName,"2015-11-17","US","USD","357.15", null, new ArrayList<ImportDutyTransactionLineItem>());
		expect.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K488-2","K488-PLUG100",   306,"322.99"));
		expect.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70056-01", 25, "11.17"));
		expect.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70057-01", 15, "22.99"));
		assertEquals(expect,result);
	}
	
	@Test @Transactional
	public void testUpdate(){
		
		String unsName = "UNS-K2-11";
		ImportDutyTransactionImpl toUpdate = new ImportDutyTransactionImpl(unsName,"2015-11-18",null,null,null,null,new ArrayList<ImportDutyTransactionLineItem>());
		toUpdate.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K488-2","K488-PLUG100",   null,"333.99"));
		toUpdate.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70056-01",null, "11.17"));
		toUpdate.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70057-01",null, "33.99"));
		this.uco.update(toUpdate);
		
		ImportDutyTransaction result = this.uco.get(unsName);
		ImportDutyTransactionImpl expect = new ImportDutyTransactionImpl(unsName,"2015-11-18","US","USD","379.15", null, new ArrayList<ImportDutyTransactionLineItem>());
		expect.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K488-2","K488-PLUG100",  306,"333.99"));
		expect.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70056-01",25, "11.17"));
		expect.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70057-01",15, "33.99"));
		assertEquals(expect,result);
	}
	
	@Test(expected=IllegalArgumentException.class) @Transactional
	public void testDelete(){
		String unsName = "UNS-K2-11";
		ImportDutyTransactionImpl dutyTran = new ImportDutyTransactionImpl(unsName,"2015-11-17",null,null,null,null,new ArrayList<ImportDutyTransactionLineItem>());
		dutyTran.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K488-2","K488-PLUG100",   null,"322.99"));
		dutyTran.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70056-01",null, "11.17"));
		dutyTran.getLineItems().add(new ImportDutyTransactionLineItemImpl("IVS-K489-3","K489-RW70057-01",null, "22.99"));
		this.uco.create(dutyTran);
		this.uco.delete(unsName);
		this.uco.get(unsName);
	}
}
