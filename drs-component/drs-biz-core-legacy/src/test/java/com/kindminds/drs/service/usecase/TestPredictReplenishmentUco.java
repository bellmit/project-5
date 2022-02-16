package com.kindminds.drs.service.usecase;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;
import com.kindminds.drs.service.security.MockAuth;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.usecase.logistics.PredictReplenishmentUco;
import com.kindminds.drs.api.data.access.usecase.logistics.PredictReplenishmentDao;

import com.kindminds.drs.api.v1.model.replenishment.SkuInventoryInfo;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.SkuReplenishmentPredition;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.Supplier;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.v1.model.impl.replenishment.ReplenishmentTimeSpentImpl;
import com.kindminds.drs.v1.model.impl.replenishment.SkuInventoryImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestPredictReplenishmentUco {
	@Spy
	private PredictReplenishmentDao dao;
	
	@Mock
	private CompanyDao cRepo;
	
	@Autowired
    @InjectMocks
    private PredictReplenishmentUco service;
	
	@Autowired private AuthenticationManager authenticationManager;
	
	@Before
	public void init() {
		dao = Mockito.mock(PredictReplenishmentDao.class);
		cRepo = Mockito.mock(CompanyDao.class);
		//MockitoAnnotations.initMocks(this);
	}

	@Test @Ignore
	public void testCalculateByQuantityByDrsUser() {
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		Mockito.when(dao.getSkuInventoryList(101, null, 28, null)).thenReturn(initialInventoryData());
		Mockito.when(dao.getReplenishmentTimeSpent(101)).thenReturn(getAmazonTimeSpent());
		Map<Supplier, List<SkuReplenishmentPredition>> dtos = service.calculate(Warehouse.FBA_US, 28);
		assertNotNull(dtos);
	}
	
	@SuppressWarnings("deprecation")
	private List<SkuInventoryInfo> initialInventoryData() {
		List<SkuInventoryInfo> list = new ArrayList<SkuInventoryInfo>();
		list.add(new SkuInventoryImpl("K151-3A", "K151", "Hsumao", 30, 85, 100, new Date(117, 0, 20),350, new Date(117, 0, 20),new Date(117, 0, 20), 854,10));
		list.add(new SkuInventoryImpl("K151-3B", "K151", "Hsumao", 20, 383, 150,new Date(117, 0, 22),400, new Date(117, 0, 22),new Date(117, 0, 22), 114,10));
		list.add(new SkuInventoryImpl("K151-3C", "K151", "Hsumao", 15, 0, 120, new Date(117, 0, 22),300, new Date(117, 0, 22),new Date(117, 0, 19), 337,10));
		list.add(new SkuInventoryImpl("K151-3D", "K151", "Hsumao", 30, 150, null,null,null, null,null, 337,10));
		list.add(new SkuInventoryImpl("K486-3A", "K486", "Hanchor", 30, 85, 100, new Date(117, 0, 20),350, new Date(117, 0, 20),new Date(117, 0, 20), 854,10));
		list.add(new SkuInventoryImpl("K486-3B", "K486", "Hanchor", 30, 383, 200, new Date(117, 0, 25),400, new Date(117, 0, 25),new Date(117, 0, 25), 198,7));
		list.add(new SkuInventoryImpl("K486-3C", "K486", "Hanchor", 30, 0, 150, new Date(117, 0, 25),300, new Date(117, 0, 25),new Date(117, 1, 1), 60,7));
		return list;
	}
	
	private ReplenishmentTimeSpent getAmazonTimeSpent() {
		return new ReplenishmentTimeSpentImpl(7, 28, 6, 14, 60);
	}
	
	@SuppressWarnings("deprecation")
	private List<SkuInventoryInfo> initialInventoryData2() {
		List<SkuInventoryInfo> list = new ArrayList<SkuInventoryInfo>();
		list.add(new SkuInventoryImpl("K151-3C", "K151", "Hsumao", 15, 0, 150, new Date(117, 0, 19),300, new Date(117, 0, 19),new Date(117, 0, 19), 337,10));
		return list;
	}
	
	@Test @Ignore
	public void testCalculateByQuantity() {
		MockAuth.login(authenticationManager, "ted.hwang@tw.drs.network", "NsFp6Ax5");
		Mockito.when(dao.getSkuInventoryList(101, null, 28, "K151-DH-500CP")).thenReturn(initialInventoryData2());
		Mockito.when(dao.getReplenishmentTimeSpent(101)).thenReturn(getAmazonTimeSpent());
		SkuReplenishmentPredition result = service.calculateManually("K151-DH-500CP", Warehouse.FBA_US, 28, new BigDecimal(60),35);
		assertNotNull(result);
	}
	
	@Test @Ignore
	public void testInitial() {
		service.initial();		
	}

}
