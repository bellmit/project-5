package com.kindminds.drs.persist.v1.model.mapping.sales;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;






import com.kindminds.drs.api.v1.model.sales.CustomerOrder;
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;
import org.springframework.util.StringUtils;


import com.kindminds.drs.util.DateHelper;


public class CustomerOrderImpl implements CustomerOrder {
	
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
	//@Column(name="transaction_time")
	private String transactionTime = null;

	private String transactionTimeUTC = null;

	//@Column(name="promotion_Id")
	private String promotionId = null;

	private String buyerEmail = null;

	private String city = null;

	private String state = null;

	private String country = null;

	private String baseCode = null;

	private String companyCode = null;

	private String asin = null;

	/*
	public void setTransactionDate(String transactionDate){
		this.transactionTimeUTC = transactionDate;
	}
	
	public void setPromotionIds(String promotionIds){
		this.promotionId = promotionIds;
	}
	*/

	public CustomerOrderImpl() {
	}


	public CustomerOrderImpl(Long orderTime, String marketplaceOrderId, String shopifyOrderId,
							 String orderStatus, String skuCode, String baseCode, String asin,
							 String companyCode, String productName, Double itemPrice,
							 Double actualRetailPrice, Integer qty, String buyer, String buyerEmail,
							 String salesChannel, String fulfillmentCenter, String refundDtId,
							 Long transactionTimeUTC, String promotionId,
							 String city, String state, String country) {

		this.orderTime = new Date(orderTime);
		this.marketplaceOrderId = marketplaceOrderId;
		this.shopifyOrderId = shopifyOrderId;
		this.orderStatus = orderStatus;
		this.skuCode = skuCode;
		this.baseCode = baseCode;
		this.asin = asin;
		this.companyCode = companyCode;
		this.productName = productName;
		this.itemPrice = itemPrice == null? null : BigDecimal.valueOf(itemPrice);
		this.actualRetailPrice = actualRetailPrice == null ? null : BigDecimal.valueOf(actualRetailPrice);
		this.qty = qty == null? null : Integer.toString(qty);
		this.buyer = buyer;
		this.buyerEmail = buyerEmail;
		this.salesChannel = salesChannel;
		this.fulfillmentCenter = fulfillmentCenter;

		this.refundDtId = StringUtils.hasText(refundDtId) ? Integer.valueOf(refundDtId) : null;
		if (transactionTimeUTC == null || transactionTimeUTC == -2208988800000F) {
			this.transactionTimeUTC = null;
		} else {
			this.transactionTimeUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					new Date(transactionTimeUTC));
		}
		this.promotionId = promotionId;
		this.city = city;
		this.state = state;
		this.country = country;
		if(this.promotionId.length()>30){
			this.shortPromotionId=this.promotionId.substring(0,29)+"...";
		}
		else{
			this.shortPromotionId=this.promotionId;
		}
	}


	public CustomerOrderImpl(int id, Long orderTime, String marketplaceOrderId, String shopifyOrderId,
							 String orderStatus, String skuCode, String productName, BigDecimal itemPrice,
							 BigDecimal actualRetailPrice, String qty, String buyer, String salesChannel,
							 String fulfillmentCenter, Integer refundDtId,
							 Long transactionTimeUTC, String promotionId) {

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

		if (transactionTimeUTC == null || transactionTimeUTC == -2208988800000F) {
			this.transactionTimeUTC = null;
		} else {
			this.transactionTimeUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					new Date(transactionTimeUTC));
		}
		this.promotionId = promotionId;
		if(this.promotionId != null) {
			if (this.promotionId.length() > 30) {
				this.shortPromotionId = this.promotionId.substring(0, 29) + "...";
			} else {
				this.shortPromotionId = this.promotionId;
			}
		}
	}




	@Override
	public String toString() {
		return "CustomerOrderImpl [getId()=" + getId() + ", getOrderTimeUTC(" +
				")=" + getOrderTimeUTC()
				+ ", getTransactionTimeUTC()=" + getTransactionTimeUTC() + ", getMarketplaceOrderId()="
				+ getMarketplaceOrderId() + ", getShopifyOrderId()=" + getShopifyOrderId() + ", getOrderStatus()="
				+ getOrderStatus() + ", getSKUCode()=" + getSKUCode() + ", getProductName()=" + getProductName()
				+ ", getItemPrice()=" + getItemPrice() + ", getActualRetailPrice()=" + getActualRetailPrice()
				+ ", getQty()=" + getQty() + ", getBuyer()=" + getBuyer() + ", getSalesChannel()=" + getSalesChannel()
				+ ", getFulfillmentCenter()=" + getFulfillmentCenter() + ", getPromotionId()=" + getPromotionId() + "]";
	}

	public int getId(){
		return this.id;
	}

	@Override
	public String getOrderTimeLocal(){
		if (this.getSalesChannel() == null) return null;
		SalesChannel salesChannel = SalesChannel.fromDisplayName(this.getSalesChannel());

		if(salesChannel == null) return null;
		if(this.orderTime == null) return  null;
		String orderTimeLocal = DateHelper.toString(this.orderTime, "yyyy-MM-dd HH:mm:ss",salesChannel.getTimeZoneAssigned());
		if(TimeZone.getTimeZone(salesChannel.getTimeZoneAssigned()).inDaylightTime(this.orderTime)){
			return orderTimeLocal+" "+salesChannel.getTimeZonePostFixTextDaylightSaving();
		}
		return orderTimeLocal+" "+salesChannel.getTimeZonePostFixTextStandard();
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
//		System.out.println("orderStatus: " + this.orderStatus + ", refundDtId: " + refundDtId);
		if(refundDtId!=null && refundDtId != 0) return "Refunded";
		if(orderStatus.equals("COMPLETE")) return StringUtils.capitalize(orderStatus.toLowerCase());
		return orderStatus;
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
		return (actualRetailPrice==null || actualRetailPrice.compareTo(BigDecimal.ZERO) == 0) ?
				"" : this.actualRetailPrice.setScale(2).toPlainString();
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

	private String shortPromotionId = "";
	@Override
	public String getShortPromotionId() {
		return this.shortPromotionId;
	}

}
