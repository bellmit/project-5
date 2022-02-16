package com.kindminds.drs.api.v1.model.product;

import java.util.Date;

public interface ProductSkuInventoryHistory {
	int getWarehouseId();
	int getProductSkuId();
	Date getSnapshotDate();
	Integer getQtyInStock();
	Integer getQtySold();
}
