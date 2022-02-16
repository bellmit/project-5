package com.kindminds.drs.core.biz.logistics;

import java.util.Calendar;
import java.util.Date;


import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.kindminds.drs.Country;
import com.kindminds.drs.Warehouse;


@Service 
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AirFreight extends AbstractLogistics{

	@Override
	public Date calculateRecommendedFcaDeliveryDate(Date expectedStockDepletionDate, ReplenishmentTimeSpent timeSpent,
			Warehouse warehouse) {
		Calendar radd = Calendar.getInstance();
		int bufferPeriod = this.calculateBufferPeriod(expectedStockDepletionDate);
		if(this.calculateDiffDaysExpectedStockDepletionDateAndToday(expectedStockDepletionDate) > DaysAbout6Months) return null;		
		Date recommendedAirFreightShippingSchedule = this.calculateRecommendedAirFreightShippingSchedule(expectedStockDepletionDate, bufferPeriod, timeSpent, warehouse);
		if(recommendedAirFreightShippingSchedule == null) return null;
		radd.setTime(recommendedAirFreightShippingSchedule);
		Calendar recommendedAirFreightFcaDeliveryDate = (Calendar)radd.clone();
		return this.calculateDateToSubtractWorkingDays(recommendedAirFreightFcaDeliveryDate.getTime(), tallyBufferAirFreight);	
	}
		
	@Override
	public Date calculateEstimatedReceivingCompletionDate(Date expectedStockDepletionDate,
			ReplenishmentTimeSpent timeSpent,Warehouse warehouse) {
		int bufferPeriod = this.calculateBufferPeriod(expectedStockDepletionDate);		
		Date recommendedAirFreightShippingDate = this.calculateRecommendedAirFreightShippingSchedule(expectedStockDepletionDate, bufferPeriod, timeSpent, warehouse);
		Calendar ercd = Calendar.getInstance();
		if(recommendedAirFreightShippingDate == null) return null;
		ercd.setTime(recommendedAirFreightShippingDate);
		Calendar afc = (Calendar)ercd.clone();
		afc.add(Calendar.DATE, timeSpent.getAirFreightDays());
		afc.add(Calendar.DATE, timeSpent.getReceivingDays());
		return afc.getTime();
	}
	
	private Date calculateRecommendedAirFreightShippingSchedule(Date expectedStockDepletionDate, int bufferPeriod, ReplenishmentTimeSpent timeSpent, Warehouse warehouse){
		Date dateToCompareIvsDeadline = this.calculateDateToSubtractWorkingDays(this.calculateSystemAirFreightShippingDate(expectedStockDepletionDate, bufferPeriod, timeSpent), ivsLeadTimeAirFreight);
		if(dateToCompareIvsDeadline.after(this.getToday())){			
			Date recommendedAirFreightShippingDate = this.shippingRouteDao.queryShippingDate(this.getTomorrow(), dateToCompareIvsDeadline, Country.TW, warehouse.getCountry(), ShippingMethod.AIR_CARGO);			
			if(recommendedAirFreightShippingDate != null) return recommendedAirFreightShippingDate;			
			return this.shippingRouteDao.queryShippingDate(dateToCompareIvsDeadline, Country.TW, warehouse.getCountry(), ShippingMethod.AIR_CARGO);
		}						
		return this.shippingRouteDao.queryShippingDate(this.getToday(), Country.TW, warehouse.getCountry(), ShippingMethod.AIR_CARGO);
	}
	
	private Date calculateSystemAirFreightShippingDate(Date expectedStockDepletionDate, int bufferPeriod, ReplenishmentTimeSpent timeSpent){
		Calendar esd = Calendar.getInstance();
		esd.setTime(expectedStockDepletionDate);
		Calendar afc = (Calendar)esd.clone();
		afc.add(Calendar.DATE, (timeSpent.getAirFreightDays() + timeSpent.getReceivingDays())*-1);
		afc.add(Calendar.DATE, bufferPeriod*-1);		
		return afc.getTime();
	}
			
}