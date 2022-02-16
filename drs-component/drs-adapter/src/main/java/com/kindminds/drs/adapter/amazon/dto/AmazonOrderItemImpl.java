package com.kindminds.drs.adapter.amazon.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.FulfillmentOrderItem;
import com.amazonservices.mws.orders._2013_09_01.model.OrderItem;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderItem;

public class AmazonOrderItemImpl implements AmazonOrderItem {

	private String orderItemId;
	private int quantityOrdered;
	private Integer quantityShipped;
	private String title;
	private String asin;
	private String sellerSKU;
	private Currency itemPriceCurrency;
	private BigDecimal itemPriceAmount;
	private Currency shippingPriceCurrency;
	private BigDecimal shippingPriceAmount;
	private Currency giftWrapPriceCurrency;
	private BigDecimal giftWrapPriceAmount;
	private Currency itemTaxCurrency;
	private BigDecimal itemTaxAmount;
	private Currency shippingTaxCurrency;
	private BigDecimal shippingTaxAmount;
	private Currency giftWrapTaxCurrency;
	private BigDecimal giftWrapTaxAmount;
	private Currency shippingDiscountCurrency;
	private BigDecimal shippingDiscountAmount;
	private Currency promotionDiscountCurrency;
	private BigDecimal promotionDiscountAmount;
	private List<String> promotionIds;
	private Currency cODFeeCurrency;
	private BigDecimal cODFeeAmount;
	private Currency cODFeeDiscountCurrency;
	private BigDecimal cODFeeDiscountAmount;
	private String giftMessageText;
	private String giftWrapLevel;
	private String invoiceTitle;
	private String invoiceRequirement;
	private String invoiceCategoryBuyerSelected;
	private String invoiceInformation;
	private String conditionNote;
	private String conditionId;
	private String conditionSubtypeId;
	private String scheduledDeliveryStartDate;
	private String scheduledDeliveryEndDate;
	
	public AmazonOrderItemImpl(FulfillmentOrderItem orderItem){
		this.sellerSKU=orderItem.getSellerSKU();
		this.orderItemId=orderItem.getSellerFulfillmentOrderItemId();
		this.quantityOrdered=orderItem.getQuantity();
		this.giftMessageText=orderItem.getGiftMessage();
//		DisplayableComment
//		FulfillmentNetworkSKU 
//		CancelledQuantity 
//		UnfulfillableQuantity 
//		EstimatedShipDateTime 
//		EstimatedArrivalDateTime 
//		PerUnitDeclaredValue
//		PerUnitPrice
//		PerUnitTax		
	}
	
