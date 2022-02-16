package com.kindminds.drs.persist.v1.model.mapping.replenishment;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;








public class ReplenishmentTimeSpentImpl implements ReplenishmentTimeSpent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6488775178988683361L;
	
	//@Id////@Column(name="id")
	private int id;
	
	//@Column(name="days_spent_for_amazon_receiving")
	private int receivingDays;
	
	//@Column(name="days_spent_for_spw_calculation")
	private int calculationDays; 
		
	//@Column(name="days_spent_for_courier")
	private int courierDays;
	
	//@Column(name="days_spent_for_air_freight")
	private int airFreightDays;
	
	//@Column(name="days_spent_for_surface_freight")
	private int surfaceFreightDays;

	public ReplenishmentTimeSpentImpl(int id, int receivingDays, int calculationDays, int courierDays, int airFreightDays, int surfaceFreightDays) {
		this.id = id;
		this.receivingDays = receivingDays;
		this.calculationDays = calculationDays;
		this.courierDays = courierDays;
		this.airFreightDays = airFreightDays;
		this.surfaceFreightDays = surfaceFreightDays;
	}

	public ReplenishmentTimeSpentImpl() {
	}

	@Override
	public int getReceivingDays() {
		return this.receivingDays;
	}

	@Override
	public int getCalculationDays() {
		return this.calculationDays;
	}
	
	@Override
	public int getCourierDays() {
		return this.courierDays;
	}

	@Override
	public int getAirFreightDays() {
		return this.airFreightDays;
	}

	@Override
	public int getSurfaceFreightDays() {
		return this.surfaceFreightDays;
	}

}
