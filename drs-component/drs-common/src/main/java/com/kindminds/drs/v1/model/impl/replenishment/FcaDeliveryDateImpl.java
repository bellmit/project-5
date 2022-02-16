package com.kindminds.drs.v1.model.impl.replenishment;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.ReplenishmentStatus;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.SkuReplenishmentPredition.FcaDeliveryDate;
import com.kindminds.drs.util.DateHelper;

import java.io.Serializable;
import java.util.Date;

public class FcaDeliveryDateImpl implements FcaDeliveryDate, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1714409448776041483L;
	private Date courierDate;
	private Date airFreightDate;
	private Date surfaceFreightDate;
	private Date earlySelloutDate;
	private Date lateSelloutDate;
	private Date courierEstimatedReceivingCompletionDate;
	private Date airFreightEstimatedReceivingCompletionDate;
	private Date surfaceFreightEstimatedReceivingCompletionDate;
	private final String dateFormat = "yyyy-MM-dd";
	
	public FcaDeliveryDateImpl(Date courierDate, Date airFreightDate, Date surfaceFreightDate,
			Date earlySelloutDate,
			Date lateSelloutDate,
			Date courierEstimatedReceivingCompletionDate,
			Date airFreightEstimatedReceivingCompletionDate,
			Date surfaceFreightEstimatedReceivingCompletionDate) {
		this.courierDate = courierDate;
		this.airFreightDate = airFreightDate;
		this.surfaceFreightDate = surfaceFreightDate;
		this.earlySelloutDate = earlySelloutDate;
		this.lateSelloutDate = lateSelloutDate;
		this.courierEstimatedReceivingCompletionDate = courierEstimatedReceivingCompletionDate;
		this.airFreightEstimatedReceivingCompletionDate = airFreightEstimatedReceivingCompletionDate;
		this.surfaceFreightEstimatedReceivingCompletionDate = surfaceFreightEstimatedReceivingCompletionDate;
	}
	
	@Override
	public String getCourierDate() {		
		return DateHelper.toString(courierDate, dateFormat);
	}

	@Override
	public ReplenishmentStatus getCourierStatus() {		
		return ReplenishmentStatusHelper.buildStatus(this.courierDate);
	}

	@Override
	public Boolean getCourierSelloutRisk() {
		return this.courierEstimatedReceivingCompletionDate.after(this.earlySelloutDate);
	}

	@Override
	public String getCourierMaxOutOfStockDays() {
		if(!this.getCourierSelloutRisk()) return null;
		return ReplenishmentStatusHelper.getMaxOutOfStockDays(this.courierEstimatedReceivingCompletionDate, this.earlySelloutDate);
	}

	@Override
	public String getCourierMinOutOfStockDays() {
		if(!this.getCourierSelloutRisk()) return null;
		return ReplenishmentStatusHelper.getMinOutOfStockDays(this.courierEstimatedReceivingCompletionDate, this.lateSelloutDate);
	}

	@Override
	public String getAirFreightDate() {
		return DateHelper.toString(airFreightDate, dateFormat);
	}

	@Override
	public ReplenishmentStatus getAirFreightStatus() {		
		return ReplenishmentStatusHelper.buildStatus(this.airFreightDate);
	}

	@Override
	public Boolean getAirFreightSelloutRisk() {
		if(this.airFreightEstimatedReceivingCompletionDate != null){
			return this.airFreightEstimatedReceivingCompletionDate.after(this.earlySelloutDate);
		}

		return  false;

	}

	@Override
	public String getAirFreightMaxOutOfStockDays() {		
		if (!this.getAirFreightSelloutRisk()) return null;		
		return ReplenishmentStatusHelper.getMaxOutOfStockDays(this.airFreightEstimatedReceivingCompletionDate, this.earlySelloutDate);
	}

	@Override
	public String getAirFreightMinOutOfStockDays() {
		if (!this.getAirFreightSelloutRisk()) return null;		
		return ReplenishmentStatusHelper.getMinOutOfStockDays(this.airFreightEstimatedReceivingCompletionDate, this.lateSelloutDate);
	}

	@Override
	public String getSurfaceFreightDate() {		
		return DateHelper.toString(surfaceFreightDate, dateFormat);
	}

	@Override
	public ReplenishmentStatus getSurfaceFreightStatus() {
		return ReplenishmentStatusHelper.buildStatus(this.surfaceFreightDate);
	}

	@Override
	public Boolean getSurfaceFreightSelloutRisk() {
		if(this.surfaceFreightEstimatedReceivingCompletionDate == null) return false;
		return this.surfaceFreightEstimatedReceivingCompletionDate.after(this.earlySelloutDate);
	}

	@Override
	public String getSurfaceFreightMaxOutOfStockDays() {		
		if(!this.getSurfaceFreightSelloutRisk()) return null;
		return ReplenishmentStatusHelper.getMaxOutOfStockDays(this.surfaceFreightEstimatedReceivingCompletionDate, this.earlySelloutDate);
	}

	@Override
	public String getSurfaceFreightMinOutOfStockDays() {
		if(!this.getSurfaceFreightSelloutRisk()) return null;
		return ReplenishmentStatusHelper.getMinOutOfStockDays(this.surfaceFreightEstimatedReceivingCompletionDate, this.lateSelloutDate);
	}

}
