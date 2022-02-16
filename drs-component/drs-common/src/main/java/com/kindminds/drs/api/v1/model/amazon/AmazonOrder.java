package com.kindminds.drs.api.v1.model.amazon;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AmazonOrder {
	public enum AmazonOrderStatus{
		PENDING("Pending"),SHIPPED("Shipped"),CANCELED("Canceled");
		private String strValue;
		AmazonOrderStatus(String value){ this.strValue = value; }
		public String getStrValue(){ return this.strValue; }
	}
	public String getAmazonOrderId();
	public Date getPurchaseDate();
	public Date getLastUpdateDate();
	public String getOrderStatus();
	public String getFulfillmentChannel();
	public String getSalesChannel();
	public Integer getNumberOfItemsShipped();
	public Integer getNumberOfItemsUnshipped();
	public String getSellerOrderId();
	public String getShipServiceLevel();
	public String getPaymentMethod();
	public String getMarketplaceId();
	public String getBuyerEmail();
	public String getBuyerName();
	public String getShipmentServiceLevelCategory();
	public String getOrderType();
	public Date getEarliestShipDate();
	public Date getLatestShipDate();
	public String getTFMShipmentStatus();
	public String getOrderChannel();
	public String getAddressCountryCode();
	public String getAddressLine1();
	public String getAddressLine2();
	public String getAddressLine3();
	public String getAddressStateOrRegion();
	public String getAddressPostalCode();
	public String getAddressPhone();
	public String getAddressCity();
	public String getAddressCounty();
	public String getAddressDistrict();
	public String getAddressName();
	public Currency getOrderTotalCurrencyCode();
	public BigDecimal getOrderTotalAmount();
	public Boolean getShippedByAmazonTFM();
	public String getCbaDisplayableShippingLabel();
	public Date getEarliestDeliveryDate();
	public Date getLatestDeliveryDate();
	public List<AmazonOrderPaymentExecDetail> getPaymentExecutionDetail();
	public List<AmazonOrderItem> getItems();
	
	public interface AmazonOrderPaymentExecDetail {
		public String getPaymentMethod();
		public Currency getCurrency();
		public BigDecimal getAmount();
	}
	
	public interface AmazonOrderItem {
		public String getOrderItemId();
		public int getQuantityOrdered();
		public Integer getQuantityShipped();
		public String getTitle();
		public String getASIN();
		public String getSellerSKU();
		public Currency getItemPriceCurrency();
		public BigDecimal getItemPriceAmount();
		public Currency getShippingPriceCurrency();
		public BigDecimal getShippingPriceAmount();
		public Currency getGiftWrapPriceCurrency();
		public BigDecimal getGiftWrapPriceAmount();
		public Currency getItemTaxCurrency();
		public BigDecimal getItemTaxAmount();
		public Currency getShippingTaxCurrency();
		public BigDecimal getShippingTaxAmount();
		public Currency getGiftWrapTaxCurrency();
		public BigDecimal getGiftWrapTaxAmount();
		public Currency getShippingDiscountCurrency();
		public BigDecimal getShippingDiscountAmount();
		public Currency getPromotionDiscountCurrency();
		public BigDecimal getPromotionDiscountAmount();
		public Currency getCODFeeCurrency();
		public BigDecimal getCODFeeAmount();
		public Currency getCODFeeDiscountCurrency();
		public BigDecimal getCODFeeDiscountAmount();
		public String getGiftMessageText();
		public String getGiftWrapLevel();
		public String getInvoiceTitle();
		public String getInvoiceRequirement();
		public String getInvoiceCategoryBuyerSelected();
		public String getInvoiceInformation();
		public String getConditionNote();
		public String getConditionId();
		public String getConditionSubtypeId();
		public String getScheduledDeliveryStartDate();
		public String getScheduledDeliveryEndDate();
		public List<String> getPromotionIds();
	}
}
