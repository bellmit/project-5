package com.kindminds.drs.impl;


import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;

public class BaseProductOnboardingDetailImplForTest extends ProductDetail {
		
	private String productBaseCode;
	private String supplierKcode;
	private String data;
	private String status;
	
	public BaseProductOnboardingDetailImplForTest(
			String productBaseCode,
			String supplierKcode,
			String data,
			String status){
		this.productBaseCode = productBaseCode;
		this.supplierKcode = supplierKcode;
		this.data = data;
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "BaseProductOnboardingDetailImpl [getProductBaseCode()=" + getProductBaseCode()
				+ ", getSupplierKcode()=" + getSupplierKcode() + ", getData()=" + getData() + ", getStatus()="
				+ getStatus() + "]";
	}

	@Override
	public boolean equals(Object obj){
		/*
		if(obj instanceof ProductDetail){
			ProductDetail baseProductOnboardingDetail = (ProductDetail) obj;
			return this.getProductBaseCode().equals(baseProductOnboardingDetail.getProductBaseCode())			
			&& TestUtil.nullableEquals(this.getSupplierKcode(),baseProductOnboardingDetail.getSupplierKcode())			
			&& TestUtil.nullableEquals(this.getData(),baseProductOnboardingDetail.getData())			
			&& TestUtil.nullableEquals(this.getStatus(),baseProductOnboardingDetail.getStatus());			
		}	
		*/	
		return false;
	}
	
	@Override
	public String getProductBaseCode() {
		return this.productBaseCode;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	@Override
	public String getData() {
		return this.data;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCountry() {
		// TODO Auto-generated method stub
		return null;
	}

}