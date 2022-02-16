package com.kindminds.drs.impl;


import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;

public class ReplenishmentTimeSpentInfoImpl implements ReplenishmentTimeSpentInfo {

	private int warehouseId;
	private String warehouseName;
	private Integer daysSpentForAmazonReceiving;
	private Integer daysSpentForSpwCalculation;
	private Integer daysSpentForCourier;
	private Integer daysSpentForAirFreight;
	private Integer daysSpentForSurfaceFreight;
	
	public ReplenishmentTimeSpentInfoImpl(
			int warehouseId,
			String warehouseName,
			Integer daysSpentForAmazonReceiving,
			Integer daysSpentForSpwCalculation,
			Integer daysSpentForCourier,
			Integer daysSpentForAirFreight,
			Integer daysSpentForSurfaceFreight){
		this.warehouseId = warehouseId;
		this.warehouseName = warehouseName;
		this.daysSpentForAmazonReceiving = daysSpentForAmazonReceiving;
		this.daysSpentForSpwCalculation = daysSpentForSpwCalculation;
		this.daysSpentForCourier = daysSpentForCourier;
		this.daysSpentForAirFreight = daysSpentForAirFreight;
		this.daysSpentForSurfaceFreight = daysSpentForSurfaceFreight;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ReplenishmentTimeSpentInfo){
			ReplenishmentTimeSpentInfo info = (ReplenishmentTimeSpentInfo)obj;
			return this.getWarehouseId()==info.getWarehouseId()
				&& this.getWarehouseName().equals(info.getWarehouseName())
				&& this.getDaysSpentForAmazonReceiving().equals(info.getDaysSpentForAmazonReceiving())
				&& this.getDaysSpentForSpwCalculation().equals(info.getDaysSpentForSpwCalculation())
				&& this.getDaysSpentForCourier().equals(info.getDaysSpentForCourier())
				&& this.getDaysSpentForAirFreight().equals(info.getDaysSpentForAirFreight())
				&& this.getDaysSpentForSurfaceFreight().equals(info.getDaysSpentForSurfaceFreight());
		}
		return false;
	}

	@Override
	public String toString() {
		return "ReplenishmentTimeSpentInfoImpl [getWarehouseId()=" + getWarehouseId() + ", getWarehouseName()="
				+ getWarehouseName() + ", getDaysSpentForAmazonReceiving()=" + getDaysSpentForAmazonReceiving()
				+ ", getDaysSpentForSpwCalculation()=" + getDaysSpentForSpwCalculation() + ", getDaysSpentForCourier()="
				+ getDaysSpentForCourier() + ", getDaysSpentForAirFreight()=" + getDaysSpentForAirFreight()
				+ ", getDaysSpentForSurfaceFreight()=" + getDaysSpentForSurfaceFreight() + "]";
	}

	@Override
	public int getWarehouseId() {
		return this.warehouseId;
	}
	
	@Override
	public String getWarehouseName() {
		return this.warehouseName;
	}
	
	@Override
	public Integer getDaysSpentForAmazonReceiving() {
		return this.daysSpentForAmazonReceiving;
	}
	
	@Override
	public Integer getDaysSpentForSpwCalculation() {
		return this.daysSpentForSpwCalculation;
	}
	
	@Override
	public Integer getDaysSpentForCourier() {
		return this.daysSpentForCourier;
	}
	
	@Override
	public Integer getDaysSpentForAirFreight() {
		return this.daysSpentForAirFreight;
	}
	
	@Override
	public Integer getDaysSpentForSurfaceFreight() {
		return this.daysSpentForSurfaceFreight;
	}
}
