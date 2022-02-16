package com.kindminds.drs.persist.v1.model.mapping.amazon;

import java.math.BigDecimal;





import com.kindminds.drs.api.v1.model.accounting.AmazonOrderDetail;


public class AmazonOrderDetailImpl implements AmazonOrderDetail {
	
	//@Id ////@Column(name="id")
	private String id;
	//@Column(name="ship_service_level")
	private String shippingMethod;	
	//@Column(name="address_country")
	private String shippingCountry;	
	//@Column(name="address_district")
	private String shippingState;	
	//@Column(name="address_city")
	private String shippingCity;	
	//@Column(name="address_postalcode")
	private String shippingPostalCode;	
	//@Column(name="item_tax_amount")
	private BigDecimal saleTax;	
	//@Column(name="shipping_price_amount")
	private BigDecimal shippingPrice;	
	//@Column(name="shipping_tax_amount")
	private BigDecimal shippingTax;	
	//@Column(name="gift_wrap_price_amount")
	private BigDecimal giftWrapPrice;	
	//@Column(name="gift_wrap_tax_amount")
	private BigDecimal giftWrapTax;	
	//@Column(name="promotion_discount_amount")
	private BigDecimal itemPromotionDiscount;	
	//@Column(name="shipping_discount_amount")
	private BigDecimal shippingPromotionDiscount;
	//@Column(name="refund_reason")
	private String refundReason;
	//@Column(name="refund_detail_disposition")
	private String refundDetailedDisposition;

	public AmazonOrderDetailImpl() {
	}

	public AmazonOrderDetailImpl(String id, String shippingMethod, String shippingCountry, String shippingState, String shippingCity, String shippingPostalCode, BigDecimal saleTax, BigDecimal shippingPrice, BigDecimal shippingTax, BigDecimal giftWrapPrice, BigDecimal giftWrapTax, BigDecimal itemPromotionDiscount, BigDecimal shippingPromotionDiscount, String refundReason, String refundDetailedDisposition) {
		this.id = id;
		this.shippingMethod = shippingMethod;
		this.shippingCountry = shippingCountry;
		this.shippingState = shippingState;
		this.shippingCity = shippingCity;
		this.shippingPostalCode = shippingPostalCode;
		this.saleTax = saleTax;
		this.shippingPrice = shippingPrice;
		this.shippingTax = shippingTax;
		this.giftWrapPrice = giftWrapPrice;
		this.giftWrapTax = giftWrapTax;
		this.itemPromotionDiscount = itemPromotionDiscount;
		this.shippingPromotionDiscount = shippingPromotionDiscount;
		this.refundReason = refundReason;
		this.refundDetailedDisposition = refundDetailedDisposition;
	}

	@Override
	public String toString() {
		return "AmazonOrderDetailImpl [getShippingMethod()=" + getShippingMethod() + ", getShippingCountry()="
				+ getShippingCountry() + ", getShippingState()=" + getShippingState() + ", getShippingCity()="
				+ getShippingCity() + ", getShippingPostalCode()=" + getShippingPostalCode() + ", getSaleTax()="
				+ getSaleTax() + ", getShippingPrice()=" + getShippingPrice() + ", getShippingTax()=" + getShippingTax()
				+ ", getGiftWrapPrice()=" + getGiftWrapPrice() + ", getGiftWrapTax()=" + getGiftWrapTax()
				+ ", getItemPromotionDiscount()=" + getItemPromotionDiscount() + ", getShippingPromotionDiscount()="
				+ getShippingPromotionDiscount() + ", getRefundReason()=" + getRefundReason()
				+ ", getRefundDetailedDisposition()=" + getRefundDetailedDisposition() + "]";
	}

	@Override
	public String getShippingMethod() {
		return this.shippingMethod;
	}

	@Override
	public String getShippingCountry() {
		return this.shippingCountry;
	}

	@Override
	public String getShippingState() {
		return this.shippingState;
	}

	@Override
	public String getShippingCity() {
		return this.shippingCity;
	}

	@Override
	public String getShippingPostalCode() {
		return this.shippingPostalCode;
	}

	@Override
	public String getSaleTax() {		
		return this.saleTax==null?"":this.saleTax.setScale(2).toPlainString();
	}

	@Override
	public String getShippingPrice() {
		return this.shippingPrice==null?"":this.shippingPrice.setScale(2).toPlainString();
	}

	@Override
	public String getShippingTax() {
		return this.shippingTax==null?"":this.shippingTax.setScale(2).toPlainString();
	}

	@Override
	public String getGiftWrapPrice() {
		return this.giftWrapPrice==null?"":this.giftWrapPrice.setScale(2).toPlainString();
	}

	@Override
	public String getGiftWrapTax() {
		return this.giftWrapTax==null?"":this.giftWrapTax.setScale(2).toPlainString();
	}

	@Override
	public String getItemPromotionDiscount() {
		return this.itemPromotionDiscount==null?"":this.itemPromotionDiscount.setScale(2).toPlainString();
	}

	@Override
	public String getShippingPromotionDiscount() {
		return this.shippingPromotionDiscount==null?"":this.shippingPromotionDiscount.setScale(2).toPlainString();
	}

	@Override
	public String getRefundReason() {
		return this.refundReason;
	}

	@Override
	public String getRefundDetailedDisposition() {
		return this.refundDetailedDisposition;
	}

}
