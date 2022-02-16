package com.kindminds.drs.persist.data.access.rdb.amazon;

import com.kindminds.drs.api.data.access.rdb.amazon.AmazonFinancialEventDao;
import com.kindminds.drs.api.v2.biz.domain.model.finance.AmazonFinanceEvent;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.ZoneId;

@Repository
public class AmazonFinancialEventDaoImpl extends Dao implements AmazonFinancialEventDao {

    @Override
    //@Transactional("transactionManager")
    public void addShipmentEventList(AmazonFinanceEvent fe) {
        fe.getShipmentEventList().forEach(x->{

        String sql = "INSERT INTO finance.amazon_shipment_event "
                    +" (amazon_order_id, seller_order_id, marketplace_name, posted_date) "
                    +" VALUES ( :amazonOrderId , :sellerOrderId , :marketplaceName, :postedDate) ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("amazonOrderId",x.getAmazonOrderId());
        q.addValue("sellerOrderId",x.getSellerOrderId());
        q.addValue("marketplaceName",x.getMarketplaceName());
        q.addValue("postedDate",x.getPostedDate());
        Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });


    }

    @Override
    public void addRefundEventList(AmazonFinanceEvent fe) {
        fe.getRefundEventList().forEach(x->{

            String sql = "INSERT INTO finance.amazon_shipment_event "
                    +" (amazon_order_id, seller_order_id, marketplace_name, event_type,posted_date) "
                    +" VALUES ( :amazonOrderId , :sellerOrderId , :marketplaceName, 'Refund', :postedDate) ";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("amazonOrderId",x.getAmazonOrderId());
            q.addValue("sellerOrderId",x.getSellerOrderId());
            q.addValue("marketplaceName",x.getMarketplaceName());
            q.addValue("postedDate",x.getPostedDate());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });

    }

    @Override
    public void addGuaranteeClaimEventList(AmazonFinanceEvent fe) {
        fe.getGuaranteeClaimEventList().forEach(x->{

            String sql = "INSERT INTO finance.amazon_shipment_event "
                    +" (amazon_order_id, seller_order_id, marketplace_name, event_type, posted_date) "
                    +" VALUES ( :amazonOrderId , :sellerOrderId , :marketplaceName, 'Guarantee Claim', :postedDate) ";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("amazonOrderId",x.getAmazonOrderId());
            q.addValue("sellerOrderId",x.getSellerOrderId());
            q.addValue("marketplaceName",x.getMarketplaceName());
            q.addValue("postedDate",x.getPostedDate());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });

    }

    @Override
    public void addChargebackEventList(AmazonFinanceEvent fe) {
        fe.getChargebackEventList().forEach(x->{

            String sql = "INSERT INTO finance.amazon_shipment_event "
                    +" (amazon_order_id, seller_order_id, marketplace_name, event_type, posted_date) "
                    +" VALUES ( :amazonOrderId , :sellerOrderId , :marketplaceName, 'Chargeback', :postedDate) ";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("amazonOrderId",x.getAmazonOrderId());
            q.addValue("sellerOrderId",x.getSellerOrderId());
            q.addValue("marketplaceName",x.getMarketplaceName());
            q.addValue("postedDate",x.getPostedDate());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });

    }

    @Override
    public void addPayWithAmazonEventList(AmazonFinanceEvent fe) {
        fe.getPayWithAmazonEventList().forEach(x->{

            String sql="INSERT INTO finance.amazon_pay_with_amazon_event "
                    +" (seller_order_id, transaction_posted_date, business_object_type, sales_channel, "
                    +" payment_amount_type, amount_description, fulfillment_channel, store_name) "
                    +" VALUES ( :sellerOrderId, :transactionPostedDate, :businessObjectType, :salesChannel,"
                    +" :paymentAmountType, :amountDescription, :fulfillmentChannel, :storeName)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("sellerOrderId",x.getSellerOrderId());
            q.addValue("transactionPostedDate",x.getTransactionPostedDate());
            q.addValue("businessObjectType",x.getBusinessObjectType());
            q.addValue("salesChannel",x.getSalesChannel());
            q.addValue("paymentAmountType",x.getPaymentAmountType());
            q.addValue("amountDescription",x.getAmountDescription());
            q.addValue("fulfillmentChannel",x.getFulfillmentChannel());
            q.addValue("storeName",x.getStoreName());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });

    }

