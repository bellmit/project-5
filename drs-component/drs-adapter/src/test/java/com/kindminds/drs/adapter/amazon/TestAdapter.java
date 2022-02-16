package com.kindminds.drs.adapter.amazon;

import com.kindminds.drs.adapter.amazon.temp.AmazonMwsProductAdapterImpl;
import com.kindminds.drs.api.data.access.rdb.amazon.AmazonFinancialEventDao;

import com.kindminds.drs.api.v2.biz.domain.model.finance.AmazonFinanceEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:META-INF/spring/app-context.xml" })
@ContextConfiguration(locations = { "classpath:META-INF/spring/base-app-context4Test.xml" })
public class TestAdapter {

	@Autowired private AmazonFinancialEventDao dao;

	@Test 
	public void test() throws Exception{
		AmazonMwsProductAdapterImpl adapterImpl = new AmazonMwsProductAdapterImpl();
		adapterImpl.requestOrders();

	}

	@Test
	public void test1() throws Exception{
		AmazonMwsReportAdapterImpl adapterImpl = new AmazonMwsReportAdapterImpl();
		//adapterImpl.requestOrders(null);

	}

	@Test
	public void test2() throws Exception{
		AmazonMwsReportAdapterImpl adapterImpl = new AmazonMwsReportAdapterImpl();
		//adapterImpl.requestOrders2();

	}

	@Test
	public void testFinances() throws Exception{
		AmazonMwsFinancesAdapterImpl adapterImpl = new AmazonMwsFinancesAdapterImpl();
		//adapterImpl.request();
		AmazonFinanceEvent afe= adapterImpl.getFinanceEvent();
		//AmazonFinanceEvent afe= adapterImpl.testget();

		this.dao.addShipmentEventList(afe);
		this.dao.addRefundEventList(afe);
		this.dao.addGuaranteeClaimEventList(afe);
		this.dao.addChargebackEventList(afe);
		this.dao.addPayWithAmazonEventList(afe);
		this.dao.addServiceProviderCreditEventList(afe);
		this.dao.addRetrochargeEventList(afe);
		this.dao.addRentalTransactionEventList(afe);
		this.dao.addPerformanceBondRefundEventList(afe);
		this.dao.addProductAdsPaymentEventList(afe);
		this.dao.addServiceFeeEventList(afe);
		this.dao.addSellerDealPaymentEventList(afe);
		this.dao.addDebtRecoveryEventList(afe);
		this.dao.addAdjustmentEventList(afe);
		this.dao.addSafetReimbursementEventList(afe);
		this.dao.addSellerReviewEnrollmentPaymentEventList(afe);
		this.dao.addFbaLiquidationEventList(afe);
		this.dao.addCouponPaymentEventList(afe);
		this.dao.addImagingServicesFeeEventList(afe);
		this.dao.addNetworkComminglingTransactionEventList(afe);
		this.dao.addAffordabilityExpenseEventList(afe);
		this.dao.addAffordabilityExpenseReversalEventList(afe);
		this.dao.addRemovalShipmentEventList(afe);
		this.dao.addTrialShipmentEventList(afe);
		this.dao.addTdsReimbursementEventList(afe);
		this.dao.addTaxWithholdingEventList(afe);



	}
	
}
