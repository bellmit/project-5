package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kindminds.drs.api.v1.model.SupplementalLink;
import com.kindminds.drs.service.security.MockAuth;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import com.kindminds.drs.api.usecase.MaintainSupplementalLinkUco;

import com.kindminds.drs.impl.SupplementalLinkImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainSupplementalLinkUco {
	
	@Autowired private MaintainSupplementalLinkUco uco;
	@Autowired private AuthenticationManager authenticationManager;
	
	@Test @Transactional
	public void testGetListForDrsStaff(){
		int id1 = this.uco.save(new SupplementalLinkImpl(0,"K408","Name","www.drs.network","Home Page"));
		int id2 = this.uco.save(new SupplementalLinkImpl(0,"K448","Name","www.drs.network","Home Page"));
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		List<SupplementalLink> linkList = this.uco.getList();
		MockAuth.logout();
		assertTrue(this.isIdInList(linkList,id1));
		assertTrue(this.isIdInList(linkList,id2));
	}
	
	@Test @Transactional
	public void testGetListForSupplier(){
		int id1 = this.uco.save(new SupplementalLinkImpl(0,"K408","Name","www.drs.network","Home Page"));
		int id2 = this.uco.save(new SupplementalLinkImpl(0,"K448","Name","www.drs.network","Home Page"));
		MockAuth.login(authenticationManager, "K448.test", "12345");
		List<SupplementalLink> linkList = this.uco.getList();
		MockAuth.logout();
		assertTrue(!this.isIdInList(linkList,id1));
		assertTrue(this.isIdInList(linkList,id2));
	}
	
	@Test @Transactional
	public void testGetListOfSupplier(){
		int id1 = this.uco.save(new SupplementalLinkImpl(0,"K408","Name","www.drs.network","Home Page"));
		int id2 = this.uco.save(new SupplementalLinkImpl(0,"K448","Name","www.drs.network","Home Page"));
		List<SupplementalLink> linkList1 = this.uco.getList("K408");
		List<SupplementalLink> linkList2 = this.uco.getList("K448");
		assertTrue(this.isIdInList(linkList1,id1));
		assertTrue(!this.isIdInList(linkList2,id1));
		assertTrue(!this.isIdInList(linkList1,id2));
		assertTrue(this.isIdInList(linkList2,id2));
	}
	
	@Test @Transactional
	public void testInsert(){
		SupplementalLinkImpl link = new SupplementalLinkImpl(0,"K486","Name","www.drs.network","Home Page");
		int id = this.uco.save(link);
		assertEquals(link,this.uco.get(id));
	}
	
	@Test @Transactional
	public void testInsertNullValues(){
		SupplementalLinkImpl link = new SupplementalLinkImpl(0,null,"Name","www.drs.network",null);
		int id = this.uco.save(link);
		assertEquals(link,this.uco.get(id));
	}
	
	@Test @Transactional
	public void testUpdate(){
		SupplementalLinkImpl link = new SupplementalLinkImpl(0,"K486","Name","www.drs.network","Home Page");
		int id = this.uco.save(link);
		SupplementalLinkImpl linkToUpdate = new SupplementalLinkImpl(id,"K486","New Name","tw.drs.network","Home Page Home Page");
		assertEquals(linkToUpdate,this.uco.update(linkToUpdate));
	}
	
	@Test @Transactional
	public void testDeleteManyLinks(){
		SupplementalLinkImpl link1 = new SupplementalLinkImpl(0,"K486","Name","www.drs.network","Home Page");
		SupplementalLinkImpl link2 = new SupplementalLinkImpl(0,"K488","Name","www.drs.network","Home Page");
		int id1 = this.uco.save(link1);
		int id2 = this.uco.save(link2);
		List<Integer> idsToDelete = Arrays.asList(id1,id2);
		this.uco.delete(idsToDelete);
		Assert.assertEquals(null,this.uco.get(id1));
		Assert.assertEquals(null,this.uco.get(id2));
	}
	
	@Test @Transactional
	public void testDeleteNoLinks(){
		SupplementalLinkImpl link1 = new SupplementalLinkImpl(0,"K486","Name","www.drs.network","Home Page");
		SupplementalLinkImpl link2 = new SupplementalLinkImpl(0,"K488","Name","www.drs.network","Home Page");
		int id1 = this.uco.save(link1);
		int id2 = this.uco.save(link2);
		List<Integer> idsToDelete = new ArrayList<Integer>();
		this.uco.delete(idsToDelete);
		this.uco.delete(null);
		assertEquals(link1,this.uco.get(id1));
		assertEquals(link2,this.uco.get(id2));
	}
	
	private boolean isIdInList(List<SupplementalLink> list, int id){
		for(SupplementalLink link:list){
			if(link.getId().equals(id)) return true;
		}
		return false;
	}
	
}
