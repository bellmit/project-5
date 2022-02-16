package com.kindminds.drs.v1.model.impl.replenishment;


import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;

public class ReplenishmentTimeSpentImpl implements ReplenishmentTimeSpent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2550235666875256040L;
	
	private int receivingDays;
	private int calculationDays;
	private int courierDays;
	private int airFreightDays;
	private int surfaceFreightDays;

	public ReplenishmentTimeSpentImpl(int receivingDays, int calculationDays, int courierDays,
			int airFreightDays, int surfaceFreightDays) {
		super();
		this.receivingDays = receivingDays;
		this.calculationDays = calculationDays; 
		this.courierDays = courierDays;
		this.airFreightDays = airFreightDays;
		this.surfaceFreightDays = surfaceFreightDays;
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
