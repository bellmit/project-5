package com.kindminds.drs.persist.v1.model.mapping.sales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;






import com.kindminds.drs.api.v1.model.sales.CustomerOrderExporting;
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;
import org.springframework.util.StringUtils;


import com.kindminds.drs.util.DateHelper;


public class CustomerOrderExportingImpl implements CustomerOrderExporting {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="order_time")
	private Date orderTime;
	//@Column(name="marketplace_order_id")
	private String marketplaceOrderId;	
	//@Column(name="shopify_order_id")
	private String shopifyOrderId;
	//@Column(name="order_status")
	private String orderStatus;
	//@Column(name="sku_code")
	private String skuCode;
	//@Column(name="product_name")
	private String productName;
	//@Column(name="item_price")
	private BigDecimal itemPrice;	
	//@Column(name="actual_retail_price")
	private BigDecimal actualRetailPrice;
	//@Column(name="qty")
	private String qty;
	//@Column(name="buyer")
	private String buyer;
	//@Column(name="sales_channel")
	private String salesChannel;
	//@Column(name="fulfillment_center")
	private String fulfillmentCenter;
	//@Column(name="refund_dt_id")
	private Integer refundDtId;
	//@Column(name="city")
	private String city;
	//@Column(name="state")
	private String state;
	//@Column(name="country")
	private String country;
	//@Column(name="transaction_time")
	private String transactionTime = null;

	private String transactionTimeUTC = null;
	//@Column(name="promotion_Id")
	private String promotionId = null;

	/*
	public void setTransactionDate(String transactionDate){
		this.transactionTimeUTC = transactionDate;
	}
	
	public void setPromotionIds(String promotionIds){
		this.promotionId = promotionIds;
	}
		*/

	public CustomerOrderExportingImpl() {
	}

	public CustomerOrderExportingImpl(int id, Long orderTime, String marketplaceOrderId, String shopifyOrderId, String orderStatus, String skuCode, String productName, BigDecimal itemPrice, BigDecimal actualRetailPrice, String qty, String buyer, String salesChannel, String fulfillmentCenter, Integer refundDtId, String city, String state, String country, String transactionTime, String transactionTimeUTC, String promotionId) {
		this.id = id;
		this.orderTime = new Date(orderTime);
		this.marketplaceOrderId = marketplaceOrderId;
		this.shopifyOrderId = shopifyOrderId;
		this.orderStatus = orderStatus;
		this.skuCode = skuCode;
		this.productName = productName;
		this.itemPrice = itemPrice;
		this.actualRetailPrice = actualRetailPrice;
		this.qty = qty;
		this.buyer = buyer;
		this.salesChannel = salesChannel;
		this.fulfillmentCenter = fulfillmentCenter;
		this.refundDtId = refundDtId;
		this.city = city;
		this.state = state;
		this.country = country;
		this.transactionTime = transactionTime;
		this.transactionTimeUTC = transactionTimeUTC;
		this.promotionId = promotionId;
	}

	@Override
	public String toString() {
		return "CustomerOrderExportingImpl [getId()=" + getId() + ", getOrderTimeLocal()=" + getOrderTimeLocal()
				+ ", getOrderTimeUTC()=" + getOrderTimeUTC() + ", getTransactionTimeUTC()=" + getTransactionTimeUTC()
				+ ", getMarketplaceOrderId()=" + getMarketplaceOrderId() + ", getShopifyOrderId()="
				+ getShopifyOrderId() + ", getOrderStatus()=" + getOrderStatus() + ", getSKUCode()=" + getSKUCode()
				+ ", getProductName()=" + getProductName() + ", getItemPrice()=" + getItemPrice()
				+ ", getActualRetailPrice()=" + getActualRetailPrice() + ", getQty()=" + getQty() + ", getBuyer()="
				+ getBuyer() + ", getSalesChannel()=" + getSalesChannel() + ", getFulfillmentCenter()="
				+ getFulfillmentCenter() + ", getPromotionId()=" + getPromotionId() + ", getCity()=" + getCity()
				+ ", getState()=" + getState() + ", getCountry()=" + getCountry() + "]";
	}

	public int getId(){
		return this.id;
	}

	@Override
	public String getOrderTimeLocal(){

		if (this.getSalesChannel() == null) return null;
		SalesChannel salesChannel = SalesChannel.fromDisplayName(this.getSalesChannel());
		String orderTimeLocal ="";

		if(this.orderTime != null && salesChannel != null && salesChannel.getTimeZoneAssigned() != null ){
			orderTimeLocal = DateHelper.toString(this.orderTime, "yyyy-MM-dd HH:mm:ss",salesChannel.getTimeZoneAssigned());

			if(TimeZone.getTimeZone(salesChannel.getTimeZoneAssigned()).inDaylightTime(this.orderTime)){
				return orderTimeLocal+" "+salesChannel.getTimeZonePostFixTextDaylightSaving();
			}else{
				return orderTimeLocal+" "+salesChannel.getTimeZonePostFixTextStandard();
			}
		}

		return orderTimeLocal ;

	}

	@Override
	public String getOrderTimeUTC() {
		return DateHelper.toString(this.orderTime, "yyyy-MM-dd HH:mm:ss", "UTC");
	}
	
	@Override
	public String getTransactionTimeUTC() {

		if(this.orderStatus.equals("Pending")||this.orderStatus.equals("Canceled")) return null;
		return  (this.transactionTime == null && this.transactionTimeUTC == null )?"(TBD)":
				this.transactionTimeUTC != null ? this.transactionTimeUTC :
						LocalDateTime.parse(this.transactionTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"))
								.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	}

	@Override
	public String getMarketplaceOrderId() {
		return this.marketplaceOrderId;
	}

	@Override
	public String getShopifyOrderId() {
		return this.shopifyOrderId;
	}
	
	@Override
	public String getOrderStatus() {
		if(this.refundDtId!=null && this.refundDtId > 0) return "Refunded";
		if(this.orderStatus.equals("COMPLETE")) return StringUtils.capitalize(this.orderStatus.toLowerCase());
		return this.orderStatus;
	}

	@Override
	public String getSKUCode() {
		return this.skuCode;
	}

	@Override
	public String getProductName() {
		return this.productName;
	}

	@Override
	public String getItemPrice() {
		return this.itemPrice==null?"":this.itemPrice.setScale(2).toPlainString();
	}
	
	@Override
	public String getActualRetailPrice() {
		return this.actualRetailPrice==null?"":this.actualRetailPrice.setScale(2).toPlainString();
	}

	@Override
	public String getQty() {
		return this.qty;
	}

	@Override
	public String getBuyer() {
		return this.buyer;
	}

	@Override
	public String getSalesChannel() {
		if(this.isEbayOrder(this.marketplaceOrderId)) return "eBay";
		return this.salesChannel;
	}
	
	private boolean isEbayOrder(String orderId){
		return orderId.toLowerCase().startsWith("ebay")||orderId.toLowerCase().startsWith("sb");
	}
	
	@Override
	public String getFulfillmentCenter() {
		return this.fulfillmentCenter;
	}

	@Override
	public String getPromotionId() {
		return this.promotionId;
	}

	@Override
	public String getCity() {
		return this.city;
	}

	@Override
	public String getState() {
		return this.state;
	}

	@Override
	public String getCountry() {
		return this.country;
	}

}