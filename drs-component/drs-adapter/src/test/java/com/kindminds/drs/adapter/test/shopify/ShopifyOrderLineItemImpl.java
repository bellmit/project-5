package com.kindminds.drs.adapter.test.shopify;

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

	public ShopifyOrderLineItemImpl(
			String lineItemId,
			String variantId,
			String title,
			Integer quantity,
			String price,
			String grams,
			String sku,
			String variantTitle,
			String vendor,
			String fulfillmentService,
			String productId,
			Boolean requiresShipping,
			Boolean taxable,
			Boolean giftCard,
			String name,
			String variantInventoryManagement,
			String jsonProperties,
			Boolean productExists,
			Integer fulfillableQuantity,
			String totalDiscount,
			String fulfillmentStatus,
			String jsonPriceSet,
			String jsonTotalDiscountSet,
			String jsonDiscountAllocations,
			String adminGraphqlApiId,
			String jsonTaxLines,
			String jsonOriginLocation,
			String jsonDestinationLocation){
		this.lineItemId=lineItemId;
		this.variantId=variantId;
		this.title=title;
		this.quantity=quantity;
		this.price=new BigDecimal(price);
		this.grams=new BigDecimal(grams);
		this.sku=sku;
		this.variantTitle=variantTitle;
		this.vendor=vendor;
		this.fulfillmentService=fulfillmentService;
		this.productId=productId;
		this.requiresShipping=requiresShipping;
		this.taxable=taxable;
		this.giftCard=giftCard;
		this.name=name;
		this.variantInventoryManagement=variantInventoryManagement;
		this.jsonProperties=jsonProperties;
		this.productExists=productExists;
		this.fulfillableQuantity=fulfillableQuantity;
		this.totalDiscount=new BigDecimal(totalDiscount);
		this.fulfillmentStatus=fulfillmentStatus;
		this.jsonPriceSet = jsonPriceSet;
		this.jsonTotalDiscountSet = jsonTotalDiscountSet;
		this.jsonDiscountAllocations=jsonDiscountAllocations;
		this.adminGraphqlApiId=adminGraphqlApiId;
		this.jsonTaxLines=jsonTaxLines;
		this.jsonOriginLocation=jsonOriginLocation;
		this.jsonDestinationLocation=jsonDestinationLocation;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof ShopifyOrderLineItem){
			ShopifyOrderLineItem soli = (ShopifyOrderLineItem)obj;
			return this.getLineItemId().equals(soli.getLineItemId())
				&& this.getVariantId().equals(soli.getVariantId())
				&& this.getTitle().equals(soli.getTitle())
				&& this.getQuantity().equals(soli.getQuantity())
				&& this.getPrice().equals(soli.getPrice())
				&& this.getGrams().equals(soli.getGrams())
				&& this.getSku().equals(soli.getSku())
				&& this.getVariantTitle().equals(soli.getVariantTitle())
				&& this.getVendor().equals(soli.getVendor())
				&& this.getFulfillmentService().equals(soli.getFulfillmentService())
				&& this.getProductId().equals(soli.getProductId())
				&& this.getRequiresShipping().equals(soli.getRequiresShipping())
				&& this.getTaxable().equals(soli.getTaxable())
				&& this.getGiftCard().equals(soli.getGiftCard())
				&& this.getName().equals(soli.getName())
				&& this.getVariantInventoryManagement().equals(soli.getVariantInventoryManagement())
				&& this.getJsonProperties().equals(soli.getJsonProperties())
				&& this.getProductExists().equals(soli.getProductExists())
				&& this.getFulfillableQuantity().equals(soli.getFulfillableQuantity())
				&& this.getTotalDiscount().equals(soli.getTotalDiscount())
				&& this.getFulfillmentStatus().equals(soli.getFulfillmentStatus())
				&& this.getJsonPriceSet().equals(soli.getJsonPriceSet())
				&& this.getJsonTotalDiscountSet().equals(soli.getJsonTotalDiscountSet())
				&& this.getJsonDiscountAllocations().equals(soli.getJsonDiscountAllocations())
				&& this.getAdminGraphqlApiId().equals(soli.getAdminGraphqlApiId())
				&& this.getJsonTaxLines().equals(soli.getJsonTaxLines())
				&& this.getJsonOriginLocation().equals(soli.getJsonOriginLocation())
				&& this.getJsonDestinationLocation().equals(soli.getJsonDestinationLocation());
		}
		return false;
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