package com.kindminds.drs.persist.v1.model.mapping.amazon;

import java.util.Date;
import java.util.List;






import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction;
import com.kindminds.drs.enums.AmazonTransactionType;


public class AmazonTransactionImpl implements AmazonTransaction {
	
	//@Id ////@Column(name="id")
	private int dbId;
	//@Column(name="order_id")
	private String orderId;
	//@Column(name="merchant_order_id")
	private String merchantOrderId;
	//@Column(name="transaction_type")
	private String type;
	//@Column(name="marketplace_name")
	private String marketplaceName;
	//@Column(name="posted_date_time")
	private Date postedDateTime;

	private List<AmazonTransactionLineItem> lineItems=null;
	
	public void setLineItems(List<AmazonTransactionLineItem> lineItems) {
		this.lineItems=lineItems;
	}
	
	@Override
	public String getOrderId() {
		return this.orderId;
	}
	
	@Override
	public String getMerchantOrderId() {
		return this.merchantOrderId;
	}
	
	@Override
	public String getMarketplaceName() {
		return this.marketplaceName;
	}
	
	@Override
	public AmazonTransactionType getType() {
		return AmazonTransactionType.fromValue(this.type);
	}
	
	@Override
	public Date getPostedDateTime() {
		return this.postedDateTime;
	}
	
	@Override
	public List<AmazonTransactionLineItem> getLineItems() {
		return this.lineItems;
	}
	
	@Override
	public String toString() {
		return "AmazonTransactionImpl [getOrderId()=" + getOrderId()
				+ ", getMerchantOrderId()=" + getMerchantOrderId()
				+ ", getMarketplaceName()=" + getMarketplaceName()
				+ ", getType()=" + getType() + ", getPostedDateTime()="
				+ getPostedDateTime() + ", getLineItems()=" + getLineItems()
				+ "]";
	}
	
}
