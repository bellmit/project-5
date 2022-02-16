package com.kindminds.drs.api.usecase.inventory;

public interface UpdateProductSkuInventoryHistoryUco {
	void update();
	void update(Integer year, Integer month, Integer day);
}