	public AmazonOrderItemImpl(OrderItem orig){
		this.orderItemId=orig.getOrderItemId();
		this.quantityOrdered=orig.getQuantityOrdered();
		this.quantityShipped=orig.getQuantityShipped();
		this.title=orig.getTitle();
		this.asin=orig.getASIN();
		this.sellerSKU=orig.getSellerSKU();
		this.itemPriceCurrency=orig.getItemPrice()==null?null:Currency.valueOf(orig.getItemPrice().getCurrencyCode());
		this.itemPriceAmount=orig.getItemPrice()==null?null:new BigDecimal(orig.getItemPrice().getAmount());
		this.shippingPriceCurrency=orig.getShippingPrice()==null?null:Currency.valueOf(orig.getShippingPrice().getCurrencyCode());
		this.shippingPriceAmount=orig.getShippingPrice()==null?null:new BigDecimal(orig.getShippingPrice().getAmount());
		this.giftWrapPriceCurrency=orig.getGiftWrapPrice()==null?null:Currency.valueOf(orig.getGiftWrapPrice().getCurrencyCode());
		this.giftWrapPriceAmount=orig.getGiftWrapPrice()==null?null:new BigDecimal(orig.getGiftWrapPrice().getAmount());
		this.itemTaxCurrency=orig.getItemTax()==null?null:Currency.valueOf(orig.getItemTax().getCurrencyCode());
		this.itemTaxAmount=orig.getItemTax()==null?null:new BigDecimal(orig.getItemTax().getAmount());
		this.shippingTaxCurrency=orig.getShippingTax()==null?null:Currency.valueOf(orig.getShippingTax().getCurrencyCode());
		this.shippingTaxAmount=orig.getShippingTax()==null?null:new BigDecimal(orig.getShippingTax().getAmount());
		this.giftWrapTaxCurrency=orig.getGiftWrapTax()==null?null:Currency.valueOf(orig.getGiftWrapTax().getCurrencyCode());
		this.giftWrapTaxAmount=orig.getGiftWrapTax()==null?null:new BigDecimal(orig.getGiftWrapTax().getAmount());
		this.shippingDiscountCurrency=orig.getShippingDiscount()==null?null:Currency.valueOf(orig.getShippingDiscount().getCurrencyCode());
		this.shippingDiscountAmount=orig.getShippingDiscount()==null?null:new BigDecimal(orig.getShippingDiscount().getAmount());
		this.promotionDiscountCurrency=orig.getPromotionDiscount()==null?null:Currency.valueOf(orig.getPromotionDiscount().getCurrencyCode());
		this.promotionDiscountAmount=orig.getPromotionDiscount()==null?null:new BigDecimal(orig.getPromotionDiscount().getAmount());
		this.promotionIds=new ArrayList<String>(orig.getPromotionIds());
		this.cODFeeCurrency=orig.getCODFee()==null?null:Currency.valueOf(orig.getCODFee().getCurrencyCode());
		this.cODFeeAmount=orig.getCODFee()==null?null:new BigDecimal(orig.getCODFee().getAmount());
		this.cODFeeDiscountCurrency=orig.getCODFeeDiscount()==null?null:Currency.valueOf(orig.getCODFeeDiscount().getCurrencyCode());
		this.cODFeeDiscountAmount=orig.getCODFeeDiscount()==null?null:new BigDecimal(orig.getCODFeeDiscount().getAmount());
		this.giftMessageText=orig.getGiftMessageText();
		this.giftWrapLevel=orig.getGiftWrapLevel();
		this.invoiceTitle=orig.getInvoiceData()==null?null:orig.getInvoiceData().getInvoiceTitle();
		this.invoiceRequirement=orig.getInvoiceData()==null?null:orig.getInvoiceData().getInvoiceRequirement();
		this.invoiceCategoryBuyerSelected=orig.getInvoiceData()==null?null:orig.getInvoiceData().getBuyerSelectedInvoiceCategory();
		this.invoiceInformation=orig.getInvoiceData()==null?null:orig.getInvoiceData().getInvoiceInformation();
		this.conditionNote=orig.getConditionNote();
		this.conditionId=orig.getConditionId();
		this.conditionSubtypeId=orig.getConditionSubtypeId();
		this.scheduledDeliveryStartDate=orig.getScheduledDeliveryStartDate();
		this.scheduledDeliveryEndDate=orig.getScheduledDeliveryEndDate();
	}

	@Override
	public String toString() {
		return "AmazonOrderItemImpl [getOrderItemId()=" + getOrderItemId()
				+ ", getQuantityOrdered()=" + getQuantityOrdered()
				+ ", getQuantityShipped()=" + getQuantityShipped()
				+ ", getTitle()=" + getTitle() + ", getASIN()=" + getASIN()
				+ ", getSellerSKU()=" + getSellerSKU()
				+ ", getItemPriceCurrency()=" + getItemPriceCurrency()
				+ ", getItemPriceAmount()=" + getItemPriceAmount()
				+ ", getShippingPriceCurrency()=" + getShippingPriceCurrency()
				+ ", getShippingPriceAmount()=" + getShippingPriceAmount()
				+ ", getGiftWrapPriceCurrency()=" + getGiftWrapPriceCurrency()
				+ ", getGiftWrapPriceAmount()=" + getGiftWrapPriceAmount()
				+ ", getItemTaxCurrency()=" + getItemTaxCurrency()
				+ ", getItemTaxAmount()=" + getItemTaxAmount()
				+ ", getShippingTaxCurrency()=" + getShippingTaxCurrency()
				+ ", getShippingTaxAmount()=" + getShippingTaxAmount()
				+ ", getGiftWrapTaxCurrency()=" + getGiftWrapTaxCurrency()
				+ ", getGiftWrapTaxAmount()=" + getGiftWrapTaxAmount()
				+ ", getShippingDiscountCurrency()="
				+ getShippingDiscountCurrency()
				+ ", getShippingDiscountAmount()="
				+ getShippingDiscountAmount()
				+ ", getPromotionDiscountCurrency()="
				+ getPromotionDiscountCurrency()
				+ ", getPromotionDiscountAmount()="
				+ getPromotionDiscountAmount() + ", getPromotionIds()="
				+ getPromotionIds() + ", getCODFeeCurrency()="
				+ getCODFeeCurrency() + ", getCODFeeAmount()="
				+ getCODFeeAmount() + ", getCODFeeDiscountCurrency()="
				+ getCODFeeDiscountCurrency() + ", getCODFeeDiscountAmount()="
				+ getCODFeeDiscountAmount() + ", getGiftMessageText()="
				+ getGiftMessageText() + ", getGiftWrapLevel()="
				+ getGiftWrapLevel() + ", getInvoiceTitle()="
				+ getInvoiceTitle() + ", getInvoiceRequirement()="
				+ getInvoiceRequirement()
				+ ", getInvoiceCategoryBuyerSelected()="
				+ getInvoiceCategoryBuyerSelected()
				+ ", getInvoiceInformation()=" + getInvoiceInformation()
				+ ", getConditionNote()=" + getConditionNote()
				+ ", getConditionId()=" + getConditionId()
				+ ", getConditionSubtypeId()=" + getConditionSubtypeId()
				+ ", getScheduledDeliveryStartDate()="
				+ getScheduledDeliveryStartDate()
				+ ", getScheduledDeliveryEndDate()="
				+ getScheduledDeliveryEndDate() + "]";
	}



