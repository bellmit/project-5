package com.kindminds.drs.persist.v1.model.mapping.amazon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;






import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;


public class AmazonOrderImpl implements AmazonOrder {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="amazon_order_id")
	private String amazonOrderId;
	//@Column(name="seller_order_id")
	private String sellerOrderId;
	//@Column(name="purchase_date")
	private Date purchaseDate;
	//@Column(name="last_update_date")
	private Date lastUpdateDate;
	//@Column(name="order_status")
	private String orderStatus;
	//@Column(name="fulfillment_channel")
	private String fulfillmentChannel;
	//@Column(name="sales_channel")
	private String salesChannel;
	//@Column(name="number_of_items_shipped")
	private Integer numberOfItemsShipped;
	//@Column(name="number_of_items_unshipped")
	private Integer numberOfItemsUnshipped;

	private List<AmazonOrderItem> items;

	public AmazonOrderImpl() {
	}

	public AmazonOrderImpl(int id, String amazonOrderId, String sellerOrderId, Date purchaseDate, Date lastUpdateDate, String orderStatus, String fulfillmentChannel, String salesChannel, Integer numberOfItemsShipped, Integer numberOfItemsUnshipped) {
		this.id = id;
		this.amazonOrderId = amazonOrderId;
		this.sellerOrderId = sellerOrderId;
		this.purchaseDate = purchaseDate;
		this.lastUpdateDate = lastUpdateDate;
		this.orderStatus = orderStatus;
		this.fulfillmentChannel = fulfillmentChannel;
		this.salesChannel = salesChannel;
		this.numberOfItemsShipped = numberOfItemsShipped;
		this.numberOfItemsUnshipped = numberOfItemsUnshipped;
	}

	public int getDbId(){
		return this.id;
	}
	
	public void setItems(List<AmazonOrderItem> items){
		this.items = items;
	}

	@Override
	public String getAmazonOrderId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getPurchaseDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastUpdateDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrderStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFulfillmentChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSalesChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNumberOfItemsShipped() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNumberOfItemsUnshipped() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSellerOrderId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShipServiceLevel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPaymentMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMarketplaceId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBuyerEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBuyerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShipmentServiceLevelCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrderType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getEarliestShipDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLatestShipDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTFMShipmentStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrderChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressCountryCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressLine1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressLine2() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressLine3() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressStateOrRegion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressPostalCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressPhone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressCity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressCounty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressDistrict() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAddressName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getOrderTotalCurrencyCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getOrderTotalAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getShippedByAmazonTFM() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCbaDisplayableShippingLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getEarliestDeliveryDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLatestDeliveryDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AmazonOrderPaymentExecDetail> getPaymentExecutionDetail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AmazonOrderItem> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

}
