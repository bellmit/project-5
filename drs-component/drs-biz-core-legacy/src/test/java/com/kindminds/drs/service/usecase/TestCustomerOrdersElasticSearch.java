package com.kindminds.drs.service.usecase;

import com.kindminds.drs.api.usecase.sales.ListCustomerOrderUco;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.sales.CustomerOrder;

import com.kindminds.drs.service.security.MockAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/TestContext.xml" })
public class TestCustomerOrdersElasticSearch {

	@Autowired
	private ListCustomerOrderUco uco;

	@Autowired
	private AuthenticationManager authenticationManager;
	
    @Test
    public void testElasticMultiMatchSearch() {
		MockAuth.login(authenticationManager, "robert.lee@drs.network", "DrIprl1Oc7a");

		DtoList<CustomerOrder> list =
				uco.getListElastic("K408", 1, null, null);
		System.out.println(list.getTotalRecords());
		for (CustomerOrder order : list.getItems()) {
			System.out.println("*********************************");
			System.out.println(order);
		}

		MockAuth.logout();
    }
	
}
