package com.kindminds.drs.adapter.shopify.dto;

import java.math.BigDecimal;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderLineItem;

public class ShopifyOrderLineItemImpl implements ShopifyOrderLineItem {
	
	private String lineItemId;
	private String variantId;
	private String title;
	private Integer quantity;
	private BigDecimal price;
	private BigDecimal grams;
	private String sku;
	private String variantTitle;
	private String vendor;
	private String fulfillmentService;
	private String productId;
	private Boolean requiresShipping;
	private Boolean taxable;
	private Boolean giftCard;
	private String name;
	private String variantInventoryManagement;
	private String jsonProperties;
	private Boolean productExists;
	private Integer fulfillableQuantity;
	private BigDecimal totalDiscount;
	private String fulfillmentStatus;
	private String jsonPriceSet;
	private String jsonTotalDiscountSet;
	private String jsonDiscountAllocations;
	private String adminGraphqlApiId;
	private String jsonTaxLines;
	private String jsonOriginLocation;
	private String jsonDestinationLocation;
	
	public ShopifyOrderLineItemImpl(){}
	
	public ShopifyOrderLineItemImpl(RawShopifyOrderLineItem rawShopifyOrderLineItem) {
		this.lineItemId = rawShopifyOrderLineItem.getLineItemId();
		this.variantId = rawShopifyOrderLineItem.getVariantId();
		this.title = rawShopifyOrderLineItem.getTitle();
		this.quantity = Integer.valueOf(rawShopifyOrderLineItem.getQuantity());
		this.price = new BigDecimal(rawShopifyOrderLineItem.getPrice());
		this.grams = new BigDecimal(rawShopifyOrderLineItem.getGrams());
		this.sku = rawShopifyOrderLineItem.getSku();
		this.variantTitle = rawShopifyOrderLineItem.getVariantTitle();
		this.vendor = rawShopifyOrderLineItem.getVendor();
		this.fulfillmentService = rawShopifyOrderLineItem.getFulfillmentService();
		this.productId = rawShopifyOrderLineItem.getProductId();
		this.requiresShipping = Boolean.valueOf(rawShopifyOrderLineItem.getRequiresShipping());
		this.taxable = Boolean.valueOf(rawShopifyOrderLineItem.getTaxable());
		this.giftCard = Boolean.valueOf(rawShopifyOrderLineItem.getGiftCard());
		this.name = rawShopifyOrderLineItem.getName();
		this.variantInventoryManagement = rawShopifyOrderLineItem.getVariantInventoryManagement();
		this.jsonProperties = rawShopifyOrderLineItem.getJsonProperties();
		this.productExists = Boolean.valueOf(rawShopifyOrderLineItem.getProductExists());
		this.fulfillableQuantity = Integer.valueOf(rawShopifyOrderLineItem.getFulfillableQuantity());
		this.totalDiscount = new BigDecimal(rawShopifyOrderLineItem.getTotalDiscount());
		this.fulfillmentStatus = rawShopifyOrderLineItem.getFulfillmentStatus();
		this.jsonPriceSet = rawShopifyOrderLineItem.getJsonPriceSet();
		this.jsonTotalDiscountSet = rawShopifyOrderLineItem.getJsonTotalDiscountSet();
		this.jsonDiscountAllocations = rawShopifyOrderLineItem.getJsonDiscountAllocations();
		this.adminGraphqlApiId  = rawShopifyOrderLineItem.getAdminGraphqlApiId();
		this.jsonTaxLines = rawShopifyOrderLineItem.getJsonTaxLines();
		this.jsonOriginLocation = rawShopifyOrderLineItem.getJsonOriginLocation()==null?null:rawShopifyOrderLineItem.getJsonOriginLocation();
		this.jsonDestinationLocation = rawShopifyOrderLineItem.getJsonDestinationLocation()==null?null:rawShopifyOrderLineItem.getJsonDestinationLocation();
	}

	@Override
	public String toString() {
		return "ShopifyOrderLineItemImpl [getLineItemId()=" + getLineItemId() + ", getVariantId()=" + getVariantId()
				+ ", getTitle()=" + getTitle() + ", getQuantity()=" + getQuantity() + ", getPrice()=" + getPrice()
				+ ", getGrams()=" + getGrams() + ", getSku()=" + getSku() + ", getVariantTitle()=" + getVariantTitle()
				+ ", getVendor()=" + getVendor() + ", getFulfillmentService()=" + getFulfillmentService()
				+ ", getProductId()=" + getProductId() + ", getRequiresShipping()=" + getRequiresShipping()
				+ ", getTaxable()=" + getTaxable() + ", getGiftCard()=" + getGiftCard() + ", getName()=" + getName()
				+ ", getVariantInventoryManagement()=" + getVariantInventoryManagement() + ", getJsonProperties()="
				+ getJsonProperties() + ", getProductExists()=" + getProductExists() + ", getFulfillableQuantity()="
				+ getFulfillableQuantity() + ", getTotalDiscount()=" + getTotalDiscount() + ", getFulfillmentStatus()="
				+ getFulfillmentStatus() + ", getJsonPriceSet()=" + getJsonPriceSet() + ", getJsonTotalDiscountSet()="
				+ getJsonTotalDiscountSet() + ", getJsonDiscountAllocations()=" + getJsonDiscountAllocations()
				+ ", getAdminGraphqlApiId()=" + getAdminGraphqlApiId() + ", getJsonTaxLines()=" + getJsonTaxLines()
				+ ", getJsonOriginLocation()=" + getJsonOriginLocation() + ", getJsonDestinationLocation()="
				+ getJsonDestinationLocation() + "]";
	}

	@Override
	public String getLineItemId() {
		return this.lineItemId;
	}

	@Override
	public String getVariantId() {
		return this.variantId;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	@Override
	public BigDecimal getPrice() {
		return this.price;
	}

	@Override
	public BigDecimal getGrams() {
		return this.grams;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getVariantTitle() {
		return this.variantTitle;
	}

	@Override
	public String getVendor() {
		return this.vendor;
	}

	@Override
	public String getFulfillmentService() {
		return this.fulfillmentService;
	}

	@Override
	public String getProductId() {
		return this.productId;
	}

	@Override
	public Boolean getRequiresShipping() {
		return this.requiresShipping;
	}

	@Override
	public Boolean getTaxable() {
		return this.taxable;
	}

	@Override
	public Boolean getGiftCard() {
		return this.giftCard;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getVariantInventoryManagement() {
		return this.variantInventoryManagement;
	}

	@Override
	public String getJsonProperties() {
		return this.jsonProperties;
	}

	@Override
	public Boolean getProductExists() {
		return this.productExists;
	}

	@Override
	public Integer getFulfillableQuantity() {
		return this.fulfillableQuantity;
	}

	@Override
	public BigDecimal getTotalDiscount() {
		return this.totalDiscount;
	}

	@Override
	public String getFulfillmentStatus() {
		return this.fulfillmentStatus;
	}

	@Override
	public String getJsonPriceSet() { return this.jsonPriceSet; }

	@Override
	public String getJsonTotalDiscountSet() { return this.jsonTotalDiscountSet; }

	@Override
	public String getJsonDiscountAllocations() {
		return this.jsonDiscountAllocations;
	}

	@Override
	public String getAdminGraphqlApiId() {
		return this.adminGraphqlApiId;
	}
	
	@Override
	public String getJsonTaxLines() {
		return this.jsonTaxLines;
	}

	@Override
	public String getJsonOriginLocation() {
		return this.jsonOriginLocation;
	}

	@Override
	public String getJsonDestinationLocation() {
		return this.jsonDestinationLocation;
	}

}
