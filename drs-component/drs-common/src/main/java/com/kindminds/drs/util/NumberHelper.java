package com.kindminds.drs.util;

import java.text.DecimalFormat;

import org.springframework.util.Assert;

public class NumberHelper {
	
	public static String toGeneralCommaSeparatedString(Object value,int scale){
		if(scale==0) return new DecimalFormat("#,###").format(value);
		if(scale==1) return new DecimalFormat("#,##0.0").format(value);	
		if(scale==2) return new DecimalFormat("#,##0.00").format(value);
		Assert.isTrue(false);
		return null;
	}
	
}
