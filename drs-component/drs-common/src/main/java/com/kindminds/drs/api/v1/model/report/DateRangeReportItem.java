package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.Date;

public interface DateRangeReportItem {

	Integer getId();
	Integer getMarketplaceId();
	Date getImportDate();
	String getDateTime();
	String getSettlementId();
	String getType();
	String getOrderId();
	String getSku();
	String getDescription();
	Integer getQuantity();
	String getMarketplace();
	String getAccountType();
	String getFulfillment();
	String getOrderCity();
	String getOrderState();
	String getOrderPostal();
	BigDecimal getProductSales();
	BigDecimal getShippingCredits();
	BigDecimal getGiftWrapCredits();
	BigDecimal getPromotionalRebates();
	BigDecimal getSalesTaxCollected();
	BigDecimal getMarketplaceFacilitatorTax();
	BigDecimal getSellingFees();
	BigDecimal getFbaFees();
	BigDecimal getOtherTransactionFees();
	BigDecimal getOther();
	BigDecimal getTotal();
	String getTaxCollectionModel();
	BigDecimal getProductSalesTax();
	BigDecimal getShippingCreditsTax();
	BigDecimal getGiftWrapCreditsTax();
	BigDecimal getPromotionalRebatesTax();

}
