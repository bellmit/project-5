package com.kindminds.drs.api.usecase.logistics;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.WarehouseDto;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.SkuReplenishmentPredition;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.Supplier;

public interface PredictReplenishmentUco {
	public List<WarehouseDto> getMarketplaceList();
	public String initial();
	public ReplenishmentTimeSpent retrieveWarehouseDescription(Warehouse warehouse);
	public Map<Supplier, List<SkuReplenishmentPredition>> retrieveSupplierSku(Warehouse warehouse);
	public Map<Supplier, List<SkuReplenishmentPredition>> calculate(Warehouse warehouse, int calculatePeriod);
	public SkuReplenishmentPredition calculateManually(String sku, Warehouse warehouse, int calculatePeriod, BigDecimal quantity, int leadTime);
}
