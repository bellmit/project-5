package com.kindminds.drs.persist.v1.model.mapping.customercare;





import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseOrderInfo;


public class CustomerCareCaseOrderInfoImpl implements CustomerCareCaseOrderInfo {
	
	//@Id ////@Column(name="id")
	private int id;	
	//@Column(name="marketpace_id")
	private Integer marketpaceId;
	//@Column(name="sales_channel")
	private String salesChannel;	
	//@Column(name="order_date")
	private String orderDate;
	//@Column(name="buyer_name")
	private String buyerName;

	public CustomerCareCaseOrderInfoImpl() {
	}

	public CustomerCareCaseOrderInfoImpl(int id, Integer marketpaceId,
										 String salesChannel, String orderDate, String buyerName) {
		this.id = id;
		this.marketpaceId = marketpaceId;
		this.salesChannel = salesChannel;
		this.orderDate = orderDate;
		this.buyerName = buyerName;
	}

	@Override
	public String toString() {
		return "CustomerCareCaseOrderInfoImpl [getMarketpaceId()=" + getMarketpaceId() + ", getSalesChannel()="
				+ getSalesChannel() + ", getOrderDate()=" + getOrderDate() + ", getCustomerName()=" + getCustomerName()
				+ "]";
	}

	@Override
	public Integer getMarketpaceId() {
		return this.marketpaceId;
	}
	
	@Override
	public String getSalesChannel() {
		return this.salesChannel;
	}
		
	@Override
	public String getOrderDate() {
		return this.orderDate;
	}

	@Override
	public String getCustomerName() {
		return this.buyerName;
	}

}