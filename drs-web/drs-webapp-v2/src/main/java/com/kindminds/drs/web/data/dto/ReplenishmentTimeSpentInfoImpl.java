package com.kindminds.drs.web.data.dto;


import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;

public class ReplenishmentTimeSpentInfoImpl implements ReplenishmentTimeSpentInfo {

	private int warehouseId;
	private String warehouseName; 
	private Integer daysSpentForAmazonReceiving;
	private Integer daysSpentForSpwCalculation;
	private Integer daysSpentForCourier;
	private Integer daysSpentForAirFreight;
	private Integer daysSpentForSurfaceFreight;
	
	public ReplenishmentTimeSpentInfoImpl(){};
	
	public ReplenishmentTimeSpentInfoImpl(ReplenishmentTimeSpentInfo origItem){
		this.warehouseId = origItem.getWarehouseId();
		this.warehouseName = origItem.getWarehouseName();
		this.daysSpentForAmazonReceiving = origItem.getDaysSpentForAmazonReceiving();
		this.daysSpentForSpwCalculation = origItem.getDaysSpentForSpwCalculation();		
		this.daysSpentForCourier = origItem.getDaysSpentForCourier();						
		this.daysSpentForAirFreight = origItem.getDaysSpentForAirFreight(); 		
		this.daysSpentForSurfaceFreight = origItem.getDaysSpentForSurfaceFreight();	
	};
		
	@Override
	public int getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId; 		
	}
	
	@Override
	public String getWarehouseName() {
		return this.warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;		
	}
		
	@Override
	public Integer getDaysSpentForAmazonReceiving() {
		return this.daysSpentForAmazonReceiving;
	}

	public void setDaysSpentForAmazonReceiving(Integer daysSpentForAmazonReceiving) {		
		this.daysSpentForAmazonReceiving = daysSpentForAmazonReceiving;
	}
	
	@Override
	public Integer getDaysSpentForSpwCalculation() {
		return this.daysSpentForSpwCalculation;
	}

	public void setDaysSpentForSpwCalculation(Integer daysSpentForSpwCalculation) {
		this.daysSpentForSpwCalculation = daysSpentForSpwCalculation;		
	}
		
	@Override
	public Integer getDaysSpentForCourier() {
		return this.daysSpentForCourier;
	}

	public void setDaysSpentForCourier(Integer daysSpentForCourier) {
		this.daysSpentForCourier = daysSpentForCourier;				
	}	
	
	@Override
	public Integer getDaysSpentForAirFreight() {
		return this.daysSpentForAirFreight;
	}

	public void setDaysSpentForAirFreight(Integer daysSpentForAirFreight) {
		this.daysSpentForAirFreight = daysSpentForAirFreight; 		
	}
	
	@Override
	public Integer getDaysSpentForSurfaceFreight() {
		return this.daysSpentForSurfaceFreight;
	}

	public void setDaysSpentForSurfaceFreight(Integer daysSpentForSurfaceFreight) {
		this.daysSpentForSurfaceFreight = daysSpentForSurfaceFreight;		
	}
		
}
