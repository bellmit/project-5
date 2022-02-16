package com.kindminds.drs.api.v1.model.replenishment;

import java.io.Serializable;

public interface ReplenishmentTimeSpent extends Serializable {
	public int getReceivingDays();
	public int getCalculationDays();
	public int getCourierDays();
	public int getAirFreightDays();
	public int getSurfaceFreightDays();
}
