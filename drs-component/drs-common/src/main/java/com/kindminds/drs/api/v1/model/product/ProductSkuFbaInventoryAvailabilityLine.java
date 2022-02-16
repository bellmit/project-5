package com.kindminds.drs.api.v1.model.product;

public interface ProductSkuFbaInventoryAvailabilityLine {
	String getSku();
	int getWarehouseId();
	Boolean isAvailable();
}