    @Override
    public void addServiceProviderCreditEventList(AmazonFinanceEvent fe) {
        fe.getServiceProviderCreditEventList().forEach(x -> {

            String sql ="INSERT INTO finance.amazon_solution_provider_credit_event "
                    +" (provider_transaction_type, seller_order_id, marketplace_id, marketplace_country, "
                    +" seller_id, seller_store_name, provider_id, provider_store_name, transaction_currency, "
                    +" transaction_amount, transaction_creation_date) "
                    +" VALUES (:providerTransactionType, :sellerOrderId, :marketplaceId, :marketplaceCountry, "
                    +" :sellerId, :sellerStoreName, :providerId, :providerStoreName, "
                    +" :transactionCurrency, :transactionAmount, :transactionCreationDate)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("providerTransactionType",x.getProviderTransactionType());
            q.addValue("sellerOrderId",x.getSellerOrderId());
            q.addValue("marketplaceId",x.getMarketplaceId());
            q.addValue("marketplaceCountry",x.getMarketplaceCountry());
            q.addValue("sellerId",x.getSellerId());
            q.addValue("sellerStoreName",x.getSellerStoreName());
            q.addValue("providerId",x.getProviderId());
            q.addValue("providerStoreName",x.getProviderStoreName());
            q.addValue("transactionCurrency",x.getTransactionCurrency());
            q.addValue("transactionAmount",x.getTransactionAmount());
            q.addValue("transactionCreationDate",x.getTransactionCreationDate());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addRetrochargeEventList(AmazonFinanceEvent fe) {
        fe.getRetrochargeEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_retrocharge_event "
                    +" (retrocharge_event_type, amazon_order_id, posted_date, base_tax_currency, base_tax_amount, "
                    +" shipping_tax_currency, shipping_tax_amount, marketplace_name) "
                    +" VALUES ( :retrochargeEventType, :amazonOrderId, :postedDate, :baseTaxCurrency, :baseTaxAmount,"
                    +" :shippingTaxCurrency, :shippingTaxAmount, :marketplaceName)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("retrochargeEventType",x.getRetrochargeEventType());
            q.addValue("amazonOrderId",x.getAmazonOrderId());
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("baseTaxCurrency",x.getBaseTaxCurrency());
            q.addValue("baseTaxAmount",x.getBaseTaxAmount());
            q.addValue("shippingTaxCurrency",x.getShippingTaxCurrency());
            q.addValue("shippingTaxAmount",x.getBaseTaxAmount());
            q.addValue("marketplaceName",x.getMarketplaceName());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addRentalTransactionEventList(AmazonFinanceEvent fe) {
        fe.getRentalTransactionEventList().forEach(x -> {

            String sql ="INSERT INTO finance.amazon_rental_transaction_event "
                    +"(amazon_order_id, rental_event_type, extension_length, posted_date, marketplace_name, "
                    +" rental_initial_currency, rental_initial_value, "
                    +" rental_reimbursement_currency, rental_reimbursement_amount) "
                    +" VALUES( :amazonOrderId, :rentalEventType, :extensionLength, :postedDate, :marketplaceName, "
                    +" :rentalInitialCurrency, :rentalInitialValue, :rentalReimbursementCurrency, :rentalReimbursementAmount)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("amazonOrderId",x.getAmazonOrderId());
            q.addValue("rentalEventType",x.getRentalEventType());
            q.addValue("extensionLength",x.getExtensionLength());
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("marketplaceName",x.getMarketplaceName());
            q.addValue("rentalInitialCurrency",x.getRentalInitialCurrency());
            q.addValue("rentalInitialValue",x.getRentalInitialValue());
            q.addValue("rentalReimbursementCurrency",x.getRentalReimbursementCurrency());
            q.addValue("rentalReimbursementAmount",x.getRentalReimbursementAmount());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addPerformanceBondRefundEventList(AmazonFinanceEvent fe) {
        fe.getPerformanceBondRefundEventList().forEach(x -> {

            String sql ="INSERT INTO finance.amazon_performance_bond_refund_event "
                    +" (marketplace_country, currency, amount, product_group_list) "
                    +" VALUES ( :marketplaceCountry, :currency, :amount, :productGroupList)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("marketplaceCountry",x.getMarketplaceCountry());
            q.addValue("currency",x.getCurrency());
            q.addValue("amount",x.getAmount());
            q.addValue("productGroupList",x.getProductGroupList());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });

    }

    @Override
    public void addProductAdsPaymentEventList(AmazonFinanceEvent fe) {
        fe.getProductAdsPaymentEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_product_ads_payment_event "
                    +" (posted_date, transaction_type, invoice_id, currency, base_value, tax_value, transaction_value) "
                    +" VALUES ( :postedDate, :transactionType, :invoiceId, :currency, "
                    +" :baseValue, :taxValue, :transactionValue) ";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("transactionType",x.getTransactionType());
            q.addValue("invoiceId",x.getInvoiceId());
            q.addValue("currency",x.getCurrency());
            q.addValue("baseValue",x.getBaseValue());
            q.addValue("taxValue",x.getTaxValue());
            q.addValue("transactionValue",x.getTransactionValue());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addServiceFeeEventList(AmazonFinanceEvent fe) {
        fe.getServiceFeeEventList().forEach(x ->{


            String sql ="INSERT INTO finance.amazon_service_fee_event "
                    +" (amazon_order_id, fee_reason, seller_sku, fnsku, fee_description, asin) "
                    +" VALUES ( :AmazonOrderId, :feeReason, :sellerSku, :fnSku, :feeDescription, :asin) ";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("AmazonOrderId",x.getAmazonOrderId());
            q.addValue("feeReason",x.getFeeReason());
            q.addValue("sellerSku",x.getSellerSKU());
            q.addValue("fnSku",x.getFnSKU());
            q.addValue("feeDescription",x.getFeeDescription());
            q.addValue("asin",x.getAsin());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addSellerDealPaymentEventList(AmazonFinanceEvent fe) {
        fe.getSellerDealPaymentEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_seller_deal_payment_event "
                    +" (posted_date, deal_id, deal_description, event_type, "
                    +" fee_type, currency, fee_amount, tax_amount, total_amount) "
                    +" VALUES ( :postedDate, :dealId, :dealDescription, :eventType, "
                    +" :feeType, :currency, :feeAmount, :taxAmount, :totalAmount) ";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("dealId",x.getDealId());
            q.addValue("dealDescription",x.getDealDescription());
            q.addValue("eventType",x.getEventType());
            q.addValue("feeType",x.getFeeType());
            q.addValue("currency",x.getCurrency());
            q.addValue("feeAmount",x.getFeeAmount());
            q.addValue("taxAmount",x.getTaxAmount());
            q.addValue("totalAmount",x.getTotalAmount());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addDebtRecoveryEventList(AmazonFinanceEvent fe) {
        fe.getDebtRecoveryEventList().forEach(x ->{

            String sql="INSERT INTO finance.amazon_debt_recovery_event "
                    +" (debt_recovery_type, currency, recovery_amount, over_payment_credit) "
                    +" VALUES ( :debtRecoveryType, :currency, :recoveryAmount, :overPaymentCredit)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("debtRecoveryType",x.getDebtRecoveryType());
            q.addValue("currency",x.getCurrency());
            q.addValue("recoveryAmount",x.getRecoveryAmount());
            q.addValue("overPaymentCredit",x.getOverPaymentCredit());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addLoanServicingEventList(AmazonFinanceEvent fe) {
        fe.getLoanServicingEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_loan_servicing_event "
                    +" loan_currency, loan_amount, source_business_event_type) "
                    +" VALUES ( :loanCurrency, :loanAmount, :sourceBusinessEventType)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("loanCurrency",x.getLoanCurrency());
            q.addValue("loanAmount",x.getLoanAmount());
            q.addValue("sourceBusinessEventType",x.getSourceBusinessEventType());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addAdjustmentEventList(AmazonFinanceEvent fe) {
        fe.getAdjustmentEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_adjustment_event "
                    +" (adjustment_type, adjustment_currency, adjustment_amount, posted_date) "
                    +" VALUES ( :adjustmentType, :adjustmentCurrency, :adjustmentAmount, :postedDate)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("adjustmentType",x.getAdjustmentType());
            q.addValue("adjustmentCurrency",x.getAdjustmentCurrency());
            q.addValue("adjustmentAmount",x.getAdjustmentAmount());
            q.addValue("postedDate",x.getPostedDate());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addSafetReimbursementEventList(AmazonFinanceEvent fe) {
        fe.getSafetReimbursementEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_safet_reimbursement_event "
                    +" (posted_date, safet_claim_id, reimbursed_currency, reimbursed_amount, reason_code) "
                    +" VALUES ( :postedDate, :safetClaimId, :reimbursedCurrency, :reimbursedAmount, :reasonCode)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("safetClaimId",x.getSafetClaimId());
            q.addValue("reimbursedCurrency",x.getReimbursedCurrency());
            q.addValue("reimbursedAmount",x.getReimbursedAmount());
            q.addValue("reasonCode",x.getReasonCode());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addSellerReviewEnrollmentPaymentEventList(AmazonFinanceEvent fe) {
        fe.getSellerReviewEnrollmentPaymentEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_seller_review_enrollment_payment_event "
                    +" (posted_date, enrollment_id, parent_asin, currency, total_amount) "
                    +" VALUES (:postedDate, :enrollmentId, :parentAsin, :currency, :totalAmount)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("enrollmentId",x.getEnrollmentId());
            q.addValue("parentAsin",x.getParentAsin());
            q.addValue("currency",x.getCurrency());
            q.addValue("totalAmount",x.getTotalAmount());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addFbaLiquidationEventList(AmazonFinanceEvent fe) {
        fe.getFbaLiquidationEventList().forEach(x ->{

            String sql="INSERT INTO finance.amazon_fba_liquidation_event "
                    +" (posted_data, original_removal_order_id, liquidation_proceeds_currency, "
                    +" liquidation_proceeds_amount, liquidation_fee_currency, liquidation_fee_amount) "
                    +" VALUES (:postedDate, :originalRemovalOrderId, :liquidationProceedsCurrency, "
                    +" :liquidationProceedsAmount, :liquidationFeeCurrency, :liquidationFeeAmount)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("originalRemovalOrderId",x.getOriginalRemovalOrderId());
            q.addValue("liquidationProceedsCurrency",x.getLiquidationProceedsCurrency());
            q.addValue("liquidationProceedsAmount",x.getLiquidationProceedsAmount());
            q.addValue("liquidationFeeCurrency",x.getLiquidationFeeCurrency());
            q.addValue("liquidationFeeAmount",x.getLiquidationFeeAmount());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addCouponPaymentEventList(AmazonFinanceEvent fe) {
        fe.getCouponPaymentEventList().forEach(x ->{

            String sql="INSERT INTO finance.amazon_coupon_payment_event "
                    +" (posted_date, coupon_id, seller_coupon_description, clip_or_redemption_count, "
                    +" payment_event_id, currency, total_amount) "
                    +" VALUES ( :postedDate, :couponId, :sellerCouponDescription, :clipOrRedemptionCount,"
                    +" :paymentEventType, :currency, :totalAmount)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("couponId",x.getCouponId());
            q.addValue("sellerCouponDescription",x.getSellerCouponDescription());
            q.addValue("clipOrRedemptionCount",x.getClipOrRedemptionCount());
            q.addValue("paymentEventType",x.getPaymentEventId());
            q.addValue("currency",x.getCurrency());
            q.addValue("totalAmount",x.getTotalAmount());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addImagingServicesFeeEventList(AmazonFinanceEvent fe) {
        fe.getImagingServicesFeeEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_imaging_services_fee_event "
                    +" (imaging_request_billing_item_id, asin, posted_date) "
                    +" VALUES ( :imagingRequestBillingItemId, :asin, :postedDate)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("imagingRequestBillingItemId",x.getImagingRequestBillingItemID());
            q.addValue("asin",x.getAsin());
            q.addValue("postedDate",x.getPostedDate());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addNetworkComminglingTransactionEventList(AmazonFinanceEvent fe) {
        fe.getNetworkComminglingTransactionEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_network_commingling_transaction_event "
                    +" (transaction_type, posted_date, net_co_transaction_id, swap_reason, asin, "
                    +" marketplace_id, currency, tax_exclusive_amount, tax_amount) "
                    +" VALUES ( :transactionType, :postedDate, :netCoTransactionId, :swapReason, :asin, "
                    +" :marketplaceId, :currency, :taxExclusiveAmount, :taxAmount)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("transactionType",x.getTransactionType());
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("netCoTransactionId",x.getNetCoTransactionID());
            q.addValue("swapReason",x.getSwapReason());
            q.addValue("asin",x.getAsin());
            q.addValue("marketplaceId",x.getMarketplaceId());
            q.addValue("currency",x.getCurrency());
            q.addValue("taxExclusiveAmount",x.getTaxExclusiveAmount());
            q.addValue("taxAmount",x.getTaxAmount());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addAffordabilityExpenseEventList(AmazonFinanceEvent fe) {
        fe.getAffordabilityExpenseEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_affordability_expense_event "
                    +" (amazon_order_id, posted_date, marketplace_id, transaction_type, expense_currency, "
                    +" base_expense, tax_type_igst, tax_type_cgst, tax_type_sgst, total_expense) "
                    +" VALUES (:amazonOrderId, :postedDate, :marketplaceId, :transactionType, "
                    +" :expenseCurrency, :baseExpense, :taxTypeIgst, :taxTypeCgst, :taxTypeSgst, :totalExpense)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("amazonOrderId",x.getAmazonOrderId());
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("marketplaceId",x.getMarketplaceId());
            q.addValue("transactionType",x.getTransactionType());
            q.addValue("expenseCurrency",x.getExpenseCurrency());
            q.addValue("baseExpense",x.getBaseExpense());
            q.addValue("taxTypeIgst",x.getTaxTypeIGST());
            q.addValue("taxTypeCgst",x.getTaxTypeCGST());
            q.addValue("taxTypeSgst",x.getTaxTypeSGST());
            q.addValue("totalExpense",x.getTotalExpense());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addAffordabilityExpenseReversalEventList(AmazonFinanceEvent fe) {
        fe.getAffordabilityExpenseReversalEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_affordability_expense_reversal_event "
                    +" (amazon_order_id, posted_date, marketplace_id, transaction_type, expense_currency, "
                    +" base_expense, tax_type_igst, tax_type_cgst, tax_type_sgst, total_expense) "
                    +" VALUES (:amazonOrderId, :postedDate, :marketplaceId, :transactionType, "
                    +" :expenseCurrency, :baseExpense, :taxTypeIgst, :taxTypeCgst, :taxTypeSgst, :totalExpense)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("amazonOrderId",x.getAmazonOrderId());
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("marketplaceId",x.getMarketplaceId());
            q.addValue("transactionType",x.getTransactionType());
            q.addValue("expenseCurrency",x.getExpenseCurrency());
            q.addValue("baseExpense",x.getBaseExpense());
            q.addValue("taxTypeIgst",x.getTaxTypeIGST());
            q.addValue("taxTypeCgst",x.getTaxTypeCGST());
            q.addValue("taxTypeSgst",x.getTaxTypeSGST());
            q.addValue("totalExpense",x.getTotalExpense());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addRemovalShipmentEventList(AmazonFinanceEvent fe) {
        fe.getRemovalShipmentEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_removal_shipment_event "
                    +" (posted_date, order_id, transaction_type) "
                    +" VALUES ( :postedDate, :orderId, :transactionType)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("orderId",x.getOrderId());
            q.addValue("transactionType",x.getTransactionType());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addTrialShipmentEventList(AmazonFinanceEvent fe) {
        fe.getTrialShipmentEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_trial_shipment_event "
                    +" (amazon_order_id, financial_event_group_id, posted_date, sku) "
                    +" VALUES ( :amazonOrderId, :financialEventGroupId, :postedDate, :sku)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("amazonOrderId",x.getAmazonOrderId());
            q.addValue("financialEventGroupId",x.getFinancialEventGroupId());
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("sku",x.getSku());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addTdsReimbursementEventList(AmazonFinanceEvent fe) {
        fe.getTdsReimbursementEventList().forEach(x ->{

            String sql="INSERT INTO finance.amazon_tds_reimbursement_event "
                    +" (posted_date, tds_order_id, reimbursed_currency, reimbursed_amount) "
                    +" VALUES ( :postedDate, :tdsOrderId, :reimbursedCurrency, :reimbursedAmount)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("tdsOrderId",x.getTdsOrderId());
            q.addValue("reimbursedCurrency",x.getReimbursedCurrency());
            q.addValue("reimbursedAmount",x.getReimbursedAmount());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }

    @Override
    public void addTaxWithholdingEventList(AmazonFinanceEvent fe) {
        fe.getTaxWithholdingEventList().forEach(x ->{

            String sql ="INSERT INTO finance.amazon_tax_withholding_event "
                    +" (posted_date, period_start, period_end, currency, base_amount, withheld_amount) "
                    +" VALUES (:postedDate, :periodStart, :periodEnd, :currency, :baseAmount, :withheldAmount)";

            MapSqlParameterSource q = new MapSqlParameterSource();
            q.addValue("postedDate",x.getPostedDate());
            q.addValue("periodStart", Timestamp.valueOf(x.getPeriodStart().atZoneSameInstant(ZoneId.of("Z")).toLocalDateTime()));
            q.addValue("periodEnd",Timestamp.valueOf(x.getPeriodEnd().atZoneSameInstant(ZoneId.of("Z")).toLocalDateTime()));
            q.addValue("currency",x.getCurrency());
            q.addValue("baseAmount",x.getBaseAmount());
            q.addValue("withheldAmount",x.getWithheldAmount());
            Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);

        });
    }
}
