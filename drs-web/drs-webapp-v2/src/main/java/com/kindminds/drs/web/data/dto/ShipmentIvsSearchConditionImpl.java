package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsSearchCondition;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import org.apache.commons.lang.StringUtils;


public class ShipmentIvsSearchConditionImpl implements IvsSearchCondition {

	private String sellerCompanyKcode;
	private String destinationCountry;
	private ShippingMethod shippingMethod;
	private ShipmentStatus status;
	
	public ShipmentIvsSearchConditionImpl(){};
	
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
	public String getSellerCompanyKcode() {	
		return StringUtils.isEmpty(this.sellerCompanyKcode)?null:this.sellerCompanyKcode;
	}

	public void setSellerCompanyKcode(String sellerCompanyKcode) {
		this.sellerCompanyKcode = sellerCompanyKcode;
	}

	@Override
	public String getDestinationCountry() {
		return StringUtils.isEmpty(this.destinationCountry)?null:this.destinationCountry;		
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	
	@Override
	public ShippingMethod getShippingMethod() {
		return this.shippingMethod;				
	}
	
	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
		
	@Override
	public ShipmentStatus getStatus() {
		return this.status;						
	}

	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}
	
}