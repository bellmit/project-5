package com.kindminds.drs.v1.model.impl.replenishment;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.ReplenishmentStatus;

import java.util.Calendar;
import java.util.Date;

public class ReplenishmentStatusHelper {
	public static ReplenishmentStatus buildStatus(Date date) {
		if(date == null) return null;
		Calendar datecal = Calendar.getInstance();
		datecal.setTime(date);
		Calendar todayCal = getTodayCalendar();
		todayCal.add(Calendar.DATE, 7);
		if (todayCal.after(datecal))
			return ReplenishmentStatus.ExpiredIn7Days;
		todayCal.add(Calendar.DATE, 7);
		if (todayCal.after(datecal))
			return ReplenishmentStatus.ExpiredIn14Days;
		todayCal.add(Calendar.DATE, 7);
		if (todayCal.after(datecal))
			return ReplenishmentStatus.ExpiredIn21Days;		
		return ReplenishmentStatus.Normal;
	}
	
	public static String getMaxOutOfStockDays(Date estimatedReceivingCompletionDate,Date earlySelloutDate){		
		Calendar ercd = Calendar.getInstance();
		ercd.setTime(estimatedReceivingCompletionDate);		
		Calendar esd = Calendar.getInstance();
		esd.setTime(earlySelloutDate);		
		long diffInSeconds = ercd.getTimeInMillis() - esd.getTimeInMillis();
		int diffDays = (int) (diffInSeconds / (24 * 60 * 60 * 1000));		
		if (diffDays > 0 ) return Integer.toString(diffDays);	
		return Integer.toString(0);
	}
		
	public static String getMinOutOfStockDays(Date estimatedReceivingCompletionDate,Date lateSelloutDateSelloutDate){
		Calendar ercd = Calendar.getInstance();
		ercd.setTime(estimatedReceivingCompletionDate);		
		Calendar lsd = Calendar.getInstance();
		lsd.setTime(lateSelloutDateSelloutDate);
		long diffInSeconds = ercd.getTimeInMillis() - lsd.getTimeInMillis();
		int diffDays = (int) (diffInSeconds / (24 * 60 * 60 * 1000));		
		if (diffDays > 0 ) return Integer.toString(diffDays);	
		return Integer.toString(0);
	}
	
	private static Calendar getTodayCalendar() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		return today;
	}
}
