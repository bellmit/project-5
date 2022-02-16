package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.product.ProductSkuFbaInventoryAvailabilityLine;

public class ProductSkuFbaInventoryAvailabilityLineImpl implements ProductSkuFbaInventoryAvailabilityLine {
	
	private String sku;
	private int warehouseId;
	private Boolean isAvailable;

	public ProductSkuFbaInventoryAvailabilityLineImpl(String sku, int warehouseId, Boolean isAvailable) {
		this.sku = sku;
		this.warehouseId = warehouseId;
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "ProductSkuFbaInventoryAvailabilityLineImpl [getSku()=" + getSku() + ", getWarehouseId()="
				+ getWarehouseId() + ", isAvailable()=" + isAvailable() + "]";
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public int getWarehouseId() {
		return this.warehouseId;
	}

	@Override
	public Boolean isAvailable() {
		return this.isAvailable;
	}

}
