package com.kindminds.drs.adapter.amazon.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.FulfillmentOrder;
import com.amazonservices.mws.orders._2013_09_01.model.Order;
import com.amazonservices.mws.orders._2013_09_01.model.PaymentExecutionDetailItem;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;
import com.kindminds.drs.util.DateHelper;

public class AmazonOrderImpl implements AmazonOrder {
	
	private final String longDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ssZ";

	private String amazonOrderId=null;
	private Date purchaseDate=null;
	private Date lastUpdateDate=null;
	private String orderStatus=null;
	private String fulfillmentChannel=null;
	private String salesChannel=null;
	private Integer numberOfItemsShipped=null;
	private Integer numberOfItemsUnshipped=null;
	private String sellerOrderId=null;
	private String shipServiceLevel=null;
	private String paymentMethod=null;
	private String marketplaceId=null;
	private String buyerEmail=null;
	private String buyerName=null;
	private String shipmentServiceLevelCategory=null;
	private String orderType=null;
	private Date earliestShipDate=null;
	private Date latestShipDate=null;
	private String tFMShipmentStatus=null;
	private String orderChannel=null;
	private String addressCountryCode=null;
	private String addressLine1=null;
	private String addressLine2=null;
	private String addressLine3=null;
	private String addressStateOrRegion=null;
	private String addressPostalCode=null;
	private String addressPhone=null;
	private String addressCity=null;
	private String addressCounty=null;
	private String addressDistrict=null;
	private String addressName=null;
	private Currency orderTotalCurrencyCode=null;
	private BigDecimal orderTotalAmount=null;
	private Boolean shippedByAmazonTFM=null;
	private String cbaDisplayableShippingLabel=null;
	private Date earliestDeliveryDate=null;
	private Date latestDeliveryDate=null;
	private List<AmazonOrderItem> items = null;
	private List<AmazonOrderPaymentExecDetail> paymentDetails = null;
	
	public AmazonOrderImpl(FulfillmentOrder fulfillmentOrder){
		this.amazonOrderId=fulfillmentOrder.getSellerFulfillmentOrderId();
		this.marketplaceId=fulfillmentOrder.getMarketplaceId();
		this.sellerOrderId=fulfillmentOrder.getDisplayableOrderId();
		this.purchaseDate=fulfillmentOrder.getDisplayableOrderDateTime().toGregorianCalendar().getTime();

		this.lastUpdateDate = Calendar.getInstance().getTime();

//		DisplayableOrderComment 
//		ShippingSpeedCategory 
//		DeliveryWindow
//		DestinationAddress 
//		FulfillmentAction
//		FulfillmentPolicy 
//		ReceivedDateTime 
		this.orderStatus= fulfillmentOrder.getFulfillmentOrderStatus(); 
//		StatusUpdatedDateTime 
//		NotificationEmailList 
//		CODSettings
	}

	private Date formatDate(String dateString) {
		if (dateString.length() == 28) {	// Ex. "2018-09-11T17:12:12.663+0000"
			return DateHelper.toDate(dateString,this.longDateTimeFormat);
		} else {	// Ex. "2018-09-11T17:12:12+0000"
			return DateHelper.toDate(dateString,this.dateTimeFormat);
		}
	}

