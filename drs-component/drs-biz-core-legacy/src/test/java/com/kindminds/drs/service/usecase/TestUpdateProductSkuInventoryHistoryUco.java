package com.kindminds.drs.service.usecase;

import com.kindminds.drs.api.usecase.inventory.UpdateProductSkuInventoryHistoryUco;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContextLocal.xml" })
public class TestUpdateProductSkuInventoryHistoryUco {
	
	@Autowired private UpdateProductSkuInventoryHistoryUco uco;
	
	@Test
	public void testUpdate(){
		uco.update(2020, 12, 4);

	}


}