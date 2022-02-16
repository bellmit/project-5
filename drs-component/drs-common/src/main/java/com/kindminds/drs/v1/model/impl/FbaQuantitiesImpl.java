package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.data.access.usecase.product.ViewKeyProductStatsDao.FbaQuantities;

public class FbaQuantitiesImpl implements FbaQuantities {

	private Integer inBound;
	private Integer inStock;
	private Integer transfer;

	public FbaQuantitiesImpl(Integer inBound, Integer inStock, Integer transfer) {
		super();
		this.inBound = inBound;
		this.inStock = inStock;
		this.transfer = transfer;
	}

	@Override
	public String toString() {
		return "FbaQuantitiesImpl [getInBound()=" + getInBound() + ", getInStock()=" + getInStock() + ", getTransfer()="
				+ getTransfer() + "]";
	}

	@Override
	public Integer getInBound() {
		return this.inBound;
	}

	@Override
	public Integer getInStock() {
		return this.inStock;
	}

	@Override
	public Integer getTransfer() {
		return this.transfer;
	}

}
