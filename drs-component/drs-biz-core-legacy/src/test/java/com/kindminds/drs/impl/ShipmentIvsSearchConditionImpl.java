package com.kindminds.drs.impl;


import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsSearchCondition;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;

public class ShipmentIvsSearchConditionImpl implements IvsSearchCondition {

	private String sellerCompanyKcode;
	private String destinationCountry;
	private ShippingMethod shippingMethod;
	private ShipmentStatus status;
	
	public ShipmentIvsSearchConditionImpl(
			String sellerCompanyKcode,
			String destinationCountry,
			ShippingMethod shippingMethod,	
			ShipmentStatus status){
		this.sellerCompanyKcode = sellerCompanyKcode;
		this.destinationCountry = destinationCountry; 
		this.shippingMethod = shippingMethod;
		this.status = status;			
	}
	
	@Override
	public String toString() {
		return "ListShipmentOfInventoryConditionImpl [getSellerCompanyKcode()=" + getSellerCompanyKcode()
				+ ", getDestinationCountry()=" + getDestinationCountry() + ", getShippingMethod()="
				+ getShippingMethod() + ", getStatus()=" + getStatus() + "]";
	}

	@Override
	public String getSellerCompanyKcode() {
		return this.sellerCompanyKcode;
	}

	@Override
	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	@Override
	public ShippingMethod getShippingMethod() {
		return this.shippingMethod;
	}

	@Override
	public ShipmentStatus getStatus() {
		return this.status;
	}

}