	public AmazonOrderImpl(Order orig){
		this.amazonOrderId=orig.getAmazonOrderId();
		this.purchaseDate=formatDate(this.replaceZwithTimeZone(orig.getPurchaseDate().toString()));
		this.lastUpdateDate=formatDate(this.replaceZwithTimeZone(orig.getLastUpdateDate().toString()));
		this.orderStatus=orig.getOrderStatus();
		this.fulfillmentChannel=orig.getFulfillmentChannel();
		this.salesChannel=orig.getSalesChannel();
		this.numberOfItemsShipped=orig.getNumberOfItemsShipped();
		this.numberOfItemsUnshipped=orig.getNumberOfItemsUnshipped();
		this.sellerOrderId=orig.getSellerOrderId();
		this.shipServiceLevel=orig.getShipServiceLevel();
		this.paymentMethod=orig.getPaymentMethod();
		this.marketplaceId=orig.getMarketplaceId();
		this.buyerEmail=orig.getBuyerEmail();
		this.buyerName=orig.getBuyerName();
		this.shipmentServiceLevelCategory=orig.getShipmentServiceLevelCategory();
		this.orderType=orig.getOrderType();
		this.earliestShipDate=orig.getEarliestShipDate()==null?null:formatDate(this.replaceZwithTimeZone(orig.getEarliestShipDate().toString()));
		this.latestShipDate=orig.getLatestShipDate()==null?null:formatDate(this.replaceZwithTimeZone(orig.getLatestShipDate().toString()));
		this.tFMShipmentStatus=orig.getTFMShipmentStatus();
		this.orderChannel=orig.getOrderChannel();
		this.addressCountryCode=orig.getShippingAddress()==null?null:orig.getShippingAddress().getCountryCode();
		this.addressLine1=orig.getShippingAddress()==null?null:orig.getShippingAddress().getAddressLine1();
		this.addressLine2=orig.getShippingAddress()==null?null:orig.getShippingAddress().getAddressLine2();
		this.addressLine3=orig.getShippingAddress()==null?null:orig.getShippingAddress().getAddressLine3();
		this.addressStateOrRegion=orig.getShippingAddress()==null?null:orig.getShippingAddress().getStateOrRegion();
		this.addressPostalCode=orig.getShippingAddress()==null?null:orig.getShippingAddress().getPostalCode();
		this.addressPhone=orig.getShippingAddress()==null?null:orig.getShippingAddress().getPhone();
		this.addressCity=orig.getShippingAddress()==null?null:orig.getShippingAddress().getCity();
		this.addressCounty=orig.getShippingAddress()==null?null:orig.getShippingAddress().getCounty();
		this.addressDistrict=orig.getShippingAddress()==null?null:orig.getShippingAddress().getDistrict();
		this.addressName=orig.getShippingAddress()==null?null:orig.getShippingAddress().getName();
		this.orderTotalCurrencyCode=orig.getOrderTotal()==null?null:Currency.valueOf(orig.getOrderTotal().getCurrencyCode());
		this.orderTotalAmount=orig.getOrderTotal()==null?null:new BigDecimal(orig.getOrderTotal().getAmount());
		this.shippedByAmazonTFM=orig.getShippedByAmazonTFM();
		this.cbaDisplayableShippingLabel=orig.getCbaDisplayableShippingLabel();
		this.earliestDeliveryDate=orig.getEarliestDeliveryDate()==null?null:formatDate(this.replaceZwithTimeZone(orig.getEarliestDeliveryDate().toString()));
		this.latestDeliveryDate=orig.getLatestDeliveryDate()==null?null:formatDate(this.replaceZwithTimeZone(orig.getLatestDeliveryDate().toString()));
		this.paymentDetails = this.toPaymentDetailsDto(orig.getPaymentExecutionDetail());
	}
	
	private String replaceZwithTimeZone(String originalDateStr){
		return originalDateStr.replaceAll("Z$", "+0000"); }
	
	@Override
	public String toString() {
		return "AmazonOrderImpl [getAmazonOrderId()=" + getAmazonOrderId()
				+ ", getPurchaseDate()=" + getPurchaseDate()
				+ ", getLastUpdateDate()=" + getLastUpdateDate()
				+ ", getOrderStatus()=" + getOrderStatus()
				+ ", getFulfillmentChannel()=" + getFulfillmentChannel()
				+ ", getSalesChannel()=" + getSalesChannel()
				+ ", getNumberOfItemsShipped()=" + getNumberOfItemsShipped()
				+ ", getNumberOfItemsUnshipped()="
				+ getNumberOfItemsUnshipped() + ", getSellerOrderId()="
				+ getSellerOrderId() + ", getShipServiceLevel()="
				+ getShipServiceLevel() + ", getPaymentMethod()="
				+ getPaymentMethod() + ", getMarketplaceId()="
				+ getMarketplaceId() + ", getBuyerEmail()=" + getBuyerEmail()
				+ ", getBuyerName()=" + getBuyerName()
				+ ", getShipmentServiceLevelCategory()="
				+ getShipmentServiceLevelCategory() + ", getOrderType()="
				+ getOrderType() + ", getEarliestShipDate()="
				+ getEarliestShipDate() + ", getLatestShipDate()="
				+ getLatestShipDate() + ", getTFMShipmentStatus()="
				+ getTFMShipmentStatus() + ", getOrderChannel()="
				+ getOrderChannel() + ", getAddressCountryCode()="
				+ getAddressCountryCode() + ", getAddressLine1()="
				+ getAddressLine1() + ", getAddressLine2()="
				+ getAddressLine2() + ", getAddressLine3()="
				+ getAddressLine3() + ", getAddressStateOrRegion()="
				+ getAddressStateOrRegion() + ", getAddressPostalCode()="
				+ getAddressPostalCode() + ", getAddressPhone()="
				+ getAddressPhone() + ", getAddressCity()=" + getAddressCity()
				+ ", getAddressCounty()=" + getAddressCounty()
				+ ", getAddressDistrict()=" + getAddressDistrict()
				+ ", getAddressName()=" + getAddressName()
				+ ", getOrderTotalCurrencyCode()="
				+ getOrderTotalCurrencyCode() + ", getOrderTotalAmount()="
				+ getOrderTotalAmount() + ", getShippedByAmazonTFM()="
				+ getShippedByAmazonTFM()
				+ ", getCbaDisplayableShippingLabel()="
				+ getCbaDisplayableShippingLabel()
				+ ", getEarliestDeliveryDate()=" + getEarliestDeliveryDate()
				+ ", getLatestDeliveryDate()=" + getLatestDeliveryDate()
				+ ", getPaymentExecutionDetail()="
				+ getPaymentExecutionDetail() + ", getItems()=" + getItems()
				+ "]";
	}



