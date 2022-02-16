package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.accounting.ServiceItem;

import java.math.BigDecimal;

public class ServiceItemImpl implements ServiceItem {
	
	public int id;	
	public String name;	
	public BigDecimal amount;

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getServiceItemName() {
		return this.name;
	}

	public void setServiceItemName(String name) {
		this.name = name;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
