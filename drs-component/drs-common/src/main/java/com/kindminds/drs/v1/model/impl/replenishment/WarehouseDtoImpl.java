package com.kindminds.drs.v1.model.impl.replenishment;

import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.WarehouseDto;

import java.io.Serializable;

public class WarehouseDtoImpl implements WarehouseDto, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3536302200501594893L;
	private Warehouse warehouse;
	
	public WarehouseDtoImpl(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@Override
	public int getKey() {
		return warehouse.getKey();
	}

	@Override
	public String getName() {
		return warehouse.getDisplayName();
	}

	@Override
	public Warehouse getWarehouse() {
		return this.warehouse;
	}

}
