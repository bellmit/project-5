package com.kindminds.drs.api.data.access.usecase.logistics;

import java.util.List;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;
import com.kindminds.drs.api.v1.model.replenishment.SkuInventoryInfo;

public interface PredictReplenishmentDao {
	public List<SkuInventoryInfo> getSkuInventoryList(int warehouseId, String supplierKCode, Integer spwCaclDays, String skuNumber);
	public ReplenishmentTimeSpent getReplenishmentTimeSpent(int warehouseId);
	public String getNotes();
}