	@Override
	public String getOrderItemId() {
		return this.orderItemId ;
	}

	@Override
	public int getQuantityOrdered() {
		return this.quantityOrdered;
	}

	@Override
	public Integer getQuantityShipped() {
		return this.quantityShipped ;
	}

	@Override
	public String getTitle() {
		return this.title ;
	}

	@Override
	public String getASIN() {
		return this.asin ;
	}

	@Override
	public String getSellerSKU() {
		return this.sellerSKU ;
	}

	@Override
	public Currency getItemPriceCurrency() {
		return this.itemPriceCurrency ;
	}

	@Override
	public BigDecimal getItemPriceAmount() {
		return this.itemPriceAmount ;
	}

	@Override
	public Currency getShippingPriceCurrency() {
		return this.shippingPriceCurrency ;
	}

	@Override
	public BigDecimal getShippingPriceAmount() {
		return this.shippingPriceAmount ;
	}

	@Override
	public Currency getGiftWrapPriceCurrency() {
		return this.giftWrapPriceCurrency ;
	}

	@Override
	public BigDecimal getGiftWrapPriceAmount() {
		return this.giftWrapPriceAmount ;
	}

	@Override
	public Currency getItemTaxCurrency() {
		return this.itemTaxCurrency ;
	}

	@Override
	public BigDecimal getItemTaxAmount() {
		return this.itemTaxAmount ;
	}

	@Override
	public Currency getShippingTaxCurrency() {
		return this.shippingTaxCurrency ;
	}

	@Override
	public BigDecimal getShippingTaxAmount() {
		return this.shippingTaxAmount ;
	}

	@Override
	public Currency getGiftWrapTaxCurrency() {
		return this.giftWrapTaxCurrency ;
	}

	@Override
	public BigDecimal getGiftWrapTaxAmount() {
		return this.giftWrapTaxAmount ;
	}

	@Override
	public Currency getShippingDiscountCurrency() {
		return this.shippingDiscountCurrency ;
	}

	@Override
	public BigDecimal getShippingDiscountAmount() {
		return this.shippingDiscountAmount ;
	}

	@Override
	public Currency getPromotionDiscountCurrency() {
		return this.promotionDiscountCurrency ;
	}

	@Override
	public BigDecimal getPromotionDiscountAmount() {
		return this.promotionDiscountAmount ;
	}

	@Override
	public List<String> getPromotionIds() {
		return this.promotionIds ;
	}

	@Override
	public Currency getCODFeeCurrency() {
		return this.cODFeeCurrency ;
	}

	@Override
	public BigDecimal getCODFeeAmount() {
		return this.cODFeeAmount ;
	}

	@Override
	public Currency getCODFeeDiscountCurrency() {
		return this.cODFeeDiscountCurrency ;
	}

	@Override
	public BigDecimal getCODFeeDiscountAmount() {
		return this.cODFeeDiscountAmount ;
	}

	@Override
	public String getGiftMessageText() {
		return this.giftMessageText ;
	}

	@Override
	public String getGiftWrapLevel() {
		return this.giftWrapLevel ;
	}

	@Override
	public String getInvoiceTitle() {
		return this.invoiceTitle ;
	}

	@Override
	public String getInvoiceRequirement() {
		return this.invoiceRequirement ;
	}

	@Override
	public String getInvoiceCategoryBuyerSelected() {
		return this.invoiceCategoryBuyerSelected ;
	}

	@Override
	public String getInvoiceInformation() {
		return this.invoiceInformation ;
	}

	@Override
	public String getConditionNote() {
		return this.conditionNote ;
	}

	@Override
	public String getConditionId() {
		return this.conditionId ;
	}

	@Override
	public String getConditionSubtypeId() {
		return this.conditionSubtypeId ;
	}

	@Override
	public String getScheduledDeliveryStartDate() {
		return this.scheduledDeliveryStartDate ;
	}

	@Override
	public String getScheduledDeliveryEndDate() {
		return this.scheduledDeliveryEndDate ;
	}

}
