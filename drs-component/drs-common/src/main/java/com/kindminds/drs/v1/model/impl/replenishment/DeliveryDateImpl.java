package com.kindminds.drs.v1.model.impl.replenishment;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.ReplenishmentStatus;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.SkuReplenishmentPredition.DeliveryDate;
import com.kindminds.drs.util.DateHelper;

import java.io.Serializable;
import java.util.Date;

public class DeliveryDateImpl implements DeliveryDate, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1747299205237932438L;
	private Date courierDate;
	private Date airFreightDate;
	private Date surfaceFreightDate;
	private final String dateFormat = "yyyy-MM-dd";
	
	public DeliveryDateImpl(Date courierDate, Date airFreightDate, Date surfaceFreightDate) {
		this.courierDate = courierDate;
		this.airFreightDate = airFreightDate;
		this.surfaceFreightDate = surfaceFreightDate;
	}

	@Override
	public String getCourierDate() {
		return DateHelper.toString(courierDate, dateFormat);
	}

	@Override
	public String getAirFreightDate() {
		return DateHelper.toString(airFreightDate, dateFormat);
	}

	@Override
	public String getSurfaceFreightDate() {
		return DateHelper.toString(surfaceFreightDate, dateFormat);
	}

	@Override
	public ReplenishmentStatus getCourierStatus() {
		return ReplenishmentStatusHelper.buildStatus(this.courierDate);
	}

	@Override
	public ReplenishmentStatus getAirFreightStatus() {
		return ReplenishmentStatusHelper.buildStatus(this.airFreightDate);
	}

	@Override
	public ReplenishmentStatus getSurfaceFreightStatus() {
		return ReplenishmentStatusHelper.buildStatus(this.surfaceFreightDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((airFreightDate == null) ? 0 : airFreightDate.hashCode());
		result = prime * result
				+ ((courierDate == null) ? 0 : courierDate.hashCode());
		result = prime
				* result
				+ ((surfaceFreightDate == null) ? 0 : surfaceFreightDate
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeliveryDateImpl other = (DeliveryDateImpl) obj;
		if (airFreightDate == null) {
			if (other.airFreightDate != null)
				return false;
		} else if (!airFreightDate.equals(other.airFreightDate))
			return false;
		if (courierDate == null) {
			if (other.courierDate != null)
				return false;
		} else if (!courierDate.equals(other.courierDate))
			return false;
		if (surfaceFreightDate == null) {
			if (other.surfaceFreightDate != null)
				return false;
		} else if (!surfaceFreightDate.equals(other.surfaceFreightDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeliveryDateImpl [getCourierDate()=" + getCourierDate()
				+ ", getAirFreightDate()=" + getAirFreightDate()
				+ ", getSurfaceFreightDate()=" + getSurfaceFreightDate()
				+ ", getCourierStatus()=" + getCourierStatus()
				+ ", getAirFreightStatus()=" + getAirFreightStatus()
				+ ", getSurfaceFreightStatus()=" + getSurfaceFreightStatus()
				+ "]";
	}
}
