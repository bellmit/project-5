package com.kindminds.drs.api.data.access.rdb.amazon;

import com.kindminds.drs.api.v2.biz.domain.model.finance.AmazonFinanceEvent;

public interface AmazonFinancialEventDao {

    public void addShipmentEventList(AmazonFinanceEvent fe);
    public void addRefundEventList(AmazonFinanceEvent fe);
    public void addGuaranteeClaimEventList(AmazonFinanceEvent fe);
    public void addChargebackEventList(AmazonFinanceEvent fe);
    public void addPayWithAmazonEventList(AmazonFinanceEvent fe);
    public void addServiceProviderCreditEventList(AmazonFinanceEvent fe);
    public void addRetrochargeEventList(AmazonFinanceEvent fe);
    public void addRentalTransactionEventList(AmazonFinanceEvent fe);
    public void addPerformanceBondRefundEventList(AmazonFinanceEvent fe);
    public void addProductAdsPaymentEventList(AmazonFinanceEvent fe);
    public void addServiceFeeEventList(AmazonFinanceEvent fe);
    public void addSellerDealPaymentEventList(AmazonFinanceEvent fe);
    public void addDebtRecoveryEventList(AmazonFinanceEvent fe);
    public void addLoanServicingEventList(AmazonFinanceEvent fe);
    public void addAdjustmentEventList(AmazonFinanceEvent fe);
    public void addSafetReimbursementEventList(AmazonFinanceEvent fe);
    public void addSellerReviewEnrollmentPaymentEventList(AmazonFinanceEvent fe);
    public void addFbaLiquidationEventList(AmazonFinanceEvent fe);
    public void addCouponPaymentEventList(AmazonFinanceEvent fe);
    public void addImagingServicesFeeEventList(AmazonFinanceEvent fe);
    public void addNetworkComminglingTransactionEventList(AmazonFinanceEvent fe);
    public void addAffordabilityExpenseEventList(AmazonFinanceEvent fe);
    public void addAffordabilityExpenseReversalEventList(AmazonFinanceEvent fe);
    public void addRemovalShipmentEventList(AmazonFinanceEvent fe);
    public void addTrialShipmentEventList(AmazonFinanceEvent fe);
    public void addTdsReimbursementEventList(AmazonFinanceEvent fe);
    public void addTaxWithholdingEventList(AmazonFinanceEvent fe);


}
