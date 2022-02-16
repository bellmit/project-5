package com.kindminds.drs.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalHelper {
	
	public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
	
	public static String toPercentageString(BigDecimal value,int scale){
		return value.multiply(new BigDecimal("100")).setScale(scale,RoundingMode.HALF_UP).toPlainString()+"%";
	}
	
	public static BigDecimal generateBigDecimal(Locale locale,String value) throws ParseException{
		NumberFormat numberFormat = NumberFormat.getInstance(locale);
		return new BigDecimal(numberFormat.parse(value).toString());
	}
	
}
