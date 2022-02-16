package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.util.List;

public class AmazonFinanceEvent {
    private List<AmazonShipmentEvent> shipmentEventList;
    private List<AmazonShipmentSettleEvent> shipmentSettleEventList;
    private List<AmazonShipmentEvent> refundEventList;
    private List<AmazonShipmentEvent> guaranteeClaimEventList;
    private List<AmazonShipmentEvent> chargebackEventList;
    private List<AmazonPayWithAmazonEvent> payWithAmazonEventList;
    private List<AmazonSolutionProviderCreditEvent> serviceProviderCreditEventList;
    private List<AmazonRetrochargeEvent> retrochargeEventList;
    private List<AmazonRentalTransactionEvent> rentalTransactionEventList;
    private List<AmazonPerformanceBondRefundEvent> performanceBondRefundEventList;
    private List<AmazonProductAdsPaymentEvent> productAdsPaymentEventList;
    private List<AmazonServiceFeeEvent> serviceFeeEventList;
    private List<AmazonSellerDealPaymentEvent> sellerDealPaymentEventList;
    private List<AmazonDebtRecoveryEvent> debtRecoveryEventList;
    private List<AmazonLoanServicingEvent> loanServicingEventList;
    private List<AmazonAdjustmentEvent> adjustmentEventList;
    private List<AmazonSAFETReimbursementEvent> safetReimbursementEventList;
    private List<AmazonSellerReviewEnrollmentPaymentEvent> sellerReviewEnrollmentPaymentEventList;
    private List<AmazonFBALiquidationEvent> fbaLiquidationEventList;
    private List<AmazonCouponPaymentEvent> couponPaymentEventList;
    private List<AmazonImagingServicesFeeEvent> imagingServicesFeeEventList;
    private List<AmazonNetworkComminglingTransactionEvent> networkComminglingTransactionEventList;
    private List<AmazonAffordabilityExpenseEvent> affordabilityExpenseEventList;
    private List<AmazonAffordabilityExpenseEvent> affordabilityExpenseReversalEventList;
    private List<AmazonRemovalShipmentEvent> removalShipmentEventList;
    private List<AmazonTrialShipmentEvent> trialShipmentEventList;
    private List<AmazonTDSReimbursementEvent> tdsReimbursementEventList;
    private List<AmazonTaxWithholdingEvent> taxWithholdingEventList;


    public List<AmazonShipmentEvent> getShipmentEventList() {
        return shipmentEventList;
    }

    public void setShipmentEventList(List<AmazonShipmentEvent> shipmentEventList) {
        this.shipmentEventList = shipmentEventList;
    }

    public List<AmazonShipmentSettleEvent> getShipmentSettleEventList() {
        return shipmentSettleEventList;
    }

    public void setShipmentSettleEventList(List<AmazonShipmentSettleEvent> shipmentSettleEventList) {
        this.shipmentSettleEventList = shipmentSettleEventList;
    }

    public List<AmazonShipmentEvent> getRefundEventList() {
        return refundEventList;
    }

    public void setRefundEventList(List<AmazonShipmentEvent> refundEventList) {
        this.refundEventList = refundEventList;
    }

    public List<AmazonShipmentEvent> getGuaranteeClaimEventList() {
        return guaranteeClaimEventList;
    }

    public void setGuaranteeClaimEventList(List<AmazonShipmentEvent> guaranteeClaimEventList) {
        this.guaranteeClaimEventList = guaranteeClaimEventList;
    }

    public List<AmazonShipmentEvent> getChargebackEventList() {
        return chargebackEventList;
    }

    public void setChargebackEventList(List<AmazonShipmentEvent> chargebackEventList) {
        this.chargebackEventList = chargebackEventList;
    }

    public List<AmazonPayWithAmazonEvent> getPayWithAmazonEventList() {
        return payWithAmazonEventList;
    }

    public void setPayWithAmazonEventList(List<AmazonPayWithAmazonEvent> payWithAmazonEventList) {
        this.payWithAmazonEventList = payWithAmazonEventList;
    }

    public List<AmazonSolutionProviderCreditEvent> getServiceProviderCreditEventList() {
        return serviceProviderCreditEventList;
    }

    public void setServiceProviderCreditEventList(List<AmazonSolutionProviderCreditEvent> serviceProviderCreditEventList) {
        this.serviceProviderCreditEventList = serviceProviderCreditEventList;
    }

    public List<AmazonRetrochargeEvent> getRetrochargeEventList() {
        return retrochargeEventList;
    }

    public void setRetrochargeEventList(List<AmazonRetrochargeEvent> retrochargeEventList) {
        this.retrochargeEventList = retrochargeEventList;
    }

    public List<AmazonRentalTransactionEvent> getRentalTransactionEventList() {
        return rentalTransactionEventList;
    }

    public void setRentalTransactionEventList(List<AmazonRentalTransactionEvent> rentalTransactionEventList) {
        this.rentalTransactionEventList = rentalTransactionEventList;
    }

    public List<AmazonPerformanceBondRefundEvent> getPerformanceBondRefundEventList() {
        return performanceBondRefundEventList;
    }

    public void setPerformanceBondRefundEventList(List<AmazonPerformanceBondRefundEvent> performanceBondRefundEventList) {
        this.performanceBondRefundEventList = performanceBondRefundEventList;
    }

    public List<AmazonProductAdsPaymentEvent> getProductAdsPaymentEventList() {
        return productAdsPaymentEventList;
    }

    public void setProductAdsPaymentEventList(List<AmazonProductAdsPaymentEvent> productAdsPaymentEventList) {
        this.productAdsPaymentEventList = productAdsPaymentEventList;
    }