	private List<AmazonOrderPaymentExecDetail> toPaymentDetailsDto(List<PaymentExecutionDetailItem> items){
		ArrayList<AmazonOrderPaymentExecDetail> listToReturn = new ArrayList<AmazonOrderPaymentExecDetail>();
		for(PaymentExecutionDetailItem item:items){
			listToReturn.add(new AmazonOrderPaymentExecDetailImpl(item));
		}
		return listToReturn;
	}

	public void setItems(List<AmazonOrderItem> items){
		this.items = items;
	}
	
	public void setPaymentDetails(List<AmazonOrderPaymentExecDetail> details){
		this.paymentDetails = details;
	}

	@Override
	public String getAmazonOrderId() {
		return this.amazonOrderId;
	}

	@Override
	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	@Override
	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	@Override
	public String getOrderStatus() {
		return this.orderStatus;
	}

	@Override
	public String getFulfillmentChannel() {
		return this.fulfillmentChannel;
	}

	@Override
	public String getSalesChannel() {
		return this.salesChannel;
	}

	@Override
	public Integer getNumberOfItemsShipped() {
		return this.numberOfItemsShipped;
	}

	@Override
	public Integer getNumberOfItemsUnshipped() {
		return this.numberOfItemsUnshipped;
	}

	@Override
	public String getSellerOrderId() {
		return this.sellerOrderId;
	}

	@Override
	public String getShipServiceLevel() {
		return this.shipServiceLevel;
	}

	@Override
	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	@Override
	public String getMarketplaceId() {
		return this.marketplaceId;
	}

	@Override
	public String getBuyerEmail() {
		return this.buyerEmail;
	}

	@Override
	public String getBuyerName() {
		return this.buyerName;
	}

	@Override
	public String getShipmentServiceLevelCategory() {
		return this.shipmentServiceLevelCategory;
	}

	@Override
	public String getOrderType() {
		return this.orderType;
	}

	@Override
	public Date getEarliestShipDate() {
		return this.earliestShipDate;
	}

	@Override
	public Date getLatestShipDate() {
		return this.latestShipDate;
	}

	@Override
	public String getTFMShipmentStatus() {
		return this.tFMShipmentStatus;
	}

	@Override
	public String getOrderChannel() {
		return this.orderChannel;
	}

	@Override
	public String getAddressCountryCode() {
		return this.addressCountryCode;
	}

	@Override
	public String getAddressLine1() {
		return this.addressLine1;
	}

	@Override
	public String getAddressLine2() {
		return this.addressLine2;
	}

	@Override
	public String getAddressLine3() {
		return this.addressLine3;
	}

	@Override
	public String getAddressStateOrRegion() {
		return this.addressStateOrRegion;
	}

	@Override
	public String getAddressPostalCode() {
		return this.addressPostalCode;
	}

	@Override
	public String getAddressPhone() {
		return this.addressPhone;
	}

	@Override
	public String getAddressCity() {
		return this.addressCity;
	}

	@Override
	public String getAddressCounty() {
		return this.addressCounty;
	}

	@Override
	public String getAddressDistrict() {
		return this.addressDistrict;
	}

	@Override
	public String getAddressName() {
		return this.addressName;
	}

	@Override
	public Currency getOrderTotalCurrencyCode() {
		return this.orderTotalCurrencyCode;
	}

	@Override
	public BigDecimal getOrderTotalAmount() {
		return this.orderTotalAmount;
	}

	@Override
	public Boolean getShippedByAmazonTFM() {
		return this.shippedByAmazonTFM;
	}

	@Override
	public String getCbaDisplayableShippingLabel() {
		return this.cbaDisplayableShippingLabel;
	}

	@Override
	public Date getEarliestDeliveryDate() {
		return this.earliestDeliveryDate;
	}

	@Override
	public Date getLatestDeliveryDate() {
		return this.latestDeliveryDate;
	}

	@Override
	public List<AmazonOrderPaymentExecDetail> getPaymentExecutionDetail() {
		return this.paymentDetails;
	}

	@Override
	public List<AmazonOrderItem> getItems() {
		return this.items;
	}


}
