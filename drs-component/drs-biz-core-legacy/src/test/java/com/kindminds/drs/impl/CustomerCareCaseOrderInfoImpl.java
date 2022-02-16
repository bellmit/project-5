package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseOrderInfo;

public class CustomerCareCaseOrderInfoImpl implements CustomerCareCaseOrderInfo{
	
	private Integer marketpaceId;
	private String salesChannel;
	private String orderDate;
	private String customerName;
	
	public CustomerCareCaseOrderInfoImpl(Integer marketpaceId,String salesChannel,String date, String customerName){
		this.marketpaceId = marketpaceId;
		this.salesChannel = salesChannel;
		this.orderDate = date;
		this.customerName = customerName;
		}
	
	public boolean equals(Object obj){
		if(obj instanceof CustomerCareCaseOrderInfo){
			CustomerCareCaseOrderInfo order = (CustomerCareCaseOrderInfo)obj;
			return this.getMarketpaceId().equals(order.getMarketpaceId())
				&& this.getSalesChannel().equals(order.getSalesChannel())
				&& this.getOrderDate().equals(order.getOrderDate())
				&& this.getCustomerName().equals(order.getCustomerName());				
		}
		return false;
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
		return this.customerName;
	}

}
