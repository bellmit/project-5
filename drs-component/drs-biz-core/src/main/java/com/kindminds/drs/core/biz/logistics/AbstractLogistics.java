package com.kindminds.drs.core.biz.logistics;

import java.util.Calendar;
import java.util.Date;

import com.kindminds.drs.api.v2.biz.domain.model.Logistics;
import org.springframework.beans.factory.annotation.Autowired;


import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShippingRouteDao;

public abstract class AbstractLogistics implements Logistics {

	@Autowired protected MaintainShippingRouteDao shippingRouteDao;
	
	private double sellUncertaintyPercentage = 0.25;
	protected int DaysAbout6Months = 183;
	protected int tallyBufferCourier = 2;
	protected int ivsLeadTimeAirFreight = 5;	
	protected int tallyBufferAirFreight = 5;
	protected int ivsLeadTimeSurfaceFreight = 7;
	protected int tallyBufferSurfaceFreight = 5;
				
	protected Date calculateDateToSubtractWorkingDays(Date date,int workingDays) {
		int limit = workingDays;		
		Calendar cal = Calendar.getInstance();		
		cal.setTime(date);
		Calendar dateToSubtractWorkingDays = (Calendar)cal.clone();		
		for(int i=0;i<limit;i++){
			dateToSubtractWorkingDays.add(Calendar.DAY_OF_MONTH, -1);			
			if (dateToSubtractWorkingDays.get(Calendar.DAY_OF_WEEK) == 1 || dateToSubtractWorkingDays.get(Calendar.DAY_OF_WEEK) == 7) limit++;									
		}
		return dateToSubtractWorkingDays.getTime();		
	}
	
	public int calculateBufferPeriod(Date expectedStockDepletionDate){
		int diffDays = this.calculateDiffDaysExpectedStockDepletionDateAndToday(expectedStockDepletionDate);
		return (int) Math.round(diffDays*sellUncertaintyPercentage);		
	} 
	
	public int calculateDiffDaysExpectedStockDepletionDateAndToday(Date expectedStockDepletionDate){
		Calendar today = Calendar.getInstance();
		today.setTime(this.getToday());		
		Calendar etdDate = Calendar.getInstance();
		etdDate.setTime(expectedStockDepletionDate);		
		long diffInSeconds = etdDate.getTimeInMillis() - today.getTimeInMillis();
		return (int) (diffInSeconds / (24 * 60 * 60 * 1000));		
	}
	
	public Date calculateEarlySelloutDate(Date expectedStockDepletionDate, int bufferPeriod) {
		Calendar esd = Calendar.getInstance();
		esd.setTime(expectedStockDepletionDate);
		Calendar sfc = (Calendar)esd.clone();
		sfc.add(Calendar.DATE, bufferPeriod*-1);	
		return sfc.getTime();
	}
	
	public Date calculateLateSelloutDate(Date expectedStockDepletionDate, int bufferPeriod) {
		Calendar esd = Calendar.getInstance();
		esd.setTime(expectedStockDepletionDate);
		Calendar sfc = (Calendar)esd.clone();
		sfc.add(Calendar.DATE, bufferPeriod);	
		return sfc.getTime();
	}
	
	protected Date getToday(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();		
	}
	
	protected Date getTomorrow(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();		
	}
	
}