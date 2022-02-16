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
public class SurfaceFreight extends AbstractLogistics {

	@Override
	public Date calculateRecommendedFcaDeliveryDate(Date expectedStockDepletionDate, ReplenishmentTimeSpent timeSpent,
			Warehouse warehouse) {
		Calendar rsdd = Calendar.getInstance();
		int bufferPeriod = this.calculateBufferPeriod(expectedStockDepletionDate);
		if(this.calculateDiffDaysExpectedStockDepletionDateAndToday(expectedStockDepletionDate) > DaysAbout6Months) return null;		
		Date recommendedSurfaceFreightShippingSchedule = this.calculateRecommendedSurfaceFreightShippingSchedule(expectedStockDepletionDate, bufferPeriod,timeSpent, warehouse);
		if(recommendedSurfaceFreightShippingSchedule == null) return null;
		rsdd.setTime(recommendedSurfaceFreightShippingSchedule);
		Calendar recommendedSurfaceFreightFcaDeliveryDate = (Calendar)rsdd.clone();
		return this.calculateDateToSubtractWorkingDays(recommendedSurfaceFreightFcaDeliveryDate.getTime(), tallyBufferSurfaceFreight);		
	}
	
	@Override
	public Date calculateEstimatedReceivingCompletionDate(Date expectedStockDepletionDate,
			ReplenishmentTimeSpent timeSpent,Warehouse warehouse) {
		int bufferPeriod = this.calculateBufferPeriod(expectedStockDepletionDate);		
		Date recommendedSurfaceFreightShippingDate = this.calculateRecommendedSurfaceFreightShippingSchedule(expectedStockDepletionDate, bufferPeriod, timeSpent, warehouse);
		Calendar ercd = Calendar.getInstance();
		if(recommendedSurfaceFreightShippingDate == null) return null;
		ercd.setTime(recommendedSurfaceFreightShippingDate);
		Calendar sfc = (Calendar)ercd.clone();
		sfc.add(Calendar.DATE, timeSpent.getSurfaceFreightDays());
		sfc.add(Calendar.DATE, timeSpent.getReceivingDays());
		return sfc.getTime();
	}
	
	private Date calculateRecommendedSurfaceFreightShippingSchedule(Date expectedStockDepletionDate, int bufferPeriod, ReplenishmentTimeSpent timeSpent, Warehouse warehouse){
		Date dateToCompareIvsDeadline = this.calculateDateToSubtractWorkingDays(this.calculateSystemSurfaceFreightShippingDate(expectedStockDepletionDate, bufferPeriod, timeSpent), ivsLeadTimeSurfaceFreight);
		if(dateToCompareIvsDeadline.after(this.getToday())){
			Date recommendedSurfaceFreightShippingDate = this.shippingRouteDao.queryShippingDate(this.getTomorrow(), dateToCompareIvsDeadline, Country.TW, warehouse.getCountry(), ShippingMethod.SEA_FREIGHT);
			if(recommendedSurfaceFreightShippingDate != null) return recommendedSurfaceFreightShippingDate;			
			return this.shippingRouteDao.queryShippingDate(this.getToday(), Country.TW, warehouse.getCountry(), ShippingMethod.SEA_FREIGHT);
		}				
		return this.shippingRouteDao.queryShippingDate(this.getToday(), Country.TW, warehouse.getCountry(), ShippingMethod.SEA_FREIGHT);		
	}
	
	private Date calculateSystemSurfaceFreightShippingDate(Date expectedStockDepletionDate, int bufferPeriod, ReplenishmentTimeSpent timeSpent){
		Calendar esd = Calendar.getInstance();
		esd.setTime(expectedStockDepletionDate);
		Calendar sfc = (Calendar)esd.clone();
		sfc.add(Calendar.DATE, (timeSpent.getSurfaceFreightDays() + timeSpent.getReceivingDays())*-1);
		sfc.add(Calendar.DATE, bufferPeriod*-1);	
		return sfc.getTime();
	}
		
}