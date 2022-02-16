package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.usecase.MaintainRemittanceUco;
import com.kindminds.drs.api.v1.model.accounting.Remittance;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.impl.RemittanceImpl;
import com.kindminds.drs.impl.RemittanceSearchConditionImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
public class TestMaintainRemittanceUco {
	
	@Autowired private MaintainRemittanceUco uco;
	
	@Test @Transactional
	public void testRetrieveListNormal() {
		int id = this.uco.create(new RemittanceImpl(0,"2014-11-17", "2014-12-01", "K2", "K3", Currency.TWD, "1000",null));
		Remittance expected = new RemittanceImpl(id,"2014-11-17", "2014-12-01", "K2", "K3", Currency.TWD, "1000",null);
		DtoList<Remittance> list = uco.retrieveList(new RemittanceSearchConditionImpl(null,null),1);
		assertTrue( list.getItems().contains(expected));
	}
	
	@Test @Transactional
	public void testRetrieveListWithReference() {
		int id = this.uco.create(new RemittanceImpl(0,"2014-11-17","2014-12-01","K2","K3",Currency.TWD,"1000","REF"));
		Remittance expected = new RemittanceImpl(id,"2014-11-17", "2014-12-01", "K2", "K3", Currency.TWD, "1000","REF");
		DtoList<Remittance> list = uco.retrieveList(new RemittanceSearchConditionImpl(null,null),1);
		assertTrue( list.getItems().contains(expected));
	}
	
	@Test @Transactional
	public void testRetrieveListWithCondition() {
		DtoList<Remittance> list = this.uco.retrieveList(new RemittanceSearchConditionImpl("K2","K486"),1);
		for(Remittance remittance:list.getItems()){
			Assert.assertEquals("K2", remittance.getSender());
			Assert.assertEquals("K486", remittance.getReceiver());
		}
	}
	
	@Test @Transactional
	public void testInsert() {
		int id = this.uco.create(new RemittanceImpl(0,"2014-11-17","2014-12-01","K2","K3",Currency.TWD, "1000",null));
		Remittance expected = new RemittanceImpl(id,"2014-11-17","2014-12-01","K2","K3",Currency.TWD,"1000",null);
		Remittance result = this.uco.retrieve(id);
		Assert.assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testInsertWithNullRcvdDate() {
		int id = this.uco.create(new RemittanceImpl(0,"2014-11-17", null, "K2", "K3", Currency.TWD, "1000",null));
		Remittance expected = new RemittanceImpl(id,"2014-11-17",null, "K2", "K3", Currency.TWD, "1000",null);
		Remittance result=uco.retrieve(id);
		Assert.assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testInsertWithNullSendDate() {
		int id = this.uco.create(new RemittanceImpl(0, null,"2014-11-17", "K2", "K3", Currency.TWD, "1000",null));
		Remittance expected = new RemittanceImpl(id,null,"2014-11-17", "K2", "K3", Currency.TWD, "1000",null);
		Remittance result=uco.retrieve(id);
		Assert.assertEquals(expected,result);
	}
	
	@Test @Transactional
	public void testInsertWithReference() {
		int id = this.uco.create(new RemittanceImpl(0,"2014-11-17","2014-12-01","K2","K3",Currency.TWD,"1000","REF"));
		Remittance expected = new RemittanceImpl(id,"2014-11-17","2014-12-01","K2","K3",Currency.TWD,"1000","REF");
		Remittance result=uco.retrieve(id);
		Assert.assertEquals(expected,result);
	}
	
	@Test @Transactional 
	public void testUpdate() {
		int id = this.uco.create(new RemittanceImpl(0,"2020-11-17","2014-12-01","K2","K3",Currency.TWD,"1000",null));
		Remittance expect = new RemittanceImpl(id,"2014-11-30","2014-11-30","K2","K3",Currency.TWD,"1000",null);
		this.uco.update(expect);
		Remittance result = uco.retrieve(id);
		Assert.assertEquals(expect,result);
	}
	
	@Test @Transactional 
	public void testUpdateWithNullRcvdDate() {
		int id = this.uco.create(new RemittanceImpl(0,"2020-11-17", "2014-12-01", "K2", "K3", Currency.TWD, "1000",null));
		Remittance expect = new RemittanceImpl(id,"2014-11-30",null,"K2","K3",Currency.TWD,"1000",null);
		this.uco.update(expect);
		Remittance result = uco.retrieve(id);
		Assert.assertEquals(expect,result);
	}
	
	@Test @Transactional 
	public void testUpdateReference() {
		int id = this.uco.create(new RemittanceImpl(0,"2020-11-17", "2014-12-01", "K2", "K3", Currency.TWD, "1000",null));
		Remittance expect = new RemittanceImpl(id, "2014-11-30", null, "K2", "K3", Currency.TWD, "1000","REF");
		this.uco.update(expect);
		Remittance result = uco.retrieve(id);
		Assert.assertEquals(expect,result);
	}
	
	@Test @Transactional 
	public void testUpdateSettled() {
		Remittance toUpdate1 = new RemittanceImpl(1,"2014-12-01", null, "K2", "K3", Currency.TWD, "1000.000000","REF");
		Remittance toUpdate2 = new RemittanceImpl(2,"2014-12-01", null, "K2", "K3", Currency.TWD, "1000.000000","REF");
		Remittance toUpdate3 = new RemittanceImpl(3,"2014-12-01", null, "K2", "K3", Currency.TWD, "1000.000000","REF");
		Remittance toUpdate4 = new RemittanceImpl(4,"2014-12-01", null, "K2", "K3", Currency.TWD, "1000.000000","REF");
		Remittance toUpdate5 = new RemittanceImpl(5,"2014-12-01", null, "K2", "K3", Currency.TWD, "1000.000000","REF");
		
		Assert.assertEquals(0, this.uco.update(toUpdate1));
		Assert.assertEquals(0, this.uco.update(toUpdate2));
		Assert.assertEquals(0, this.uco.update(toUpdate3));
		Assert.assertEquals(0, this.uco.update(toUpdate4));
		Assert.assertEquals(0, this.uco.update(toUpdate5));
	}
	
	@Test @Transactional
	public void testDelete() {
		int id = this.uco.create(new RemittanceImpl(0,"2020-11-17", "2014-12-01", "K2", "K3", Currency.TWD, "1000",null));
		uco.delete(id);
		Remittance result = uco.retrieve(id);
		Assert.assertEquals(null,result);
	}

}
