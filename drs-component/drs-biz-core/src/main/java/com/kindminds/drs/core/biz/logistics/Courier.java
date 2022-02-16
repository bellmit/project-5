package com.kindminds.drs.core.biz.logistics;

import java.util.Calendar;
import java.util.Date;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.kindminds.drs.Warehouse;


@Service 
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Courier extends AbstractLogistics{

	@Override
	public Date calculateRecommendedFcaDeliveryDate(Date expectedStockDepletionDate, ReplenishmentTimeSpent timeSpent,
			Warehouse warehouse) {
		Calendar rcdd = Calendar.getInstance();
		int bufferPeriod = this.calculateBufferPeriod(expectedStockDepletionDate);
		if(this.calculateDiffDaysExpectedStockDepletionDateAndToday(expectedStockDepletionDate) > DaysAbout6Months) return null;		
		Date recommendedCourierShippingSchedule = this.calculateRecommendedCourierShippingSchedule(expectedStockDepletionDate, bufferPeriod, timeSpent);
		if(recommendedCourierShippingSchedule == null) return null;
		rcdd.setTime(recommendedCourierShippingSchedule);
		Calendar recommendedCourierFcaDeliveryDate = (Calendar)rcdd.clone();
		return this.calculateDateToSubtractWorkingDays(recommendedCourierFcaDeliveryDate.getTime(), tallyBufferCourier);	
	}

	@Override
	public Date calculateEstimatedReceivingCompletionDate(Date expectedStockDepletionDate,
			ReplenishmentTimeSpent timeSpent,Warehouse warehouse) {
		int bufferPeriod = this.calculateBufferPeriod(expectedStockDepletionDate);		
		Date recommendedCourierShippingDate = this.calculateRecommendedCourierShippingSchedule(expectedStockDepletionDate, bufferPeriod, timeSpent);
		Calendar ercd = Calendar.getInstance();
		if(recommendedCourierShippingDate == null) return null;
		ercd.setTime(recommendedCourierShippingDate);
		Calendar cc = (Calendar)ercd.clone();
		cc.add(Calendar.DATE, timeSpent.getCourierDays());
		cc.add(Calendar.DATE, timeSpent.getReceivingDays());		
		return cc.getTime();
	}
	
	private Date calculateRecommendedCourierShippingSchedule(Date expectedStockDepletionDate, int bufferPeriod, ReplenishmentTimeSpent timeSpent){
		Date systemCourierShippingDate = this.calculateSystemCourierShippingDate(expectedStockDepletionDate, bufferPeriod, timeSpent);
		Date dateToAddTallyBuffer = this.calculateDateToAddTallyBuffer(this.getToday(), tallyBufferCourier);
		if(dateToAddTallyBuffer.after(systemCourierShippingDate)) return dateToAddTallyBuffer;		
		return systemCourierShippingDate;
	}
	
	private Date calculateSystemCourierShippingDate(Date expectedStockDepletionDate, int bufferPeriod, ReplenishmentTimeSpent timeSpent){
		Calendar esd = Calendar.getInstance();
		esd.setTime(expectedStockDepletionDate);
		Calendar cc = (Calendar)esd.clone();
		cc.add(Calendar.DATE, (timeSpent.getCourierDays() + timeSpent.getReceivingDays())*-1);
		cc.add(Calendar.DATE, bufferPeriod*-1);		
		return cc.getTime();
	}

	private Date calculateDateToAddTallyBuffer(Date today, int tallyBuffer){
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		Calendar dateToAddTallyBuffer = (Calendar)cal.clone();
		dateToAddTallyBuffer.add(Calendar.DATE, tallyBuffer);
		return dateToAddTallyBuffer.getTime();		
	}
			
}