    public List<AmazonServiceFeeEvent> getServiceFeeEventList() {
        return serviceFeeEventList;
    }

    public void setServiceFeeEventList(List<AmazonServiceFeeEvent> serviceFeeEventList) {
        this.serviceFeeEventList = serviceFeeEventList;
    }

    public List<AmazonSellerDealPaymentEvent> getSellerDealPaymentEventList() {
        return sellerDealPaymentEventList;
    }

    public void setSellerDealPaymentEventList(List<AmazonSellerDealPaymentEvent> sellerDealPaymentEventList) {
        this.sellerDealPaymentEventList = sellerDealPaymentEventList;
    }

    public List<AmazonDebtRecoveryEvent> getDebtRecoveryEventList() {
        return debtRecoveryEventList;
    }

    public void setDebtRecoveryEventList(List<AmazonDebtRecoveryEvent> debtRecoveryEventList) {
        this.debtRecoveryEventList = debtRecoveryEventList;
    }

    public List<AmazonLoanServicingEvent> getLoanServicingEventList() {
        return loanServicingEventList;
    }

    public void setLoanServicingEventList(List<AmazonLoanServicingEvent> loanServicingEventList) {
        this.loanServicingEventList = loanServicingEventList;
    }

    public List<AmazonAdjustmentEvent> getAdjustmentEventList() {
        return adjustmentEventList;
    }

    public void setAdjustmentEventList(List<AmazonAdjustmentEvent> adjustmentEventList) {
        this.adjustmentEventList = adjustmentEventList;
    }

    public List<AmazonSAFETReimbursementEvent> getSafetReimbursementEventList() {
        return safetReimbursementEventList;
    }

    public void setSafetReimbursementEventList(List<AmazonSAFETReimbursementEvent> safetReimbursementEventList) {
        this.safetReimbursementEventList = safetReimbursementEventList;
    }

    public List<AmazonSellerReviewEnrollmentPaymentEvent> getSellerReviewEnrollmentPaymentEventList() {
        return sellerReviewEnrollmentPaymentEventList;
    }

    public void setSellerReviewEnrollmentPaymentEventList(List<AmazonSellerReviewEnrollmentPaymentEvent> sellerReviewEnrollmentPaymentEventList) {
        this.sellerReviewEnrollmentPaymentEventList = sellerReviewEnrollmentPaymentEventList;
    }

    public List<AmazonFBALiquidationEvent> getFbaLiquidationEventList() {
        return fbaLiquidationEventList;
    }

    public void setFbaLiquidationEventList(List<AmazonFBALiquidationEvent> fbaLiquidationEventList) {
        this.fbaLiquidationEventList = fbaLiquidationEventList;
    }

    public List<AmazonCouponPaymentEvent> getCouponPaymentEventList() {
        return couponPaymentEventList;
    }

    public void setCouponPaymentEventList(List<AmazonCouponPaymentEvent> couponPaymentEventList) {
        this.couponPaymentEventList = couponPaymentEventList;
    }

    public List<AmazonImagingServicesFeeEvent> getImagingServicesFeeEventList() {
        return imagingServicesFeeEventList;
    }

    public void setImagingServicesFeeEventList(List<AmazonImagingServicesFeeEvent> imagingServicesFeeEventList) {
        this.imagingServicesFeeEventList = imagingServicesFeeEventList;
    }

    public List<AmazonNetworkComminglingTransactionEvent> getNetworkComminglingTransactionEventList() {
        return networkComminglingTransactionEventList;
    }

    public void setNetworkComminglingTransactionEventList(List<AmazonNetworkComminglingTransactionEvent> networkComminglingTransactionEventList) {
        this.networkComminglingTransactionEventList = networkComminglingTransactionEventList;
    }

    public List<AmazonAffordabilityExpenseEvent> getAffordabilityExpenseEventList() {
        return affordabilityExpenseEventList;
    }

    public void setAffordabilityExpenseEventList(List<AmazonAffordabilityExpenseEvent> affordabilityExpenseEventList) {
        this.affordabilityExpenseEventList = affordabilityExpenseEventList;
    }

    public List<AmazonAffordabilityExpenseEvent> getAffordabilityExpenseReversalEventList() {
        return affordabilityExpenseReversalEventList;
    }

    public void setAffordabilityExpenseReversalEventList(List<AmazonAffordabilityExpenseEvent> affordabilityExpenseReversalEventList) {
        this.affordabilityExpenseReversalEventList = affordabilityExpenseReversalEventList;
    }

    public List<AmazonRemovalShipmentEvent> getRemovalShipmentEventList() {
        return removalShipmentEventList;
    }

    public void setRemovalShipmentEventList(List<AmazonRemovalShipmentEvent> removalShipmentEventList) {
        this.removalShipmentEventList = removalShipmentEventList;
    }

    public List<AmazonTrialShipmentEvent> getTrialShipmentEventList() {
        return trialShipmentEventList;
    }

    public void setTrialShipmentEventList(List<AmazonTrialShipmentEvent> trialShipmentEventList) {
        this.trialShipmentEventList = trialShipmentEventList;
    }

    public List<AmazonTDSReimbursementEvent> getTdsReimbursementEventList() {
        return tdsReimbursementEventList;
    }

    public void setTdsReimbursementEventList(List<AmazonTDSReimbursementEvent> tdsReimbursementEventList) {
        this.tdsReimbursementEventList = tdsReimbursementEventList;
    }

    public List<AmazonTaxWithholdingEvent> getTaxWithholdingEventList() {
        return taxWithholdingEventList;
    }

    public void setTaxWithholdingEventList(List<AmazonTaxWithholdingEvent> taxWithholdingEventList) {
        this.taxWithholdingEventList = taxWithholdingEventList;
    }
}
