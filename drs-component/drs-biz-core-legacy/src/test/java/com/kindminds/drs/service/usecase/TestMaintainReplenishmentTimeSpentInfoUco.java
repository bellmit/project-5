package com.kindminds.drs.service.usecase;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.usecase.logistics.MaintainReplenishmentTimeSpentInfoUco;

import com.kindminds.drs.impl.ReplenishmentTimeSpentInfoImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestMaintainReplenishmentTimeSpentInfoUco {
	
	@Autowired private MaintainReplenishmentTimeSpentInfoUco uco;
	
	@Test @Transactional
	public void testSave(){
		List<ReplenishmentTimeSpentInfo> expected = new ArrayList<>();
		expected.add(new ReplenishmentTimeSpentInfoImpl(101,"FBA US",100, 28, 6, 14, 60));
		expected.add(new ReplenishmentTimeSpentInfoImpl(102,"FBA UK",  7,100, 6, 14,100));
		expected.add(new ReplenishmentTimeSpentInfoImpl(103,"FBA CA",  7, 28, 7,100, 65));
		this.uco.save(expected);
		Assert.assertEquals(expected,this.uco.getList());
	}
}
