package com.kindminds.drs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.util.StringUtils;

public class DateHelper {
	
	public static Date toDate(String dateStr,String formatStr){
		if (!StringUtils.hasText(dateStr)) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {date = sdf.parse(dateStr);}
		catch(ParseException e) { e.printStackTrace();}
		return date;
	}

	public static String toString(Date date,String format) {
		if (date == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String toString(Date date,String format,String timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(timezone));
		return sdf.format(date);
	}
	
	public static Date addDays(Date d,int days){
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    c.add(Calendar.DATE, days);
	    d.setTime( c.getTime().getTime() );
	    return d;
	}
	
	public static Date getDaysBefore(Date date, int days){
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE,-days);
		return c.getTime();
	}
	
	public static Date getDaysAfter(Date date, int days){
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE,days);
		return c.getTime();
	}

}
