package com.kindminds.drs.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class IntegerHelper {
	
	public static Integer generateInteger(Locale locale,String value) throws ParseException{
		NumberFormat numberFormat = NumberFormat.getInstance(locale);
		return new Integer(numberFormat.parse(value).toString());
	}
	
}
