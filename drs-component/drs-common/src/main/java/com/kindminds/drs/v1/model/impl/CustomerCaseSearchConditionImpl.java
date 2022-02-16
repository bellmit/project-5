package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.customercare.CustomerCaseSearchCondition;

public class CustomerCaseSearchConditionImpl implements CustomerCaseSearchCondition {
	
	private String supplierKcode=null;
	private String customerName=null;
	
	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	@Override
	public String getCustomerName() {
		return this.customerName;
	}

	@Override
	public Boolean isNull() {
		return this.supplierKcode==null
			&& this.customerName==null;
	}

}
