package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsSearchCondition;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;

public class ShipmentIvsSearchConditionImplSvc implements IvsSearchCondition {

	private IvsSearchCondition origin = null;
	private String sellerCompanyKcode;
	
	public ShipmentIvsSearchConditionImplSvc(IvsSearchCondition origin, String userCompanyKcode){
		this.origin=origin;
		this.sellerCompanyKcode = userCompanyKcode;
	}
	
	@Override
	public String toString() {
		return "ListShipmentOfInventoryConditionImplSvc [getSellerCompanyKcode()=" + getSellerCompanyKcode()
				+ ", getDestinationCountry()=" + getDestinationCountry() + ", getShippingMethod()="
				+ getShippingMethod() + ", getStatus()=" + getStatus() + "]";
	}

	@Override
	public String getSellerCompanyKcode() {
		return this.sellerCompanyKcode;
	}

	@Override
	public String getDestinationCountry() {
		return this.origin.getDestinationCountry();
	}

	@Override
	public ShippingMethod getShippingMethod() {
		return this.origin.getShippingMethod();
	}

	@Override
	public ShipmentStatus getStatus() {
		return this.origin.getStatus();
	}
	
}